package com.mcredit.unit.test.mobile4Sales;


import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.object.mobile.dto.TestListCaseNoteDTO;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

public class ListCaseNoteTest {

	MobileManager manager = null;
	UserDTO user = null;
	
	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}
	
	@BeforeEach
	public void init() {
		manager = new MobileManager(user);
	}
	
	@BeforeAll
	static void setup() {
	}
	
	@AfterEach
	void tearDown() {
	}
	 
	@AfterAll
	static void done() throws Exception {
		System.out.println("Running: tearDown");
	}
	
	public static TestListCaseNoteDTO initDateTest() throws Exception {
		try(MobileManagerTest managerTest = new MobileManagerTest()) {
			return managerTest.getAppNumberForTestGetCase();
		}
	}
	
	@Test
	public void testGetListCaseNoteSuccess() throws Exception {
		TestListCaseNoteDTO dto = initDateTest();
		UserDTO user = new UserDTO();
		user.setEmpId(dto.getSaleId().toString());
		MobileManager manager = new MobileManager(user);
		Object obj = manager.getCaseNote(initDateTest().getAppNumber().toString());
		assertNotEquals(null, obj);
	}
	
	@Test
	public void testGetListCaseNoteCaseNotExits() throws Exception {
		try {
			TestListCaseNoteDTO dto = initDateTest();
			UserDTO user = new UserDTO();
			user.setEmpId(dto.getSaleId().toString());
			MobileManager manager = new MobileManager(user);
			Object obj = manager.getCaseNote(initDateTest().getAppNumber().toString().substring(0, 2));
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")), e.getMessage());
		}
	}
	
	@Test
	public void testGetListCaseNotePermision() throws Exception {
		try {
			TestListCaseNoteDTO dto = initDateTest();
			UserDTO user = new UserDTO();
			user.setEmpId(dto.getSaleId().toString().substring(0, 2));
			MobileManager manager = new MobileManager(user);
			Object obj = manager.getCaseNote(initDateTest().getAppNumber().toString());
		} catch (ValidationException e) {
			// TODO: handle exception
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.dowload.permission.required"), e.getMessage());
		}
		
		
	}

}
