package com.nhahv.speechrecognitionpoint.ui.excel

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.nhahv.speechrecognitionpoint.BaseRecyclerAdapter
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.*
import com.nhahv.speechrecognitionpoint.util.*
import com.nhahv.speechrecognitionpoint.util.Constant.CLASSES
import com.nhahv.speechrecognitionpoint.util.Constant.NAME_STUDENT_OF_SUBJECT
import com.nhahv.speechrecognitionpoint.util.Constant.NAME_SUBJECT_LIST
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECTS
import kotlinx.android.synthetic.main.file_excel_fragment.*
import kotlinx.android.synthetic.main.item_excel_files.view.*
import java.util.*
import kotlin.concurrent.schedule

class ExcelFragment : Fragment() {
    private lateinit var viewModel: ExcelViewModel
    private var aClass: AClass? = null
    private var subject: Subject? = null
    var semester: SemesterType? = SemesterType.SEMESTER_I

    private val excelFiles: ArrayList<FileExcel> = ArrayList()
    private val excelFileAdapter = ExcelFilesAdapter(excelFiles) { _, excelFile, _ ->
        if (aClass == null || TextUtils.isEmpty(aClass?.name)) {
            toast("Không thể import bảng điểm kiểm tra lại lớp học và môn học")
            return@ExcelFilesAdapter
        }
        val value = getStudentList()
        aClass?.name?.let {
            if (value.size > 0) {
                showDialogConfirmImport(excelFile, it)
            } else {
                readFileExcel(excelFile, it)
            }
        }
    }

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
        viewModel = obtainViewModel(ExcelViewModel::class.java, "")
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
        excelFileAdapter.notifyDataSetChanged()
    }

    class ExcelFilesAdapter(
            items: ArrayList<FileExcel>,
            listener: ((View, FileExcel, Int) -> Unit)?)
        : BaseRecyclerAdapter<FileExcel>(items, R.layout.item_excel_files, listener) {
        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val view = holder.itemView
            val excelFile = items[position]
            view.nameFile.text = excelFile.nameFile
            view.fileTime.text = excelFile.time
        }
    }

    private fun updateSubjects(subject: Subject?, excelFile: String) {
        if (subject == null) {
            toast("Cập nhật lớp học thất bại")
            return
        }
        val value = sharePrefs().get(NAME_SUBJECT_LIST(requireContext(), aClass?.name), "")
        if (TextUtils.isEmpty(value)) {
            toast("Cập nhật lớp học thất bại")
            return
        }
        val subjects = Gson().fromJson<ArrayList<Subject>>(value)
        var isChanged = false
        for (item in subjects) {
            if (convertCompare(item.subjectName) == convertCompare(subject.subjectName)) {
                item.excelFile = excelFile
                isChanged = true
            }
        }
        if (isChanged) {
            sharePrefs().put(NAME_SUBJECT_LIST(requireContext(), aClass?.name), subjects)
        }
    }

    private fun showDialogConfirmImport(excelFile: FileExcel, nameClass: String) {
        AlertDialog.Builder(requireContext())
                .setTitle("Import danh sách học sinh")
                .setMessage("Môn học đã có danh sách học sinh, bạn có muốn tiếp tục import hay không")
                .setPositiveButton("Có") { dialog, which ->
                    dialog.dismiss()
                    readFileExcel(excelFile, nameClass)
                }
                .setNegativeButton("Không") { dialog, which ->
                    dialog.dismiss()
                }.show()

    }

    private fun readFileExcel(excelFile: FileExcel?, nameClass: String) {
        Thread().run {
            excelFile?.path?.let {
                (requireActivity() as MainActivity).showProgress()
                val students: ArrayList<Student> = ReadWriteExcelFile.readStudentExcel(it, nameClass) { importStatus ->
                    when (importStatus) {
                        ReadWriteExcelFile.ImportStatus.SUCCESS -> {
                            toast("Import bảng điểm thành công")
                        }
                        ReadWriteExcelFile.ImportStatus.ERROR_FILE -> {
                            toast("File bảng điểm có định dạng không đúng, kiểm tra lại file Excel")
                        }
                        ReadWriteExcelFile.ImportStatus.CLASS_NAME -> {
                            toast("Lớp trong File Excel không đúng với lớp học")
                        }
                        ReadWriteExcelFile.ImportStatus.IMPORT_ERROR -> {
                            toast("Import không thành công, kiểm tra lại định dạng File Excel")
                        }
                    }
                }
                if (students.isEmpty()) {
                    Timer().schedule(1000) {
                        (requireActivity() as MainActivity).hideProgress()
                    }
                    return@let
                }
                // copy file
                val nameFile = "${subject?.subjectName}_${aClass?.name}_${subject?.semester?.getSemesterName()}_${aClass?.year}.xls"
                ReadWriteExcelFile.copyFileExcel(it, nameFile)
                updateSubjects(subject, nameFile)
                sharePrefs().put(NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, subject?.subjectName, semester), students)
                Timer().schedule(1000) {
                    (requireActivity() as MainActivity).hideProgress()
                    (requireActivity() as MainActivity).back()
                }
            }
        }
    }

    private fun getStudentList(): ArrayList<Student> {
        val value = sharePrefs().get(Constant.NAME_STUDENT_OF_SUBJECT(requireContext(), aClass?.name, subject?.subjectName, semester), "")
        if (value.isEmpty()) {
            return ArrayList()
        }
        return Gson().fromJson<ArrayList<Student>>(value)
    }
}
