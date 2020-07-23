package com.mcredit.business.checkcat.aggregate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.checkcat.validation.CheckCatValidation;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.model.dto.ResponseIds;
import com.mcredit.model.dto.check_cat.ApproveCatDTO;
import com.mcredit.model.dto.check_cat.CompInsertDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.CustCompanyCheckDTO;
import com.mcredit.model.dto.check_cat.CustCompanyInfoDTO;
import com.mcredit.model.dto.check_cat.ResponseCompTypeDTO;
import com.mcredit.model.dto.check_cat.ResponseSearchCompany;
import com.mcredit.model.dto.check_cat.ResultCheckCatDTO;
import com.mcredit.model.dto.check_cat.ResultRemoveCompDTO;
import com.mcredit.model.dto.check_cat.ResultUpdateListCompDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CompNameHavingEnums;
import com.mcredit.model.enums.EconomicTypeCheckEnums;
import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleResult;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

import common.mcredit.business.checkcat.callout.EsbApi;

public class CheckCatAggregate {
	
	private CheckCatValidation _validCheckCat = new CheckCatValidation();
	private ObjectMapper _mapper = new ObjectMapper();
	private UnitOfWorkCustomer unitOfWorkCustomer = null;
	private UnitOfWork uow = null;
	private CodeTableCacheManager _cacheCodeTable = CodeTableCacheManager.getInstance();
	private ApiResult _result = new ApiResult();
	private static final SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static final EsbApi _esb = new EsbApi();
	
	// Var enums
	private static final String ONE = "1";
	private static final String ZERO = "0";
	private static final int TAX_MAX_LENGHT = 14;
	private static final String DASH = "-";
	private static final String CAT_A = "CAT A";
	private static final String CAT_B = "CAT B";
	private static final String CAT_C = "CAT C";
	private static final String DOU_ZERO = "00";
	private static final String TRI_ZERO = "000";
	private static final String ACTIVE = "A";
	private static final String IN_ACTIVE = "C";
	private static final String HTTP_ERR_STATUS = "400";
	private static final String TYPE_MANUAL = "MANUAL";
	private static final String SPACE = " ";
	private static final String BR = "<br/>";
	private static final String ENTERPRISE_CODE_03 = "TYPE_OF_ENTERPRISE_CODE_03";
	private final Long THIRTY_MINUTE = 1800l;
	private static final String RATING_A = "A";
	private static final String RATING_AA = "AA";
	private static final String RATING_AAA = "AAA";

	public CheckCatAggregate(UnitOfWork uow) {

		this.uow = uow;
	}
	
	public CheckCatAggregate(UnitOfWorkCustomer uok) {

		this.unitOfWorkCustomer = uok;
	}

	/* ============ Check cat manager ============  */
	
	/**
	 * Lookup company by sales
	 * 
	 * @author hoanx.ho
	 * @param taxNumber company tax number
	 * @param compName company name
	 * @param catTypeId category type 
	 * @return List Company list company was searched
	 * @throws Exception
	 */
	public List<CustCompanyInfoDTO> saleLookupCompany(String taxNumber, String compName, Long catTypeId) throws Exception {
		return this.unitOfWorkCustomer.customerCompanyInfoRepo().saleLookupCompany(taxNumber, compName, catTypeId);
	}
	
	
	/**
	 * Lookup company by risk
	 * 
	 * @author hoanx.ho
	 * @param SearchCompanyDTO searchDTO
	 * @return
	 * @throws Exception
	 */
	public ResponseSearchCompany riskLookupCompany(SearchCompanyDTO searchDTO) {
		return this.unitOfWorkCustomer.customerCompanyInfoRepo().riskLookupCompany(searchDTO);
	}
	
