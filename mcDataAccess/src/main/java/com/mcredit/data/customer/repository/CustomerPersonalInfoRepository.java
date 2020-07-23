package com.mcredit.data.customer.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerAccountLink;
import com.mcredit.data.customer.entity.CustomerAddlInfo;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.customer.entity.CustomerContactInfo;
import com.mcredit.data.customer.entity.CustomerFinancialInfo;
import com.mcredit.data.customer.entity.CustomerIdentity;
import com.mcredit.data.customer.entity.CustomerPersonalInfo;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.object.ContractCheck;
import com.mcredit.model.object.warehouse.DocumentStateInfo;
import com.mcredit.model.telesale.ContractSearch;
import com.mcredit.util.StringUtils;

public class CustomerPersonalInfoRepository extends BaseRepository implements IUpsertRepository<CustomerPersonalInfo> {

	public CustomerPersonalInfoRepository(Session session) {
		super(session);
	}

	public void update(CustomerPersonalInfo item) throws DataException {
		this.session.save("CustomerPersonalInfo", item);
	}

	public void upsert(CustomerPersonalInfo item) throws DataException {

		if (item.getId() != null) { // Update case
			updateEntity(item, "CustomerPersonalInfo");
		} else
			this.session.save("CustomerPersonalInfo", item);
	}

	public void remove(CustomerPersonalInfo item) throws DataException {

		CriteriaBuilder builder = this.session.getCriteriaBuilder();

		removeEntity(CustomerIdentity.class, builder, "custId", item.getId());
		removeEntity(CustomerFinancialInfo.class, builder, "custId", item.getId());
		removeEntity(CustomerContactInfo.class, builder, "custId", item.getId());
		removeEntity(CustomerCompanyInfo.class, builder, "custId", item.getId());
		removeEntity(CustomerAddlInfo.class, builder, "custId", item.getId());
		removeEntity(CustomerAccountLink.class, builder, "custId", item.getId());
		removeEntity(CustomerPersonalInfo.class, builder, "id", item.getId());
	}

