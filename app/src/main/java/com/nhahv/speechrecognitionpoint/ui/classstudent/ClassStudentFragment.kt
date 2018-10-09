package com.nhahv.speechrecognitionpoint.ui.classstudent

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.SubjectsActivity
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.ui.classcreate.ClassCreateFragment
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.start
import kotlinx.android.synthetic.main.class_student_fragment.*
import kotlinx.android.synthetic.main.item_class.view.*

class ClassStudentFragment : Fragment() {

    companion object {
        fun newInstance() = ClassStudentFragment()
    }

    private lateinit var viewModel: ClassStudentViewModel
    private val aClasses: ArrayList<AClass> = ArrayList()
    private lateinit var aClassAdapter: AClassAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aClasses.clear()
        aClasses.addAll(getClasses())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_student_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClassStudentViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aClassAdapter = AClassAdapter(requireContext(), aClasses, object : BaseRecyclerViewAdapter.OnItemListener<AClass> {
            override fun onClick(item: AClass, position: Int) {
                start<SubjectsActivity>(Bundle().apply { putString("className", item.name) })
            }
        })
        classList.adapter = aClassAdapter
        aClassAdapter.notifyDataSetChanged()
        classCreate.setOnClickListener {

            fragmentManager?.let {
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("classCreate")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = ClassCreateFragment.newInstance()
                dialog.show(fm, "classCreate")
                dialog.setOnDismissListener(object : OnDismissListener {
                    override fun onRefreshWhenDismiss() {
                        refreshData()
                    }
                })
            }
        }
    }

    private fun refreshData() {
        aClasses.clear()
        aClasses.addAll(getClasses())
        aClassAdapter.notifyDataSetChanged()
    }

    private fun getClasses(): ArrayList<AClass> {
        val value = SharedPrefs.getInstance(requireContext()).get(SharedPrefs.PREF_CLASS, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<AClass>>(value)
    }


    class AClassAdapter(context: Context,
                        items: ArrayList<AClass>,
                        listener: BaseRecyclerViewAdapter.OnItemListener<AClass>)
        : BaseRecyclerViewAdapter<AClass>(items, R.layout.item_class, listener) {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BaseRecyclerViewAdapter.BaseViewHolder<AClass> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
            val lp: GridLayoutManager.LayoutParams = view.layoutParams as GridLayoutManager.LayoutParams
            lp.width = parent.measuredWidth / 2 - 32
            lp.leftMargin = 16
            lp.rightMargin = 16
            lp.topMargin = 16
            lp.bottomMargin = 16
            view.layoutParams = lp
            return BaseViewHolder(view, items, listener!!)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<AClass>, position: Int) {
            super.onBindViewHolder(holder, position)
            val aClass = items[position]
            holder.itemView.apply {
                subjectName.text = aClass.name
                yearClass.text = aClass.year
                numberOfClass.setText(Html.fromHtml("Sĩ số: <font color='#f4511e'>${aClass.number}</font> "), TextView.BufferType.SPANNABLE)

                deleteClass.setOnClickListener {
                    AlertDialog.Builder(context)
                            .setTitle("Xóa lớp học")
                            .setMessage("Bạn có muốn xóa lớp ${aClass.name}")
                            .setPositiveButton("Đồng ý") { _, _ ->
                                items.removeAt(position)
                                notifyItemRemoved(position)
                                notifyItemRangeChanged(position, items.size)
                                SharedPrefs.getInstance(context).put(SharedPrefs.PREF_CLASS, items)
                            }
                            .setNegativeButton("Không") { dialog, _ -> dialog?.cancel() }
                            .show()
                }
            }
        }
    }

    interface OnDismissListener {
        fun onRefreshWhenDismiss()
    }
}
