package com.nhahv.speechrecognitionpoint.util

object CommonUtils {
    fun round(value: Double, places: Int): Double {
        if (places < 0) {
            return 0.0
        }

        var valueDouble = recursiveAround10(value)
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        valueDouble *= factor
        val tmp = Math.round(valueDouble)
        return tmp.toDouble() / factor
    }


    fun recursiveAround10(value: Double): Double {
        if (value <= 10) {
            return value
        }
        return recursiveAround10(value / 10)
    }

    fun textPoint(label: String, point: String): String {
        return "<font color='#177FAC'>$label :  </font><font color='#2222FF'>$point &#160&#160&#160</font>"
    }
}