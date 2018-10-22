package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.FileExcel
import com.nhahv.speechrecognitionpoint.data.models.SemesterType
import com.nhahv.speechrecognitionpoint.data.models.Student
import com.nhahv.speechrecognitionpoint.util.Constant.CLASS_NAME
import com.nhahv.speechrecognitionpoint.util.Constant.SEMESTER_PARAM
import com.nhahv.speechrecognitionpoint.util.Constant.SUBJECT_NAME
import com.nhahv.speechrecognitionpoint.util.FileExcelManager
import com.nhahv.speechrecognitionpoint.util.PermissionUtil
import com.nhahv.speechrecognitionpoint.util.ReadWriteExcelFile
import com.nhahv.speechrecognitionpoint.util.SharedPrefs
import com.nhahv.speechrecognitionpoint.util.SharedPrefs.Companion.PREF_STUDENT
import kotlinx.android.synthetic.main.file_excel_fragment.*
import kotlinx.android.synthetic.main.item_excel_files.view.*

class FileExcelFragment : Fragment() {
    val CODE_EXTERNAL_STORAGE = 100

    companion object {
        fun newInstance() = FileExcelFragment().apply {}
    }

    private lateinit var viewModel: FileExcelViewModel
    var className: String? = null
    var subjectName: String? = null
    var semester: SemesterType? = SemesterType.SEMESTER_I

    private val excelFiles: ArrayList<FileExcel> = ArrayList()
    private val excelFileAdapter = ExcelFilesAdapter(excelFiles, object : BaseRecyclerViewAdapter.OnItemListener<FileExcel> {
        override fun onClick(item: FileExcel, position: Int) {
            println("============= ${item.parent}")
            println("=============== ${item.path}")
            println("=============== ${item}")
            Thread().run {
                //                ReadWriteExcelFile.copyFile(item, PATH_APP, "TEST_DIEM.xls")

                item.path?.let {
                    val students: ArrayList<Student> = ReadWriteExcelFile.readStudentExcel(it)
                    SharedPrefs.getInstance(requireContext()).put(PREF_STUDENT.format(className, subjectName, semester?.getSemesterName()), students)
//                    requireActivity().finish()
                    (requireActivity() as MainActivity).back()


                }
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            className = it.getString(CLASS_NAME)
            subjectName = it.getString(SUBJECT_NAME)
            semester = SemesterType.SEMESTER_I
        }
        (requireActivity() as MainActivity).readExcel()
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
        excelFiles.addAll(FileExcelManager.getExcelFiles())
        excelFileAdapter.notifyDataSetChanged()
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
}
