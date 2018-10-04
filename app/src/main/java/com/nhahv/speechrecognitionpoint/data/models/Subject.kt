package com.nhahv.speechrecognitionpoint.data.models

data class Subject(var subjectName: String = "", var semester: SemesterType = SemesterType.SEMESTER_I)

enum class SemesterType(var type: Int) {
    SEMESTER_I(0),
    SEMESTER_II(1);

    fun getSemester(): String {
        return when (type) {
            0 -> "Học Kỳ I"
            1 -> "Học Kỳ II"
            else -> "Học Kỳ I"
        }
    }

    override fun toString(): String {
        return when (type) {
            0 -> "Học Kỳ I"
            1 -> "Học Kỳ II"
            else -> "Học Kỳ I"
        }
    }
}