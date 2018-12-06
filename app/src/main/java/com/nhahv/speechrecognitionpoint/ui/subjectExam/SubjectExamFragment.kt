package com.nhahv.speechrecognitionpoint.ui.subjectExam


import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter

import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.GroupExam
import com.nhahv.speechrecognitionpoint.data.models.SubjectExam
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.fragment_subject_exam.*
import kotlinx.android.synthetic.main.item_subject_exam.view.*

class SubjectExamFragment : Fragment() {

    private var idExamObject: String? = null
    private var idGroupExam: String? = null
    private val subjectExams = ArrayList<SubjectExam>()
    private val subjectExamAdapter: SubjectExamAdapter by lazy {
        SubjectExamAdapter(subjectExams) { view, subjectExam, i ->

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idExamObject = it.getString(Constant.BUNDLE_ID_EXAM)
            idGroupExam = it.getString(Constant.BUNDLE_ID_GROUP_EXAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subject_exam, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subjectExams.add(SubjectExam())
        subjectExams.add(SubjectExam())
        subjectExams.add(SubjectExam())
        subjectExams.add(SubjectExam())
        subjectExamList.adapter = subjectExamAdapter

        subjectExamCreate.setOnClickListener {
            fragmentManager?.let { it ->
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("createSubjectExam")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = CreateSubjectExamFragment.newInstance()
                dialog.show(it, "createSubjectExam")
            }
        }
    }


    private fun getSubjectExams(): ArrayList<SubjectExam> {
        val value = sharePrefs().get(prefSubjectExam(idExamObject, idGroupExam), "")
        if (TextUtils.isEmpty(value)) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<SubjectExam>>(value)
    }


    private fun updateSubjectExams(items: ArrayList<GroupExam>) {
        sharePrefs().put(prefSubjectExam(idExamObject, idGroupExam), items)
    }

    class SubjectExamAdapter(groupExams: ArrayList<SubjectExam>,
                             listener: ((View, SubjectExam, Int) -> Unit)?
    ) : BaseRecyclerAdapter<SubjectExam>(groupExams, R.layout.item_subject_exam, listener) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = items[position]
            holder.itemView.apply {
                sttSubjectExam.text = item.stt
                idSubjectExam.text = item.idSubjectExam
                nameSubjectExam.text = item.nameSubjectExam
            }
        }
    }
}
