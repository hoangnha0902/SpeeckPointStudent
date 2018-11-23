package com.nhahv.speechrecognitionpoint.ui.subjects

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.data.models.SemesterType
import com.nhahv.speechrecognitionpoint.data.models.Student
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.ui.subjectCreate.SubjectCreateFragment
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import kotlinx.android.synthetic.main.item_subject.view.*
import kotlinx.android.synthetic.main.subjects_fragment.*


class SubjectsFragment : Fragment() {
    private lateinit var viewModel: SubjectsViewModel
    private var aClass: AClass? = null
    private val adapter = SubjectAdapter { _, subject, _ ->
        navigate(R.id.action_subjectsFragment_to_mainFragment, Bundle().apply {
            putParcelable(CLASSES, aClass)
            putParcelable(SUBJECTS, subject)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        aClass = arguments?.getParcelable(CLASSES)
        return inflater.inflate(R.layout.subjects_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(SubjectsViewModel::class.java, aClass?.name)
        viewModel.subjects.observe(this, Observer { subjects ->
            adapter.refresh(subjects)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar, "Danh sách môn học lớp ${aClass?.name}")

        subjectList.adapter = adapter
        addSubject.setOnClickListener {
            fragmentManager?.let {
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("subjectCreate")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = SubjectCreateFragment.newInstance()
                dialog.show(fm, "subjectCreate")
                dialog.setOnDismissListener(aClass?.name) { subjectName, semesterType ->
                    // todo import show dialog import student from another subject
                    showImportStudentFromAnotherSubject(subjectName, semesterType)
                }
            }
        }
    }

    private fun showImportStudentFromAnotherSubject(subjectName: String, semesterType: SemesterType) {
        if (adapter.itemCount <= 0) {
            addSubjectEmpty(subjectName, semesterType)
            return
        }
        val nameSubject = ArrayList<String>()
        val subjectsTemp = ArrayList<Subject>()
        adapter.items.forEach { subject: Subject ->
            if (hasStudentInSubject(subject.subjectName, subject.semester)) {
                nameSubject.add("${subject.subjectName} - ${subject.semester.getSemester()}")
                subjectsTemp.add(subject)
            }
        }
        if (nameSubject.size <= 0) {
            addSubjectEmpty(subjectName, semesterType)
            return
        }
        var itemCheck = 0
        AlertDialog.Builder(requireContext())
                .setTitle("Import học sinh từ môn học")
                .setSingleChoiceItems(nameSubject.toTypedArray(), itemCheck) { _, which ->
                    itemCheck = which
                }
                .setPositiveButton("OK") { dialog, _ ->
                    val studentTemp = studentOfSubject(subjectsTemp[itemCheck].subjectName, subjectsTemp[itemCheck].semester)

                    val subjects = viewModel.getSubjectList()
                    val subject = Subject(subjectName, semesterType, subjectsTemp[itemCheck].excel)
                    subjects.add(subject)
                    sharePrefs().put(Constant.NAME_SUBJECT_LIST(requireContext(), aClass?.name), subjects)
                    sharePrefs().put(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, subjectName, semesterType), studentTemp)
                    viewModel.getSubjects()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .create().show()

    }

    private fun addSubjectEmpty(subjectName: String, semesterType: SemesterType) {
        val subjects = viewModel.getSubjectList()
        val subject = Subject(subjectName, semesterType)
        subjects.add(subject)
        sharePrefs().put(Constant.NAME_SUBJECT_LIST(requireContext(), aClass?.name), subjects)
        viewModel.getSubjects()
    }

    private fun hasStudentInSubject(name: String, semester: SemesterType): Boolean {
        val value = sharePrefs().get(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, name, semester), "")
        if (value.isEmpty()) {
            return false
        }
        val temp = Gson().fromJson<ArrayList<Student>>(value)
        return !temp.isEmpty() && temp.size > 0
    }

    private fun studentOfSubject(name: String, semester: SemesterType): ArrayList<Student> {
        val value = sharePrefs().get(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, name, semester), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        val students = Gson().fromJson<ArrayList<Student>>(value)
        students.forEach { it -> it.reset() }
        return students
    }

    class SubjectAdapter(
            listener: ((View, Subject, Int) -> Unit)?
    ) : BaseRecyclerAdapter<Subject>(R.layout.item_subject, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val subject = items[position]
            holder.itemView.apply {
                subjectName.text = subject.subjectName
                semester.text = subject.semester.getSemester()
            }
        }
    }
}
