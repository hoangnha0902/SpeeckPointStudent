package com.nhahv.speechrecognitionpoint.ui.subjectExam


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

import com.nhahv.speechrecognitionpoint.R

class CreateSubjectExamFragment : DialogFragment() {

    companion object {
        fun newInstance() = CreateSubjectExamFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_create_subject_exam, container, false)
    }


}
