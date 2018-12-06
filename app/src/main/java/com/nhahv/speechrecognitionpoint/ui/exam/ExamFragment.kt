package com.nhahv.speechrecognitionpoint.ui.exam

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.ExamObject
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.exam_fragment.*
import kotlinx.android.synthetic.main.item_exam.view.*

class ExamFragment : Fragment() {

    private lateinit var viewModel: ExamViewModel
    private val examObjectList = ArrayList<ExamObject>()
    private val examAdapter: ExamAdapter by lazy {
        ExamAdapter(examObjectList) { view, examObject, i ->
            navigate(R.id.action_examFragment_to_groupExamFragment, Bundle().apply {
                putParcelable(Constant.BUNLE_EXAM_OBJECT, examObject)
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.exam_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        examList.adapter = examAdapter
        refresh()
        examCreate.setOnClickListener {
            fragmentManager?.let {
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("examCreate")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = ExamCreateFragment.newInstance()
                dialog.createExamCallback { examObject ->
                    examObjectList.add(examObject)
                    updateExamList(examObjectList)
                    refresh()
                    println(examObject)
                }
                dialog.show(fm, "examCreate")
            }
        }
    }

    private fun refresh() {
        examObjectList.clear()
        val value = sharePrefs().getString(SharedPrefs.PREF_EXAM_LIST, "")
        if (TextUtils.isEmpty(value) || value == null || value.isEmpty()) {
            return
        }
        examObjectList.addAll(Gson().fromJson<ArrayList<ExamObject>>(value))
    }

    private fun updateExamList(list: ArrayList<ExamObject>) {
        sharePrefs().put(SharedPrefs.PREF_EXAM_LIST, list)
    }

    class ExamAdapter(
            examObjects: ArrayList<ExamObject>,
            listener: ((View, ExamObject, Int) -> Unit)?
    ) : BaseRecyclerAdapter<ExamObject>(examObjects, R.layout.item_exam, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)

            val item = items[position]
            holder.itemView.apply {
                idExam.text = item.idExam
                titleExam.text = item.nameExam
                idSchoolYear.text = item.idSchoolYear
                nameSchoolYear.text = item.nameSchoolYear
            }
        }
    }

}
