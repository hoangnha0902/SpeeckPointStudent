package com.nhahv.speechrecognitionpoint.ui.fileexcel

import android.arch.lifecycle.ViewModel
import com.nhahv.speechrecognitionpoint.data.model.Student
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.IOException

class FileExcelViewModel : ViewModel() {
    val TAG = FileExcelViewModel::class.java.simpleName
    fun importStudentApache(path: String): ArrayList<Student> {
        try {
            val workbook = WorkbookFactory.create(File(path))
            val sheet = workbook.getSheetAt(0)
            val dataFormatter = DataFormatter()
            val students = ArrayList<Student>()
            for (row in sheet) {
                if (row.rowNum >= 7) {
                    val student = Student()
                    for (cell in row) {
                        val cellValue = dataFormatter.formatCellValue(cell)
                        when (cell.columnIndex) {
                            0 -> student.stt = cellValue
                            2 -> student.numberStudent = cellValue
                            3 -> student.name = cellValue
                            4 -> student.m1 = cellValue
                            5 -> student.m2 = cellValue
                            6 -> student.m3 = cellValue
                            7 -> student.m4 = cellValue
                            8 -> student.m5 = cellValue
                            9 -> student.p1 = cellValue
                            10 -> student.p2 = cellValue
                            11 -> student.p3 = cellValue
                            12 -> student.p4 = cellValue
                            13 -> student.p5 = cellValue
                            14 -> student.v1 = cellValue
                            15 -> student.v2 = cellValue
                            16 -> student.v3 = cellValue
                            17 -> student.v4 = cellValue
                            18 -> student.v5 = cellValue
                            19 -> student.hk = cellValue
                            22 -> {
                                student.tbm = cell.numericCellValue.toString()
                            }
                        }
                    }
                    students.add(student)
                }
            }
            workbook.close()
            return students
        } catch (e: IOException) {
            e.printStackTrace()
            return ArrayList()
        }
    }
}
