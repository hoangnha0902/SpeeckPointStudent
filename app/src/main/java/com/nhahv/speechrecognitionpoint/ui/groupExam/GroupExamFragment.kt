package com.nhahv.speechrecognitionpoint.ui.groupExam

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.ExamObject
import com.nhahv.speechrecognitionpoint.data.models.GroupExam
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.BUNDLE_ID_EXAM
import com.nhahv.speechrecognitionpoint.util.Constant.BUNDLE_ID_GROUP_EXAM
import com.nhahv.speechrecognitionpoint.util.Constant.BUNLE_EXAM_OBJECT
import kotlinx.android.synthetic.main.fragment_group_exam.*
import kotlinx.android.synthetic.main.item_group_exam.view.*

class GroupExamFragment : Fragment() {

    val examObject: ExamObject? by lazy {
        arguments?.getParcelable(BUNLE_EXAM_OBJECT) as ExamObject? ?: ExamObject()
    }
    val groupExamList = ArrayList<GroupExam>()
    val groupExamAdapter: GroupExamAdapter by lazy {
        GroupExamAdapter(groupExamList) { view, groupExam, i ->
            navigate(R.id.action_groupExam_to_subjectExam, Bundle().apply {
                putString(BUNDLE_ID_EXAM, groupExam.idExam)
                putString(BUNDLE_ID_GROUP_EXAM, groupExam.idGroupExam)
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_group_exam, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupExamLists.adapter = groupExamAdapter
        groupExamList.clear()
        groupExamList.addAll(getGroupExamLists())

        groupExamCreate.setOnClickListener {
            fragmentManager?.let {
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("groupExamCreate")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = GroupExamCreateFragment.newInstance()
                dialog.idExamObject = examObject?.idExam
                dialog.listenerCallback { groupExam ->
                    val temp = groupExam.copy(idExam = examObject?.idExam ?: "")
                    groupExamList.add(temp)
                    updateGroupExams(groupExamList)
                    groupExamList.clear()
                    groupExamList.addAll(getGroupExamLists())
                    groupExamAdapter.notifyDataSetChanged()
                }
                dialog.show(it, "groupExamCreate")
            }
        }
    }


    private fun getGroupExamLists(): ArrayList<GroupExam> {
        val value = sharePrefs().get(prefGroupExam(examObject?.idExam), "")
        if (TextUtils.isEmpty(value)) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<GroupExam>>(value)
    }

    private fun updateGroupExams(items: ArrayList<GroupExam>) {
        sharePrefs().put(prefGroupExam(examObject?.idExam), items)
    }

    class GroupExamAdapter(groupExams: ArrayList<GroupExam>,
                           listener: ((View, GroupExam, Int) -> Unit)?
    ) : BaseRecyclerAdapter<GroupExam>(groupExams, R.layout.item_group_exam, listener) {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_group_exam, p0, false)
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
                idGroupExam.text = item.idGroupExam
                yearGroupExam.text = item.nameYearGroupExam
                nameGroupExam.text = item.nameGroupExam
            }
        }
    }
}
