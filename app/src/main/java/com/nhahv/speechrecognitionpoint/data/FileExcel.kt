package com.nhahv.speechrecognitionpoint.data

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nhahv on 12/9/2016.
 * <>>
 */
class FileExcel(var path: String?, var parent: String?, time: Long) {
    var time: String? = null
    /*/storage/emulated/0/LopHoc.xls*/ var nameFile: String? = null
        private set(path) {
            path?.let {
                val indexPlash = it.lastIndexOf("/")
                field = it.substring(indexPlash.plus(1))
                Log.d("fileExcel = ", nameFile)
                val indexDots = nameFile!!.indexOf(".")
                field = nameFile!!.substring(0, indexDots)
                Log.d("fileExcel = ", nameFile)
            }
        }

    init {
        nameFile = path
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        this.time = format.format(time)
    }
}
