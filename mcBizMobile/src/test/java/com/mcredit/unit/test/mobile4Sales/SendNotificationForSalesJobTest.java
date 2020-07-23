package com.mcredit.unit.test.mobile4Sales;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

public class SendNotificationForSalesJobTest {
	MobileManager manager = new MobileManager();
	
	private static Long upl_id;
	private static Long msg_log_true;
	@BeforeAll
	static void setUp() throws Exception {
		System.out.println("Setting it up!");
		upl_id = createUplCreditAppRequest();
		msg_log_true = createMessageLog("[17422,0]");
	}

	@Test
	public void routeCaseJobTest() throws Exception {
		
		RACCaseResultDTO rACCaseResultDTO = new RACCaseResultDTO();
		rACCaseResultDTO = manager.sendNotificationForSale();
		System.out.println("Total ---------->   " + rACCaseResultDTO.getTotal());
		System.out.println("Sucess ---------->   " + rACCaseResultDTO.getSuccessCount());
		System.out.println("Fail ---------->   " + rACCaseResultDTO.getFailCount());
		
	}

	@AfterAll
	static void tearDown() throws Exception {
		System.out.println("Running: tearDown");
		HashSet<Long> lst = new HashSet<>();
		lst.add(upl_id);
		MobileManagerTest mmt = new MobileManagerTest();
		mmt.removeUplCreditAppRequest(lst);

	}
	
	
	public static Long createUplCreditAppRequest() throws Exception {
		UplCreditAppRequest uplTrue = new UplCreditAppRequest();
		MobileManagerTest mmt1 = new MobileManagerTest();
		Long saleId = mmt1.getSaleId("sonhv.ho");
		uplTrue = new UplCreditAppRequest();
		uplTrue.setCreatedBy("sonhv.ho");
		uplTrue.setCreatedDate(new Date());
		uplTrue.setSaleCode("IS170700064");
		uplTrue.setSaleId(saleId);
		uplTrue.setCustomerName("Hoang Van Son");
		uplTrue.setCitizenId("162932537");
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
		uplTrue.setAppNumber(41047L);
		uplTrue.setAppId("7370959315c6d1cbc1c97a6022091436");
		MobileManagerTest mmt2 = new MobileManagerTest();
		return mmt2.addUplCreditAppRequest(uplTrue);
	}
	
	public static Long createMessageLog(String mes_req) throws Exception {
		MessageLog msgLog = new MessageLog();
		msgLog.setMsgType("R");
		msgLog.setFromChannel("BPM");
		msgLog.setToChannel("MCP");
		msgLog.setRelationId("7370959315c6d1cbc1c97a6022091436");
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
}
