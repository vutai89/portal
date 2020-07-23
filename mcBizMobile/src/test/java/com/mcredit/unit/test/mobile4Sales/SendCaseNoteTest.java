package com.mcredit.unit.test.mobile4Sales;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.object.mobile.dto.NoteDto;
import com.mcredit.model.object.mobile.dto.TestListCaseNoteDTO;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;
import com.mcredit.unit.test.mobile.manager.SetEsbApiToManager;

public class SendCaseNoteTest {

	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
	}
	
	@AfterAll
	static void done() throws Exception {
		System.out.println("Running: tearDown");
	}
	
	
	@Test
	public void testSendCaseNoteSuccess() throws Exception {
		
		NoteDto dto = new NoteDto();
		MobileManagerTest managerTest = new MobileManagerTest();
		TestListCaseNoteDTO testCaseSend = managerTest.getAppNumberForTestGetCase();
		String appNumber = testCaseSend.getAppNumber().toString();
		UserDTO user = new UserDTO();
		user.setEmpId(testCaseSend.getSaleId().toString());
		dto.setAppNumber(appNumber);
		dto.setNoteContent("Test send case note");
		
		////Mock result call out
		ApiResult apiResultExpect1 = new ApiResult();
		apiResultExpect1.setBodyContent("Success");
		apiResultExpect1.setCode(200);
		
		MobileManager manager = new MobileManager(user);
		// Set esbApiMock into MobileManager
//		EsbApi esbApiMock = PowerMockito.mock(EsbApi.class);
//		PowerMockito.whenNew(EsbApi.class).withAnyArguments().thenReturn(esbApiMock);
		EsbApi esbApiMock = Mockito.mock(EsbApi.class);
		SetEsbApiToManager.setEsbApiToManager(manager, esbApiMock);
		
		doNothing().when(esbApiMock).addCaseNote(anyString(), anyString(), eq("Test send case note"));
		
		ResponseSuccess res = manager.sendCaseNote(dto);
		assertEquals("Success", res.getReturnMes());
	}
	
	@Test
	public void testSendCaseNoteAppNumberNotExits() throws Exception {
		ResponseSuccess res = null;
		NoteDto dto = new NoteDto();
		dto.setAppNumber("12");
		dto.setNoteContent("123");
		try {
			MobileManager manager = new MobileManager();
			res = manager.sendCaseNote(dto);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")), e.getMessage());
		}
	}
	
	
	@Test
	public void testSendCaseNoteAppNumberNullOrEmpty() throws Exception {
		ResponseSuccess res = null;
		NoteDto dto = new NoteDto();
		dto.setAppNumber("");
		dto.setNoteContent("123");
		try {
			MobileManager manager = new MobileManager();
			res = manager.sendCaseNote(dto);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.caseId.required")), e.getMessage());
		}
	}
	
	@Test
	public void testSendCaseNoteNoteContentNull() throws Exception {
		ResponseSuccess res = null;
		NoteDto dto = new NoteDto();
		dto.setAppNumber("12");
		dto.setNoteContent("");
		try {
			MobileManager manager = new MobileManager();
			res = manager.sendCaseNote(dto);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.noteContent.required")), e.getMessage());
		}
	}
	
	@Test
	public void testSendCaseNotePermsion() throws Exception {
		try {
			NoteDto dto = new NoteDto();
			MobileManagerTest managerTest = new MobileManagerTest();
			TestListCaseNoteDTO testCaseSend = managerTest.getAppNumberForTestGetCase();
			String appNumber = testCaseSend.getAppNumber().toString();
			UserDTO user = new UserDTO();
			dto.setAppNumber(appNumber);
			dto.setNoteContent("123");
			user.setEmpId(testCaseSend.getSaleId().toString().substring(0, 2));
			MobileManager manager = new MobileManager(user);
			ResponseSuccess res = manager.sendCaseNote(dto);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.dowload.permission.required"), e.getMessage());
		}
	}
	

}
