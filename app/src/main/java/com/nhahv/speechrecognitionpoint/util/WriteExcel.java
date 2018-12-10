package com.nhahv.speechrecognitionpoint.util;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class WriteExcel {
    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;

    public void write(String pathFile) throws IOException, WriteException {
        File file = new File(pathFile);
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        File fileDir = new File(pathFile).getParentFile().getAbsoluteFile();
        if (!fileDir.exists()) fileDir.mkdirs();
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("vi", "VN"));
        wbSettings.setEncoding("UTF-8");
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Bảng điểm", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
//        createLabel(excelSheet);
//        createContent(excelSheet);
        workbook.write();
        workbook.close();
    }

    private void createLabel(WritableSheet sheet) throws WriteException {
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);
        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline =
            new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);
        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);
        // Write a few headers
        addCaption(sheet, 0, 0, "Mã phách ");
        addCaption(sheet, 1, 0, "Điểm");
    }

    private void createContent(WritableSheet sheet, String date) throws WriteException {
        // Write a few number
        /*List<LopLoi> listErrorClass = new ArrayList<>();
        listErrorClass.addAll(DatabaseManager.getListErrorClassByDate(date));
        Log.d("TAG", "date = " + date);
        for (LopLoi item : listErrorClass) {
            Log.d("TAG", "data errro = " + item.getNgayViPham());
        }
        int size = listErrorClass.size();
        for (int i = 0; i < size; i++) {
            int index = i + 1;
            LopLoi item = listErrorClass.get(i);
            addNumber(sheet, 0, index, item.getUid());
            addNumber(sheet, 1, index, Integer.parseInt(item.getfK_LopHocID()));
            addLabel(sheet, 2, index, item.getUser());
            addLabel(sheet, 3, index, item.getfK_LoiID());
            addLabel(sheet, 4, index, item.getfK_HocSinhID());
            addNumber(sheet, 5, index, item.getfK_NamHocID());
            addDate(sheet, 6, index, item.getNgayViPham());
            addBoolean(sheet, 7, index, java.lang.Boolean.parseBoolean(item.getKhoa()));
            addLabel(sheet, 8, index, item.getGhiChu());
            addLabel(sheet, 9, index, item.getSoLuong());
        }*/
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
        throws WriteException {
        Label label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row, Integer integer)
        throws WriteException {
//        Number number = new Number(column, row, integer, times);
//        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
        throws WriteException {
        Label label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addBoolean(WritableSheet sheet, int column, int row, boolean s)
        throws WriteException {
//        Boolean label = new Boolean(column, row, s, times);
//        sheet.addCell(label);
    }

    private void addDate(WritableSheet sheet, int column, int row, String date)
        throws WriteException {
        /*SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date data = formatter.parse(date);
            DateFormat customDateFormat = new DateFormat("dd/MM/yyyy", Locale.getDefault());
            WritableCellFormat dateFormat = new WritableCellFormat(customDateFormat);
            DateTime dateTime = new DateTime(column, row, data, dateFormat);
            Log.d("TAG", "data " + dateTime.toString());
            sheet.addCell(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }
}
