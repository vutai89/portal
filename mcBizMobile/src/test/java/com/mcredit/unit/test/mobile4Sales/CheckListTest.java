package com.mcredit.unit.test.mobile4Sales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.validation.ValidationException;

@DisplayName("Test CheckList: get list manadatory image for new case Id")
public class CheckListTest {
	
	MobileManager mobileManager = new MobileManager();
	
	@BeforeAll
	static void setup() {
	}
	 
	@BeforeEach
	void init() {
	}
	
	@AfterEach
	void tearDown() {
	}
	 
	@AfterAll
	static void done() {
	}
	
	@Test
	@DisplayName("Test CheckList success")
	public void testCheckListSuccess() throws Exception {
		String res = mobileManager.checklist("C0000029", "1");
		assertNotNull(res);
	}
	
	@Test
	@DisplayName("Test CheckList fail with productCode empty")
	public void testCheckListFailProductCode() throws Exception{
		try {
			String res = mobileManager.checklist("", "1");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.productCode.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test CheckList fail with empty tempResidence")
	public void testCheckListFailTempResidence() throws Exception {
		try {
			String res = mobileManager.checklist("C0000029", "");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.temporyResidence.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test CheckList fail with not Numeric tempResidence")
	public void testCheckListFailTempResidenceNotNumeric() throws Exception {
		try {
			String res = mobileManager.checklist("C0000029", "e34");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.temporyResidence.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test CheckList fail with not exist tempResidence")
	public void testCheckListFailTempResidenceNotExists() throws Exception {
		try {
			String res = mobileManager.checklist("M0000001", "1");
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.get.checklist.required"), e.getMessage());
		}
	}
}
