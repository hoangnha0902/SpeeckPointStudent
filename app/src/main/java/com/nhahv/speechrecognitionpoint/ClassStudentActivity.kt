package com.nhahv.speechrecognitionpoint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nhahv.speechrecognitionpoint.ui.classstudent.ClassStudentFragment

class ClassStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.class_student_activity)
        title = "Danh sách lớp học "
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ClassStudentFragment.newInstance())
                    .commitNow()
        }
    }

}
