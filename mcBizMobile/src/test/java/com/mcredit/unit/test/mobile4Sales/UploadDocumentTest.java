package com.mcredit.unit.test.mobile4Sales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.model.enums.ValidateCreateCase;
import com.mcredit.model.object.mobile.dto.UploadDocumentReturnDTO;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

public class UploadDocumentTest {
	static UserDTO user;
	static UplCreditAppRequest uplTrue;
	static UplCreditAppRequest uplFalse;
	static Long idTrue;
	static Long idFalse;
	static InputStream isCreateCase;
	static InputStream isReturnCase;
	static HashSet<Long> lstRemoveId;
	static int productTrue;
	static int productFalse;
	static List<Integer> lstRemoveProduct;
	static Product pTrue;
	static Product pFalse;
	static String shopCode;
	static String companyTaxNumber;
	static String empCode;
	static String loginId;
	static String empId;

	@BeforeAll
	static void setup() {
		try {
			MobileManagerTest mobileManagerTest1 = new MobileManagerTest();
			lstRemoveProduct = new ArrayList<>();
			lstRemoveProduct = mobileManagerTest1.addProducts();
			productTrue = lstRemoveProduct.get(0);
			productFalse = lstRemoveProduct.get(1);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		try {
			MobileManagerTest mmt = new MobileManagerTest();
			insertReturnedCaseTrue();
			insertReturnedCaseFalse();
			List<Long> res = mmt.addUplCreditAppRequest(uplTrue, uplFalse);
			idTrue = res.get(0);
			idFalse = res.get(1);
			lstRemoveId = new HashSet<>();
			lstRemoveId.addAll(res);

			// create user
			user = new UserDTO();
			loginId = "hoanx.ho";
			MobileManagerTest test = new MobileManagerTest();
			Object[] result = test.getEmpCodeEmpId(loginId);
			empCode = (String) result[0];
			BigDecimal _empId = (BigDecimal) result[1];
			empId =  _empId.toString();
			user.setEmpCode(empCode);
			user.setLoginId(loginId);
			user.setEmpId(empId);
			user.setUsrType("S");

			// create dynamic shopCode
			MobileManagerTest mobileManagerTest = new MobileManagerTest();
			shopCode = mobileManagerTest.getShopCode();

			// create dynamic companyTaxNumber
			MobileManagerTest mobileTest = new MobileManagerTest();
			companyTaxNumber = mobileTest.getCompanyTaxNumber();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
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
		MobileManagerTest mmt = new MobileManagerTest();
		mmt.removeUplCreditAppRequest(lstRemoveId);

		MobileManagerTest mobileManagerTest = new MobileManagerTest();
		mobileManagerTest.removeProducts(lstRemoveProduct);
	}

	@Test
	public void testUploadDocumentReturnNotId() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"customerName\": \"TEST01\",\r\n" + "\"productId\": \""
				+ productTrue + "\",\r\n" + "\"citizenId\": \"012345678\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"15/12/1987\",\r\n"
				+ "\"appStatus\":\"2\",\r\n" + "\"md5\":\"85eb718a5da0382c25ccca52fc20ceba\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"2.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n" + "      \"fileName\": \"3.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadReturn(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.caseId.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotCustomerName() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.customerName.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotProductId() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"\",\r\n" + "\"citizenId\": \"142790706\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.productId.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotCMND() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.citizenId.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotValidCMND() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"12345678\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.length",
					Labels.getString("label.mfs.get.checklist.citizenId.required"),
					ValidateCreateCase.MIN_LENGTH_CITIZEND_ID.intValue() + "-"
							+ ValidateCreateCase.MAX_LENGTH_CITIZEN_ID.intValue()),
					e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotHasInsurance() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.hasInsurance.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseInvalidHasInsurance() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"2\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\"\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.hasInsurance.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotIssueDateCitizen() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\"\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseInvalidIssueDateCitizen() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2099\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\"\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotTempResidence() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2011\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\"\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.temporyResidence.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotIssuePlace() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790752\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000001\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n" + "\"issuePlace\": \"\",\r\n" + "\"shopCode\":\"" + shopCode
				+ "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n"
				+ "\"appStatus\":\"1\",\r\n" + "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.issueplace.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotMd5() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2011\",\r\n" + "\"appStatus\":\"1\",\r\n" + "\"md5\":\"\"\r\n"
				+ "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.md5.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotInfo() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2011\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\"\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.info.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotShopCode() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\"\",\r\n"
				+ "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.shopcode.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongIssueCitizen() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"12\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotLoanTenor() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.loanTenor.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseInvalidLoanTenor() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"-1\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.loanTenor.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNotLoanAmount() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.loanAmount.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseInvalidLoanAmount() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"-1\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.loanAmount.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNullCompanyTaxNumber() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790750\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.taxNumber.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseInvalidCompanyTaxNumber() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790751\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"111111111111111111111\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.length",
					Labels.getString("label.mfs.get.upload.taxNumber.required"),
					ValidateCreateCase.MAX_LENGTH_COMPANY_TAX_NUMBER.intValue()), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongCategory() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790714\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"1111111\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.taxNumber.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongProductId() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"-1\",\r\n" + "\"citizenId\": \"142790706\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.productId.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongShopCode() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n"
				+ "\"shopCode\":\"-1\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";

		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.shopCode.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongMd5() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";

		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(
					Messages.getString("validation.field.notcorect", Labels.getString("label.mfs.post.upload.zip")),
					e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseEmptyPayload() throws Exception {
		String payload = "";
		try {
			testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.payload.required")), e.getMessage());
		}
	}

	@Test
	public void testCreateCaseEmptyInfo() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.validation.image.false"), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseLackImage() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.validation.image.false"), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNullImei() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		UserDTO user = new UserDTO();
		user.setLoginId("xxx");
		user.setEmpCode("xxx");
		user.setUsrType("S");
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790740\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, user);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.upload.imei.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}
	
	@Test
	public void testCreateCaseNullSaleCodeWithThirdParty() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		UserDTO user = new UserDTO();
		user.setLoginId("xxx");
		user.setEmpCode("xxx");
		user.setUsrType("3");
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790740\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, user);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
                    Labels.getString("label.mfs.post.upload.saleCode.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseNullSaleId() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		UserDTO user = new UserDTO();
		user.setLoginId(loginId);
		user.setEmpCode("xxx");
		user.setUsrType("S");
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790720\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000001\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, user);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.upload.saleId.required")), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseImageNameWrong() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790707\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"10.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.validation.image.extension"), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongImageExtension() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790720\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50001\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"e9d5791264f44a4a5c21cbccce116bb8\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.pdf\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, "errorExtension.zip");
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.validation.image.extension"), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongDuplicateCaseId() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n"
				+ "\"customerName\": \"Tran Tuan Linh\",\r\n" + "\"productId\": \"" + productTrue + "\",\r\n"
				+ "\"citizenId\": \"013451271\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload);
		} catch (ValidationException e) {
			assertEquals(Messages.getString("validation.field.duplicate",
					Labels.getString("label.mfs.get.checklist.caseId.required")), e.getMessage());
			// assertEquals(400, e.getHttpStatusCode());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseWrongImageNumber() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n"
				+ "\"appStatus\":\"1\",\r\n" + "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"6.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, user);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("mfs.validation.image.false"), e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testCreateCaseFileTypeError() throws Exception {
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790706\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n"
				+ "\"appStatus\":\"1\",\r\n" + "\"md5\":\"adaf18623a61f57f6ebfaa92e6218914\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"6\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			upload = testPayloadCreateCase(payload, "error.zip");
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(
					Messages.getString("validation.field.invalidFormat", Labels.getString("label.mfs.post.upload.zip")),
					e.getMessage());
		}
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
	}

	@Test
	public void testUploadDocumentSuccess() throws Exception {
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"\",\r\n" + "\"customerName\": \"sonhv\",\r\n"
				+ "\"productId\": \"" + productTrue + "\",\r\n" + "\"citizenId\": \"142790752\",\r\n"
				+ "\"tempResidence\": \"1\",\r\n" + "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n"
				+ "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\",\r\n" + "\"test\":\"1\"\r\n"
				+ "},\r\n" + "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"test2\":\"2\",\r\n"
				+ "\"mobileIssueDateCitizen\": \"20/12/2008\",\r\n" + "\"appStatus\":\"1\",\r\n"
				+ "\"md5\":\"63c7eac924143b8b5511631d05c75a25\",\r\n" + "\"info\":[\r\n" + "    {\r\n"
				+ "      \"fileName\": \"1.jpg\",\r\n" + "      \"documentCode\": \"CivicIdentity\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"22\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"2.jpg\",\r\n" + "      \"documentCode\": \"FacePhoto\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"26\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"3.jpg\",\r\n" + "      \"documentCode\": \"FamilyBook\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"19\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"4.jpg\",\r\n" + "      \"documentCode\": \"CustomerInformationSheet\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n"
				+ "      \"fileName\": \"5.jpg\",\r\n" + "      \"documentCode\": \"StatementPaymentAccount\",\r\n"
				+ "      \"mimeType\": \"jpg\",\r\n" + "      \"groupId\": \"30\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		UploadDocumentReturnDTO upload = new UploadDocumentReturnDTO(-1L);
		upload = testPayloadCreateCase(payload);
		if (!upload.getId().equals(-1L)) {
			lstRemoveId.add(upload.getId());
		}
		assertTrue(upload.getId() > 0L);
	}

	@Test
	public void testUploadDocumentReturnSuccess() throws Exception {
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"" + idTrue + "\",\r\n"
				+ "\"customerName\": \"TEST01\",\r\n" + "\"productId\": \"14\",\r\n"
				+ "\"citizenId\": \"012345678\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"15/12/1987\",\r\n"
				+ "\"appStatus\":\"2\",\r\n" + "\"md5\":\"85eb718a5da0382c25ccca52fc20ceba\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"2.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n" + "      \"fileName\": \"3.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		UploadDocumentReturnDTO upload = testPayloadReturn(payload);
		assertTrue(idTrue.equals(upload.getId()));
	}

	@Test
	public void testUploadReturnedCaseFail() throws Exception {
		String payload = "{\r\n" + "\"request\":{\r\n" + "\"id\":\"" + idTrue + "\",\r\n"
				+ "\"customerName\": \"TEST01\",\r\n" + "\"productId\": \"14\",\r\n"
				+ "\"citizenId\": \"012345678\",\r\n" + "\"tempResidence\": \"1\",\r\n"
				+ "\"loanAmount\": \"50000000\",\r\n" + "\"loanTenor\": \"12\",\r\n" + "\"hasInsurance\": \"1\",\r\n"
				+ "\"issuePlace\": \"54 Nguyễn Chí Thanh,Láng Thượng, Đống Đa, Hà Nội\",\r\n" + "\"shopCode\":\""
				+ shopCode + "\",\r\n" + "\"companyTaxNumber\":\"" + companyTaxNumber + "\"\r\n" + "},\r\n"
				+ "\"mobileProductType\":\"Cash Loan\",\r\n" + "\"mobileIssueDateCitizen\": \"15/12/1987\",\r\n"
				+ "\"appStatus\":\"2\",\r\n" + "\"md5\":\"85eb718a5da0382c25ccca52fc20ceba\",\r\n" + "\"info\":[\r\n"
				+ "    {\r\n" + "      \"fileName\": \"2.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    },\r\n" + "    {\r\n" + "      \"fileName\": \"3.jpg\",\r\n"
				+ "      \"documentCode\": \"CustomerInformationSheet\",\r\n" + "      \"mimeType\": \"jpg\",\r\n"
				+ "      \"groupId\": \"34\"\r\n" + "    }\r\n" + "  ]\r\n" + "}";
		try {
			testPayloadReturn(payload);
		} catch (ValidationException e) {
			assertEquals(400, e.getHttpStatusCode());
			assertEquals(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")), e.getMessage());
		}
	}

	public static void insertReturnedCaseTrue() throws Exception {
		uplTrue = new UplCreditAppRequest();
		uplTrue.setCreatedBy(loginId);
		uplTrue.setCreatedDate(new Date());
		uplTrue.setSaleCode(empCode);
		uplTrue.setSaleId(1274115L);
		uplTrue.setCustomerName("Tran Tuan Linh");
		uplTrue.setCitizenId("013451271");
		uplTrue.setIssueDateCitizen(new Date());
		uplTrue.setIssuePlace("Ha Noi");
		uplTrue.setProductId(Long.valueOf(productTrue));
		uplTrue.setTempResidence("1");
		uplTrue.setLoanTenor(BigDecimal.valueOf(12L));
		uplTrue.setLoanAmount(BigDecimal.valueOf(50000L));
		uplTrue.setHasInsurance(1L);
		uplTrue.setMobileImei("imei");
		uplTrue.setShopCode(shopCode);
		uplTrue.setCompanyTaxNumber(companyTaxNumber);
		uplTrue.setStatus("R");
		uplTrue.setCheckList("Test");
		uplTrue.setAppStatus(17427L);
		// MobileFactory.getMobileAggregate(uok.mobile, null);
	}

	public static void insertReturnedCaseFalse() {
		uplFalse = new UplCreditAppRequest();
		uplFalse.setCreatedBy(loginId);
		uplFalse.setCreatedDate(new Date());
		uplFalse.setSaleCode(empCode);
		uplFalse.setSaleId(1274115L);
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
		uplFalse.setShopCode(shopCode);
		uplFalse.setCompanyTaxNumber(companyTaxNumber);
		uplFalse.setStatus("T");
		uplFalse.setCheckList("Test");
		uplFalse.setAppStatus(17427L);
	}

	public static void insertProductTrue() throws ParseException {
		pTrue = new Product();
		pTrue.setCreatedDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pTrue.setLastUpdatedDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pTrue.setCreatedBy("test");
		pTrue.setProductGroupId(new BigDecimal("1"));
		pTrue.setProductCode("C0000004");
		pTrue.setCcy("ccy");
		pTrue.setEndEffDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pTrue.setStartEffDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
	}

	public static void insertProductFalse() throws ParseException {
		pFalse = new Product();
		pFalse.setCreatedDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pFalse.setLastUpdatedDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pFalse.setCreatedBy("test");
		pFalse.setProductGroupId(new BigDecimal("1"));
		pFalse.setProductCode("C0000004");
		pFalse.setCcy("ccy");
		pFalse.setEndEffDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
		pFalse.setStartEffDate(new SimpleDateFormat("dd/mm/yyyy").parse("20/12/2011"));
	}

	public UploadDocumentReturnDTO testPayloadCreateCase(String payload) throws Exception {
		MobileManager manager = new MobileManager(user);
		String fileName = "LowQuality.zip";
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream uploadedInputStream = new FileInputStream(file);
		return manager.createCase(uploadedInputStream, payload, fileName);
	}

	public UploadDocumentReturnDTO testPayloadCreateCase(String payload, String fileName) throws Exception {
		MobileManager manager = new MobileManager(user);
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream uploadedInputStream = new FileInputStream(file);
		return manager.createCase(uploadedInputStream, payload, fileName);
	}

	public UploadDocumentReturnDTO testPayloadCreateCase(String payload, UserDTO user) throws Exception {
		MobileManager manager = new MobileManager(user);
		String fileName = "LowQuality.zip";
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream uploadedInputStream = new FileInputStream(file);
		return manager.createCase(uploadedInputStream, payload, fileName);
	}

	public UploadDocumentReturnDTO testPayloadReturn(String payload) throws Exception {
		MobileManager manager = new MobileManager(user);
		String fileName = "uploadReturnedCase.zip";
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		InputStream uploadedInputStream = new FileInputStream(file);
		return manager.createCase(uploadedInputStream, payload, fileName);
	}

}
