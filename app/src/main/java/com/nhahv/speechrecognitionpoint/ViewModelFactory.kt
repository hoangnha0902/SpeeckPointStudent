package com.nhahv.speechrecognitionpoint

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nhahv.speechrecognitionpoint.ui.classstudent.ClassStudentViewModel
import com.nhahv.speechrecognitionpoint.ui.excel.ExcelViewModel
import com.nhahv.speechrecognitionpoint.ui.subjects.SubjectsViewModel

class ViewModelFactory private constructor(
        private val context: Context,
        private val key: String?
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SubjectsViewModel::class.java) -> SubjectsViewModel(context, key)
                    isAssignableFrom(ClassStudentViewModel::class.java) -> ClassStudentViewModel(context)
                    isAssignableFrom(ExcelViewModel::class.java) -> ExcelViewModel(context, key)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context, key: String?) = ViewModelFactory(context, key)

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}