package com.nhahv.speechrecognitionpoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nhahv.speechrecognitionpoint.ui.subjects.SubjectsFragment

class SubjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            title = "Danh sách môn học lớp " + it.getString("className")

        }
        setContentView(R.layout.subjects_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SubjectsFragment.newInstance())
                    .commitNow()
        }
    }

}
