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

import android.content.Context
import com.example.victor_ai.data.location.LocationProvider
import com.example.victor_ai.data.network.PlacesApi
import com.example.victor_ai.data.repository.LocationHistoryRepository
import com.example.victor_ai.data.repository.StatsRepository
import com.example.victor_ai.data.repository.VisitedPlacesRepository
import com.example.victor_ai.data.settings.SoundPlayerSettingsRepository
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
        @ApplicationContext context: Context,
        settingsRepository: SoundPlayerSettingsRepository,
    ): SoundPlayer {
        return SoundPlayer(context, settingsRepository)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context,
        locationHistoryRepository: LocationHistoryRepository
    ): LocationProvider {
        return LocationProvider(context, locationHistoryRepository)
    }

    @Provides
    @Singleton
    fun provideStatsRepository(
        @ApplicationContext context: Context,
        placesApi: PlacesApi
    ): StatsRepository {
        return StatsRepository(context, placesApi)
    }

    @Provides
    @Singleton
    @javax.inject.Named("cacheDir")
    fun provideCacheDir(
        @ApplicationContext context: Context
    ): java.io.File {
        return context.cacheDir
    }

    @Provides
    @Singleton
    fun provideVisitedPlacesRepository(
        @ApplicationContext context: Context
    ): VisitedPlacesRepository {
        return VisitedPlacesRepository(context)
    }
}
