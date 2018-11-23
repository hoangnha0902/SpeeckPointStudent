package com.nhahv.speechrecognitionpoint

import android.view.View
import android.widget.Filter
import android.widget.Filterable
import com.nhahv.speechrecognitionpoint.data.models.Student
import com.nhahv.speechrecognitionpoint.data.models.TypeOfTypePoint
import com.nhahv.speechrecognitionpoint.data.models.TypePoint
import kotlinx.android.synthetic.main.item_students2.view.*
import com.daimajia.swipe.SwipeLayout

class StudentsAdapter(
        private val students: ArrayList<Student> = ArrayList(),
        typePoint: TypePoint,
        typeOfPoint: TypeOfTypePoint,
        listener: ((View, Student, Int) -> Unit)?

) : BaseRecyclerAdapter<Student>(students, R.layout.item_students2, listener), Filterable {
    private val MIN_DISTANCE = 150
    val studentOriginal = students
    var studentFilter = students
    var restorePointListener: ((SwipeLayout, Student, Int) -> Unit)? = null
    var deletePointListener: ((SwipeLayout, Student, Int) -> Unit)? = null


    override fun getItemCount(): Int {
        return studentFilter.size
    }

    fun setOnRestoreListener(restoreListener: ((SwipeLayout, Student, Int) -> Unit)?) {
        restorePointListener = restoreListener
    }

    fun setOnDeleteListener(deleteListener: ((SwipeLayout, Student, Int) -> Unit)?) {
        deletePointListener = deleteListener
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val student = studentFilter[position]

        holder.itemView.apply {
            stt.text = "Stt: ${student.stt}"
            studentNumber.text = "Mã HS: ${student.numberStudent}"
            studentName.text = student.name
            m1.text = student.m1
            m2.text = student.m2
            m3.text = student.m3
            m4.text = student.m4
            m5.text = student.m5
            p1.text = student.p1
            p2.text = student.p2
            p3.text = student.p3
            p4.text = student.p4
            p5.text = student.p5
            v1.text = student.v1
            v2.text = student.v2
            v3.text = student.v3
            v4.text = student.v4
            v5.text = student.v5
            hk.text = student.hk
            tbm.text = student.tbm

            /*   swipeLayout.setOnTouchListener { v, event ->
                   var x1 = 0f
                   var x2 = 0f

                   when (event.action) {

                   }
               }*/

            deletePoint.setOnClickListener {
                deletePointListener?.invoke(swipeLayout, student, position)
            }
            restorePoint.setOnClickListener {
                restorePointListener?.invoke(swipeLayout, student, position)
            }
        }
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
            val charString = charSequence.toString()
            studentFilter = if (charString.isEmpty()) {
                studentOriginal
            } else {
                val filteredList = ArrayList<Student>()
                for (row in studentOriginal) {
                    if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row)
                    }
                }

                filteredList
            }

            val filterResults = Filter.FilterResults()
            filterResults.values = studentFilter
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
            studentFilter = filterResults.values as ArrayList<Student>
            notifyDataSetChanged()
        }
    }
}
