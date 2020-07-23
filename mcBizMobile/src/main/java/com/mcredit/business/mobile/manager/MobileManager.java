package com.mcredit.business.mobile.manager;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.mcredit.business.mobile.aggregate.MobileAggregate;
import com.mcredit.business.mobile.factory.MobileFactory;
import com.mcredit.business.mobile.validation.MobileValidation;
import com.mcredit.common.Messages;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.model.dto.KioskDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.ValidateCreateCase;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.model.object.mobile.UploadDocumentDTO;
import com.mcredit.model.object.mobile.dto.CancelCaseDTO;
import com.mcredit.model.object.mobile.dto.CaseDTO;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.model.object.mobile.dto.DashboardInfoDTO;
import com.mcredit.model.object.mobile.dto.NoteDto;
import com.mcredit.model.object.mobile.dto.NotificationDTO;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.model.object.mobile.dto.UpdateNotificationIdDTO;
import com.mcredit.model.object.mobile.dto.UploadDocumentReturnDTO;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class MobileManager extends BaseManager {


	private UserDTO _user;
	private MobileAggregate _mobileAgg = null;
	private MobileValidation _mobileValid = new MobileValidation();
	
	public MobileManager() {
		_mobileAgg =  MobileFactory.getMobileAggregate(this.uok);
	}
	
	public MobileManager(UserDTO user) {
		_user = user;
		_mobileAgg =  MobileFactory.getMobileAggregate(this.uok);
	}
	
	/**
	 * Function get dashboard information
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return case processing number, case abort number 
	 * @throws Exception
	 */
	public DashboardInfoDTO getDashboardInfo() throws Exception {
		
		return this.tryCatch( () -> {
			_mobileValid.validateSaleCode(this._user);
			return MobileFactory.getCaseAggreagate(this.uok, this._user).getDashboardInfo();
		});
	}
	
	/**
	 * Function get cases
	 * 
	 * @author hoanx.ho
	 * @param status (processing or abort), keyword, page number, page size
	 * @return cases processing or abort
	 * @throws Exception
	 */
	public List<CaseDTO> getCases(SearchCaseDTO searchCaseDTO) throws Exception {
		
		return this.tryCatch(() -> {
			_mobileValid.validateGetCases(searchCaseDTO, this._user);
			return MobileFactory.getCaseAggreagate(this.uok, this._user).getCases(searchCaseDTO);
		});
	}
	
	/**
	 * @author sonhv.ho
	 * routeCaseBPM Job : fixedDelay = 10 minute
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong ho so route thanh cong
	 * RACCaseResultDTO.failCount: So luong ho so route that bai
	 * RACCaseResultDTO.total: Tong so ho so route
	 * @throws Exception
	 */
	public RACCaseResultDTO routeCaseBPM() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.routeCaseBPM();
		});
	}
	
	
	/**
	 * Get products
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list product effective
	 * @throws Exception
	 */
	public List<ProductDTO> getProducts() throws Exception {
		return this.tryCatch(() -> {
			return MobileFactory.getCommonAggreagate(this.uok).getProducts();
		});
	}
	
	/**
	 * Get kiosks
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list kiosk
	 * @throws Exception
	 */
	public List<KioskDTO> geKiosks() throws Exception {
		return this.tryCatch(() -> {
			return MobileFactory.getCommonAggreagate(this.uok).getKiosks();
		});
	}
	
	/**
	 * @author sonhv.ho
	 * updateTokenMobile Job : fixedDelay = 24 hours
	 * @return ExternalUserMapping List
	 * ExternalUserMapping.accessToken
	 * ExternalUserMapping.refreshToken
	 * @throws Exception
	 */
	public List<ExternalUserMapping> updateTokenMobile() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.updateTokenMobile();
		});
	}
	
	/**
	 * Get notifications
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list notifications
	 * @throws Exception
	 */
	public List<NotificationDTO> getNotifications(SearchNotiDTO searchDTO) throws Exception {
		return this.tryCatch(() -> {
			_mobileValid.validateGetNotifications(searchDTO, this._user);
			return MobileFactory.getCommonAggreagate(this.uok).getNotifications(searchDTO, this._user);
		});
	}
	
	/**
	 * Check category of company
	 * @author cuongvt.ho
	 * @param companyTaxNumber thong tin ma so thue cua cong ty
	 * @return Company infomation
	 * @throws Exception
	 */
	public CategoryDTO checkCategory(String companyTaxNumber) throws Exception {
		return this.tryCatch(()-> {
			_mobileValid.validateCheckCategory(companyTaxNumber);
			CategoryDTO dto = MobileFactory.getCommonAggreagate(this.uok).getCompanyInfo(companyTaxNumber);
			if(dto != null) return dto;
			else throw new ValidationException(Messages.getString("mfs.check.cat.false"));
		});
	}
	
	/**
	 * Get case note
	 * @author cuongvt.ho
	 * @param appNumber appNumber cua ho so ben BPM
	 * @return return List case note
	 * @throws Exception
	 */
	public Object getCaseNote(String appNumber) throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.getCaseNote(appNumber, _user);
		});
	}
	
	/**
	 * Send case note
	 * @author cuongvt.ho
	 * @param noteDto gom co appID cua ho so sinh ra tu BPM va noi dung cua note
	 * @return return message success
	 * @throws Exception
	 */
	public ResponseSuccess sendCaseNote(NoteDto noteDto) throws Exception {
		return this.tryCatch(() -> {
			_mobileValid.validateCaseNote(noteDto);
			return _mobileAgg.sendCaseNote(noteDto, _user);
		});
	}
	
	/**
	 * @author sonhv.ho
	 * createCaseToBPM Job : fixedDelay = 10 minute
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong ho so create thanh cong
	 * RACCaseResultDTO.failCount: So luong ho so create that bai
	 * RACCaseResultDTO.total: Tong so ho so create
	 * @throws Exception
	 */
	public RACCaseResultDTO createCaseToBPM() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.createCaseToBPM();
		});
	}
	
	/**
	 * Lay danh sach checklist anh tu BPM
	 * 
	 * @author linhtt2.ho
	 * @param mobileSchemaProductCode: ma san pham
	 * @param mobileTemResidence: dia chi song trung voi ho khau
	 * @return danh sach anh checklist can thiet tuong ung
	 * @throws Exception
	 */
	public String checklist(String mobileSchemaProductCode, String mobileTemResidence) throws Exception {

		return this.tryCatch(() -> {
			_mobileValid.validateCheckList(mobileSchemaProductCode, mobileTemResidence);

			return _mobileAgg.checklist(mobileSchemaProductCode, mobileTemResidence);
		});
	}

	/**
	 * Tao moi ho so
	 * 
	 * @author linhtt2.ho
	 * @param uploadedInputStream: luong stream file .zip gui ve
	 * @param payload: thong tin ho so gui len he thong
	 * @param fileName: ten file .zip gui len he thong
	 * @return ket qua tao lenh thanh cong
	 * @throws Exception: loi xay ra trong qua trinh tao moi ho so
	 */
	public UploadDocumentReturnDTO createCase(InputStream uploadedInputStream, String payload, String fileName) throws Exception {
		return this.tryCatch(() -> {
			// validate payload + checksum - tra ve danh sach upload
			UploadDocumentDTO upload = _mobileValid.validateCreateCase(payload, _user, uploadedInputStream, fileName);
			if (upload.getAppStatus() == ValidateCreateCase.NEW_CASE.intValue()) {
				// validate new case: cac truong co ton tai trong he thong khong
				MobileFactory.getMobileAggregate(this.uok).validateCheckList(upload);
			} else {
				// kiem tra ma ho so day len co trang thai BPM_RETURN_UPLOADED khong
				MobileFactory.getMobileAggregate(this.uok).validateReturnCase(upload);
			}
			return MobileFactory.getMobileAggregate(this.uok).createCase(upload,fileName,_user);
		});

	}
	
	
	/**
	 * Download file pdf
	 * 
	 * @author hoanx.ho
	 * @param document id
	 * @return file pdf
	 * @throws Exception
	 */
	public File downloadFile(Long documentId) throws Exception {
		
		return this.tryCatch(() -> {
			_mobileValid.validateDownload(documentId, this.uok, this._user);
			
			return MobileFactory.getCommonAggreagate(this.uok).downloadFile(documentId);
		});
	}
	
	/**
	 * Job huy case
	 * 
	 * @author cuongvt.ho
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong case huy thanh cong.
	 * RACCaseResultDTO.failCount: So case huy that bai.
	 * RACCaseResultDTO.total: Tong so case can huy.
	 * @throws Exception
	 */
	public RACCaseResultDTO abortCase() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.abortCase();
		});
	}

	/**
	 * Service huy ho so tra ve
	 * 
	 * @author linhtt2.ho
	 * @param CancelCaseDTO
	 * @return success
	 * @throws Exception 
	 */
	public ResponseSuccess cancelCase(CancelCaseDTO ccDTO) throws Exception {
		
		return this.tryCatch(() -> {
			_mobileValid.validateCancel(ccDTO);
			
			return _mobileAgg.cancelCase(ccDTO,_user);
		});
	}
	
	/**
	 * Job sync file to FTP
	 * 
	 * @author cuongvt.ho
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong case sync thanh cong.
	 * RACCaseResultDTO.failCount: So case sync that bai.
	 * RACCaseResultDTO.total: Tong so case can sync.
	 * @throws Exception
	 */
	public RACCaseResultDTO syncPdfToFTPServer() throws Exception {
		return this.tryCatch(() -> {
			 return _mobileAgg.syncFileToFTP();
		});
	}

	/**
	 * @author sonhv.ho
	 * sendNotificationForSale Job : fixedDelay = 5 minute
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong gui thong bao thanh cong.
	 * RACCaseResultDTO.failCount: So luong gui thong bao that bai.
	 * RACCaseResultDTO.total: Tong so thong bao can gui di.
	 * @throws Exception
	 */
	public RACCaseResultDTO sendNotificationForSale() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.sendNotificationForSale();
		});
	}
	
	/**
	 * @author linhtt2.ho
	 * sendNotificationForThirdParty Job : fixedDelay = 5 minute
	 * @return RACCaseResultDTO
	 * RACCaseResultDTO.successCount: So luong gui thong bao thanh cong.
	 * RACCaseResultDTO.failCount: So luong gui thong bao that bai.
	 * RACCaseResultDTO.total: Tong so thong bao can gui di.
	 * @throws Exception
	 */
	public RACCaseResultDTO sendNotificationForThirdParty() throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.sendNotificationForThirdParty();
		});
	}
	
	/**
	 * Lấy danh sách báo cáo
	 * @param dateExport ngày lấy danh sách báo cáo
	 * @param user UserDTO của người dùng
	 * @return list danh sách báo cáo
	 * @throws Exception
	 */
	public List<DataReport> getReport(String dateExport) throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.getReport(dateExport, _user);
		});
	}
	
	/**
	 * Lấy danh sách báo cáo phê duyệt
	 * @param dateExport ngày lấy danh sách báo cáo phê duyệt
	 * @param user UserDTO của người dùng
	 * @return list danh sách báo cáo phê duyệt
	 * @throws Exception
	 */
	public List<ApprovalReport> getApprovalReport(String dateExport) throws Exception {
		return this.tryCatch(() -> {
			return _mobileAgg.getApprovalReport(dateExport, _user);
		});
	}

	/**
	 * update NotificationId
	 * 
	 * @author sonhv.ho
	 * @param 
	 * @return 1L
	 * @throws Exception
	 */
	public Integer updateNotificationId(UpdateNotificationIdDTO updateNotificationIdDTO) throws Exception {
		return this.tryCatch(() -> {
			_mobileValid.validateNofiticationId(updateNotificationIdDTO);
			return _mobileAgg.updateNotificationId(updateNotificationIdDTO, _user);
		});
	}
}
