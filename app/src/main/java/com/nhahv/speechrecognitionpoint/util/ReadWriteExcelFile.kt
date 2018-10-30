package com.nhahv.speechrecognitionpoint.util

import com.nhahv.speechrecognitionpoint.data.models.FileExcel
import com.nhahv.speechrecognitionpoint.data.models.Student
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ReadWriteExcelFile {
    private const val rowStart = 7
    private const val MOUTH_POINT_1 = 4
    private const val MOUTH_POINT_2 = 5
    private const val MOUTH_POINT_3 = 6
    private const val MOUTH_POINT_4 = 7
    private const val MOUTH_POINT_5 = 8
    private const val P15_1 = 9
    private const val P15_2 = 10
    private const val P15_3 = 11
    private const val P15_4 = 12
    private const val P15_5 = 13
    private const val WRITE_POINT_1 = 14
    private const val WRITE_POINT_2 = 15
    private const val WRITE_POINT_3 = 16
    private const val WRITE_POINT_4 = 17
    private const val WRITE_POINT_5 = 18
    private const val SEMESTER_POINT = 19
    private const val TBM_POINT = 22


    fun readStudentExcel(path: String): ArrayList<Student> {
        try {
            val workbook = WorkbookFactory.create(File(path))
            val sheet = workbook.getSheetAt(0)
            val dataFormatter = DataFormatter()
            val students = ArrayList<Student>()
            val cell = sheet.getRow(5).getCell(0)
            if (cell.cellType != CellType.STRING){
                return ArrayList()
            }
            if (cell.stringCellValue.trim().toUpperCase() != "STT"){
                return ArrayList()
            }
            for (row in sheet) {
                if (row.rowNum >= rowStart) {
                    val student = Student()
                    for (cell in row) {
                        val cellValue = dataFormatter.formatCellValue(cell)
                        when (cell.columnIndex) {
                            0 -> student.stt = cellValue
                            2 -> student.numberStudent = cellValue
                            3 -> student.name = cellValue
                            MOUTH_POINT_1 -> student.m1 = cellValue
                            MOUTH_POINT_2 -> student.m2 = cellValue
                            MOUTH_POINT_3 -> student.m3 = cellValue
                            MOUTH_POINT_4 -> student.m4 = cellValue
                            MOUTH_POINT_5 -> student.m5 = cellValue
                            P15_1 -> student.p1 = cellValue
                            P15_2 -> student.p2 = cellValue
                            P15_3 -> student.p3 = cellValue
                            P15_4 -> student.p4 = cellValue
                            P15_5 -> student.p5 = cellValue
                            WRITE_POINT_1 -> student.v1 = cellValue
                            WRITE_POINT_2 -> student.v2 = cellValue
                            WRITE_POINT_3 -> student.v3 = cellValue
                            WRITE_POINT_4 -> student.v4 = cellValue
                            WRITE_POINT_5 -> student.v5 = cellValue
                            SEMESTER_POINT -> student.hk = cellValue
                            TBM_POINT -> {
                                try {
                                    student.tbm = cell.numericCellValue.toString()
                                } catch (ex: IllegalStateException) {
                                    student.tbm = ""
                                }
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

    fun writeStudentExcel(excelFile: FileExcel, path: String, name: String, students: ArrayList<Student>): Boolean {
        try {
            val fileWrite = File(path, name)
            if (fileWrite.exists()) {
                fileWrite.delete()
            }
            val workbook = WorkbookFactory.create(File(excelFile.parent, excelFile.nameFile + ".xls"))
            val sheet = workbook.getSheetAt(0)
            for (i in rowStart..sheet.lastRowNum) {
                val student = students[i - rowStart]
                val row = sheet.getRow(i)
                if (student.stt == row.getCell(0).stringCellValue) {
                    if (row.getCell(MOUTH_POINT_1) == null) {
                        row.createCell(MOUTH_POINT_1).setCellValue(student.m1)
                    } else {
                        row.getCell(MOUTH_POINT_1).setCellValue(student.m1)
                    }

                    if (row.getCell(MOUTH_POINT_2) == null) {
                        row.createCell(MOUTH_POINT_2).setCellValue(student.m2)
                    } else {
                        row.getCell(MOUTH_POINT_2).setCellValue(student.m2)
                    }

                    if (row.getCell(MOUTH_POINT_3) == null) {
                        row.createCell(MOUTH_POINT_3).setCellValue(student.m3)
                    } else {
                        row.getCell(MOUTH_POINT_3).setCellValue(student.m3)
                    }
                    if (row.getCell(MOUTH_POINT_4) == null) {
                        row.createCell(MOUTH_POINT_4).setCellValue(student.m4)
                    } else {
                        row.getCell(MOUTH_POINT_4).setCellValue(student.m4)
                    }
                    if (row.getCell(MOUTH_POINT_5) == null) {
                        row.createCell(MOUTH_POINT_5).setCellValue(student.m5)
                    } else {
                        row.getCell(MOUTH_POINT_5).setCellValue(student.m5)
                    }

                    if (row.getCell(P15_1) == null) {
                        row.createCell(P15_1).setCellValue(student.p1)
                    } else {
                        row.getCell(P15_1).setCellValue(student.p1)
                    }
                    if (row.getCell(P15_2) == null) {
                        row.createCell(P15_2).setCellValue(student.p2)
                    } else {
                        row.getCell(P15_2).setCellValue(student.p2)
                    }

                    if (row.getCell(P15_3) == null) {
                        row.createCell(P15_3).setCellValue(student.p3)
                    } else {
                        row.getCell(P15_3).setCellValue(student.p3)
                    }

                    if (row.getCell(P15_4) == null) {
                        row.createCell(P15_4).setCellValue(student.p4)
                    } else {
                        row.getCell(P15_4).setCellValue(student.p4)
                    }

                    if (row.getCell(P15_5) == null) {
                        row.createCell(P15_5).setCellValue(student.p5)
                    } else {
                        row.getCell(P15_5).setCellValue(student.p5)
                    }

                    if (row.getCell(WRITE_POINT_1) == null) {
                        row.createCell(WRITE_POINT_1).setCellValue(student.v1)
                    } else {
                        row.getCell(WRITE_POINT_1).setCellValue(student.v1)
                    }

                    if (row.getCell(WRITE_POINT_2) == null) {
                        row.createCell(WRITE_POINT_2).setCellValue(student.v2)
                    } else {
                        row.getCell(WRITE_POINT_2).setCellValue(student.v2)
                    }

                    if (row.getCell(WRITE_POINT_3) == null) {
                        row.createCell(WRITE_POINT_3).setCellValue(student.v3)
                    } else {
                        row.getCell(WRITE_POINT_3).setCellValue(student.v3)
                    }

                    if (row.getCell(WRITE_POINT_4) == null) {
                        row.createCell(WRITE_POINT_4).setCellValue(student.v4)
                    } else {
                        row.getCell(WRITE_POINT_4).setCellValue(student.v4)
                    }

                    if (row.getCell(WRITE_POINT_5) == null) {
                        row.createCell(WRITE_POINT_5).setCellValue(student.v5)
                    } else {
                        row.getCell(WRITE_POINT_5).setCellValue(student.v5)
                    }

                    if (row.getCell(SEMESTER_POINT) == null) {
                        row.createCell(SEMESTER_POINT).setCellValue(student.hk)
                    } else {
                        row.getCell(SEMESTER_POINT).setCellValue(student.hk)
                    }

                    if (row.getCell(TBM_POINT) == null) {
                        row.createCell(TBM_POINT).setCellValue(student.tbm)
                    } else {
                        row.getCell(TBM_POINT).setCellValue(student.tbm)
                    }
                }
            }
            val fileOut = FileOutputStream(fileWrite)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            return true
        } catch (ex: IOException) {
            ex.printStackTrace()
            return false
        }
    }
}