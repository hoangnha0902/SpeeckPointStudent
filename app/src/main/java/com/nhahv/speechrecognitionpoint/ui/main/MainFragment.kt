package com.nhahv.speechrecognitionpoint.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.FileExcelActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.model.Student
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT
import com.nhahv.speechrecognitionpoint.util.SpeechPoint
import com.nhahv.speechrecognitionpoint.util.fromJson
import kotlinx.android.synthetic.main.item_students2.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var speechPoint: SpeechPoint
    private val students = ArrayList<Student>()
    private val studentAdapter = StudentsAdapter(students, object : BaseRecyclerViewAdapter.OnItemListener<Student> {
        override fun onClick(item: Student, position: Int) {

        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        speechPoint = SpeechPoint(activity!!)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_import -> {
                requireActivity().startActivity(Intent(activity, FileExcelActivity::class.java))
            }
            R.id.menu_export -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        students.clear()
        students.addAll(getStudentList())
        studentAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        studentList.adapter = studentAdapter
        students.addAll(getStudentList())
        studentAdapter.notifyDataSetChanged()

        startMic.setOnClickListener {
            speechPoint.startSpeech()
        }

        stopMic.setOnClickListener {
            speechPoint.destroy()
        }
    }

    private fun getStudentList(): ArrayList<Student> {
        val value = SharedPrefs.getInstance(activity!!.applicationContext).get(PREF_STUDENT, "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Student>>(value)
    }


    class StudentsAdapter(
            val students: ArrayList<Student> = ArrayList(),
            listener: BaseRecyclerViewAdapter.OnItemListener<Student>

    ) : BaseRecyclerViewAdapter<Student>(students, R.layout.item_students2, listener) {
        override fun onBindViewHolder(holder: BaseViewHolder<Student>, position: Int) {
            super.onBindViewHolder(holder, position)
            val student = students[position]

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
            }
        }
    }
}
