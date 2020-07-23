package com.mcredit.unit.test.mobile4Sales;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Messages;
import com.mcredit.model.object.mobile.dto.DashboardInfoDTO;
import com.mcredit.model.object.mobile.dto.NotificationDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.sharedbiz.dto.UserDTO;

import junit.framework.TestCase;

public class GetNotificationsTest extends TestCase {
	
	UserDTO user = null;
	MobileManager mobileManager = null;
	 
	@BeforeEach
	public void init() {
		user = new UserDTO();
		user.setLoginId("hoanx.ho");
		user.setEmpCode("TSA10000010");
		
		mobileManager = new MobileManager(user);
	}
	
	@Test
	@DisplayName("Should return list notifications")
	public void shouldReturnNotifications() throws Exception {
		SearchNotiDTO searchDTO = new SearchNotiDTO(1, 10, "");
		List<NotificationDTO> notifications = mobileManager.getNotifications(searchDTO);
		assertTrue(notifications.size() > -1);
	}
	
	@Test
	@DisplayName("Should return exception when keyword null")
	public void shouldReturnExceptionKeyword() throws Exception {
		
		try {
			SearchNotiDTO searchDTO = new SearchNotiDTO(1, 5, null);
			List<NotificationDTO> notifications = mobileManager.getNotifications(searchDTO);
			assertTrue(notifications.size() == 0);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.keyword.required"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return exception when page size zero")
	public void shouldReturnExceptionPageSize() throws Exception {
		
		try {
			SearchNotiDTO searchDTO = new SearchNotiDTO(1, 0, "");
			List<NotificationDTO> notifications = mobileManager.getNotifications(searchDTO);
			assertTrue(notifications.size() == 0);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.pagesize.required"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return exception when page number zero")
	public void shouldReturnExceptionPageNumber() throws Exception {
		
		try {
			SearchNotiDTO searchDTO = new SearchNotiDTO(0, 1, "");
			List<NotificationDTO> notifications = mobileManager.getNotifications(searchDTO);
			assertTrue(notifications.size() == 0);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.get.pagenumber.required"), e.getMessage());
		}
	}
	
	@AfterEach
	public void cleanup() {
		user = null;
		mobileManager = null;
	}
}
