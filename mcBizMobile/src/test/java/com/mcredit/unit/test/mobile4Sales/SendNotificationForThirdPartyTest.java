package com.mcredit.unit.test.mobile4Sales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.model.object.mobile.dto.NotiForThirdPartyDTO;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;
import com.mcredit.util.JSONConverter;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EsbApi.class)
public class SendNotificationForThirdPartyTest {

	static Long upl_id;
	static Long msg_log_true;
	static Long msg_log_false;
	static String appId;
	static Long appNumber;

	@BeforeAll
	static void setup() throws Exception {
		try {
//			appId = randomString();
			appId = "Y7NWQLPLCTARBSTMEO";
			appNumber = 1235134231L;
			// create case in upl_credit_app_request
			upl_id = createUplCreditAppRequest();

			// create message_log
			msg_log_true = createMessageLog("[17422,0]");
			msg_log_false = createMessageLog("[abc,0]");
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	@BeforeEach
	void init() {

	}

	@AfterEach
	void teardown() {

	}

	@AfterAll
	static void done() throws Exception {
		HashSet<Long> lst = new HashSet<>();
		lst.add(upl_id);
		MobileManagerTest mmt = new MobileManagerTest();
		mmt.removeUplCreditAppRequest(lst);
	}

	@Test
	public void testSendNotificationForThirdParty() throws Exception {
		MobileManager mo = PowerMockito.spy(new MobileManager());
		
		EsbApi esbApiMock = PowerMockito.mock(EsbApi.class);
		PowerMockito.whenNew(EsbApi.class).withAnyArguments().thenReturn(esbApiMock);
//		EsbApi esbApiMock = Mockito.mock(EsbApi.class);
//		SetEsbApiToManager.setEsbApiToManager(mo, esbApiMock);
		
		//Mock result call out
		ApiResult apiResultExpect1 = new ApiResult();
		apiResultExpect1.setBodyContent("Success");
		apiResultExpect1.setCode(200);
		NotiForThirdPartyDTO noti = new NotiForThirdPartyDTO(upl_id, "SALE Đang chờ nhập liệu bổ sung - Return",
				appNumber, appId);
		String json = JSONConverter.toJSON(noti);
		PowerMockito.doReturn(apiResultExpect1).when(esbApiMock).postThirdParty(eq("/api/v1.0/mobile_4_sale_phase2/thien-tu/status"),anyString());
//		when(esbApiMock.postThirdParty("/api/v1.0/mobile_4_sale_phase2/thien-tu/status",json)).thenReturn(apiResultExpect1);
		mo.sendNotificationForThirdParty();
		MobileManagerTest mmt1 = new MobileManagerTest();
		MessageLog msg_true = mmt1.getMessageLogNotification(msg_log_true);
		
		assertEquals("E", msg_true.getMsgStatus());
		assertEquals("[17422,0]", msg_true.getMsgRequest());
		
		MobileManagerTest mmt2 = new MobileManagerTest();
		MessageLog msg_false = mmt2.getMessageLogNotification(msg_log_false);
		assertEquals("E", msg_false.getMsgStatus());
		assertEquals("[abc,0]", msg_false.getMsgRequest());
	}

	public static Long createUplCreditAppRequest() throws Exception {
		UplCreditAppRequest uplTrue = new UplCreditAppRequest();
		uplTrue = new UplCreditAppRequest();
		uplTrue.setCreatedBy("cuongvt.ho");
		uplTrue.setCreatedDate(new Date());
		uplTrue.setSaleCode("DS123400001");
		uplTrue.setSaleId(161852L);
		uplTrue.setCustomerName("Vu Tuan Cuong");
		uplTrue.setCitizenId("013451271");
		uplTrue.setIssueDateCitizen(new Date());
		uplTrue.setIssuePlace("Ha Noi");
		uplTrue.setProductId(Long.valueOf(12L));
		uplTrue.setTempResidence("1");
		uplTrue.setLoanTenor(BigDecimal.valueOf(12L));
		uplTrue.setLoanAmount(BigDecimal.valueOf(50000L));
		uplTrue.setHasInsurance(1L);
		uplTrue.setMobileImei("imei");
		uplTrue.setShopCode("TEST");
		uplTrue.setCompanyTaxNumber("12345");
		uplTrue.setStatus("R");
		uplTrue.setCheckList("Test");
		uplTrue.setAppStatus(17427L);
		uplTrue.setAppId(appId);
		uplTrue.setAppNumber(appNumber);
		MobileManagerTest mmt = new MobileManagerTest();
		return mmt.addUplCreditAppRequest(uplTrue);
	}

	public static Long createMessageLog(String mes_req) throws Exception {
		MessageLog msgLog = new MessageLog();
		msgLog.setMsgType("R");
		msgLog.setFromChannel("BPM");
		msgLog.setToChannel("MCP");
		msgLog.setRelationId(appId);
		msgLog.setTransId(upl_id);
		msgLog.setTransType("notifyLOSApplication");
		msgLog.setMsgOrder(1);
		msgLog.setMsgRequest(mes_req);
		msgLog.setMsgStatus("N");
		msgLog.setRequestTime(new Timestamp(new Date().getTime()));
		msgLog.setTaskId(0L);
		MobileManagerTest mmt = new MobileManagerTest();
		return mmt.addMessageLog(msgLog);
	}
	
	private static String randomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
