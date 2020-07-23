package com.mcredit.business.audit.aggregate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.business.audit.utils.Converter;
import com.mcredit.business.audit.utils.Validation;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.audit.entity.DebtCollection;
import com.mcredit.data.audit.entity.Disbursement;
import com.mcredit.model.dto.audit.AuditCommandDTO;
import com.mcredit.model.dto.audit.AuditDuplicateDTO;
import com.mcredit.model.dto.audit.AuditResultDTO;
import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;
import com.mcredit.model.dto.audit.OverviewResultDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DetailReturnResult;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.model.object.audit.GeneralReturnResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

import scala.collection.mutable.StringBuilder;

public class ConsolidateAggregate {
	private UnitOfWork _uok = null;

	private final static SimpleDateFormat dfMB = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private final static SimpleDateFormat dfS = new SimpleDateFormat("dd/MM/yyyy");
	private ObjectMapper mapper = new ObjectMapper();

	public ConsolidateAggregate(UnitOfWork _uok) throws Exception {
		this._uok = _uok;
	}

	/**
	 * Service lay thong tin bao cao: thong tin tong quan
	 * 
	 * @author linhtt2.ho
	 * @param file:
	 *            File bao cao cua Viettel (neu thuc hien doi soat Viettel) payload:
	 *            Noi dung thong tin day len de lay bao cao
	 * @return return json chua thong tin tong quan
	 * @throws Exception
	 */
	public GeneralReturnResult auditResult(InputStream uploadedInputStream, AuditCommandDTO auditCD, String fileName)
			throws Exception {

		// import Viettel data from file
		if (auditCD.getThirdParty().equals(ThirdParty.VIETTEL.value())) {
			compareViettel(uploadedInputStream, auditCD, fileName);
		}

		return returnGeneralResult(auditCD);
	}

