package com.nhahv.speechrecognitionpoint.data.models

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AClass(
        var name: String = "",
        var number: Int = 0,
        var year: String = "",
        var excelFile: FileExcel? = null
) : Parcelable