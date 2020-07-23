package com.mcredit.business.appraisal.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.mcredit.business.appraisal.callout.EsbApi;
import com.mcredit.business.appraisal.convert.Converter;
import com.mcredit.business.appraisal.object.AppraisalResult;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.appraisal.entity.CreditAppraisalData;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.appraisal.AppraisalDataDetailDTO;
import com.mcredit.model.dto.appraisal.AppraisalObjectDTO;
import com.mcredit.model.dto.appraisal.GoodsInformation;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RuleCode;
import com.mcredit.model.enums.appraisal.AppraisalRule;
import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleOutputList;
import com.mcredit.model.object.RuleResult;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.cache.TokenCacheManager;
import com.mcredit.sharedbiz.validation.InternalException;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class AppraisalAggregate {

	private static ParametersCacheManager parameter = CacheManager.Parameters();
	private static ProductCacheManager productCache = ProductCacheManager.getInstance();
	private static TokenCacheManager tokenCache = CacheManager.Token();
	public static final String INSTALLMENT_LOAN = "InstallmentLoan";
	public static final String CASH_LOAN = "CashLoan";
	public static final String CONCENTRATING_DATA_ENTRY = "ConcentratingDataEntry";
	public static final String STATUS_CODE_OK = "200";
	public static final String STATUS_CODE_400 = "400";
	public static final String STATUS_CODE_401 = "401";
	public static final String STATUS_CODE_500 = "500";
	public static String ACTION_CALL = "C";
	public static String ACTION_APPROVE = "A";
	public static String ACTION_BPM = "B";
	private static final String PRODUCT_EXP_VALUE_PREFIX = "[goodsPrice] *";		// prepay rate
	private static final String COMMA = ",";
	private static List<String> PRD_GRP_INCOME_INTERPOLATE;
	
	private UnitOfWork _uok = null;
	private EsbApi esbApi = null;

	public AppraisalAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		esbApi = new EsbApi();
		
		PRD_GRP_INCOME_INTERPOLATE = new ArrayList<>();
		String income_interpolate = parameter.findParamValueAsString(ParametersName.PRD_GRP_INCOME_INTERPOLATE);
		
		if (!StringUtils.isNullOrEmpty(income_interpolate))
			PRD_GRP_INCOME_INTERPOLATE.addAll(Arrays.asList(income_interpolate.split(COMMA)));
	}

	/**
	 * Process update appraisal data from appraisal tool
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by appraisal tool (portal)
	 * @return result with code 200 if success
	 * @throws Exception
	 */
	public Object saveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		CreditAppraisalData creditAppraisalData = Converter.getCreditAppraisalData(appraisalObj);
		
		_uok.appraisal.creditAppraisalDataRepository().add(creditAppraisalData);
		if (creditAppraisalData != null && creditAppraisalData.getId() != null)
			return new Result(STATUS_CODE_OK, "");

		return new Result(STATUS_CODE_500, "Save fail");
	}
	
	/**
	 * Process update appraisal data from bpm
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @return result with code 200 if success
	 * @throws Exception
	 */
	public Object losSaveAppraisal(AppraisalObjectDTO appraisalObj) throws Exception {
		// check product exists
		ProductDTO product = null;
		List<ProductDTO> listProduct = productCache.findListProductByCode(appraisalObj.getAppraisalDataDetail().getProductCode());
		if (listProduct == null)
			throw new Exception(Messages.getString("appraisal.product.notFound", appraisalObj.getAppraisalDataDetail().getProductCode()));
		
		Date createdDate = DateUtil.toDate(appraisalObj.getAppraisalDataDetail().getCreatedDate(), DateTimeFormat.yyyyMMddHHmmss);
		for (ProductDTO pro : listProduct) {
			if (pro == null || pro.getStartEffDate() == null || pro.getStartEffDate().after(createdDate))
				continue;
			
			if (product == null || pro.getStartEffDate().after(product.getStartEffDate()))
				product = pro;
		}
		
		if (product == null)
			throw new Exception(Messages.getString("appraisal.product.notFound", appraisalObj.getAppraisalDataDetail().getProductCode()));
		
		CreditAppraisalData appraisalData = _uok.appraisal.creditAppraisalDataRepository().getAppraisalInfoByAction(appraisalObj.getBpmAppId(), new ArrayList<String>(){{add(ACTION_BPM + appraisalObj.getAction());}});	// BA, BC
		appraisalData = Converter.losGetCreditAppraisalData(appraisalObj, appraisalData);

		_uok.appraisal.creditAppraisalDataRepository().upsert(appraisalData);
		if (appraisalData != null && appraisalData.getId() != null)
			return new Result(STATUS_CODE_OK, "");

		return new Result(STATUS_CODE_500, "Save fail");
	}

	/**
	 * Process request get appraisal result
	 * @author catld.ho
	 * @param appraisalObj : appraisal object sent by bpm
	 * @return result with code 200 if success 
	 * @throws Exception
	 */
	public Object getAppraisalResult(AppraisalObjectDTO appraisalObj) throws Exception {
		CreditAppraisalData appraisalData = _uok.appraisal.creditAppraisalDataRepository().getAppraisalResult(appraisalObj.getTransactionId());
		if (appraisalData == null)
			throw new InternalException(STATUS_CODE_401, "Appraisal result not found. transactionId: " + appraisalObj.getTransactionId());
		
		if (StringUtils.isNullOrEmpty(appraisalData.getConclude()))
			throw new InternalException(STATUS_CODE_401, "Appraisal result not save");
		else if (!"1".equals(appraisalData.getConclude()))
			throw new InternalException(STATUS_CODE_400, "Appraisal result is false");
		
		return new Result(STATUS_CODE_OK, "");
	}

	/**
	 * Process request get appraisal data
	 * @author catld.ho
	 * @param bpmAppId : appId correspond customer profile
	 * @param action : actor request data
	 * @param ottToken : ott token sent by bpm
	 * @return appraisal object in dto model
	 * @throws Exception
	 */
	public AppraisalObjectDTO getAppraisalData(String bpmAppId, String action, String ottToken) throws Exception {
		AppraisalObjectDTO appraisalObjectDTO = null;
		
		CreditAppraisalData appraisalData = _uok.appraisal.creditAppraisalDataRepository().getAppraisalInfoByAction(bpmAppId, new ArrayList<String>(){{add(ACTION_APPROVE); add(ACTION_CALL);}});
		CreditAppraisalData appraisalDataFromLOS = _uok.appraisal.creditAppraisalDataRepository().getAppraisalInfoByAction(bpmAppId, new ArrayList<String>(){{add(ACTION_BPM + action);}});
		if (appraisalDataFromLOS == null || StringUtils.isNullOrEmpty(appraisalDataFromLOS.getAppraisalDataDetail()))
			throw new Exception(Messages.getString("appraisal.dataDetail.notFound", bpmAppId + ":" + action));
		
		appraisalObjectDTO = Converter.getAppraisalData(appraisalData, appraisalDataFromLOS);
		
//		ProductDTO product = productCache.findProductById(appraisalObjectDTO.getAppraisalDataDetail().getProductId());
//		if (product == null)
//			throw new Exception(Messages.getString("appraisal.product.notFound", appraisalObjectDTO.getAppraisalDataDetail().getProductId()));
//
//		if (StringUtils.isNullOrEmpty(product.getProductCode()))
//			throw new Exception(Messages.getString("appraisal.productCode.notFound", appraisalObjectDTO.getAppraisalDataDetail().getProductId()));
//
//		appraisalObjectDTO.getAppraisalDataDetail().setProductCode(product.getProductCode());
		
		String token = tokenCache.getBearerToken(ottToken);
		if (StringUtils.isNullOrEmpty(token))
			throw new Exception(Messages.getString("appraisal.esbService.getToken.error3"));

		appraisalObjectDTO.setToken(token);
		
		return appraisalObjectDTO;
	}

	/**
	 * Validate customer info
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @return list result of conditions
	 * @throws Exception
	 */
	public Object validCustomer(AppraisalDataDetailDTO dataDetail) throws Exception {
		List<AppraisalResult> listResult = new ArrayList<>();
		Map<String, RuleObject> ruleList = null; //new HashMap<AppraisalRule, RuleObject>();
		ApiResult apiResult = null;
		
		/* get product */
		ProductDTO product = null;
		List<ProductDTO> listProduct = productCache.findListProductByCode(dataDetail.getProductCode());
		if (listProduct == null)
			throw new Exception(Messages.getString("appraisal.product.notFound", dataDetail.getProductCode()));
		
		Date createdDate = DateUtil.toDate(dataDetail.getCreatedDate(), DateTimeFormat.yyyyMMdd);
		for (ProductDTO pro : listProduct) {
			if (pro == null || pro.getStartEffDate() == null || pro.getStartEffDate().after(createdDate))
				continue;
			
			if (product == null || pro.getStartEffDate().after(product.getStartEffDate()))
				product = pro;
		}
		
//		System.out.println("Product: " + JSONConverter.toJSON(product));
		if (product == null)
			throw new Exception(Messages.getString("appraisal.product.notFound", dataDetail.getProductCode()));
		
		/** BUILD RULE OBJECT **/
		ruleList = buildRuleList(dataDetail);
		
		/** CHECK RULES **/
		apiResult = esbApi.checkRules(ruleList);
		
		/** PROCESS CHECK RULES RESULT **/
		listResult = processRuleResult(dataDetail, product, apiResult, listResult);
		
		return JSONConverter.toJSON(listResult);
	}	
	
	/**
	 * Build list check rule
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @return list rule object
	 * @throws Exception
	 */
	private Map<String, RuleObject> buildRuleList(AppraisalDataDetailDTO dataDetail) throws Exception {
		Map<String, RuleObject> ruleList = new HashMap<String, RuleObject>();
		RuleObject ruleObjAge = null, ruleObjIncome = null, ruleObjPrepayRate = null, ruleObjAppraisal = null, ruleObjGoods = null;
		
		/* CHECK AGE (rule AGE_VALIDATION) */
		ruleObjAge = Converter.createRuleObject(RuleCode.AGE_VALIDATION.value(), dataDetail.getProductCode(), dataDetail.getProductGroup(), dataDetail.getCreatedDate(),
				dataDetail.getBirthDate(), dataDetail.getLoanTenor(), dataDetail.getSalaryPaymentType(), dataDetail.getCustCompanyCat(), null, null, null, null, 
				null, null, null, null, null, null, null, null, null);
		ruleList.put(AppraisalRule.AGE.value(), ruleObjAge);
		
		/* CHECK INCOME (rule INCOME_VALIDATION) */
		if (PRD_GRP_INCOME_INTERPOLATE.contains(dataDetail.getProductCode())) {
			// sp CS SY A 37 (C0000027), CS SY B 47 (C0000028), CS Bank Acct VIP 37 (C0000001) su dung thu nhap noi suy
			ruleObjIncome = Converter.createRuleObject(RuleCode.INCOME_VALIDATION.value(), dataDetail.getProductCode(), null, dataDetail.getCreatedDate(),
					null, null , dataDetail.getSalaryPaymentType(), dataDetail.getCustCompanyCat(), dataDetail.getCustomerIncomeInterpolate(), null, null, null,
					null, null, null, null, null, null, null, null, null);
			ruleList.put(AppraisalRule.INCOME_1.value(), ruleObjIncome);
		} else {
			// else case su dung thu nhap tham dinh
			ruleObjIncome = Converter.createRuleObject(RuleCode.INCOME_VALIDATION.value(), dataDetail.getProductCode(), null, dataDetail.getCreatedDate(),
					null, null , dataDetail.getSalaryPaymentType(), dataDetail.getCustCompanyCat(), dataDetail.getCustomerIncomeAppraisal(), null, null, null, 
					null, null, null, null, null, null, null, null, null);
			ruleList.put(AppraisalRule.INCOME_2.value(), ruleObjIncome);
		}
		
		/* CHECK EXP VALUE (rule PREPAY_RATE) */
		if (INSTALLMENT_LOAN.equals(dataDetail.getProductGroup())) {
			// default typeOfGoods and brand index 1 (chi co toi da 1 hang hoa cho san pham can check rule PREPAY RATE lien quan den hang hoa)
			ruleObjPrepayRate = Converter.createRuleObject(RuleCode.PREPAY_RATE.value(), dataDetail.getProductCode(), null, dataDetail.getCreatedDate(),
					null, null , null, null, null, dataDetail.getGoodsPrice(), dataDetail.getGoodsInformation().get(0).getTypeOfGoods(), dataDetail.getGoodsInformation().get(0).getBrand(),
					null, null, null, null, null, null, null, null, null);
			ruleList.put(AppraisalRule.PREPAY_RATE.value(), ruleObjPrepayRate);
		}
		
		/* CHECK APPRAISAL (rule APPRAISAL_VALIDATION) */
		// request check new rule TCTD, CIC, PTI, DTI, LOAN AMOUNT
		ruleObjAppraisal = Converter.createRuleObject(RuleCode.APPRAISAL_VALIDATION.value(), dataDetail.getProductCode(), dataDetail.getProductGroup(), dataDetail.getCreatedDate(),
				null, null , null, null, dataDetail.getCustomerIncomeInterpolate(), null, null, null, dataDetail.getNumberOfRelationOrganize(), dataDetail.getNumberOfRelationFinanceCompany(), 
				dataDetail.getTotalOutstandingLoanCIC(), dataDetail.getPti(), dataDetail.getDti(), dataDetail.getLoanAmount(), dataDetail.getAverageElectricBill(), 
				dataDetail.getAverageAccountBalance(), dataDetail.getAnnualFeeLifeInsurance());
		ruleList.put(AppraisalRule.APPRAISAL.value(), ruleObjAppraisal);
		
		/* CHECK GOODS */
		// request check rule GOODS_VALIDATION
		if (INSTALLMENT_LOAN.equals(dataDetail.getProductGroup())) {
			GoodsInformation gi = null;
			for (int i = 0; i < dataDetail.getGoodsInformation().size(); i++) {
				gi = dataDetail.getGoodsInformation().get(i);
				ruleObjGoods = Converter.createRuleObject(RuleCode.GOODS_VALIDATION.value(), dataDetail.getProductCode(), null, dataDetail.getCreatedDate(),
						null, null , null, null, null, gi.getGoodsPrice(), gi.getTypeOfGoods(), gi.getBrand(), 
						null, null, null , null, null, null , null, null, null);
				ruleList.put(AppraisalRule.GOODS_.value() + (i+1), ruleObjGoods);
			}
		}

		return ruleList;
	}

	/**
	 * Process list rule result
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param apiResult : check rule response from Rule Service
	 * @param listResult : list result output
	 * @return list result of conditions
	 * @throws Exception
	 */
	private List<AppraisalResult> processRuleResult(AppraisalDataDetailDTO dataDetail, ProductDTO product, ApiResult apiResult, List<AppraisalResult> listResult) throws Exception {
		if (apiResult == null || !apiResult.getStatus())
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error1"));
		
		Map<String, RuleResult> ruleResults = JSONConverter.toObject(apiResult.getBodyContent(), new TypeToken<Map<String, RuleResult>>() {}.getType());
		if (ruleResults == null) 
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error2"));
		
		System.out.println(JSONConverter.toJSON(ruleResults));
		/* CHECK LOAN AMOUNT SUGGEST */
		processRuleCheckLoanAmountSuggest(dataDetail, product, ruleResults, listResult);
		
		/* CHECK LOAN TENOR */
		processRuleCheckLoanTenor(dataDetail, product, ruleResults, listResult);
		
		/* CHECK AGE (rule AGE_VALIDATION) */
		processRuleCheckAge(dataDetail, product, ruleResults, listResult);
		
		/* CHECK INCOME (rule INCOME_VALIDATION) */
		processRuleCheckIncome(dataDetail, product, ruleResults, listResult);
		
		/* CHECK EXP VALUE (PREPAY_RATE)*/
		processRuleCheckPrepayRate(dataDetail, product, ruleResults, listResult);
		
		/* CHECK APPRAISAL (rule APPRAISAL_VALIDATION) */
		processRuleCheckAppraisal(dataDetail, product, ruleResults, listResult);
		
		/* CHECK GOODS */
		processRuleCheckGoods(dataDetail, product, ruleResults, listResult);
		
		return listResult;
	}

	/**
	 * Process rule check loan amount
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 */
	private void processRuleCheckLoanAmountSuggest(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) {
		// check by products info
		if (product.getMinLoanAmount() != null && dataDetail.getLoanAmountAfterInsurance() < product.getMinLoanAmount().longValue())
			listResult.add(new AppraisalResult(AppraisalRule.LOAN_AMOUNT_AFTER_INSURANCE.value(), false, Messages.getString("appraisal.validate.loanAmountSuggest.min.error", product.getMinLoanAmount(), product.getMaxLoanAmount())));
		else if (product.getMaxLoanAmount() != null && dataDetail.getLoanAmountAfterInsurance() > product.getMaxLoanAmount().longValue())
			listResult.add(new AppraisalResult(AppraisalRule.LOAN_AMOUNT_AFTER_INSURANCE.value(), false, Messages.getString("appraisal.validate.loanAmountSuggest.max.error", product.getMinLoanAmount(), product.getMaxLoanAmount())));
		else
			listResult.add( new AppraisalResult(AppraisalRule.LOAN_AMOUNT_AFTER_INSURANCE.value(), true, "PASS"));
	}
	
	/**
	 * Process rule check loan tenor
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 */
	private void processRuleCheckLoanTenor(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) {
		// check by products info
		if (product.getMinTenor() != null && dataDetail.getLoanTenor() < product.getMinTenor().longValue())
			listResult.add(new AppraisalResult(AppraisalRule.LOAN_TENOR.value(), false, Messages.getString("appraisal.validate.loanTenor.min.error", product.getMinTenor(), product.getMaxTenor())));
		else if (product.getMaxTenor() != null && dataDetail.getLoanTenor() > product.getMaxTenor().longValue())
			listResult.add(new AppraisalResult(AppraisalRule.LOAN_TENOR.value(), false, Messages.getString("appraisal.validate.loanTenor.max.error", product.getMinTenor(), product.getMaxTenor())));
		else
			listResult.add(new AppraisalResult(AppraisalRule.LOAN_TENOR.value(), true, "PASS"));
	}
	
	/**
	 * Process rule check age
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 * @throws Exception
	 */
	private void processRuleCheckAge(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) throws Exception {
		AppraisalResult appraisalResult = new AppraisalResult(AppraisalRule.AGE.value(), true, "PASS");
		RuleResult ruleResult = ruleResults.get(AppraisalRule.AGE.value());
		if (ruleResult == null)
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "AGE_VALIDATION"));
		
		if (!STATUS_CODE_OK.equals(ruleResult.getReturnCode()) || ruleResult.getListValue() == null || ruleResult.getListValue().size() <= 0) {
			listResult.add(appraisalResult);
			return;
		}
		
		for (RuleOutputList rol : ruleResult.getListValue()) {
			if ("OA".equals(rol.getOutputType()))
				appraisalResult = new AppraisalResult(AppraisalRule.AGE.value(), false, Messages.getString("appraisal.validate.age.max.error"));
			else if ("UA".equals(rol.getOutputType()))
				appraisalResult = new AppraisalResult(AppraisalRule.AGE.value(), false, Messages.getString("appraisal.validate.age.min.error"));
			else
				appraisalResult = new AppraisalResult(AppraisalRule.AGE.value(), false, ruleResult.getListValue().isEmpty()?"":ruleResult.getListValue().get(0).getOutputValue());
		}
		
		listResult.add(appraisalResult);
	}

	/**
	 * Process rule check income customer
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 * @throws Exception
	 */
	private void processRuleCheckIncome(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) throws Exception {
		RuleResult ruleResult = null;
		if (PRD_GRP_INCOME_INTERPOLATE.contains(dataDetail.getProductCode())) {
			// sp CS SY A 37 (C0000027), CS SY B 47 (C0000028), CS Bank Acct VIP 37 (C0000001) su dung thu nhap noi suy
			ruleResult = ruleResults.get(AppraisalRule.INCOME_1.value());
			if (ruleResult == null) 
				throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "INCOME_1"));
				
			if (STATUS_CODE_OK.equals(ruleResult.getReturnCode()))
				listResult.add(new AppraisalResult(AppraisalRule.INCOME_1.value(), false, Messages.getString("appraisal.validate.income.error")));		// ruleResult.getScalarValue()
			else
				listResult.add(new AppraisalResult(AppraisalRule.INCOME_1.value(), true, "PASS"));
			
			listResult.add(new AppraisalResult(AppraisalRule.INCOME_2.value(), true, "PASS"));
		} else {
			// else case su dung thu nhap tham dinh
			ruleResult = ruleResults.get(AppraisalRule.INCOME_2.value());
			if (ruleResult == null) 
				throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "INCOME_2"));
				
			if (STATUS_CODE_OK.equals(ruleResult.getReturnCode()))
				listResult.add(new AppraisalResult(AppraisalRule.INCOME_2.value(), false, Messages.getString("appraisal.validate.income.error")));
			else
				listResult.add(new AppraisalResult(AppraisalRule.INCOME_2.value(), true, "PASS"));
			
			listResult.add(new AppraisalResult(AppraisalRule.INCOME_1.value(), true, "PASS"));
		}
	}

	/**
	 * Process rule check prepay rate
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 * @throws Exception
	 */
	private void processRuleCheckPrepayRate(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) throws Exception {
		AppraisalResult appraisalResult = new AppraisalResult(AppraisalRule.PREPAY_RATE.value(), true, "PASS");
		RuleResult ruleResult = null;
		// request check rule PREPAY_RATE first. If no response then check by products info
		if (!INSTALLMENT_LOAN.equals(dataDetail.getProductGroup())) {
			listResult.add(appraisalResult);
			return;
		}
		
		// check rule PREPAY_RATE
		ruleResult = ruleResults.get(AppraisalRule.PREPAY_RATE.value());
		if (ruleResult == null) 
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "PREPAY_RATE"));
		
		if (STATUS_CODE_OK.equals(ruleResult.getReturnCode())) {
			// get prepay rate range success
			if (ruleResult.getListValue() != null && ruleResult.getListValue().size() > 0) {
				for (RuleOutputList rol : ruleResult.getListValue()) {
					if ("Min".equals(rol.getOutputType()) && dataDetail.getDownPaymentRate()/100 < Double.valueOf(rol.getOutputValue()))
						appraisalResult = new AppraisalResult(AppraisalRule.PREPAY_RATE.value(), false, Messages.getString("appraisal.validate.prepayRate.min.error"));
					else if ("Max".equals(rol.getOutputType()) && dataDetail.getDownPaymentRate()/100 > Double.valueOf(rol.getOutputValue()))
						appraisalResult = new AppraisalResult(AppraisalRule.PREPAY_RATE.value(), false, Messages.getString("appraisal.validate.prepayRate.max.error"));
				}
			}
		} else {
			// check by products info
			if (!StringUtils.isNullOrEmpty(product.getExpMinValue()) && product.getExpMinValue().startsWith(PRODUCT_EXP_VALUE_PREFIX)) {
				Double expMinValue = Double.valueOf(product.getExpMinValue().replace(PRODUCT_EXP_VALUE_PREFIX, "").trim());
				if (dataDetail.getDownPaymentRate()/100 < expMinValue)
					appraisalResult = new AppraisalResult(AppraisalRule.PREPAY_RATE.value(), false, Messages.getString("appraisal.validate.prepayRate.min.error"));
			} 
			if (!StringUtils.isNullOrEmpty(product.getExpMaxValue()) && product.getExpMaxValue().startsWith(PRODUCT_EXP_VALUE_PREFIX)) {
				Double expMaxValue = Double.valueOf(product.getExpMaxValue().replace(PRODUCT_EXP_VALUE_PREFIX, "").trim());
				if (dataDetail.getDownPaymentRate()/100 > expMaxValue)
					appraisalResult = new AppraisalResult(AppraisalRule.PREPAY_RATE.value(), false, Messages.getString("appraisal.validate.prepayRate.max.error"));
			}
		}
		
		listResult.add(appraisalResult);
	}

	/**
	 * Process rule check appraisal (new rule)
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 * @throws Exception
	 */
	private void processRuleCheckAppraisal(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) throws Exception {
		AppraisalResult appraisalResult = new AppraisalResult(AppraisalRule.NO_OF_RELATION_ORGANIZE.value(), true, "PASS"),		// TCTD
						appraisalResult1 = new AppraisalResult(AppraisalRule.NO_OF_RELATION_COMPANY.value(), true, "PASS"),		// CTTC
						appraisalResult2 = new AppraisalResult(AppraisalRule.OUTSTANDING_CIC.value(), true, "PASS"), 			// CIC
						appraisalResult3 = new AppraisalResult(AppraisalRule.PTI.value(), true, "PASS"), 						// PTI
						appraisalResult4 = new AppraisalResult(AppraisalRule.DTI.value(), true, "PASS"), 						// DTI
						appraisalResult5 = new AppraisalResult(AppraisalRule.LOAN_AMOUNT.value(), true, "PASS");				// LOAN AMOUNT
		RuleResult ruleResult = ruleResults.get(AppraisalRule.APPRAISAL.value());
		if (ruleResult == null) 
			throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "APPRAISAL"));
			
		if (!STATUS_CODE_OK.equals(ruleResult.getReturnCode()) || ruleResult.getListValue() == null || ruleResult.getListValue().size() <= 0) {
			listResult.add(appraisalResult);
			listResult.add(appraisalResult1);
			listResult.add(appraisalResult2);
			listResult.add(appraisalResult3);
			listResult.add(appraisalResult4);
			listResult.add(appraisalResult5);
			return;
		}
		
		for (RuleOutputList rol : ruleResult.getListValue()) {
			switch(rol.getOutputType()) {
				case "TCTD":
					appraisalResult = new AppraisalResult(AppraisalRule.NO_OF_RELATION_ORGANIZE.value(), false, Messages.getString("appraisal.validate.appraisal.financeOrganize.error"));
					break;
				case "CTTC":
					appraisalResult1 = new AppraisalResult(AppraisalRule.NO_OF_RELATION_COMPANY.value(), false, Messages.getString("appraisal.validate.appraisal.financeCompany.error"));
					break;
				case "CIC":
					appraisalResult2 = new AppraisalResult(AppraisalRule.OUTSTANDING_CIC.value(), false, Messages.getString("appraisal.validate.appraisal.cic.error"));
					break;
				case "PTI30":
				case "PTI35":
					appraisalResult3 = new AppraisalResult(AppraisalRule.PTI.value(), false, Messages.getString("appraisal.validate.appraisal.pti.error"));
					break;
				case "DTI45":
				case "DTI40":
					appraisalResult4 = new AppraisalResult(AppraisalRule.DTI.value(), false, Messages.getString("appraisal.validate.appraisal.dti.error"));
					break;
				case "A":
				case "B":
				case "C":
				case "D":
				case "E":
				case "F":
				case "G":
				case "H":
					appraisalResult5 = new AppraisalResult(AppraisalRule.LOAN_AMOUNT.value(), false, Messages.getString("appraisal.validate.appraisal.loanAmount.error"));
					break;
					
				default : break;
			}
		}

		listResult.add(appraisalResult);
		listResult.add(appraisalResult1);
		listResult.add(appraisalResult2);
		listResult.add(appraisalResult3);
		listResult.add(appraisalResult4);
		listResult.add(appraisalResult5);
	}

	/**
	 * Process rule check goods
	 * @author catld.ho
	 * @param dataDetail : appraisal object sent by appraisal tool (portal)
	 * @param product : product info get from portal db
	 * @param ruleResults : check rule response from Rule Service
	 * @param listResult : list result output
	 * @throws Exception
	 */
	private void processRuleCheckGoods(AppraisalDataDetailDTO dataDetail, ProductDTO product, Map<String, RuleResult> ruleResults, List<AppraisalResult> listResult) throws Exception {
		RuleResult ruleResult = null;
		
		if (!INSTALLMENT_LOAN.equals(dataDetail.getProductGroup()))
			return;
		
		for (int i = 0; i < dataDetail.getGoodsInformation().size(); i++) {
			ruleResult = ruleResults.get(AppraisalRule.GOODS_.value() + (i+1));
			if (ruleResult == null)
				throw new Exception(Messages.getString("appraisal.esbService.checkRules.error3", "GOODS_" + (i+1)));
				
			if (STATUS_CODE_OK.equals(ruleResult.getReturnCode()))
				listResult.add(new AppraisalResult(AppraisalRule.GOODS_.value() + (i+1), false, Messages.getString("appraisal.validate.goods.error")));
			else
				listResult.add(new AppraisalResult(AppraisalRule.GOODS_.value() + (i+1), true, "PASS"));
		}
	}
	
}
