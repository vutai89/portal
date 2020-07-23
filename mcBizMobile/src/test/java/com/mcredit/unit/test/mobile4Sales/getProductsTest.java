package com.mcredit.unit.test.mobile4Sales;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.model.dto.ProductDTO;

import junit.framework.TestCase;

public class getProductsTest extends TestCase {
	
	MobileManager mobileManager = null;
	
	@BeforeEach
	public void init() {
		mobileManager = new MobileManager();
	}
	
	
	@Test
	@DisplayName("Should get list product")
	public void shouldGetListProduct() throws Exception {
		List<ProductDTO> products = mobileManager.getProducts();
		assertTrue(products.size() > 0);
	}
	
	@Test
	@DisplayName("Test first product should has value")
	public void productShouldHasValue() throws Exception {
		ProductDTO product = mobileManager.getProducts().get(0);
		
		assertNotNull(product.getProductCode());
		
		// FIXME: Mock data test
		// assertTrue(product.getMinTenor().compareTo(zero) > 0);
		// assertTrue(product.getMinLoanAmount().compareTo(zero) > 0);
		// assertTrue(product.getMaxLoanAmount().compareTo(zero) > 0);
		// assertTrue(product.getProductCode().length() > 0);
		// assertTrue(product.getProductName().length() > 0);
	}
	
	@AfterEach
	public void cleanup() {
		mobileManager = null;
	}
}
