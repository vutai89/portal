package com.mcredit.unit.test.mobile.aggregate;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.mobile.UnitOfWorkMobileTest;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.enums.MessageLogStatus;
import com.mcredit.model.enums.UplCreAppRequestStatus;
import com.mcredit.model.object.mobile.dto.AbortCaseDTO;
import com.mcredit.model.object.mobile.dto.TestListCaseNoteDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.model.object.mobile.enums.MfsServiceName;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.util.JSONConverter;

public class MobileAggregateTest {
	private UnitOfWorkMobileTest _unitOfWorkMobileTest = null;
	
	public MobileAggregateTest(UnitOfWorkMobileTest _uok) {
		this._unitOfWorkMobileTest = _uok;
	}
	
	public Boolean setNullForImei(String loginId) {
		Users user = this._unitOfWorkMobileTest.usersRepository().findUserByLoginId(loginId);
		UsersProfiles usersProfiles = this._unitOfWorkMobileTest.usersProfilesRepository().getByUserId(user.getId());
		
		usersProfiles.setMobileImei(null);
		this._unitOfWorkMobileTest.usersProfilesRepository().update(usersProfiles);
		return true;
	}
	
	public Long addUplCreditAppRequest(UplCreditAppRequest upl) {
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(upl);
	}
	
	public void addUplCreditAppFiles(UplCreditAppFiles upl) {
		this._unitOfWorkMobileTest.uplCreditAppFilesRepository().add(upl);
	}
	
	public int deleteUplCreditAppRequest(HashSet<Long> lstRemoveId) {
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().remove(lstRemoveId);
	}

	public UplCreditAppRequest getUplCreditAppRequest(Long id) {
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().get(id);
	}
	
	public Users getUserByLoginId(String loginId) {
		return this._unitOfWorkMobileTest.usersRepository().findUserByLoginId(loginId);
	}
	
	public MessageLog createDataForCreateCaseBPMJob(String loginId) {
		
		Users user = this._unitOfWorkMobileTest.usersRepository().findUserByLoginId(loginId);
		
		UplCreditAppRequest uplCreditAppRequest = new UplCreditAppRequest();
		uplCreditAppRequest.setSaleId(Long.valueOf(user.getEmpId()));
		uplCreditAppRequest.setStatus(UplCreAppRequestStatus.X.value());
		this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(uplCreditAppRequest);
		
		MessageLog messageLog = new MessageLog();
		messageLog.setTransId(uplCreditAppRequest.getId());
		messageLog.setMsgStatus(MessageLogStatus.NEW.value());
		messageLog.setMsgRequest("{\"variables\":[{\"issueDateCitizenID_sale\":\"2008-12-20\",\"signContractAt\":\"KIK280001\",\"signContractAddress\":\"Tầng 3, Số 3 Nguyễn Oanh, Phường 10, Quận Gò Vấp, TPHoChiMinh\",\"saleMobile\":\"\",\"filesPath\":\"/mobile4sales-3651ebd02b25dcdf424af542a7265979//2019/02/15/142790701/\",\"loanTenor_sale\":12,\"saleCode\":\"BDS12000001\",\"schemeProduct_sale\":\"C0000004\",\"loanAmount_sale\":50000000,\"createFromMobile\":\"1\",\"cat_result_company_name\":\"\",\"saleName\":\"\",\"temporaryResidence_sale\":\"1\",\"citizenID_sale\":\"142790701\",\"productSchemesFull\":\"\",\"hasInsurrance_sale\":1,\"startUserName\":\"kienmt.ho\",\"customerName_sale\":\"sonhv\",\"saleCode_label\":\"BDS12000001\",\"cat_result_type\":\"\",\"companyTaxNumber\":\"\",\"saleID\":\"4933507645ac19077378411055612915\"}],\"pro_uid\":\"46997778359b23c7d855ce6051135589\",\"tas_uid\":\"34520810659b23c7e0de770024812196\"}");
		messageLog.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_CREATE_CASE_BPM.value());
		this._unitOfWorkMobileTest.messageLogRepository().add(messageLog);
		
