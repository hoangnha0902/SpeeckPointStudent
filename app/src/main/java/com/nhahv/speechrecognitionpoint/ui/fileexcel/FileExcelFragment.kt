package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.*
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_SUBJECT
import kotlinx.android.synthetic.main.file_excel_fragment.*
import kotlinx.android.synthetic.main.item_excel_files.view.*

class FileExcelFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewModel: FileExcelViewModel
    private var aClass: AClass? = null
    private var subject: Subject? = null
    var semester: SemesterType? = SemesterType.SEMESTER_I

    private val excelFiles: ArrayList<FileExcel> = ArrayList()
    private val excelFileAdapter = ExcelFilesAdapter(excelFiles, object : BaseRecyclerViewAdapter.OnItemListener<FileExcel> {
        override fun onClick(item: FileExcel, position: Int) {
            Thread().run {
                item.path?.let {
                    val students: ArrayList<Student> = ReadWriteExcelFile.readStudentExcel(it)
                    updateSubjects(subject, item)
                    SharedPrefs.getInstance(requireContext()).put(PREF_STUDENT.format(aClass?.name, subject?.subjectName, semester?.getSemesterName()), students)
                    (requireActivity() as MainActivity).back()
                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            aClass = it.getParcelable(CLASSES)
            subject = it.getParcelable(SUBJECTS)
            semester = subject?.semester
        }
        (requireActivity() as MainActivity).readExcel { excelFiles.addAll(FileExcelManager.getExcelFiles()) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.file_excel_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FileExcelViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        excelFileList.adapter = excelFileAdapter
        excelFileAdapter.notifyDataSetChanged()
        swipeRefresh.setOnRefreshListener {
            (requireActivity() as MainActivity).readExcel {
                getExcelList()
                excelFileAdapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    fun getExcelList() {
        excelFiles.clear()
        excelFiles.addAll(FileExcelManager.getExcelFiles())
    }

    class ExcelFilesAdapter(
            items: ArrayList<FileExcel>,
            listener: BaseRecyclerViewAdapter.OnItemListener<FileExcel>)
        : BaseRecyclerViewAdapter<FileExcel>(items, R.layout.item_excel_files, listener) {

        override fun onBindViewHolder(holder: BaseViewHolder<FileExcel>, position: Int) {
            super.onBindViewHolder(holder, position)
            val view = holder.itemView
            val excelFile = items[position]
            view.nameFile.text = excelFile.nameFile
            view.fileTime.text = excelFile.time
        }
    }

    private fun updateSubjects(subject: Subject?, excelFile: FileExcel) {
        if (subject == null) {
            toast("Cập nhật lớp học thất bại")
            return
        }
        val value = sharePrefs().get(PREF_SUBJECT.format(aClass?.name), "")
        if (TextUtils.isEmpty(value)) {
            toast("Cập nhật lớp học thất bại")
            return
        }
        val subjects = Gson().fromJson<ArrayList<Subject>>(value)
        var isChanged = false
        for (item in subjects) {
            if (convertCompare(item.subjectName) == convertCompare(subject.subjectName)) {
                item.excel = excelFile
                isChanged = true
            }
        }
        if (isChanged) {
            sharePrefs().put(PREF_SUBJECT.format(aClass?.name), subjects)
        }
    }
}
