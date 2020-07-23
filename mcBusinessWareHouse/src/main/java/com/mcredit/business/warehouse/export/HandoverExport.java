package com.mcredit.business.warehouse.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

//itext libraries to write PDF file
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverBorrowedDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.DateUtil;
import com.mcredit.util.ZipFiles;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class HandoverExport implements IExportPdf {

    public static final String FONT = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "fonts/Times_New_Roman.ttf";
    public static final String imgUrl = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "img/logo.png";

    @Override
    public String exportPdf(List<WareHouseExportHandoverDTO> lstHandoverDTO) {
        String urlZipFilePDF = null;
        try {
            ZipFiles zipFiles = new ZipFiles();
            String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            zipFiles.deleteFiles(
                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_HANDOVER_EXPORT) + "/");
            for (int i = 0; i < lstHandoverDTO.size(); i++) {
                if (lstHandoverDTO.get(i) != null) {
                    String strFileFullPathPdf = CacheManager.Parameters()
                            .findParamValueAsString(ParametersName.WH_HANDOVER_EXPORT_FILE_NAME)
                            + lstHandoverDTO.get(i).getAccountReceiver() + ".pdf";
                    String outputPathPdf = url + strFileFullPathPdf;
                    Document document = new Document(PageSize.A4);
                    document.setMargins(-40, -40, 30, 30);

                    PdfWriter.getInstance(document, new FileOutputStream(outputPathPdf));
                    document.open();
                    //Title      
                    PdfPTable tableTitle = new PdfPTable(5); // the arg is the number of columns                         
                    PdfPCell cellTitle = createrCellBold("BIÊN BẢN BÀN GIAO VẬN HÀNH 2", 22, 5, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableTitle.addCell(cellTitle);
                    document.add(tableTitle);

                    document.add(new Paragraph("\n"));
                    PdfPTable tableDate = new PdfPTable(2);
                    tableDate.setWidths(new int[]{2, 7});
                    PdfPCell cellDate = createrCell("Ngày bàn giao: ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableDate.addCell(cellDate);
                    PdfPCell cellValueDate = createrCellBold(DateUtil.toString(new Date(), "dd/MM/yyyy"), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 3);
                    tableDate.addCell(cellValueDate);
                    document.add(tableDate);

                    PdfPTable tableEmployee1 = new PdfPTable(3);
                    tableEmployee1.setWidths(new int[]{2, 5, 3});
                    PdfPCell cellEmployee1 = createrCellBold("Nhân viên 1: ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableEmployee1.addCell(cellEmployee1);
                    PdfPCell cellEmployee1Value = createrCellBold(lstHandoverDTO.get(i).getExecutioner(), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableEmployee1.addCell(cellEmployee1Value);
                    PdfPCell cellAccEmployee1 = createrCellBold("Account: " + lstHandoverDTO.get(i).getAccountExcutioner(), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableEmployee1.addCell(cellAccEmployee1);
                    document.add(tableEmployee1);

                    PdfPTable tableEmployee2 = new PdfPTable(3);
                    tableEmployee2.setWidths(new int[]{2, 5, 3});
                    PdfPCell cellEmployee2 = createrCellBold("Nhân viên 2: ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableEmployee2.addCell(cellEmployee2);
                    PdfPCell cellEmployee2Value = createrCellBold(lstHandoverDTO.get(i).getReceiver(), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 3);
                    tableEmployee2.addCell(cellEmployee2Value);
                    PdfPCell cellAccEmployee2 = createrCellBold("Account: " + lstHandoverDTO.get(i).getAccountReceiver(), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableEmployee2.addCell(cellAccEmployee2);
                    document.add(tableEmployee2);

                    document.add(new Paragraph("\n"));
                    PdfPTable table = new PdfPTable(6); // the arg is the number of columns
                    table.setWidths(new int[]{1, 4, 4, 2, 5, 3});

                    PdfPCell numberOrderTitle = createrCellBold("STT", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(numberOrderTitle);

                    PdfPCell docTypeTile = createrCellBold("Loại giấy tờ", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(docTypeTile);

                    PdfPCell contractNumberTitle = createrCellBold("Số hợp đồng", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(contractNumberTitle);

                    PdfPCell caseNumberTitle = createrCellBold("Số case", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(caseNumberTitle);

                    PdfPCell cusNameTitle = createrCellBold("Tên khách hàng", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(cusNameTitle);

                    PdfPCell signTitle = createrCellBold("Ký nhận/Ghi chú", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    table.addCell(signTitle);

                    for (int j = 0; j < lstHandoverDTO.get(i).getLstHandover().size(); j++) {
                        PdfPCell numberOrder = createrCell(String.valueOf(lstHandoverDTO.get(i).getLstHandover().get(j).getNumberOrder()), 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(numberOrder);

                        PdfPCell docType = createrCell(lstHandoverDTO.get(i).getLstHandover().get(j).getDocType(), 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(docType);

                        PdfPCell contractNumber = createrCell(lstHandoverDTO.get(i).getLstHandover().get(j).getContractNumber(), 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(contractNumber);

                        PdfPCell caseNumber = createrCell(lstHandoverDTO.get(i).getLstHandover().get(j).getCaseNumber(), 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(caseNumber);

                        PdfPCell customerName = createrCell(lstHandoverDTO.get(i).getLstHandover().get(j).getCustomerName(), 13, 1, PdfPCell.BOX, Element.ALIGN_LEFT);
                        table.addCell(customerName);

                        PdfPCell sign = createrCell("", 13, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(sign);
                    }

                    document.add(table);

                    int sumContract = 0;
                    int sumErr = 0;
                    int sumCavet = 0;
                    int sumLetter = 0;
                    for (int k = 0; k < lstHandoverDTO.get(i).getLstHandover().size(); k++) {
                        if (lstHandoverDTO.get(i).getLstHandover().get(k).getCodeValue1().equalsIgnoreCase("WH_LOAN_DOC")) {
                            sumContract++;
                        } else if (lstHandoverDTO.get(i).getLstHandover().get(k).getCodeValue1().equalsIgnoreCase("WH_CAVET")) {
                            sumCavet++;
                        } else if (lstHandoverDTO.get(i).getLstHandover().get(k).getCodeValue1().equalsIgnoreCase("WH_ERR_UPDATE")) {
                            sumErr++;
                        } else if (lstHandoverDTO.get(i).getLstHandover().get(k).getCodeValue1().equalsIgnoreCase("WH_THANKS_LETTER")) {
                            sumLetter++;
                        }
                    }
                    document.add(new Paragraph("\n"));
                    PdfPTable tableSum = new PdfPTable(2);
                    tableSum.setWidths(new int[]{3, 7});

                    PdfPCell cellCountDoc = createrCell("Tống giấy tờ bàn giao : ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableSum.addCell(cellCountDoc);
                    PdfPCell cellCountValueDoc = createrCellBold(String.valueOf(lstHandoverDTO.get(i).getLstHandover().size()), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableSum.addCell(cellCountValueDoc);

                    PdfPCell cellSumContract = createrCell("Hợp đồng cho vay : ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableSum.addCell(cellSumContract);
                    PdfPCell cellSumContractValue = createrCellBold(String.valueOf(sumContract), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableSum.addCell(cellSumContractValue);

                    PdfPCell cellSumErr = createrCell("Update lỗi : ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableSum.addCell(cellSumErr);
                    PdfPCell cellSumErrValue = createrCellBold(String.valueOf(sumErr), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableSum.addCell(cellSumErrValue);

                    PdfPCell cellSumCavet = createrCell("Cavet : ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableSum.addCell(cellSumCavet);
                    PdfPCell cellSumCavetValue = createrCellBold(String.valueOf(sumCavet), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableSum.addCell(cellSumCavetValue);

                    PdfPCell cellSumLetter = createrCell("Thư cảm ơn : ", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT);
                    tableSum.addCell(cellSumLetter);
                    PdfPCell cellSumLetterValue = createrCellBold(String.valueOf(sumLetter), 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableSum.addCell(cellSumLetterValue);

                    document.add(tableSum);
                    document.add(new Paragraph("\n"));
                    PdfPTable tableSign = new PdfPTable(2);

                    PdfPCell cellSign1 = createrCellBold("Bên giao", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableSign.addCell(cellSign1);

                    PdfPCell cellSign2 = createrCellBold("Bên nhận", 13, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableSign.addCell(cellSign2);

                    document.add(tableSign);

                    document.close();
                    System.out.println(outputPathPdf.substring(1, outputPathPdf.length()));
//                    urlExport = outputPathPdf.substring(1, outputPathPdf.length());
                }
            }

            File dir = new File(
                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_HANDOVER_EXPORT));
            String zipDirName = url
                    + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_HANDOVER_EXPORT_ZIP_FILE);

            zipFiles.zipDirectory(dir, zipDirName);
            urlZipFilePDF = zipDirName.substring(1, zipDirName.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlZipFilePDF;
    }

    @Override
    public String exportPdfBorrowed(List<WareHouseExportHandoverBorrowedDTO> lstHandoverBorrowDTO) {
        String urlZipFileBorrPDF = null;
        try {
            ZipFiles zipFiles = new ZipFiles();
            String url = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            zipFiles.deleteFiles(
                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_HANDOVER_BORR_EXPORT) + "/");

            long date = new Date().getTime();
            SimpleDateFormat formatNowDay = new SimpleDateFormat("dd");
            SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
            SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

            String day = formatNowDay.format(date);
            String month = formatNowMonth.format(date);
            String year = formatNowYear.format(date);
            String strFileFullPathPdf = CacheManager.Parameters()
                    .findParamValueAsString(ParametersName.WH_HANDOVER_BORR_EXPORT_FILE_NAME) + ".pdf";
            String outputPathPdf = url + strFileFullPathPdf;
            Document document = new Document(PageSize.A4);
            document.setMargins(-40, -40, 30, 30);

            PdfWriter.getInstance(document, new FileOutputStream(outputPathPdf));
            document.open();

            for (int i = 0; i < lstHandoverBorrowDTO.size(); i++) {
                if (lstHandoverBorrowDTO.get(i) != null) {

                    PdfPTable tableHeader = new PdfPTable(2); // the arg is the number of columns   
                    tableHeader.setWidths(new int[]{3, 9});
                    Image img = Image.getInstance(imgUrl);
                    PdfPCell cellImage = new PdfPCell(img, true);
                    cellImage.setColspan(0);
                    cellImage.setBorder(PdfPCell.NO_BORDER);
                    cellImage.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tableHeader.addCell(cellImage);

                    PdfPCell cellHeader1 = createrCellBold("CÔNG TY TÀI CHÍNH TNHH MB SHINSEI \n"
                            + "\n Địa chỉ: Tầng 12 – Tòa nhà TNR – 54 Nguyễn Chí Thanh – Hà Nội \n"
                            + "\n Điện thoại: 04.710 86 888 Fax: 04.710.86 999 Website: www.mcredit.com.vn", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableHeader.addCell(cellHeader1);

                    document.add(tableHeader);
                    document.add(new Paragraph("\n"));

                    //Title      
                    PdfPTable tableTitle = new PdfPTable(5); // the arg is the number of columns                         
                    PdfPCell cellTitle = createrCellBold("BIÊN BẢN BÀN GIAO", 15, 5, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableTitle.addCell(cellTitle);
                    document.add(tableTitle);

                    document.add(new Paragraph("\n"));
                    PdfPTable tableDate = new PdfPTable(2);
                    tableDate.setWidths(new int[]{4, 3});
                    PdfPCell cellSpace = createrCell("", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER);
                    tableDate.addCell(cellSpace);
                    PdfPCell cellDate = createrCell("Ngày " + day + " Tháng " + month + " Năm " + year, 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER);
                    tableDate.addCell(cellDate);
                    document.add(tableDate);

                    PdfPTable tableAccountBorrowed = new PdfPTable(2);
                    tableAccountBorrowed.setWidths(new int[]{2, 7});
                    PdfPCell cellAccountBorrowed1 = createrCellBold("Người yêu cầu :", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableAccountBorrowed.addCell(cellAccountBorrowed1);
                    PdfPCell cellAccountBorrowed2 = createrCellBold(lstHandoverBorrowDTO.get(i).getAccountBorrowed(), 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableAccountBorrowed.addCell(cellAccountBorrowed2);
                    document.add(tableAccountBorrowed);

                    PdfPTable tableComment = new PdfPTable(1);
                    PdfPCell cellComment = createrCellBold("Lý do : ", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableComment.addCell(cellComment);
                    document.add(tableComment);

                    PdfPTable tableAppointExtenDate = new PdfPTable(2);
                    tableAppointExtenDate.setWidths(new int[]{2, 7});
                    PdfPCell cellAppointExtenDate1 = createrCellBold("Ngày dự kiến hoàn trả :", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableAppointExtenDate.addCell(cellAppointExtenDate1);
                    String appointExtenDate = "";
                    if (lstHandoverBorrowDTO.get(i).getExtensionDate() != null && lstHandoverBorrowDTO.get(i).getExtensionDate().equals("")) {
                        appointExtenDate = lstHandoverBorrowDTO.get(i).getExtensionDate();
                    } else if (lstHandoverBorrowDTO.get(i).getAppointmentDate() != null && !lstHandoverBorrowDTO.get(i).getAppointmentDate().equals("")) {
                        appointExtenDate = lstHandoverBorrowDTO.get(i).getAppointmentDate();
                    }
                    PdfPCell cellAppointExtenDate2 = createrCellBold(appointExtenDate, 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableAppointExtenDate.addCell(cellAppointExtenDate2);
                    document.add(tableAppointExtenDate);

                    PdfPTable tableDoctype = new PdfPTable(2);
                    tableDoctype.setWidths(new int[]{2, 7});
                    PdfPCell cellDoctype1 = createrCellBold("Loại giấy tờ :", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 1);
                    tableDoctype.addCell(cellDoctype1);
                    PdfPCell cellDoctype2 = createrCellBold(lstHandoverBorrowDTO.get(i).getDocType(), 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_LEFT, 2);
                    tableDoctype.addCell(cellDoctype2);
                    document.add(tableDoctype);

                    document.add(new Paragraph("\n"));
                    PdfPTable table = new PdfPTable(4);
                    table.setWidths(new int[]{1, 3, 4, 2});

                    PdfPCell numberOrderTitle = createrCellBold("STT", 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    numberOrderTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(numberOrderTitle);

                    PdfPCell contractNumberTitle = createrCellBold("SỐ HĐTD", 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    contractNumberTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(contractNumberTitle);

                    PdfPCell cusNameTitle = createrCellBold("TÊN KHÁCH HÀNG", 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    cusNameTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(cusNameTitle);

                    PdfPCell whCodeTitle = createrCellBold("MÃ LƯU KHO \n"
                            + "(Dành cho TTVH)", 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER, 1);
                    whCodeTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(whCodeTitle);

                    for (int j = 0; j < lstHandoverBorrowDTO.get(i).getLstHandoverBrr().size(); j++) {
                        PdfPCell numberOrder = createrCell(String.valueOf(lstHandoverBorrowDTO.get(i).getLstHandoverBrr().get(j).getNumberOrder()), 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(numberOrder);

                        PdfPCell contractNumber = createrCell(lstHandoverBorrowDTO.get(i).getLstHandoverBrr().get(j).getContractNumber(), 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(contractNumber);

                        PdfPCell cusName = createrCell(lstHandoverBorrowDTO.get(i).getLstHandoverBrr().get(j).getCustomerName(), 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(cusName);

                        PdfPCell whCode = createrCell(lstHandoverBorrowDTO.get(i).getLstHandoverBrr().get(j).getWareHouseCode(), 11, 1, PdfPCell.BOX, Element.ALIGN_CENTER);
                        table.addCell(whCode);
                    }

                    document.add(table);

                    document.add(new Paragraph("\n"));

                    PdfPTable tableSign = new PdfPTable(2);

                    PdfPCell cellSign1 = createrCellBold("Bên giao", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableSign.addCell(cellSign1);

                    PdfPCell cellSign2 = createrCellBold("Bên nhận", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 1);
                    tableSign.addCell(cellSign2);

                    document.add(tableSign);

                    PdfPTable tableNoteSign = new PdfPTable(2);

                    PdfPCell cellNoteSign1 = createrCellBold("(Ký và ghi rõ họ tên)", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 2);
                    tableNoteSign.addCell(cellNoteSign1);

                    PdfPCell cellNoteSign2 = createrCellBold("(Ký và ghi rõ họ tên)", 11, 1, PdfPCell.NO_BORDER, Element.ALIGN_CENTER, 2);
                    tableNoteSign.addCell(cellNoteSign2);

                    document.add(tableNoteSign);
                    
                    document.newPage();

                }
            }
            System.out.println(outputPathPdf.substring(1, outputPathPdf.length()));
            document.close();

            File dir = new File(
                    url + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_PATH_HANDOVER_BORR_EXPORT));
            String zipDirName = url
                    + CacheManager.Parameters().findParamValueAsString(ParametersName.WH_HANDOVER_BORR_EXPORT_ZIP_FILE);

            zipFiles.zipDirectory(dir, zipDirName);
            urlZipFileBorrPDF = zipDirName.substring(1, zipDirName.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlZipFileBorrPDF;
    }

    public static PdfPCell createrCell(String title, int size, int col, int border, int textAlign) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Paragraph titleIn = new Paragraph(title, new Font(bf, size));
        PdfPCell cellIn = new PdfPCell(titleIn);
        cellIn.setColspan(col);
        cellIn.setBorder(border);
        cellIn.setHorizontalAlignment(textAlign);
        return cellIn;
    }

    public static PdfPCell createrCellBold(String title, int size, int col, int border, int textAlign, int typeFont) throws DocumentException, IOException {
        BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Paragraph titleIn = null;
        if (typeFont == 1) {
            titleIn = new Paragraph(title, new Font(bf, size, Font.BOLD));
        } else if (typeFont == 2) {
            titleIn = new Paragraph(title, new Font(bf, size, Font.ITALIC));
        } else if (typeFont == 3) {
            titleIn = new Paragraph(title, new Font(bf, size, Font.BOLDITALIC));
        }
        PdfPCell cellIn = new PdfPCell(titleIn);
        cellIn.setColspan(col);
        cellIn.setBorder(border);
        cellIn.setHorizontalAlignment(textAlign);
        return cellIn;
    }

    @Override
    public void close() throws IOException {
        // TODO Auto-generated method stub

    }
}
