package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.base.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.data.FileExcel
import com.nhahv.speechrecognitionpoint.util.FileExcelManager
import kotlinx.android.synthetic.main.file_excel_fragment.*
import kotlinx.android.synthetic.main.item_excel_files.view.*
import java.util.*

class FileExcelFragment : Fragment() {

    companion object {
        fun newInstance() = FileExcelFragment()
    }

    private lateinit var viewModel: FileExcelViewModel
    private val excelFiles: ArrayList<FileExcel> = ArrayList()
    private val excelFileAdapter = ExcelFilesAdapter(excelFiles, object : BaseRecyclerViewAdapter.OnItemListener<FileExcel> {
        override fun onClick(item: FileExcel, position: Int) {
            Thread().run {
                item.path?.let {
                    viewModel.importStudent(it)
                }
            }
        }
    })

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

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val view = holder.itemView
            val excelFile = items[position]
            view.nameFile.text = excelFile.nameFile
            view.fileTime.text = excelFile.time
        }
    }

}