	public Long findCustContactInfoIdBy(Long custId, Integer contactType, Integer contactCategory,
			String contactValue) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findCustContactInfoIdBy").setParameter("custId", custId)
				.setParameter("contactType", contactType).setParameter("contactCategory", contactCategory)
				.setParameter("contactValue", contactValue).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public Long findCustAddressInfoIdBy(Long custId, Integer addrType, String address) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findCustAddressInfoIdBy").setParameter("custId", custId)
				.setParameter("addrType", addrType).setParameter("address", address).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DocumentStateInfo> findContractInfo(ContractSearch contractSearch) {

		List<DocumentStateInfo> results = new ArrayList<>();

		String subSelect = "";
		String condition = "";

		if ( !StringUtils.isNullOrEmpty(contractSearch.getFromDate()) && !StringUtils.isNullOrEmpty(contractSearch.getToDate()) )
			condition += "  (car.CREATED_DATE >= to_date('" + contractSearch.getFromDate().trim()
					+ " 00:00:00', 'DD/MM/YYYY HH24:MI:SS') and car.CREATED_DATE <= to_date('" + contractSearch.getToDate().trim() + " 23:59:59', 'DD/MM/YYYY HH24:MI:SS')) ";
		else if (!StringUtils.isNullOrEmpty(contractSearch.getFromDate()))
			condition += "  car.CREATED_DATE >= to_date('" + contractSearch.getFromDate().trim() + " 00:00:00', 'DD/MM/YYYY HH24:MI:SS') ";
		else if (!StringUtils.isNullOrEmpty(contractSearch.getToDate()))
			condition += "  car.CREATED_DATE <= to_date('" + contractSearch.getToDate().trim() + " 23:59:59', 'DD/MM/YYYY HH24:MI:SS') ";

		if (!StringUtils.isNullOrEmpty(contractSearch.getIdentityNumber())) {
			subSelect += " inner join CUST_IDENTITY ci on car.CUST_ID = ci.CUST_ID ";
			condition += " and ci.IDENTITY_NUMBER = '" + contractSearch.getIdentityNumber().trim() + "'";
		}

		if (contractSearch.getStatus() != null && contractSearch.getStatus() > 0)
			condition += " and car.STATUS = " + contractSearch.getStatus();
//			condition += " and car.STATUS in ( select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and ( CODE_VALUE2 = (select CODE_VALUE1 from CODE_TABLE where ID = " + contractSearch.getStatus() + ") or CODE_VALUE2 = (select CODE_VALUE2 from CODE_TABLE where ID = " + contractSearch.getStatus() + ") ) ) ";

		if (!StringUtils.isNullOrEmpty(contractSearch.getDsaTsaCode())) {

			String saleCode = contractSearch.getDsaTsaCode().trim();

			if (saleCode.contains(",")) {

				String newValue = "";
				for (String str : saleCode.substring(0, saleCode.length() - 1).split(",")) {
					newValue += "'" + str + "',";
				}
				saleCode = newValue.substring(0, newValue.length() - 1);
			} else
				saleCode = "'" + saleCode + "'";

			condition += " and upper(car.SALE_CODE) in (" + saleCode + ")";
		}

		if (!StringUtils.isNullOrEmpty(contractSearch.getCaseId())) {
			subSelect += " inner join CREDIT_APP_BPM cab on car.ID = cab.credit_app_id ";
			condition += " and cab.BPM_APP_NUMBER = '" + contractSearch.getCaseId().trim() + "'";
		}

		if (!"".equals(condition))
			condition = " where " + condition;

		String sql = " select distinct "
				+ " cab.WORKFLOW as flow, to_char(car.CREATED_DATE, 'dd/MM/yyyy') as initDocTime, to_char(car.COMPLETED_DATE, 'dd/MM/yyyy') as lastDocTime, car.CREATED_BY as initDocUser, cab.BPM_APP_NUMBER as caseNumber, car.MC_CONTRACT_NUMBER as contractNumber "
				+ " , cpi.CUST_NAME as custName, ci.IDENTITY_NUMBER as identityNumber, p.PRODUCT_NAME as schemeSp, caa.APPROVED_AMOUNT as approveBalance, ct2.description1 as insuranceLoan "
				+ " , ct3.description1 as disbursementChannel, car.SALE_CODE as saleCode, e.FULL_NAME as saleName, ct4.description1 as sipHubKioskCode "
				+ " , rr.DE1 as noteDe1, rr.DE2 as noteDe2, rr.DC as noteDc, rr.CA as noteCa, rr.OP1 as noteOp1, rr.OP2 as noteOp2 "
				+ " , ct1.description2 as state, caa.APPROVED_TENOR as approvePeriod, to_char(caa.APPROVED_DATE, 'dd/MM/yyyy') as approveDate, car.CREDIT_DOC_UPLOADER as uploadVktdUser, car.ID "
				+ " from       CUST_PERSONAL_INFO cpi "
				+ " inner join CUST_IDENTITY ci           on cpi.ID = ci.CUST_ID "
				+ " inner join CREDIT_APP_REQUEST car     on (ci.IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CODE_GROUP='CUST' and CATEGORY='IDTYP' and CODE_VALUE1='1') and ci.CUST_ID = car.CUST_ID) ";
		
			if ( StringUtils.isNullOrEmpty(contractSearch.getDsaTsaCode()) ) //3rd (thientu, toancau, ...)
				sql += " inner join CODE_TABLE ct5 on (car.SALE_CHANNEL = ct5.ID and ct5.CODE_VALUE1 = 'ThirdParty') ";
		
			// backup for optimize
//			sql += " left  join ( " + "    select * from ( "
//				+ "        select cat.credit_app_id,cat.from_user_code,ct.description1 "
//				+ "        from CREDIT_APP_TRAIL cat inner join "
//				+ "        (select credit_app_id,from_user_code,max(last_updated_date),max(ID) max_ID from CREDIT_APP_TRAIL "
//				+ " where to_user_code = 'Sale' " 
//				+ "        group by credit_app_id,from_user_code) cat1 on cat.ID = cat1.max_ID "
//				+ "        inner join CODE_TABLE ct on ct.ID = cat.REASON_ID where CATEGORY = 'RSRTDTL_CD' ) pivot ( "
//				+ "        min(description1) for from_user_code in ('DE1' as DE1, 'DE2' as DE2, 'DC' as DC, 'CA' as CA, 'OP1' as OP1, 'OP2' as OP2) "
//				+ "    )) rr on car.ID = rr.credit_app_id "
//				+ " inner join CREDIT_APP_BPM cab         on car.ID = cab.CREDIT_APP_ID "
//				+ " left  join PRODUCTS p                 on car.PRODUCT_ID = p.ID "
//				+ " left  join CREDIT_APP_APPRAISAL caa   on car.ID = caa.CREDIT_APP_ID "
//				+ " left  join CREDIT_APP_COMMODITIES cac on car.ID = cac.CREDIT_APP_ID "
//				+ " left  join CREDIT_APP_TRAIL cat       on car.ID = cat.CREDIT_APP_ID "
//				+ " left  join EMPLOYEES e                on car.SALE_ID = e.ID "
//				+ " left  join CODE_TABLE ct1             on car.STATUS = ct1.ID "
//				+ " left  join CODE_TABLE ct2             on car.HAS_INSURANCE = ct2.ID "
//				+ " left  join CODE_TABLE ct3             on car.DISBURSEMENT_METHOD = ct3.ID "
//				+ " left  join CODE_TABLE ct4             on cac.SIP_ID = ct4.ID "
//				+ condition
//				+ " order by car.ID desc ";
			
			sql += " left  join ( " + "    select * from ( "
					+ " select cat1.credit_app_id,cat1.from_user_code,ct.description1 from CREDIT_APP_TRAIL cat1 inner join "
					+ " (select cat.credit_app_id,max(cat.LAST_UPDATED_DATE) LAST_UPDATED_DATE "
					+ " from CREDIT_APP_REQUEST car "
					+ subSelect 
					+ " inner join  CREDIT_APP_TRAIL cat on car.ID = cat.credit_app_id "
					+ condition
					+ " and cat.TO_USER_CODE = 'Sale' "
					+ " group by cat.credit_app_id, cat.from_user_code) cat2 on cat1.credit_app_id = cat2.credit_app_id and cat1.LAST_UPDATED_DATE = cat2.LAST_UPDATED_DATE "
					+ " inner join CODE_TABLE ct on ct.ID = cat1.REASON_ID) cat3  "
					+ " pivot (min(description1) for from_user_code in ('DE1' as DE1, 'DE2' as DE2, 'DC' as DC, 'CA' as CA, 'OP1' as OP1, 'OP2' as OP2) "
					+ "    )) rr on car.ID = rr.credit_app_id ";
			
			sql +=  " inner join CREDIT_APP_BPM cab         on car.ID = cab.CREDIT_APP_ID "
					+ " left  join PRODUCTS p                 on car.PRODUCT_ID = p.ID "
					+ " left  join CREDIT_APP_APPRAISAL caa   on car.ID = caa.CREDIT_APP_ID "
					+ " left  join CREDIT_APP_COMMODITIES cac on car.ID = cac.CREDIT_APP_ID "
					+ " left  join CREDIT_APP_TRAIL cat       on car.ID = cat.CREDIT_APP_ID "
					+ " left  join EMPLOYEES e                on car.SALE_ID = e.ID "
					+ " left  join CODE_TABLE ct1             on car.STATUS = ct1.ID "
					+ " left  join CODE_TABLE ct2             on car.HAS_INSURANCE = ct2.ID "
					+ " left  join CODE_TABLE ct3             on car.DISBURSEMENT_METHOD = ct3.ID "
					+ " left  join CODE_TABLE ct4             on cac.SIP_ID = ct4.ID "
					+ condition
					+ " order by car.ID desc ";

			
		System.out.println("----- findContractInfo: " + sql);
		List lst = this.session.createNativeQuery(sql).list();

		if (lst != null && lst.size() > 0)
			results = transformList(lst, DocumentStateInfo.class);

		return results;
	}
	
