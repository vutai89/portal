/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.business.warehouse.export;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.model.dto.warehouse.WareHouseExportHistoryDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.warehouse.WareHouseHistory;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.ZipFiles;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.util.CellRangeAddress;

public class WarehouseHistoryExport implements IExportXLS {

    private UnitOfWorkWareHouse _uok = null;

    public WarehouseHistoryExport(UnitOfWorkWareHouse uok) {
        _uok = uok;
    }
    public static String WH_LOAN_DOC = "Hồ sơ khoản vay";
    public static String WH_CAVET = "Cavet";
    public static String WH_ERR_UPDATE = "Update lỗi";
    public static String WH_THANKS_LETTER = "Thư cảm ơn";

    @Override
    public String exportWarehouseHistory(List<WareHouseExportHistoryDTO> lstHistory) {
        String urlFile = null;
        try {
            String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            ZipFiles zipFiles = new ZipFiles();
            zipFiles.deleteFiles(
                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_HISTORY_EXPORT) + File.separator);
            // Create a Workbook
            Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

            Font headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            // Create a CellStyle with the font
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 0; i < lstHistory.size(); i++) {
                Sheet sheet = workbook.createSheet(lstHistory.get(i).getDocTypeName() + " sheet " + (i + 1));
                for (int j = 2; j < 40; j++) {
                    Row headerRow = sheet.createRow(j);
                    for (int k = 0; k < 30; k++) {
                        headerRow.createCell(k);
                        headerRow.getCell(k).setCellStyle(headerCellStyle);
                    }
                }
                createTitle(lstHistory.get(i), sheet);
                createData(lstHistory.get(i), sheet);

            }
            // Write the output to a file
            FileOutputStream fileOut = new FileOutputStream(url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_HISTORY));
