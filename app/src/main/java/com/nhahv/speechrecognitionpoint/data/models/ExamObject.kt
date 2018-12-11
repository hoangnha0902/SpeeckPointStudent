package com.nhahv.speechrecognitionpoint.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExamObject(var idExam: String = "",
                      var nameExam: String = "",
                      var idSchoolYear: String = "",
                      var nameSchoolYear: String = "") : Parcelable

@Parcelize
data class GroupExam(var idGroupExam: String = "",
                     var nameGroupExam: String = "",
                     val idExam: String = "",
                     var nameYearGroupExam: String = "") : Parcelable


@Parcelize
data class SubjectExam(var idSubjectExam: String = "",
                       var nameSubjectExam: String = "",
                       val stt: String = "",
                       var nameFile: String? = null) : Parcelable


@Parcelize
class MarmotExam(var idMarmot: String = "",
                 var numberStudent: String = "0"
) : Parcelable {
    override fun toString(): String = idMarmot
}

@Parcelize
data class MarmotExamItem(var idMarmot: String = "",
                          var pointOfMarmot: String = "") : Parcelable

@Parcelize
data class MarmotExamPointItem(var marmotExams: ArrayList<MarmotExam> = ArrayList(),
                               var marmotExamItems: ArrayList<MarmotExamItem> = ArrayList()) : Parcelable