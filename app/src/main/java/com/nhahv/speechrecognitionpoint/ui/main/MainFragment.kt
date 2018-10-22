package com.nhahv.speechrecognitionpoint.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.SemesterType
import com.nhahv.speechrecognitionpoint.data.models.Student
import com.nhahv.speechrecognitionpoint.data.models.TypeOfTypePoint
import com.nhahv.speechrecognitionpoint.data.models.TypePoint
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASS_NAME
import com.nhahv.speechrecognitionpoint.util.Constant.SEMESTER_PARAM
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECT_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT
import kotlinx.android.synthetic.main.item_students2.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle?) = MainFragment().apply { arguments = bundle }
    }

    var className: String? = null
    var subjectName: String? = null
    var semester: SemesterType? = SemesterType.SEMESTER_I

    private lateinit var viewModel: MainViewModel
    private lateinit var speechPoint: SpeechPoint
    private val students = ArrayList<Student>()
    private val studentAdapter = StudentsAdapter(students, object : BaseRecyclerViewAdapter.OnItemListener<Student> {
        override fun onClick(item: Student, position: Int) {

        }
    })


    val typePointList = ArrayList<TypePoint>()
    var typePoint: TypePoint = TypePoint.MOUTH
    val typeOfPointList = ArrayList<TypeOfTypePoint>()
    var typeOfPoint: TypeOfTypePoint = TypeOfTypePoint.TYPE_1
    lateinit var title: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            className = it.getString(CLASS_NAME)
            subjectName = it.getString(SUBJECT_NAME)
            semester = it.getSerializable(SEMESTER_PARAM) as SemesterType
            title = "Môn $subjectName lớp học " + it.getString(CLASS_NAME)
        }
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
        setUpToolbar(toolbar, title)
        speechPoint = SpeechPoint(requireContext())

        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.fileExcelFragment -> {
                Navigation.findNavController(requireActivity(), R.id.navLoginHost).navigate(R.id.action_mainFragment_to_fileExcelFragment, Bundle().apply {
                    putString(CLASS_NAME, className)
                    putString(SUBJECT_NAME, subjectName)
                    putSerializable(SEMESTER_PARAM, semester)
                })
            }
            R.id.exportExcelFragment -> {
                Navigation.findNavController(requireActivity(), R.id.navLoginHost).navigate(R.id.action_mainFragment_to_exportExcelFragment, Bundle().apply {
                    putString(CLASS_NAME, className)
                    putString(SUBJECT_NAME, subjectName)
                    putSerializable(SEMESTER_PARAM, semester)
                })
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

        initSpTypePoint()
        initSpTypeOfPoint()
        startMic.setOnClickListener {
            speechPoint.startSpeech()
        }

        stopMic.setOnClickListener {
            speechPoint.destroy()
        }
    }

    fun initSpTypePoint() {
        typePointList.add(TypePoint.MOUTH)
        typePointList.add(TypePoint.P15)
        typePointList.add(TypePoint.WRITE)
        typePointList.add(TypePoint.SEMESTER)
        val adapterTypePoint = ArrayAdapter<TypePoint>(requireContext(), android.R.layout.simple_list_item_1, typePointList)
        spTypePoint.adapter = adapterTypePoint
        spTypePoint.setSelection(0)
        spTypePoint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                typePoint = typePointList[p2]
                if (typePoint == TypePoint.SEMESTER) {
                    typeOfPointLayout.visibility = View.INVISIBLE
                } else {
                    typeOfPointLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initSpTypeOfPoint() {
        typeOfPointList.add(TypeOfTypePoint.TYPE_1)
        typeOfPointList.add(TypeOfTypePoint.TYPE_2)
        typeOfPointList.add(TypeOfTypePoint.TYPE_3)
        typeOfPointList.add(TypeOfTypePoint.TYPE_4)
        typeOfPointList.add(TypeOfTypePoint.TYPE_5)
        val adapter = ArrayAdapter<TypeOfTypePoint>(requireContext(), android.R.layout.simple_list_item_1, typeOfPointList)
        spTypeOfPoint.adapter = adapter
        spTypeOfPoint.setSelection(0)
        spTypeOfPoint.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                typeOfPoint = typeOfPointList[p2]
            }
        }
        if (typePoint == TypePoint.SEMESTER) {
            typeOfPointLayout.visibility = View.INVISIBLE
        } else {
            typeOfPointLayout.visibility = View.VISIBLE
        }
    }

    private fun getStudentList(): ArrayList<Student> {
        val value = SharedPrefs.getInstance(requireContext()).get(PREF_STUDENT.format(className, subjectName, semester?.getSemesterName()), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Student>>(value)
    }


    class StudentsAdapter(
            private val students: ArrayList<Student> = ArrayList(),
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
