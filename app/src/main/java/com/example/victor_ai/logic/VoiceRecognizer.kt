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

package com.example.victor_ai.logic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceRecognizer(
    private val context: Context,
    private val onTextRecognized: (String) -> Unit,
    private val onListeningStateChanged: (Boolean) -> Unit
) {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU")
    }

    init {
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() { onListeningStateChanged(true) }
            override fun onEndOfSpeech() { onListeningStateChanged(false) }
            override fun onError(error: Int) { onListeningStateChanged(false) }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onRmsChanged(rmsdB: Float) {}

            override fun onResults(results: Bundle?) {
                val recognizedText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.getOrNull(0) ?: return

                onTextRecognized(recognizedText)
            }
        })
    }

    fun start() {
        recognizer.startListening(speechIntent)
    }

    fun destroy() {
        recognizer.destroy()
    }

    fun stopListening() {
        recognizer.stopListening()
    }
}
