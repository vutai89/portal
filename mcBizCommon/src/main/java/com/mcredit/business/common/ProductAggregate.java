package com.mcredit.business.common;

import java.io.Closeable;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.Product;

public class ProductAggregate implements Closeable{
	private UnitOfWorkCommon uokCommon = null;
	private Product product;
	
	public UnitOfWorkCommon getUokCommom() {
		return uokCommon;
	}
	public void setUokCommom(UnitOfWorkCommon uokCommon) {
		this.uokCommon = uokCommon;
	}
	public ProductAggregate(UnitOfWorkCommon uok, String productCode) {
		this.uokCommon = uok;
		this.product = uokCommon.productTaskRepo().getProductBy(productCode);
	}

	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public void close() {
		if (uokCommon != null)
			uokCommon = null;
	}

}
