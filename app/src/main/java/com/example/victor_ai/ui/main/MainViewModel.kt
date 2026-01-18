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

package com.example.victor_ai.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.victor_ai.domain.playback.PlaybackController
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * MainViewModel теперь реализует PlaybackController для развязки с ChatViewModel
 */
class MainViewModel(application: Application) : AndroidViewModel(application), PlaybackController {

    private var playlistViewModel: PlaylistViewModel? = null

    fun setPlaylistViewModel(vm: PlaylistViewModel) {
        playlistViewModel = vm
    }

    /**
     * Реализация PlaybackController.playTrack()
     * Делегирует воспроизведение в PlaylistViewModel
     */
    override suspend fun playTrack(trackId: Int) {
        withContext(Dispatchers.Main) {
            Log.d("Music", "🎵 [PlaybackController] Делегирую воспроизведение трека: $trackId")
            playlistViewModel?.playTrack(trackId)
                ?: Log.w("MainViewModel", "⚠️ PlaylistViewModel не установлен, невозможно воспроизвести трек $trackId")
        }
    }
}