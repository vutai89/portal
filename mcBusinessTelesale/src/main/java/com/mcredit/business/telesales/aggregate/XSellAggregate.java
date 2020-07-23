package com.mcredit.business.telesales.aggregate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.mobile.object.xsell.XSellSearchInfo;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.assign.UsersDTO;
import com.mcredit.model.dto.xsell.XSellFileDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.object.ListObjectResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;
import com.mcredit.util.ZipFiles;

public class XSellAggregate {

	private UnitOfWorkTelesale unitOfWorkTelesale = null;

	public XSellAggregate(UnitOfWorkTelesale uok) {

		this.unitOfWorkTelesale = uok;
	}

	public ResponseSuccess changeCampaignStatus() throws ValidationException, Exception {
		// job nay phai chay sau 0 gio

		List<XSellFileDTO> result = unitOfWorkTelesale.uplDetailRepo().searchXSellFiles(new Date());
		CodeTableCacheManager cache = CacheManager.CodeTable();
		
		//thay doi trang thai tu ACTIVE -> INACTIVE neu ngay hien tai > ngay het hieu luc cua ban ghi
		CodeTableDTO ct = cache.getBy(CTCodeValue1.XSELL_APP_ACTIVE, CTCat.STATUS_APP_XSELL);
		CodeTableDTO inactive = cache.getBy(CTCodeValue1.XSELL_APP_IN_ACTIVE, CTCat.STATUS_APP_XSELL);
		unitOfWorkTelesale.uplDetailRepo().disableByDate(new Date(), ct.getId().longValue(),inactive.getId().longValue());
		
		if (result != null && !result.isEmpty())
			for (XSellFileDTO item : result) {
				Date fromDate = item.getValidDateFrom();
				Date toDate = item.getValidDateTo();
				Date today = DateUtil.getDateWithoutTime(new Date());
				
				ct = cache.getbyID(StringUtils.toInt(item.getStatus_app()));
				if(ct == null){
					System.out.println("Code Table is invalid: " + item.getStatus_app());
					continue;
				}
				
				CTCodeValue1 statusApp = CTCodeValue1.from(ct.getCodeValue1());
				
				// den ngay chuyen trang thai sang Active
				if (DateUtil.isSameDay(today, fromDate) && CTCodeValue1.XSELL_APP_OK == statusApp)
					ct = cache.getBy(CTCodeValue1.XSELL_APP_ACTIVE, CTCat.STATUS_APP_XSELL);				
				
				// trong khoang ngay tu fromDate den toDate neu chua chuyen trang thai thi chuyen trang thai cho no
				if (!today.after(toDate) && !today.before(fromDate) && CTCodeValue1.XSELL_APP_OK == statusApp)
					ct = cache.getBy(CTCodeValue1.XSELL_APP_ACTIVE, CTCat.STATUS_APP_XSELL);	
				
				// qua ngay chuyen trang thai thanh inactive
				if (today.after(toDate))
					ct = cache.getBy(CTCodeValue1.XSELL_APP_IN_ACTIVE, CTCat.STATUS_APP_XSELL);
				
				unitOfWorkTelesale.uplDetailRepo().changeStatus(ct.getId().toString(), item.getId());
			}

		return new ResponseSuccess();
	}

	public List<XSellFileDTO> searchXSellFiles(String statusCodeValue1, String fromDateUpload, String toDateUpload) {

		Long codeTableId = null;
		if (!StringUtils.isNullOrEmpty(statusCodeValue1)) {
			CTCodeValue1 eCodeValue1 = CTCodeValue1.from(statusCodeValue1);
			codeTableId = CacheManager.CodeTable().getIdBy(eCodeValue1, CTCat.STATUS_APP_XSELL, CTGroup.MIS);
		}

		return unitOfWorkTelesale.uplDetailRepo().searchXSellFiles(codeTableId, fromDateUpload, toDateUpload);
	}

	public List<XSellSearchInfo> searchXSellInfoBorrow(String identityNumber) {
		return unitOfWorkTelesale.uplDetailRepo().searchXSellInfoBorrow(identityNumber);
	}

	public UplDetail getUplDetailById(Long uplDetailId) {
		return unitOfWorkTelesale.uplDetailRepo().findUplDetailbyID(uplDetailId);
	}

	public Object approvalQTTR(UserDTO currentUser, Long idFile) {

		ListObjectResult result = new ListObjectResult();
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();

		Long codeTableId = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_OK, CTCat.STATUS_APP_XSELL, CTGroup.MIS);
		if (codeTableId != null) {
			int rs = unitOfWorkTelesale.uplDetailRepo().approvalUpl(currentUser.getLoginId(), codeTableId, idFile);
			if (rs > 0) {
				sucsessUpdate.add(idFile);
			} else {
				errorUpdate.add(idFile);
			}
		} else {
			errorUpdate.add(idFile);
		}

		result.setSucsessUpdate(sucsessUpdate);
		result.setErrorUpdate(errorUpdate);
		return result;
	}

	public Object refuseQTTR(UserDTO currentUser, Long idFile, String rejectReson) {

		ListObjectResult result = new ListObjectResult();
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();

		Long codeTableId = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_REJECT, CTCat.STATUS_APP_XSELL, CTGroup.MIS);
		if (codeTableId != null) {
			int rs = unitOfWorkTelesale.uplDetailRepo().refuseUpl(currentUser.getLoginId(), codeTableId, rejectReson, idFile);
			if (rs > 0) {
				sucsessUpdate.add(idFile);
			} else {
				errorUpdate.add(idFile);
			}
		} else {
			errorUpdate.add(idFile);
		}

		result.setSucsessUpdate(sucsessUpdate);
		result.setErrorUpdate(errorUpdate);
		return result;
	}

	public Object deleteFileMIS(UplDetail upl, boolean isRemove) {

		ListObjectResult result = new ListObjectResult();
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();

		int rs = 0;

		if (isRemove) {
			rs = unitOfWorkTelesale.uplDetailRepo().deleteUpl(upl.getId());
			rs= unitOfWorkTelesale.uplCustomerRepo().removeWithDetailsID(upl.getId());
			rs= unitOfWorkTelesale.uplMasterRepo().removeWithId(upl.getUplMasterId());
		} else {
			rs = unitOfWorkTelesale.uplDetailRepo().updateUpl("C", upl.getId());
			rs = unitOfWorkTelesale.uplMasterRepo().updateUpl("C", upl.getUplMasterId());
		}

		if (rs > 0) {
			ZipFiles zip = new ZipFiles();
			if (zip.deleteFile(upl.getServerFileName()))
				sucsessUpdate.add(upl.getId());
			else
				errorUpdate.add(upl.getId());
		} else {
			errorUpdate.add(upl.getId());
		}

		result.setSucsessUpdate(sucsessUpdate);
		result.setErrorUpdate(errorUpdate);
		return result;
	}

	public UplDetail getUplDetail(Long idFile) {
		try {
			return unitOfWorkTelesale.uplDetailRepo().getUplDetailbyId(idFile);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public boolean checkStatusUPLIsWait(Long uplStatus) {
		long idWait = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_WAIT, CTCat.STATUS_APP_XSELL, CTGroup.MIS);

		if (uplStatus == idWait) {
			return true;
		}
		return false;
	}

	public boolean checkStatusUPLIsRefuse(Long uplStatus) {

		long idOK = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_OK, CTCat.STATUS_APP_XSELL, CTGroup.MIS);
		long idWait = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_WAIT, CTCat.STATUS_APP_XSELL, CTGroup.MIS);

		if (uplStatus == idOK || uplStatus == idWait) {
			return true;
		}

		return false;
	}
}
