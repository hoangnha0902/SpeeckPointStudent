package com.nhahv.speechrecognitionpoint.ui.classcreate

import androidx.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.ui.classstudent.ClassStudentFragment
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_CLASS
import kotlinx.android.synthetic.main.class_create_fragment.*

class ClassCreateFragment : androidx.fragment.app.DialogFragment() {

    companion object {
        fun newInstance() = ClassCreateFragment()
    }

    private val yearAdapter: ArrayAdapter<String> by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1) }
    private lateinit var viewModel: ClassCreateViewModel
    private var listener: ClassStudentFragment.OnDismissListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.class_create_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        spClassYear.adapter = yearAdapter
        spClassYear.setSelection(4)
        cancel.setOnClickListener { dismiss() }
        createClass.setOnClickListener {
            if (TextUtils.isEmpty(subjectName.text.toString())) {
                toast("Tên lớp không thể để trống.")
                return@setOnClickListener
            }
            val aClasses = getClassList()
            for (iClass in aClasses) {
                if (convertCompare(iClass.name) == convertCompare(subjectName.text.toString())) {
                    toast("Lớp đã được tạo")
                    return@setOnClickListener
                }
            }
            val aClass = AClass(subjectName.text.toString(), classNumber.text.toString().toInt(), spClassYear.selectedItem.toString())
            aClasses.add(aClass)
            putPref(PREF_CLASS, aClasses)
            toast("Tạo lớp học thành công!")
            dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClassCreateViewModel::class.java)
        viewModel.years.observe(this, Observer { years ->
            yearAdapter.addAll(years)
        })
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.onRefreshWhenDismiss()
    }

    fun setOnDismissListener(onDismissListener: ClassStudentFragment.OnDismissListener) {
        listener = onDismissListener
    }

    private fun getClassList(): ArrayList<AClass> {
        val value = sharePrefs().get(PREF_CLASS, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<AClass>>(value)
    }
}
