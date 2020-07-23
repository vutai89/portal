package com.mcredit.business.telesales.manager;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.mcredit.business.telesales.aggregate.UploadAggregate;
import com.mcredit.business.telesales.background.UploadBackground;
import com.mcredit.business.telesales.background.UploadBackgroundCustom;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.telesales.ImportStatusDTO;
import com.mcredit.model.dto.telesales.ImportStatusTSADTO;
import com.mcredit.model.dto.telesales.UploadFileDTO;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.sharedbiz.BusinessLogs;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.FileHelper;
import com.mcredit.util.JSONConverter;

public class UploadManager extends BaseManager{

	
	private UserDTO _user;
	
	public UploadManager(UserDTO user){
		_user = user;
	}

	public Object importCusomterProspect(UploadFileDTO UFDTO) throws Exception {
		
		return this.tryCatch(()->{
			
			TelesalesFactory.getUploadValidation().validateUpload(UFDTO);
			
			if(!this._user.isSuperVisor())
				throw new PermissionException(Messages.getString("unauthorized"));
			
			UploadAggregate uplAgg = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
			
			String FileUUID = UUID.randomUUID().toString();
					
			ImportStatusDTO IMPDTO = null;
			try {
				IMPDTO = uplAgg.importStatus(this._user.getLoginId());
			} catch (Exception ex) {
				IMPDTO = null;
			}

			if (IMPDTO != null)
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.another.import.pending")));
			
			/*if (UFDTO.getFileContent() == null)
				throw new ValidationException("No file uploaded!");*/
			
			UplMaster UM = null;

			if (UFDTO.getUplCode() != null)
				UM = uplAgg.getUploadMasterByUplCode(UFDTO.getUplCode());
			
			CodeTableDTO source = CacheManager.CodeTable().getCodeByCategoryCodeValue1("UPL_SRC", !"".equals(UFDTO.getMbMis())?"MB":"TELESALE");
			if(source == null)
				throw new ValidationException(Messages.getString("validation.field.notFound", "UPL SRC"));
			
			String fileExt = "." + FilenameUtils.getExtension(UFDTO.getUserFileName());
			if (UM == null) {	
				
				if (UFDTO.getUplType() == null)
					throw new ValidationException(Messages.getString("validation.field.madatory", "Upl Type"));
				
				CodeTableDTO uplType = CacheManager.CodeTable().getCodeByCategoryCodeValue1("UPL_TYPE",UFDTO.getUplType());
				if(uplType == null)
					throw new ValidationException(Messages.getString("validation.field.notFound", "UPL TYPE"));
				
				if("PC".equalsIgnoreCase(uplType.getCodeValue1()) && UFDTO.getOwnerId() == null)
					throw new ValidationException(Messages.getString("validation.field.madatory", "Owner Id"));

				UM = new UplMaster();
				UM.setCreatedBy(this._user.getLoginId());
				UM.setOwnerId("GC".equalsIgnoreCase(uplType.getCodeValue1()) ?  this._user.getId().intValue() : UFDTO.getOwnerId());
				UM.setUplFormat(fileExt);
				UM.setUplType(uplType.getId());
				UM.setUplCode(UFDTO.getUplCode());
				UM.setFromSource(new Long(source.getId()));
				UM.setRecordStatus("A");
				UM.setImported(0L);
				UM.setTotalAllocated(0L);
				uplAgg.upsertUplMaster(UM);
			}

			UplDetail UD = new UplDetail();
			UD.setUplMasterId(UM.getId());
			UD.setUplFileName(UFDTO.getUserFileName());
			UD.setImported(0);
			UD.setTotalAllocated(0);
			UD.setStatus("A");
			
			String ServerFilename = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY) + DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt;
			//String ServerFilename = "E:\\TMP\\telesale_upload" +  DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt ;
//			String ServerFilename = "D:\\telesale_upload\\" +  DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt ;
			System.out.println(ServerFilename);			
			
			File targetFile = new File(ServerFilename);
			FileHelper.copyInputStreamToFile(UFDTO.getFileContent(),targetFile);

			UD.setServerFileName(targetFile.getCanonicalPath());
			UD.setRecordStatus(RecordStatus.ACTIVE.value());
			UD.setUplSeq(uplAgg.getNextUplDetailSeq(UM.getId()));
			UD.setTotalAllocated(0);
			UD.setStatus("N");
			UD.setCreatedBy(this._user.getLoginId());
			UD.setLastUpdatedBy(this._user.getLoginId());
			
			/* Fix reset imported & totalAllocated */
			UM.setImported(0L);
			UM.setTotalAllocated(0L);
			
			UM.setRecordStatus("A");
			uplAgg.setUpLoadDetail(UD);
			uplAgg.upsertUploadDetail();

			UploadBackground UB = new UploadBackground(UD);
			UB.start();

			return new IdDTO(UD.getId());
			
		});

	}

	/*private ImportStatusDTO getUDStatus(UplDetail UD) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getUploadAggregateInstance(uok.telesale).getUDStatus(UD);
		});
	}*/

	public String importStatus() throws Exception {
		return this.tryCatch(()->{
			try {
				return JSONConverter.toJSON(TelesalesFactory.getUploadAggregateInstance(uok.telesale).importStatus(this._user.getLoginId()));
			} catch (Exception e) {
				return "{}";
			}
		});
	}

	public Object confirmUpload(Long uplDetailId) throws Exception {
		
		return this.tryCatch(()->{
			
			TelesalesFactory.getUploadValidation().validateObjectUpload(uplDetailId);
			
			/*if(UplDetailId == null)
				throw new ValidationException("Upl Detail Id is required.");*/
		
			ImportStatusDTO ipm = null ;
			try{
				 ipm = TelesalesFactory.getUploadAggregateInstance(uok.telesale).importStatus(this._user.getLoginId()) ;
			}catch (Exception e) {
				System.out.println(e);
			}
			
			if(ipm != null)
				TelesalesFactory.getUploadAggregateInstance(uok.telesale).confirmUpload(uplDetailId , ipm.getDupCurrentData() + ipm.getDupOldData() + ipm.getInvalidData());
			else 
				TelesalesFactory.getUploadAggregateInstance(uok.telesale).confirmUpload(uplDetailId , 0);
			
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).deleteInvalidCustomerByUplDetailId(uplDetailId);
			
			return new ResponseSuccess();
		});
		
	}
	
	public Object updateData(Long uplDetailId) throws Exception {
		
		return this.tryCatch(()->{
			
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).updateImportedAndAllocatedUplMaster(uplDetailId);
			
			return new ResponseSuccess();
		});
		
	}

	/*public Object importStatus(Long uplDetailId) throws Exception {
		
		return this.tryCatch(()->{
			
			TelesalesFactory.getUploadValidation().validateObjectUpload(uplDetailId);
			
			UploadAggregate UA = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
			
			UplDetail UD = UA.getUplDetailById(uplDetailId);
			
			return getUDStatus(UD);
		});		
	}*/

	public Object deleteUpload(Long uplDetailId) throws Exception {
		
		return this.tryCatch(()->{
			
			TelesalesFactory.getUploadValidation().validateObjectUpload(uplDetailId);
			
			UploadAggregate UA = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
			
			UplDetail UD = UA.getUplDetailById(uplDetailId);
			if (UD == null)
				throw new ValidationException(Messages.getString("validation.not.exists", "Upl Detail"));
			
			UD.setStatus("R");
			UA.setUpLoadDetail(UD);
			UA.upsertUploadDetail();
			UA.deleteUplCustomerByUplDetail(UD.getId());
			
			// For remote errror Imported
			/*if(errorNum!= null && !errorNum.equals(0)){
				UplMaster uplMaster = UA.findUplMasterbyID(uplDetailId);
				uplMaster.setImported(new Long(uplMaster.getImported() - errorNum));
				uok.telesale.uplMasterRepository().merge(uplMaster);	
			}*/
			
			return new ResponseSuccess();
		});
		
	}
	
	public Object importProspectTsa(UploadFileDTO UFDTO) throws Exception {
		
		return this.tryCatch(()->{
			System.out.println(new BusinessLogs().writeLog("--Start importProspectTsa --"));
			TelesalesFactory.getUploadValidation().validateUpload(UFDTO);
			
//			if(!this._user.isSuperVisor())
//				throw new PermissionException(Messages.getString("unauthorized"));
			
			UploadAggregate uplAgg = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
			UploadAggregate uplAggComm = TelesalesFactory.getUploadAggregateInstanceCustom(uok.common);
			
			String FileUUID = UUID.randomUUID().toString();
					
			ImportStatusDTO IMPDTO = null;
			try {
				IMPDTO = uplAgg.importStatus(this._user.getLoginId());
			} catch (Exception ex) {
				IMPDTO = null;
			}

			if (IMPDTO != null) 
				return new ImportStatusTSADTO(IMPDTO.getUplDetailId(), 
						StringUtils.isNotBlank(uplAgg.getUplCodeByDetailId(IMPDTO.getUplDetailId())) ? uplAgg.getUplCodeByDetailId(IMPDTO.getUplDetailId()) : "0");
//				throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.another.import.pending")));
			
			/*if (UFDTO.getFileContent() == null)
				throw new ValidationException("No file uploaded!");*/
			
			UplMaster um = null;

			if (UFDTO.getUplCode() != null)
				um = uplAgg.getUploadMasterByUplCode(UFDTO.getUplCode());
			
			CodeTableDTO source = CacheManager.CodeTable().getCodeByCategoryCodeValue1("UPL_SRC", "TELESALE");
			if(source == null)
				throw new ValidationException(Messages.getString("validation.field.notFound", "UPL SRC"));
			
			String fileExt = "." + FilenameUtils.getExtension(UFDTO.getUserFileName());
			if (um == null) {	
				
				if (UFDTO.getUplType() == null)
					throw new ValidationException(Messages.getString("validation.field.madatory", "Upl Type"));
				
				CodeTableDTO uplType = CacheManager.CodeTable().getCodeByCategoryCodeValue1("UPL_TYPE",UFDTO.getUplType());
				if(uplType == null)
					throw new ValidationException(Messages.getString("validation.field.notFound", "UPL TYPE"));
				
				if("PC".equalsIgnoreCase(uplType.getCodeValue1()) && UFDTO.getOwnerId() == null)
					throw new ValidationException(Messages.getString("validation.field.madatory", "Owner Id"));

				um = new UplMaster();
				um.setCreatedBy(this._user.getLoginId());
				um.setOwnerId(UFDTO.getOwnerId());
				um.setUplFormat(fileExt);
				um.setUplType(uplType.getId());
				um.setUplCode(UFDTO.getUplCode());
				um.setFromSource(new Long(source.getId()));
				um.setRecordStatus("A");
				um.setImported(0L);
				um.setTotalAllocated(0L);
				uplAgg.upsertUplMaster(um);
				System.out.println(new BusinessLogs().writeLog("Insert UplMaster: " + new Gson().toJson(um)));
			}

			UplDetail ud = new UplDetail();
			ud.setUplMasterId(um.getId());
			ud.setUplFileName(UFDTO.getUserFileName());
			ud.setImported(0);
			ud.setTotalAllocated(0);
			ud.setStatus("A");
			
			String serverFilename = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY) + DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt;
			//String serverFilename = "E:\\TMP\\telesale_upload" +  DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt ;
//			String serverFilename = "D:\\telesale_upload\\" +  DateUtil.toString(new Date(), "yyyyMMdd") + "-" + FileUUID + fileExt ;
			System.out.println(new BusinessLogs().writeLog("File name: " + serverFilename));			
			
			File targetFile = new File(serverFilename);
			FileHelper.copyInputStreamToFile(UFDTO.getFileContent(),targetFile);

			ud.setServerFileName(targetFile.getCanonicalPath());
			ud.setRecordStatus(RecordStatus.ACTIVE.value());
			ud.setUplSeq(uplAgg.getNextUplDetailSeq(um.getId()));
			ud.setTotalAllocated(0);
			ud.setStatus("N");
			ud.setCreatedBy(this._user.getLoginId());
			ud.setLastUpdatedBy(this._user.getLoginId());
			
			um.setImported(0L);
			um.setTotalAllocated(0L);
			
			um.setRecordStatus("A");
			uplAgg.setUpLoadDetail(ud);
			uplAgg.upsertUploadDetail();
			System.out.println(new BusinessLogs().writeLog("Insert UplDetail request: " + new Gson().toJson(ud)));
			
			List<AllocationMaster> aList = uplAggComm.findAllocationMaster(um.getId(), "T");
			AllocationMaster am = new AllocationMaster();
			am.setRecordStatus("A");
			am.setCreatedDate(new Date());
			am.setLastUpdatedDate(new Date());
			am.setCreatedBy(this._user.getLoginId());
			am.setLastUpdatedBy(this._user.getLoginId());
			am.setUplMasterId(um.getId());
			am.setAllocatedNumber(0);
			CodeTableDTO allo = CacheManager.CodeTable().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM");
			am.setAllocatedTo((null != allo) ? Long.valueOf(allo.getId()) : null);
			am.setAllocatedType("T");
			if(!CollectionUtils.isEmpty(aList)) {
				am.setAllocatedSeq(uplAggComm.buidAllocatedSeq(um.getId(), "T"));
			} else am.setAllocatedSeq(1);
			uplAggComm.insertAllocationMaster(am);
			System.out.println(new BusinessLogs().writeLog("Insert AllocationMaster request: " + new Gson().toJson(am)));
			
			UploadBackgroundCustom UB = new UploadBackgroundCustom(ud, am, _user);
			UB.start();
			
			System.out.println(new BusinessLogs().writeLog("--End importProspectTsa return UplDetailId = " + ud.getId()));
			return new IdDTO(ud.getId());
			
		});
	}
	
	public Object confirmUploadTsa(Long uplDetailId) throws Exception {
		return this.tryCatch(()->{
			
			System.out.println(new BusinessLogs().writeLog("--Start confirmUploadTsa uplDetailId: " + uplDetailId));
			TelesalesFactory.getUploadValidation().validateObjectUpload(uplDetailId);
			
			ImportStatusDTO ipm = null ;
			try{
				 ipm = TelesalesFactory.getUploadAggregateInstance(uok.telesale).importStatus(this._user.getLoginId()) ;
			}catch (Exception e) {
				System.out.println(e);
			}
			
			if(ipm != null)
				TelesalesFactory.getUploadAggregateInstance(uok.telesale).confirmUpload(uplDetailId , ipm.getDupCurrentData() + ipm.getDupOldData() + ipm.getInvalidData());
			else 
				TelesalesFactory.getUploadAggregateInstance(uok.telesale).confirmUpload(uplDetailId , 0);
			
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).deleteInvalidCustomerByUplDetailId(uplDetailId);
			
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).deleteInvalidAllocationDetail(uplDetailId);
			
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).deleteInvalidAllocationMaster(uplDetailId);
			
			System.out.println(new BusinessLogs().writeLog("--End confirmUploadTsa--"));
			return new ResponseSuccess();
		});
		
	}
	
	public Object deleteUploadTsa(Long uplDetailId) throws Exception {
		
		return this.tryCatch(()->{
			System.out.println(new BusinessLogs().writeLog("--Start deleteUploadTsa uplDetailId: " + uplDetailId));
			TelesalesFactory.getUploadValidation().validateObjectUpload(uplDetailId);
			
			UploadAggregate ua = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
			
			UplDetail ud = ua.getUplDetailById(uplDetailId);
			if (ud == null)
				throw new ValidationException(Messages.getString("validation.not.exists", "Upl Detail"));
			
			ud.setStatus("R");
			ua.setUpLoadDetail(ud);
			ua.upsertUploadDetail();
			List<Long> amIds = ua.findListByDetailId(ud.getId());
			//delete upldetail
			ua.deleteUplCustomerByUplDetail(ud.getId());
			
			//delete allocation_detail
			ua.deleteInvalidAllocationDetail(ud.getId());
			
			//delete allocation_master
			if(!CollectionUtils.isEmpty(amIds)) ua.deleteAllocationMaster(amIds);
			
			System.out.println(new BusinessLogs().writeLog("--End deleteUploadTsa--"));
			return new ResponseSuccess();
		});
		
	}
	
	public Object confirmNextImportTSA(Long uplDetailId) throws Exception {
		return this.tryCatch(()->{
			System.out.println(new BusinessLogs().writeLog("--Start confirmNextImportTSA uplDetailId: " + uplDetailId));
			TelesalesFactory.getUploadAggregateInstance(uok.telesale).confirmNextImportTSA(uplDetailId) ;
			return new ResponseSuccess();
		});
	}
	
}
