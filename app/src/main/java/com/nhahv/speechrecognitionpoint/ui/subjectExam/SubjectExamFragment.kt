package com.nhahv.speechrecognitionpoint.ui.subjectExam


import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
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
        subjectExams.clear()
        subjectExams.addAll(getSubjectExams())
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
                dialog.idExamObject = idExamObject
                dialog.idGroupExam = idGroupExam
                dialog.createSubjectExamCallback { subjectExam ->
                    val items = getSubjectExams()
                    items.add(subjectExam.copy())
                    updateSubjectExams(items)
                    subjectExams.clear()
                    subjectExams.addAll(getSubjectExams())
                    subjectExamAdapter.notifyDataSetChanged()
                }
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

    private fun updateSubjectExams(items: ArrayList<SubjectExam>) {
        sharePrefs().put(prefSubjectExam(idExamObject, idGroupExam), items)
    }

    class SubjectExamAdapter(groupExams: ArrayList<SubjectExam>,
                             listener: ((View, SubjectExam, Int) -> Unit)?
    ) : BaseRecyclerAdapter<SubjectExam>(groupExams, R.layout.item_subject_exam, listener) {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_subject_exam, p0, false)
            val lp: androidx.recyclerview.widget.GridLayoutManager.LayoutParams = view.layoutParams as androidx.recyclerview.widget.GridLayoutManager.LayoutParams
            lp.width = p0.measuredWidth / 2 - 32
            lp.leftMargin = 16
            lp.rightMargin = 16
            lp.topMargin = 16
            lp.bottomMargin = 16
            view.layoutParams = lp
            return BaseRecyclerAdapter.BaseViewHolder(view)
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val item = items[position]
            holder.itemView.apply {
                idSubjectExam.text = item.idSubjectExam
                nameSubjectExam.text = item.nameSubjectExam
            }
        }
    }
}
