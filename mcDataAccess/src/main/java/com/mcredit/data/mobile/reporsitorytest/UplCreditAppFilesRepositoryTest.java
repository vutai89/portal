package com.mcredit.data.mobile.reporsitorytest;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.repository.IRepository;

public class UplCreditAppFilesRepositoryTest extends BaseRepository implements IRepository<UplCreditAppFiles>{
	
	public UplCreditAppFilesRepositoryTest(Session session) {
		super(session);
	}
	
	public int remove(HashSet<Long> lstRemoveId) {
		return this.session.getNamedNativeQuery("removeUplCreditAppFilesId").setParameterList("ids", lstRemoveId.toArray(new Long[0])).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getIdFileByUplCreditAppId(Long id) {
		return this.session.getNamedQuery("getFilesByUplCreditRequestId").setParameter("uplId", id).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getDocumentByUplCreditAppId(long id) {
		return this.session.getNamedQuery("getDocumentByUplCreditAppId").setParameter("id", id).getResultList();
	}
	
	public int removeDocument(HashSet<Long> lstRemoveId) {
		return this.session.getNamedNativeQuery("removeDocument").setParameterList("ids", lstRemoveId.toArray(new Long[0])).executeUpdate();
	}
}
