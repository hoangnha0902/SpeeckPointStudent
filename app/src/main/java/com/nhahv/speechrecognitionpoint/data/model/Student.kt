package com.nhahv.speechrecognitionpoint.data.model

class Student(var stt: String = "0", var numberStudent: String = "", var name: String = "",
              var m1: String = "", var m2: String = "",
              var m3: String = "", var m4: String = "", var m5: String = "",
              var p1: String = "", var p2: String = "",
              var p3: String = "", var p4: String = "", var p5: String = "",
              var v1: String = "", var v2: String = "",
              var v3: String = "", var v4: String = "", var v5: String = "",
              var hk: String = "", var tbm: String = "") {

    fun getTBM(): String {
        if (hk.isEmpty()) {
            return ""
        }
        var radio = 0
        var total = 0.0
        if (m1.isNotEmpty()) {
            total += m1.toDouble()
            radio += 1
        }
        if (m2.isNotEmpty()) {
            total += m2.toDouble()
            radio += 1
        }
        if (m3.isNotEmpty()) {
            total += m3.toDouble()
            radio += 1
        }
        if (m4.isNotEmpty()) {
            total += m4.toDouble()
            radio += 1
        }
        if (m5.isNotEmpty()) {
            total += m5.toDouble()
            radio += 1
        }
        if (p1.isNotEmpty()) {
            total += p1.toDouble()
            radio += 1
        }
        if (p2.isNotEmpty()) {
            total += p2.toDouble()
            radio += 1
        }
        if (p3.isNotEmpty()) {
            total += p3.toDouble()
            radio += 1
        }
        if (p4.isNotEmpty()) {
            total += p4.toDouble()
            radio += 1
        }
        if (p5.isNotEmpty()) {
            total += p5.toDouble()
            radio += 1
        }
        if (v1.isNotEmpty()) {
            total += v1.toDouble() * 2
            radio += 2
        }
        if (v2.isNotEmpty()) {
            total += v2.toDouble() * 2
            radio += 2
        }
        if (v3.isNotEmpty()) {
            total += v3.toDouble() * 2
            radio += 2
        }
        if (v4.isNotEmpty()) {
            total += v4.toDouble() * 2
            radio += 2
        }
        if (v5.isNotEmpty()) {
            total += v5.toDouble() * 2
            radio += 2
        }
        if (hk.isNotEmpty()) {
            total += hk.toDouble() * 3
            radio += 3
        }
        return (round(total / radio, 1)).toString()

    }

    fun round(value: Double, places: Int): Double {
        var value = value
        if (places < 0) {
            return 0.0
        }

        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value *= factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }
}