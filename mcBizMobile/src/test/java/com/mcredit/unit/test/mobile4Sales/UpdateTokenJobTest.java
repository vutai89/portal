package com.mcredit.unit.test.mobile4Sales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.unit.test.mobile.manager.SetEsbApiToManager;

public class UpdateTokenJobTest {


	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}

	@Test
	public void updateTokenJobTest01() throws Exception {
		
		MobileManager manager = new MobileManager();
		EsbApi esbApiMock = Mockito.mock(EsbApi.class);
		SetEsbApiToManager.setEsbApiToManager(manager, esbApiMock);
		
		//Mock result call out
		ApiResult apiResultExpect = new ApiResult();
		apiResultExpect.setBodyContent("{\r\n" + 
				" \"access_token\":\"123abc\",\r\n" + 
				" \"refresh_token\":\"123abc\"\r\n" + 
				"}");
		apiResultExpect.setCode(200);
		when(esbApiMock.authorized(anyObject())).thenReturn(apiResultExpect);
		when(esbApiMock.refreshToken(anyString())).thenReturn(apiResultExpect);
		List<ExternalUserMapping> eumList = manager.updateTokenMobile();
		
		assertEquals("123abc", eumList.get(0).getAccessToken());
		assertEquals("123abc", eumList.get(0).getRefreshToken());
		
	}

	@AfterAll
	static void tearDown() throws Exception {
		System.out.println("Running: tearDown");

	}
}
