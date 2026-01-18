/**
Victor AI - Personal AI Companion for Android
Copyright (C) 2025-2026 Olga Kalinina

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.
 */

package com.example.victor_ai.di

import com.example.victor_ai.BuildConfig
import com.example.victor_ai.data.network.AlarmsApi
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.data.network.AssistantStateApi
import com.example.victor_ai.data.network.CareBankApi
import com.example.victor_ai.data.network.ChatApi
import com.example.victor_ai.data.network.MemoriesApi
import com.example.victor_ai.data.network.MusicApi
import com.example.victor_ai.data.network.MusicApiImpl
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.network.ReminderApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.ResponseBody
import okio.Buffer
import okio.ForwardingSource
import okio.Source
import okio.buffer
import java.util.UUID
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

/** Qualifier для streaming OkHttpClient (без read timeout для SSE) */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamingClient

/** Qualifier для streaming Retrofit instance */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamingRetrofit

/** Qualifier для streaming ApiService */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamingApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * BASE_URL из BuildConfig — единственный источник истины
     */
    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        val url = BuildConfig.BASE_URL
        if (BuildConfig.DEBUG) {
            android.util.Log.d("NetworkModule", "🌐 Providing baseUrl: $url")
        }
        return url
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            // Логируем только в DEBUG режиме
            if (BuildConfig.DEBUG) {
                if (message.length < 500) {
                    android.util.Log.d("OkHttpLog", message)
                } else {
                    android.util.Log.d("OkHttpLog", "BODY: ${message.length} chars")
                }
            }
        }.apply {
            // В release отключаем логирование полностью
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.HEADERS
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            if (BuildConfig.DEBUG) {
                android.util.Log.d("NetworkModule", "📝 HttpLoggingInterceptor level: ${level.name}")
            }
        }
    }

    /**
     * Стандартный OkHttpClient для обычных запросов
     * Поддержка HTTP/2 и оптимизированные таймауты
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        if (BuildConfig.DEBUG) {
            android.util.Log.d("NetworkModule", "🔧 Creating OkHttpClient with debugging enabled")
        }
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = if (BuildConfig.DEBUG) {
                    // Только в DEBUG добавляем trace header
                    val requestId = UUID.randomUUID().toString().takeLast(8)
                    chain.request().newBuilder()
                        .header("X-Client-Trace", requestId)
                        .build()
                } else {
                    chain.request()
                }
                
                val startTime = System.currentTimeMillis()
                try {
                    val response = chain.proceed(request)
                    
                    // Логируем только в DEBUG
                    if (BuildConfig.DEBUG) {
                        val requestId = request.header("X-Client-Trace") ?: "unknown"
                        val duration = System.currentTimeMillis() - startTime
                        val contentLength = response.header("Content-Length") ?: "chunked"
                        val transferEncoding = response.header("Transfer-Encoding") ?: "none"
                        android.util.Log.d(
                            "VictorHttp",
                            "⬅️ [$requestId] ${response.code} ${request.url} (${duration}ms) [len=$contentLength, transfer=$transferEncoding]"
                        )
                        
                        // Оборачиваем body для детальной диагностики только в DEBUG
                        val path = request.url.encodedPath
                        if (path.endsWith("/chat/get_history") || path.endsWith("/assistant/memories")) {
                            val body = response.body
                            if (body != null) {
                                val wrapped = CountingResponseBody(
                                    path = path,
                                    traceId = requestId,
                                    original = body
                                )
                                return@addInterceptor response.newBuilder()
                                    .body(wrapped)
                                    .build()
                            }
                        }
                    }
                    response
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        val requestId = request.header("X-Client-Trace") ?: "unknown"
                        val duration = System.currentTimeMillis() - startTime
                        android.util.Log.e(
                            "VictorHttp",
                            "❌ [$requestId] ${request.url} FAILED after ${duration}ms: ${e.javaClass.simpleName}: ${e.message}"
                        )
                    }
                    throw e
                }
            }
            .addInterceptor(loggingInterceptor)
            .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))  // Поддержка HTTP/2
            .connectionPool(ConnectionPool(5, 30, TimeUnit.SECONDS))
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .callTimeout(90, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * CountingResponseBody для детальной диагностики чтения response body.
     * Используется только в DEBUG режиме для отладки.
     * 
     * Важно: Создается как top-level функция без захвата внешнего контекста,
     * чтобы избежать утечек памяти через implicit reference на outer class.
     */
    private class CountingResponseBody(
        private val path: String,
        private val traceId: String,
        private val original: ResponseBody
    ) : ResponseBody() {
        
        override fun contentType(): MediaType? = original.contentType()
        override fun contentLength(): Long = original.contentLength()

        override fun source(): okio.BufferedSource {
            // Создаем counting source, который не держит ссылку на outer class
            return CountingSource(
                delegate = original.source(),
                path = path,
                traceId = traceId,
                expectedLength = contentLength()
            ).buffer()
        }
    }

    /**
     * Отдельный класс для подсчета байтов без утечек памяти.
     * Все данные передаются через конструктор, нет implicit references.
     */
    private class CountingSource(
        delegate: Source,
        private val path: String,
        private val traceId: String,
        private val expectedLength: Long
    ) : ForwardingSource(delegate) {
        
        private var totalBytesRead = 0L
        private var loggedStart = false

        override fun read(sink: Buffer, byteCount: Long): Long {
            // Логируем только в DEBUG режиме
            if (BuildConfig.DEBUG) {
                if (!loggedStart) {
                    loggedStart = true
                    android.util.Log.d(
                        "VictorBody",
                        "▶️ [$traceId] start read path=$path expected=$expectedLength"
                    )
                }
            }
            
            return try {
                val bytesRead = super.read(sink, byteCount)
                
                if (BuildConfig.DEBUG && bytesRead > 0) {
                    totalBytesRead += bytesRead
                    // Логируем каждые 16KB (реже, чем было)
                    if (totalBytesRead % 16384L == 0L) {
                        android.util.Log.d(
                            "VictorBody",
                            "⬇️ [$traceId] read=$totalBytesRead path=$path"
                        )
                    }
                }
                
                if (BuildConfig.DEBUG && bytesRead == -1L) {
                    android.util.Log.d(
                        "VictorBody",
                        "✅ [$traceId] complete path=$path total=$totalBytesRead"
                    )
                }
                
                bytesRead
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    android.util.Log.e(
                        "VictorBody",
                        "❌ [$traceId] read failed path=$path at=$totalBytesRead: ${e.javaClass.simpleName}: ${e.message}"
                    )
                }
                throw e
            }
        }
    }

    /**
     * Streaming OkHttpClient для SSE (Server-Sent Events)
     * Без read timeout, так как стрим может идти долго
     */
    @Provides
    @Singleton
    @StreamingClient
    fun provideStreamingOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        if (BuildConfig.DEBUG) {
            android.util.Log.d("NetworkModule", "🔧 Creating Streaming OkHttpClient")
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .protocols(listOf(Protocol.HTTP_1_1))  // SSE требует HTTP/1.1
            .connectionPool(ConnectionPool(2, 60, TimeUnit.SECONDS))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)      // Без таймаута для стриминга
            .writeTimeout(60, TimeUnit.SECONDS)
            .callTimeout(15, TimeUnit.MINUTES)     // 15 минут на весь стрим
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * Стандартный Retrofit для обычных запросов
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    /**
     * Streaming Retrofit для SSE запросов
     */
    @Provides
    @Singleton
    @StreamingRetrofit
    fun provideStreamingRetrofit(
        @StreamingClient okHttpClient: OkHttpClient,
        moshi: Moshi,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    /**
     * Streaming ApiService для SSE запросов (чат со стримингом)
     */
    @Provides
    @Singleton
    @StreamingApi
    fun provideStreamingApiService(@StreamingRetrofit retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReminderApi(retrofit: Retrofit): ReminderApi {
        return retrofit.create(ReminderApi::class.java)
    }

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicApi(retrofit: Retrofit): MusicApi {
        // Базовый Retrofit API (без streaming)
        return retrofit.create(MusicApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicApiImpl(
        musicApi: MusicApi,
        @StreamingClient streamingClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): MusicApiImpl {
        // Обёртка с поддержкой streaming
        return MusicApiImpl(
            retrofitApi = musicApi,
            baseUrl = baseUrl,
            streamingClient = streamingClient
        )
    }

    @Provides
    @Singleton
    fun provideAlarmsApi(retrofit: Retrofit): AlarmsApi {
        return retrofit.create(AlarmsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMemoriesApi(retrofit: Retrofit): MemoriesApi {
        return retrofit.create(MemoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCareBankApi(retrofit: Retrofit): CareBankApi {
        return retrofit.create(CareBankApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlacesApi(retrofit: Retrofit): PlacesApi {
        return retrofit.create(PlacesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAssistantStateApi(retrofit: Retrofit): AssistantStateApi {
        return retrofit.create(AssistantStateApi::class.java)
    }
}
