package com.mcredit.data.mobile.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.object.mobile.dto.CaseDTO;
import com.mcredit.model.object.mobile.dto.DashboardInfoDTO;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.model.object.mobile.enums.CaseStatusEnum;
import com.mcredit.util.StringUtils;

public class CaseRepository extends BaseRepository implements IRepository<UplCreditAppRequest> {

	public CaseRepository(Session session) {
		super(session);
	}
	
	public DashboardInfoDTO getDashboardInfo(String loginId) {
		
		NativeQuery query = this.session.getNamedNativeQuery("getDashboardInfo").setParameter("loginId", loginId);
		Object info = query.uniqueResult();
		
		DashboardInfoDTO result = transformObject(info, DashboardInfoDTO.class);
		return result;
	}
	
	public List<CaseDTO> getCase(SearchCaseDTO searchCaseDTO, String loginId) {
		
		String status = searchCaseDTO.getStatus();
		
		Integer pageNumber = searchCaseDTO.getPageNumber();
		Integer pageSize = searchCaseDTO.getPageSize();
		
		Integer maxNumber = (pageNumber * pageSize) + 1;
		Integer minNumber = ((pageNumber - 1) * pageSize) + 1;
		
		boolean isGetCaseProcessing = status.equalsIgnoreCase(CaseStatusEnum.PROCESSING.toString());
		boolean isGetCaseAbort = status.equalsIgnoreCase(CaseStatusEnum.ABORT.toString());
		
		String sqlString = "SELECT * FROM (" + 
				" " + 
				"	SELECT a.*, rownum rn FROM ("
				+ " SELECT uc.id,  " + 
				"   to_char(uc.created_date, 'dd/mm/yyyy') AS created_date," + 
				"	uc.app_number, "+ 
				"   uc.credit_app_id, " + 
				"	uc.customer_name, " + 
				"	uc.citizen_id, " + 
				"	p.product_name, " + 
				"	uc.loan_amount, " + 
				"	uc.loan_tenor, " + 
				"	uc.has_insurance, " + 
				"	uc.temp_residence, " + 
				"	ct_kiosk.description2 AS kiosk_address, " + 
				"	ct_bpm_status.description2 AS bpm_status, " + 
				"	uc.checklist " + 
				"	 " + 
				"FROM UPL_CREDIT_APP_REQUEST uc " + 
				"	INNER JOIN products p ON p.id = uc.PRODUCT_ID  " + 
				"	LEFT JOIN code_table ct_kiosk ON ct_kiosk.CODE_VALUE1 = uc.shop_code " + 
				"	LEFT JOIN code_table ct_bpm_status ON ct_bpm_status.id = uc.app_status " +
				" WHERE uc.LAST_UPDATED_BY = :loginId ";
		
		if (isGetCaseProcessing) {
			sqlString += "AND uc.status IN ('C', 'T')";
		} else if (isGetCaseAbort) {
			sqlString += "AND uc.status = 'R'";
		}
		
		boolean isSearch = !StringUtils.isNullOrEmpty(searchCaseDTO.getKeyword().trim());
		if (isSearch) {
			sqlString += "AND  (LOWER(uc.customer_name) LIKE :keyword OR LOWER(uc.citizen_id) LIKE :keyword OR LOWER(uc.app_number) LIKE :keyword )";
		}
		
		sqlString += " ORDER BY uc.CREATED_DATE DESC " + 
					 " ) a WHERE rownum < :maxNumber" + 
					 " " + 
					 " ) WHERE rn >= :minNumber";

		NativeQuery query = this.session.createNativeQuery(sqlString);
		query.setParameter("loginId", loginId);
		
		if (isSearch) {
			String keywordSearch = "%" + searchCaseDTO.getKeyword().toLowerCase() + "%";
			query.setParameter("keyword", keywordSearch);
		}
		
		query.setParameter("maxNumber", maxNumber);
		query.setParameter("minNumber", minNumber);
		
		List lst = query.list();
		List<CaseDTO> cases = transformList(lst, CaseDTO.class);
		return cases;
	}

}
