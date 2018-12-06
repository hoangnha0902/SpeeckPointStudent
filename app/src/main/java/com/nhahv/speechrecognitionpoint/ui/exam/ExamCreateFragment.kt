package com.nhahv.speechrecognitionpoint.ui.exam


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.ExamObject
import com.nhahv.speechrecognitionpoint.util.Constant
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_EXAM_LIST
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.sharePrefs
import com.nhahv.speechrecognitionpoint.util.toast
import kotlinx.android.synthetic.main.fragment_exam_create.*

class ExamCreateFragment : DialogFragment() {

    companion object {
        fun newInstance() = ExamCreateFragment()
    }

    var listener: ((ExamObject) -> Unit)? = null
    val items: String = String()

    private val yearAdapter: ArrayAdapter<String> by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.fragment_exam_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
        yearAdapter.addAll(Constant.arrayYear)
        spYear.adapter = yearAdapter
        spYear.setSelection(4)
        cancel.setOnClickListener { dismiss() }
        createGroupExam.setOnClickListener {

            if (TextUtils.isEmpty(edtSttSubjectExam.text)) {
                toast("Mã kỳ thi không được để trống")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(edtIdSubjectExam.text)) {
                toast("Tên kỳ thi không được để trống")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(edtIdSchoolYear.text)) {
                toast("Mã kỳ thi không được để trống")
                return@setOnClickListener
            }
            getExamList().forEach { it ->
                if (it.idExam.trim().toLowerCase() == edtSttSubjectExam.text.toString().trim().toLowerCase()) {
                    toast("Mã kỳ thi đã tồn tại")
                    return@setOnClickListener
                }
            }
            val examObject = ExamObject(edtSttSubjectExam.text.toString(), edtIdSubjectExam.text.toString(), edtIdSchoolYear.text.toString(), spYear.selectedItem.toString())
            listener?.invoke(examObject)
            dismiss()
        }
    }

    private fun getExamList(): ArrayList<ExamObject> {
        val value = sharePrefs().getString(PREF_EXAM_LIST, "")
        if (TextUtils.isEmpty(value) || value == null || value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<ExamObject>>(value)
    }

    fun createExamCallback(l: ((ExamObject) -> Unit)?) {
        listener = l
    }

}
