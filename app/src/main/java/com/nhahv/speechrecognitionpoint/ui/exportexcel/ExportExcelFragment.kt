package com.nhahv.speechrecognitionpoint.ui.exportexcel

import androidx.lifecycle.ViewModelProviders
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nhahv.speechrecognitionpoint.BaseRecyclerViewAdapter
import com.nhahv.speechrecognitionpoint.MainActivity
import com.nhahv.speechrecognitionpoint.R
import com.nhahv.speechrecognitionpoint.data.models.Folder
import com.nhahv.speechrecognitionpoint.util.setUpToolbar
import kotlinx.android.synthetic.main.export_excel_fragment.*
import kotlinx.android.synthetic.main.item_export_excel.view.*
import java.io.File
import java.util.*

class ExportExcelFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = ExportExcelFragment()
    }

    val FOLDER_STORAGE_INTERNAL = "/storage/emulated"

    private lateinit var viewModel: ExportExcelViewModel
    private val folders = ArrayList<Folder>()
    private val adapter = ExportAdapter(folders)
    private var pathFolder: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val folder = folder(Environment.getExternalStorageDirectory().path)
        if (folder != null) {
            pathFolder = folder.path
            folders.add(folder)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.export_excel_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExportExcelViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(toolbar, "Chọn đường dẫn Export bảng điểm")
        excelExportList.adapter = adapter
        adapter.listener = object : BaseRecyclerViewAdapter.OnItemListener<Folder> {
            override fun onClick(item: Folder, position: Int) {
                val path = item.path
                val file = File(path)
                if (file.isFile) return
                if (file.isDirectory) {
                    pathFolder = file.path
                    Log.d(TAG, "path = " + file.path)
                    folders.clear()
                    for (itemFolder in item.getPathChild()) {
                        val folder1 = folder(itemFolder)
                        if (folder1 != null) {
                            folders.add(folder1)
                        }
                    }
                    folders.sort()
                    adapter.notifyDataSetChanged()
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun onBackPress() {
        Log.d(TAG, "mPathFolder = $pathFolder")
        if (pathFolder == FOLDER_STORAGE_INTERNAL) {
            Log.d(TAG, "mPathFolder$pathFolder")
            Navigation.findNavController(requireActivity(), R.id.navLoginHost).popBackStack()
            return
        }
        val path = File(pathFolder).parent
        if (path == FOLDER_STORAGE_INTERNAL) {
            pathFolder = path
            Log.d(TAG, "mPathFolder$pathFolder")
            folders.clear()
            val folder = folder(Environment.getExternalStorageDirectory().path)
            if (folder != null) {
                folders.add(folder)
            }
            folders.sort()
            adapter.notifyDataSetChanged()
            return
        }
        refresh(path)
    }

    private fun refresh(path: String) {
        Log.d(TAG, "path = $path")
        val folder = folder(path)
        if (folder == null) {
            (requireActivity() as MainActivity).back()
            return
        }
        pathFolder = path
        folders.clear()
        for (itemFolder in folder.getPathChild()) {
            val folder1 = folder(itemFolder)
            if (folder1 != null) {
                folders.add(folder1)
            }
            folders.sort()
            adapter.notifyDataSetChanged()
        }
    }

    class ExportAdapter(items: ArrayList<Folder>
    ) : BaseRecyclerViewAdapter<Folder>(items, R.layout.item_export_excel) {

        override fun onBindViewHolder(holder: BaseViewHolder<Folder>, position: Int) {
            super.onBindViewHolder(holder, position)
            val folder = items[position]
            holder.itemView.apply {
                folderName.text = folder.name
                if (checkFile(folder.path)) {
                    imgFolder.setImageResource(R.drawable.ic_file_excel)
                } else {
                    imgFolder.setImageResource(R.drawable.ic_folder)
                }
                folderNumberItem.text = "${folder.numberItem} item"
            }
        }

        private fun checkFile(path: String?): Boolean {
            return File(path).isFile
            return File(path).isFile
        }
    }

    fun folder(path: String): Folder? {
        val file = File(path)
        val pathChild = java.util.ArrayList<String>()
        val pathFile = file.path
        val parent = file.parent
        var isFolder = false
        var name = pathFile.substring(pathFile.lastIndexOf("/") + 1)
        if (name == "sdcard1")
            name = "Bộ nhớ ngoài"
        else if (name == "0") name = "Bộ nhớ trong"
        if (name.startsWith(".")) return null
        if (file.isFile)
            pathChild.clear()
        else if (file.isDirectory) {
            val listChild = file.listFiles()
            if (listChild != null) {
                isFolder = true
                for (item in listChild) pathChild.add(item.path)
            }
        }
        return Folder(name, pathFile, pathChild, parent, isFolder)
    }
}
