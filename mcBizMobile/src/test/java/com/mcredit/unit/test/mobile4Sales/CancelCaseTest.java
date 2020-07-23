package com.mcredit.unit.test.mobile4Sales;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.MessageLogStatus;
import com.mcredit.model.object.mobile.dto.CancelCaseDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.model.object.mobile.enums.MfsServiceName;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

@DisplayName("Test Cancel Case for Mobile For Sales")
public class CancelCaseTest {
	
	static CancelCaseDTO ccDTO;
	static Long idTrue_zero;
	static Long idTrue_one;
	static Long idFalse;
	static Long reason;
	static String comment;
	static UserDTO user;
	static UplCreditAppRequest uplTrue_zero;
	static UplCreditAppRequest uplTrue_one;
	static UplCreditAppRequest uplFalse;
	static HashSet<Long> lstRemoveId;
	
	@BeforeAll
	static void setup() throws Exception {
		MobileManagerTest mobileManagerTest = new MobileManagerTest();
		// create user
		user = new UserDTO();
		user.setEmpCode("DS123400001"); // linhtt2.ho

		// create default cancel command
		ccDTO = new CancelCaseDTO();
		comment = "Test";
		reason = 0L;
		
		// insert fake data true to upl_credit_app_request
		uplTrue_zero = createCancelCaseTrue("TEST01", 2L, "0105180631","123");
		uplTrue_one = createCancelCaseTrue("TEST02", 3L, "0105180632","122");
		// insert fake data false to upl_credit_app_request
		uplFalse = createCancelCaseFalse();
		
		List<Long> res = mobileManagerTest.addUplCreditAppRequest(uplTrue_zero, uplTrue_one, uplFalse);
		idTrue_zero = res.get(0);
		idTrue_one = res.get(1);
		idFalse = res.get(2);
		lstRemoveId = new HashSet<Long>();
		lstRemoveId.addAll(res);
	}
	
	@BeforeEach
	void init() {
		ccDTO = new CancelCaseDTO();
	}
	
	@AfterEach
	void tearDown() {

	}
	 
	@AfterAll
	static void done() throws Exception {
		MobileManagerTest mobileManagerTest = new MobileManagerTest();
		// delete fake data
		mobileManagerTest.removeUplCreditAppRequest(lstRemoveId);
	}
	
	@Test
	@DisplayName("Test Cancel Case True with reason = 0")
	public void testCancelCaseValidationTrue() throws Exception {
		MobileManager manager = new MobileManager(user);
		MobileManagerTest mmt = new MobileManagerTest();
		MobileManagerTest mmt_message_log = new MobileManagerTest();
		ccDTO.setId(idTrue_zero);
		ccDTO.setComment("Test");
		ccDTO.setReason(0L);
		ResponseSuccess res = manager.cancelCase(ccDTO);
		assertNotNull(res);
		UplCreditAppRequest response = mmt.getReturnedCaseAfterUploaded(idTrue_zero);
		assertEquals("Test",response.getAbortComment());
		assertTrue(CacheManager.CodeTable().getIdBy(CTCodeValue1.MFS_CUST_NOT_NEED_BORROW, CTCat.MFS_ABORT_REASON).equals(response.getAbortReason()));
		MessageLog msl = mmt_message_log.getMessageLogCancelCase(idTrue_zero);
		assertEquals("123", msl.getRelationId());
		assertEquals(idTrue_zero, msl.getTransId());
		assertEquals(MessageLogStatus.NEW.value(), msl.getMsgStatus());
		assertEquals(MfsMsgTransType.MSG_TRANS_TYPE_ABORT_CASE_BPM.value(),msl.getTransType());
		assertEquals(BusinessConstant.MCP, msl.getFromChannel());
		assertEquals(BusinessConstant.BPM, msl.getToChannel());
		assertEquals(MfsServiceName.SERVICE_ABORT_CASE_BPM.value(), msl.getServiceName());
		assertEquals(MessageLogStatus.NEW.value(), msl.getMsgStatus());
	}
	
	@Test
	@DisplayName("Test Cancel Case True with reason = 1")
	public void testCancelCaseValidationOneTrue() throws Exception {
		MobileManager manager = new MobileManager(user);
		MobileManagerTest mmt = new MobileManagerTest();
		MobileManagerTest mmt_message_log = new MobileManagerTest();
		ccDTO.setId(idTrue_one);
		ccDTO.setComment("Test");
		ccDTO.setReason(1L);
		ResponseSuccess res = manager.cancelCase(ccDTO);
		assertNotNull(res);
		UplCreditAppRequest response = mmt.getReturnedCaseAfterUploaded(idTrue_one);
		assertEquals("Test",response.getAbortComment());
		assertTrue(CacheManager.CodeTable().getIdBy(CTCodeValue1.MFS_OTHER_REASON, CTCat.MFS_ABORT_REASON).equals(response.getAbortReason()));
		MessageLog msl = mmt_message_log.getMessageLogCancelCase(idTrue_one);
		assertEquals("122", msl.getRelationId());
		assertEquals(idTrue_one, msl.getTransId());
		assertEquals(MessageLogStatus.NEW.value(), msl.getMsgStatus());
		assertEquals(MfsMsgTransType.MSG_TRANS_TYPE_ABORT_CASE_BPM.value(),msl.getTransType());
		assertEquals(BusinessConstant.MCP, msl.getFromChannel());
		assertEquals(BusinessConstant.BPM, msl.getToChannel());
		assertEquals(MfsServiceName.SERVICE_ABORT_CASE_BPM.value(), msl.getServiceName());
		assertEquals(MessageLogStatus.NEW.value(), msl.getMsgStatus());
	}
	