	public ContractCheck findContractDuplicate(String identityNumber, String phoneNumber) {
		
		Long soNgayQuaHan = 0L;
		
		//---------------------------------------------------------
		String sql =  "  select sum(nvl(ktn.Y_SONGAY_QH, 0)) as soNgayQuaHan " +
				       " from CREDIT_APP_REQUEST car " +
					   (!"".equals(identityNumber)?" inner join CUST_IDENTITY ci on car.CUST_ID = ci.CUST_ID ":"") +
					   " inner join UPL_LOAN_PYMNT_NEXT ktn on car.MC_CONTRACT_NUMBER = ktn.Y_CONTRACT_REF " +
					   (!"".equals(phoneNumber)?" inner join CUST_CONTACT_INFO cci on (car.CUST_ID = cci.CUST_ID and cci.CONTACT_PRIORITY = 1 and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CODE_GROUP='CONT' and CATEGORY='CONTAC_TYP' and CODE_VALUE1='MOBILE')) ":"") +
					   " where 1 = 1 " +
					   (!"".equals(identityNumber)?" and ci.IDENTITY_NUMBER = :identityNumber ":"") +
					   (!"".equals(phoneNumber)?" and cci.CONTACT_VALUE = :phoneNumber ":"");
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sql);
		
		if( !"".equals(identityNumber) )
			query.setParameter("identityNumber", identityNumber.trim());
		if( !"".equals(phoneNumber) )
			query.setParameter("phoneNumber", phoneNumber.trim());
		
