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