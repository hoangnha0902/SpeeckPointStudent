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

    fun remove(key: String) {
        sharedPref.edit().remove(key).apply()
    }

    companion object : SingletonHolder<SharedPrefs, Context>(::SharedPrefs) {
        val PREF_STUDENT = "pref_student_%s_%s_%s_%s"
        val PREF_CLASS = "pref_class_%s"
        val PREF_SUBJECT = "pref_subject_%s_%s"
        const val PREF_ACCOUNTS = "accounts"
        const val PREF_EXAM_LIST = "pref_exam_list"
        const val PREF_GROUP_EXAM_LIST = "pref_group_exam_list_%s"
        const val PREF_SUBJECT_EXAM_LIST = "pref_subject_exam_list_%s_%s"
        const val PREF_MARMOT_EXAM = "pref_marmot_exam_%s_%s_%s"
    }
}