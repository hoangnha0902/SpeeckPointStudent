package com.nhahv.speechrecognitionpoint

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.nhahv.speechrecognitionpoint.ui.exportexcel.ExportExcelFragment
import com.nhahv.speechrecognitionpoint.util.FileExcelManager
import com.nhahv.speechrecognitionpoint.util.PermissionUtil
import com.nhahv.speechrecognitionpoint.util.SharedPrefs

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    val CODE_EXTERNAL_STORAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)


        SharedPrefs.getInstance(this)


        /*
         private var className: String? = null
    private var subjectName: String? = null
    private var semester: SemesterType? = SemesterType.SEMESTER_I

        * val bundle = intent.extras
        bundle?.let {
            className = it.getString(CLASS_NAME)
            subjectName   = it.getString(SUBJECT_NAME)
            semester = it.getSerializable(SEMESTER_PARAM) as SemesterType?
            title = "Môn $subjectName lớp học " + it.getString(CLASS_NAME)
        }
        */
    }

    fun back() {
        Navigation.findNavController(this, R.id.navLoginHost).popBackStack()
    }

    fun readExcel() {
        PermissionUtil.requestPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, CODE_EXTERNAL_STORAGE) { FileExcelManager.getExcelFile() }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navLoginHost).navigateUp()

    override fun onBackPressed() {
        if (Navigation.findNavController(this, R.id.navLoginHost).currentDestination?.id == R.id.exportExcelFragment) {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navLoginHost)
            val exportFragment: ExportExcelFragment? = navHostFragment?.childFragmentManager?.fragments?.get(0) as ExportExcelFragment
            exportFragment?.onBackPress()
        } else {
            Navigation.findNavController(this, R.id.navLoginHost).popBackStack()
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
        }
    }

}
