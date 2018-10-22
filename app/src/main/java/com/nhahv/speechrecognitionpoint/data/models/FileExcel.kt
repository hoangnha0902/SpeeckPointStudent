package com.nhahv.speechrecognitionpoint.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Nhahv on 12/9/2016.
 * <>>
 */
@Parcelize
class FileExcel(var path: String?, var parent: String?, var timeLOng: Long) : Parcelable {
    var time: String? = null
    var nameFile: String? = null
        private set(path) {
            path?.let {
                val indexPlash = it.lastIndexOf("/")
                field = it.substring(indexPlash.plus(1))
                val indexDots = nameFile!!.indexOf(".")
                field = nameFile!!.substring(0, indexDots)
            }
        }

    init {
        nameFile = path
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        this.time = format.format(timeLOng)
    }
}
