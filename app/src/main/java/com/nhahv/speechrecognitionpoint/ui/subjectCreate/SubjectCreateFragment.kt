package com.nhahv.speechrecognitionpoint.ui.subjectCreate

import androidx.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.SemesterType
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_SUBJECT
import com.nhahv.speechrecognitionpoint.util.convertCompare
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.toast
import kotlinx.android.synthetic.main.subject_create_fragment.*


class SubjectCreateFragment : androidx.fragment.app.DialogFragment() {

    companion object {
        fun newInstance() = SubjectCreateFragment()
    }

    private lateinit var viewModel: SubjectCreateViewModel
    private lateinit var semester: SemesterType
    private val semesterList = ArrayList<SemesterType>()
    private var listener: (() -> Unit)? = null
    var className: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        semesterList.add(SemesterType.SEMESTER_I)
        semesterList.add(SemesterType.SEMESTER_II)
        semester = SemesterType.SEMESTER_I
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.subject_create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubjectCreateViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        val adapter: ArrayAdapter<SemesterType> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, semesterList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spClassSemester.adapter = adapter
        spClassSemester.setSelection(0)

        spClassSemester.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                semester = semesterList[p2]
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }
        createSubject.setOnClickListener {
            if (subjectName.text == null || subjectName.text.toString().isEmpty()) {
                toast("Tên môn không thể để trống.")
                return@setOnClickListener
            }
            val subjects = getSubjects()
            for (subject in subjects) {
                if (convertCompare(subject.subjectName) == convertCompare(subjectName.text.toString()) && subject.semester == semester) {
                    toast("Môn học đã được tạo")
                    return@setOnClickListener
                }
            }
            val subject = Subject(subjectName.text.toString(), semester)
            subjects.add(subject)
            SharedPrefs.getInstance(requireContext()).put(PREF_SUBJECT.format(className), subjects)
            toast("Tạo môn học thành công!")
            dismiss()
        }
    }

    private fun getSubjects(): ArrayList<Subject> {
        val value = SharedPrefs.getInstance(requireContext()).get(PREF_SUBJECT.format(className), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Subject>>(value)
    }


    fun setOnDismissListener(function: () -> Unit) {
        listener = function
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.invoke()
    }
}
