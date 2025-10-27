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