	@Test
	@DisplayName("Test Cancel Case False")
	public void testCancelCaseValidationFail() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(idFalse);
		ccDTO.setComment("Test");
		ccDTO.setReason(reason);
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test Cancel Case False with ID null")
	public void testCancelCaseValidationNotId() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(null);
		ccDTO.setComment("Test");
		ccDTO.setReason(reason);
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.caseId.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test Cancel Case False with ID null")
	public void testCancelCaseValidationWrongReason() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(idTrue_zero);
		ccDTO.setComment("Test");
		ccDTO.setReason(2L);
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
                    Labels.getString("label.mfs.get.checklist.reason.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test Cancel Case False with comment null")
	public void testCancelCaseValidationNullComment() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(idTrue_zero);
		ccDTO.setReason(reason);
		ccDTO.setComment(null);
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.comment.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test Cancel Case False with comment empty")
	public void testCancelValidationEmptyComment() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(idTrue_zero);
		ccDTO.setReason(reason);
		ccDTO.setComment("");
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.comment.required")), e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Test Cancel Case False with reason null")
	public void testCancelCaseValidationNullReason() throws Exception {
		MobileManager manager = new MobileManager(user);
		ccDTO.setId(idTrue_zero);
		ccDTO.setReason(null);
		ccDTO.setComment(comment);
		try {
			ResponseSuccess res = manager.cancelCase(ccDTO);
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.reason.required")), e.getMessage());
		}
	}
	
	public static UplCreditAppRequest createCancelCaseTrue(String name, Long productId, String companyTaxNumber, String appId) throws Exception {
		UplCreditAppRequest uplTrue = new UplCreditAppRequest();
		uplTrue = new UplCreditAppRequest();
		uplTrue.setCreatedBy(user.getEmpCode());
		uplTrue.setCreatedDate(new Date());
		uplTrue.setSaleCode("DS123400001");
		uplTrue.setSaleId(161852L);
		uplTrue.setCustomerName(name);
		uplTrue.setCitizenId("013451271");
		uplTrue.setIssueDateCitizen(new Date());
		uplTrue.setIssuePlace("Ha Noi");
		uplTrue.setProductId(productId);
		uplTrue.setTempResidence("1");
		uplTrue.setLoanTenor(BigDecimal.valueOf(12L));
		uplTrue.setLoanAmount(BigDecimal.valueOf(5000000L));
		uplTrue.setHasInsurance(1L);
		uplTrue.setMobileImei("imei");
		uplTrue.setShopCode("KIK280001");
		uplTrue.setCompanyTaxNumber(companyTaxNumber); 
		uplTrue.setStatus("R");
		uplTrue.setCheckList("Test");
		uplTrue.setAppId(appId);
		uplTrue.setAppNumber(123L);
		return uplTrue;
	}
	
	private static UplCreditAppRequest createCancelCaseFalse() throws Exception {
		UplCreditAppRequest uplFalse = new UplCreditAppRequest();
		uplFalse.setCreatedBy(user.getEmpCode());
		uplFalse.setCreatedDate(new Date());
		uplFalse.setSaleCode("DS123400001");
		uplFalse.setSaleId(161852L);
		uplFalse.setCustomerName("Tran Tuan Linh");
		uplFalse.setCitizenId("013451271");
		uplFalse.setIssueDateCitizen(new Date());
		uplFalse.setIssuePlace("Ha Noi");
		uplFalse.setProductId(6L);
		uplFalse.setTempResidence("1");
		uplFalse.setLoanTenor(BigDecimal.valueOf(12L));
		uplFalse.setLoanAmount(BigDecimal.valueOf(5000000L));
		uplFalse.setHasInsurance(1L);
		uplFalse.setMobileImei("imei");
		uplFalse.setShopCode("KIK280001");
		uplFalse.setCompanyTaxNumber("0105180631");
		uplFalse.setStatus("T");
		uplFalse.setCheckList("Test");
		uplFalse.setAppId("1");
		uplFalse.setAppNumber(1L);
		return uplFalse;
	}
}
