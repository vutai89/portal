package com.mcredit.data.mobile.reporsitorytest;

import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.object.mobile.dto.TestListCaseNoteDTO;

public class UplCreditAppRequestRepositoryTest extends BaseRepository implements IRepository<UplCreditAppRequest>{
	
	public UplCreditAppRequestRepositoryTest(Session session) {
		super(session);
	}
	
	public int remove(HashSet<Long> lstRemoveId) {
		return this.session.getNamedNativeQuery("removeUplCreditAppRequestId").setParameterList("ids", lstRemoveId.toArray(new Long[0])).executeUpdate();
	}

	public UplCreditAppRequest get(Long id) {
		return (UplCreditAppRequest) this.session.getNamedNativeQuery("getUplCreditAppRequestId").setParameter("id", id).addEntity(UplCreditAppRequest.class).uniqueResult();
	}
	
	public void remove(UplCreditAppRequest item) {
		this.session.delete(item);
	}
	
	public UplCreditAppRequest getById(Long id) {
		return this.session.find(UplCreditAppRequest.class, id);
	}
	
	public String getRandomAppNumber() {
		return this.session.getNamedNativeQuery("getRandomAppNumber").uniqueResult().toString();
	}
	
	public UplCreditAppRequest getByAppNumber(String appNumber) {
		return (UplCreditAppRequest) this.session.getNamedQuery("getRequestByAppNumber").setParameter("appNumber", Long.parseLong(appNumber)).uniqueResult();
	}
	
	public TestListCaseNoteDTO getRandomAppNumberSale() {
		Object res = this.session.getNamedNativeQuery("testGetCaseNote").uniqueResult();
		if (res!= null) {
			TestListCaseNoteDTO dto = transformObject(res, TestListCaseNoteDTO.class);
			return dto;
		}
		return null;
	}
	
	
}
