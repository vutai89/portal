package com.mcredit.unit.test.mobile4Sales;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Messages;
import com.mcredit.model.object.mobile.dto.DashboardInfoDTO;
import com.mcredit.sharedbiz.dto.UserDTO;

import junit.framework.TestCase;

public class GetDashBoardInfoTest extends TestCase {
	
	MobileManager mobileManager = null;
	UserDTO user = null;
	
	@BeforeEach
	public void init() {
		user = new UserDTO();
		user.setLoginId("hoanx.ho");
		user.setEmpCode("TSA10000010");
		
		mobileManager = new MobileManager(user);
	}
	
	@Test
	@DisplayName("Should return dashboard info")
	public void shouldReturnDashboardInfo() throws Exception {
		DashboardInfoDTO dashboardInfoDTO = mobileManager.getDashboardInfo();
		assertTrue(dashboardInfoDTO.getCaseAbortNumber() > -1);
		assertTrue(dashboardInfoDTO.getCaseProcessingNumber() > -1);
	}
	
	@Test
	@DisplayName("Should return exception")
	public void shouldReturnException() throws Exception {
		
		user.setEmpCode("");
		try {
			DashboardInfoDTO dashboardInfoDTO = mobileManager.getDashboardInfo();
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.common.salecode.required"), e.getMessage());
		}
	}
	
	@AfterEach
	public void cleanup() {
		user = null;
		mobileManager = null;
	}
}




