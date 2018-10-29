package com.nhahv.speechrecognitionpoint.ui.subjects

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.ui.classstudent.ClassStudentFragment
import com.nhahv.speechrecognitionpoint.ui.subjectCreate.SubjectCreateFragment
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.CLASS_NAME
import com.nhahv.speechrecognitionpoint.util.Constant.SEMESTER_PARAM
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECT_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.navigate
import com.nhahv.speechrecognitionpoint.util.setUpToolbar
import kotlinx.android.synthetic.main.item_subject.view.*
import kotlinx.android.synthetic.main.subjects_fragment.*

class SubjectsFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: SubjectsViewModel
    private val subjects = ArrayList<Subject>()
    private var aClass: AClass? = null
    private val adapter = SubjectAdapter(subjects, object : BaseRecyclerViewAdapter.OnItemListener<Subject> {
        override fun onClick(item: Subject, position: Int) {
            navigate(R.id.action_subjectsFragment_to_mainFragment, Bundle().apply {
                putParcelable(CLASSES, aClass)
                putParcelable(SUBJECTS, item)
            })
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aClass = arguments?.getParcelable(CLASSES)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.subjects_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubjectsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar, "Danh sách môn học lớp ${aClass?.name}")
        subjects.clear()
        subjects.addAll(getSubjects())
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
                dialog.className = aClass?.name
                dialog.setOnDismissListener(object : ClassStudentFragment.OnDismissListener {
                    override fun onRefreshWhenDismiss() {
                        refreshData()
                    }
                })
            }
        }
    }

    private fun refreshData() {
        subjects.clear()
        subjects.addAll(getSubjects())
        adapter.notifyDataSetChanged()
    }

    private fun getSubjects(): ArrayList<Subject> {
        val value = SharedPrefs.getInstance(requireContext()).get(SharedPrefs.PREF_SUBJECT.format(aClass?.name), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Subject>>(value)
    }

    class SubjectAdapter(items: ArrayList<Subject>,
                         listener: OnItemListener<Subject>
    ) : BaseRecyclerViewAdapter<Subject>(items, R.layout.item_subject, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder<Subject>, position: Int) {
            super.onBindViewHolder(holder, position)
            val subject = items[position]
            holder.itemView.apply {
                subjectName.text = subject.subjectName
                semester.text = subject.semester.getSemester()
            }
        }
    }
}
