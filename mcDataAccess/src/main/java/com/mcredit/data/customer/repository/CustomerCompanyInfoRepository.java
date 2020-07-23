package com.mcredit.data.customer.repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.ValidationException;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.common.repository.CodeTableRepository;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.dto.check_cat.ApproveCatDTO;
import com.mcredit.model.dto.check_cat.CompInsertDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.CustCompanyCheckDTO;
import com.mcredit.model.dto.check_cat.CustCompanyInfoDTO;
import com.mcredit.model.dto.check_cat.CustCompanyInfoFullDTO;
import com.mcredit.model.dto.check_cat.ResponseCompCheckCat;
import com.mcredit.model.dto.check_cat.ResponseSearchCompany;
import com.mcredit.model.dto.check_cat.ResultCheckCatDTO;
import com.mcredit.model.dto.check_cat.ResultRemoveCompDTO;
import com.mcredit.model.dto.check_cat.ResultUpdateListCompDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.leadgen.UploadDataId;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.util.StringUtils;
import com.mcredit.util.cache.CacheManager;

public class CustomerCompanyInfoRepository extends BaseRepository implements
		IUpsertRepository<CustomerCompanyInfo> {

	public CustomerCompanyInfoRepository(Session session) {
		super(session);
	}

	public void upsert(CustomerCompanyInfo item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerCompanyInfo");
		else
			this.session.save("CustomerCompanyInfo", item);
	}
	
	public Long save(CustomerCompanyInfo item) throws DataException {
		return (Long) this.session.save("CustomerCompanyInfo", item);		
	}
	
	public void remove(CustomerCompanyInfo item) throws DataException {
		this.session.delete("CustomerCompanyInfo", item);
	}
	
	public List<CategoryDTO> getListCompany() throws DataException {
		List<CategoryDTO> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getListCompany").list();
		if (lst != null && !lst.isEmpty()){
			results = transformList(lst, CategoryDTO.class);
		}
		return results;
	}
	
	
	// Check CAT
	
	public Long getExistId(CustCompanyCheckDTO c) {
		Long result = null;
		String sqlString = "SELECT id FROM CUST_COMPANY_INFO cci"
				+ " where processing = 1";
		
		boolean isSearchByTaxNumber = this.isCheckByTaxNumber(c.getTaxNumber());
		if (isSearchByTaxNumber) {
			sqlString += " and  cci.COMP_TAX_NUMBER = :taxNumber";
		} else {
			sqlString += " and lower(replace(cci.COMP_NAME, ' ')) = :compName";
		}
		
		NativeQuery<?> query = this.session.createNativeQuery(sqlString);
		
		if (isSearchByTaxNumber) {
			query.setParameter("taxNumber", c.getTaxNumber());
		} else {
			query.setParameter("compName", StringUtils.formatName(c.getName()));
		}
		
		Object rs = query.uniqueResult();
		
		if (rs != null) {
			BigDecimal number = (BigDecimal) rs;
			result = number.longValue();
		}
		
		return result;
	}
	
	public String getDateCheckCat(CustCompanyCheckDTO c) {
		String sqlString = "SELECT TO_CHAR(CIC_CONSULTING_DATE, 'dd/mm/yyyy')  FROM CUST_COMPANY_INFO WHERE id = :id";
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("id", c.getId());

		String date =  (String) query.uniqueResult();
		return date != null ? date : "";
	}
	
	public List<CustCompanyInfoDTO> saleLookupCompany(String taxNumber, String companyName, Long catTypeId) throws Exception {
		
		String sqlString = "SELECT cci.ID, cci.COMP_NAME, cci.COMP_TAX_NUMBER, cci.COMP_ADDR_STREET, cci.OFFICE_PHONE_NUMBER, TO_CHAR(cci.CIC_CONSULTING_DATE, 'dd/mm/yyyy') AS date_check_cat, ct.DESCRIPTION1  AS cat_type, cci.ESTABLISH_DATE" + 
				" FROM CUST_COMPANY_INFO  cci" + 
				" LEFT JOIN CODE_TABLE ct ON ct.id = cci.CAT_TYPE" + 
				" WHERE cci.PROCESSING = 1 ";
		
		String moreCondition = " AND  cci.COMP_TAX_NUMBER LIKE :searchTaxNumber";
		
		boolean isSearchCompanyName = !StringUtils.isNullOrEmpty(companyName);
		boolean isSearchCatType = (catTypeId != null && catTypeId > 0);
		
		if (isSearchCompanyName) {
			moreCondition += " AND lower(cci.COMP_NAME) LIKE :searchComp";
		}
	
		if (isSearchCatType) {
			moreCondition += " AND cci.CAT_TYPE = :catTypeId";
		}
		
		
		sqlString = sqlString += moreCondition;
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		
		String searchTaxNumber = "%" + taxNumber + "%";
		query.setParameter("searchTaxNumber", searchTaxNumber.trim());
		
		if (isSearchCompanyName) {
			String searchComp = "%" + companyName + "%";
			query.setParameter("searchComp", searchComp.toLowerCase());
		}
		
		if (isSearchCatType) {
			query.setParameter("catTypeId", catTypeId);
		}
		
		List lst = query.list();
		List<CustCompanyInfoDTO> companies = transformList(lst, CustCompanyInfoDTO.class);
		return companies;
	}
	
	public ResponseSearchCompany riskLookupCompany(SearchCompanyDTO searchDTO) {
		
		ResponseSearchCompany response = new ResponseSearchCompany();
		
		Integer pageNumber = searchDTO.getPageNumber();
		Integer pageSize = searchDTO.getPageSize();
		
		response.setPageNumber(pageNumber);
		response.setPageSize(pageSize);
		
		
		String sqlSearch = "SELECT * FROM ( " + 
				" " + 
				"	SELECT a.*, rownum rn FROM ( " + 
				"	 " + 
				"		SELECT  cci.ID, cci.COMP_NAME, cci.COMP_TAX_NUMBER, cci.COMP_ADDR_STREET,  " + 
				"			cci.OFFICE_PHONE_NUMBER, TO_CHAR(cci.CIC_CONSULTING_DATE, 'DD/MM/YYYY') AS date_check_cat, ct.DESCRIPTION1  AS cat_type,  " +
				"			TO_CHAR(cci.ESTABLISH_DATE, 'dd/mm/yyyy') AS ESTABLISH_DATE,  " + 
				"			cci.TAX_CHAPTER,  " + 
				"			cci.OPERATION_MONTH,  " + 
				"			cci.CIC_CODE, " + 
				"           ct_cic_info.DESCRIPTION1 AS cic_info,  " + 
				"			CASE(cci.IS_TOP_500_1000_COM) " + 
				"			WHEN '1' " + 
				"			THEN  " + 
				"				'Có' " + 
				"			ELSE  " + 
				"				'Không' " + 
				"			END is_top_500_1000,  " + 
				"			 " + 
				"			CASE(cci.IS_TOP_500_1000_BRANCH) " + 
				"			WHEN '1' " + 
				"			THEN  " + 
				"				'Có' " + 
				"			ELSE  " + 
				"				'Không' " + 
				"			END is_top_500_1000_branch, " + 
				"			ct_comp_type.CODE_VALUE1 AS company_type, " + 
				"           ct_eco_type.DESCRIPTION1 AS ecomonic_type, " +
				"			CASE(cci.IS_MULTINATIONAL_COMPANY) " + 
				"			WHEN '1' " + 
				"			THEN  " + 
				"				'Có' " + 
				"			ELSE  " + 
				"				'Không' " + 
				"			END IS_MULTINATIONAL_COMP, " + 
				"			 " + 
				"			CASE cci.RECORD_STATUS " + 
				"			WHEN 'A' " + 
				"			THEN  " + 
				"				'Đang hoạt động' " + 
				"			ELSE " + 
				"			    'Ngừng hoạt động' " + 
				"			END AS comp_status " + 
				"			 " + 
				"		FROM CUST_COMPANY_INFO cci " + 
				"		LEFT JOIN CODE_TABLE ct ON ct.id = cci.CAT_TYPE " +
				"		LEFT JOIN CODE_TABLE ct_cic_info ON ct_cic_info.id = cci.CIC_INFO" + 
				"		LEFT JOIN CODE_TABLE ct_comp_type ON ct_comp_type.id = cci.COMP_TYPE" + 
				"		LEFT JOIN CODE_TABLE ct_eco_type ON ct_eco_type.id = cci.ECONOMIC_TYPE";
		
		String condition = " WHERE cci.PROCESSING = 1 ";
		
		boolean isSearchName = !StringUtils.isNullOrEmpty(searchDTO.getName());
		
		boolean isSearcTaxNumber = (searchDTO.getTaxNumbers() != null && searchDTO.getTaxNumbers().size() > 0);
		
		boolean isSearchEstablishDate = !StringUtils.isNullOrEmpty(searchDTO.getEstablishDate());
		boolean isSearchCicInfo = (searchDTO.getCicInfo() != null && searchDTO.getCicInfo() > 0);
		boolean isSearchDateCheckCat = !StringUtils.isNullOrEmpty(searchDTO.getDateCheckCat());
		boolean isSearchTop500_1000 = !StringUtils.isNullOrEmpty(searchDTO.getTop500_1000());
		boolean isSearchTop100_1000_branch = !StringUtils.isNullOrEmpty(searchDTO.getTop100_1000_branch());
		
		boolean isSearchCompanyType = (searchDTO.getCompanyType() != null && searchDTO.getCompanyType() > 0);
		boolean isSearchEconomicType = (searchDTO.getEconomicType() != null && searchDTO.getEconomicType() > 0);
		boolean isSearchMultinationalCompany =  !StringUtils.isNullOrEmpty(searchDTO.getIsMultinationalCompany());
		boolean isSearchOperationStatus = !StringUtils.isNullOrEmpty(searchDTO.getOperationStatus());
		boolean isSearchCatType =(searchDTO.getCatType() != null && searchDTO.getCatType() > 0);
		
	
		if (isSearcTaxNumber) {
			condition += "	AND cci.COMP_TAX_NUMBER in (:taxNumber) ";
		}
		
		if (isSearchName) {
			condition += " AND LOWER(cci.COMP_NAME) LIKE :compName ";
		}
		
		if (isSearchEstablishDate) {
			condition += "	AND to_char(cci.ESTABLISH_DATE, 'dd/mm/yyyy') = :establishDate ";
		}
		
		if (isSearchCicInfo) {
			condition += " AND cci.CIC_INFO = :cicInfo ";
		}
		
		if (isSearchDateCheckCat) {
			condition += " AND to_char(cci.CIC_CONSULTING_DATE, 'dd/mm/yyyy') = :dateCheckCat ";
		}
		
		if (isSearchTop500_1000) {
			condition += " AND cci.IS_TOP_500_1000_COM = :isTop_500_1000  ";
		}
		
		if (isSearchTop100_1000_branch) {
			condition += "	AND  cci.IS_TOP_500_1000_BRANCH = :isTop500_1000_branch";
		}
		
		if (isSearchCompanyType) {
			condition += "	AND cci.COMP_TYPE = :compType ";
		}
		
		if (isSearchEconomicType) {
			condition += "	AND cci.ECONOMIC_TYPE = :economicType ";
		}
		
		if (isSearchMultinationalCompany) {
			condition += " AND cci.IS_MULTINATIONAL_COMPANY = :isMultinationCompany ";
		}
		
		if (isSearchOperationStatus) {
			condition += " AND cci.RECORD_STATUS = :operationStatus ";
		}
		
		if (isSearchCatType) {
			condition += " AND cci.CAT_TYPE = :catType ";
		}
		
		sqlSearch += condition;
		sqlSearch += "	) a WHERE rownum < :maxNumber" + 
				") WHERE rn > :minNumber";
		
		Integer maxNumber = (pageNumber * pageSize) + 1;
		Integer minNumber = ((pageNumber - 1) * pageSize);

		NativeQuery query = this.session.createNativeQuery(sqlSearch);
	
		query.setParameter("maxNumber", maxNumber);
		query.setParameter("minNumber", minNumber);
		
		String sqlCount = "Select count(id) from CUST_COMPANY_INFO cci" + condition;
		NativeQuery querySearch = this.session.createNativeQuery(sqlCount);
		
		if (isSearcTaxNumber) {
			query.setParameterList("taxNumber", searchDTO.getTaxNumbers());
			querySearch.setParameterList("taxNumber", searchDTO.getTaxNumbers());
		}
		
		if (isSearchName) {
			String keywordSearch = "%" + searchDTO.getName().toLowerCase() + "%";
			query.setParameter("compName", keywordSearch);
			querySearch.setParameter("compName", keywordSearch);
		}
		
		if (isSearchEstablishDate) {
			query.setParameter("establishDate", searchDTO.getEstablishDate());
			querySearch.setParameter("establishDate", searchDTO.getEstablishDate());
		}
		
		if (isSearchCicInfo) {
			query.setParameter("cicInfo", searchDTO.getCicInfo());
			querySearch.setParameter("cicInfo", searchDTO.getCicInfo());
		}
		
		if (isSearchDateCheckCat) {
			query.setParameter("dateCheckCat", searchDTO.getDateCheckCat());
			querySearch.setParameter("dateCheckCat", searchDTO.getDateCheckCat());
		}
		
		if (isSearchTop500_1000) {
			query.setParameter("isTop_500_1000", searchDTO.getTop500_1000());
			querySearch.setParameter("isTop_500_1000", searchDTO.getTop500_1000());
		}
		
		if (isSearchTop100_1000_branch) {
			query.setParameter("isTop500_1000_branch", searchDTO.getTop100_1000_branch());
			querySearch.setParameter("isTop500_1000_branch", searchDTO.getTop100_1000_branch());
		}
		
		if (isSearchCompanyType) {
			query.setParameter("compType", searchDTO.getCompanyType());
			querySearch.setParameter("compType", searchDTO.getCompanyType());
		}
		
		if (isSearchEconomicType) {
			query.setParameter("economicType", searchDTO.getEconomicType());
			querySearch.setParameter("economicType", searchDTO.getEconomicType());
		}
		
		if (isSearchMultinationalCompany) {
			query.setParameter("isMultinationCompany", searchDTO.getIsMultinationalCompany());
			querySearch.setParameter("isMultinationCompany", searchDTO.getIsMultinationalCompany());
		}
		
		if (isSearchOperationStatus) {
			query.setParameter("operationStatus", searchDTO.getOperationStatus());
			querySearch.setParameter("operationStatus", searchDTO.getOperationStatus());
		}
		
		if (isSearchCatType) {
			query.setParameter("catType", searchDTO.getCatType());
			querySearch.setParameter("catType", searchDTO.getCatType());
		}
		
		List lst = query.list();
		List<CustCompanyInfoFullDTO> data = transformList(lst, CustCompanyInfoFullDTO.class);
		
		BigDecimal totalCount = (BigDecimal) querySearch.uniqueResult();
		
		response.setTotalCount(totalCount.longValue());
		response.setData(data);
		return response;
	}
	
	public Integer countDateCheckCat(CustCompanyCheckDTO c) {
		
		String sqlString = "SELECT  " + 
				"CASE  " + 
				"WHEN cci.CIC_CONSULTING_DATE IS NULL  " + 
				"THEN " + 
				"   -1 " + 
				"ELSE  " + 
				"  (trunc(sysdate) - trunc(cci.CIC_CONSULTING_DATE)) " + 
				"END AS countDate  " + 
				"FROM CUST_COMPANY_INFO cci ";
		
		boolean isSearchByTaxNumber = this.isCheckByTaxNumber(c.getTaxNumber());
		if (isSearchByTaxNumber) {
			sqlString += " WHERE  cci.COMP_TAX_NUMBER = :taxNumber";
		} else {
			sqlString += " WHERE lower(replace(cci.COMP_NAME, ' ')) = :compName";
		}
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		
		if (isSearchByTaxNumber) {
			query.setParameter("taxNumber", c.getTaxNumber());
		} else {
			query.setParameter("compName", StringUtils.formatName(c.getName()));
		}
		
		BigDecimal number = (BigDecimal) query.uniqueResult();
		return (number != null) ?  number.intValue() :  null;
	}
	
	public boolean isDuplicateCheckCat(CustCompanyCheckDTO company) {
		
		String sqlString = "SELECT (id) FROM CUST_COMPANY_INFO WHERE PROCESSING = 0";
		
		boolean isSearchByTaxNumber = this.isCheckByTaxNumber(company.getTaxNumber());
		
		if (isSearchByTaxNumber) {
			sqlString += " AND COMP_TAX_NUMBER = :taxNumber";
		} else {
			sqlString += " AND lower(replace(COMP_NAME, ' ')) = :compName";
		}
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		if (isSearchByTaxNumber) {
			query.setParameter("taxNumber", company.getTaxNumber());
		} else {
			query.setParameter("compName", StringUtils.formatName(company.getName()));
		}
		
		BigDecimal id = (BigDecimal) query.uniqueResult();
		
		return (id != null);
		
	}
	
	public Boolean checkBlackListByTaxNumber(String taxNumber) {
		BigDecimal count = (BigDecimal) this.session.createNativeQuery("SELECT COUNT(ID) FROM CUST_MONITOR WHERE ID_NUMBER = :taxNumber and ID_TYPE = (SELECT ID FROM CODE_TABLE WHERE CODE_GROUP = 'CUST' AND CATEGORY = 'IDTYP' AND CODE_VALUE1 = '5') AND MONITOR_TYPE = 'B'")
				.setParameter("taxNumber", taxNumber).uniqueResult();
		
		return count.intValue() > 0;
	}
	
	public CustCompanyInfoDTO getCompanyCheck(CustCompanyCheckDTO company) {
		String sqlString = "SELECT cci.ID, cci.COMP_NAME, cci.COMP_TAX_NUMBER, cci.COMP_ADDR_STREET, cci.OFFICE_PHONE_NUMBER" + 
				" FROM CUST_COMPANY_INFO  cci";
		
		boolean isSearchByTaxNumber = this.isCheckByTaxNumber(company.getTaxNumber());
		if (isSearchByTaxNumber) {
			sqlString += " WHERE cci.COMP_TAX_NUMBER = :taxNumber";
		} else {
			sqlString += " WHERE lower(replace(COMP_NAME, ' ')) = :compName";
		}
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		
		if (isSearchByTaxNumber) {
			query.setParameter("taxNumber", company.getTaxNumber());
		} else {
			query.setParameter("compName", StringUtils.formatName(company.getName()));
		}
		
		Object obj = query.uniqueResult();
		return  obj != null ? transformObject(obj, CustCompanyInfoDTO.class) : null;
	}
	
	public Integer changeProcessStatus(Long id, String loginId) {
		String sqlString = "UPDATE CUST_COMPANY_INFO SET PROCESSING = 0, LAST_UPDATED_DATE = SYSDATE, LAST_UPDATED_BY = :loginId WHERE id = :id";
		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("id", id);
		query.setParameter("loginId", loginId);
		return query.executeUpdate();
	}
	
	// Utils
	public boolean isCheckByTaxNumber(String taxNumber) {
		return (!taxNumber.substring(0, 2).equals("00"));
	}
	
	@SuppressWarnings({ "null", "unchecked" })
	public List<ApproveCatDTO> getAllCompForCheckCat() throws DataException {
		List<ApproveCatDTO> lstResult = new ArrayList<>();
		List<?> lst = this.session.getNamedNativeQuery("getAllCompForCheckCat").getResultList();
		if(lst !=null || lst.size() != 0) {
			lstResult = transformList(lst, ApproveCatDTO.class);
		}
		return lstResult;
	}
	
	
	public void removeCompany(List<String> lstTaxNumber) {
		this.session.getNamedQuery("removeCompany").setParameterList("lstTaxNumber", lstTaxNumber).executeUpdate();
	}
	
	public void deteleTopComp(List<String> lstRemove, String ctCat) {
		this.session.getNamedNativeQuery("deteleTopComp").setParameterList("lstTaxNumber", lstRemove).setParameter("ctCat", ctCat).executeUpdate();
	}
	
	public void insertCompToCodeTable(String taxNumber, String compName, String ctCat) {
		this.session.getNamedNativeQuery("insertCompToCodeTable").setParameter("ctCat", ctCat)
		.setParameter("taxNumber", taxNumber)
		.setParameter("compName", compName).executeUpdate();
	}
	
	public void changeProcessingAfterAdjustComp(List<String> lstComp, String loginId) {
		this.session.getNamedNativeQuery("changeProcessingAfterAdjustComp")
			.setParameterList("lstTaxNumber", lstComp)
			.setParameter("loginId", loginId).executeUpdate();
	}
	
	public CustomerCompanyInfo checkCompExist(String taxNumber) {
		return (CustomerCompanyInfo) this.session.getNamedQuery("checkCompExist").setParameter("taxNumber", taxNumber).uniqueResult();
	}
	
	public Boolean checkTopCompExist(String taxNumber, String ctCat) {
		BigDecimal count = (BigDecimal) this.session.getNamedNativeQuery("checkTopComp").setParameter("taxNumber", taxNumber).setParameter("ctCat", ctCat).uniqueResult();
		return count.intValue() > 0;
	}
	
	public String getChapterGroup(String taxChapter) {
		return (String) this.session.getNamedNativeQuery("getChapterGroup").setParameter("taxChapter", taxChapter).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCompHaving(String groupTaxChapter, String typeCheck){
		List<String> lstCheck = new ArrayList<String>();
		String result = (String) this.session.getNamedNativeQuery("getCompHaving")
				.setParameter("groupTaxChapter", groupTaxChapter)
				.setParameter("typeCheck", typeCheck).uniqueResult();
		if(!StringUtils.isNullOrEmpty(result))
			lstCheck = Arrays.asList(result.split(","));
		return lstCheck;
		
	}
	
	public ResultCheckCatDTO getCheckCatForLOSService(String taxNumber) {
		String sqlString = " SELECT comp_name, ct.description1, comp_addr_street, office_phone_number " + 
				" FROM cust_company_info cci " + 
				" INNER JOIN code_table ct on cci.cat_type = ct.id " + 
				" WHERE cci.PROCESSING = 1 " + 
				" AND cci.comp_tax_number = :taxNumber ";
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("taxNumber", taxNumber.trim());
		
		List<?> rs = null;
		rs = query.list();
		if (rs != null && !rs.isEmpty())
			return (ResultCheckCatDTO) transformList(rs, ResultCheckCatDTO.class).get(0);
		return null;
	}
	
	public ResultCheckCatDTO getMBCreditRating(String taxNumber) {
		String sqlString = " SELECT comp_name, credit_ratings, comp_addr_street, office_phone_number " + 
				" FROM cust_company_info cci " + 
				" WHERE source_type = 'MB' " + 
				" AND cci.comp_tax_number = :taxNumber ";
		
		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("taxNumber", taxNumber.trim());
		
		List<?> rs = null;
		rs = query.list();

		return rs.size() > 0 ? (ResultCheckCatDTO) transformList(rs, ResultCheckCatDTO.class).get(0) : null;
	}

}
