package com.mcredit.business.audit.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.audit.entity.AuditDifferent;
import com.mcredit.model.dto.audit.AuditResultDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.sharedbiz.cache.CacheManager;

public class ExcelProcessing {

	private ExcelProcessing() {
		// private constructor
	}

	public static ExcelProcessing getInstance() {
		return Excel.INSTANCE;
	}

	// Inner class to provide instance of class
	private static class Excel {
		private static final ExcelProcessing INSTANCE = new ExcelProcessing();
	}

	public File generateExcelFile(HashMap<String, AuditResultDTO> result, String thirdParty, String fromDate,
			String toDate) throws IOException {

		XSSFWorkbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();

		XSSFSheet sheet = workbook.createSheet(Labels.getString("label.audit.excel.report.name"));

		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setFontName(AuditEnum.TIMES_NEW_ROMAN.value());
		boldFont.setBold(true);

		// Font biFont = workbook.createFont();
		// biFont.setFontHeightInPoints((short) 13);
		// biFont.setFontName("Times New Roman");
		// biFont.setBold(true);
		//
		// Font italicFont = workbook.createFont();
		// italicFont.setFontHeight((short) 11);
		// italicFont.setFontName("Times New Roman");
		// italicFont.setItalic(true);

		CellStyle boldCellStyle = setStyle(workbook, boldFont);

		// CellStyle biCellStyle = setStyle(workbook, biFont);
		//
		// CellStyle italicStyle = setStyle(workbook, italicFont);

		String excelName = Labels.getString("label.audit.excel.report.introduce") + " " + thirdParty + " - MC";

		createHeader(workbook, sheet, thirdParty, fromDate, toDate, excelName);

		addMergeRegion(sheet, new CellRangeAddress(9, 11, 0, 0));
		addMergeRegion(sheet, new CellRangeAddress(9, 11, 1, 1));
		addMergeRegion(sheet, new CellRangeAddress(9, 9, 2, 3));
		addMergeRegion(sheet, new CellRangeAddress(9, 9, 4, 5));
		addMergeRegion(sheet, new CellRangeAddress(9, 9, 6, 7));
		addMergeRegion(sheet, new CellRangeAddress(9, 9, 8, 9));
		Row row9 = sheet.createRow(9);
		Cell cell90 = row9.createCell(0);
		cell90.setCellStyle(boldCellStyle);
		cell90.setCellValue("STT");
		Cell cell91 = row9.createCell(1);
		cell91.setCellStyle(boldCellStyle);
		cell91.setCellValue(Labels.getString("label.audit.excel.report.trade"));
		Cell cell92 = row9.createCell(2);
		cell92.setCellStyle(boldCellStyle);
		cell92.setCellValue(Labels.getString("label.audit.excel.report.figure") + " " + thirdParty.toUpperCase());
		Cell cell94 = row9.createCell(4);
		cell94.setCellStyle(boldCellStyle);
		cell94.setCellValue(Labels.getString("label.audit.excel.report.figure.mc"));
		Cell cell96 = row9.createCell(6);
		cell96.setCellStyle(boldCellStyle);
		cell96.setCellValue(Labels.getString("label.audit.excel.report.trade.true"));
		Cell cell98 = row9.createCell(8);
		cell98.setCellStyle(boldCellStyle);
		cell98.setCellValue(Labels.getString("label.audit.excel.report.trade.false"));
		Row row10 = sheet.createRow(10);
		row10.setHeightInPoints((float) 25.50);
		for (int i = 2; i < 10; i++) {
			if (i % 2 == 0) {
				Cell celli = row10.createCell(i);
				celli.setCellStyle(boldCellStyle);
				celli.setCellValue(Labels.getString("label.audit.excel.report.total.deal"));
			} else {
				Cell celli = row10.createCell(i);
				celli.setCellStyle(boldCellStyle);
				celli.setCellValue(Labels.getString("label.audit.excel.report.total.money"));
			}
		}

		Row row11 = sheet.createRow(11);
		Cell cell112 = row11.createCell(2);
		cell112.setCellStyle(boldCellStyle);
		cell112.setCellValue("1A");
		Cell cell113 = row11.createCell(3);
		cell113.setCellStyle(boldCellStyle);
		cell113.setCellValue("2A");
		Cell cell114 = row11.createCell(4);
		cell114.setCellStyle(boldCellStyle);
		cell114.setCellValue("1B");
		Cell cell115 = row11.createCell(5);
		cell115.setCellStyle(boldCellStyle);
		cell115.setCellValue("2B");
		Cell cell116 = row11.createCell(6);
		cell116.setCellStyle(boldCellStyle);
		cell116.setCellValue("3");
		Cell cell117 = row11.createCell(7);
		cell117.setCellStyle(boldCellStyle);
		cell117.setCellValue("4");
		Cell cell118 = row11.createCell(8);
		cell118.setCellStyle(boldCellStyle);
		cell118.setCellValue("5=1A-1B");
		Cell cell119 = row11.createCell(9);
		cell119.setCellStyle(boldCellStyle);
		cell119.setCellValue("6=2A-2B");

		setMergeRegion(sheet, new CellRangeAddress(9, 11, 0, 0), BorderStyle.THIN);
		setMergeRegion(sheet, new CellRangeAddress(9, 11, 1, 1), BorderStyle.THIN);
		setMergeRegion(sheet, new CellRangeAddress(9, 9, 2, 3), BorderStyle.THIN);
		setMergeRegion(sheet, new CellRangeAddress(9, 9, 4, 5), BorderStyle.THIN);
		setMergeRegion(sheet, new CellRangeAddress(9, 9, 6, 7), BorderStyle.THIN);
		setMergeRegion(sheet, new CellRangeAddress(9, 9, 8, 9), BorderStyle.THIN);

		Row row12 = sheet.createRow(12);
		Cell cell120 = row12.createCell(0);
		cell120.setCellStyle(boldCellStyle);
		cell120.setCellValue("1");

		Cell cell121 = row12.createCell(1);
		cell121.setCellStyle(boldCellStyle);
		cell121.setCellValue(Labels.getString("label.audit.excel.report.debt.collection"));

		Row row13 = sheet.createRow(13);
		Cell cell130 = row13.createCell(0);
		cell130.setCellStyle(boldCellStyle);
		cell130.setCellValue("2");

		Cell cell131 = row13.createCell(1);
		cell131.setCellStyle(boldCellStyle);
		cell131.setCellValue(Labels.getString("label.audit.excel.report.disbursement"));
		addMergeRegion(sheet, new CellRangeAddress(14, 14, 0, 1));

		Row row14 = sheet.createRow(14);
		Cell cell140 = row14.createCell(0);
		cell140.setCellStyle(boldCellStyle);
		cell140.setCellValue(Labels.getString("label.audit.excel.report.total"));

		for (String key : result.keySet()) {
			if (key.equals(AuditEnum.THU.value())) {
				insertValue(row12, result.get(key), boldCellStyle);
			} else if (key.equals(AuditEnum.CHI.value())) {
				insertValue(row13, result.get(key), boldCellStyle);
			} else {
				insertValue(row14, result.get(key), boldCellStyle);
			}
		}

		setMergeRegion(sheet, new CellRangeAddress(14, 14, 0, 1), BorderStyle.THIN);

		addMergeRegion(sheet, new CellRangeAddress(17, 17, 0, 4));
		addMergeRegion(sheet, new CellRangeAddress(17, 17, 5, 9));
		Row row17 = sheet.createRow(17);
		Cell cell170 = row17.createCell(0);
		cell170.setCellStyle(boldCellStyle); // biCellStyle
		cell170.setCellValue(Labels.getString("label.audit.excel.report.representative") + " " + thirdParty);

		Cell cell175 = row17.createCell(5);
		cell175.setCellStyle(boldCellStyle); // biCellStyle
		cell175.setCellValue(Labels.getString("label.audit.excel.report.representative.mc"));

		setMergeRegion(sheet, new CellRangeAddress(17, 17, 0, 4), BorderStyle.NONE);
		setMergeRegion(sheet, new CellRangeAddress(17, 17, 5, 9), BorderStyle.NONE);

		addMergeRegion(sheet, new CellRangeAddress(20, 20, 0, 9));
		Cell cell200 = sheet.createRow(20).createCell(0);
		cell200.setCellStyle(boldCellStyle); // italicStyle
		cell200.setCellValue(Messages.getString("audit.excel.report.note") + " " + fromDate + " "
				+ Messages.getString("audit.excel.report.end.note") + " " + toDate + "");

		setMergeRegion(sheet, new CellRangeAddress(20, 20, 0, 9), BorderStyle.NONE);

		// Write the output to a file
		String outputFile = CacheManager.Parameters().findParamValueAsString(ParametersName.LOCAL_AUDIT_FILE_ADDRESS)
				+ UUID.randomUUID().toString() + ".xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(outputFile);

			workbook.write(fileOut);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != fileOut) {
				fileOut.close();
			}
			if (null != workbook) {
				// Closing the workbook
				workbook.close();
			}

		}

		return new File(outputFile);
	}

	private void addMergeRegion(Sheet sheet, CellRangeAddress cellRange) {
		sheet.addMergedRegion(cellRange);

	}

	private void setMergeRegion(Sheet sheet, CellRangeAddress cellRange, BorderStyle style) {
		RegionUtil.setBorderBottom(style, cellRange, sheet);
		RegionUtil.setBorderTop(style, cellRange, sheet);
		RegionUtil.setBorderRight(style, cellRange, sheet);
		RegionUtil.setBorderLeft(style, cellRange, sheet);
	}

	private void createHeader(Workbook workbook, Sheet sheet, String thirdParty, String toDate, String fromDate,
			String excelName) {

		// Font normalFont = workbook.createFont();
		// normalFont.setFontHeightInPoints((short) 14);
		// normalFont.setFontName("Times New Roman");
		// normalFont.setBold(false);
		// normalFont.setItalic(false);

		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 14);
		boldFont.setFontName(AuditEnum.TIMES_NEW_ROMAN.value());
		boldFont.setBold(true);

		// Font biFont = workbook.createFont();
		// biFont.setFontHeightInPoints((short) 14);
		// biFont.setFontName("Times New Roman");
		// biFont.setBold(true);
		// biFont.setItalic(true);

		// CellStyle normalCellStype = setStyle(workbook, normalFont);
		//
		// CellStyle biCellStyle = setStyle(workbook, biFont);

		CellStyle headerCellStyle = setStyle(workbook, boldFont);
		addMergeRegion(sheet, CellRangeAddress.valueOf("A1:J1"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A2:J2"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A3:J3"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A4:J4"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A5:J5"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A6:J6"));
		addMergeRegion(sheet, CellRangeAddress.valueOf("A7:J7"));

		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints((float) 20.0);
		// Create a Row
		Row headerRow = sheet.createRow(0);
		Cell cell0 = headerRow.createCell(0);
		cell0.setCellValue(Messages.getString("audit.excel.report.introduce.one"));
		cell0.setCellStyle(headerCellStyle); // normalCellStyle
		Cell cell1 = sheet.createRow(1).createCell(0);
		cell1.setCellValue(Messages.getString("audit.excel.report.introduce.two"));
		cell1.setCellStyle(headerCellStyle); // normalCellStyle
		Cell cell2 = sheet.createRow(2).createCell(0);
		cell2.setCellValue("-------o0o--------");
		cell2.setCellStyle(headerCellStyle); // normalCellStyle
		Cell cell30 = sheet.createRow(3).createCell(0);
		cell30.setCellValue(excelName);
		cell30.setCellStyle(headerCellStyle);
		Cell cell4 = sheet.createRow(4).createCell(0);
		cell4.setCellValue("" + thirdParty.toUpperCase() + " & MCREDIT");
		cell4.setCellStyle(headerCellStyle); // normalCellStyle

		Cell cell6 = sheet.createRow(6).createCell(0);
		cell6.setCellValue(Messages.getString("audit.excel.report.start.time") + " " + fromDate + " "
				+ Messages.getString("audit.excel.report.end.time") + " " + toDate + "");
		cell6.setCellStyle(headerCellStyle); // biCellStyle

		setMergeRegion(sheet, CellRangeAddress.valueOf("A1:J1"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A2:J2"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A3:J3"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A4:J4"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A5:J5"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A6:J6"), BorderStyle.NONE);
		setMergeRegion(sheet, CellRangeAddress.valueOf("A7:J7"), BorderStyle.NONE);
	}

	private CellStyle setStyle(Workbook workbook, Font font) {
		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		return style;
	}

	private void insertValue(Row row, AuditResultDTO audit, CellStyle boldCellStyle) {
		Cell cell2 = row.createCell(2);
		cell2.setCellStyle(boldCellStyle);
		cell2.setCellValue(audit.getThirdPartyTotalDeal());

		Cell cell3 = row.createCell(3);
		cell3.setCellStyle(boldCellStyle);
		cell3.setCellValue(audit.getThirdPartyTotalMoney());

		Cell cell4 = row.createCell(4);
		cell4.setCellStyle(boldCellStyle);
		cell4.setCellValue(audit.getMcreditTotalDeal());

		Cell cell5 = row.createCell(5);
		cell5.setCellStyle(boldCellStyle);
		cell5.setCellValue(audit.getMcreditTotalMoney());

		Cell cell6 = row.createCell(6);
		cell6.setCellStyle(boldCellStyle);
		cell6.setCellValue(audit.getMatchTotalDeal());

		Cell cell7 = row.createCell(7);
		cell7.setCellStyle(boldCellStyle);
		cell7.setCellValue(audit.getMatchTotalMoney());

		Cell cell8 = row.createCell(8);
		cell8.setCellStyle(boldCellStyle);
		cell8.setCellValue(audit.getUnMatchTotalDeal());

		Cell cell9 = row.createCell(9);
		cell9.setCellStyle(boldCellStyle);
		cell9.setCellValue(audit.getUnMatchTotalMoney());
	}

	public File generateDetailExcelFile(List<AuditDifferent> report, String thirdParty, String fromDate, String toDate,
			String reportType) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet;
		String excelName;
		if (reportType.equals(AuditEnum.BAO_CAO_CHI_HO.value())) {
			sheet = workbook.createSheet(Labels.getString("label.audit.excel.export.disbursement.sheet"));
			excelName = Labels.getString("label.audit.excel.export.disbursement.name");
		} else {
			sheet = workbook.createSheet(Labels.getString("label.audit.excel.export.debtcollection.sheet"));
			excelName = Labels.getString("label.audit.excel.export.debtcollection.name");
		}

		Font boldFont = workbook.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setFontName(AuditEnum.TIMES_NEW_ROMAN.value());
		boldFont.setBold(true);

		// Font biFont = workbook.createFont();
		// biFont.setFontHeightInPoints((short) 14);
		// biFont.setFontName("Times New Roman");
		// biFont.setBold(true);
		// biFont.setItalic(true);
		createHeader(workbook, sheet, thirdParty, fromDate, toDate, excelName);

		CellStyle boldCellStyle = setStyle(workbook, boldFont);
		sheet.setDefaultColumnWidth(19);

		// Font normalFont = workbook.createFont();
		// normalFont.setFontHeight((short) 10);
		// normalFont.setFontName("Times New Roman");
		//
		// CellStyle normalStyle = setStyle(workbook, normalFont);
		//
		// Font italicFont = workbook.createFont();
		// italicFont.setFontHeight((short) 11);
		// italicFont.setFontName("Times New Roman");
		// italicFont.setItalic(true);
		//
		// CellStyle italicStyle = setStyle(workbook, italicFont);

		Row row9 = sheet.createRow(9);
		Cell cell90 = row9.createCell(0);
		cell90.setCellStyle(boldCellStyle);
		cell90.setCellValue("STT");

		Cell cell91 = row9.createCell(1);
		cell91.setCellStyle(boldCellStyle);
		cell91.setCellValue(Labels.getString("label.audit.excel.report.contract"));

		Cell cell92 = row9.createCell(2);
		cell92.setCellStyle(boldCellStyle);
		cell92.setCellValue(Labels.getString("label.audit.excel.report.customer.name"));

		Cell cell93 = row9.createCell(3);
		cell93.setCellStyle(boldCellStyle);
		cell93.setCellValue(Labels.getString("label.audit.excel.report.cmnd"));

		Cell cell96 = row9.createCell(4);
		cell96.setCellStyle(boldCellStyle);
		cell96.setCellValue(Labels.getString("label.audit.excel.report.ref.id"));
		if (reportType.equals(AuditEnum.BAO_CAO_CHI_HO.value())) {
			Cell cell94 = row9.createCell(4);
			cell94.setCellStyle(boldCellStyle);
			cell94.setCellValue("Số tiền giải ngân");

			Cell cell95 = row9.createCell(5);
			cell95.setCellStyle(boldCellStyle);
			cell95.setCellValue("Ngày giải ngân");
		} else {
			Cell cell94 = row9.createCell(4);
			cell94.setCellStyle(boldCellStyle);
			cell94.setCellValue("Số tiền thu nợ");

			Cell cell95 = row9.createCell(5);
			cell95.setCellStyle(boldCellStyle);
			cell95.setCellValue("Ngày thu nợ");
		}

		int num = 1;
		int rownum = 10;
		Long total = 0L;
		for (AuditDifferent diff : report) {
			Row row = sheet.createRow(rownum++);
			Cell cell0 = row.createCell(0);
			cell0.setCellStyle(boldCellStyle); // normalStyle
			cell0.setCellValue(num++);

			Cell cell1 = row.createCell(1);
			cell1.setCellStyle(boldCellStyle); // normalStyle
			row.createCell(1).setCellValue(diff.getContractNumber());

			Cell cell2 = row.createCell(2);
			cell2.setCellStyle(boldCellStyle); // normamlStyle
			row.createCell(2).setCellValue(diff.getCustomerName());

			Cell cell3 = row.createCell(3);
			cell3.setCellStyle(boldCellStyle); // normalStyle
			cell3.setCellValue(diff.getCmnd());

			Cell cell4 = row.createCell(4);
			cell4.setCellStyle(boldCellStyle); // normalStyle
			cell4.setCellValue(diff.getMoney());

			Cell cell5 = row.createCell(5);
			cell5.setCellStyle(boldCellStyle); // normalStyle
			cell5.setCellValue(diff.getDate());

			Cell cell6 = row.createCell(6);
			cell6.setCellStyle(boldCellStyle); // normalStyle
			cell6.setCellValue(diff.getRef());
			total += diff.getMoney();
		}

		Row row = sheet.createRow(rownum++);
		Cell celli0 = row.createCell(0);
		celli0.setCellStyle(boldCellStyle);
		celli0.setCellValue("Tổng");

		Cell celli4 = row.createCell(4);
		celli4.setCellStyle(boldCellStyle); // normalStyle
		celli4.setCellValue(total);

		rownum += 2;

		addMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 0, 4));
		addMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 5, 9));
		Row row17 = sheet.createRow(rownum);
		Cell cell170 = row17.createCell(0);
		cell170.setCellStyle(boldCellStyle);
		cell170.setCellValue("ĐẠI DIỆN " + thirdParty);

		Cell cell175 = row17.createCell(5);
		cell175.setCellStyle(boldCellStyle);
		cell175.setCellValue("ĐẠI DIỆN MCREDIT");

		setMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 0, 2), BorderStyle.NONE);
		setMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 3, 5), BorderStyle.NONE);

		rownum += 4;

		addMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 0, 9));
		Cell cellNum = sheet.createRow(rownum).createCell(0);
		cellNum.setCellStyle(boldCellStyle); // italicStyle
		cellNum.setCellValue("Ghi chú: Báo cáo đối soát ngày được xác định từ 00h 00 phút 00 giây ngày " + fromDate
				+ " đến 23h 59 phút 59 giây ngày " + toDate + "");

		setMergeRegion(sheet, new CellRangeAddress(rownum, rownum, 0, 9), BorderStyle.NONE);

		// Write the output to a file
		String outputFile = CacheManager.Parameters().findParamValueAsString(ParametersName.LOCAL_AUDIT_FILE_ADDRESS)
				+ UUID.randomUUID().toString() + ".xlsx";
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(outputFile);

			workbook.write(fileOut);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != workbook) {
				// Closing the workbook
				workbook.close();
			}

			if (null != fileOut) {
				fileOut.close();
			}

		}

		return new File(outputFile);
	}

}
