package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.os.Environment
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.data.models.SemesterType
import com.nhahv.speechrecognitionpoint.data.models.Subject

object Constant {
    const val USER_NAME = "user_name"
    const val PASSWORD = "password"
    const val IS_LOGIN = "is_login"
    const val CLASSES = "classes"
    const val SUBJECTS = "subjects"
    const val BUNLE_EXAM_OBJECT = "exam_object"
    const val BUNDLE_ID_EXAM = "id_exam_object"
    const val BUNDLE_ID_GROUP_EXAM = "id_group_exam"
    const val BUNDLE_ID_SUBJECT_EXAM = "id_subject_exam"
    const val BUNDLE_NAME_SUBJECT_EXAM = "name_subject_exam"
    const val BUNDLE_IS_MAIN_EXAM = "is_main_exam"


    val PATH_APP = Environment.getExternalStorageDirectory().absolutePath + "/SpeechPoint"
    val arrayYear = arrayListOf(
            "2014 - 2015", "2015 - 2016", "2016 - 2017", "2017 - 2018", "2018 - 2019",
            "2019 - 2020", "2020 - 2021", "2021 - 2022", "2023 - 2024"
    )

    val arrayYearGroupExam = arrayListOf(
            "2014", "2015", "2016", "2017", "2018",
            "2019", "2020", "2021", "2023"
    )

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

    fun nameFile(subjectName: String?, semester: SemesterType, aClass: AClass?): String {
        return "${subjectName}_${aClass?.name}_${semester.getSemesterName()}_${aClass?.year}.xls"
    }

    fun marmotExamPointNameFile(idExamObject: String?, idGroupExam: String?, idSubjectExam: String?, nameSubjectExam: String?): String {
        val nameFile = "MonThi_${nameSubjectExam}_MaKyThi_${idExamObject}_MaNhomThi_${idGroupExam}_MaMonThi_$idSubjectExam"
        return ReadWriteExcelFile.pathFile("$nameFile.xls")
    }
}