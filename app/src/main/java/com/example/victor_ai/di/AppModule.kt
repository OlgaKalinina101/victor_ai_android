package com.example.victor_ai.di

import android.content.Context
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.notification.PushyTokenManager
import com.example.victor_ai.logic.SoundPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt модуль для предоставления зависимостей приложения
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSoundPlayer(
        @ApplicationContext context: Context
    ): SoundPlayer {
        return SoundPlayer(context)
    }

    @Provides
    @Singleton
    fun providePushyTokenManager(
        @ApplicationContext context: Context
    ): PushyTokenManager {
        return PushyTokenManager(context)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): LocationProvider {
        return LocationProvider(context)
    }
}
