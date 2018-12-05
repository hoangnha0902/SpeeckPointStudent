package com.nhahv.speechrecognitionpoint

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.nhahv.speechrecognitionpoint.ui.exportexcel.ExportExcelFragment
import com.nhahv.speechrecognitionpoint.ui.excel.ExcelFragment
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment
import com.nhahv.speechrecognitionpoint.util.PermissionUtil
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.currentFragment
import com.nhahv.speechrecognitionpoint.util.currentFragmentId

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val CODE_EXTERNAL_STORAGE = 100
    private val CODE_EXTERNAL_RECORD_AUDIO = 200
    private var progress: ProgressDialog? = null

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
                Manifest.permission.WRITE_EXTERNAL_STORAGE, CODE_EXTERNAL_STORAGE) { function() }
    }

    fun startSpeech(function: () -> Unit) {
        PermissionUtil.requestPermission(this,
                Manifest.permission.RECORD_AUDIO, CODE_EXTERNAL_STORAGE) { function() }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navLoginHost).navigateUp()

    override fun onBackPressed() {
        val resId = currentFragmentId()
        when (resId) {
            R.id.exportExcelFragment -> {
                val exportFragment = currentFragment<ExportExcelFragment>()
                exportFragment?.onBackPress()
            }
            R.id.managerFragment -> finish()
            R.id.loginFragment -> finish()
            else -> Navigation.findNavController(this, R.id.navLoginHost).popBackStack()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val resId = currentFragmentId()
        when (requestCode) {
            CODE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (resId) {
                        R.id.fileExcelFragment -> {
                            val excelFragment = currentFragment<ExcelFragment>()
                            excelFragment?.getExcelList()
                        }
                    }
                } else {
                    Toast.makeText(this, "Bạn phải cấp quyền truy cập bộ nhớ!", Toast.LENGTH_SHORT).show()
                }
            }
            CODE_EXTERNAL_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (resId) {
                        R.id.mainFragment -> {
                            val exportFragment = currentFragment<MainFragment>()
                            exportFragment?.startSpeech()
                        }
                    }
                } else {
                    Toast.makeText(this, "Bạn phải cấp quyền ghi am thanh!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun showProgress() {
        if (progress == null) {
            progress = ProgressDialog(this)
            progress?.setMessage("Đang xử lý")
            progress?.setCancelable(false)
        }
        progress?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    fun hideProgress() {
        progress?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}
