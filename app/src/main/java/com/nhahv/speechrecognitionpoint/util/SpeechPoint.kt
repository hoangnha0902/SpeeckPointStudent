package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment

class SpeechPoint(context: Context) : RecognitionListener {
    private val TAG = SpeechPoint::class.java.simpleName
    private var speechIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    private var speech: SpeechRecognizer
    private var speechStarted: Boolean = false
    private val language : String = "vi"
    private var mainFragment :MainFragment ? = null

    init {
        speechIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, language)
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)

        speech = SpeechRecognizer.createSpeechRecognizer(context)
        speech.setRecognitionListener(this)
    }
    fun setMainFragment(fragment: MainFragment){
        mainFragment = fragment
    }

    fun startSpeech() {
        speech.startListening(speechIntent)
    }

    fun destroy() {
        speech.destroy()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d(TAG, "onReadyForSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Log.d(TAG, "onRmsChanged :$rmsdB")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Log.d(TAG, "onBufferReceived: $buffer")
    }

    override fun onPartialResults(bundle: Bundle?) {
        bundle?.let {
            val matches = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d(TAG, "onPartialResults: $matches")
        }

    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d(TAG, "onEvent")

    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech")
        speechStarted = true
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech")
        speechStarted = false
        startSpeech()

    }

    override fun onError(error: Int) {
        Log.d(TAG, "onError")
        if (!speechStarted) {
            startSpeech()
        }

    }

    override fun onResults(results: Bundle?) {
        Log.d(TAG, "onResults")
        results?.let {
            val matches = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.d(TAG, "onPartialResults: $matches")
        }
    }

}