package com.mcredit.unit.test.mobile4Sales;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

public class CreateCaseJobTest {
	MobileManager manager = new MobileManager();

	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}

	@SuppressWarnings("resource")
	@Test
	public void testCreateCaseJobTest() throws Exception {
		System.out.println("---------->   " + "Test111111111111");
		
		//Create data : Messlog, uplCreappRequest table
		MobileManagerTest managerTest1 = new MobileManagerTest();
		MessageLog messageLog = managerTest1.createDataForCreateCaseBPMJob("honglt.cs");
		
		System.out.println("MessageLog Id ---------->   " + messageLog.getId());
		System.out.println("MessageLog TranId---------->   " + messageLog.getTransId());
		
		RACCaseResultDTO rACCaseResultDTO = new RACCaseResultDTO();
		rACCaseResultDTO = manager.createCaseToBPM();
		System.out.println("Total ---------->   " + rACCaseResultDTO.getTotal());
		System.out.println("Sucess ---------->   " + rACCaseResultDTO.getSuccessCount());
		System.out.println("Fail ---------->   " + rACCaseResultDTO.getFailCount());
		
		MobileManagerTest managerTest2 = new MobileManagerTest();
		managerTest2.deleteDataForCreateCaseBPMJob(messageLog);
	}

	@AfterAll
	static void tearDown() throws Exception {
		System.out.println("Running: tearDown");

	}

}
