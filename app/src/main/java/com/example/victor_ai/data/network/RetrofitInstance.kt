package com.example.victor_ai.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    const val BASE_URL = "https://pentavalent-conrad-unbreathed.ngrok-free.dev/"

    // ✅ 1. Логирование запросов и ответов
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ✅ 2. Клиент OkHttp с логами
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS) // ⏱ до 30 сек на подключение
        .readTimeout(60, TimeUnit.SECONDS)    // ⏱ до 60 сек на ответ от сервера
        .writeTimeout(60, TimeUnit.SECONDS)   // ⏱ до 60 сек на отправку данных
        .build()


    // ✅ 3. Создаём Moshi-инстанс
    // Кодогенерированные адаптеры (для @JsonClass) имеют приоритет автоматически
    // KotlinJsonAdapterFactory служит fallback для классов без @JsonClass
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())  // Fallback для классов без @JsonClass
        .build()

    // ✅ 4. Retrofit + Moshi
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // <-- теперь moshi есть
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val reminderApi: ReminderApi by lazy {
        retrofit.create(ReminderApi::class.java)
    }

    val assistantApi: AssistantStateApi by lazy {
        retrofit.create(AssistantStateApi::class.java)
    }

    val placesApi: PlacesApi = retrofit.create(PlacesApi::class.java)
}
