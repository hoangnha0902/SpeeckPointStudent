package com.nhahv.speechrecognitionpoint

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nhahv.speechrecognitionpoint.ui.exportexcel.ExportExcelFragment
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment
import com.nhahv.speechrecognitionpoint.util.FileExcelManager
import com.nhahv.speechrecognitionpoint.util.PermissionUtil
import com.nhahv.speechrecognitionpoint.util.SharedPrefs

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    val CODE_EXTERNAL_STORAGE = 100
    val CODE_EXTERNAL_RECORD_AUDIO = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        SharedPrefs.getInstance(this)
    }

    fun back() {
        Navigation.findNavController(this, R.id.navLoginHost).popBackStack()
    }

    fun readExcel(function: () -> Unit) {
        PermissionUtil.requestPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, CODE_EXTERNAL_STORAGE) { function }
    }

    fun startSpeech(function: () -> Unit) {
        PermissionUtil.requestPermission(this,
                Manifest.permission.RECORD_AUDIO, CODE_EXTERNAL_STORAGE) { function() }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navLoginHost).navigateUp()

    override fun onBackPressed() {
        val resId = Navigation.findNavController(this, R.id.navLoginHost).currentDestination?.id
        when (resId) {
            R.id.exportExcelFragment -> {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.navLoginHost)
                val exportFragment: ExportExcelFragment? = navHostFragment?.childFragmentManager?.fragments?.get(0) as ExportExcelFragment
                exportFragment?.onBackPress()
            }
            R.id.classStudentFragment -> finish()
            else -> Navigation.findNavController(this, R.id.navLoginHost).popBackStack()
        }
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
            CODE_EXTERNAL_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val resId = Navigation.findNavController(this, R.id.navLoginHost).currentDestination?.id
                    when (resId) {
                        R.id.mainFragment -> {
                            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navLoginHost)
                            val exportFragment: MainFragment? = navHostFragment?.childFragmentManager?.fragments?.get(0) as MainFragment
                            exportFragment?.startSpeech()
                        }
                    }
                } else {
                    Toast.makeText(this, "Bạn phải cấp quyền ghi am thanh!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
