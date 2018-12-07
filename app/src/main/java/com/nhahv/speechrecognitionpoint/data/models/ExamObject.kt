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
                       var nameFile: String? = null,
                       var pathFile: String? = null) : Parcelable


@Parcelize
data class MarmotExam(var idMarmot: String = "",
                      var numberStudent: String = "0",
                      var pointOfMarmot: ArrayList<MarmotExamItem> = ArrayList()
) : Parcelable

@Parcelize
data class MarmotExamItem(var idMarmot: String = "",
                          var pointOfMarmot: String = "") : Parcelable
