package com.nhahv.speechrecognitionpoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.nhahv.speechrecognitionpoint.ui.exportexcel.ExportExcelFragment

class ExportExcelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.export_excel_activity)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        title = "Chọn đường dẫn Export bảng điểm"
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ExportExcelFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onBackPressed() {
        val fragment : ExportExcelFragment? = supportFragmentManager.findFragmentById(R.id.container) as ExportExcelFragment
        fragment?.onBackPress()
    }

}
