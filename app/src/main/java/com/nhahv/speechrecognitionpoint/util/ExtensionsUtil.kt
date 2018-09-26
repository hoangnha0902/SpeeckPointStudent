package com.nhahv.speechrecognitionpoint.util

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText

fun Toast.showToast(context: Context, message: String) {
    makeText(context, message, Toast.LENGTH_SHORT).show()
}