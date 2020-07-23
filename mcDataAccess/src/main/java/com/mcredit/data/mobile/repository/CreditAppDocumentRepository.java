package com.mcredit.data.mobile.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppDocument;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.object.mobile.dto.PdfDTO;
import com.mcredit.model.object.mobile.dto.SeachFilterDto;

public class CreditAppDocumentRepository extends BaseRepository implements IRepository<UplCreditAppDocument>, IAddRepository<UplCreditAppDocument>, IUpsertRepository<UplCreditAppDocument> {
	
	public CreditAppDocumentRepository(Session session) {
		super(session);
	}
	
	@Override
	public void add(UplCreditAppDocument item) {
		this.session.save("UplCreditAppDocument", item);
        this.session.flush();
        this.session.clear();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PdfDTO> getPdfFilesByUplCreditAppId(Long uplCreditAppId) {
		List files = this.session.getNamedNativeQuery("getPdfFilesByUplCreditAppId").setParameter("id", uplCreditAppId).list();
		if (files != null) {
			return transformList(files, PdfDTO.class);
		}
		return null;
	}
	
	public PdfDTO getPdfFileById(Long id) {
		
		Object file = this.session.getNamedNativeQuery("getPdfFileById").setParameter("id", id).uniqueResult();
		if (file != null) {
			return transformObject(file, PdfDTO.class);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UplCreditAppDocument> getListCreditAppDocument(SeachFilterDto dto) {
		return this.session.getNamedQuery("checkListCreditDocument").setParameter("docId", dto.getDocumentId()).setParameter("id", dto.getId()).list();
	}
	
	public boolean isSaleOwnerFile(Long documentId, String saleCode) {
		
		Object result = this.session.getNamedNativeQuery("isSaleOwnerDocument").setParameter("id", documentId).setParameter("saleCode", saleCode).uniqueResult();
		if (result != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public void upsert(UplCreditAppDocument item) {
		// TODO Auto-generated method stub
		this.session.saveOrUpdate(item);
		
	}
	
}
