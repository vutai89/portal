package com.mcredit.unit.test.mobile4Sales;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;
public class JobSyncFileFTPTest {
	
	MobileManager manager = null;

	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}
	
	@BeforeEach
	public void init() {
		manager = new MobileManager();
	}
	
	@AfterAll
	static void done() throws Exception {
		System.out.println("Running: tearDown");
	}
	
	@Test
	public void testSyncCaseJobTest() throws Exception {
		RACCaseResultDTO rACCaseResultDTO = new RACCaseResultDTO();
		MobileManagerTest create = new MobileManagerTest();
		int cr = create.createDataForSyncFile();
		rACCaseResultDTO = manager.syncPdfToFTPServer();
		System.out.println("Total ---------->   " + rACCaseResultDTO.getTotal());
		System.out.println("Sucess ---------->   " + rACCaseResultDTO.getSuccessCount());
		System.out.println("Fail ---------->   " + rACCaseResultDTO.getFailCount());
		MobileManagerTest remove = new MobileManagerTest();
		int rm = remove.removeDataForSyncFile();
	}
	
	public void testNotCaseForSync() throws Exception {
		
	}
	
}
