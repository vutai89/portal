package com.mcredit.unit.test.mobile4Sales;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.model.dto.KioskDTO;

import junit.framework.TestCase;

public class GetKiosksTest extends TestCase {
	MobileManager mobileManager = null;
	 
	@BeforeEach
	public void init() {
		mobileManager = new MobileManager();
	}
	
	@Test
	@DisplayName("Should get list kiosk")
	public void shouldGetListProduct() throws Exception {
		List<KioskDTO> kiosks = mobileManager.geKiosks();
		assertTrue(kiosks.size() > 0);
	}
	
	@Test
	@DisplayName("Test first kiosk should has value")
	public void productShouldHasValue() throws Exception {
		KioskDTO kiosk = mobileManager.geKiosks().get(0);
		
		assertTrue(kiosk.getId() > 0);
		assertTrue(kiosk.getKioskCode().length() > 0);
		assertTrue(kiosk.getKioskAddress().length() > 0);
	}
	
	@AfterEach
	public void cleanup() {
		mobileManager = null;
	}
}
