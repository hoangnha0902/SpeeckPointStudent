package com.nhahv.speechrecognitionpoint.util

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.ViewModelFactory

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

fun androidx.fragment.app.Fragment.toast(message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

fun androidx.fragment.app.Fragment.setUpToolbar(toolbar: Toolbar, title: String) {
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

fun Fragment.navigateClearStack(view: View, action: Int) {
    view.findNavController().navigate(action, null, NavOptions.Builder().setClearTask(true).build())

}

fun Fragment.navigateClearStack(action: Int) {
    Navigation.findNavController(requireActivity(), R.id.navLoginHost).navigate(action, null, NavOptions.Builder().setClearTask(true).build())
}

fun Fragment.navigate(view: View, action: Int) {
    view.findNavController().navigate(action)
}

fun Fragment.navigate(action: Int, bundle: Bundle) {
    Navigation.findNavController(requireActivity(), R.id.navLoginHost).navigate(action, bundle)
}

inline fun <reified T> Fragment.putPref(key: String, value: T) {
    sharePrefs().put(key, value)
}

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>, key: String?) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(requireContext(), key)).get(viewModelClass)


fun Fragment.prefGroupExam(idExam: String?): String {
    return SharedPrefs.PREF_GROUP_EXAM_LIST.format(idExam)
}

fun Fragment.prefSubjectExam(idExam: String?, idGroupExam: String?): String {
    return SharedPrefs.PREF_SUBJECT_EXAM_LIST.format(idExam, idGroupExam)
}

fun Fragment.prefMarmotName(idExam: String?, idGroupExam: String?, idSubjectExam: String?): String {
    return SharedPrefs.PREF_MARMOT_EXAM.format(idExam, idGroupExam, idSubjectExam)
}