	/**
	 * Check company by risk
	 * 
	 * @author hoanx.ho
	 * @param companies List CustCompanyCheckDTO
	 * @return List CustCompanyCheckDTO
	 * @throws Exception
	 */
	public List<CustCompanyCheckDTO> saleCheckCompanies(List<CustCompanyCheckDTO> companies) throws ValidationException {
		
		for (CustCompanyCheckDTO c: companies) {
			
			this.handleSetId(c);
			
			if(c.getId() != null) {
				this.setDateCheckCat(c);
			}
			
			c.setCanReClassifyCat(this.handleCheckCanReClassifyCat(c));
			
			c.setResultCheck(this.handleCanCheckCat(c));
			
			c.setDbInfo(this.checkDbInfo(c));
			
		}
		
		return companies;
	}
	
	public void setDateCheckCat(CustCompanyCheckDTO c) {
		String dateCheckCat =  this.unitOfWorkCustomer.customerCompanyInfoRepo().getDateCheckCat(c);
		c.setDateCheckCat(dateCheckCat);
	}
	

	public void handleSetId(CustCompanyCheckDTO c) throws ValidationException {
		Long id =  this.unitOfWorkCustomer.customerCompanyInfoRepo().getExistId(c);
		if (id != null) {
			if (id == -1) {
				throw new ValidationException("Company is duplicated in DB " + c.getTaxNumber() + " : " + c.getName());
			} else {
				c.setId(id);
			}
		}
	}
	
	public String handleCheckCanReClassifyCat(CustCompanyCheckDTO c) {
		
		Integer countDateCheckCat = this.unitOfWorkCustomer.customerCompanyInfoRepo().countDateCheckCat(c);
		if (countDateCheckCat == null || (countDateCheckCat != null && countDateCheckCat == -1)) {
			return "";
		}
		
		if ((countDateCheckCat / 30) >= 6) {
			return Messages.getString("check.cat.yes");
		}
		
		return Messages.getString("check.cat.no");
		
	}
	
	public String handleCanCheckCat(CustCompanyCheckDTO company) {
		boolean isDuplicate = this.unitOfWorkCustomer.customerCompanyInfoRepo().isDuplicateCheckCat(company);
		
		if (isDuplicate) {
			return Messages.getString("check.cat.duplicate");
		}
		
		boolean isBlackList = this.unitOfWorkCustomer.customerCompanyInfoRepo().checkBlackListByTaxNumber(company.getTaxNumber());
		boolean isCanCheckCat = !isBlackList && (company.getCanReClassifyCat().equals(Messages.getString("check.cat.yes")) ||  company.getCanReClassifyCat().equals(""));
		if (isCanCheckCat) {
			return Messages.getString("check.cat.qualify");
		} else {
			return Messages.getString("check.cat.not.qualify");
		}
	}
	
	public String checkDbInfo(CustCompanyCheckDTO currentCompany) {
		
		CustCompanyInfoDTO existCompany = this.unitOfWorkCustomer.customerCompanyInfoRepo().getCompanyCheck(currentCompany);
		
		if (existCompany == null) {
			return "";
		}
		
		String result = "";
		
		if (!currentCompany.getTaxNumber().equals(existCompany.getTaxNumber())) {
			result += Labels.getString("lable.cat.tax.number") + existCompany.getTaxNumber() + BR;
		}
		
		String newName = StringUtils.formatName(currentCompany.getName());
		String existName = StringUtils.formatName(existCompany.getName());
		
		if (!newName.equals(existName)) {
			result += Labels.getString("lable.cat.comp.name") + existCompany.getName() + BR;
		}
		
		if (!currentCompany.getPhoneNumber().trim().equals(existCompany.getPhoneNumber().trim())) {
			result += Labels.getString("lable.cat.comp.phone") + existCompany.getPhoneNumber() + BR;
		}
		
		String newAddress = StringUtils.formatName(currentCompany.getAddress());
		String existAddress = StringUtils.formatName(existCompany.getAddress());
		
		if (!newAddress.equals(existAddress)) {
			result += Labels.getString("lable.cat.comp.add") + existCompany.getAddress();
		}
		
		return result;
	}
	

