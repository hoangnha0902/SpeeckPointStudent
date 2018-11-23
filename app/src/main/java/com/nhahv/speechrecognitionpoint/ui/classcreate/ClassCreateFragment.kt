package com.nhahv.speechrecognitionpoint.ui.classcreate

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.util.*
import kotlinx.android.synthetic.main.class_create_fragment.*

class ClassCreateFragment : androidx.fragment.app.DialogFragment() {

    companion object {
        fun newInstance() = ClassCreateFragment()
    }

    private val yearAdapter: ArrayAdapter<String> by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1) }
    private lateinit var viewModel: ClassCreateViewModel
    private var listener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
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
            putPref(Constant.NAME_CLASS_LIST(requireContext()), aClasses)
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
        listener?.invoke()
    }

    fun setOnDismissListener(onListener: () -> Unit) {
        listener = onListener
    }

    private fun getClassList(): ArrayList<AClass> {
        val value = sharePrefs().get(Constant.NAME_CLASS_LIST(requireContext()), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<AClass>>(value)
    }
}
