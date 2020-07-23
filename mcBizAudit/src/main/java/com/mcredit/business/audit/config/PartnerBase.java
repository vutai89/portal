package com.mcredit.business.audit.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mcredit.business.audit.ftp.FTPFactory;
import com.mcredit.business.audit.ftp.IFTP;
import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerBase implements IPartner {
	private static String dateFormat = "yyyy/MM/dd HH:mm";
	private static String payoo_format = "dd/MM/yyyy HH:mm:ss";
	private static String vnptepay_format = "dd/MM/yyyy HH:mm";
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private static SimpleDateFormat dfS = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat dfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static SimpleDateFormat dfVNPT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private IFTP _ftp = null;

	public PartnerBase() {
		super();
	}

	public void readConfig(String config) throws Exception {
		_ftp = FTPFactory.create(config);
	}

	// get File from third Party
	@SuppressWarnings("deprecation")
	public HashMap<DynamicFile, Object> getFile(String day) throws Exception {

		try {
			_ftp.connect();
			return _ftp.getFile(day);
		} catch (Exception e) {
			throw e;
		} finally {
			this._ftp.disconnect();
		}
	}

	// read detail File
	public HashMap<String, Audit> readDetailFile(String day, List<Audit> duplicateRecords) throws Exception {
		throw new Exception("not implement.");
	}

	protected HashMap<String, Audit> readExcelFile(DynamicFile df, Object value, int skipRow, String paymentChannelCode,
			String type, List<Audit> duplicateRecords) throws IOException {
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
							audit = CommonUtils.convertData(currentRow, fieldName, position, vnptepay_format);
							day = dfVNPT.parse(audit.getAuditDate());
						} else if (paymentChannelCode.equals(ThirdParty.PAYOO.value())) {
							audit = CommonUtils.convertData(currentRow, fieldName, position, payoo_format);
							day = dfTime.parse(audit.getAuditDate());
						} else {
							audit = CommonUtils.convertData(currentRow, fieldName, position, vnptepay_format);
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
}
