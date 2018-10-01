package com.nhahv.speechrecognitionpoint.ui.classstudent

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.R
import kotlinx.android.synthetic.main.class_student_fragment.*

class ClassStudentFragment : Fragment() {

    companion object {
        fun newInstance() = ClassStudentFragment()
    }

    private lateinit var viewModel: ClassStudentViewModel
    private val aClasses: ArrayList<String> = ArrayList()
    private val aClassAdapter = AClassAdapter(aClasses, object : BaseRecyclerViewAdapter.OnItemListener<String> {
        override fun onClick(item: String, position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_student_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClassStudentViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aClasses.add("")
        aClasses.add("")
        aClasses.add("")
        aClasses.add("")
        aClasses.add("")
        aClasses.add("")
        classList.adapter = aClassAdapter
        aClassAdapter.notifyDataSetChanged()

    }

    class AClassAdapter(items: ArrayList<String>,
                        val listener: BaseRecyclerViewAdapter.OnItemListener<String>)
        : BaseRecyclerViewAdapter<String>(items, R.layout.item_class, listener) {

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): BaseViewHolder<String> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
            val lp: GridLayoutManager.LayoutParams = view.layoutParams as GridLayoutManager.LayoutParams
            lp.width = parent.measuredWidth / 2 - 32
            lp.leftMargin = 16
            lp.rightMargin = 16
            lp.topMargin = 16
            lp.bottomMargin = 16
            view.layoutParams = lp
            return BaseViewHolder(view, items, listener)
        }

        override fun onBindViewHolder(holder: BaseViewHolder<String>, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.itemView.apply {

            }
        }

    }
}
