package com.nhahv.speechrecognitionpoint.util

import android.os.Environment

object Constant {
    const val CLASS_NAME = "className"
    const val SUBJECT_NAME = "subjectName"
    const val SEMESTER_PARAM = "semester"
    const val USER_NAME = "user_name"
    const val PASSWORD = "password"
    const val IS_LOGIN = "is_login"
    const val CLASSES = "classes"
    const val SUBJECTS = "subjects"
    val PATH_APP = Environment.getExternalStorageDirectory().absolutePath + "/SpeechPoint"
}