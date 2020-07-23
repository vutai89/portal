package com.mcredit.unit.test.mobile4Sales;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;

public class RouteCaseJobTest {
	MobileManager manager = new MobileManager();
	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}

	@Test
	public void routeCaseJobTest() throws Exception {
		
		RACCaseResultDTO rACCaseResultDTO = new RACCaseResultDTO();
		rACCaseResultDTO = manager.routeCaseBPM();
		System.out.println("Total ---------->   " + rACCaseResultDTO.getTotal());
		System.out.println("Sucess ---------->   " + rACCaseResultDTO.getSuccessCount());
		System.out.println("Fail ---------->   " + rACCaseResultDTO.getFailCount());
		
	}

	@AfterAll
	static void tearDown() throws Exception {
		System.out.println("Running: tearDown");

	}
}
