package com.nhahv.speechrecognitionpoint.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Html
import android.text.TextUtils
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.*
import com.nhahv.speechrecognitionpoint.ui.pointInput.PointInputFragment
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.CommonUtils.textPoint
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.CLASS_NAME
import com.nhahv.speechrecognitionpoint.util.Constant.SEMESTER_PARAM
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECT_NAME
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT
import kotlinx.android.synthetic.main.item_students.view.*
import kotlinx.android.synthetic.main.item_students2.view.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : androidx.fragment.app.Fragment() {
    var semester: SemesterType? = SemesterType.SEMESTER_I
    var indexChange: Int = -1
    var isPointing: Boolean = false

    var subject: Subject? = null
    var aClass: AClass? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var speechPoint: SpeechPoint
    private val students = ArrayList<Student>()
    private val studentAdapter = StudentsAdapter(students, object : BaseRecyclerViewAdapter.OnItemListener<Student> {
        override fun onClick(item: Student, position: Int) {

        }
    })

    private val studentSwapAdapter = StudentsSwapAdapter(students)

    val typePointList = ArrayList<TypePoint>()
    var typePoint: TypePoint = TypePoint.MOUTH
    val typeOfPointList = ArrayList<TypeOfTypePoint>()
    var typeOfPoint: TypeOfTypePoint = TypeOfTypePoint.TYPE_1
    lateinit var title: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        studentSwapAdapter.setMainFragment(this)
        arguments?.let {
            aClass = it.getParcelable(CLASSES)
            subject = it.getParcelable(SUBJECTS)
            semester = subject?.semester
            title = "Môn ${subject?.subjectName} lớp học ${aClass?.name}"
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
        speechPoint.setMainFragment(this)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.fileExcelFragment -> {
                navigate(R.id.action_mainFragment_to_fileExcelFragment, arguments!!)
            }
            R.id.exportExcelFragment -> {
                navigate(R.id.action_mainFragment_to_exportExcelFragment, arguments!!)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        notifyUI()
    }

    private fun initViews() {

        studentList.adapter = studentAdapter
        studentListSwap.adapter = studentSwapAdapter
        notifyUI()

        studentListSwap.visibility = View.GONE
        initSpTypePoint()
        initSpTypeOfPoint()
        startMic.setOnClickListener {
            if (!speechPoint.speechStarted) {
                (requireActivity() as MainActivity).startSpeech { startSpeech() }
                startMic.setImageResource(R.drawable.ic_stop)
            } else {
                speechPoint.destroy()
                startMic.setImageResource(R.mipmap.ic_mic_white_24dp)
            }
        }

        swap.setOnClickListener {
            studentList.visibility = if (studentList.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            studentListSwap.visibility = if (studentListSwap.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    private fun notifyUI() {
        students.clear()
        students.addAll(getStudentList())
        notifyAdapter()
    }

    private fun notifyAdapter() {
        studentAdapter.notifyDataSetChanged()
        studentSwapAdapter.notifyDataSetChanged()
    }

    fun startSpeech() {
        speechPoint.startSpeech()
    }

    private fun initSpTypePoint() {
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
        val value = SharedPrefs.getInstance(requireContext()).get(PREF_STUDENT.format(aClass?.name, subject?.subjectName, semester?.getSemesterName()), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Student>>(value)
    }

    fun onTextRecognition(matches: String) {
        println("=================== $matches")
        textSpeech.text = matches
        if (checkNameIsExist(matches)) {

        } else if (isPointing) {
            matches.replace(",", ".")
            try {
                val point = CommonUtils.round(matches.toDouble(), 2)
                writePointToStudent(point)
            } catch (ex: NumberFormatException) {
                toast("Nhập lai điểm")
            }
        }

    }

    private fun checkNameIsExist(name: String): Boolean {
        for ((index, student) in students.withIndex()) {
            if (name.trim() == student.name) {
                println("=============== $index")
                indexChange = index
                isPointing = true
                return true
            }
        }
        return false
    }

    fun showInputPointDialog(label: String, typePoint: TypePoint, position: Int) {
        fragmentManager?.let {
            val fm = it.beginTransaction()
            val prev = it.findFragmentByTag("inputPoint")
            if (prev != null) {
                fm.remove(prev)
            }
            fm.addToBackStack(null)
            val dialog = PointInputFragment.newInstance(label, typePoint, position)
            dialog.show(fm, "inputPoint")
            dialog.setOnDismissListener(object : OnDismissListener {
                override fun onRefreshWhenDismiss(pointValue: Double, typePointValue: TypePoint, position: Int?) {
                    writePointToStudentWidthInput(pointValue, typePointValue, position)
                }
            })
        }
    }

    private fun writePointToStudent(point: Double) {
        if (indexChange == -1) return
        val student = students[indexChange]
        isPointing = false
        indexChange = -1
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.m1 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.m2 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.m3 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.m4 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.m5 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.p1 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.p2 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.p3 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.p4 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.p5 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.v1 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.v2 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.v3 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.v4 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.v5 = point.toString()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.SEMESTER) {
            student.hk = point.toString()
            notifyAdapter()
            return
        }
    }

    private fun writePointToStudentWidthInput(point: Double, typePointValue: TypePoint, position: Int?) {
        if (position == null) return
        val student = students[position]
        when (typePointValue) {
            TypePoint.MOUTH -> {
                when (typeOfPoint) {
                    TypeOfTypePoint.TYPE_1 -> student.m1 = point.toString()
                    TypeOfTypePoint.TYPE_2 -> student.m2 = point.toString()
                    TypeOfTypePoint.TYPE_3 -> student.m3 = point.toString()
                    TypeOfTypePoint.TYPE_4 -> student.m4 = point.toString()
                    TypeOfTypePoint.TYPE_5 -> student.m5 = point.toString()
                }
            }
            TypePoint.P15 -> {
                when (typeOfPoint) {
                    TypeOfTypePoint.TYPE_1 -> student.p1 = point.toString()
                    TypeOfTypePoint.TYPE_2 -> student.p2 = point.toString()
                    TypeOfTypePoint.TYPE_3 -> student.p3 = point.toString()
                    TypeOfTypePoint.TYPE_4 -> student.p4 = point.toString()
                    TypeOfTypePoint.TYPE_5 -> student.p5 = point.toString()
                }
            }
            TypePoint.WRITE -> {
                when (typeOfPoint) {
                    TypeOfTypePoint.TYPE_1 -> student.v1 = point.toString()
                    TypeOfTypePoint.TYPE_2 -> student.v2 = point.toString()
                    TypeOfTypePoint.TYPE_3 -> student.v3 = point.toString()
                    TypeOfTypePoint.TYPE_4 -> student.v4 = point.toString()
                    TypeOfTypePoint.TYPE_5 -> student.v5 = point.toString()
                }
            }
            TypePoint.SEMESTER -> student.hk = point.toString()
        }
        notifyAdapter()
    }

    override fun onStop() {
        speechPoint.destroy()
        SharedPrefs.getInstance(requireContext()).put(PREF_STUDENT.format(aClass?.name, subject?.subjectName, semester?.getSemesterName()), students)
        super.onStop()
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

    class StudentsSwapAdapter(
            private val students: ArrayList<Student> = ArrayList()

    ) : BaseRecyclerViewAdapter<Student>(students, R.layout.item_students) {
        var fragment: MainFragment? = null

        fun setMainFragment(mainFragment: MainFragment) {
            fragment = mainFragment
        }

        override fun onBindViewHolder(holder: BaseViewHolder<Student>, position: Int) {
            super.onBindViewHolder(holder, position)
            val student = students[position]

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
    }

    interface OnDismissListener {
        fun onRefreshWhenDismiss(pointValue: Double, typePointValue: TypePoint, position: Int?)
    }
}
