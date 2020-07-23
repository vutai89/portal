package com.mcredit.data.mobile.reporsitorytest;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.UplCreAppRequestStatus;

public class ProductRepositoryTest extends BaseRepository implements IRepository<Product> {
	public ProductRepositoryTest(Session session) {
		super(session);
	}
	
	public Long add(Product pd) {
		return (Long) this.session.save("Product", pd);
	}

	public int remove(List<Integer> lstRemoveId) {
		return this.session.getNamedNativeQuery("removeProducts").setParameterList("ids", lstRemoveId.toArray(new Integer[0])).executeUpdate();
	}

	public int add(int id, int productId, String productCode, String ccy) {
		return this.session.getNamedNativeQuery("insertData")
				.setParameter("id", id)
				.setParameter("productGroupId",productId)
				.setParameter("productCode", productCode)
				.setParameter("ccy", ccy)
				.executeUpdate();
	}

	public String getShopCode() {
		return (String) this.session.getNamedNativeQuery("getShopCode").uniqueResult();
	}

	public String getCompanyTaxNumber() {
		return (String) this.session.getNamedNativeQuery("getTaxNumber").uniqueResult();
	}
}
