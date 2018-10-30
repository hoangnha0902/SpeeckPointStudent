package com.nhahv.speechrecognitionpoint

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nhahv.speechrecognitionpoint.ui.subjects.SubjectsFragment
import com.nhahv.speechrecognitionpoint.ui.subjects.SubjectsViewModel

class ViewModelFactory private constructor(
        private val fragment: Fragment
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
            with(modelClass) {
                when {
                    isAssignableFrom(SubjectsViewModel::class.java) ->
                        SubjectsViewModel(fragment as SubjectsFragment)

                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(fragment: Fragment) = INSTANCE
                ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(fragment).also { INSTANCE = it }
                }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}