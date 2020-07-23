package com.mcredit.business.mobile.aggregate;

import java.sql.Clob;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.mcredit.business.mobile.utils.CaseUtils;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.object.mobile.dto.CaseDTO;
import com.mcredit.model.object.mobile.dto.DashboardInfoDTO;
import com.mcredit.model.object.mobile.dto.PdfDTO;
import com.mcredit.model.object.mobile.dto.ReasonDTO;
import com.mcredit.model.object.mobile.dto.SearchCaseDTO;
import com.mcredit.model.object.mobile.enums.CaseStatusEnum;
import com.mcredit.sharedbiz.dto.UserDTO;

public class CaseAggregate {
	private UnitOfWork uok = null;
	private UserDTO currentUser = null;
	


	public CaseAggregate(UnitOfWork uok, UserDTO user) {
		this.uok = uok;
		this.currentUser = user;
	}
	
	/**
	 * Service: get total processing cases and total aborted cases
	 * 
	 * @author hoanx.ho
	 * @editor dongtd.ho
	 * @param 
	 * @return case processing number, case abort number 
	 * @throws Exception
	 */
	public DashboardInfoDTO getDashboardInfo() {
		String loginId = this.currentUser.getLoginId();
		DashboardInfoDTO dashboardInfo = this.uok.mobile.caseRepo().getDashboardInfo(loginId);
		return dashboardInfo;
	}
	
	
	/**
	 * Service get case list
	 * 
	 * @author hoanx.ho
	 * @param status (processing or abort), keyword, page number, page size
	 * @return cases processing or abort
	 * @throws Exception
	 */
	public List<CaseDTO> getCases(SearchCaseDTO searchCaseDTO) throws Exception {
		String loginId = this.currentUser.getLoginId();
		List<CaseDTO> cases =  this.uok.mobile.caseRepo().getCase(searchCaseDTO, loginId);
		
		boolean isGetCaseReturn = CaseStatusEnum.ABORT.value().equals(searchCaseDTO.getStatus());
		this.populateData(cases, isGetCaseReturn);
		
		return cases;
	}
	
	
	/**
	 * Add more data for case (pdf files, reason)
	 * 
	 * @author hoanx.ho
	 * @param list cases, boolean is get case return
	 * @return
	 * @throws Exception
	 */
	private void populateData(List<CaseDTO> cases, boolean isGetCaseReturn) throws Exception {
		
		List<PdfDTO> pdfFiles = null;
		List<ReasonDTO> reasons = null;
		
		for (CaseDTO c: cases) {
			
			this.handleGetCheckList(c);
			
			if (isGetCaseReturn) { 
				
				reasons = c.getCreditAppId() != null ? this.getReasonsByCreditAppId(c.getCreditAppId()) : null ;
				if (reasons != null) {
					c.setReasons(reasons);
				}
				
				pdfFiles = c.getId() != null ? this.getPdfFilesByUplCreditAppRequestId(c.getId()) : null;
				if (pdfFiles != null) {
					c.setPdfFiles(pdfFiles);
				}
			}
			
		}
	}
	
	/**
	 * Add checklist for case
	 * 
	 * @author hoanx.ho
	 * @param case
	 * @return
	 * @throws Exception
	 */
	private void handleGetCheckList(CaseDTO c) throws SQLException {
		String checkList = "";
		Clob cl = c.getcLobChecklist();
		
		if (cl != null) {
			checkList = cl.getSubString(1, (int) cl.length());
			c.setChecklist(checkList);
		}
		c.setcLobChecklist(null);
	}
	
	/**
	 * Get reasons for returned case
	 * 
	 * @author hoanx.ho
	 * @param credit app id of case
	 * @return list reason
	 * @throws Exception
	 */
	private List<ReasonDTO> getReasonsByCreditAppId(Long creditAppId) throws Exception {
		if (creditAppId == null) {
			throw new Exception("creditAppId can not be null");
		}
		
		return  this.uok.mobile.creditAppTrailRepo().getReasonsByCreditAppId(creditAppId);
	}
	
	/**
	 * Get pdf files for case
	 * 
	 * @author hoanx.ho
	 * @param id of case
	 * @return list pdf file
	 * @throws Exception
	 */
	private List<PdfDTO> getPdfFilesByUplCreditAppRequestId(Long uplCreditAppRequestId) throws Exception {
		
		if (uplCreditAppRequestId == null) {
			throw new Exception("uplCreditAppRequestId can not be null");
		}
		
		List<PdfDTO> files = null;
		files =  this.uok.mobile.creditAppDocumentRepo().getPdfFilesByUplCreditAppId(uplCreditAppRequestId);
		files = this.filterPdfFiles(files);
		return files;
	}
	
	
	/**
	 * Add file name, remove file path
	 * 
	 * @author hoanx.ho
	 * @param pdf file
	 * @return pdf file after filter
	 * @throws Exception
	 */
	private List<PdfDTO> filterPdfFiles(List<PdfDTO> files) throws ParseException {
		List<PdfDTO> fiterFiles = files;
		String fileName = "";
		
		for (int i = 0; i < fiterFiles.size(); i++) {
			PdfDTO currentFile = fiterFiles.get(i);
			fileName = CaseUtils.getFileNameByPath(currentFile.getRemotePathServer());
			currentFile.setFileName(fileName);
			
			// Hidden remote path send to client
			currentFile.setRemotePathServer("");
			
		}
		
		return fiterFiles;
	}
	
	
}
