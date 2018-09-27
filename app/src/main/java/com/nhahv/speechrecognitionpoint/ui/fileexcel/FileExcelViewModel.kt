package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.nhahv.speechrecognitionpoint.data.model.Student
import jxl.Workbook
import jxl.WorkbookSettings
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class FileExcelViewModel : ViewModel() {
    val TAG = FileExcelViewModel::class.java.simpleName

    fun importStudent(path: String): ArrayList<Student> {
        val students: ArrayList<Student> = ArrayList()
        try {
            val ws = WorkbookSettings()
            ws.locale = Locale("vi", "VN")
            ws.encoding = "UTF-8"
            val inputStream = FileInputStream(path)
            val workbook = Workbook.getWorkbook(inputStream, ws)
            val sheet = workbook.getSheet(0)
            val row = sheet.getColumn(0).size
            if (sheet.getCell(0, 1).contents == null) {
                return students
            }
            for (i in 7..row) {
                if (sheet.getCell(0, i).contents == null) {
                    return students
                }
                val student = Student(sheet.getCell(0, i).contents.toInt(),
                        sheet.getCell(2, i).contents,
                        sheet.getCell(3, i).contents,
                        sheet.getCell(4, i).contents,
                        sheet.getCell(5, i).contents,
                        sheet.getCell(6, i).contents,
                        sheet.getCell(7, i).contents,
                        sheet.getCell(7, i).contents,
                        sheet.getCell(9, i).contents,
                        sheet.getCell(10, i).contents,
                        sheet.getCell(11, i).contents,
                        sheet.getCell(12, i).contents,
                        sheet.getCell(13, i).contents,
                        sheet.getCell(14, i).contents,
                        sheet.getCell(15, i).contents,
                        sheet.getCell(16, i).contents,
                        sheet.getCell(17, i).contents,
                        sheet.getCell(18, i).contents,
                        sheet.getCell(19, i).contents,
                        sheet.getCell(22, i).contents
                )
                students.add(student)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            Log.d(TAG, "import error BiffException ")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "import error IOException ")
        } finally {
            return students
        }
    }
}
