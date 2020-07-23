package com.mcredit.unit.test.mobile4Sales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.sharedbiz.cache.CompanyCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class CheckCategoryTest {
	MobileManager manager = null;
	
	@BeforeEach
	public void init() {
		manager = new MobileManager();
	}
	
	@BeforeAll
	static void setup() {
	}
	
	@AfterEach
	void tearDown() {
	}
	 
	@AfterAll
	static void done() {
	}
	
	public CategoryDTO initDataTest(){
		Random random = new Random();
		List<CategoryDTO> lst = CompanyCacheManager.getInstance().getLstCompany();
		int num = random.nextInt(lst.size());
		return lst.get(num);
	}
	
	@Test
	public void testCheckCategorySuccess() throws Exception {
		CategoryDTO test = initDataTest();
		CategoryDTO res = manager.checkCategory(test.getCompanyTaxNumber());
		assertNotNull(test.getCompanyTaxNumber(),res.getCompanyTaxNumber());
		assertNotNull(test.getCompName(),res.getCompName());
		assertNotNull(test.getCompAddrStreet(),res.getCompAddrStreet());
		assertNotNull(test.getOfficeNumber(),res.getOfficeNumber());
	}
	
	@Test
	public void testCheckCategoryNotExits() throws Exception{
		try {
			CategoryDTO test = initDataTest();
			String taxNumberTestFail = test.getCompanyTaxNumber().substring(0, 2);
			CategoryDTO res = manager.checkCategory(taxNumberTestFail);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.check.cat.false"), e.getMessage());
		}
	}
	
	@Test
	public void testCheckCategoryNullOrEmpty() throws Exception{
		try {
			CategoryDTO test = initDataTest();
			String taxNumberTestFail = "";
			CategoryDTO res = manager.checkCategory(taxNumberTestFail);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",Labels.getString("label.mfs.get.upload.taxNumber.required")), e.getMessage());
		}
	}
	
	@Test
	public void testCheckCategoryMaxValue() throws Exception{
		try {
			CategoryDTO test = initDataTest();
			String taxNumberTestFail = "123456789123456";
			CategoryDTO res = manager.checkCategory(taxNumberTestFail);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",Labels.getString("label.mfs.get.upload.taxNumber.required")), e.getMessage());
		}
	}

}