	public ResponseIds changeProcessStatus(List<CustCompanyCheckDTO> companies, String loginId) {
		ResponseIds response = new ResponseIds();
		List<Long> successIds = new ArrayList<Long>();
		List<Long> errorIds = new ArrayList<Long>();
		
		for (CustCompanyCheckDTO c: companies) {
			try {
				
				Long id = null;
				if (c.getId() != null) {
					 this.unitOfWorkCustomer.customerCompanyInfoRepo().changeProcessStatus(c.getId(), loginId);
					 id = c.getId();
				} else {
					CustomerCompanyInfo comp = new CustomerCompanyInfo(c.getId(), c.getName(), c.getTaxNumber(), c.getAddress(), c.getPhoneNumber(), loginId, ZERO);
					comp.setLastUpdatedBy(loginId);
					id = this.unitOfWorkCustomer.customerCompanyInfoRepo().save(comp);
				}
				successIds.add(id);
				
			} catch (Exception e) {
				errorIds.add(c.getId());
				e.printStackTrace();
			}
		}
		
		response.setSuccessIds(successIds);
		response.setErrorIds(errorIds);
		
		return response;
	}
	
	public List<ApproveCatDTO> getAllCompForCheckCat(){
		return unitOfWorkCustomer.customerCompanyInfoRepo().getAllCompForCheckCat();
	}

	public ResultRemoveCompDTO removeListComp(String lstTaxNumber) {
		List<String> lstTaxErrors = new ArrayList<String>();
		String[] str = lstTaxNumber.trim().split(",");
		List<String> lstParam = new ArrayList<String>();
		int index = 0;
		for (int i = 0; i < str.length; i++) {
			lstParam.add(str[i]);
		}
		if(lstParam.size() == 0) {
			lstTaxErrors.add(Messages.getString("check.cat.remove.comp.null"));
			return new ResultRemoveCompDTO(0, lstTaxErrors);
		}
		for (String taxItem : lstParam) {
			index++;
			if(StringUtils.isNullOrEmpty(taxItem)) {
				lstTaxErrors.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.null"));
				continue;
			}
			if(unitOfWorkCustomer.customerCompanyInfoRepo().checkCompExist(taxItem) == null) {
				lstTaxErrors.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.tax.number.not.system"));
			}
		}
		int countRemove = lstParam.size() - lstTaxErrors.size();
		unitOfWorkCustomer.customerCompanyInfoRepo().removeCompany(lstParam);
		return new ResultRemoveCompDTO(countRemove, lstTaxErrors);
	}

