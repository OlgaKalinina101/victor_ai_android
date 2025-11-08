package com.example.victor_ai

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(), DefaultLifecycleObserver {

    companion object {
        @Volatile var isForeground: Boolean = false
    }

    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: androidx.lifecycle.LifecycleOwner) { isForeground = true }
    override fun onStop(owner: androidx.lifecycle.LifecycleOwner) { isForeground = false }
}