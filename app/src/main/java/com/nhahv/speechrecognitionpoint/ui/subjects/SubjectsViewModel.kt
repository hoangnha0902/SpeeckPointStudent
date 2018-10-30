package com.nhahv.speechrecognitionpoint.ui.subjects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.util.Constant
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_SUBJECT
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.get
import com.nhahv.speechrecognitionpoint.util.sharePrefs

class SubjectsViewModel(val fragment: SubjectsFragment) : ViewModel() {
    val subjects = MutableLiveData<ArrayList<Subject>>()
    val aClass = MutableLiveData<AClass>()


    init {
        aClass.value = getAClass()
        getSubjects()
    }

    private fun getAClass(): AClass? {
        return fragment.arguments?.getParcelable(Constant.CLASSES)
    }

    fun getSubjects() {
        val value = fragment.sharePrefs().get(PREF_SUBJECT.format(aClass.value?.name), "")
        if (value.isEmpty()) {
            subjects.value = ArrayList()
        }
        subjects.value = Gson().fromJson<ArrayList<Subject>>(value)
    }
}
