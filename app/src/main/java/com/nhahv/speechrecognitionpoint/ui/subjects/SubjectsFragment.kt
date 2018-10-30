package com.nhahv.speechrecognitionpoint.ui.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.data.models.Subject
import com.nhahv.speechrecognitionpoint.ui.subjectCreate.SubjectCreateFragment
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import com.nhahv.speechrecognitionpoint.util.navigate
import com.nhahv.speechrecognitionpoint.util.obtainViewModel
import com.nhahv.speechrecognitionpoint.util.setUpToolbar
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
                dialog.setOnDismissListener(aClass?.name) { viewModel.getSubjects() }
            }
        }
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
