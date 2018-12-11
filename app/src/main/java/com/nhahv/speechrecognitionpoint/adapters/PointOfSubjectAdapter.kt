package com.nhahv.speechrecognitionpoint.adapters

import android.text.TextUtils
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.MarmotExamItem
import kotlinx.android.synthetic.main.item_main_exam.view.*

class PointOfSubjectAdapter(private val marmotExams: ArrayList<MarmotExamItem>,
                            listener: ((View, MarmotExamItem, Int) -> Unit)?
) : BaseRecyclerAdapter<MarmotExamItem>(marmotExams, R.layout.item_main_exam, listener), Filterable {

    val marmotOriginal = marmotExams
    var marmotFilter = marmotExams

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        with(marmotFilter[position]) {
            holder.itemView.apply {
                idMarmotExam.text = idMarmot
                pointOfMarmotExam.text = pointOfMarmot
            }
        }
    }

    override fun getItemCount() = marmotFilter.size

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charFilter = constraint.toString()
            val temp = if (TextUtils.isEmpty(charFilter)){
                marmotOriginal
            }else{
                val filteredList = ArrayList<MarmotExamItem>()
                for (row in marmotOriginal){
                    if (row.idMarmot.trim().toUpperCase().contains(charFilter.trim().toUpperCase())){
                        filteredList.add(row)
                    }
                }
                filteredList
            }
            val filterResults = Filter.FilterResults()
            filterResults.values = temp
            return  filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            marmotFilter = results?.values as ArrayList<MarmotExamItem>
            notifyDataSetChanged()
        }
    }
}