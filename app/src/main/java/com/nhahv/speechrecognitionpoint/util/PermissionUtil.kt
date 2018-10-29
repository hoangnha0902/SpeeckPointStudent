package com.nhahv.speechrecognitionpoint.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {

    private fun checkSelfPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(activity: Activity, permission: String, codePermission: Int, funcAfterRequest: () -> Unit) {
        if (!checkSelfPermission(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), codePermission)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(permission), codePermission)
            }
        } else {
            funcAfterRequest()
        }
    }
}