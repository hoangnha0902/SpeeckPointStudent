package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import android.widget.Toast.makeText
import com.google.gson.Gson

fun Toast.showToast(context: Context, message: String) {
    makeText(context, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    val editor = edit()
    when (T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> editor.putString(key, Gson().toJson(value))
    }
    editor.apply()
}

inline fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    return when (T::class) {
        Boolean::class -> getBoolean(key, defaultValue as Boolean) as T
        Int::class -> getInt(key, defaultValue as Int) as T
        Float::class -> getFloat(key, defaultValue as Float) as T
        Long::class -> getLong(key, defaultValue as Long) as T
        String::class -> getString(key, defaultValue as String) as T
        else -> Gson().fromJson(getString(key, ""), T::class.java) as T
    }
}

