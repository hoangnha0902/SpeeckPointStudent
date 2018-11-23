package com.nhahv.speechrecognitionpoint

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.nhahv.speechrecognitionpoint.data.models.Student
import com.nhahv.speechrecognitionpoint.data.models.TypePoint
import com.nhahv.speechrecognitionpoint.ui.main.MainFragment
import com.nhahv.speechrecognitionpoint.util.CommonUtils.textPoint
import kotlinx.android.synthetic.main.item_students.view.*

class StudentsSwapAdapter(
        private val students: ArrayList<Student> = ArrayList()

) : BaseRecyclerViewAdapter<Student>(students, R.layout.item_students), Filterable {
    var studentOriginal = students
    var studentFilter = students
    var fragment: MainFragment? = null

    override fun getItemCount() = studentFilter.size

    fun setMainFragment(mainFragment: MainFragment) {
        fragment = mainFragment
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Student>, position: Int) {
        super.onBindViewHolder(holder, position)
        val student = studentFilter[position]

        holder.itemView.apply {
            nameStudent.text = student.name
            var textMouth = ""
            if (!TextUtils.isEmpty(student.m1)) {
                textMouth += textPoint("M1", student.m1)
            }
            if (!TextUtils.isEmpty(student.m2)) {
                textMouth += textPoint("M2", student.m2)
            }
            if (!TextUtils.isEmpty(student.m3)) {
                textMouth += textPoint("M3", student.m3)
            }
            if (!TextUtils.isEmpty(student.m4)) {
                textMouth += textPoint("M4", student.m4)
            }
            if (!TextUtils.isEmpty(student.m5)) {
                textMouth += textPoint("M5", student.m5)
            }
            valueMouth.setText(Html.fromHtml(textMouth), TextView.BufferType.SPANNABLE)

            var textP15 = ""
            if (!TextUtils.isEmpty(student.p1)) {
                textP15 += textPoint("P1", student.p1)
            }
            if (!TextUtils.isEmpty(student.p2)) {
                textP15 += textPoint("P2", student.p2)
            }
            if (!TextUtils.isEmpty(student.p3)) {
                textP15 += textPoint("P3", student.p3)
            }
            if (!TextUtils.isEmpty(student.p4)) {
                textP15 += textPoint("P4", student.p4)
            }
            if (!TextUtils.isEmpty(student.p5)) {
                textP15 += textPoint("P5", student.p5)
            }
            valueP15.setText(Html.fromHtml(textP15), TextView.BufferType.SPANNABLE)

            var textWrite = ""
            if (!TextUtils.isEmpty(student.v1)) {
                textWrite += textPoint("V1", student.v1)
            }
            if (!TextUtils.isEmpty(student.v2)) {
                textWrite += textPoint("V2", student.v2)
            }
            if (!TextUtils.isEmpty(student.v3)) {
                textWrite += textPoint("V3", student.v3)
            }
            if (!TextUtils.isEmpty(student.v4)) {
                textWrite += textPoint("V4", student.v4)
            }
            if (!TextUtils.isEmpty(student.v5)) {
                textWrite += textPoint("V5", student.v5)
            }
            valueWrite.setText(Html.fromHtml(textWrite), TextView.BufferType.SPANNABLE)
            if (!TextUtils.isEmpty(student.hk)) {
                val textSemester = textPoint("HK", student.hk)
                valueSemester.setText(Html.fromHtml(textSemester), TextView.BufferType.SPANNABLE)
            }
            if (!TextUtils.isEmpty(student.tbm)) {
                val textTBM = textPoint("TBM", student.tbm)
                valueTBM.setText(Html.fromHtml(textTBM), TextView.BufferType.SPANNABLE)
            }

/*

                pointMouth.visibility = View.VISIBLE
                point15P.visibility = View.VISIBLE
                pointWrite.visibility = View.VISIBLE
                pointSemester.visibility = View.VISIBLE
                pointTBM.visibility = View.VISIBLE
                lineTBM.visibility = View.VISIBLE
                minusPlus.setImageResource(R.mipmap.circle_minus)
*/


            llInfo.setOnClickListener {
                pointMouth.visibility = if (pointMouth.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                point15P.visibility = if (point15P.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                pointWrite.visibility = if (pointWrite.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                pointSemester.visibility = if (pointSemester.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                pointTBM.visibility = if (pointTBM.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                lineTBM.visibility = if (lineTBM.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                minusPlus.setImageResource(if (lineTBM.visibility == View.VISIBLE) R.mipmap.circle_plus else R.mipmap.circle_minus)
            }

            pointMouth.setOnClickListener {
                fragment?.showInputPointDialog("Điểm miệng", TypePoint.MOUTH, position)
            }

            point15P.setOnClickListener {
                fragment?.showInputPointDialog("Điểm 15 phút", TypePoint.P15, position)
            }

            pointWrite.setOnClickListener {
                fragment?.showInputPointDialog("Điểm 1 tiết", TypePoint.WRITE, position)
            }

            pointSemester.setOnClickListener {
                fragment?.showInputPointDialog("Học kỳ", TypePoint.SEMESTER, position)
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