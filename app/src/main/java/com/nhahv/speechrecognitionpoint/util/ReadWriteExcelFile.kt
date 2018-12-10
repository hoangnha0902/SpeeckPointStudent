package com.nhahv.speechrecognitionpoint.util

import android.os.Environment
import android.text.TextUtils
import com.nhahv.speechrecognitionpoint.data.models.MarmotExam
import com.nhahv.speechrecognitionpoint.data.models.MarmotExamItem
import com.nhahv.speechrecognitionpoint.data.models.Student
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
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

    private const val ROW_CLASS = 3
    private const val CELL_CLASS = 0

    private const val TITLE_EXCEL = "BẢNG ĐIỂM MÔN %s HKI  LỚP 10A1\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n"
    private const val FOLDER_NAME = "/speech_point_student"
    private var PATH_FILE = "${Environment.getExternalStorageDirectory()}$FOLDER_NAME/%s"


    private const val MARMOT_FIRST_CHECK = "Mã phách"
    private const val MARMOT_SECOND_CHECK = "Số thí sinh"


    enum class ImportStatus {
        ERROR_FILE, CLASS_NAME, IMPORT_ERROR, SUCCESS
    }

    fun readStudentExcel(path: String, name: String, listener: ((ImportStatus) -> Unit)): ArrayList<Student> {
        try {
            val workbook = WorkbookFactory.create(File(path))
            val sheet = workbook.getSheetAt(0)
            val dataFormatter = DataFormatter()
            val students = ArrayList<Student>()

            val cellClass = sheet.getRow(ROW_CLASS).getCell(CELL_CLASS)
            if (cellClass.cellType != CellType.STRING) {
                listener.invoke(ImportStatus.ERROR_FILE)
                return ArrayList()
            }

            val stringTemp = cellClass.stringCellValue.trim().toUpperCase()
            val isContainer = stringTemp.contains(name.trim().toUpperCase())
            if (!isContainer) {
                listener.invoke(ImportStatus.CLASS_NAME)
                return ArrayList()
            }

            val cell = sheet.getRow(5).getCell(0)
            if (cell.cellType != CellType.STRING) {
                listener.invoke(ImportStatus.ERROR_FILE)
                return ArrayList()
            }
            if (cell.stringCellValue.trim().toUpperCase() != "STT") {
                listener.invoke(ImportStatus.ERROR_FILE)
                return ArrayList()
            }
            for (row in sheet) {
                if (row.rowNum >= rowStart && row.rowNum < sheet.lastRowNum) {
                    val student = Student()
                    for (cellTemp in row) {
                        val cellValue = dataFormatter.formatCellValue(cellTemp)
                        when (cellTemp.columnIndex) {
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
                                    student.tbm = cellTemp.numericCellValue.toString()
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
            listener.invoke(ImportStatus.SUCCESS)
            return students
        } catch (e: IOException) {
            e.printStackTrace()
            listener.invoke(ImportStatus.IMPORT_ERROR)
            return ArrayList()
        }
    }

    fun writeStudentExcel(excelFile: String, path: String, students: ArrayList<Student>, subjectName: String?): Boolean {
        try {
            val fileWrite = File(path, excelFile)
            if (fileWrite.exists()) {
                fileWrite.delete()
            }
            val pathFile = PATH_FILE.format(excelFile)
            val workbook = WorkbookFactory.create(File(pathFile))
            val sheet = workbook.getSheetAt(0)


            val cellClass = sheet.getRow(ROW_CLASS).getCell(CELL_CLASS)

            if (cellClass.cellType == CellType.STRING && !TextUtils.isEmpty(cellClass.stringCellValue)) {
                cellClass.setCellValue(TITLE_EXCEL.format(subjectName?.toUpperCase()))
            }

            for (i in rowStart..sheet.lastRowNum) {
                if (i - rowStart < students.size) {
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


    // TODO Support Main Exam Marmot
    private const val ROW_FIRST_CHECK = 0
    private const val CELL_FIRST_CHECK = 0
    private const val ROW_SECOND_CHECK = 0
    private const val CELL_SECOND_CHECK = 1
    private const val START_ROW_MARMOT = 1
    private const val POINT_CELL = "Điểm"


    enum class StatusMarmot {
        ERROR_FILE, SUCCESS
    }

    enum class StatusExport {
        ERROR, SUCCESS
    }

    fun readMarmotPoint(pathExcelFile: String, callback: ((ArrayList<MarmotExam>, StatusMarmot) -> Unit)) {
        try {
            val workbook = WorkbookFactory.create(File(pathExcelFile))
            val sheet = workbook.getSheetAt(0)
            val dataFormatter = DataFormatter()
            val marmotExams = ArrayList<MarmotExam>()

            val firstCheck = sheet.getRow(ROW_FIRST_CHECK).getCell(CELL_FIRST_CHECK)
            if (firstCheck.cellType != CellType.STRING) {
                callback.invoke(marmotExams, StatusMarmot.ERROR_FILE)
                return
            }
            if (firstCheck.stringCellValue.trim().toLowerCase() != MARMOT_FIRST_CHECK.trim().toLowerCase()) {
                callback.invoke(marmotExams, StatusMarmot.ERROR_FILE)
                return
            }

            val secondCheck = sheet.getRow(ROW_SECOND_CHECK).getCell(CELL_SECOND_CHECK)
            if (secondCheck.cellType != CellType.STRING) {
                callback.invoke(marmotExams, StatusMarmot.ERROR_FILE)
                return
            }
            if (secondCheck.stringCellValue.trim().toLowerCase() != MARMOT_SECOND_CHECK.trim().toLowerCase()) {
                callback.invoke(marmotExams, StatusMarmot.ERROR_FILE)
                return
            }
            for (row in sheet) {
                if (row.rowNum >= START_ROW_MARMOT && row.rowNum <= sheet.lastRowNum) {
                    val marmotExam = MarmotExam()
                    for (cellRow in row) {
                        val cellValue = dataFormatter.formatCellValue(cellRow)
                        when (cellRow.columnIndex) {
                            0 -> {
                                marmotExam.idMarmot = cellValue
                            }
                            1 -> {
                                marmotExam.numberStudent = cellValue
                            }
                            else -> {
                                println("vuot qua chuoi")
                            }
                        }
                    }
                    marmotExams.add(marmotExam)
                }
            }
            workbook.close()
            callback.invoke(marmotExams, StatusMarmot.SUCCESS)
            return
        } catch (ex: IOException) {
            ex.printStackTrace()
            callback.invoke(ArrayList(), StatusMarmot.ERROR_FILE)
        }
    }

    fun exportMarmotExamPointItem(pathFile: String, data: ArrayList<MarmotExamItem>, callback: ((StatusExport) -> Unit)?) {
        if (data.isEmpty()) {
            callback?.invoke(StatusExport.ERROR)
            return
        }
        try {
            val workbook = HSSFWorkbook()
            val sheet: Sheet = workbook.createSheet("Bảng điểm")
            val headerFont: Font = workbook.createFont()
            headerFont.bold = true
            headerFont.fontHeightInPoints = 14.toShort()
            headerFont.color = IndexedColors.RED.getIndex()

            val headerRow = sheet.createRow(0)
            val callHeaderMarmot = headerRow.createCell(0)
            callHeaderMarmot.setCellValue(MARMOT_FIRST_CHECK)

            val cellHeaderPoint = headerRow.createCell(1)
            cellHeaderPoint.setCellValue(POINT_CELL)

            for (index in 0 until data.size) {
                val rowTemp = sheet.createRow(index + 1)
                rowTemp.createCell(0).setCellValue(data[index].idMarmot)
                rowTemp.createCell(1).setCellValue(data[index].pointOfMarmot)
            }
            val fileOut = FileOutputStream(pathFile)
            workbook.write(fileOut)
            fileOut.flush()
            fileOut.close()
            workbook.close()

            callback?.invoke(StatusExport.SUCCESS)
        } catch (ex: IOException) {
            ex.printStackTrace()
            callback?.invoke(StatusExport.ERROR)
        }
    }

    fun copyFileExcel(sourceFile: String, nameTargetFile: String) {
        val folderSpeech = File("${Environment.getExternalStorageDirectory()}$FOLDER_NAME")
        if (!folderSpeech.exists()) {
            folderSpeech.mkdir()
        }
        File(sourceFile).copyTo(File(pathFile(nameTargetFile)), true)
    }

    fun pathFile(nameFile: String?) = PATH_FILE.format(nameFile)
}