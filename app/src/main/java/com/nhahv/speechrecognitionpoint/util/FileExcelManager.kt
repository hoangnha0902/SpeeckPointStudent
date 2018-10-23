package com.nhahv.speechrecognitionpoint.util

import android.os.Environment
import com.nhahv.speechrecognitionpoint.data.models.FileExcel


import java.io.File
import java.util.ArrayList

object FileExcelManager {
    private const val SDCARD = "/storage/sdcard1/"
    private const val FILE_EXTENSION = ".xls"

    fun getExcelFiles(): ArrayList<FileExcel> {
        val excelFiles = ArrayList<FileExcel>()

        // get all file in external sdcard
        val fileExternal = File(SDCARD).listFiles()
        if (fileExternal != null) {
            for (file in fileExternal) {
                recursiveScan(file, excelFiles)
            }
        }

        // get all file xls in internal
        val fileInternal = Environment.getExternalStorageDirectory().listFiles()
        if (fileInternal != null) {
            for (file in fileInternal) {
                recursiveScan(file, excelFiles)
            }
        }
        return excelFiles
    }


    private fun recursiveScan(file: File, excels: ArrayList<FileExcel>) {
        if (file.isFile) {
            if (file.path.endsWith(FILE_EXTENSION)) {
                excels.add(FileExcel(file.path, file.parent, file.lastModified()))
            }
            return
        }
        val files = file.listFiles() ?: return
        for (item in files) {
            recursiveScan(item, excels)
        }
    }
}