	public ResultUpdateListCompDTO updateListTopComp(List<String> lstRemoveComp, List<CompInsertDTO> lstInsertComp, String ctCat, String loginId) {
		List<String> lstRemoveErrors = new ArrayList<String>();
		List<String> lstInsertErrors = new ArrayList<String>();
		List<String> lstErr = new ArrayList<String>();
		int index =0;
		ResultUpdateListCompDTO result = new ResultUpdateListCompDTO();
		// removeComp
		if(lstRemoveComp == null || lstRemoveComp.size() == 0) {
			result.setRemoveComp(0);
		} else {
			for (String taxNum : lstRemoveComp) {
				index++;
				if(StringUtils.isNullOrEmpty(taxNum)) {
					lstRemoveErrors.add(Labels.getString("lable.cat.sheet.delete") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.taxnumber.null"));
					continue;
				}
				if(!checkIsCompExits(taxNum,ctCat))
					lstRemoveErrors.add(Labels.getString("lable.cat.sheet.delete")
							+ Labels.getString("lable.cat.tool.line") + index + SPACE
							+ Messages.getString("check.cat.tax.number.not.exits") + " " + getTextListCompany(ctCat));
			}
			unitOfWorkCustomer.customerCompanyInfoRepo().deteleTopComp(lstRemoveComp, ctCat);
			// set processing cust_company_info to 0
			unitOfWorkCustomer.customerCompanyInfoRepo().changeProcessingAfterAdjustComp(lstRemoveComp, loginId);
			result.setRemoveComp(lstRemoveComp.size()-lstRemoveErrors.size());
		}
		lstErr.addAll(lstRemoveErrors);
		
		// insertComp
		int countSuccess = 0;
		int index2 = 0;
		if(lstInsertComp == null || lstInsertComp.size() == 0) {
			result.setInsertComp(0);
		} else {
			for (CompInsertDTO item : lstInsertComp) {
				index2++;
				if(!_validCheckCat.validateTaxNumberFormat(item,lstInsertErrors, index2) || !checkInsertCat(item, lstInsertErrors, index2, ctCat))
					continue;
				unitOfWorkCustomer.customerCompanyInfoRepo().insertCompToCodeTable(item.getTaxNumber(), item.getCompName(), ctCat);
				// set processing cust_company_info to 0
				List<String> taxCompNumber = new ArrayList<String>();
				taxCompNumber.add(item.getTaxNumber());
				unitOfWorkCustomer.customerCompanyInfoRepo().changeProcessingAfterAdjustComp(taxCompNumber, loginId);
				countSuccess++;
			}
			result.setInsertComp(countSuccess);
		}
		lstErr.addAll(lstInsertErrors);
		result.setLstErrors(lstErr);
		return result;
	}
	
	private String getTextListCompany(String ctCat) {
		CTCat category = CTCat.from(ctCat);
		if(CTCat.TOP_COMP == category )
			return Labels.getString("lable.cat.top.com");
		if(CTCat.MULTINATIONAL_COMP == category)
			return Labels.getString("lable.cat.multi.com");
		return null;
	}

	// Response get rule
	public ResponseCompTypeDTO checkCompType(List<CompanyDTO> lstComp) throws Exception {
		ResponseCompTypeDTO result = new ResponseCompTypeDTO();
		List<String> lstErrs = new ArrayList<String>();
		List<CompanyDTO> lstSuccess = new ArrayList<CompanyDTO>();
		List<String> lstCheckedCompTax = new ArrayList<String>();
		for (CompanyDTO compDto : lstComp) {
			int index = lstComp.indexOf(compDto) + 1;
			System.out.println("==> "+ compDto);
			CustomerCompanyInfo dtoCheck = unitOfWorkCustomer.customerCompanyInfoRepo().checkCompExist(compDto.getCompTaxNumber());
			
			// Main check:
			if (!validateGetCompType(compDto, index, lstErrs, dtoCheck, lstCheckedCompTax) || !_validCheckCat.validateCheckCompType(compDto, lstErrs, index)) 
				continue;
			
			if (dtoCheck.getCatType() != null) {
				compDto.setCatType(_cacheCodeTable.getbyID(dtoCheck.getCatType().intValue()).getDescription1());
				compDto.setDateCheckCat(_sdf.format(dtoCheck.getLastUpdatedDate()));
			} else
				compDto.setDateCheckCat(_sdf.format(new Date()));
			compDto.setRecordStatus(changeRecordStatus(compDto.getRecordStatus()));
			compDto.setOperationMonth(getOperationMonth(compDto.getEstablishDate()));
			
			// Set: top_comp, top_branch, multi_comp
			checkComp(compDto);
			
			List<String> lstMultiComp = _cacheCodeTable.getMultiComp();
			if(lstMultiComp.contains(compDto.getCompTaxNumber()))
				compDto.setCompType(ENTERPRISE_CODE_03);
			else
				compDto.setCompType(getCompanyType(compDto));
			lstSuccess.add(compDto);
		}
		sortCheckComp(lstSuccess);
		result.setLstComp(lstSuccess);
		result.setErrors(lstErrs);
		return result;
	}
	
	public List<CompanyDTO> getCheckCatResult(List<CompanyDTO> lstComp) throws Exception{
		for (CompanyDTO comItem : lstComp) {
			comItem.setCatType(getCatResult(comItem));
			comItem.setCicConsultingDate(_sdf.format(new Date()));
			comItem.setDateCheckCat(_sdf.format(new Date()));
		}
		return lstComp;
	}
	
	public ResponseCompTypeDTO saveResultCheckCat(List<CompanyDTO> lstComp) throws Exception{
		ResponseCompTypeDTO res = new ResponseCompTypeDTO();
		for (CompanyDTO comItem : lstComp) {
			CustomerCompanyInfo comp = unitOfWorkCustomer.customerCompanyInfoRepo().checkCompExist(comItem.getCompTaxNumber());
			comp.setCatType(_cacheCodeTable.getCodeByCategoryCodeValue1(CTCat.CAT_TYPE.value(), getCatCodeValue1(comItem.getCatType().trim()).value()).getId());
			comp.setCicInfo(_cacheCodeTable.getCodeByCategoryCodeValue1(CTCat.CORP_CIC.value(), comItem.getCicInfo().trim()).getId().toString());
			comp.setCompType(_cacheCodeTable.getCodeByCategoryCodeValue1(CTCat.CORP_TYPE.value(), comItem.getCompType().trim()).getId());
			comp.setIsMultiComp(comItem.getIsMultiComp());
			comp.setIsTopBranch(comItem.getIsTopBranch());
			comp.setIsTopComp(comItem.getIsTopComp());
			comp.setCompAddrStreet(comItem.getCompAddrStreet());
			comp.setCicCode(comItem.getCicCode());
			comp.setCompName(comItem.getCompName());
			comp.setOfficePhoneNumber(comItem.getOfficePhoneNumber());
			comp.setEstablishDate(_sdf.parse(comItem.getEstablishDate()));
			comp.setTaxChapter(comItem.getTaxChapter());
			comp.setOperationMonth(comItem.getOperationMonth());
			comp.setRecordStatus(comItem.getRecordStatus());
			comp.setCicConsultingDate(new Date());
			comp.setEconomicType((_cacheCodeTable.getIdBy(CTCodeValue1.from(comItem.getEconomicType()), CTCat.ECONOMIC_TYPE)).intValue());
			comp.setProcessing(ONE);
			comp.setLastUpdatedDate(new Date());
			unitOfWorkCustomer.customerCompanyInfoRepo().upsert(comp);
		}
		res.setLstComp(lstComp);
		res.setMessage(Messages.getString("check.cat.save.result"));
		return res;
	}
	
	public ResultCheckCatDTO checkCatForLOSService(String taxNumber, String productCode) throws ValidationException {
		
		// RQ1043 tested in 3 months
		if (!StringUtils.isNullOrEmpty(productCode)) {
			if ("C0000052".equals(productCode)) { // CS SY A MB
				ResultCheckCatDTO mbCreditRating = this.unitOfWorkCustomer.customerCompanyInfoRepo().getMBCreditRating(taxNumber);
				
				System.out.println("CS SY A MB");
				if (mbCreditRating == null) {
					throw new ValidationException("Công ty chưa được xếp hạng");
				}
				
				mbCreditRating.setCatType(this.checkCatTypeForMBCreditRating(mbCreditRating.getCatType()));
				
				return mbCreditRating;
			}
			if ("C0000027".equals(productCode) || "C0000028".equals(productCode) || "C0000029".equals(productCode)) { // CS SY A 37, CS SY B 47, CS SY C 60
				ResultCheckCatDTO mbCreditRating = this.unitOfWorkCustomer.customerCompanyInfoRepo().getMBCreditRating(taxNumber);
				
				System.out.println("CS SY A 37");
				if (mbCreditRating != null) {
					mbCreditRating.setCatType(this.checkCatTypeForMBCreditRating(mbCreditRating.getCatType()));
					
					return mbCreditRating;
				}
			}
		}
		// RQ1043 End
		
		ResultCheckCatDTO compCheckCat = this.unitOfWorkCustomer.customerCompanyInfoRepo().getCheckCatForLOSService(taxNumber);
		if (compCheckCat == null) {
			throw new ValidationException(Messages.getString("check.cat.comp.not.checked.cat"));
		}
		// Update: catType of out follow request from BPM (catType is not CAT A B C return "NO")
		if ( !CAT_A.equals(compCheckCat.getCatType()) && !CAT_B.equals(compCheckCat.getCatType()) 
				&& !CAT_C.equals(compCheckCat.getCatType()) ) {
			compCheckCat.setCatType("NO");
		}
		return compCheckCat;
	}

	// Convert rule object check compType
	private RuleObject getObjectCheckCompType(CompanyDTO compDTO) {
		RuleObject object = new RuleObject();
		String taxChapter = getTaxChapterGroup(compDTO.getCompTaxNumber(), compDTO.getTaxChapter());
		String compName = StringUtils.removeAccent(compDTO.getCompName()).toLowerCase();
		object.setRuleCode(CTCat.CORP_TYPE.value());
		object.setEconomicTypeHave(setEconomicType(compDTO.getEconomicType()));
		if (!StringUtils.isNullOrEmpty(object.getEconomicTypeHave())) {
			return object;
		} else {
			object.setTaxChapterGroup(taxChapter);
			object.setCompNameHave(setCompNameHaving(compName, taxChapter));
			object.setCompNameHave2(setCompNameHaving2(compName, taxChapter));
			object.setCompNameNotHave(setCompNameNotHaving(compName, taxChapter));
			object.setLenghtTaxNumber(setLengthTaxNumber(compDTO.getCompTaxNumber()));
		}
		return object;
	}
	
	// Convert rule object check cat
	private RuleObject getObjectCheckCat(CompanyDTO compDTO) {
		RuleObject object = new RuleObject();
		object.setRuleCode(CTCat.CAT_TYPE.value());
		object.setCompType(compDTO.getCompType());
		object.setTopComp(Integer.parseInt(compDTO.getIsTopComp()));
		object.setTopBranch(Integer.parseInt(compDTO.getIsTopBranch()));
		object.setCicInfo(compDTO.getCicInfo());
		object.setOperationTime(compDTO.getOperationMonth());
		object.setRecordStatus(compDTO.getRecordStatus().equals("A") ? 1 : 0);
		return object;
	}

	// Call rule get companyType
	private String getCompanyType(CompanyDTO dto) throws Exception {
		RuleResult out = new RuleResult();
		_result = _esb.getRuleApi(getObjectCheckCompType(dto));
		if (_result.getStatus()) {
			if (_result.getBodyContent().contains(HTTP_ERR_STATUS))
				return TYPE_MANUAL;
			else {
				out = _mapper.readValue(_result.getBodyContent().toString(), new TypeReference<RuleResult>() {});
				return out.getScalarValue();
			}
		} else
			throw new ValidationException(Messages.getString("check.cat.process.err"));
	}
	
	private String getCatResult(CompanyDTO dto) throws Exception {
		RuleResult out = new RuleResult();
		_result = _esb.getRuleApi(getObjectCheckCat(dto));
		if (_result.getStatus()) {
			if(_result.getBodyContent().contains(HTTP_ERR_STATUS))
				return _cacheCodeTable.getBy(CTCodeValue1.OTHERS, CTCat.CAT_TYPE).getDescription1();
			else {
				out = _mapper.readValue(_result.getBodyContent().toString(), new TypeReference<RuleResult>() {});
				return out.getScalarValue();
			}
		} else 
			throw new ValidationException(Messages.getString("check.cat.process.err"));
	}

	//Get tax chapter group for check rule compType
	private String getTaxChapterGroup(String taxNumber, String taxChapter) {
		if (!StringUtils.isNullOrEmpty(taxNumber))
			return (taxNumber.substring(0, 2).equals(DOU_ZERO) || taxChapter.equals(TRI_ZERO))
					? CTCodeValue1.OTHERS.value()
					: unitOfWorkCustomer.customerCompanyInfoRepo().getChapterGroup(taxChapter);
		return null;
	}

	//Set company having for check rule compType
	private String setCompNameHaving(String compName, String taxChapterGr) {
		String compHaving = null;
		List<String> lstHaving = unitOfWorkCustomer.customerCompanyInfoRepo().getCompHaving(taxChapterGr,
				CompNameHavingEnums.COMP_NAME_HAVE.value());
		for (String item : lstHaving) {
			if (compName.contains(item)) {
				compHaving = item;
				break;
			}
		}
		return compHaving;
	}

	//Set company Having2 for check rule compType
	private String setCompNameHaving2(String compName, String taxChapterGr) {
		String having2 = null;
		if(taxChapterGr.equals("D") && setCompNameHaving(compName, taxChapterGr) == null)
			return having2;
		List<String> lstHaving2 = unitOfWorkCustomer.customerCompanyInfoRepo().getCompHaving(taxChapterGr,
				CompNameHavingEnums.COMP_NAME_HAVE_2.value());
		if (lstHaving2 == null || lstHaving2.size() == 0)
			having2 = null;
		else {
			for (String item : lstHaving2) {
				String having = setCompNameHaving(compName, taxChapterGr);
				if (!StringUtils.isNullOrEmpty(having)) {
					if (compName.replace(having, "").contains(item)) {
						having2 = item;
						break;
					}
				} else {
					if (compName.contains(item)) {
						having2 = item;
						break;
					}
				}
			}
		}

		return having2;
	}

	// Set company not Having for check rule compType
	private String setCompNameNotHaving(String compName, String taxChapterGr) {
		List<String> lstNotHaving = unitOfWorkCustomer.customerCompanyInfoRepo().getCompHaving(taxChapterGr,
				CompNameHavingEnums.COMP_NAME_NOTHAVE.value());
		if (lstNotHaving == null || lstNotHaving.size() == 0)
			return null;
		if (StringUtils.isNullOrEmpty(setCompNameHaving(compName, taxChapterGr))
				&& StringUtils.isNullOrEmpty(setCompNameHaving2(compName, taxChapterGr)))
			return lstNotHaving.get(new Random().nextInt(lstNotHaving.size()));
		return null;
	}

	// Set economic type for check rule compType
	private String setEconomicType(String ecoType) {
		String economicTypeHave = null;
		String economicType = null;
		List<CTCodeValue1> lst = Arrays.asList(CTCodeValue1.values());
		for (CTCodeValue1 ctCodeValue1 : lst) {
			if(ctCodeValue1.value().equals(ecoType.trim())) {
				economicType = _cacheCodeTable.getBy(ctCodeValue1, CTCat.ECONOMIC_TYPE).getDescription1();
				break;
			}
		}
		if (!StringUtils.isNullOrEmpty(economicType)) {
			economicType = StringUtils.removeAccent(economicType.toLowerCase().replaceAll(",", ""));
			List<EconomicTypeCheckEnums> lstEcoHave = Arrays.asList(EconomicTypeCheckEnums.values());
			for (EconomicTypeCheckEnums item : lstEcoHave) {
				if (economicType.contains(item.value())) {
					economicTypeHave = item.value();
					break;
				}
			}
		}
		return economicTypeHave;
	}

	//Get length taxnumber
	private String setLengthTaxNumber(String taxNumber) {
		return StringUtils.isNullOrEmpty(taxNumber) ? null : String.valueOf(taxNumber.length());
	}

	// Check company in list
	private void checkComp(CompanyDTO dto) {
		// Check company in top 500-1000
		if (_cacheCodeTable.getTopComp().contains(dto.getCompTaxNumber())) {
			dto.setIsTopComp(ONE);
			dto.setIsTopBranch(ZERO);
		} else {
			// Check company in top 500-1000 branch
			dto.setIsTopComp(ZERO);
			if (dto.getCompTaxNumber().length() == TAX_MAX_LENGHT && _cacheCodeTable.getTopComp()
					.contains(dto.getCompTaxNumber().substring(0, dto.getCompTaxNumber().indexOf(DASH))))
				dto.setIsTopBranch(ONE);
			else
				dto.setIsTopBranch(ZERO);
		}
		// Check company in list multi comp
		if (_cacheCodeTable.getMultiComp().contains(dto.getCompTaxNumber()))
			dto.setIsMultiComp(ONE);
		else
			dto.setIsMultiComp(ZERO);
	}
	
	private int getOperationMonth(String dateInput) throws ParseException {
		Long totalDay = TimeUnit.MILLISECONDS.toDays((new Date()).getTime() - _sdf.parse(dateInput).getTime());
		return (int) (totalDay/30);
	}
	
	private CTCodeValue1 getCatCodeValue1(String input) {
		if(input.trim().equals(CAT_A))
			return CTCodeValue1.CAT_A;
		else if(input.trim().equals(CAT_B))
			return CTCodeValue1.CAT_B;
		else if(input.trim().equals(CAT_C))
			return CTCodeValue1.CAT_C;
		return CTCodeValue1.OTHERS;
	}
	
//	private Integer getCICInfoValue(String input) {
//		List<CodeTableDTO> list = CacheManager.CodeTable().getByGroupAndCategory("CORP", "CORP_CIC");
////		list.contains();
//		
//		return 111;
//	}
	
	private String changeRecordStatus(String input) {
		return input.equals(ONE)? ACTIVE : IN_ACTIVE;
	}
	
	private void sortCheckComp(List<CompanyDTO> lstComp){
		Comparator<CompanyDTO> sortManulComp = new Comparator<CompanyDTO>() {
			public int compare(CompanyDTO com1, CompanyDTO com2) {
				if(com1.getCompType().equals(TYPE_MANUAL))
					return -1;
				if(com2.getCompType().equals(TYPE_MANUAL))
					return 1;
				return 0;
			}
		};
		Collections.sort(lstComp, sortManulComp);
	}
	
	private boolean checkInsertCat(CompInsertDTO dto, List<String> lstInsertErrors, int index, String ctCat) {
		if(checkIsBlackList(dto.getTaxNumber())) {
			lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.tax.number.black.list"));
			return false;
		}
		if(checkIsCompExits(dto.getTaxNumber(), ctCat)) {
			lstInsertErrors.add(Labels.getString("lable.cat.sheet.insert") + Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.tax.number.exist"));
			return false;
		}
		return true;
	}
	
	private boolean checkIsBlackList(String taxNumber) {
		return unitOfWorkCustomer.customerCompanyInfoRepo().checkBlackListByTaxNumber(taxNumber)? true : false;
	}
	
	private boolean checkIsCompExits(String taxNumber, String ctCat) {
		return unitOfWorkCustomer.customerCompanyInfoRepo().checkTopCompExist(taxNumber, ctCat)? true : false;
	}

	
	private boolean validateGetCompType(CompanyDTO compDto, int index, List<String> lstErrs, CustomerCompanyInfo dtoCheck, List<String> lstCheckedCompTax) {
		// check company exist
		if(dtoCheck == null) {
			lstErrs.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.not.request"));
			return false;
		}
		
		// checkDuplicateComp
		if(checkDuplicateComp(compDto.getCompTaxNumber(), lstCheckedCompTax)) {
			lstErrs.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.tax.duplicate"));
			return false;
		}
		
		// validate processing
		if(!dtoCheck.getProcessing().equals(ZERO)) {
			lstErrs.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.is.checked.cat"));
			return false;
		}
				
		//validate black list
		if(checkIsBlackList(compDto.getCompTaxNumber())) {
			lstErrs.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.tax.number.black.list"));
			return false;
		}
		
		// validate tax chapter group
		if (StringUtils.isNullOrEmpty(getTaxChapterGroup(compDto.getCompTaxNumber(), compDto.getTaxChapter()))) {
			lstErrs.add(Labels.getString("lable.cat.tool.line") + index + SPACE + Messages.getString("check.cat.comp.tax.chapter.err"));
			return false;
		}
		return true;
	}
	
	private boolean checkDuplicateComp (String compTax, List<String> lstCheckedCompTax) {
		boolean result = false;
		if (lstCheckedCompTax.contains(compTax)) {
			result = true;
		}
		lstCheckedCompTax.add(compTax);
		return result;
	}
	
	private String checkCatTypeForMBCreditRating (String catType) {
		if (RATING_A.equals(catType) 
				|| RATING_AA.equals(catType)
				|| RATING_AAA.equals(catType)) {
			return catType;
		} else {
			return "NO";
		}
	}
	
}
