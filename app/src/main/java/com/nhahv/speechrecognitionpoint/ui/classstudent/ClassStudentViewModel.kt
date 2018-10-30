package com.nhahv.speechrecognitionpoint.ui.classstudent

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_CLASS
import com.nhahv.speechrecognitionpoint.util.fromJson

class ClassStudentViewModel(val context: Context) : ViewModel() {
    val aClasses = MutableLiveData<ArrayList<AClass>>()

    init {

        getClasses()
    }

    fun getClasses() {
        aClasses.value = ArrayList()
        val value = SharedPrefs.getInstance(context).get(PREF_CLASS, "")
        if (!value.isEmpty()) {
            aClasses.value = Gson().fromJson<ArrayList<AClass>>(value)
        }
    }
}
