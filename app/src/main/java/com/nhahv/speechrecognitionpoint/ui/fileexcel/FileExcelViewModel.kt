package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.nhahv.speechrecognitionpoint.data.model.Student
import jxl.Workbook
import jxl.WorkbookSettings
import java.io.FileInputStream
import java.io.IOException
import java.util.*

class FileExcelViewModel : ViewModel() {
    val TAG = FileExcelViewModel::class.java.simpleName

    fun importStudent(path: String) {
        try {
            val ws = WorkbookSettings()
            ws.locale = Locale("vi", "VN")
            ws.encoding = "UTF-8"
            val inputStream = FileInputStream(path)
            val workbook = Workbook.getWorkbook(inputStream, ws)
            val sheet = workbook.getSheet(0)
            val row = sheet.getColumn(0).size
            if (sheet.getCell(0, 1).contents == null) {
                return
            }
            for (i in 7..row) {
                val item = Student(sheet.getCell(0, i).contents.toInt(),
                        sheet.getCell(2, i).contents,
                        sheet.getCell(3, i).contents,
                        sheet.getCell(4, i).contents.toDouble(),
                        sheet.getCell(5, i).contents.toDouble(),
                        sheet.getCell(6, i).contents.toDouble(),
                        sheet.getCell(7, i).contents.toDouble(),
                        sheet.getCell(7, i).contents.toDouble(),
                        sheet.getCell(9, i).contents.toDouble(),
                        sheet.getCell(10, i).contents.toDouble(),
                        sheet.getCell(11, i).contents.toDouble(),
                        sheet.getCell(12, i).contents.toDouble(),
                        sheet.getCell(13, i).contents.toDouble(),
                        sheet.getCell(14, i).contents.toDouble(),
                        sheet.getCell(15, i).contents.toDouble(),
                        sheet.getCell(16, i).contents.toDouble(),
                        sheet.getCell(17, i).contents.toDouble(),
                        sheet.getCell(18, i).contents.toDouble(),
                        sheet.getCell(19, i).contents.toDouble(),
                        sheet.getCell(22, i).contents.toDouble()
                )

                println("$item")
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            Log.d(TAG, "import error BiffException ")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "import error IOException ")
        }
    }
}
