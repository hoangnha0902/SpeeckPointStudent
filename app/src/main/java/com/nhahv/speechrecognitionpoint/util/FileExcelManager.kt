package com.nhahv.speechrecognitionpoint.util

import android.os.Environment
import android.util.Log
import com.nhahv.speechrecognitionpoint.data.models.FileExcel


import java.io.File
import java.util.ArrayList

object FileExcelManager {
    private val mListFileExcelSdcard = ArrayList<FileExcel>()
    private val mListFileExcelInternal = ArrayList<FileExcel>()
    private val TAG = FileExcelManager.javaClass.simpleName

    fun getExcelFile() {
        getImagesExternal("/storage/sdcard1/")
        getImagesInternal()
    }

    fun getExcelFiles(): ArrayList<FileExcel> {
        val list: ArrayList<FileExcel> = ArrayList()
        list.addAll(mListFileExcelInternal)
        list.addAll(mListFileExcelSdcard)
        return list
    }

    private fun getImagesExternal(pathFile: String) {
        val file = File(pathFile).listFiles() ?: return
        mListFileExcelSdcard.clear()
        for (f in file) {
            recursiveScan(f)
        }
    }

    private fun getImagesInternal() {
        val file = Environment.getExternalStorageDirectory().listFiles()
        Log.d(TAG, Environment.getExternalStorageDirectory().path)
        if (file == null) {
            return
        }
        Log.d(TAG, Environment.getExternalStorageDirectory().absoluteFile.path)
        mListFileExcelInternal.clear()
        for (f in file) {
            recursiveScanInternal(f)
        }
    }

    private fun recursiveScan(f: File) {
        if (f.isFile) {
            if (f.path.endsWith(".xls")) {
                mListFileExcelSdcard
                        .add(FileExcel(f.path, f.parent, f.lastModified()))
            }
            return
        }
        val file = f.listFiles() ?: return
        for (item in file) {
            recursiveScan(item)
        }
    }

    private fun recursiveScanInternal(f: File) {
        if (f.isFile) {
            if (f.path.endsWith(".xls")) {
                mListFileExcelInternal
                        .add(FileExcel(f.path, f.parent, f.lastModified()))
            }
            return
        }
        val file = f.listFiles() ?: return
        for (item in file) {
            recursiveScanInternal(item)
        }
    }

    val listFileExcelSdcard: List<FileExcel>
        get() = mListFileExcelSdcard

    val listFileExcelInternal: List<FileExcel>
        get() = mListFileExcelInternal
}
