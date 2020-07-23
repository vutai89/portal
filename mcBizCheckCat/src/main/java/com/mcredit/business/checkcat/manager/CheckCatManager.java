package com.mcredit.business.checkcat.manager;

import java.util.ArrayList;
import java.util.List;

import com.mcredit.business.checkcat.aggregate.CheckCatAggregate;
import com.mcredit.business.checkcat.validation.CheckCatValidation;
import com.mcredit.model.dto.ResponseIds;
import com.mcredit.model.dto.check_cat.ApproveCatDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.CustCompanyCheckDTO;
import com.mcredit.model.dto.check_cat.CustCompanyInfoDTO;
import com.mcredit.model.dto.check_cat.ResponseCompTypeDTO;
import com.mcredit.model.dto.check_cat.ResponseSearchCompany;
import com.mcredit.model.dto.check_cat.ResultRemoveCompDTO;
import com.mcredit.model.dto.check_cat.ResultUpdateListCompDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.dto.check_cat.UpdateTopCompDTO;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class CheckCatManager extends BaseManager {
	
	private CheckCatValidation _custValid = new CheckCatValidation();
	private final String USER_ON_PAGE_REQUEST_CAT = "USER_ON_PAGE_REQUEST_CAT";
	private CheckCatAggregate _catAgg = new CheckCatAggregate(this.uok.customer);
	
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
		return this.tryCatch( () -> {
			List<CustCompanyInfoDTO> result = null;
			_custValid.validateTaxNumber(taxNumber);
			result = _catAgg.saleLookupCompany(taxNumber, compName, catTypeId);
			return result;
		});
	}

	/**
	 * Lookup company by risk
	 * 
	 * @author hoanx.ho
	 * @param SearchCompanyDTO searchDTO
	 * @return
	 * @throws Exception
	 */
	public ResponseSearchCompany riskLookupCompany(SearchCompanyDTO searchDTO) throws Exception {
		return this.tryCatch( () -> {
		ResponseSearchCompany response = null;
			_custValid.validateRiskLookup(searchDTO);
			response = _catAgg.riskLookupCompany(searchDTO);
			return response;
		});
		
	}
	
	/**
	 * Check company by risk
	 * 
	 * @author hoanx.ho
	 * @param companies List CustCompanyCheckDTO
	 * @return List CustCompanyCheckDTO
	 * @throws Exception
	 */
	public List<CustCompanyCheckDTO> saleCheckCompanies(List<CustCompanyCheckDTO> companies) throws Exception {
		return this.tryCatch( () -> {
			List<CustCompanyCheckDTO> response = null;
			response = _catAgg.saleCheckCompanies(companies);
			return response;
		});
	}
	
	/**
	 * Check process status
	 * 
	 * @author hoanx.ho
	 * @param companies List CustCompanyCheckDTO 
	 * @return ResponseIds
	 * @throws Exception
	 */
	public ResponseIds changeProcessStatus(List<CustCompanyCheckDTO> companies, String loginId) throws Exception {
		return this.tryCatch( () -> {
			ResponseIds response = new ResponseIds();
			response = _catAgg.changeProcessStatus(companies, loginId);
			return response;
		});
		
	}
	
	/**
	 * Get list company will be checked cat
	 * @author cuongvt.ho
	 * @return List company for check category
	 * @throws Exception
	 */
	public List<ApproveCatDTO> getAllCompForCheckCat() throws Exception {
		return this.tryCatch( () -> {
			List<ApproveCatDTO> lstResult = new ArrayList<ApproveCatDTO>();
			lstResult = _catAgg.getAllCompForCheckCat();
			return lstResult;
		});
	}
	
	/**
	 * Remove companies
	 * @author cuongvt.ho
	 * @param taxNumbers list company need remove
	 * @return ResultRemoveCompDTO: list errors remove, count remove success
	 * @throws Exception
	 */
	public ResultRemoveCompDTO removeListComp(String lstTaxNumber) throws Exception {
		return this.tryCatch( () -> {
			ResultRemoveCompDTO result = null;
			_custValid.validateRemoveComp(lstTaxNumber);
			result = _catAgg.removeListComp(lstTaxNumber);
			return result;
		});
		
	}
	
	/**
	 * Update list top company
	 * @author cuongvt.ho
	 * @param dto list UpdateTopCompDTO include list company for removed and list company for insert
	 * @return ResultUpdateListCompDTO: count remove company, count insert company, and list errors.
	 * @throws Exception
	 */
	public ResultUpdateListCompDTO updateTopComp(UpdateTopCompDTO dto, String loginId) throws Exception {
		return this.tryCatch( () -> {
			ResultUpdateListCompDTO result = null;
			this._custValid.validateUpdateTopComp(dto.getLstRemove(), dto.getLstInsert(), dto.getCtCat());
			result = _catAgg.updateListTopComp(dto.getLstRemove(), dto.getLstInsert(), dto.getCtCat(), loginId);
			return result;
		});
			
	}
	
	/**
	 * Check company type
	 * @author cuongvt.ho
	 * @param lstComp list company
	 * @return ResponseCompTypeDTO: list company was checked company type, and list errors
	 * @throws Exception
	 */
	public ResponseCompTypeDTO checkCompType(List<CompanyDTO> lstComp) throws Exception{
		return this.tryCatch( () -> {
			ResponseCompTypeDTO result = null;
			_custValid.validateCheckCompType(lstComp);
			result = _catAgg.checkCompType(lstComp);
			return result;
		});
	}
	
	/**
	 * Check category
	 * @author cuongvt.ho
	 * @param lstComp list company
	 * @return List<CompanyDTO>: list company were checked category
	 * @throws Exception
	 */
	public List<CompanyDTO> checkCatResult(List<CompanyDTO> lstComp) throws Exception{
		return this.tryCatch( () -> {
			List<CompanyDTO> lstResult = null;
			_custValid.validateCheckCat(lstComp);
			lstResult = _catAgg.getCheckCatResult(lstComp);
			return lstResult;
		});	
	}
	
	/**
	 * Save check cat
	 * @author cuongvt.ho
	 * @param lstComp list company were checked category
	 * @return List<CompanyDTO>: list company were checked category
	 * @throws Exception
	 */
	public ResponseCompTypeDTO saveCatResult(List<CompanyDTO> lstComp) throws Exception{
		return this.tryCatch( () -> {
			ResponseCompTypeDTO res = new ResponseCompTypeDTO();
			_custValid.validateCheckCat(lstComp);
			res = _catAgg.saveResultCheckCat(lstComp);
			return res;
		});
		
	}
	
	/**
	 * Check user in one screen
	 * @author hoanx.ho
	 * @return String checked user in screen
	 * @throws Exception
	 */
	public String getStatusOnPageRequestCat() throws Exception {
		String status = null;
		RedisAggregate ra = null;
		try {
			ra = new RedisAggregate();
			status = ra.get(USER_ON_PAGE_REQUEST_CAT) != null ?  ra.get(USER_ON_PAGE_REQUEST_CAT) : "0" ;
		} catch (Exception e) {
			throw new ValidationException(e.toString());
		}
		return status;
	}
	
	/**
	 * Check status on page request category
	 * @author hoanx.ho
	 * @return String check status on page request category
	 * @throws Exception
	 */
	public String changeStatusOnPageRequestCat(String value) throws ValidationException {

		RedisAggregate ra = null;
		String result = "0";
		try {
			ra = new RedisAggregate();
			ra.set(USER_ON_PAGE_REQUEST_CAT, value);
			result = "1";
		} catch (Exception e) {
			throw new ValidationException(e.toString());
		}
		
		return result;
	}
	
	public Object checkCatForLOSService(String comTaxNumber, String productCode) throws Exception{
		return this.tryCatch( () -> {
			_custValid.validateTaxNumber(comTaxNumber);
			return _catAgg.checkCatForLOSService(comTaxNumber, productCode);
		});
	}

}