	private GeneralReturnResult returnGeneralResult(AuditCommandDTO auditCD) throws ParseException {
		List<AuditResultDTO> result = new ArrayList<>();
		// get number of date
		BigDecimal num_page = this._uok.audit.auditPDCRepository().getDayCountReport(auditCD);
		long page = 0L;
		if (null != num_page) {
			page = num_page.longValue();
		}

		int max_page = (int) Math.ceil((double) page / auditCD.getPageSize());
		if (max_page == auditCD.getPageNum()) {
			// read total data
			getTotalData(auditCD, result);
		}
		List<AuditDuplicateDTO> auditDuplicate = new ArrayList<>();
		if (auditCD.getPageNum() == 1) {
			auditDuplicate = this._uok.audit.auditPDCRepository().getDuplicate(auditCD);
			Converter.changeValue(auditDuplicate);
		}

		// get fromDate and toDate through pageNum and pageSize
		this._uok.audit.auditPDCRepository().getIntervalDate(auditCD);

		// read third party data through pagesize and pagenum
		compareThirdParty(auditCD, result);

		Comparator<AuditResultDTO> auditComparator = new Comparator<AuditResultDTO>() {
			@Override
			public int compare(AuditResultDTO e1, AuditResultDTO e2) {
				if (e1.getDate().equals(AuditEnum.TOTAL.value())) {
					return 1;
				} else if (e2.getDate().equals(AuditEnum.TOTAL.value())) {
					return -1;
				} else {
					try {
						if (dfS.parse(e1.getDate()).after(dfS.parse(e2.getDate()))) {
							return 1;
						} else {
							return -1;
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				return 0;
			}
		};

		Collections.sort(result, auditComparator);

		GeneralReturnResult res = new GeneralReturnResult(page, result, auditDuplicate);

		return res;
	}

	private void getTotalData(AuditCommandDTO auditCD, List<AuditResultDTO> result) {
		AuditResultDTO res = new AuditResultDTO();
		res.setDate(AuditEnum.TOTAL.value());

		// get total match
		BigDecimal total_match_deal = this._uok.audit.auditPDCRepository().getTotalMatchDeal(auditCD);
		if (null == total_match_deal) {
			res.setMatchTotalDeal(0L);
		} else {
			res.setMatchTotalDeal(total_match_deal.longValue());
		}
		BigDecimal total_match_money = this._uok.audit.auditPDCRepository().getTotalMatchMoney(auditCD);
		if (null == total_match_money) {
			res.setMatchTotalMoney(0L);
		} else {
			res.setMatchTotalMoney(total_match_money.longValue());
		}

		// get total mcredit
		BigDecimal unmatch_mcredit = this._uok.audit.auditPDCRepository().getTotalUnMatchMcreditDeal(auditCD);
		if (null == unmatch_mcredit) {
			res.setMcreditTotalDeal(res.getMatchTotalDeal());
		} else {
			res.setMcreditTotalDeal(res.getMatchTotalDeal() + unmatch_mcredit.longValue());
		}
		BigDecimal unmatch_money_mcredit = this._uok.audit.auditPDCRepository().getTotalUnMatchMcreditMoney(auditCD);
		if (null == unmatch_money_mcredit) {
			res.setMcreditTotalMoney(res.getMatchTotalMoney());
		} else {
			res.setMcreditTotalMoney(res.getMatchTotalMoney() + unmatch_money_mcredit.longValue());
		}

		// get total third party
		BigDecimal unmatch_tp = this._uok.audit.auditPDCRepository().getTotalUnMatchTPDeal(auditCD);
		if (null == unmatch_tp) {
			res.setThirdPartyTotalDeal(res.getMatchTotalDeal());
		} else {
			res.setThirdPartyTotalDeal(res.getMatchTotalDeal() + unmatch_tp.longValue());
		}
		BigDecimal unmatch_money_tp = this._uok.audit.auditPDCRepository().getTotalUnMatchTPMoney(auditCD);
		if (null == unmatch_money_tp) {
			res.setThirdPartyTotalMoney(res.getMatchTotalMoney());
		} else {
			res.setThirdPartyTotalMoney(res.getMatchTotalMoney() + unmatch_money_tp.longValue());
		}

		res.setUnMatchTotalDeal(res.getThirdPartyTotalDeal() - res.getMcreditTotalDeal());
		res.setUnMatchTotalMoney(res.getThirdPartyTotalMoney() - res.getMcreditTotalMoney());
		result.add(res);
	}

	private void compareThirdParty(AuditCommandDTO auditCD, List<AuditResultDTO> result) {
		// TODO Auto-generated method stub
		List<OverviewResultDTO> lst = this._uok.audit.auditPDCRepository().getGeneralReport(auditCD);
		HashMap<String, AuditResultDTO> generalResult = new HashMap<>();
		for (OverviewResultDTO res : lst) {
			AuditResultDTO auditResult;
			String date = dfS.format(res.getDate());
			if (!generalResult.containsKey(date)) {
				auditResult = new AuditResultDTO();
				generalResult.put(date, auditResult);
			} else {
				auditResult = generalResult.get(date);
			}
			if (res.getResult().equals(AuditEnum.EQUAL.value())) {
				if (null != res.getCountMc()) {
					auditResult.setMatchTotalDeal(auditResult.getMatchTotalDeal() + res.getCountMc());
					auditResult.setMcreditTotalDeal(auditResult.getMcreditTotalDeal() + res.getCountMc());
					auditResult.setThirdPartyTotalDeal(auditResult.getThirdPartyTotalDeal() + res.getCountMc());
				}
				if (null != res.getSumMc()) {
					auditResult.setMatchTotalMoney(auditResult.getMatchTotalMoney() + res.getSumMc());
					auditResult.setMcreditTotalMoney(auditResult.getMcreditTotalMoney() + res.getSumMc());
					auditResult.setThirdPartyTotalMoney(auditResult.getThirdPartyTotalMoney() + res.getSumMc());
				}
			} else {
				if (null != res.getCountTp()) {
					auditResult.setThirdPartyTotalDeal(auditResult.getThirdPartyTotalDeal() + res.getCountTp());
				}
				if (null != res.getCountMc()) {
					auditResult.setMcreditTotalDeal(auditResult.getMcreditTotalDeal() + res.getCountMc());
				}
				if (null != res.getSumTp()) {
					auditResult.setThirdPartyTotalMoney(auditResult.getThirdPartyTotalMoney() + res.getSumTp());
				}
				if (null != res.getSumMc()) {
					auditResult.setMcreditTotalMoney(auditResult.getMcreditTotalMoney() + res.getSumMc());
				}
				auditResult
						.setUnMatchTotalDeal(auditResult.getThirdPartyTotalDeal() - auditResult.getMcreditTotalDeal());
				auditResult.setUnMatchTotalMoney(
						auditResult.getThirdPartyTotalMoney() - auditResult.getMcreditTotalMoney());
			}
		}

		for (String date : generalResult.keySet()) {
			AuditResultDTO audit = generalResult.get(date);
			audit.setDate(date);
			result.add(audit);
		}
	}

	private StringBuilder compareViettel(InputStream uploadedInputStream, AuditCommandDTO auditCD, String fileName)
			throws Exception {

		StringBuilder day = new StringBuilder();
		List<Audit> duplicates = new ArrayList<>();

		// get Viettel data
		HashMap<String, Audit> vtData = getViettelData(uploadedInputStream, auditCD, fileName, duplicates);
		addDuplicate(duplicates);

		// get MC data
		HashMap<String, Audit> mcData = getMcreditViettelData(auditCD);

		// delete current Viettel records
		deleteRecords(auditCD.getFromDate(), auditCD.getToDate(), ThirdParty.VIETTEL.value(), auditCD.getType());

		compareAndImportData(vtData, mcData, AuditEnum.TIME_CONTROL_24h.value());

		return day;
	}

	private void addDuplicate(List<Audit> duplicateRecords) throws ParseException {
		HashMap<String, Integer> lst = this._uok.audit.auditPDCRepository().getMapThirdParty();
		for (Audit audit : duplicateRecords) {
			audit.setPaymentChannelCode(String.valueOf(lst.get(audit.getPaymentChannelCode())));
			this._uok.audit.auditPDCRepository().add(Converter.convertThirdParty(audit));
		}

	}

	private HashMap<String, Audit> getMcreditViettelData(AuditCommandDTO auditCD) throws Exception {

		if (auditCD.getType().equals(AuditEnum.THU.value())) {
			List<DebtCollection> debtCollectionList = _uok.audit.debtCollectionRepository()
					.getListDC(auditCD.getFromDate(), auditCD.getToDate(), auditCD.getThirdParty());
			return CommonUtils.convertDebtToAudit(debtCollectionList);
		} else if (auditCD.getType().equals(AuditEnum.CHI.value())) {
			List<Disbursement> disbursementList = _uok.audit.disbursementRepository()
					.getListDisbursement(auditCD.getFromDate(), auditCD.getToDate(), auditCD.getThirdParty());
			return CommonUtils.convertDisbursementToAudit(disbursementList);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private HashMap<String, Audit> getViettelData(InputStream uploadedInputStream, AuditCommandDTO auditCD,
			String fileName, List<Audit> duplicates) throws IOException, ParseException, ValidationException {
		List<String> wrongRecords = new ArrayList<>();
		HashMap<String, Audit> lstAudit = new HashMap<>();
		try {
			List<DynamicFile> dfs = mapper.readValue(
					CacheManager.Parameters().findParamValueAsString(ParametersName.AUDIT_VIETTEL_CONFIG),
					new TypeReference<List<DynamicFile>>() {
					});
			DynamicFile df = new DynamicFile();
			String type = "";
			Workbook wb = null;
			if (fileName.endsWith(".xls")) {
				wb = new HSSFWorkbook(uploadedInputStream);
			} else if (fileName.endsWith(".xlsx")) {
				wb = new XSSFWorkbook(uploadedInputStream);
			}
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> iterator = sheet.iterator();
			if (auditCD.getType().equals(AuditEnum.CHI.value())) {
				for (int i = 0; i < 3; i++) {
					iterator.next();
				}
				Row currentRow = iterator.next();
				Validation.validateExcel(currentRow.getCell(1).getStringCellValue(), auditCD);
				for (int i = 0; i < 4; i++) {
					iterator.next();
				}

				currentRow = iterator.next();
				validateChiRow(currentRow);
				df = getDynamicFile(dfs, "chi");
				type = AuditEnum.CHI.value();

				lstAudit = readExcelFile(iterator, df, type, duplicates);
			} else if (auditCD.getType().equals(AuditEnum.THU.value())) {
				Row currentRow1 = iterator.next();
				validateThuRow(currentRow1);
				df = getDynamicFile(dfs, "thu");
				type = AuditEnum.THU.value();
				lstAudit = readExcelFile(iterator, df, type, duplicates);
			}

			Date startDate = dfS.parse(auditCD.getFromDate());
			Date endDate = dfS.parse(auditCD.getToDate());
			// remove records not from startDate to endDate
			for (String key : lstAudit.keySet()) {
				try {
					if (null == lstAudit.get(key).getAuditDate()) {
						wrongRecords.add(key);
					} else {
						Date cur = dfS.parse(lstAudit.get(key).getAuditDate());
						if (cur.before(startDate) || cur.after(endDate)) {
							wrongRecords.add(key);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			for (String key : wrongRecords) {
				lstAudit.remove(key);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			IOUtils.closeQuietly(uploadedInputStream);
		}

		return lstAudit;
	}

	private void deleteRecords(String fromDate, String toDate, String thirdParty, String type) throws ParseException {
		_uok.audit.auditPDCRepository().deleteRecords(fromDate, toDate, thirdParty, type);
	}

	private void compareAndImportData(HashMap<String, Audit> lstThirdPartyData, HashMap<String, Audit> lstMcreditData,
			String time_control) throws ParseException {

		HashMap<String, Integer> lst = new HashMap<>();
		lst = this._uok.audit.auditPDCRepository().getMapThirdParty();
		List<String> lstCp = new ArrayList<>();
		for (String key : lstMcreditData.keySet()) {
			try {
				ConsolidatePaymentDTO auditDiff = new ConsolidatePaymentDTO();
				Audit audit = lstMcreditData.get(key);
				if (!lstThirdPartyData.containsKey(key)) {
					auditDiff = Converter.convertEmptyTP(audit, String.valueOf(lst.get(audit.getPaymentChannelCode())),
							time_control);
				} else {
					auditDiff = Converter.convertCompare(audit, lstThirdPartyData.get(key),
							String.valueOf(lst.get(audit.getPaymentChannelCode())), time_control);
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
					ConsolidatePaymentDTO auditDiff = Converter.convertEmptyMC(audit,
							String.valueOf(lst.get(audit.getPaymentChannelCode())), time_control);
					_uok.audit.auditPDCRepository().add(auditDiff);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		// flush data
		this._uok.audit.auditPDCRepository().flush();
	}

	private DynamicFile getDynamicFile(List<DynamicFile> dfs, String name) {
		for (DynamicFile df : dfs) {
			if (df.getFileName().contains(name)) {
				return df;
			}
		}
		return null;
	}

	private HashMap<String, Audit> readExcelFile(Iterator<Row> iterator, DynamicFile df, String type,
			List<Audit> duplicates) throws IOException {
		HashMap<String, Audit> res = new HashMap<>();

		// read config
		String[] lstRule = df.getConfig().split("\\|");
		String[] fieldName = new String[lstRule.length];
		Integer[] position = new Integer[lstRule.length];
		for (int i = 0; i < lstRule.length; i++) {
			String[] eachRule = lstRule[i].split(":");
			fieldName[i] = eachRule[0];
			position[i] = Integer.valueOf(eachRule[1]);
		}

		HashMap<String, Date> lstRecord = new HashMap<>();
		while (iterator.hasNext()) {
			try {
				Audit audit = new Audit();
				Row currentRow = iterator.next();
				if (CommonUtils.isRowEmpty(currentRow)) {
					continue;
				}
				audit = CommonUtils.convertData(currentRow, fieldName, position, "dd/MM/yyyy HH:mm:ss"); // 9 - 10
				if (!StringUtils.isNullOrEmpty(audit.getPartnerRefId())
						&& !StringUtils.isNullOrEmpty(audit.getContractId())) {
					audit.setPaymentChannelCode(ThirdParty.VIETTEL.value());
					audit.setType(type);
					Date day = dfMB.parse(audit.getAuditDate());
					String date = dfS.format(day);
					audit.setAuditDate(date);
					audit.setTimeControl(AuditEnum.TIME_CONTROL_24h.value());
					if (res.containsKey(audit.getPartnerRefId())) {
						if (day.after(lstRecord.get(audit.getPartnerRefId()))) {
							duplicates.add(res.get(audit.getPartnerRefId()));
							res.put(audit.getPartnerRefId(), audit);
							lstRecord.put(audit.getPartnerRefId(), day);
						} else {
							duplicates.add(audit);
						}
					} else {
						res.put(audit.getPartnerRefId(), audit);
						lstRecord.put(audit.getPartnerRefId(), day);
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}

		return res;
	}

	public DetailReturnResult getResult(String thirdParty, String fromDate, String toDate, String reportType,
			String result, String workflow, String time, int pageSize, int pageNum) {
		List<ConsolidatePaymentDTO> lst = new ArrayList<>();

		BigDecimal num_page = this._uok.audit.auditPDCRepository().getNumDetailReport(thirdParty, fromDate, toDate,
				reportType, result, workflow, time);
		long num = 0L;
		if (null != num_page) {
			num = num_page.longValue();
		}

		lst = this._uok.audit.auditPDCRepository().getDetailReport(thirdParty, fromDate, toDate, reportType, result,
				workflow, time, pageSize, pageNum);
		changeValue(lst);

		DetailReturnResult return_result = new DetailReturnResult(num, lst);

		return return_result;
	}

	private void changeValue(List<ConsolidatePaymentDTO> lstAudit) {
		HashMap<String, String> listResult = getListResult();
		for (ConsolidatePaymentDTO cs : lstAudit) {
			if (null != cs.getMcStatus()) {
				if (cs.getMcStatus().equals(AuditEnum.SUCCESS.value())) {
					cs.setMcStatus(Labels.getString("label.audit.status.success"));
				} else if (cs.getMcStatus().equals(AuditEnum.FAIL.value())) {
					cs.setMcStatus(Labels.getString("label.audit.status.fail"));
				} else if (cs.getMcStatus().equals(AuditEnum.WAIT_PERMISSION_FAIL.value())) {
					cs.setMcStatus(Labels.getString("label.audit.status.waitpermissionfail"));
				}
			}
			if (null != cs.getType()) {
				if (cs.getType().equals(AuditEnum.THU.value())) {
					cs.setType("THU");
				} else if (cs.getType().equals(AuditEnum.CHI.value())) {
					cs.setType("CHI");
				}
			}
			if (null != cs.getTpStatus()) {
				if (cs.getTpStatus().equals(AuditEnum.SUCCESS.value())) {
					cs.setTpStatus(Labels.getString("label.audit.status.success"));
				} else if (cs.getTpStatus().equals(AuditEnum.FAIL.value())) {
					cs.setTpStatus(Labels.getString("label.audit.status.fail"));
				}
			}
			if (null != cs.getResult()) {
				if (cs.getResult().equals(AuditEnum.EQUAL.value())) {
					cs.setResult(Labels.getString("label.audit.status.equal"));
				}
				String[] lstRes = cs.getResult().split("\\|");
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < lstRes.length; i++) {
					result.append(listResult.get(lstRes[i])).append(" | ");
				}
				cs.setResult(result.substring(0, result.length() - 3));
			}
		}
	}

	private HashMap<String, String> getListResult() {
		HashMap<String, String> lst = new HashMap<>();
		lst.put("1", Labels.getString("label.audit.result.notExistMC"));
		lst.put("2", Labels.getString("label.audit.result.notExistTP"));
		lst.put("3", Labels.getString("label.audit.result.notEqualMoney"));
		lst.put("4", Labels.getString("label.audit.result.notEqualStatus"));
		lst.put("5", Labels.getString("label.audit.result.notEqualContractId"));
		lst.put("6", Labels.getString("label.audit.result.notEqualContractDate"));
		lst.put("7", Labels.getString("label.audit.result.notEqualType"));
		return lst;
	}

	private void validateChiRow(Row currentRow) throws ValidationException {
		try {
			if (!Validation.compareString(currentRow.getCell(1).getStringCellValue(),
					Labels.getString("label.audit.excel.stt"))
					|| !Validation.compareString(currentRow.getCell(4).getStringCellValue(),
							Labels.getString("label.audit.excel.partnerRefId"))
					|| !Validation.compareString(currentRow.getCell(6).getStringCellValue(),
							Labels.getString("label.audit.excel.contractid"))
					|| !Validation.compareString(currentRow.getCell(7).getStringCellValue(),
							Labels.getString("label.audit.excel.out.money"))
					|| !Validation.compareString(currentRow.getCell(8).getStringCellValue(),
							Labels.getString("label.audit.excel.contractdate"))) {
				throw new ValidationException(
						Messages.getString("validation.field.invalidFormat", Labels.getString("label.cp.filename")));
			}

		} catch (Exception ex) {
			throw new ValidationException(
					Messages.getString("validation.field.invalidFormat", Labels.getString("label.cp.filename")));
		}
	}

	private void validateThuRow(Row currentRow1) throws ValidationException {
		try {
			if (!Validation.compareString(currentRow1.getCell(0).getStringCellValue(),
					Labels.getString("label.audit.excel.stt"))
					|| !Validation.compareString(currentRow1.getCell(2).getStringCellValue(),
							Labels.getString("label.audit.excel.dc.partnerRefId"))
					|| !Validation.compareString(currentRow1.getCell(4).getStringCellValue(),
							Labels.getString("label.audit.excel.dc.contractid"))
					|| !Validation.compareString(currentRow1.getCell(6).getStringCellValue(),
							Labels.getString("label.audit.excel.dc.money"))
					|| !Validation.compareString(currentRow1.getCell(7).getStringCellValue(),
							Labels.getString("label.audit.excel.dc.contractdate"))) {
				throw new ValidationException(
						Messages.getString("validation.field.invalidFormat", Labels.getString("label.cp.filename")));
			}
		} catch (Exception ex) {
			throw new ValidationException(
					Messages.getString("validation.field.invalidFormat", Labels.getString("label.cp.filename")));
		}
	}
}
