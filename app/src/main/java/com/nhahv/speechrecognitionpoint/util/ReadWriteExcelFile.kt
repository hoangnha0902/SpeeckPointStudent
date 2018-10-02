package com.nhahv.speechrecognitionpoint.util

import com.nhahv.speechrecognitionpoint.data.models.FileExcel
import com.nhahv.speechrecognitionpoint.data.models.Student
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

    fun writeStudentExcel(path: String, students: ArrayList<Student>): Boolean {
        try {
            val workbook = WorkbookFactory.create(File(path))
            val sheet = workbook.getSheetAt(0)
            for (i in rowStart..sheet.lastRowNum) {
                val student = students[i - rowStart]
                val row = sheet.getRow(i)
                if (student.stt.toDouble() == row.getCell(0).numericCellValue) {
                    if (row.getCell(MOUTH_POINT_1) == null) {
                        row.createCell(MOUTH_POINT_1).setCellValue(student.m1.toDouble())
                    } else {
                        row.getCell(MOUTH_POINT_1).setCellValue(student.m1.toDouble())
                    }

                    if (row.getCell(MOUTH_POINT_2) == null) {
                        row.createCell(MOUTH_POINT_2).setCellValue(student.m2.toDouble())
                    } else {
                        row.getCell(MOUTH_POINT_2).setCellValue(student.m2.toDouble())
                    }

                    if (row.getCell(MOUTH_POINT_3) == null) {
                        row.createCell(MOUTH_POINT_3).setCellValue(student.m3.toDouble())
                    } else {
                        row.getCell(MOUTH_POINT_3).setCellValue(student.m3.toDouble())
                    }
                    if (row.getCell(MOUTH_POINT_4) == null) {
                        row.createCell(MOUTH_POINT_4).setCellValue(student.m4.toDouble())
                    } else {
                        row.getCell(MOUTH_POINT_4).setCellValue(student.m4.toDouble())
                    }
                    if (row.getCell(MOUTH_POINT_5) == null) {
                        row.createCell(MOUTH_POINT_5).setCellValue(student.m5.toDouble())
                    } else {
                        row.getCell(MOUTH_POINT_5).setCellValue(student.m5.toDouble())
                    }

                    if (row.getCell(P15_1) == null) {
                        row.createCell(P15_1).setCellValue(student.p1.toDouble())
                    } else {
                        row.getCell(P15_1).setCellValue(student.p1.toDouble())
                    }
                    if (row.getCell(P15_2) == null) {
                        row.createCell(P15_2).setCellValue(student.p2.toDouble())
                    } else {
                        row.getCell(P15_2).setCellValue(student.p2.toDouble())
                    }

                    if (row.getCell(P15_3) == null) {
                        row.createCell(P15_3).setCellValue(student.p3.toDouble())
                    } else {
                        row.getCell(P15_3).setCellValue(student.p3.toDouble())
                    }

                    if (row.getCell(P15_4) == null) {
                        row.createCell(P15_4).setCellValue(student.p4.toDouble())
                    } else {
                        row.getCell(P15_4).setCellValue(student.p4.toDouble())
                    }

                    if (row.getCell(P15_5) == null) {
                        row.createCell(P15_5).setCellValue(student.p5.toDouble())
                    } else {
                        row.getCell(P15_5).setCellValue(student.p5.toDouble())
                    }

                    if (row.getCell(WRITE_POINT_1) == null) {
                        row.createCell(WRITE_POINT_1).setCellValue(student.v1.toDouble())
                    } else {
                        row.getCell(WRITE_POINT_1).setCellValue(student.v1.toDouble())
                    }

                    if (row.getCell(WRITE_POINT_2) == null) {
                        row.createCell(WRITE_POINT_2).setCellValue(student.v2.toDouble())
                    } else {
                        row.getCell(WRITE_POINT_2).setCellValue(student.v2.toDouble())
                    }

                    if (row.getCell(WRITE_POINT_3) == null) {
                        row.createCell(WRITE_POINT_3).setCellValue(student.v3.toDouble())
                    } else {
                        row.getCell(WRITE_POINT_3).setCellValue(student.v3.toDouble())
                    }

                    if (row.getCell(WRITE_POINT_4) == null) {
                        row.createCell(WRITE_POINT_4).setCellValue(student.v4.toDouble())
                    } else {
                        row.getCell(WRITE_POINT_4).setCellValue(student.v4.toDouble())
                    }

                    if (row.getCell(WRITE_POINT_5) == null) {
                        row.createCell(WRITE_POINT_5).setCellValue(student.v5.toDouble())
                    } else {
                        row.getCell(WRITE_POINT_5).setCellValue(student.v5.toDouble())
                    }

                    if (row.getCell(SEMESTER_POINT) == null) {
                        row.createCell(SEMESTER_POINT).setCellValue(student.hk.toDouble())
                    } else {
                        row.getCell(SEMESTER_POINT).setCellValue(student.hk.toDouble())
                    }

                    if (row.getCell(TBM_POINT) == null) {
                        row.createCell(TBM_POINT).setCellValue(student.tbm.toDouble())
                    } else {
                        row.getCell(TBM_POINT).setCellValue(student.tbm.toDouble())
                    }
                }

            }
            val fileOut = FileOutputStream(path)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            return true
        } catch (ex: IOException) {
            ex.printStackTrace()
            return false
        }
    }

    fun renameExcelFile(excelFile: FileExcel, name: String) {
        var fileFrom = File(excelFile.parent, excelFile.nameFile + ".xls")
        var fileTo = File(excelFile.parent, name)
        var isRenameFile = fileFrom.renameTo(fileTo)
        fileFrom.copyTo(fileTo, true)
        println("=================== $isRenameFile")
    }
}