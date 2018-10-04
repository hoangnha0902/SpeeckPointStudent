package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs private constructor(context: Context) {
    private val PREFS_NAME = "share_prefs"
    val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    inline fun <reified T> put(key: String, value: T) {
        sharedPref.put(key, value)
    }

    inline fun <reified T> get(key: String, value: T): T {
        return sharedPref.get(key, value)
    }

    companion object : SingletonHolder<SharedPrefs, Context>(::SharedPrefs) {
        val PREF_STUDENT = "pref_student"
        val PREF_CLASS = "pref_class"
        val PREF_SUBJECT = "pref_subject_%s"
    }
}