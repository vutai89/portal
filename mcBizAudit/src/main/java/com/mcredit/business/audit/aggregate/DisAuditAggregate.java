package com.mcredit.business.audit.aggregate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import com.mcredit.business.audit.config.PartnerBase;
import com.mcredit.business.audit.config.IPartner;
import com.mcredit.business.audit.config.MBPartner;
import com.mcredit.business.audit.config.MomoPartner;
import com.mcredit.business.audit.config.PartnerFactory;
import com.mcredit.business.audit.config.PayooPartner;
import com.mcredit.business.audit.config.VNPOST17Partner;
import com.mcredit.business.audit.config.VNPostPartner;
import com.mcredit.business.audit.config.VnptEpayPartner;
import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.business.audit.utils.Converter;
import com.mcredit.business.audit.utils.ExcelProcessing;
import com.mcredit.common.Labels;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.audit.entity.AuditDifferent;
import com.mcredit.data.audit.entity.DebtCollection;
import com.mcredit.data.audit.entity.Disbursement;
import com.mcredit.data.audit.entity.ReportOverviewDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.audit.AuditResultDTO;
import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class DisAuditAggregate {

	private UnitOfWork _uok = null;

	private final static String dateFormat = "ddMMyyyy";
	private final static SimpleDateFormat dfVNPOST = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat df = new SimpleDateFormat(dateFormat);

	public DisAuditAggregate(UnitOfWork _uok) throws Exception {
		this._uok = _uok;
	}

	/**
	 * Service lay va doi soat du lieu hang ngay
	 * 
	 * @author linhtt2.ho
	 * @param date:
	 *            Ngay lay du lieu thirdParty: Doi tac lay du lieu override: co ghi
	 *            de du lieu cu khong
	 * @return return success
	 * @throws Exception:
	 *             loi khi lay file that bai hoac cau truc file khong hop le
	 */
	public ResponseSuccess importData(String date, String thirdParty, int override) throws Exception {

		if (StringUtils.isNullOrEmpty(thirdParty)) {
			thirdParty = ThirdParty.ALL.value();
		}
		if (StringUtils.isNullOrEmpty(date)) {
			Date day = df.parse(date);
			day = new Date(day.getTime() - 1);
			date = df.format(day);
		}

		List<String> tp = new ArrayList<>();
		HashMap<String, Audit> lstThirdPartyData = new HashMap<>();

		HashMap<String, Audit> lstMcreditData = new HashMap<>();

		if (thirdParty.equals(AuditEnum.VNPOST17.value())) {

			deleteRecord17(date);
			lstThirdPartyData = readThirdPartyData17(date);
			lstMcreditData = readMcreditData17(date);
			compareAndImportData(lstThirdPartyData, lstMcreditData, AuditEnum.TIME_CONTROL_17h.value());

		} else {
			lstThirdPartyData = readThirdPartyData(date, thirdParty, override, tp);
			if (null == lstThirdPartyData) {
				return new ResponseSuccess();
			}

			lstMcreditData = readMcreditData(date, thirdParty, tp);
			compareAndImportData(lstThirdPartyData, lstMcreditData, AuditEnum.TIME_CONTROL_24h.value());
		}

		return new ResponseSuccess();
	}

	private HashMap<String, Audit> readMcreditData17(String date) throws ParseException {
		HashMap<String, Audit> lstMcreditData = new HashMap<>();

		List<DebtCollection> debtCollectionLst = this._uok.audit.debtCollectionRepository()
				.getDebtCollection17ByDate(date);
		lstMcreditData.putAll(CommonUtils.convertDebtToAudit(debtCollectionLst));

		return lstMcreditData;
	}

	private HashMap<String, Audit> readThirdPartyData17(String date) throws Exception {
		IPartner partner = PartnerFactory.create(ThirdParty.VNPOST17.value());
		List<Audit> duplicateRecords = new ArrayList<>();
		HashMap<String,Audit> result = partner.readDetailFile(date, duplicateRecords);
		addDuplicate(duplicateRecords);
		return result;

	}

	private void deleteRecord17(String date) {
		this._uok.audit.auditPDCRepository().deleteRecord17(date);

	}

	private void compareAndImportData(HashMap<String, Audit> lstThirdPartyData, HashMap<String, Audit> lstMcreditData, String time_control)
			throws ParseException {

		HashMap<String, Integer> lst = new HashMap<>();
		lst = this._uok.audit.auditPDCRepository().getMapThirdParty();
		List<String> lstCp = new ArrayList<>();
		for (String key : lstMcreditData.keySet()) {
			try {
				ConsolidatePaymentDTO auditDiff = new ConsolidatePaymentDTO();
				Audit audit = lstMcreditData.get(key);
				if (!lstThirdPartyData.containsKey(key)) {
					auditDiff = Converter.convertEmptyTP(audit, String.valueOf(lst.get(audit.getPaymentChannelCode())), time_control);
				} else {
					auditDiff = Converter.convertCompare(audit, lstThirdPartyData.get(key), String.valueOf(lst.get(audit.getPaymentChannelCode())), time_control);
				}
				_uok.audit.auditPDCRepository().add(auditDiff);
				lstCp.add(key);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		for (String key : lstCp) {
			if (lstThirdPartyData.containsKey(key)) {
				lstThirdPartyData.remove(key);
			}
		}

		if (!lstThirdPartyData.isEmpty()) {
			for (String key : lstThirdPartyData.keySet()) {
				try {
					Audit audit = lstThirdPartyData.get(key);
					ConsolidatePaymentDTO auditDiff = Converter.convertEmptyMC(audit, String.valueOf(lst.get(audit.getPaymentChannelCode())), time_control);
					_uok.audit.auditPDCRepository().add(auditDiff);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		// flush data
		this._uok.audit.auditPDCRepository().flush();
	}

	private HashMap<String, Audit> readThirdPartyData(String date, String thirdParty, int override, List<String> tp)
			throws Exception {
		
		HashMap<String, Audit> lstThirdPartyData = new HashMap<>();
		List<Audit> duplicateRecords = new ArrayList<>();
		deleteRecords(date, thirdParty);
		
		IPartner partner = PartnerFactory.create(thirdParty);
		lstThirdPartyData.putAll(partner.readDetailFile(date, duplicateRecords));
		
		
		addDuplicate(duplicateRecords);

		return lstThirdPartyData;
	}

	private void addDuplicate(List<Audit> duplicateRecords) throws ParseException {
		HashMap<String, Integer> lst = this._uok.audit.auditPDCRepository().getMapThirdParty();
		for (Audit audit : duplicateRecords) {
			audit.setPaymentChannelCode(String.valueOf(lst.get(audit.getPaymentChannelCode())));
			this._uok.audit.auditPDCRepository().add(Converter.convertThirdParty(audit));
		}

	}

	private void deleteRecords(String date, String thirdParty) throws ParseException {
		_uok.audit.auditPDCRepository().deleteRecords(date, thirdParty);
	}

	private HashMap<String, Audit> readMcreditData(String date, String thirdParty, List<String> tp) throws Exception {
		HashMap<String, Audit> lstMcreditData = new HashMap<>();

		Date day = df.parse(date);
		if (DateUtil.getDayOfMonth(day) == 1 && thirdParty.equals(ThirdParty.MB.value())) {
			Date yesterday = new Date(day.getTime() - 1);
			// delete MB compare information
			_uok.audit.auditPDCRepository().deleteMBRecords(yesterday);
			List<Disbursement> disbursementList = _uok.audit.disbursementRepository().getAllMBDisbursement(yesterday);
			List<DebtCollection> debtCollectionList = _uok.audit.debtCollectionRepository()
					.getAllMBDebtCollection(yesterday);
			lstMcreditData.putAll(CommonUtils.convertDebtToAudit(debtCollectionList));
			lstMcreditData.putAll(CommonUtils.convertDisbursementToAudit(disbursementList));
		}
		// Lay ra list thu va chi ho cua Mcredit
		if (!tp.contains(thirdParty)) {
			List<DebtCollection> debtCollectionList = _uok.audit.debtCollectionRepository()
					.getDebtCollectionByDate(date, thirdParty, tp);
			if (thirdParty.equals(ThirdParty.ALL.value()) || thirdParty.equals(ThirdParty.MB.value())
					|| thirdParty.equals(ThirdParty.VNPOST.value())) {
				List<Disbursement> disbursementList = _uok.audit.disbursementRepository().getDisbursementByDate(date,
						thirdParty, tp);
				lstMcreditData.putAll(CommonUtils.convertDisbursementToAudit(disbursementList));
			}

			lstMcreditData.putAll(CommonUtils.convertDebtToAudit(debtCollectionList));
		}

		return lstMcreditData;
	}

	public List<String> getTpDownloaded(String date, String thirdParty) throws ParseException {
		return _uok.audit.auditPDCRepository().getTpDownloaded(date, thirdParty);
	}

	/**
	 * Service thuc hien lay thong tin bao cao duoi dang excel
	 * 
	 * @author linhtt2.ho
	 * @param fromDate:
	 *            Ngay bat dau lay du lieu toDate: Ngay ket thuc lay du lieu
	 *            thirdParty: Doi tac lay du lieu reportType: Loai bao cao thu/chi
	 * @return return file bao cao excel
	 * @throws Exception
	 */
	public File getReport(String thirdParty, String fromDate, String toDate, String reportType) throws IOException {

		if (reportType.equals(AuditEnum.TONG_HOP.value())) {
			// read Value
			List<ReportOverviewDTO> reports = this._uok.audit.auditPDCRepository().getReport(thirdParty, fromDate,
					toDate);

			HashMap<String, AuditResultDTO> result = new HashMap<>();
			for (ReportOverviewDTO report : reports) {
				AuditResultDTO auditReturn;
				if (!result.containsKey(report.getType())) {
					auditReturn = new AuditResultDTO();
					result.put(report.getType(), auditReturn);
				} else {
					auditReturn = result.get(report.getType());
				}
				if (report.getResult().equals(AuditEnum.SUCCESS.value())) {
					if (null != report.getSumMc()) {
						auditReturn.setMatchTotalMoney(auditReturn.getMatchTotalMoney() + report.getSumMc());
						auditReturn.setMcreditTotalMoney(auditReturn.getMcreditTotalMoney() + report.getSumMc());
						auditReturn.setThirdPartyTotalMoney(auditReturn.getThirdPartyTotalMoney() + report.getSumMc());
					}
					auditReturn.setMatchTotalDeal(auditReturn.getMatchTotalDeal() + report.getCountMc());
					auditReturn.setMcreditTotalDeal(auditReturn.getMcreditTotalDeal() + report.getCountMc());
					auditReturn.setThirdPartyTotalDeal(auditReturn.getThirdPartyTotalDeal() + report.getCountMc());
				} else {
					if (null != report.getCountMc()) {
						auditReturn.setMcreditTotalDeal(auditReturn.getMcreditTotalDeal() + report.getCountMc());
					}
					if (null != report.getSumMc()) {
						auditReturn.setMcreditTotalMoney(auditReturn.getMcreditTotalMoney() + report.getSumMc());
					}
					if (null != report.getCountTp()) {
						auditReturn.setThirdPartyTotalDeal(auditReturn.getThirdPartyTotalDeal() + report.getCountTp());
					}
					if (null != report.getSumTp()) {
						auditReturn.setThirdPartyTotalMoney(auditReturn.getThirdPartyTotalMoney() + report.getSumTp());
					}
				}
				auditReturn
						.setUnMatchTotalDeal(auditReturn.getThirdPartyTotalDeal() - auditReturn.getMcreditTotalDeal());
				auditReturn.setUnMatchTotalMoney(
						auditReturn.getThirdPartyTotalMoney() - auditReturn.getMcreditTotalMoney());
			}
			AuditResultDTO audit = new AuditResultDTO();
			for (String key : result.keySet()) {
				AuditResultDTO res = result.get(key);
				audit.setMatchTotalDeal(audit.getMatchTotalDeal() + res.getMatchTotalDeal());
				audit.setMatchTotalMoney(audit.getMatchTotalMoney() + res.getMatchTotalMoney());
				audit.setMcreditTotalDeal(audit.getMcreditTotalDeal() + res.getMcreditTotalDeal());
				audit.setMcreditTotalMoney(audit.getMcreditTotalMoney() + res.getMcreditTotalMoney());
				audit.setThirdPartyTotalDeal(audit.getThirdPartyTotalDeal() + res.getThirdPartyTotalDeal());
				audit.setThirdPartyTotalMoney(audit.getThirdPartyTotalMoney() + res.getThirdPartyTotalMoney());
				audit.setUnMatchTotalDeal(audit.getUnMatchTotalDeal() + res.getUnMatchTotalDeal());
				audit.setUnMatchTotalMoney(audit.getUnMatchTotalMoney() + res.getUnMatchTotalMoney());
			}
			result.put(AuditEnum.TOTAL.value(), audit);

			// create excel file
			return ExcelProcessing.getInstance().generateExcelFile(result, thirdParty, fromDate, toDate);
		} else {
			List<AuditDifferent> report = new ArrayList<>();
			if (reportType.equals(AuditEnum.BAO_CAO_CHI_HO.value())) { // chi ho

				report = this._uok.audit.auditPDCRepository().getDetailDifferent(thirdParty, fromDate, toDate,
						AuditEnum.CHI.value(),
						CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.CUST_IDTYP_1, CTCat.IDENTITY_TYPE_ID));
			} else if (reportType.equals(AuditEnum.BAO_CAO_THU_HO.value())) {

				report = this._uok.audit.auditPDCRepository().getDetailDifferent(thirdParty, fromDate, toDate,
						AuditEnum.THU.value(),
						CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.CUST_IDTYP_1, CTCat.IDENTITY_TYPE_ID));
			}

			return ExcelProcessing.getInstance().generateDetailExcelFile(report, thirdParty, fromDate, toDate, reportType);
		}
	}

	
}