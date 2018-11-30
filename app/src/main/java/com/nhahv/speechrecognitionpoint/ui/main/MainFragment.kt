package com.nhahv.speechrecognitionpoint.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.StudentsAdapter
import com.nhahv.speechrecognitionpoint.StudentsSwapAdapter
import com.nhahv.speechrecognitionpoint.data.models.*
import com.nhahv.speechrecognitionpoint.ui.pointInput.PointInputFragment
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.IS_LOGIN
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    var semester: SemesterType? = SemesterType.SEMESTER_I
    var indexChange: Int = -1
    var isPointing: Boolean = false

    var subject: Subject? = null
    var aClass: AClass? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var speechPoint: SpeechPoint
    private val students = ArrayList<Student>()

    private val studentRestore = ArrayList<Student>()


    private val studentSwapAdapter = StudentsSwapAdapter(students)

    val typePointList = ArrayList<TypePoint>()
    var typePoint: TypePoint = TypePoint.MOUTH
    val typeOfPointList = ArrayList<TypeOfTypePoint>()
    var typeOfPoint: TypeOfTypePoint = TypeOfTypePoint.TYPE_1
    lateinit var title: String
    private val studentAdapter by lazy {
        StudentsAdapter(students, typePoint, typeOfPoint) { view, student, i -> }
    }

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
        studentRestore.addAll(getStudentList())
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
            R.id.logout -> {
                SharedPrefs.getInstance(requireContext()).put(IS_LOGIN, false)
                navigateClearStack(R.id.action_mainFragment_to_loginFragment)
            }
            R.id.deletePointAllStudent -> {
                AlertDialog.Builder(requireContext())
                        .setTitle("Xóa điểm của tất cả học sinh")
                        .setMessage("Xóa ${typeOfPoint.toString()}  của điểm ${typePoint.toString()} của tất cả học sinh")
                        .setPositiveButton("OK") { dialog, which ->
                            deletePointAllStudent()
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
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
        studentAdapter.setOnRestoreListener { swipeLayout, student, i ->
            restorePointStudent(student, i)
            notifyAdapter()
        }
        studentAdapter.setOnDeleteListener { swipeLayout, student, i ->
            AlertDialog.Builder(requireContext())
                    .setTitle("Xóa điểm của học sinh ${student.name}")
                    .setMessage("Xóa ${typeOfPoint.toString()}  của điểm ${typePoint.toString()}")
                    .setPositiveButton("OK") { dialog, which ->
                        deletePointOfStudent(student)
                        notifyAdapter()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
        }

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
                speechPoint.cancel()
                textSpeech.text = ""
                startMic.setImageResource(R.mipmap.ic_mic_white_24dp)
            }
        }

        swap.setOnClickListener {
            studentList.visibility = if (studentList.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            studentListSwap.visibility = if (studentListSwap.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        textSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                studentSwapAdapter.filter.filter(s.toString())
                studentAdapter.filter.filter(s.toString())

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
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
        typePointList.clear()
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
        typeOfPointList.clear()
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
        val value = sharePrefs().get(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, subject?.subjectName, semester), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Student>>(value)
    }

    // TODO word receiver from Speech
    fun onTextRecognition(matches: String) {
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
            dialog.setOnDismissListener { pointValue, typePointValue, position ->
                writePointToStudentWidthInput(pointValue, typePointValue, position)
            }
        }
    }

    private fun writePointToStudent(point: Double) {
        if (indexChange == -1) return
        val student = students[indexChange]
        isPointing = false
        indexChange = -1
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.m1 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.m2 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.m3 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.m4 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.m5 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.p1 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.p2 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.p3 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.p4 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.p5 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.v1 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.v2 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.v3 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.v4 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.v5 = point.toString()
            student.getTBM()
            notifyAdapter()
            return
        }
        if (typePoint == TypePoint.SEMESTER) {
            student.hk = point.toString()
            student.getTBM()
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
        student.getTBM()
        notifyAdapter()
    }

    private fun restorePointStudent(student: Student, position: Int) {
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.m1 = studentRestore[position].m1
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.m2 = studentRestore[position].m2
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.m3 = studentRestore[position].m3
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.m4 = studentRestore[position].m4
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.m5 = studentRestore[position].m5
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.p1 = studentRestore[position].p1
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.p2 = studentRestore[position].p2
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.p3 = studentRestore[position].p3
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.p4 = studentRestore[position].p4
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.p5 = studentRestore[position].p5
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.v1 = studentRestore[position].v1
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.v2 = studentRestore[position].v2
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.v3 = studentRestore[position].v3
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.v4 = studentRestore[position].v4
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.v5 = studentRestore[position].v5
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.SEMESTER) {
            student.hk = studentRestore[position].hk
            student.getTBM()
            return
        }
    }


    private fun deletePointAllStudent() {
        students.forEach { it -> deletePointOfStudent(it) }
        notifyAdapter()
    }

    private fun deletePointOfStudent(student: Student) {
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.m1 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.m2 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.m3 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.m4 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.MOUTH && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.m5 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.p1 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.p2 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.p3 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.p4 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.P15 && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.p5 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_1) {
            student.v1 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_2) {
            student.v2 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_3) {
            student.v3 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_4) {
            student.v4 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.WRITE && typeOfPoint == TypeOfTypePoint.TYPE_5) {
            student.v5 = ""
            student.getTBM()
            return
        }
        if (typePoint == TypePoint.SEMESTER) {
            student.hk = ""
            student.getTBM()
            return
        }
    }

    override fun onStop() {
        speechPoint.cancel()
        sharePrefs().put(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, subject?.subjectName, semester), students)
        super.onStop()
    }
}
