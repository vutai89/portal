package com.mcredit.business.checkcic.manager;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.mcredit.business.checkcic.aggregate.CheckCICAggregate;
import com.mcredit.business.checkcic.validation.CheckCICValidation;
import com.mcredit.model.dto.cic.CICDetailDTO;
import com.mcredit.model.dto.cic.CICDetailForBpmDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class CheckCICManager extends BaseManager {
	private CheckCICAggregate _agg;
	private CheckCICValidation validation = new CheckCICValidation(); 

	public CheckCICManager() {
		_agg = new CheckCICAggregate(this.uok);
	}
	
	/**
	 * Create new request check cic if not exists
	 * @author catld.ho
	 * @param citizenID : cmnd
	 * @param oldCitizenID : cmnd old
	 * @param militaryID : cccd
	 * @param customerName : customer name
	 * @param user : login id
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> checkCIC(String citizenID, String oldCitizenID, String militaryID, String customerName, String user, Integer fromSource) throws Exception {
		return this.tryCatch( () -> {
			validation.checkCIC(citizenID, oldCitizenID, militaryID, customerName);
			return _agg.checkCIC(citizenID, oldCitizenID, militaryID, customerName, user, fromSource);
		});
	}
	
	/**
	 * Search CIC result
	 * @author catld.ho
	 * @param citizenID : cmnd/cccd/cmqd
	 * @param customerName
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> searchCIC(String citizenID, String customerName) throws Exception {
		return this.tryCatch( () -> {
//			validation.searchCIC(citizenID);
			return _agg.searchCIC(citizenID, customerName);
		});
	}
	
	/**
	 * Search list cic by list identity
	 * @author catld.ho
	 * @param listIdentity
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailForBpmDTO> searchListCIC(List<String> listIdentity) throws Exception {
		return this.tryCatch( () -> {
			return _agg.searchListCIC(listIdentity);
		});
	}
	
	/**
	 * CIC checker claim request check cic to process
	 * @author catld.ho
	 * @param username : login id
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> claimCICRequest(String username) throws Exception {
		return this.tryCatch( () -> {
			return _agg.claimRequest(username);
		});
	}
	
	/**
	 * 
	 * @author catld.ho
	 * @param fileContent : file cic result
	 * @param payload : cic result
	 * @param currentUser : login id
	 * @return CICDetailDTO
	 * @throws Exception
	 */
	public Object updateCIC(InputStream fileContent, String payload, String currentUser) throws Exception {
		return this.tryCatch( () -> {
			CICDetailDTO dataDetail = validation.updateCIC(fileContent, payload);
			return _agg.updateCIC(fileContent, dataDetail, currentUser);
		});
	}
	
	/**
	 * Get file cic result
	 * @author catld.ho
	 * @param identity : cmnd/cccd/cmqd
	 * @return File
	 * @throws Exception
	 */
	public File getFile(String identity) throws Exception {
		return this.tryCatch( () -> {
			validation.downloadCICResult(identity);
			return _agg.getDownloadFile(identity);
		});
	}
	
	/**
	 * Scan identity of new contract from bpm and create check cic request if not exists
	 * @author catld.ho
	 * @return success or fail
	 * @throws Exception
	 */
	public Object autoCreateCICRequest() throws Exception {
		return this.tryCatch( () -> {
			return _agg.autoCreateCICRequest();
		});
	}
	
	/**
	 * Auto Job check cic result and approve leadgen data
	 * @return
	 * @throws Exception
	 */
	public Object jobApproveLeadgenData() throws Exception {
		return this.tryCatch(() -> {
			return _agg.jobApproveLeadgenData();
		});
	}
	
	/**
	 * Báo cáo kết quả CIC sai hoặc lỗi
	 * @param citizenID
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public Object reportCICResult(String citizenID, String userName) throws Exception {
		return this.tryCatch( () -> {
			validation.reportCICResult(citizenID);
			return _agg.reportCICResult(citizenID, userName);
		});
	}
	
	/**
	 * Đồng bộ lại tên file ảnh.
	 * Đã đồng bộ xong không dùng nữa
	 * @param encode
	 * @return
	 * @throws Exception
	 */
	public Object syncFileName(String encode) throws Exception {
		return this.tryCatch( () -> {
			return _agg.syncFileName(encode);
		});
	}
	
}
