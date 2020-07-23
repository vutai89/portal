package com.mcredit.unit.test.mobile4Sales;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Messages;
import com.mcredit.model.object.mobile.dto.CaseDTO;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.sharedbiz.dto.UserDTO;

import junit.framework.TestCase;

public class GetCasesTest extends TestCase {
	
	MobileManager mobileManager = null;
	UserDTO user = null;
	SearchCaseDTO searchCaseDTO = null;
	
	@BeforeEach
	public void init() {
		user = new UserDTO();
		user.setLoginId("hoanx.ho");
		user.setEmpCode("TSA10000010");
		
		mobileManager = new MobileManager(user);
	}
	
	@Test
	@DisplayName("Should return list case processing")
	public void listCaseProcessing() throws Exception {
		searchCaseDTO = new SearchCaseDTO("", 1, 10, "PROCESSING");
		List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		
		assertTrue(cases.size() > -1);
	}
	
	@Test
	@DisplayName("Should return list case abort")
	public void listCaseAbort() throws Exception {
		searchCaseDTO = new SearchCaseDTO("", 1, 10, "ABORT");
		List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		
		assertTrue(cases.size() > -1);
	}
	
	@Test
	@DisplayName("Should return exception keyword")
	public void exceptionKeyword() throws Exception {
		try {
			searchCaseDTO = new SearchCaseDTO(null, 0, 10, "PROCESSING");
			List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.keyword.required"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return exception page number")
	public void exceptionPagenumber() throws Exception {
		try {
			searchCaseDTO = new SearchCaseDTO("", 0, 10, "PROCESSING");
			List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.pagenumber.required"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return exception page size")
	public void exceptionPageSize() throws Exception {
		try {
			searchCaseDTO = new SearchCaseDTO("", 1, 0, "PROCESSING");
			List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.pagesize.required"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return exception status")
	public void exceptionStatus() throws Exception {
		try {
			searchCaseDTO = new SearchCaseDTO("", 1, 10, "XXX");
			List<CaseDTO> cases = mobileManager.getCases(searchCaseDTO);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.getcases.status.required"), e.getMessage());
		}
	}
	
	@AfterEach
	public void cleanup() {
		user = null;
		mobileManager = null;
	}
}
