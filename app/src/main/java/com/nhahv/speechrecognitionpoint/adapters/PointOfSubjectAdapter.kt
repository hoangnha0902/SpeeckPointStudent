package com.nhahv.speechrecognitionpoint.adapters

import android.view.View
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.MarmotExamItem
import kotlinx.android.synthetic.main.item_main_exam.view.*

class PointOfSubjectAdapter(val marmotExams: ArrayList<MarmotExamItem>,
                            listener: ((View, MarmotExamItem, Int) -> Unit)?
) : BaseRecyclerAdapter<MarmotExamItem>(marmotExams, R.layout.item_main_exam, listener) {

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        with(marmotExams[position]) {
            holder.itemView.apply {
                idMarmotExam.text = idMarmot
                pointOfMarmotExam.text = pointOfMarmot
            }
        }
    }
}