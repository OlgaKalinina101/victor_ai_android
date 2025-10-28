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

    private lateinit var playlistViewModel: PlaylistViewModel  // ← добавь

    fun setPlaylistViewModel(vm: PlaylistViewModel) {  // ← добавь этот метод
        playlistViewModel = vm
    }


    suspend fun playTrack(trackId: Int) {
        withContext(Dispatchers.Main) {
            Log.d("Music", "🎵 Делегирую воспроизведение трека: $trackId")
            playlistViewModel.playTrack(trackId)
        }
    }
}

class PlaylistViewModelFactory(
    private val apiService: ApiService,
    private val accountId: String,
    private val cacheDir: File
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlaylistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlaylistViewModel(apiService, accountId, cacheDir) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}