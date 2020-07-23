package com.mcredit.unit.test.mobile4Sales;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;
import com.mcredit.unit.test.mobile.manager.SetEsbApiToManager;

public class JobAbortCaseTest {

	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
		
	}
	
	@SuppressWarnings("resource")
	@Test
	public void testAbortCaseJobTestUse() throws Exception {
		
		MobileManager manager = new MobileManager();
		//Create data : Messlog, uplCreappRequest table
		MobileManagerTest managerTestCreate = new MobileManagerTest();
		List<MessageLog> lstLog = managerTestCreate.createDataForAbortCase();
		List<String> lstId = new ArrayList<>();
		for(MessageLog msg : lstLog)
			lstId.add(msg.getRelationId());
		
		EsbApi esbApiMock = PowerMockito.mock(EsbApi.class);
		PowerMockito.whenNew(EsbApi.class).withAnyArguments().thenReturn(esbApiMock);
		
		//Mock result call out
		ApiResult apiResultExpect1 = new ApiResult();
		apiResultExpect1.setBodyContent("Success");
		apiResultExpect1.setCode(200);
		
		PowerMockito.doReturn(apiResultExpect1).when(esbApiMock).abortCase(anyString(), eq("01231"), anyString());
		PowerMockito.doReturn(apiResultExpect1).when(esbApiMock).abortCase(anyString(), eq("01232"), anyString());
		
		ApiResult apiResultExpect2 = new ApiResult();
		apiResultExpect2.setCode(400);
		PowerMockito.doReturn(apiResultExpect2).when(esbApiMock).abortCase(anyString(), eq("01233"), anyString());
		
		
		
		//Assert Actual result
		RACCaseResultDTO rACCaseResultDTO = new RACCaseResultDTO();
		rACCaseResultDTO = manager.abortCase();
		assertNotNull("3", rACCaseResultDTO.getTotal());
		assertNotNull("1", rACCaseResultDTO.getSuccessCount());
		assertNotNull("2", rACCaseResultDTO.getFailCount());
	
		
		//Remote data test
		MobileManagerTest managerTestRemove = new MobileManagerTest();
		managerTestRemove.removeDataForAbortCase(lstId);
	}
	
	@AfterAll
	static void done() throws Exception {
		System.out.println("Running: tearDown");
	}
	

}