//            FileOutputStream fileOut = new FileOutputStream("D:\\MCredit\\poi-generated-file.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            // Closing the workbook
            workbook.close();
            String urlXlsxName = url
                    + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_EXPORT_HISTORY);
            urlFile = urlXlsxName.substring(1, urlXlsxName.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlFile;
    }

    public static void createTitle(WareHouseExportHistoryDTO dTO, Sheet sheet) {
        // Create a Row
        Row headerRow1 = sheet.createRow(0);
        headerRow1.createCell(0).setCellValue("Số Hợp đồng:");
        headerRow1.createCell(1).setCellValue(dTO.getContractNumber());
        Row headerRow2 = sheet.createRow(1);
        headerRow2.createCell(0).setCellValue("Loại giấy tờ:");
        headerRow2.createCell(1).setCellValue(dTO.getDocTypeName());

        Row headerRow3 = sheet.getRow(2);
        headerRow3.getCell(0).setCellValue("Nhận giấy tờ");        
        sheet.addMergedRegion(CellRangeAddress.valueOf("A3:A4"));

        headerRow3.getCell(1).setCellValue("Ngày nhận");
        Row headerRow4 = sheet.getRow(3);
        headerRow4.getCell(1).setCellValue("Người nhận");

        Row headerRow5 = sheet.getRow(4);
        headerRow5.getCell(0).setCellValue("Phân bổ");
        headerRow5.getCell(1).setCellValue("Ngày phân bổ");
        Row headerRow6 = sheet.getRow(5);
        headerRow6.getCell(1).setCellValue("Người phân bổ");
        Row headerRow7 = sheet.getRow(6);
        headerRow7.getCell(1).setCellValue("Người nhận phân bổ");
        Row headerRow8 = sheet.getRow(7);
        headerRow8.getCell(1).setCellValue("Ngày Reassign");
        Row headerRow9 = sheet.getRow(8);
        headerRow9.getCell(1).setCellValue("Người Reassign");
        Row headerRow10 = sheet.getRow(9);
        headerRow10.getCell(1).setCellValue("Người nhận Reassign");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A5:A10"));

        Row headerRow11 = sheet.getRow(10);
        headerRow11.getCell(0).setCellValue("Check VH2");
        headerRow11.getCell(1).setCellValue("Người check VH2");
        Row headerRow12 = sheet.getRow(11);
        headerRow12.getCell(1).setCellValue("Ngày check VH2");
        Row headerRow13 = sheet.getRow(12);
        headerRow13.getCell(1).setCellValue("Mã lỗi");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A11:A13"));

        Row headerRow14 = sheet.getRow(13);
        headerRow14.getCell(0).setCellValue("Return");
        headerRow14.getCell(1).setCellValue("Ngày Return");
        Row headerRow15 = sheet.getRow(14);
        headerRow15.getCell(1).setCellValue("Người Return");
        sheet.addMergedRegion(CellRangeAddress.valueOf("A14:A15"));

        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)) {
            Row headerRow16 = sheet.getRow(15);
            headerRow16.getCell(0).setCellValue("Lưu kho");
            headerRow16.getCell(1).setCellValue("Người lưu kho");
            Row headerRow17 = sheet.getRow(16);
            headerRow17.getCell(1).setCellValue("Ngày lưu kho");
            Row headerRow18 = sheet.getRow(17);
            headerRow18.getCell(1).setCellValue("Mã lưu kho");
            sheet.addMergedRegion(CellRangeAddress.valueOf("A16:A18"));
        }
        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_THANKS_LETTER)) {
            Row headerRow19 = sheet.getRow(18);
            headerRow19.getCell(0).setCellValue("Cho mượn");
            headerRow19.getCell(1).setCellValue("Ngày cho mượn");
            Row headerRow20 = sheet.getRow(19);
            headerRow20.getCell(1).setCellValue("Người cho mượn");
            Row headerRow21 = sheet.getRow(20);
            headerRow21.getCell(1).setCellValue("Người mượn");
            Row headerRow22 = sheet.getRow(21);
            headerRow22.getCell(1).setCellValue("Phòng ban");
            Row headerRow23 = sheet.getRow(22);
            headerRow23.getCell(1).setCellValue("Ngày gia hạn");
            Row headerRow24 = sheet.getRow(23);
            headerRow24.getCell(1).setCellValue("Ngày trả");
            sheet.addMergedRegion(CellRangeAddress.valueOf("A19:A24"));
        }
        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_LOAN_DOC)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_THANKS_LETTER)) {
            Row headerRow25 = sheet.getRow(24);
            headerRow25.getCell(0).setCellValue("Xuất trả");
            headerRow25.getCell(1).setCellValue("Người xuất trả");
            Row headerRow26 = sheet.getRow(25);
            headerRow26.getCell(1).setCellValue("Ngày xuất trả");
            sheet.addMergedRegion(CellRangeAddress.valueOf("A25:A26"));

            Row headerRow27 = sheet.getRow(26);
            headerRow27.getCell(0).setCellValue("Thư hoàn");
            headerRow27.getCell(1).setCellValue("Ngày nhận hoàn");
            Row headerRow28 = sheet.getRow(27);
            headerRow28.getCell(1).setCellValue("Người nhận");
            sheet.addMergedRegion(CellRangeAddress.valueOf("A27:A28"));
        }

    }

    public boolean createData(WareHouseExportHistoryDTO dTO, Sheet sheet) {
        // nhan giay to
        List<WareHouseHistory> list1 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField1");
        if (!list1.isEmpty()) {
            Row headerRow3 = sheet.getRow(2);
            headerRow3.getCell(2).setCellValue(list1.get(0).getDataField1());

            Row headerRow4 = sheet.getRow(3);
            headerRow4.getCell(2).setCellValue(list1.get(0).getDataField2());
        }

        //phan bo
        List<WareHouseHistory> list2 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField2");
        if (!list2.isEmpty()) {
            Row headerRow5 = sheet.getRow(4);
            headerRow5.getCell(2).setCellValue(list2.get(0).getDataField1());
            Row headerRow6 = sheet.getRow(5);
            headerRow6.getCell(2).setCellValue(list2.get(0).getDataField2());
            Row headerRow7 = sheet.getRow(6);
            headerRow7.getCell(2).setCellValue(list2.get(0).getDataField3());
        }

        // reassign
        List<WareHouseHistory> list3 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField3");
        if (!list3.isEmpty()) {
            Row headerRow8 = sheet.getRow(7);
            Row headerRow9 = sheet.getRow(8);
            Row headerRow10 = sheet.getRow(9);
            for (int j = 0; j < list3.size(); j++) {
                headerRow8.getCell(j + 2).setCellValue(list3.get(j).getDataField1());

                headerRow9.getCell(j + 2).setCellValue(list3.get(j).getDataField2());

                headerRow10.getCell(j + 2).setCellValue(list3.get(j).getDataField3());
            }
        }

        //check vh2
        List<WareHouseHistory> list4 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField4");
        if (!list4.isEmpty()) {
            Row headerRow11 = sheet.getRow(10);
            headerRow11.getCell(2).setCellValue(list4.get(0).getDataField1());
            Row headerRow12 = sheet.getRow(11);
            headerRow12.getCell(2).setCellValue(list4.get(0).getDataField2());
        }

        //lay ma loi
        List<WareHouseHistory> list5 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField5");
        if (!list5.isEmpty()) {
            Row headerRow13 = sheet.getRow(12);
            String errCode = "";
            for (int j = 0; j < list5.size(); j++) {
                errCode = errCode + list5.get(j).getDataField1() + " \n";
            }
            errCode = errCode.substring(0, errCode.length() - 2);
            headerRow13.getCell(2).setCellValue(errCode);
        }

        //return
        List<WareHouseHistory> list6 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField6");
        if (!list6.isEmpty()) {
            Row headerRow14 = sheet.getRow(13);
            headerRow14.getCell(2).setCellValue(list6.get(0).getDataField1());
            Row headerRow15 = sheet.getRow(14);
            headerRow15.getCell(2).setCellValue(list6.get(0).getDataField2());
        }
        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)) {
            //luu kho
            List<WareHouseHistory> list7 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField7");
            if (!list7.isEmpty()) {
                Row headerRow16 = sheet.getRow(15);
                headerRow16.getCell(2).setCellValue(list7.get(0).getDataField1());
                Row headerRow17 = sheet.getRow(16);
                headerRow17.getCell(2).setCellValue(list7.get(0).getDataField2());
                Row headerRow18 = sheet.getRow(17);
                headerRow18.getCell(2).setCellValue(list7.get(0).getDataField3());
            }
        }

        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_THANKS_LETTER)) {
            //Cho muon
            List<WareHouseHistory> list8 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField8");
            if (!list8.isEmpty()) {
                Row headerRow19 = sheet.getRow(18);
                Row headerRow20 = sheet.getRow(19);
                Row headerRow21 = sheet.getRow(20);
                Row headerRow22 = sheet.getRow(21);
                Row headerRow23 = sheet.getRow(22);
                Row headerRow24 = sheet.getRow(23);
                for (int j = 0; j < list8.size(); j++) {
                    headerRow19.getCell(j + 2).setCellValue(list8.get(j).getDataField1());

                    headerRow20.getCell(j + 2).setCellValue(list8.get(j).getDataField2());

                    headerRow21.getCell(j + 2).setCellValue(list8.get(j).getDataField3());

                    headerRow22.getCell(j + 2).setCellValue(list8.get(j).getDataField4());

                    headerRow23.getCell(j + 2).setCellValue(list8.get(j).getDataField5());

                    headerRow24.getCell(j + 2).setCellValue(list8.get(j).getDataField6());
                }
            }
        }

        if (!dTO.getDocTypeName().trim().equalsIgnoreCase(WH_LOAN_DOC)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_ERR_UPDATE)
                && !dTO.getDocTypeName().trim().equalsIgnoreCase(WH_THANKS_LETTER)) {
            //xuat tra
            List<WareHouseHistory> list9 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField9");
            if (!list9.isEmpty()) {
                Row headerRow25 = sheet.getRow(24);
                headerRow25.getCell(2).setCellValue(list9.get(0).getDataField1());
                Row headerRow26 = sheet.getRow(25);
                headerRow26.getCell(2).setCellValue(list9.get(0).getDataField2());
            }

            //thu hoan
            List<WareHouseHistory> list10 = this._uok.whDocumentRepo().getLstHistoryWarehouse(dTO.getWhId(), "getLstHistoryWhDataField10");
            if (!list10.isEmpty()) {
                Row headerRow27 = sheet.getRow(26);
                headerRow27.getCell(2).setCellValue(list10.get(0).getDataField1());
                Row headerRow28 = sheet.getRow(27);
                headerRow28.getCell(2).setCellValue(list10.get(0).getDataField2());
            }
        }

        for (int j = 0; j < 30; j++) {
            sheet.autoSizeColumn(j);
        }
        return true;
    }

    public void close() throws IOException {
        // TODO Auto-generated method stub

    }

}
