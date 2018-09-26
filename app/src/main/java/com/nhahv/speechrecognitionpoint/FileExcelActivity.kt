package com.nhahv.speechrecognitionpoint

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.nhahv.speechrecognitionpoint.ui.fileexcel.FileExcelFragment

class FileExcelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_excel_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, FileExcelFragment.newInstance())
                    .commitNow()
        }
    }

}
