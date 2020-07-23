package com.mcredit.unit.test.mobile4Sales;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.ReportManager;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.util.JSONConverter;

public class ReportAggregateTest {

	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}

	@SuppressWarnings("resource")
	@Test
	public void getReportTest01() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<DataReport> result = new ArrayList<>();

		try {
			result = manger.getReport("");
		} catch (Exception e) {
			assertEquals("Data not empty!", e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void getReportTest02() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<DataReport> result = new ArrayList<>();

		try {
			result = manger.getReport("12/02/2018");
		} catch (Exception e) {
			assertEquals("Data not formatted!", e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void getReportTest03() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<DataReport> result = new ArrayList<>();
		result = manger.getReport("2018-02-12");

		System.out.println(JSONConverter.toJSON(result).toString());
	}

	@SuppressWarnings("resource")
	@Test
	public void getApprovalReportTest01() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<ApprovalReport> result = new ArrayList<>();

		try {
			result = manger.getApprovalReport("");
		} catch (Exception e) {
			assertEquals("Data not empty!", e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void getApprovalReportTest02() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<ApprovalReport> result = new ArrayList<>();

		try {
			result = manger.getApprovalReport("12/02/2018");
		} catch (Exception e) {
			assertEquals("Data not formatted!", e.getMessage());
		}
	}

	@SuppressWarnings("resource")
	@Test
	public void getApprovalReportTest03() throws Exception {
		UserDTO user = new UserDTO();
		ReportManager manger = new ReportManager(user);

		List<ApprovalReport> result = new ArrayList<>();
		result = manger.getApprovalReport("2018-02-12");
		System.out.println(JSONConverter.toJSON(result).toString());
		
	}

	@AfterAll
	static void tearDown() throws Exception {
		System.out.println("Running: tearDown");

	}
}
