package com.mcredit.data.warehouse.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.warehouse.WhBorrowedDocumentDTO;
import com.mcredit.model.object.warehouse.RenewalDocumentDTO;
import com.mcredit.model.object.warehouse.ResultsDocumentInfo;
import com.mcredit.model.object.warehouse.ReturnDocumentInfo;
import com.mcredit.util.StringUtils;

public class WareHouseDocumentBorrowRepository extends BaseRepository implements IRepository<WhBorrowedDocument> {

	public WareHouseDocumentBorrowRepository(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	public void upsert(WhBorrowedDocument item) {
		this.session.saveOrUpdate("WhBorrowedDocument", item);
	}

	public WhBorrowedDocument getBorrowDocument(RenewalDocumentDTO document, Long type) {
		Object obj = this.session.getNamedNativeQuery("getWhBorrowedDocumentById")
				.setParameter("whDocId", document.getWhDocId()).setParameter("type", type).uniqueResult();
		if(obj==null){
			return new WhBorrowedDocument();
		}
		WhBorrowedDocument docBorrow = transformObject(obj, WhBorrowedDocument.class);
		return docBorrow;
	}

	// gia han them ngay muon
	public void renewal(Date date, Long idDocument, Long appointStatus, Long typeBorrowed) {
		NativeQuery<?> query = session.createNativeQuery(" update WH_BORROWED_RETURN_DOCUMENT d set d.APPOINTMENT_DATE=:dateNew, d.APPROVE_STATUS=:appointStatus,"
                        + " d.BORROWED_DATE=:borrowedDate, d.TYPE=:typeBorrowed where d.WH_DOC_ID = :documentID and d.RECORD_STATUS ='A' ");
		query.setParameter("dateNew", date);
		query.setParameter("appointStatus", appointStatus);
		query.setParameter("documentID", idDocument);
		query.setParameter("borrowedDate", new Date());
		query.setParameter("typeBorrowed", typeBorrowed);
		query.executeUpdate();
	}

	// lay ve ngay appoint date
	public String getAppointDate(Long idDocument) {

		NativeQuery<?> query = session
				.createNativeQuery("select CREATED_DATE from WH_BORROWED_RETURN_DOCUMENT where WH_DOC_ID =(:docId)");
		query.setParameter("docId", idDocument);

		Object obj = query.getSingleResult();
		return obj != null ? obj.toString() : StringUtils.Empty;
	}

	public void add(WhBorrowedDocument whbDocument) {
		session.save(whbDocument);
	}

//	public WhBorrowedDocument getWhDocument(WhBorrowedDocument whbDocument) {
//		Object obj = this.session.getNamedQuery("getWhBorrowedDocumentById")
//				.setParameter("whDocId", whbDocument.getWhDocId()).uniqueResult();
//		if(obj==null){
//			return null;
//		}
//		WhBorrowedDocument docBorrow = transformObject(obj, WhBorrowedDocument.class);
//		return docBorrow;
//
//	}
	
	public WhBorrowedDocument getLastestBorrowDocument(Long idDocument, Long type) {
		@SuppressWarnings("unchecked")
		List<WhBorrowedDocument> result = new ArrayList<>();
		List<?> listItem = this.session.getNamedQuery("getWhBorrowedDocumentById")
				.setParameter("whDocId", idDocument).setParameter("type", type).getResultList();
		 if (listItem != null && !listItem.isEmpty()) {
	            for (Object obj : listItem) {
	                result.add(transformObject(obj, WhBorrowedDocument.class));
	            }
	        }
		return !result.isEmpty() ? result.get(result.size() - 1) : null;

	}
        
        
	public void updateReturnDate(Long idDocument, Long approveStatus) {
		NativeQuery<?> query = session.createNativeQuery(" update WH_BORROWED_RETURN_DOCUMENT a set a.RETURN_DATE=:returnDate, a.APPROVE_STATUS=:approveStatus where a.WH_DOC_ID = :documentID and a.RECORD_STATUS ='A' ");
		query.setParameter("returnDate", new Date());
		query.setParameter("documentID", idDocument);
		query.setParameter("approveStatus", approveStatus);
		query.executeUpdate();
	}

}
