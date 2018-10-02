package com.nhahv.speechrecognitionpoint

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment
import com.nhahv.speechrecognitionpoint.util.FileExcelManager
import com.nhahv.speechrecognitionpoint.util.PermissionUtil
import com.nhahv.speechrecognitionpoint.util.SharedPrefs

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val CODE_EXTERNAL_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val bundle = intent.extras
        bundle?.let {
            title = "Lớp học " + it.getString("className")
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        PermissionUtil.requestPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, CODE_EXTERNAL_STORAGE) { FileExcelManager.getExcelFile() }

        SharedPrefs.getInstance(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileExcelManager.getExcelFile()
                } else {
                    Toast.makeText(this, "Bạn phải cấp quyền truy cập bộ nhớ!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
