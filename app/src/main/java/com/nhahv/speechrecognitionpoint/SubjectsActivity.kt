package com.nhahv.speechrecognitionpoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nhahv.speechrecognitionpoint.ui.subjects.SubjectsFragment

class SubjectsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subjects_activity)
        var className: String? = null
        intent.extras?.let {
            className = it.getString("className")
            title = "Danh sách môn học lớp $className"
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SubjectsFragment.newInstance(className))
                    .commitNow()
        }
    }

}
