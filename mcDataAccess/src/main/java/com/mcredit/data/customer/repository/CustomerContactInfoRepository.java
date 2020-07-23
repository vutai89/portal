package com.mcredit.data.customer.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerContactInfo;
import com.mcredit.data.repository.IUpsertRepository;

public class CustomerContactInfoRepository extends BaseRepository implements
		IUpsertRepository<CustomerContactInfo> {

	public CustomerContactInfoRepository(Session session) {
		super(session);
	}

	public void upsert(CustomerContactInfo item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "CustomerContactInfo");
		else
			this.session.save("CustomerContactInfo", item);
	}
	
	public void remove(CustomerContactInfo item) throws DataException {
		this.session.delete("CustomerContactInfo", item);
	}
	
	public Integer findNextContactPriority(Long custId, Integer contactType, Integer contactCategory) {
		Integer result = null;
		List<?> lst = this.session.getNamedQuery("CustomerContactInfo.nextContactPriority")
				.setParameter("custId", custId)
				.setParameter("contactType", contactType)
				.setParameter("contactCategory", contactCategory)
				.list();
		if( lst!=null && lst.size()>0 )
			result = (Integer) lst.get(0);
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public List queryNativeCustomerByLink(String linkType) throws DataException {
		
		/*List cust = this.session.getNamedNativeQuery("findNativeCustomerByLinkType")
								.setParameter("lnk_type", linkType)
								.list();
		
		if(cust!=null && cust.size()>0) {
			for (Iterator iterator = cust.iterator(); iterator.hasNext();) {
				Object[] record = (Object[]) iterator.next();
				String coreCode = (String) record[0];
				String lnkType = (String) record[1];
				BigDecimal lnkSeq = (BigDecimal) record[2];
				String lnkValue = (String) record[3];
				String lnkName = (String) record[4];
				System.out.println("Core customer code = " + coreCode);
				System.out.println("Link type = " + lnkType);
				System.out.println("Link sequence = " + lnkSeq);
				System.out.println("Link value = " + lnkValue);
				System.out.println("Link name = " + lnkName);
			}
		}
		return cust;*/
		return this.session.getNamedNativeQuery("findNativeCustomerByLinkType")
				.setParameter("lnk_type", linkType)
				.list();
	}
}
