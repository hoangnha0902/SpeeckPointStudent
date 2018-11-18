package com.nhahv.speechrecognitionpoint.ui.subjects

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_SUBJECT
import com.nhahv.speechrecognitionpoint.util.fromJson

class SubjectsViewModel(val context: Context, private val key: String?) : ViewModel() {
    val subjects = MutableLiveData<ArrayList<Subject>>()

    init {
        getSubjects()
    }


    fun getSubjects() {
        subjects.value = ArrayList()
        val value = SharedPrefs.getInstance(context).get(PREF_SUBJECT.format(key), "")
        if (!value.isEmpty()) {
            subjects.value = Gson().fromJson<ArrayList<Subject>>(value)
        }
    }

    fun getSubjectList(): ArrayList<Subject> {
        val subjects = ArrayList<Subject>()
        val value = SharedPrefs.getInstance(context).get(PREF_SUBJECT.format(key), "")
        if (!value.isEmpty()) {
            subjects.addAll(Gson().fromJson<ArrayList<Subject>>(value))
        }
        return subjects
    }
}