		Object item = query.uniqueResult();
		
		if( item!=null )
			soNgayQuaHan = ((BigDecimal) item).longValue();
		
		//---------------------------------------------------------
		/*
			â€¢	KhÃ¡ch hÃ ng cÃ³ há»“ sÆ¡ Ä‘ang xá»­ lÃ½ trÃªn há»‡ thá»‘ng BPM cá»§a Mcredit.

			â€¢	KhÃ¡ch hÃ ng cÃ³ há»“ sÆ¡ bá»‹ tá»« chá»‘i khoáº£n vay trÃªn há»‡ thá»‘ng BPM trong vÃ²ng 3 thÃ¡ng (tÃ­nh tá»« thá»�i Ä‘iá»ƒm Tá»« chá»‘i Ä‘áº¿n thá»�i Ä‘iá»ƒm user check Duplicate)
			
			â€¢	KhÃ¡ch hÃ ng Ä‘Ã£ Ä‘Æ°á»£c duyá»‡t khoáº£n vay nhÆ°ng chÆ°a tráº£ Ä‘á»§ 4 ká»³ (TÃ­nh sá»‘ ká»³ khÃ¡ch hÃ ng Ä‘Ã£ thá»±c tráº£ tÃ­nh Ä‘áº¿n thá»�i Ä‘iá»ƒm user check Duplicate, dá»±a trÃªn Sao kÃª thu há»“i ná»£)
				Count tráº£ Ä‘á»§ 4 ká»³ Ä‘áº§u tiÃªn, check sá»‘ ká»³ tráº£ ná»£ thá»±c táº¿
				
			â€¢	KH táº¡i thá»�i Ä‘iá»ƒm check dup cÃ³ tá»•ng dÆ° ná»£ táº¡i Mcredit lá»›n hÆ¡n hoáº·c báº±ng 100 triá»‡u VNÄ�
		*/
		sql =  "  select car.CUST_ID " +
		        "   from CREDIT_APP_REQUEST car " +
		        (!"".equals(identityNumber)?" inner join CUST_IDENTITY ci on car.CUST_ID = ci.CUST_ID ":"") +
		        (!"".equals(phoneNumber)?" inner join CUST_CONTACT_INFO cci on (car.CUST_ID = cci.CUST_ID and cci.CONTACT_PRIORITY = 1 and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CODE_GROUP='CONT' and CATEGORY='CONTAC_TYP' and CODE_VALUE1='MOBILE')) ":"") +
		        "   left  join CODE_TABLE          ct  on (car.STATUS = ct.ID) " +
		        "   left  join UPL_LOAN_PYMNT_NEXT ktn on car.MC_CONTRACT_NUMBER = ktn.Y_CONTRACT_REF " +
		        " where ( " +
		        " ( " +
		        "    ct.CODE_VALUE1 <> 'DONE' and ct.CODE_VALUE1 not like '%_REJECT' and ct.CODE_VALUE1 not like '%_ABORT' and ct.CODE_VALUE1 <> 'ABORT' " +
		        " ) " +
		        " or ( " +
		        "    ct.CODE_VALUE1 like '%_REJECT' and add_months(trunc(sysdate), -3) < car.COMPLETED_DATE " +
		        " ) " +
		        " or ( " +
		        "    ct.CODE_VALUE1 = 'DONE' and (car.COMPLETED_DATE + 15) >= trunc(sysdate) " +
		        " ) " +
		        " or " +
		        " 	ktn.Y_KYTRANO_TT < :kyTraNo " +
		        " ) " +
		        (!"".equals(identityNumber)?" and ci.IDENTITY_NUMBER = :identityNumber ":"") +
		        (!"".equals(phoneNumber)?" and cci.CONTACT_VALUE = :phoneNumber ":"");
		
		query = this.session.createNativeQuery(sql)
				.setParameter("kyTraNo", Commons.KY_TRA_NO.intValue());
		
