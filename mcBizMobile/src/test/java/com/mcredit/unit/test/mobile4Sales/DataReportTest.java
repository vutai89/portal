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
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

public class DataReportTest {
MobileManager manager = null;
	
	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}
	
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
	static void done() throws Exception {
		System.out.println("Running: tearDown");
	}
	
	@Test
	public void testGetDailyReportSuccess() throws Exception {
		String dateExport = "2018-10-06";
		Object obj = manager.getReport(dateExport);
		assertNotEquals(null, obj);
	}
	
	@Test
	public void testGetDailyReportDateNull() throws Exception {
		String dateExport = "";
		try {
			Object obj = manager.getReport(dateExport);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.report.dateExport")), e.getMessage());
		}
	}
	
	@Test
	public void testGetApprovalReportErrors() throws Exception {
		String dateExport = "2018-11-06";
		Object obj = manager.getApprovalReport(dateExport);
		assertNotEquals(null, obj);
	}
	
	@Test
	public void testGetApprovalReportDateNull() throws Exception {
		String dateExport = "";
		try {
			Object obj = manager.getApprovalReport(dateExport);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.report.dateExport")), e.getMessage());
		}
	}

}
