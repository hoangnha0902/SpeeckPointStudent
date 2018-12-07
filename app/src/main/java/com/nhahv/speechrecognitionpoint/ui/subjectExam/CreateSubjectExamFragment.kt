package com.nhahv.speechrecognitionpoint.ui.subjectExam


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson

import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.SubjectExam
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.fragment_create_subject_exam.*

class CreateSubjectExamFragment : DialogFragment() {

    var idExamObject: String? = null
    var idGroupExam: String? = null
    private var listener: ((SubjectExam) -> Unit)? = null

    companion object {
        fun newInstance() = CreateSubjectExamFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_create_subject_exam, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cancel.setOnClickListener { dismiss() }
        createSubjectExam.setOnClickListener {
            if (TextUtils.isEmpty(edtIdSubjectExam.text)) {
                toast("Mã môn thi không được để trống")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(edtNameSubjectExam.text)) {
                toast("Tên môn thi không được đẻ trống")
                return@setOnClickListener
            }

            getSubjectExamOfGroupExam().forEach { it ->
                if (it.idSubjectExam.trim().toLowerCase() == edtIdSubjectExam.text.toString().trim().toLowerCase()) {
                    toast("Mã môn thi đã tồn tại, hãy nhập mã môn thi khác")
                    return@setOnClickListener
                }
            }
            val subjectExam = SubjectExam(edtIdSubjectExam.text.toString().trim(), edtNameSubjectExam.text.toString().trim())
            listener?.invoke(subjectExam)
            dismiss()
        }
    }

    private fun getSubjectExamOfGroupExam(): ArrayList<SubjectExam> {
        val value = sharePrefs().get(prefSubjectExam(idExamObject, idGroupExam), "")
        if (TextUtils.isEmpty(value)) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<SubjectExam>>(value)
    }

    fun createSubjectExamCallback(l: ((SubjectExam) -> Unit)?) {
        listener = l
    }
}
