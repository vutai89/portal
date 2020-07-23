package com.mcredit.data.credit.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.credit.entity.CreditApplicationSignature;
import com.mcredit.data.repository.IAddRepository;


public class CreditApplicationSignatureRepository extends BaseRepository implements IAddRepository<CreditApplicationSignature> {

	public CreditApplicationSignatureRepository(Session session) {
		super(session);
	}

	@Override
	public void add(CreditApplicationSignature item) {
		this.session.save("CreditApplicationSignature", item);
	}
	
	public Integer getLatestVersionBy(String contractNumber) {	
		Object item = this.session.getNamedNativeQuery("getLatestVersionCreditApplicationSignatureBy").setParameter("contract_number", contractNumber).uniqueResult();
		return item != null ? transformObject(item, CreditApplicationSignature.class).getVersion().intValue() : null;		
	}
	
	public CreditApplicationSignature getBy(String contractNumber,String version,String signature,String appId,String appNumber) {	
		Object item = this.session.getNamedNativeQuery("getCreditApplicationSignatureBy")
				.setParameter("contract_number", contractNumber)
				.setParameter("VERSION", version)
				.setParameter("SIGNATURE", signature)
				.uniqueResult();
		return item != null ? transformObject(item, CreditApplicationSignature.class) : null;		
	}
}