		return messageLog;
		
	}

	public int deleteUplCreditAppFiles(HashSet<Long> lstRemoveId) {
		return this._unitOfWorkMobileTest.uplCreditAppFilesRepositoryTest().remove(lstRemoveId);
		
	}

	public int deleteMessageLog(HashSet<Long> lstRemoveId) {
		return this._unitOfWorkMobileTest.messageLogRepositoryTest().remove(lstRemoveId);
	}
	
	
	public Boolean deleteDataForCreateCaseBPMJob(MessageLog messageLog) {
		MessageLog mess = this._unitOfWorkMobileTest.messageLogRepository().getMessageByMessId(messageLog.getId());
		System.out.println("Delte messLog ------> Id = " + mess.getId());
		this._unitOfWorkMobileTest.messageLogRepository().remove(mess);
		UplCreditAppRequest upl = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getById(messageLog.getTransId());
		System.out.println("Delte uplcreAppRequest ------> Id = " + upl.getId());
		this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().remove(upl);
		return true;
	}

	public MessageLog getMessageLog(Long idTrue) {
		return this._unitOfWorkMobileTest.messageLogRepositoryTest().getMessageLogByTransId(idTrue);
	}
	
	public MessageLog getMessageLogNotification(Long id) {
		return this._unitOfWorkMobileTest.messageLogRepositoryTest().getMessageLogNotification(id);
	}

	public List<Long> addProducts(Product pTrue, Product pFalse) {
		List<Long> lstProduct = new ArrayList<>();
		Long idTrue = this._unitOfWorkMobileTest.productRepository().add(pTrue);
		Long idFalse = this._unitOfWorkMobileTest.productRepository().add(pFalse);
		lstProduct.add(idTrue);
		lstProduct.add(idFalse);
		return lstProduct;
	}

	public int removeProducts(List<Integer> lstRemoveProduct) {
		return this._unitOfWorkMobileTest.productRepository().remove(lstRemoveProduct);
	}
	
	public TestListCaseNoteDTO getDateForTestGetCaseNote() {
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getRandomAppNumberSale();
	}
	
	public List<MessageLog> createDataForAbortCaseBPMJob() {
		List<MessageLog> lst = new ArrayList<MessageLog>();
		// -- messageLogSuccess
		lst.add(createMessageLogForAbortCaseBPMJob("01231"));
		this._unitOfWorkMobileTest.messageLogRepository().add(createMessageLogForAbortCaseBPMJob("01231"));
		// -- messageLogFailRoute
		lst.add(createMessageLogForAbortCaseBPMJob("01232"));
		this._unitOfWorkMobileTest.messageLogRepository().add(createMessageLogForAbortCaseBPMJob("01232"));
		// -- messageLogFailAbort
		lst.add(createMessageLogForAbortCaseBPMJob("01233"));
		this._unitOfWorkMobileTest.messageLogRepository().add(createMessageLogForAbortCaseBPMJob("01233"));
		return lst;
	}
	
	public int removeMessageLogById(List<String> lstId) {
		return this._unitOfWorkMobileTest.messageLogRepositoryTest().removeById(lstId);
	}

	private MessageLog createMessageLogForAbortCaseBPMJob(String appId) {
		AbortCaseDTO dto = new AbortCaseDTO(1, "Khác","Huy ho so");
		String appNumber = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getRandomAppNumber();
		UplCreditAppRequest request = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber(appNumber);
		MessageLog messageLog = new MessageLog();
		messageLog.setServiceName(MfsServiceName.SERVICE_ABORT_CASE_BPM.value());
		messageLog.setFromChannel(BusinessConstant.MCP);
		messageLog.setToChannel(BusinessConstant.BPM);
		messageLog.setTransId(request.getId());
		messageLog.setRequestTime(new Timestamp((new Date()).getTime()));
		messageLog.setMsgStatus(MessageLogStatus.NEW.value());
		messageLog.setMsgRequest(JSONConverter.toJSON(dto).toString());
		messageLog.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_ABORT_CASE_BPM.value());
		messageLog.setRelationId(appId);
		return messageLog;
	}
	
	public int addProducts(int id, int productId, String productCode, String ccy) {
		return this._unitOfWorkMobileTest.productRepository().add(id, productId, productCode, ccy);
	}

	public String getShopCode() {
		return this._unitOfWorkMobileTest.productRepository().getShopCode();
	}

	public String getCompanyTaxNumber() {
		// TODO Auto-generated method stub
		return this._unitOfWorkMobileTest.productRepository().getCompanyTaxNumber();
	}

	public Long addMessageLog(MessageLog msgLog) {
		return this._unitOfWorkMobileTest.messageLogRepositoryTest().addMessageLog(msgLog);
	}
	
	public void createDataForTestSyncFile() throws Exception {
		Long request1 = createCaseSyncFileSuccessStatusS();
		Long request2 = createCaseSyncFileSuccessStatusI();
		Long request3 = createCaseSyncFileNotFileStatusS();
		Long request4 = createCaseSyncFileNotFileStatusI();
	}
	
	public void removeTestSyncFile() {
		Long re1 = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("1").getId();
		Long re2 = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("2").getId();
		Long re3 = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("3").getId();
		Long re4 = this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("4").getId();
		HashSet<Long> lst = new HashSet<>();
		lst.add(re1);
		lst.add(re2);
		lst.add(re3);
		lst.add(re4);
		HashSet<Long> lstFile = new HashSet<>();
		lstFile.add(re1);
		lstFile.add(re2);
		HashSet<Long> lstDoc = new HashSet<>();
		lstDoc.add(re1);
		lstDoc.add(re2);
		this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().remove(lst);
		this._unitOfWorkMobileTest.uplCreditAppFilesRepositoryTest().remove(lstFile);
		this._unitOfWorkMobileTest.uplCreditAppFilesRepositoryTest().removeDocument(lstDoc);
	}
	
	private Long createCaseSyncFileSuccessStatusS() throws Exception {
		UplCreditAppRequest request = new UplCreditAppRequest();
		request.setAppNumber(1L);
		request.setCitizenId("100000001");
		request.setCustomerName("Test 01");
		request.setStatus("S");
		this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(request);
		UplCreditAppFiles fileRequest = new UplCreditAppFiles();
		fileRequest.setUplCreditAppId(this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("1").getId());
		fileRequest.setDocumentId(6L);
		fileRequest.setStatus("X");
		URL res = getClass().getClassLoader().getResource("photo.jpg");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		fileRequest.setFilePathServer(absolutePath);
		this._unitOfWorkMobileTest.uplCreditAppFilesRepository().add(fileRequest);
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("1").getId();
	}

	
	private Long createCaseSyncFileSuccessStatusI() throws Exception {
		UplCreditAppRequest request = new UplCreditAppRequest();
		request.setAppNumber(2L);
		request.setCitizenId("100000002");
		request.setCustomerName("Test 02");
		request.setStatus("I");
		this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(request);
		UplCreditAppFiles fileRequest = new UplCreditAppFiles();
		fileRequest.setUplCreditAppId(this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("2").getId());
		fileRequest.setDocumentId(6L);
		fileRequest.setStatus("X");
		URL res = getClass().getClassLoader().getResource("photo.jpg");
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		fileRequest.setFilePathServer(absolutePath);
		this._unitOfWorkMobileTest.uplCreditAppFilesRepository().add(fileRequest);
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("2").getId();
	}
	
	
	private Long createCaseSyncFileNotFileStatusS() {
		UplCreditAppRequest request = new UplCreditAppRequest();
		request.setAppNumber(3L);
		request.setCitizenId("100000003");
		request.setCustomerName("Test 03");
		request.setStatus("S");
		this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(request);
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("3").getId();
	}
	
	private Long createCaseSyncFileNotFileStatusI() {
		UplCreditAppRequest request = new UplCreditAppRequest();
		request.setAppNumber(4L);
		request.setCitizenId("100000004");
		request.setCustomerName("Test 04");
		request.setStatus("I");
		this._unitOfWorkMobileTest.uplCreditAppRequestRepository().add(request);
		return this._unitOfWorkMobileTest.uplCreditAppRequestRepositoryTest().getByAppNumber("4").getId();
	}

	public Long getSaleId(String loginId) {
		return Long.valueOf(this._unitOfWorkMobileTest.usersRepository().findUserByLoginId(loginId).getEmpId());
	}

	public Object[] getEmpCodeEmpId(String loginId) {
		return this._unitOfWorkMobileTest.employeeRepository().getEmpCodeEmpId(loginId);
	}
}
