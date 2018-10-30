package com.nhahv.speechrecognitionpoint.ui.classstudent

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.ui.classcreate.ClassCreateFragment
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import kotlinx.android.synthetic.main.class_student_fragment.*
import kotlinx.android.synthetic.main.item_class.view.*

class ClassStudentFragment : Fragment() {

    private lateinit var viewModel: ClassStudentViewModel
    private lateinit var aClassAdapter: AClassAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.class_student_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = obtainViewModel(ClassStudentViewModel::class.java, "")
        viewModel.aClasses.observe(this, Observer { aclasses ->
            aClassAdapter.refresh(aclasses)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aClassAdapter = AClassAdapter { _, aClass, _ ->
            navigate(R.id.action_classStudentFragment_to_subjectsFragment, Bundle().apply { putParcelable(CLASSES, aClass) })
        }
        classList.adapter = aClassAdapter
        classCreate.setOnClickListener {
            fragmentManager?.let {
                val fm = it.beginTransaction()
                val prev = it.findFragmentByTag("classCreate")
                if (prev != null) {
                    fm.remove(prev)
                }
                fm.addToBackStack(null)
                val dialog = ClassCreateFragment.newInstance()
                dialog.setOnDismissListener { viewModel.getClasses() }
                dialog.show(fm, "classCreate")
            }
        }
    }

    class AClassAdapter(
            listener: ((View, AClass, Int) -> Unit)?
    ) : BaseRecyclerAdapter<AClass>(R.layout.item_class, listener) {

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BaseViewHolder {
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_class, p0, false)
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
}