		if( !"".equals(phoneNumber) )
			query.setParameter("phoneNumber", phoneNumber.trim());
		if( !"".equals(identityNumber) )
			query.setParameter("identityNumber", identityNumber.trim());
		
		List<?> lst = query.list();
		
		if( lst!=null && lst.size()>0 ) //Duplicated, set custId contains value
			return new ContractCheck(new Long("1"), soNgayQuaHan);
		
		return new ContractCheck(0L, soNgayQuaHan);
	}
	
	public Long findTotalDebtPaymentNext(String identityNumber, String phoneNumber) {
		
		String sql =  " select sum(ktn.Y_DUNO_CL) as tongDuNo " + 
					   " from UPL_LOAN_PYMNT_NEXT ktn " +
					   " inner join CREDIT_APP_REQUEST car on ktn.Y_CONTRACT_REF = car.MC_CONTRACT_NUMBER " +
					   (!"".equals(phoneNumber)?" inner join CUST_CONTACT_INFO cci on (car.CUST_ID = cci.CUST_ID and cci.CONTACT_PRIORITY = 1 and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CODE_GROUP='CONT' and CATEGORY='CONTAC_TYP' and CODE_VALUE1='MOBILE')) ":"") +
					   (!"".equals(identityNumber)?" inner join CUST_IDENTITY  ci  on car.CUST_ID        = ci.CUST_ID ":"") +
					   " where 1 = 1 " +
					   (!"".equals(identityNumber)?" and ci.IDENTITY_NUMBER = :identityNumber ":"") +
					   (!"".equals(phoneNumber)?" and cci.CONTACT_VALUE = :phoneNumber ":"") +
					   " group by " +
					   (!"".equals(identityNumber)?" ci.IDENTITY_NUMBER ":" cci.CONTACT_VALUE ");
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sql);
		
		if( !"".equals(phoneNumber) )
			query.setParameter("phoneNumber", phoneNumber.trim());
		if( !"".equals(identityNumber) )
			query.setParameter("identityNumber", identityNumber.trim());
		
		Object item = query.uniqueResult();
		
		if( item!=null )
			return ((BigDecimal) item).longValue();
		
		return 0L;
	}
	
	public Long findTotalDebtForBPM(String identityNumber, String oldIdentityNumber, String militaryNumber) {
		
		//String condition = "";
		
		List<String> lstParam = new ArrayList<String>();
		
		if( !"".equals(identityNumber) && !"".equals(oldIdentityNumber) && !"".equals(militaryNumber) ) {
			//condition = " ('"+identityNumber+"', '"+oldIdentityNumber+"', '"+militaryNumber+"') ";
			lstParam.add(identityNumber);
			lstParam.add(oldIdentityNumber);
			lstParam.add(militaryNumber);
		}
		
		else if( !"".equals(identityNumber) && !"".equals(oldIdentityNumber) ) {
			//condition = " ('"+identityNumber+"', '"+oldIdentityNumber+"') ";
			lstParam.add(identityNumber);
			lstParam.add(oldIdentityNumber);
		}
		
		else if( !"".equals(identityNumber) && !"".equals(militaryNumber) ) {
			//condition = " ('"+identityNumber+"', '"+militaryNumber+"') ";
			lstParam.add(identityNumber);
			lstParam.add(militaryNumber);
		}
		
		else if( !"".equals(oldIdentityNumber) && !"".equals(militaryNumber) ) {
			//condition = " ('"+oldIdentityNumber+"', '"+militaryNumber+"') ";
			lstParam.add(oldIdentityNumber);
			lstParam.add(militaryNumber);
		}
		
		else if( !"".equals(identityNumber) )
			//condition = " ('"+identityNumber+"') ";
			lstParam.add(identityNumber);
		
		else if( !"".equals(oldIdentityNumber) )
			//condition = " ('"+oldIdentityNumber+"') ";
			lstParam.add(oldIdentityNumber);
		
		else // militaryNumber
			//condition = " ('"+militaryNumber+"') ";
			lstParam.add(militaryNumber);
		
//		String sql =  " select sum(ktn.Y_DUNO_CL) as tongDuNo " + 
//					   " from UPL_LOAN_PYMNT_NEXT ktn " +
//					   " inner join CREDIT_APP_REQUEST car on ktn.Y_CONTRACT_REF = car.MC_CONTRACT_NUMBER " +
//					   " inner join CUST_IDENTITY ci on car.CUST_ID = ci.CUST_ID " +
//					   " where ci.IDENTITY_NUMBER in (:lstParam) " +
//					   " group by ci.IDENTITY_NUMBER ";
		String sql = "select sum(ktn.Y_DUNO_CL) as tongDuNo" + 
				" from UPL_LOAN_PYMNT_NEXT ktn" + 
				" where ktn.y_contract_ref IN (select distinct car.mc_contract_number from credit_app_request car" + 
				" inner join cust_identity ci on ci.cust_id = car.cust_id" + 
				" where ci.identity_number IN (:lstParam))";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sql);
		
		query.setParameterList("lstParam", lstParam);
		
		@SuppressWarnings("unchecked")
		List<BigDecimal> lst = query.list();
		
		if( lst!=null && !lst.isEmpty()) {
			if( lst.size()==1) {
				if(lst.get(0) != null) {
					return lst.get(0).longValue();
				} else {
					return 0L;
				}
			}
			else {
				Long result = 0L;
				for( BigDecimal obj : lst ) {
					result += obj.longValue();
				}
				return result;
			}
		}
		
		return 0L;
	}

	public void updateCoreCustCode(CustomerPersonalInfo item) throws DataException {

		CriteriaBuilder builder = this.session.getCriteriaBuilder();
		CriteriaUpdate<CustomerPersonalInfo> criteria = builder.createCriteriaUpdate(CustomerPersonalInfo.class);
		Root<CustomerPersonalInfo> root = criteria.from(CustomerPersonalInfo.class);
		criteria.set(root.get("coreCustCode"), item.getCoreCustCode());
		criteria.where(builder.equal(root.get("id"), item.getId()));
		this.session.createQuery(criteria).executeUpdate();
	}

	public Long findCustomerByMcCustCode(String mcCustCode) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findCustomerByMcCustCode").setParameter("mcCustCode", mcCustCode)
				.list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public Long findValueByField(String nameQuery, String field, Object value) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery(nameQuery).setParameter(field, value).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public Long findIdentityIdBy(Long custId, Integer identityTypeId) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findIdentityIdBy").setParameter("custId", custId)
				.setParameter("identityTypeId", identityTypeId).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public Long findCustIdByRelationIdMessageLog(String relationId) {
		Long result = null;

		List<?> lst = this.session.getNamedQuery("findCustIdByRelationIdMessageLog")
				.setParameter("relationId", relationId).list();

		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public Long findAppIdByRelationIdMessageLog(String relationId) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findAppIdByRelationIdMessageLog")
				.setParameter("relationId", relationId).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}

	public CustomerPersonalInfo findCustPersonalInfoByRelationIdMessageLog(String relationId) {
		CustomerPersonalInfo result = null;
		List<?> lst = this.session.getNamedQuery("findCustomerByRelationIdMessageLog")
				.setParameter("relationId", relationId).list();
		if (lst != null && lst.size() > 0)
			result = (CustomerPersonalInfo) lst.get(0);
		return result;
	}

	public List<?> findCustomerByMcCustCode2(String mcCustCode) {
		return this.session.getNamedQuery("findCustomerByMcCustCode").setParameter("mcCustCode", mcCustCode).list();
	}

	public CustomerPersonalInfo findCustomerPersonalInfoById(Long id) {
		CustomerPersonalInfo customer = (CustomerPersonalInfo) this.session.find(CustomerPersonalInfo.class, id);
		return customer;
	}

	public Long findCustomerByCoreCustCode(String coreCustCode) {
		Long result = null;
		List<?> lst = this.session.getNamedQuery("findCustomerByCoreCustCode")
				.setParameter("coreCustCode", coreCustCode).list();
		if (lst != null && lst.size() > 0)
			result = (Long) lst.get(0);
		return result;
	}
	
	/**
	 * author: truongvd
	 * @param identityNumber
	 * @param birthDay
	 * @return
	 */
	public Long getCusIdByIdentityAndBirthDay(String identityNumber,Date birthDay) {
		long result = 0;
		List<?> lst = this.session.getNamedQuery("getCusIdByIdentityAndBirthDay").setParameter("identityNumber", identityNumber).setParameter("birthDay", birthDay).list();
		if (lst != null && lst.size() > 0)
			result = ((BigDecimal) lst.get(0)).longValue();
		return result;		
	}

}
