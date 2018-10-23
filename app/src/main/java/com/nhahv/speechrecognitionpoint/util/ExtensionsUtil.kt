package com.nhahv.speechrecognitionpoint.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.ui.exportexcel.ExportExcelFragment

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
        else -> {
            getString(key, defaultValue as String) as T
        }
    }
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Activity.start() {
    startActivity(Intent(this, T::class.java))
}

inline fun <reified T> Activity.start(bundle: Bundle) {
    startActivity(Intent(this, T::class.java).apply {
        putExtras(bundle)
    })
}

inline fun <reified T> Fragment.start(bundle: Bundle) {
    startActivity(Intent(activity, T::class.java).apply {
        putExtras(bundle)
    })
}


inline fun <reified T> Fragment.start() {
    startActivity(Intent(activity, T::class.java))
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

fun Fragment.setUpToolbar(toolbar: Toolbar, title: String) {
    (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    (requireActivity() as AppCompatActivity).supportActionBar?.title = title

}

fun Fragment.convertCompare(text: String): String {
    return text.trim().replace(" ", "").toUpperCase()
}


fun Fragment.sharePrefs(): SharedPreferences {
    return SharedPrefs.getInstance(requireContext()).sharedPref
}

inline fun <reified T> AppCompatActivity.currentFragment(): T? {
    return supportFragmentManager.findFragmentById(R.id.navLoginHost)?.childFragmentManager?.fragments?.get(0) as T?
}

fun AppCompatActivity.currentFragmentId(): Int? {
    return Navigation.findNavController(this, R.id.navLoginHost).currentDestination?.id
}