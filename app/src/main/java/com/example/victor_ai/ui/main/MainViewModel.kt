package com.example.victor_ai.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.victor_ai.data.network.ApiService
import com.example.victor_ai.ui.playlist.PlaylistViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var playlistViewModel: PlaylistViewModel? = null

    fun setPlaylistViewModel(vm: PlaylistViewModel) {
        playlistViewModel = vm
    }


    suspend fun playTrack(trackId: Int) {
        withContext(Dispatchers.Main) {
            Log.d("Music", "🎵 Делегирую воспроизведение трека: $trackId")
            playlistViewModel?.playTrack(trackId)
                ?: Log.w("MainViewModel", "⚠️ PlaylistViewModel не установлен, невозможно воспроизвести трек $trackId")
        }
    }
}

class PlaylistViewModelFactory(
    private val apiService: ApiService,
    private val accountId: String,
    private val cacheDir: File,
    private val application: Application  // 🔥 Добавлен context для Wake Lock
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(apiService, accountId, cacheDir, application.applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}