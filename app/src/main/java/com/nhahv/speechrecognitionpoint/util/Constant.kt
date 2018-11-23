package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.os.Environment
import com.nhahv.speechrecognitionpoint.data.models.SemesterType

object Constant {
    const val USER_NAME = "user_name"
    const val PASSWORD = "password"
    const val IS_LOGIN = "is_login"
    const val CLASSES = "classes"
    const val SUBJECTS = "subjects"
    val PATH_APP = Environment.getExternalStorageDirectory().absolutePath + "/SpeechPoint"

    fun NAME_CLASS_LIST(context: Context): String {
        val userName = SharedPrefs.getInstance(context).get(USER_NAME, "")
        return SharedPrefs.PREF_CLASS.format(userName)
    }

    fun NAME_SUBJECT_LIST(context: Context, className: String?): String {
        val userName = SharedPrefs.getInstance(context).get(USER_NAME, "")
        return SharedPrefs.PREF_SUBJECT.format(userName, className)
    }

    fun NAME_STUDENT_OF_SUBJECT(context: Context, className: String?, subjectName: String?, semester: SemesterType?): String {
        val userName = SharedPrefs.getInstance(context).get(USER_NAME, "")
        return SharedPrefs.PREF_STUDENT.format(userName, className, subjectName, semester?.getSemesterName())
    }
}