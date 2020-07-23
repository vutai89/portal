package com.mcredit.business.mobile.validation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.ValidateCreateCase;
import com.mcredit.model.object.mobile.UploadDocumentDTO;
import com.mcredit.model.object.mobile.dto.CancelCaseDTO;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.model.object.mobile.dto.NoteDto;
import com.mcredit.model.object.mobile.dto.PdfDTO;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.model.object.mobile.dto.UpdateNotificationIdDTO;
import com.mcredit.model.object.mobile.enums.CaseStatusEnum;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class MobileValidation extends AbstractValidation {
	private static final String DATE_FORMAT_NOW = "yyyyMMddHHmmss";

	public void validateCaseNote(NoteDto noteDto) throws Exception {
		if (StringUtils.isNullOrEmpty(noteDto.getAppNumber()))
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.caseId.required")));
		if (StringUtils.isNullOrEmpty(noteDto.getNoteContent()))
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.noteContent.required")));
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public void validateCheckList(String mobileSchemaProductCode, String mobileTemResidence)
			throws ValidationException {
		if (StringUtils.isNullOrEmpty(mobileSchemaProductCode)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.productCode.required")));
		}

		if (StringUtils.isNullOrEmpty(mobileTemResidence)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.temporyResidence.required")));
		}
		if (!StringUtils.isNumberic(mobileTemResidence)) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.temporyResidence.required")));
		}

	}

	/**
	 * Validate cac thong tin co ban ho so gui len
	 * 
	 * @param
	 * @param _user:
	 *            thong tin NVKD tao moi ho so
	 * @param uploadedInputStream:
	 *            luong du lieu file .zip
	 * @param fileName:
	 *            ten file .zip
	 * @return UploadDocumentDTO: thong tin ho so sau khi duoc chuyen thanh 1 class
	 *         xu ly
	 * @throws Exception:
	 *             ket qua loi tra ve khi thong tin ho so bi loi
	 */
	public UploadDocumentDTO validateCreateCase(String payload, UserDTO _user, InputStream uploadedInputStream,
			String fileName) throws Exception {

		if (StringUtils.isNullOrEmpty(payload))
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.payload.required")));

		if (null == uploadedInputStream) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.upload.zip")));
		}

		if (StringUtils.isNullOrEmpty(fileName)) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.post.upload.filename")));
		}
		UploadDocumentDTO upload = new UploadDocumentDTO();
		try {
			ObjectMapper mapper = new ObjectMapper();
			upload = mapper.readValue(payload, new TypeReference<UploadDocumentDTO>() {
			});
		} catch (Exception ex) {
			throw new ValidationException((Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.payload.required"))));
		}

		// validate cac truong trong payload
		checkValid(upload);

		// validate and save zip file
		String dirPath = validateZipFile(uploadedInputStream, upload.getMd5(), fileName, _user.getLoginId());
		if (null == dirPath)
			throw new ValidationException(
					Messages.getString("validation.field.notcorect", Labels.getString("label.mfs.post.upload.zip")));

		upload.setFilePath(dirPath);
		return upload;
	}

	private String validateZipFile(InputStream uploadedInputStream, String md5, String fileName, String loginId)
			throws Exception {

		if (loginId.contains("."))
			loginId = loginId.substring(0, loginId.indexOf("."));

		String dirPath = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_TEMP_DIR) + loginId
				+ File.separator + DateUtil.today(DATE_FORMAT_NOW) + File.separator;
		String filePath = dirPath + fileName;
		File theDir = new File(dirPath);
		if (!theDir.exists())
			theDir.mkdirs();

		String checkSum = this.getCheckSum(uploadedInputStream, filePath);
		if (checkSum != null && checkSum.equals(md5)) {
			return dirPath;
		} else {
			// xac thuc fail delete files
			new File(filePath).deleteOnExit();
			return null;
		}
	}

	private String getCheckSum(InputStream uploadedInputStream, String filePath) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] buffer = new byte[8192];
		int read = 0;
		OutputStream out = null;
		String output = null;

		try {
			// save file
			out = new FileOutputStream(new File(filePath));
			while ((read = uploadedInputStream.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
				out.write(buffer, 0, read);
			}

			// get md5 checksum
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			output = bigInt.toString(16);

			while (output.length() < 32)
				output = "0" + output;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}

		return output;
	}

	private void checkValid(UploadDocumentDTO upload) throws Exception {
		if (upload.getAppStatus() == ValidateCreateCase.RETURNED_CASE.intValue()) {// ho so tra lai
			if (null == upload.getRequest().getId()) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.caseId.required")));
			}
		} else if (upload.getAppStatus() == ValidateCreateCase.NEW_CASE.intValue()) { // ho so tao moi
			// validate ten khach hang
			if (StringUtils.isNullOrEmpty(upload.getRequest().getCustomerName())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.customerName.required")));
			}
			// validate ma san pham
			if (null == upload.getRequest().getProductId()) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.productId.required")));
			}
			// validate CMND
			if (StringUtils.isNullOrEmpty(upload.getRequest().getCitizenId())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.citizenId.required")));
			} else if (upload.getRequest().getCitizenId().length() != ValidateCreateCase.MIN_LENGTH_CITIZEND_ID
					.intValue()
					&& upload.getRequest().getCitizenId().length() != ValidateCreateCase.MAX_LENGTH_CITIZEN_ID
							.intValue()) {
				throw new ValidationException(Messages.getString("validation.field.length",
						Labels.getString("label.mfs.get.checklist.citizenId.required"),
						ValidateCreateCase.MIN_LENGTH_CITIZEND_ID.intValue() + "-"
								+ ValidateCreateCase.MAX_LENGTH_CITIZEN_ID.intValue()));
			}
			// validate KH co tham gia bao hiem hay khong
			if (null == upload.getRequest().getHasInsurance()) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.hasInsurance.required")));
			}
			if (!upload.getRequest().getHasInsurance().equals(0L)
					&& !upload.getRequest().getHasInsurance().equals(1L)) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.mfs.get.checklist.hasInsurance.required")));
			}

			// validate ngay DK CMND
			if (StringUtils.isNullOrEmpty(upload.getMobileIssueDateCitizen())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")));
			} else if (!compareDate(upload.getMobileIssueDateCitizen())) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")));
			}
			// validate Dia chi song trung SHK?
			if (StringUtils.isNullOrEmpty(upload.getRequest().getTempResidence())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.temporyResidence.required")));
			}

			// validate new case: cac truong co ton tai trong he thong khong
			validateNewCase(upload);
		} else {
			throw new ValidationException(Messages.getString("mfs.validation.field.unknown.status"));
		}

		if (StringUtils.isNullOrEmpty(upload.getMd5())) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.md5.required")));
		}

		if (null == upload.getInfo()) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.info.required")));
		}
	}

	private void validateNewCase(UploadDocumentDTO upload) throws Exception {

		// validate ShopCode
		if (StringUtils.isNullOrEmpty(upload.getRequest().getShopCode())) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.shopcode.required")));
		} else {
			CodeTableDTO isValidShopCode = CacheManager.CodeTable().getCodeByCategoryCodeValue1(CTCat.TRAN_OFF.value(), upload.getRequest().getShopCode());
			if (null == isValidShopCode) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.mfs.get.upload.shopCode.required")));
			}
		}

		ProductDTO product = CacheManager.Product().findProductById(upload.getRequest().getProductId());
		if (null == product) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.productId.required")));
		}
		if (null == product.getMinLoanAmount()) {
			product.setMinLoanAmount(new BigDecimal(
					CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_DEFAULT_MIN_LOAN_AMOUNT)));
		}

		if (null == product.getMaxLoanAmount()) {
			product.setMaxLoanAmount(new BigDecimal(
					CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_DEFAULT_MAX_LOAN_AMOUNT)));
		}

		if (null == product.getMinTenor() || null == product.getMaxTenor()) {
			throw new ValidationException(Messages.getString("validation.field.incorrect",
					Labels.getString("label.mfs.get.checklist.productId.required")));
		}

		// validate loan amount
		if (null == upload.getRequest().getLoanAmount()) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.loanAmount.required")));
		} else if (upload.getRequest().getLoanAmount().compareTo(product.getMinLoanAmount()) == -1
				|| upload.getRequest().getLoanAmount().compareTo(product.getMaxLoanAmount()) == 1) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.loanAmount.required")));
		}

		// validate loan tenor
		if (null == upload.getRequest().getLoanTenor()) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.upload.loanTenor.required")));
		} else if (upload.getRequest().getLoanTenor().compareTo(product.getMinTenor()) == -1
				|| upload.getRequest().getLoanTenor().compareTo(product.getMaxTenor()) == 1) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.loanTenor.required")));
		}

		// validate product Name
		if (ValidateCreateCase.HAS_CHECK_CAT.value()
				.equals(CacheManager.Product().findProductById(upload.getRequest().getProductId()).getIsCheckCat())) {
			// validate issuePlace
			if (com.mcredit.util.StringUtils.isNullOrEmpty(upload.getRequest().getIssuePlace())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.checklist.issueplace.required")));
			}

			if (com.mcredit.util.StringUtils.isNullOrEmpty(upload.getRequest().getCompanyTaxNumber())) {
				throw new ValidationException(Messages.getString("validation.field.madatory",
						Labels.getString("label.mfs.get.upload.taxNumber.required")));
			}
			if (upload.getRequest().getCompanyTaxNumber().length() > ValidateCreateCase.MAX_LENGTH_COMPANY_TAX_NUMBER
					.intValue()) {
				throw new ValidationException(Messages.getString("validation.field.length",
						Labels.getString("label.mfs.get.upload.taxNumber.required"),
						ValidateCreateCase.MAX_LENGTH_COMPANY_TAX_NUMBER.intValue()));
			}
			CategoryDTO category = CacheManager.CompanyCache()
					.checkCategoryById(upload.getRequest().getCompanyTaxNumber());
			if (null == category) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.mfs.get.upload.taxNumber.required")));
			}
		} else {
			upload.getRequest().setIssuePlace("");
			upload.getRequest().setCompanyTaxNumber("");
		}

	}

	private boolean compareDate(String dateCitizen) throws ValidationException {
		try {
			Date dc = sdf.parse(dateCitizen);
			Date date = new Date();
			if (dc.compareTo(date) > 0) {
				return false;
			}
			return true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.issueDateCitizen.required")));
		}
	}

	public void validateGetCases(SearchCaseDTO searchCaseDTO, UserDTO currentUser) throws ValidationException {

		this.validateSaleCode(currentUser);

		if (searchCaseDTO.getKeyword() == null) {
			throw new ValidationException(Messages.getString("mfs.get.keyword.required"));
		}

		boolean isPageNumberValid = (searchCaseDTO.getPageNumber() != null && searchCaseDTO.getPageNumber() > 0);
		if (!isPageNumberValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}

		boolean isPageSizeValid = (searchCaseDTO.getPageSize() != null && searchCaseDTO.getPageSize() > 0);
		if (!isPageSizeValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagesize.required"));
		}

		String status = searchCaseDTO.getStatus();
		boolean isStatusValid = (status != null
				&& (status.equals(CaseStatusEnum.ABORT.value()) || status.equals(CaseStatusEnum.PROCESSING.value())));
		if (!isStatusValid) {
			throw new ValidationException(Messages.getString("mfs.getcases.status.required"));
		}
	}

	public void validateGetNotifications(SearchNotiDTO searchNotiDTO, UserDTO currentUser) throws ValidationException {

		this.validateSaleCode(currentUser);

		if (searchNotiDTO.getKeyword() == null) {
			throw new ValidationException(Messages.getString("mfs.get.keyword.required"));
		}

		boolean isPageNumberValid = (searchNotiDTO.getPageNumber() != null && searchNotiDTO.getPageNumber() > 0);
		if (!isPageNumberValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagenumber.required"));
		}

		boolean isPageSizeValid = (searchNotiDTO.getPageSize() != null && searchNotiDTO.getPageSize() > 0);
		if (!isPageSizeValid) {
			throw new ValidationException(Messages.getString("mfs.get.pagesize.required"));
		}
	}

	public void validateDownload(Long documentId, UnitOfWork uok, UserDTO currentUser) throws ValidationException {

		boolean isValid = (documentId != null && documentId > 0);
		if (!isValid) {
			throw new ValidationException(Messages.getString("mfs.dowload.id.valid"));
		}

		PdfDTO file = uok.mobile.creditAppDocumentRepo().getPdfFileById(documentId);
		if (file == null) {
			throw new ValidationException(Messages.getString("mfs.dowload.id.exists"));
		}

		this.validateSaleCode(currentUser);

		boolean isUserOwnerFile = uok.mobile.creditAppDocumentRepo().isSaleOwnerFile(documentId,
				currentUser.getEmpCode());
		if (!isUserOwnerFile) {
			throw new ValidationException(Messages.getString("mfs.dowload.permission.required"));
		}

	}

	public void validateSaleCode(UserDTO currentUser) throws ValidationException {
		if (StringUtils.isNullOrEmpty(currentUser.getEmpCode())) {
			throw new ValidationException(Messages.getString("mfs.common.salecode.required"));
		}
	}

	public void validateCancel(CancelCaseDTO ccDTO) throws ValidationException {
		if (null == ccDTO.getId()) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.caseId.required")));
		}
		if (StringUtils.isNullOrEmpty(ccDTO.getComment())) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.comment.required")));
		}
		if (null == ccDTO.getReason()) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.checklist.reason.required")));
		}

		if (!ccDTO.getReason().equals(ValidateCreateCase.MFS_CUST_NOT_NEED_BORROW.longValue())
				&& !ccDTO.getReason().equals(ValidateCreateCase.MFS_OTHER_REASON.longValue())) {
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.checklist.reason.required")));
		}
	}

	public void validateNofiticationId(UpdateNotificationIdDTO updateNotificationIdDTO) throws ValidationException {
		if (StringUtils.isNullOrEmpty(updateNotificationIdDTO.getNotificationId())) {
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.post.notificationId.required")));
		}
	}
	
	public void validateCheckCategory(String taxNumber) throws ValidationException {
		if(StringUtils.isNullOrEmpty(taxNumber))
			throw new ValidationException(Messages.getString("validation.field.madatory",Labels.getString("label.mfs.get.upload.taxNumber.required")));
		if (taxNumber.length() > 14)
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",Labels.getString("label.mfs.get.upload.taxNumber.required")));
	}
}
