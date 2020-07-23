package com.mcredit.business.audit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.IOUtil;

import com.mcredit.business.audit.common.GetterAndSetter;
import com.mcredit.common.Labels;
import com.mcredit.data.audit.entity.DebtCollection;
import com.mcredit.data.audit.entity.Disbursement;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.util.DateUtil;

public class CommonUtils {
	private static String dateFormat = "yyyy/MM/dd HH:mm:ss";
	private static String payoo_format = "dd/MM/yyyy HH:mm:ss";
	private static String vnptepay_format = "dd/MM/yyyy HH:mm";
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat dfS = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat dfVNPT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
	private static SimpleDateFormat dfVNPOST = new SimpleDateFormat("yyyyMMddHHmmss");

	public static void convertInputStream(InputStream is, String filePath) throws IOException {
		Files.copy(is, Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
	}

	public static Audit convertData(Row data, String[] fieldName, Integer[] position) throws ParseException {
		Audit audit = new Audit();
		for (int i = 0; i <= fieldName.length; i++) {
			try {
				GetterAndSetter.callSetter(audit, fieldName[i],
						getCellValueAsString(data.getCell(position[i]), dateFormat));
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return audit;
	}

	public static Audit convertData(Row data, String[] fieldName, Integer[] position, String format)
			throws ParseException {
		Audit audit = new Audit();
		for (int i = 0; i <= fieldName.length; i++) {
			try {
				String s = getCellValueAsString(data.getCell(position[i]), format);
				if (fieldName[i].equals(AuditEnum.STATUS.value())) {
					if (s.equals(Labels.getString("label.audit.status.success"))) {
						s = AuditEnum.SUCCESS.value();
					} else {
						s = AuditEnum.FAIL.value();
					}
				}
				GetterAndSetter.callSetter(audit, fieldName[i], s);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return audit;
	}

	public static Audit convertData(String data, String[] fieldName, Integer[] position, String chSplit) {
		Audit audit = new Audit();
		String[] lstData = data.split(chSplit);
		for (int i = 0; i <= fieldName.length; i++) {
			try {
				GetterAndSetter.callSetter(audit, fieldName[i], (Object) lstData[position[i]]);
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return audit;
	}

	public static boolean isRowEmpty(Row row) {
		if (row == null)
			return true;

		if (row.getLastCellNum() <= 0)
			return true;

		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && !"".equals(cell.toString()))
				return false;
		}
		return true;
	}

	public static String getCellValueAsString(Cell cell, String dateFormat) throws ParseException {
		if (cell == null)
			return "";
		try {
			switch (cell.getCellTypeEnum()) {
			case BOOLEAN:
				return Boolean.toString(cell.getBooleanCellValue());
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
					try {
						return DateUtil.toString(cell.getDateCellValue(), dateFormat);
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				} else {
					NumberFormat nf = DecimalFormat.getInstance();
					nf.setMaximumFractionDigits(0);
					nf.setGroupingUsed(false);
					return nf.format(cell.getNumericCellValue());
				}
			default:
				return null;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static HashMap<String, Audit> readExcelFile(DynamicFile df, Object value, int skipRow,
			String paymentChannelCode, String type, List<Audit> duplicateRecords) throws IOException {
		HashMap<String, Audit> res = new HashMap<>();

		String[] lstRule = df.getConfig().split("\\|");
		String[] fieldName = new String[lstRule.length];
		Integer[] position = new Integer[lstRule.length];
		for (int i = 0; i < lstRule.length; i++) {
			String[] eachRule = lstRule[i].split(":");
			fieldName[i] = eachRule[0];
			position[i] = Integer.valueOf(eachRule[1]);
		}
		File file = null;
		FileInputStream excelFile = null;
		try {
			if (value instanceof String) { // duong dan
				String path = (String) value;
				String status = "";
				if (path.contains(FileFormat.HUY.value())) {
					status = AuditEnum.FAIL.value();
				} else {
					status = AuditEnum.SUCCESS.value();
				}
				file = new File(path);
				excelFile = new FileInputStream(file);
				XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = datatypeSheet.iterator();
				// Ignore number skipRow row of Headers
				for (int i = 0; i < skipRow; i++) {
					iterator.next();
				}
				HashMap<String, Date> lstRecord = new HashMap<>();
				while (iterator.hasNext()) {
					try {
						Audit audit = new Audit();
						Row currentRow = iterator.next();
						if (CommonUtils.isRowEmpty(currentRow)) {
							continue;
						}
						Date day = null;
						if (paymentChannelCode.equals(ThirdParty.MOMO.value())) {
							audit = convertData(currentRow, fieldName, position, dateFormat);
							day = format.parse(audit.getAuditDate());
						} else if (paymentChannelCode.equals(ThirdParty.PAYOO.value())) {
							audit = convertData(currentRow, fieldName, position, payoo_format);
							day = dfTime.parse(audit.getAuditDate());
						} else {
							audit = convertData(currentRow, fieldName, position, vnptepay_format);
							day = dfVNPT.parse(audit.getAuditDate());
						}
						audit.setStatus(status);
						audit.setPaymentChannelCode(paymentChannelCode);
						audit.setType(type);
						audit.setPaymentAmount(audit.getPaymentAmount().replaceAll("\\D", ""));
						audit.setAuditDate(dfS.format(day));
						audit.setTimeControl(AuditEnum.TIME_CONTROL_24h.value());
						if (res.containsKey(audit.getPartnerRefId())) {
							if (day.after(lstRecord.get(audit.getPartnerRefId()))) {
								duplicateRecords.add(res.get(audit.getPartnerRefId()));
								res.put(audit.getPartnerRefId(), audit);
								lstRecord.put(audit.getPartnerRefId(), day);
							} else {
								duplicateRecords.add(audit);
							}
						} else {
							res.put(audit.getPartnerRefId(), audit);
							lstRecord.put(audit.getPartnerRefId(), day);
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != excelFile) {
				excelFile.close();
			}
			if (null != file) {
				// delete file
				file.delete();
			}
		}

		return res;
	}

	public static HashMap<String, Audit> convertDebtToAudit(List<DebtCollection> debtCollectionList) {
		HashMap<String, Audit> lstMcreditData = new HashMap<>();
		for (DebtCollection debtCollection : debtCollectionList) {
			try {
				Audit audit = new Audit();
				String day = dfS.format(debtCollection.getCreatedDate());
				audit.setAuditDate(day);
				audit.setContractId(debtCollection.getContractNumber());
				audit.setPartnerRefId(debtCollection.getPartnerRefid());
				audit.setPaymentAmount(String.valueOf(Long.valueOf(debtCollection.getPaymentAmount())));
				audit.setPaymentChannelCode(debtCollection.getPaymentChannelCode());
				if (debtCollection.getCancel().equals(1L)) { // 1 = true: da huy. mac dinh la 0: khong huy
					audit.setStatus(AuditEnum.FAIL.value());
				} else if (debtCollection.getCancel().equals(0L)){
					audit.setStatus(AuditEnum.SUCCESS.value());
				} else {
					audit.setStatus(AuditEnum.WAIT_PERMISSION_FAIL.value());
				}
				// audit.setTimeControl(debtCollection.get);
				audit.setType(AuditEnum.THU.value());
				// Check xem day la khoan vay hay the tin dung, mac dinh la khoan vay
				audit.setWorkFlow(AuditEnum.INSTALLMENT_LOAN.value());
				lstMcreditData.put(audit.getPartnerRefId(), audit);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return lstMcreditData;
	}

	public static HashMap<String, Audit> convertDisbursementToAudit(List<Disbursement> disbursementList) {
		HashMap<String, Audit> lstMcreditData = new HashMap<>();

		for (Disbursement disbursement : disbursementList) {
			try {
				Audit audit = new Audit();
				String day = dfS.format(df.parse(disbursement.getDisbursementDate()));
				audit.setAuditDate(day);
				audit.setContractId(disbursement.getContractNumber());
				audit.setPartnerRefId(disbursement.getPartnerRefid());
				audit.setPaymentAmount(String.valueOf(disbursement.getPaymentAmount()));
				audit.setPaymentChannelCode(disbursement.getPaymentChannelCode());
				// audit.setTimeControl(debtCollection.get);
				audit.setType(AuditEnum.CHI.value());
				audit.setStatus(AuditEnum.SUCCESS.value());
				// Check xem day la khoan vay hay the tin dung, mac dinh la khoan vay
				audit.setWorkFlow("0");
				lstMcreditData.put(audit.getPartnerRefId(), audit);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return lstMcreditData;
	}

	public void convertInputStreamToExcel(InputStream is, String filePath) throws IOException {
		byte[] buffer = new byte[8192];
		int read = 0;
		OutputStream out = null;

		try {
			// save file
			out = new FileOutputStream(new File(filePath));
			while ((read = is.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public static HashMap<String, Audit> getTxtFiles(InputStream in, String name, String time,
			List<Audit> duplicateRecords) throws IOException {
		HashMap<String, Audit> audits = new HashMap<>();
		BufferedReader reader = null;
		try {
			String type = AuditEnum.THU.value();
			int start = 52;
			int end = 84;
			if (name.contains(FileFormat.VNPOST_CHI.value())) {
				type = AuditEnum.CHI.value();
				start = 500;
				end = 700;
			} else if (name.contains(FileFormat.VNPOST_THU.value())) {
				type = AuditEnum.THU.value();
			}
			reader = new BufferedReader(new InputStreamReader(in, "UTF-16"));
			reader.readLine();
			String line;
			HashMap<String, Date> lstRecord = new HashMap<>();
			while ((line = reader.readLine()) != null) {
				try {
					Audit audit = new Audit();
					int paymentAmount = Integer.valueOf(line.substring(41, 52).trim());
					audit.setPaymentAmount("" + paymentAmount);
					audit.setPaymentChannelCode(ThirdParty.VNPOST.value());
					audit.setPartnerRefId(line.substring(22, 34).trim());
					audit.setContractId(line.substring(start, end).trim());
					Date day = dfVNPOST.parse(line.substring(8, 16) + line.substring(16, 22));
					String date = dfS.format(day);
					audit.setAuditDate(date);
					audit.setType(type);
					audit.setTimeControl(time);
					audit.setStatus(AuditEnum.SUCCESS.value());
					if (audits.containsKey(audit.getPartnerRefId())) {
						if (day.after(lstRecord.get(audit.getPartnerRefId()))) {
							duplicateRecords.add(audits.get(audit.getPartnerRefId()));
							audits.put(audit.getPartnerRefId(), audit);
							lstRecord.put(audit.getPartnerRefId(), day);
						} else {
							duplicateRecords.add(audit);
						}
					} else {
						audits.put(audit.getPartnerRefId(), audit);
						lstRecord.put(audit.getPartnerRefId(), day);
					}
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != reader) {
				reader.close();
			}
		}

		return audits;
	}

}
