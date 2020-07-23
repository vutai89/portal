package com.mcredit.data.warehouse.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.object.warehouse.ResultsDocumentInfo;
import com.mcredit.model.object.warehouse.WhDocumentChangeDTO;

public class WhDocumentChangeRepository extends BaseRepository implements IUpdateRepository<WhDocumentChange>,IUpsertRepository<WhDocumentChange>,IAddRepository<WhDocumentChange> {

	public WhDocumentChangeRepository(Session session) {
		super(session);
	}

	public void add(WhDocumentChange item) {
		this.session.save(item);
                this.session.flush();
                this.session.clear();
	}

	public void update(WhDocumentChange item) {
		this.session.update(item);
                this.session.flush();
                this.session.clear();
	}

	public void upsert(WhDocumentChange item) {
		this.session.saveOrUpdate("WhDocumentChange", item);
                this.session.flush();
                this.session.clear();
	}

	public List<WhDocumentChangeDTO> getListWareHouseErrorCase(Long whId) {
		List<?> lst = session.getNamedNativeQuery("getErrorByWHId").setParameter("whDocId", whId).list();
		List<WhDocumentChangeDTO> retList = new ArrayList<WhDocumentChangeDTO>();
		if (lst != null && !lst.isEmpty()) {
			for (Object o : lst) {
				retList.add(transformObject(o, WhDocumentChangeDTO.class));
			}
		}
       	
		return retList;
	}
	
	public List<WhDocumentChangeDTO> getListWareHouseErrorListCase( List<Long> whIdList) {
		List<?> lst = session.getNamedNativeQuery("getListWareHouseErrorListCaseByWHId").setParameter("whDocId", whIdList).list();
		List<WhDocumentChangeDTO> retList = new ArrayList<WhDocumentChangeDTO>();
		if (lst != null && !lst.isEmpty()) {
			for (Object o : lst) {
				retList.add(transformObject(o, WhDocumentChangeDTO.class));
			}
		}
       	
		return retList;
	}

	public List<WhDocumentChangeDTO> getListCavetError(Long whId) {
		List<?> lst = session.getNamedNativeQuery("getListWareHouseCavetErrorByWHId").setParameter("whDocId", whId).list();
		List<WhDocumentChangeDTO> retList = new ArrayList<WhDocumentChangeDTO>();
		 
		if (lst != null && !lst.isEmpty()) {
			for (Object o : lst) {
				retList.add(transformObject(o, WhDocumentChangeDTO.class));
			}
		}
		return retList;
	}
	
	public List<WhDocumentChangeDTO> getListError(String creditAppId) {
		List<?> lst = session.getNamedNativeQuery("getListError").setParameter("creditAppId", creditAppId).list();
		List<WhDocumentChangeDTO> retList = new ArrayList<WhDocumentChangeDTO>();
		 
		if (lst != null && !lst.isEmpty()) {
			for (Object o : lst) {
				retList.add(transformObject(o, WhDocumentChangeDTO.class));
			}
		}
		return retList;
	}
	
	public int deleteErrById(Long whDocumentId, Long statusType) {
		
		@SuppressWarnings("rawtypes")
		Query query = this.session.createNativeQuery("DELETE FROM WH_DOCUMENT_CHANGE WHERE WH_DOC_ID = :whDocumentId and TYPE = :statusType");
		query.setParameter("whDocumentId", whDocumentId).setParameter("statusType", statusType);
		return query.executeUpdate();

	}
	
	public List<ResultsDocumentInfo> getResultsDocuments(Long whId) {
		List<ResultsDocumentInfo> result = new ArrayList<>();
		List<?> lst = session.getNamedNativeQuery("getLstResultsDocumentInfo").setParameter("whDocId", whId).list();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, ResultsDocumentInfo.class));
			}
		}

		return result;
	}
	
	public Long getLatestStatus(Long whId, Long type) {
		
		@SuppressWarnings("unchecked")
		List<Long> query = this.session.createNativeQuery(" select approve_status from wh_borrowed_return_document "
                        + " where wh_doc_id = :WH_DOC_ID and record_status ='A'and type = :TYPE and ROWNUM = 1 ")
				.setParameter("WH_DOC_ID", whId).setParameter("TYPE", type).addScalar("approve_status", LongType.INSTANCE).getResultList();

		return !query.isEmpty() ? query.get(0) : null;
	}

	public Date getBorrowDate(Long whId , String changeStatusCode ,String typeCode) {
		List<?> wdc = this.session.getNamedNativeQuery("getBorrowDate").setParameter("whId", whId).setParameter("changeStatusCode", changeStatusCode).setParameter("typeCode", typeCode).getResultList();
		return transformObject(wdc.get(0), WhDocumentChange.class).getCreatedDate();
	}
	
	public WhDocumentChange getWhDocumentChangeByWhIdAndType(Long whId, Long type){
		List<WhDocumentChange> result = new ArrayList<>();
		List<?> lst = this.session.createNativeQuery("select * from wh_document_change  where wh_doc_id = :WH_DOC_ID and type = :TYPE ORDER BY ID DESC ")
				.setParameter("WH_DOC_ID", whId).setParameter("TYPE", type).getResultList();
		
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, WhDocumentChange.class));
			}
		}
		
		return !result.isEmpty() ? result.get(0) : new WhDocumentChange();
		
	}

	public HashMap<String, Date> getLstUpdateErrorExpectedReceiptByCreditAppId(List<Long> creditAppIdList) {
		
		
		return null;
	}
        
    public String countExportPaperReceipt(Long creditAppId) {
        return (String) this.session.getNamedNativeQuery("countExportPaperReceipt").setParameter("creditAppId", creditAppId).getSingleResult();
    }    
   
}
