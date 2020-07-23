package com.mcredit.data.credit.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationRequest;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.util.PartitionListHelper;
import com.mcredit.util.StringUtils;
import com.mcredit.model.object.cancelCaseBPM.CancelCaseInfoDTO;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.PartitionListHelper;

public class CreditApplicationRequestRepository extends BaseRepository
		implements IUpsertRepository<CreditApplicationRequest> {

	public CreditApplicationRequestRepository(Session session) {
		super(session);
	}

	public void upsert(CreditApplicationRequest item) {
		this.session.saveOrUpdate("CreditApplicationRequest", item);
	}

	public void remove(CreditApplicationRequest item) {
		this.session.delete("CreditApplicationRequest", item);
	}
	
	public CreditApplicationRequest get(Long id) {
		if (id == null) return null;
		@SuppressWarnings("rawtypes")
		Query query = this.session.createQuery("from "+CreditApplicationRequest.class.getName()+ " where id =:id");
		query.setParameter("id", id);
		CreditApplicationRequest result = (CreditApplicationRequest) query.uniqueResult();
		return result;
	}
	
	public Long findNextSeq() {
		@SuppressWarnings("rawtypes")
		Query query = this.session.getNamedNativeQuery("CreditAppRequest.nextSeq");
		BigDecimal result = (BigDecimal) query.uniqueResult();
		if(result == null)
			return null;
		return result.longValue();
	}
	
	public CreditApplicationRequest getAppRequestBy(String mcContractNumber) {
		if (mcContractNumber == null) return null;
		CreditApplicationRequest item = null;
		List<?> lst = this.session.createQuery("from CreditApplicationRequest where mcContractNumber = :mcContractNumber", CreditApplicationRequest.class)
								.setParameter("mcContractNumber", mcContractNumber!=null?mcContractNumber.trim():"")
								.list();
		if( lst!=null && lst.size()>0)
			item = (CreditApplicationRequest) lst.get(0);
		return item;
	}
	
	public Long checkContractNumberDebt(String contractNumber) {
		Object result =  this.session.getNamedNativeQuery("checkExistContractNumberForDebtColection").setParameter("contractNumber", contractNumber).uniqueResult();
		return result != null && StringUtils.isNumberic(result.toString())  ? Long.valueOf(result.toString())  :null ;
	}

	public List<InputUploadECM> getListInforECM(List<String> listAppId) {
		if (null == listAppId || listAppId.isEmpty())
			return null;

		List<List<String>> tmp = new ArrayList<List<String>>(PartitionListHelper.partition(listAppId, 1000));
		String stQerry = " SELECT null as documentCode , null as documentSource, cab.BPM_APP_NUMBER as appNumber , cab.BPM_APP_ID as appId, null as  productCode, ci.IDENTITY_NUMBER  as citizenID ,"
				+ "null as ldNumber , cpi.CUST_NAME  as custName , null as serverFileName ,null as userFileName FROM CREDIT_APP_BPM cab"
				+ "    INNER JOIN CREDIT_APP_REQUEST car ON car.ID = cab.CREDIT_APP_ID"
				+ "    INNER JOIN CUST_PERSONAL_INFO cpi  ON cpi.ID = car.CUST_ID"
				+ "    INNER JOIN CUST_IDENTITY ci ON ci.ID = cpi.IDENTITY_ID "
				+ "    INNER JOIN CODE_TABLE ctIdeType ON  ctIdeType.ID = ci.IDENTITY_TYPE_ID AND ctIdeType.CATEGORY = 'IDTYP' AND ctIdeType.CODE_GROUP =  'CUST' WHERE ";

		for (int i = 0; i < tmp.size(); i++) {
			if (i == 0) {
				stQerry = stQerry + " cab.BPM_APP_ID IN (:praram" + i + ")";
			} else {
				stQerry = stQerry + " OR cab.BPM_APP_ID IN (:praram" + i + ")";
			}
		}
		
		@SuppressWarnings("rawtypes")
		Query query = this.session.createNativeQuery(stQerry).setHibernateFlushMode(FlushMode.ALWAYS);

		for (int i = 0; i < tmp.size(); i++) {
			query.setParameterList("praram" +i, tmp.get(i));
		}

		List<?> lst = query.list();
		
		List<InputUploadECM> out = new ArrayList<>();
		for (Object o : lst) {
			out.add(transformObject(o, InputUploadECM.class));
		}
		return out;
	}

	public List<CancelCaseInfoDTO> findInforByCaseId(List<String> sucsessAppIdLst) {
		
		if (null == sucsessAppIdLst || sucsessAppIdLst.isEmpty())
			return null;

		List<List<String>> tmp = new ArrayList<List<String>>(PartitionListHelper.partition(sucsessAppIdLst, 1000));
		String stQerry = " SELECT cab.BPM_APP_ID  as appId, emp.FULL_NAME as saleName , CONCAT(u.LOGIN_ID,'@mcredit.com.vn') as saleEmail ,cpi.CUST_NAME as custName "
				+ "FROM CREDIT_APP_BPM  cab "
				+ "INNER JOIN CREDIT_APP_REQUEST car ON cab.CREDIT_APP_ID = car.ID "
				+ "INNER JOIN CUST_PERSONAL_INFO cpi ON car.CUST_ID = cpi.ID "
				+ "INNER JOIN EMPLOYEES emp ON emp.ID = car.SALE_ID AND emp.STATUS = 'A'"
				+ "INNER JOIN USERS u ON u.EMP_ID = emp.ID AND u.STATUS = 'A' WHERE ";

		for (int i = 0; i < tmp.size(); i++) {
			if (i == 0) {
				stQerry = stQerry + " cab.BPM_APP_ID IN (:praram" + i + ")";
			} else {
				stQerry = stQerry + " OR cab.BPM_APP_ID IN (:praram" + i + ")";
			}
		}
		
		@SuppressWarnings("rawtypes")
		Query query = this.session.createNativeQuery(stQerry).setHibernateFlushMode(FlushMode.ALWAYS);

		for (int i = 0; i < tmp.size(); i++) {
			query.setParameter("praram" +i,tmp.get(i));
		}

		List<?> lst = query.list();
		
		List<CancelCaseInfoDTO> out = new ArrayList<>();
		for (Object o : lst) {
			out.add(transformObject(o, CancelCaseInfoDTO.class));
		}
		return out;
	}

}
