package com.mcredit.unit.test.mobile4Sales;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Messages;
import com.mcredit.sharedbiz.dto.UserDTO;

import junit.framework.TestCase;

public class DownloadPDFTest extends TestCase {
	MobileManager mobileManager = null;
	UserDTO user = null;
	
	@BeforeEach
	public void init() {
		user = new UserDTO();
		user.setLoginId("hoanx.ho");
		user.setEmpCode("X_TSA10000010");
		
		mobileManager = new MobileManager(user);
	}
	
	@Test
	@DisplayName("Should return id exception")
	public void idException() {
		try {
			File pdf = mobileManager.downloadFile(-1L);
		} catch (Exception e) {
			assertEquals(Messages.getString("mfs.dowload.id.valid"), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Should return file exists or permision exception")
	public void fileExistsException() {
		try {
			File pdf = mobileManager.downloadFile(1L);
		} catch (Exception e) {
			boolean isOK = Messages.getString("mfs.dowload.id.exists").equals(e.getMessage()) ||  Messages.getString("mfs.dowload.permission.required").equals(e.getMessage());
			assertTrue(isOK);
		}
	}
	
	@AfterEach
	public void cleanup() {
		user = null;
		mobileManager = null;
	}
}
