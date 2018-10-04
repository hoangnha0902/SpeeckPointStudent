package com.nhahv.speechrecognitionpoint.data.models

enum class TypePoint {
    MOUTH,
    P15,
    WRITE,
    SEMESTER;

    override fun toString(): String {

        return when (ordinal) {
            0 -> "Miệng"
            1 -> "Viết/15P"
            2 -> "Viết/45P"
            3 -> "Học kỳ"
            else -> "Miệng"
        }
    }
}

enum class TypeOfTypePoint {
    TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5;

    override fun toString(): String {
        return when (ordinal) {
            0 -> "Điểm số 1"
            1 -> "Điểm số 2"
            2 -> "Điểm số 3"
            3 -> "Điểm số 4"
            4 -> "Điểm số 5"
            else -> "Điểm số 1"
        }
    }
}