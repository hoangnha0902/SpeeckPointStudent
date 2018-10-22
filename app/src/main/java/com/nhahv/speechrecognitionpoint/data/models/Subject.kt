package com.nhahv.speechrecognitionpoint.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Subject(var subjectName: String = "",
                   var semester: SemesterType = SemesterType.SEMESTER_I,
                   var excel: FileExcel? = null) : Parcelable

@Parcelize
enum class SemesterType(var type: Int) : Parcelable {
    SEMESTER_I(0),
    SEMESTER_II(1);

    fun getSemester() = when (type) {
        0 -> "Học Kỳ I"
        1 -> "Học Kỳ II"
        else -> "Học Kỳ I"
    }

    override fun toString() = when (type) {
        0 -> "Học Kỳ I"
        1 -> "Học Kỳ II"
        else -> "Học Kỳ I"
    }

    fun getSemesterName() = when (type) {
        0 -> "semester_1"
        1 -> "semester_2"
        else -> "semester_1"
    }
}