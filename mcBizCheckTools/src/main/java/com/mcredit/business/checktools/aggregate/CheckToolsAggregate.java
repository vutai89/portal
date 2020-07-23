package com.mcredit.business.checktools.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.FinanceLib;

import com.google.common.reflect.TypeToken;
import com.mcredit.business.checkcic.aggregate.CheckCICAggregate;
import com.mcredit.business.checktools.callout.EsbApi;
import com.mcredit.business.checktools.convert.Converter;
import com.mcredit.business.checktools.object.ContractInfo;
import com.mcredit.business.checktools.object.EMIContractApproved;
import com.mcredit.business.checktools.utils.Constants;
import com.mcredit.business.customer.CustomerManager;
import com.mcredit.business.pcb.aggregate.PCBAggregate;
import com.mcredit.business.pcb.jsonobject.PcbInfo;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.checktools.ConditionInitContract;
import com.mcredit.model.dto.cic.CICDetailDTO;
import com.mcredit.model.dto.pcb.IdCheckPcbDTO;
import com.mcredit.model.dto.pcb.request.RIReqInput;
import com.mcredit.model.dto.product.ProductCategoryEnum;
import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleOutputList;
import com.mcredit.model.object.RuleResult;
import com.mcredit.model.object.TotalDebt;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CheckToolsAggregate {
	
	private static final Logger loggerPrecheckTool = LogManager.getLogger(Constants.LOGGER_PRE_CHECK_TOOL);

	public static final String INSTALLMENT_LOAN = "InstallmentLoan";
	public static final String CASH_LOAN = "CashLoan";
	public static final Long MAX_LOAN_LIMIT_DEFAULT = 100000000L;
	public static final String STATUS_CODE_OK = "200";
	public static final String STATUS_CODE_400 = "400";
	public static final String STATUS_CODE_401 = "401";
	public static final String STATUS_CODE_500 = "500";
	
	private UnitOfWork _uok = null;
	private EsbApi esbApi = null;
	
	public CheckToolsAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		esbApi = new EsbApi();
	}
	
	/**
	 * Kiem tra thong tin cmnd co hop le khong: blacklist, duplicate hop dong, cic s37, tong du no qua 100M
	 * @param productGroup
	 * @param citizenId
	 * @param loanAmount
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	public Object checkCitizenId(String productGroup, String citizenId, Long loanAmount, String appNumber, String saleCode) throws Exception {
		
		Long start = System.currentTimeMillis();
		Set<String> listError = new HashSet<String>();
		String returnMsg = "CMND đạt điều kiện nhập thông tin";
		
		/* check black list first */
		if (checkBlacklist(citizenId)) {
			System.out.println("Check citizenId, in blacklist: " + citizenId + " in " + (System.currentTimeMillis() - start) + "ms");
			listError.add("Khách hàng không đủ điều kiện vay!");
			loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_400, "Check black list", Converter.buildErrorMsg(listError), appNumber, saleCode);
			return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError)); 
		}
		
		/* check duplicate citizenid */
		// Get info from data warehouse
		Long t1 = System.currentTimeMillis();
		List<ContractInfo> listContractDuplicate = checkDuplicateCitizenId(citizenId, appNumber);
		if (null != listContractDuplicate && listContractDuplicate.size() > 0) {
			// lay message thong bao TH success nhung co hop dong cu tra dung 3 ky
			String msg = Converter.getDuplicateContractMessageFor3PaymentTenor(listContractDuplicate);
			if (!StringUtils.isNullOrEmpty(msg))
				returnMsg = msg;
			
			for (ContractInfo ci : listContractDuplicate) {
				String errMsg = Converter.getDuplicateContractMessage(ci.getDescription());
				// chi add loi khi khong roi vao truong hop tra dung 3 ky: PAYMENT_TENOR_EQUAL_3. TH tra dung 3 ky khong phai TH loi
				if (!StringUtils.isNullOrEmpty(errMsg))
					listError.add(errMsg);
			}
			
			if (!listError.isEmpty()) {
				System.out.println("Check citizenId, contract duplicate error: citizenId=" + citizenId + ", appNumber=" + appNumber + " in " + (t1-start) + " + " + (System.currentTimeMillis()-t1) + "ms");
				loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_400, "Check DUP", Converter.buildErrorMsg(listError), appNumber, saleCode);
				return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError));
			}
		}
		
		/* check cic S37 */
		Long t2 = System.currentTimeMillis();
		if (CASH_LOAN.equalsIgnoreCase(productGroup)) {		// chi check cic s37 o luong tien mat
			String cicResult = checkCIC(citizenId);
			if (StringUtils.isNullOrEmpty(cicResult)) {
				System.out.println("Check citizenId, cic result not found: " + citizenId + " in " + (t1-start) + " + " + (t2-t1) + " + " + (System.currentTimeMillis()-t2) + "ms");
				listError.add("Số CMND chưa có kết quả CIC_S37 – Vui lòng gửi yêu cầu tra cứu!");
				loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_400, "Check S37", Converter.buildErrorMsg(listError), appNumber, saleCode);
				return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError));
			} else if (!Converter.getCICStatus(cicResult)) {
				System.out.println("Check citizenId, cic result false: " + citizenId + " in " + (t1-start) + " + " + (t2-t1) + " + " + (System.currentTimeMillis()-t2) + "ms");
				listError.add("Từ chối do K.H đang có nợ xấu/nợ cần chú ý - Không hợp lệ để đẩy hồ sơ!");
				loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_400, "Check S37", Converter.buildErrorMsg(listError), appNumber, saleCode);
				return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError)); 
			}
		}
		
		/* check total debt: khong check o buoc check cmnd, chuyen buoc init-contract */
		/*
		 * Long t3 = System.currentTimeMillis(); if (!checkTotalDebt(citizenId,
		 * loanAmount, MAX_LOAN_LIMIT_DEFAULT)) {
		 * System.out.println("Check citizenId, total debt over limit: " + citizenId +
		 * " in " + (t1-start) + " + " + (t2-t1) + " + " + (t3-t2) + " + " +
		 * (System.currentTimeMillis()-t3) + "ms");
		 * listError.add("Tổng dư nợ của KH đang vượt quá 100 triệu");
		 * loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(),
		 * STATUS_CODE_400, "Check 100tr", Converter.buildErrorMsg(listError),
		 * appNumber); return new Result(STATUS_CODE_400,
		 * Converter.buildErrorMsg(listError)); }
		 */
		
		System.out.println("Check citizenId: " + citizenId + " in " + (t1-start) + " + " + (t2-t1) + " + " + (System.currentTimeMillis()-t2) + "ms");
		
		if (!listError.isEmpty()) {
			// TH nay hien tai se khong xay ra do da return het o tren roi
			loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_400, "", Converter.buildErrorMsg(listError), appNumber, saleCode);
			return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError));
		}
		
		loggerPrecheckTool.info("Ignored", citizenId, productGroup, new Date(), STATUS_CODE_OK, "", returnMsg, appNumber, saleCode);
		return new Result(STATUS_CODE_OK, returnMsg);
	}
	
	/**
	 * Kiem tra cmnd co trong blacklist khong
	 * @param citizenId
	 * @return
	 * @throws Exception
	 */
	public boolean checkBlacklist(String citizenId) throws Exception {
		Integer statusBlackList = this._uok.telesale.uplCustomerRepo().checkBlackListCitizenId(citizenId);
		if (null != statusBlackList && statusBlackList > 0)
			return true;
		
		return false;
	}
	
	/**
	 * Kiem tra cmnd co bi duplicate hop dong khong
	 * @param citizenId
	 * @param appNumber
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("serial")
	public List<ContractInfo> checkDuplicateCitizenId(String citizenId, String appNumber) throws Exception{
		List<ContractInfo> result = new ArrayList<>();
		
		if (StringUtils.isNullOrEmpty(appNumber))
			appNumber = "0";
		
		// goi den dwh de lay thong tin hop dong da ton tai khong thoa man dieu kien
		ApiResult apiResult = esbApi.getDuplicateContract(citizenId, appNumber);
		
		if (null == apiResult || !apiResult.getStatus()) {
			System.out.println("Check citizenId, check duplicate contract error, appNumber=" + appNumber + ", citizenId=" + citizenId + ", http code=" + (null == apiResult?"-1":apiResult.getCode()));
			throw new Exception("Check citizenId, check duplicate contract error, appNumber=" + appNumber + ", citizenId=" + citizenId);
		}
		
		try {
			result = JSONConverter.toObject(apiResult.getBodyContent(), new TypeToken<List<ContractInfo>>() {}.getType());
		} catch (Exception e) {} 		// TH khong co thong tin trung hop dong se tra ve '{}' nen parse se loi
		
		return result;
	}
	
	/**
	 * Kiem tra cmnd co ket qua cic s37 thoa man khong
	 * @param citizenId
	 * @return
	 * @throws Exception
	 */
	public String checkCIC(String citizenId) throws Exception {
		CheckCICAggregate cicAgg = new CheckCICAggregate(this._uok);
		List<CICDetailDTO> cicResult = cicAgg.searchCIC(citizenId, null);
		
		if (null != cicResult && cicResult.size() > 0)
			return cicResult.get(0).getCicResult();
		
		return null;
	}
	
	/**
	 * Kiem tra tong du no co qua 100M khong
	 * @param citizenId
	 * @param loanAmount
	 * @param loanLimit
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public boolean checkTotalDebt (String citizenId, Long loanAmount, Long loanLimit) throws Exception {
		CustomerManager custMng = new CustomerManager();
//		TotalDebt totalDebt = custMng.findTotalDebt(citizenId, "", "");
		TotalDebt totalDebt = custMng.findTotalDebtMC(citizenId, citizenId, "");
		
		if (null == totalDebt || totalDebt.getTotalDebt() < 0) {
			System.out.println("check citizenid -> check total debt null or invalid citizenId=" + citizenId);
			throw new Exception("Check citizenId, check total debt error");
		}
		
		if (null == loanAmount)
			loanAmount = 0L;
		
		if (totalDebt.getTotalDebt() + loanAmount > loanLimit) {
			System.out.println("check citizenId, total debt over: " + (totalDebt.getTotalDebt() + loanAmount));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Kiem tra thong tin khach hang truoc khi tao khoan vay (LSTD (PCB), DTI, PTI)
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public Object checkConditionInitContract(ConditionInitContract conditionInitContract) throws Exception {
		// luu y khong throw exception sau khi call pcb info, tranh bi rollback lai ket qua da goi sang PCB		
		Long start = System.currentTimeMillis();
		Set<String> listError = new HashSet<String>();
		List<RuleOutputList> returnMsg = new ArrayList<>();
		PCBAggregate pcbAggregate = new PCBAggregate(this._uok);
		double dti = 0.0;
		double pti = 0.0;
		PcbInfo pcbInfo = null;
		
		ProductDTO product = CacheManager.Product().findProductByCode(conditionInitContract.getProductCode());
		if (null == product)
			throw new Exception("checkConditionInitContract -> get product code null. citizenId=" + conditionInitContract.getCitizenId() + ", productCode=" + conditionInitContract.getProductCode());
		
		CodeTableDTO ctProdCate = CacheManager.CodeTable().getbyID(product.getProductCategoryId().intValue());
		
		String cicResult = checkCIC(conditionInitContract.getCitizenId());
		boolean checkCicLoanAmt = false;
		if (StringUtils.isNullOrEmpty(cicResult)) {
			listError.add("Số CMND chưa có kết quả CIC_S37 – Vui lòng gửi yêu cầu tra cứu!");
			loggerPrecheckTool.info("Ignored", conditionInitContract.getCitizenId(), conditionInitContract.getProductGroup(), new Date(), STATUS_CODE_400, "Check S37", Converter.buildErrorMsg(listError), conditionInitContract.getAppNumber(), conditionInitContract.getSaleCode());
			return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError)); 
		} else {
			checkCicLoanAmt = Converter.checkCicWithLoanAmt(cicResult, conditionInitContract.getLoanAmount());
		}
		
		Long t1 = System.currentTimeMillis();
		if(checkCicLoanAmt) {
			/* Lay thong tin tin dung PCB */
			List<RIReqInput> lstPayload = new ArrayList<RIReqInput>();
			RIReqInput payload = Converter.getPCBReqInput(conditionInitContract) ;
			lstPayload.add(payload);
			// Khong truyen vao appNumber. TH pre-check khong luu lai lich su lien quan den khoan vay. Tra ra id cua credit_bureau_data (TH truyen appNumber se tra ra id cua messsage_log)
			List<IdCheckPcbDTO> lstIdCheckPcb = pcbAggregate.checkPCB(lstPayload.toArray(), null, conditionInitContract.getCitizenId(), "", DateUtil.toDate(conditionInitContract.getDateOfBirth(), DateTimeFormat.dd_MM_yyyy), null, "CHECK",Integer.valueOf(cicResult),(double)conditionInitContract.getLoanAmount(),"P");
			if(lstIdCheckPcb == null || lstIdCheckPcb.size() == 0) {
				System.out.println("checkConditionInitContract -> get PCB info null. citizenId=" + conditionInitContract.getCitizenId());
				return new Result(STATUS_CODE_400, "PCB info is null");
			}
			IdCheckPcbDTO idCheckPcb = lstIdCheckPcb.get(0);
			if (idCheckPcb == null) {
				System.out.println("checkConditionInitContract -> get PCB info null. citizenId=" + conditionInitContract.getCitizenId());
				return new Result(STATUS_CODE_400, "PCB info is null");
			}
			
			if (StringUtils.isNullOrEmpty(idCheckPcb.id)) {
				System.out.println("checkConditionInitContract -> get PCB info id null. citizenId=" + conditionInitContract.getCitizenId());
				return new Result(STATUS_CODE_400, idCheckPcb.returnMes);
			}				
			
			System.out.println("ConditionInitContract.pcbinfo of citizenId=" + conditionInitContract.getCitizenId() + ",pcbId=" + idCheckPcb.id + " in " + (System.currentTimeMillis() - t1) + "ms");
			pcbInfo = pcbAggregate.getPcbRawInfo(idCheckPcb.id);
			if (null == pcbInfo) {
				System.out.println("checkConditionInitContract -> get PCB info not found. citizenId=" + conditionInitContract.getCitizenId());
				return new Result(STATUS_CODE_400, "PCB info not found"); 
			}		
			
			/* kiem tra thong tin lich su tin dung (PCB) */
			Long t2 = System.currentTimeMillis();
			if (!checkCreditHistory(pcbInfo, idCheckPcb.id)) {
				System.out.println("checkConditionInitContract.lstd fail: " + conditionInitContract.getCitizenId() + " in pcb=" + (t2 - t1) + " + lstd=" + (System.currentTimeMillis() - t2) + "ms");
				listError.add("KH không thỏa điều kiện lịch sử tín dụng");
				loggerPrecheckTool.info("Ignored", conditionInitContract.getCitizenId(), conditionInitContract.getProductGroup(), new Date(), STATUS_CODE_400, "LSTD", Converter.buildErrorMsg(listError), conditionInitContract.getAppNumber(), conditionInitContract.getSaleCode());
				return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError)); 
			}
		}
		
		//SP: Credit_card: khong check dti, pti, du no
		if(ctProdCate.getCodeValue1().equalsIgnoreCase(ProductCategoryEnum.CREDIT_CARD.value())) {
			return new Result(STATUS_CODE_OK, JSONConverter.toJSON(new ArrayList<String>()));
		} else {
			/* kiem tra dieu kien DTI, PTI */
			Long t2 = System.currentTimeMillis();
			try {
				dti = calculateEMI(product, conditionInitContract)/conditionInitContract.getCustomerIncome();
				pti = calculatePTI(product, conditionInitContract);
				if(pcbInfo != null) {
					dti = calculateDTI(product, conditionInitContract, pcbInfo);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Calculate dti, pti error: " + ex.getMessage());
				return new Result(STATUS_CODE_400, "Calculate dti, pti error");
			}			
			System.out.println("===== TEST:" + conditionInitContract.getCitizenId() + ", product=" + product.getProductCode() + ", dti=" + dti + ", pti=" + pti);
			
			// goi rule check dti pti de kiem tra thong tin
			RuleObject ruleObject = Converter.getRuleObjectCheckDtiPti(dti, pti, product.getProductCode());
			ApiResult apiResult = esbApi.checkRule(ruleObject);
			RuleResult ruleResult = null;
			System.out.println("checkConditionInitContract, rule dti/pti citizenId=" + conditionInitContract.getCitizenId() + " in pcb=" + (t2 - t1) + ",rule= " + (System.currentTimeMillis() - t2) + "ms");
			
			if (null == apiResult || !apiResult.getStatus()) {
				System.out.println("checkConditionInitContract -> get rule CHECK_DTI_PTI error, citizenId=" + conditionInitContract.getCitizenId() + ", product=" + product.getProductCode() + ", http code=" + (null == apiResult?"-1":apiResult.getCode()));
				return new Result(STATUS_CODE_400, "Get rule CHECK_DTI_PTI error"); 
			}
			
			ruleResult = JSONConverter.toObject(apiResult.getBodyContent(), RuleResult.class);
			if (null == ruleResult) {
				System.out.println("checkConditionInitContract -> get rule CHECK_DTI_PTI content error, citizenId=" + conditionInitContract.getCitizenId() + ", product=" + product.getProductCode() + ", content=" + apiResult.getBodyContent());
				return new Result(STATUS_CODE_400, "Get rule CHECK_DTI_PTI content error");
			}
			
			if (STATUS_CODE_OK.equals(ruleResult.getReturnCode()) && ruleResult.getListValue() != null)
				returnMsg = ruleResult.getListValue();
			
			// Check tong du no
			Long t3 = System.currentTimeMillis();
			if (!checkTotalDebt(conditionInitContract.getCitizenId(), conditionInitContract.getLoanAmount(), MAX_LOAN_LIMIT_DEFAULT)) {
				System.out.println("checkConditionInitContract, total debt over limit: " + conditionInitContract.getCitizenId() + " in cic=" + (t1-start) + ",pcb=" + (t2-t1) + ", rule=" + (t3-t2) + "debt=" + (System.currentTimeMillis()-t3) + "ms");
				listError.add("Tổng dư nợ của KH đang vượt quá 100 triệu");
				loggerPrecheckTool.info("Ignored", conditionInitContract.getCitizenId(), conditionInitContract.getProductGroup(), new Date(), STATUS_CODE_400, "Check 100tr", Converter.buildErrorMsg(listError), conditionInitContract.getAppNumber(), conditionInitContract.getSaleCode());
				return new Result(STATUS_CODE_400, Converter.buildErrorMsg(listError)); 
			}
			System.out.println("Total.checkConditionInitContract: " + conditionInitContract.getCitizenId() + " in cic=" + (t1-start) + ",pcb=" + (t2-t1) + ",rule=" + (t3-t2) + ",debt=" + (System.currentTimeMillis()-t3) + "ms");		
			loggerPrecheckTool.info("Ignored", conditionInitContract.getCitizenId(), conditionInitContract.getProductGroup(), new Date(), STATUS_CODE_OK, "", JSONConverter.toJSON(returnMsg), conditionInitContract.getAppNumber(), conditionInitContract.getSaleCode());
			return new Result(STATUS_CODE_OK, JSONConverter.toJSON(returnMsg));
		}
	}
	
	/**
	 * Kiem tra LSTD (PCB) co thuoc nhom no nao khong
	 * @param pcbInfo
	 * @param pcbId
	 * @return
	 */
	public boolean checkCreditHistory(PcbInfo pcbInfo, String pcbId) throws Exception {
		// nhom no trong 12/36 thang: Instalments/NonInstalments/Cards -> GrantedContract -> Profiles -> Status
		
		// kiem tra trong 12 thang co qua nhom no 2 khong
		String h12 = pcbInfo.getHighest12MonthLoan();
		System.out.println("=====TEST:checkCreditHistory.pcbId=" + pcbId + ", nhom no max 12 thang=" + h12);
		if (!StringUtils.isNullOrEmpty(h12) && Integer.valueOf(h12) >= 2) {
			System.out.println("checkCreditHistory.pcbId=" + pcbId + " over group debt 2 in 12 months: " + h12);
			return false;
		}
		
		// kiem tra trong 36 thang co qua nhom no 3 khong
		String h36 = pcbInfo.getHighest3YearLoan();
		System.out.println("=====TEST:checkCreditHistory.pcbId=" + pcbId + ", nhom no max 36 thang=" + h36);
		if (!StringUtils.isNullOrEmpty(h36) && Integer.valueOf(h36) >= 3) {
			System.out.println("checkCreditHistory.pcbId=" +pcbId+"over group debt 3 in 36 months: " + h36);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Tinh DTI (PCB)
	 * @param product
	 * @param conditionInitContract
	 * @param pcbInfo
	 * @return
	 * @throws Exception
	 */
	public double calculateDTI(ProductDTO product, ConditionInitContract conditionInitContract, PcbInfo pcbInfo) throws Exception {
		Double EMIAtOtherOrganize, EMIAtMC;
		
		// tinh EMI khoan vay de xuat tai Mcredit
		EMIAtMC = this.calculateEMI(product, conditionInitContract);
		
		// tinh EMI tai to chuc tin dung khac (lay bao cao tren PCB)
		EMIAtOtherOrganize = Double.valueOf(pcbInfo.getEMIInstalments() + pcbInfo.getEMINonInstalments() + pcbInfo.getEMICard());
//		System.out.println("===== TEST: product: " + product.getProductCode() + ", emiOther=" + EMIAtOtherOrganize + ", emiMC=" + EMIAtMC);
		
		return (EMIAtMC + EMIAtOtherOrganize)/conditionInitContract.getCustomerIncome();
	}
	
	/**
	 * Tinh PTI (PCB)
	 * @param product
	 * @param conditionInitContract
	 * @param pcbInfo
	 * @return
	 * @throws Exception
	 */
	public double calculatePTI(ProductDTO product, ConditionInitContract conditionInitContract) throws Exception {
		Double EMIContractApprovedAtMC, EMIAtMC;
		
		// tinh EMI khoan vay de xuat tai Mcredit
		EMIAtMC = this.calculateEMI(product, conditionInitContract);
		
		// tinh EMI cac khoan vay da phe duyet giai ngan tai Mcredit
		// goi den dwh de lay thong tin EMI (so tien phai tra hang thang cua tat ca cac khoan vay co trang thai Active/Backdate tren sao ke Lich tra no)
		ApiResult apiResult = esbApi.getEMIContractApproved(conditionInitContract.getCitizenId());
		
		if (null == apiResult || !apiResult.getStatus()) 
			throw new Exception("calculatePTI -> getEMIContractApproved error, citizenId=" + conditionInitContract.getCitizenId() + ", http code=" + (null == apiResult?"-1":apiResult.getCode()));
		
		EMIContractApproved emi = JSONConverter.toObject(apiResult.getBodyContent(), EMIContractApproved.class);
		if (null == emi)
			throw new Exception("calculatePTI -> getEMIContractApproved content error, citizenId=" + conditionInitContract.getCitizenId() + ", content=" + apiResult.getBodyContent());
		
		if (emi.getTotalAMT() == null)	
			EMIContractApprovedAtMC = 0d;
		else 
			EMIContractApprovedAtMC = Double.valueOf(emi.getTotalAMT());
		
//		System.out.println("===== TEST: product: " + product.getProductCode() + ", emiApprovedMC=" + EMIContractApprovedAtMC + ", emiMC=" + EMIAtMC);
		return (EMIAtMC + EMIContractApprovedAtMC)/conditionInitContract.getCustomerIncome();
	}
	
	/**
	 * Tinh EMI khoan vay de xuat tai Mcredit
	 * @param product
	 * @param conditionInitContract
	 * @return
	 * @throws Exception
	 */
	private double calculateEMI(ProductDTO product, ConditionInitContract conditionInitContract) throws Exception {
		if (null == product.getRateIndex())
			throw new Exception("calculateDTI/PIT rate index null. product id=" + product.getId());
		Double interestYearly = this._uok.common.productTaskRepo().getInterestYearlyById(product.getRateIndex().longValue());
		if (null == interestYearly)
			throw new Exception("calculateDTI/PIT interest yearly null. product id=" + product.getId());
		
		return -FinanceLib.pmt(interestYearly/1200, 
				conditionInitContract.getLoanTenor(), 
				conditionInitContract.getLoanAmount() + ((conditionInitContract.getHasInsurance() != null && conditionInitContract.getHasInsurance()==1)?(conditionInitContract.getLoanAmount()*Constants.INSURANCE_RATE/100):0), 
				0, false);
	}
}
