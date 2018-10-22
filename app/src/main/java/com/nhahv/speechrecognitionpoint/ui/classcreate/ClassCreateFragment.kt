package com.nhahv.speechrecognitionpoint.ui.classcreate

import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.AClass
import com.nhahv.speechrecognitionpoint.ui.classstudent.ClassStudentFragment
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_CLASS
import com.nhahv.speechrecognitionpoint.util.convertCompare
import com.nhahv.speechrecognitionpoint.util.fromJson
import com.nhahv.speechrecognitionpoint.util.toast
import kotlinx.android.synthetic.main.class_create_fragment.*

class ClassCreateFragment : DialogFragment() {

    companion object {
        fun newInstance() = ClassCreateFragment()
    }

    private lateinit var viewModel: ClassCreateViewModel
    private var listener: ClassStudentFragment.OnDismissListener? = null
    private val yearClasses = ArrayList<String>()
    private var classYear: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.class_create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClassCreateViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        yearClasses.add("2014 - 2015")
        yearClasses.add("2015 - 2016")
        yearClasses.add("2016 - 2017")
        yearClasses.add("2017 - 2018")
        yearClasses.add("2018 - 2019")
        yearClasses.add("2019 - 2020")
        yearClasses.add("2020 - 2021")
        yearClasses.add("2021 - 2022")
        yearClasses.add("2022 - 2023")
        yearClasses.add("2023 - 2024")
        classYear = yearClasses[4]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        val adapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, yearClasses)
        spClassYear.adapter = adapter
        spClassYear.setSelection(4)
        cancel.setOnClickListener { dismiss() }
        createClass.setOnClickListener {
            if (TextUtils.isEmpty(subjectName.text.toString())) {
                toast("Tên lớp không thể để trống.")
                return@setOnClickListener
            }
            val aclasses = getClasses()
            for (iClass in aclasses) {
                if (convertCompare(iClass.name) == convertCompare(subjectName.text.toString())) {
                    toast("Lớp đã được tạo")
                    return@setOnClickListener
                }
            }
            val aClass = AClass(subjectName.text.toString(), classNumber.text.toString().toInt(), classYear)
            aclasses.add(aClass)
            SharedPrefs.getInstance(requireContext()).put(PREF_CLASS, aclasses)
            toast("Tạo lớp học thành công!")
            dismiss()
        }
        spClassYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                classYear = yearClasses[p2]
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        listener?.let {
            it.onRefreshWhenDismiss()
        }
    }

    private fun getClasses(): ArrayList<AClass> {
        val value = SharedPrefs.getInstance(requireContext()).get(PREF_CLASS, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<AClass>>(value)
    }

    fun setOnDismissListener(onDismissListener: ClassStudentFragment.OnDismissListener) {
        listener = onDismissListener
    }

}
