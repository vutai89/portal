package com.mcredit.business.telesales.aggregate;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jxl.Sheet;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.modelmapper.ModelMapper;
import org.w3c.dom.Document;

import com.mcredit.business.telesales.background.FileImportUtils;
import com.mcredit.business.telesales.background.ImportXsellBackground;
import com.mcredit.business.telesales.background.ImportXsellXLSBackground;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.telesales.AllocateDTO;
import com.mcredit.model.dto.telesales.ImportStatusDTO;
import com.mcredit.model.dto.telesales.UplMasterDTO;
import com.mcredit.model.dto.telesales.UploadFileXsellDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.FileHelper;
import com.mcredit.util.StringUtils;

public class UploadAggregate {

    /**
     * ************** Begin Property **************
     */
    private UnitOfWorkTelesale unitOfWorkTelesale = null;
    private UnitOfWorkCommon unitOfWorkCommon = null;
    private static ModelMapper modelMapper = new ModelMapper();
    private UplDetail upLoadDetail;

    /**
     * ************** End Property **************
     */
    /**
     * **************
     * Begin Constructor
     *
     *************
     * @param uok
     */
    public UploadAggregate(UnitOfWorkTelesale uok) {

        this.unitOfWorkTelesale = uok;
    }
    
    public UploadAggregate(UnitOfWorkCommon uok) {

        this.unitOfWorkCommon = uok;
    }

    public UplDetail getUpLoadDetail() {
        return upLoadDetail;
    }

    public void setUpLoadDetail(UplDetail upLoadDetail) {
        this.upLoadDetail = upLoadDetail;
    }

    public void confirmUpload(Long UplDetailId, Integer errorNum) throws ValidationException {

        UplDetail uplDetail = this.getUplDetailById(UplDetailId);

        if (uplDetail == null)
            throw new ValidationException(Messages.getString("validation.not.exists", "Upl Detail"));

        uplDetail.setStatus("C");

        // For remote errror Imported
        if (errorNum != null && !errorNum.equals(0)) {
        	
        	/* Fix calculate imported after error prospect */
            uplDetail.setImported(uplDetail.getImported() - errorNum);
        	if( uplDetail.getImported() < 0 )
        		uplDetail.setImported(0);
        }

        unitOfWorkTelesale.uplDetailRepo().merge(uplDetail);

        //Update Total Import to UPL Master table
        UplMaster uplMaster = this.findUplMasterbyID(uplDetail.getUplMasterId());
        
        uplMaster.setImported(new Long(uplMaster.getImported() + uplDetail.getImported().intValue()));
        
        unitOfWorkTelesale.uplMasterRepo().merge(uplMaster);
    }

    public ArrayList<UplMasterDTO> getActiveCampaigns(String step, Long userId, String isAsm, String xsm, String ntb, boolean queryForProspectManagement, String tsaLst) {

        List<UplMaster> items = unitOfWorkTelesale.uplMasterRepo().findActiveCampaigns(step, userId, isAsm, xsm, ntb, queryForProspectManagement, tsaLst);
        ArrayList<UplMasterDTO> list = null;
        if (items != null && items.size() > 0) {
            list = new ArrayList<UplMasterDTO>();
            for (UplMaster item : items) {
                UplMasterDTO newObj = modelMapper.map(item, UplMasterDTO.class);
                newObj.setUplType(CacheManager.CodeTable().getCodeById(item.getUplType()));
                list.add(newObj);
            }
        }
        return list;

    }

    public UplMaster getUploadMasterByUplCode(String uplCode) {
        UplMaster UM = unitOfWorkTelesale.uplMasterRepo().findUplMasterbyUplCode(uplCode);
        return UM;
    }

    public ImportStatusDTO importStatus(String LoginUser) throws Exception {

        String[] statusFilter = new String[]{"V", "N", "E"};
        UplDetail UD = this.findPendingUplDetail(LoginUser, Arrays.asList(statusFilter));

        ImportStatusDTO ISDTO = new ImportStatusDTO();
        ISDTO = this.getUDStatus(UD);
        return ISDTO;
    }

    private ImportStatusDTO getUDStatus(UplDetail UD) throws Exception {

        if (UD == null)
            throw new ValidationException(Messages.getString("validation.not.exists", Labels.getString("label.ts.record")));

        ImportStatusDTO ISDTO = new ImportStatusDTO();
        ISDTO.setUplDetailId(UD.getId());
        if (UD.getStatus().equals("N")) {
            ISDTO.setStatus("New");
            return ISDTO;
        }
        if (UD.getStatus().equals("A")) {
            ISDTO.setStatus("Allocated All");
            return ISDTO;
        }
        if (UD.getStatus().equals("P")) {
            ISDTO.setStatus("Partial Allocated");
            return ISDTO;
        }
        if (UD.getStatus().equals("C")) {
            ISDTO.setStatus("Confirmed");
            return ISDTO;
        }
        if (UD.getStatus().equals("R")) {
            ISDTO.setStatus("Rejected");
            return ISDTO;
        }

        if (UD.getStatus().equals("E")) {
            ISDTO.setStatus("Error Processing");
            return ISDTO;
        }
        if (UD.getStatus().equals("V")) {
            ISDTO.setStatus("Validated");
        }

        ISDTO.setDupOldData(this.checkDupOldData(UD.getId()));
        ISDTO.setDupCurrentData(this.checkDupCurrentData(UD.getId()));
        ISDTO.setInvalidData(this.checkInvalidData(UD.getId()));
        ISDTO.setErrorMessage(UD.getErrorMessage());
        return ISDTO;
    }

    public void upsertUploadDetail() {
        unitOfWorkTelesale.uplDetailRepo().upsert(upLoadDetail);
    }

    public Integer getNextUplDetailSeq(Long uplMasterId) {
        return unitOfWorkTelesale.uplDetailRepo().getNextCallTimesByCustProspectId(uplMasterId);
    }

    public AllocateDTO queryUploadMaster(String uplCode, String uplType, String uplSequence, String uplOwner, String isAsm, String xsm) {

        UplMaster obj = unitOfWorkTelesale.uplMasterRepo().queryUploadMaster(uplCode, uplType, uplSequence, uplOwner, isAsm, xsm);

        if (obj != null) {
            if (obj.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "GC").getId())) {
                obj.setOwnerTeamLead(null);
                obj.setOwnerId(null);
                obj.setOwnerTeamLead(null);
                obj.setOwnerName(null);
            }
            if (obj.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "PC").getId())) {//for show teamlead name when teamlead is owner
                if (obj.getOwnerName().trim() != null && StringUtils.isNullOrEmpty(obj.getOwnerTeamLead())) {
                    obj.setOwnerTeamLead(obj.getOwnerName());
                }
            }

            return modelMapper.map(obj, AllocateDTO.class);
        }

        return null;
    }

    /*public void insertBatchUplCustomer(List<UplCustomer> UCList) {
        for (UplCustomer UC : UCList) {
            unitOfWorkTelesale.uplCustomerRepo().upsert(UC);
        }
    }*/

    public void insertUplCustomer(UplCustomer UC) {
        unitOfWorkTelesale.uplCustomerRepo().upsert(UC);
    }

    public UplDetail getUplDetailById(Long id) {
        return unitOfWorkTelesale.uplDetailRepo().getUplDetailbyId(id);
    }

    public void checkUplCustomer(Long id) {
        unitOfWorkTelesale.uplCustomerRepo().checkUplCustomer(id);
    }

    private Integer checkDupOldData(Long UplDetailId) {
        return unitOfWorkTelesale.uplCustomerRepo().checkDupOldData(UplDetailId);
    }

    private Integer checkDupCurrentData(Long UplDetailId) {
        return unitOfWorkTelesale.uplCustomerRepo().checkDupCurrentData(UplDetailId);
    }

    private Integer checkInvalidData(Long UplDetailId) {
        return unitOfWorkTelesale.uplCustomerRepo().checkInvalidData(UplDetailId);
    }

    private UplDetail findPendingUplDetail(String userLogin, List<String> status) {
        return unitOfWorkTelesale.uplDetailRepo().findCurrentPendingUplDetail(userLogin, status);
    }

    public void deleteUplCustomerByUplDetail(Long UplDetailId) {
        unitOfWorkTelesale.uplCustomerRepo().deleteUplCustomerByUplDetail(UplDetailId);
    }

    public void deleteInvalidCustomerByUplDetailId(Long UplDetailId) throws ValidationException {
        unitOfWorkTelesale.uplCustomerRepo().deleteInvalidCustomerByUplDetailId(UplDetailId);
    }
    
    public void updateImportedAndAllocatedUplMaster(Long uplDetailId) throws ValidationException {
        unitOfWorkTelesale.uplCustomerRepo().updateImportedAndAllocatedUplMaster(uplDetailId);
    }

    public void upsertUplMaster(UplMaster UM) {
        unitOfWorkTelesale.uplMasterRepo().upsert(UM);
    }

    public UplMaster findUplMasterbyID(long longValue) {
        return unitOfWorkTelesale.uplMasterRepo().findUplMasterbyID(longValue);
    }
    
    /*public String getTotalImportedAndTotalAllocated(Long uplMasterId) {
        return unitOfWorkTelesale.uplMasterRepo().getTotalImportedAndTotalAllocated(uplMasterId);
    }*/
    
    /*public Integer getTotalImportedFromUplDetail(Long uplDetailId) {
        return unitOfWorkTelesale.uplMasterRepo().getTotalImportedFromUplDetail(uplDetailId);
    }*/

    public void insertUplCustomerXsell(UplCustomer UC, int count) {
        unitOfWorkTelesale.uplCustomerRepo().upsertImportXsell(UC, count);
    }

    public Object importXsell(UploadFileXsellDTO xsellDTO, UserDTO user) throws Exception {
        String campaignCode = unitOfWorkTelesale.uplMasterRepo().getUplCode();
        String fileExt = "." + FilenameUtils.getExtension(xsellDTO.getUserFileName());

        //dung de test file tren local
//        FileHelper.createFolder("FileImportXsell");
//        String serverFilename = "FileImportXsell\\" + campaignCode + fileExt;
        String serverFilename = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY) + campaignCode + fileExt;

        File targetFile = new File(serverFilename);
        FileHelper.copyInputStreamToFile(xsellDTO.getFileContent(), targetFile);
        String path = targetFile.getCanonicalPath();
        System.out.println(path);
        File importFile = new File(path);
        XSSFSheet xlsxSheet = null;
        Sheet sheet = null;
        if (fileExt.equalsIgnoreCase(".xls")) {
            sheet = FileImportUtils.getExcelSheet(importFile, 0);
        } else if (fileExt.equalsIgnoreCase(".xlsx")) {
            xlsxSheet = FileImportUtils.getSheetXlsxExcel(importFile, 0);
        }

        //Doc file cau hinh import
        Document doc = (Document) FileImportUtils.readFileImportConfig();
        if (xlsxSheet == null && sheet == null)
            throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.no.record.inserted")));
        else {
            Long uplTypeId = CacheManager.CodeTable().getIdBy(CTCodeValue1.CX, CTCat.UPL_TYPE, CTGroup.MISC);
            Long sourceId = CacheManager.CodeTable().getIdBy(CTCodeValue1.MIS, CTCat.UPL_SRC, CTGroup.MISC);

            UplMaster UM = new UplMaster("A", new Date(), user.getLoginId(), fileExt, sourceId, campaignCode, uplTypeId.intValue(), user.getId(), 0L, 0L);
            upsertUplMaster(UM);

            UplDetail UD = new UplDetail(UM.getId(), 1, 0, 0, xsellDTO.getUserFileName(),
                    targetFile.getCanonicalPath(), "N", "A", new Date(), user.getLoginId(), DateFormatTag.DATEFORMAT_dd_MM_yyyy.formatter().parse(xsellDTO.getDateFrom()),
                    DateFormatTag.DATEFORMAT_dd_MM_yyyy.formatter().parse(xsellDTO.getDateTo()), null);
            unitOfWorkTelesale.uplDetailRepo().upsert(UD);

            if (fileExt.equalsIgnoreCase(".xls")) {
                ImportXsellXLSBackground xLSBackground = new ImportXsellXLSBackground(UD, UM, sheet, doc);
                xLSBackground.start();
            } else if (fileExt.equalsIgnoreCase(".xlsx")) {
                ImportXsellBackground importXsellBackground = new ImportXsellBackground(UD, UM, xlsxSheet, doc);
                importXsellBackground.start();
            }

            return new IdDTO(UD.getId());
        }

    }

    public Result setStatusImportXsell(Long uplDetailId) throws Exception {
        
    	UplDetail detail = (UplDetail) this.unitOfWorkTelesale.uplDetailRepo().get(UplDetail.class, uplDetailId);
        
    	if (detail == null)
            throw new ValidationException(Messages.getString("validation.not.exists", "Upl Detail"));
        else {
            Long statusAppId = CacheManager.CodeTable().getIdBy(CTCodeValue1.XSELL_APP_WAIT, CTCat.STATUS_APP_XSELL, CTGroup.MIS);
            detail.setStatusApp(statusAppId);
            detail.setStatus("C");
            unitOfWorkTelesale.uplDetailRepo().upsert(detail);
            UplMaster master = (UplMaster) this.unitOfWorkTelesale.uplMasterRepo().get(UplMaster.class, detail.getUplMasterId());
            Result result = new Result();
            result.setReturnCode("200");
            result.setReturnMes(master.getUplCode());
            return result;
        }

    }
    
    public void insertAllocationMaster(AllocationMaster master) {
    	unitOfWorkCommon.allocationMasterRepo().upsert(master);
    }
    
    public void insertAllocationDetail(AllocationDetail dt) {
    	unitOfWorkCommon.allocationDetailRepo().upsert(dt);
    }
    
    public int buidAllocatedSeq(Long uplMasterId, String allocatedType) {
    	return unitOfWorkCommon.allocationMasterRepo().buidAllocatedSeq(uplMasterId, allocatedType).intValue()+1;
    }
    
    public List<AllocationMaster> findAllocationMaster(Long uplMasterId, String allocatedType) {
    	return unitOfWorkCommon.allocationMasterRepo().findAllocationMasterByuplMasterIdAllocatedType(uplMasterId, allocatedType);
    	
    }
    
    public void deleteInvalidAllocationDetail(Long uplDetailId) throws ValidationException {
        unitOfWorkTelesale.uplCustomerRepo().deleteInvalidAllocationDetail(uplDetailId);
    }
    
    public void deleteAllocationMaster(List<Long> lstId) throws ValidationException {
        unitOfWorkTelesale.uplCustomerRepo().deleteAllocationMaster(lstId);
    }
    
    public void deleteInvalidAllocationMaster(Long uplDetailId) throws ValidationException {
        unitOfWorkTelesale.uplCustomerRepo().deleteInvalidAllocationMaster(uplDetailId);
    }
    
    public List<Long> findListByDetailId(Long uplDetailId) throws ValidationException {
        return unitOfWorkTelesale.uplCustomerRepo().findListByDetailId(uplDetailId);
    }
    
    public void confirmNextImportTSA(Long uplDetailId) throws Exception {
        unitOfWorkTelesale.uplDetailRepo().confirmNextImportTSA(uplDetailId);
    }
    
    public String getUplCodeByDetailId(Long uplDetailId) {
    	return unitOfWorkTelesale.uplDetailRepo().getUplCodeByDetailId(uplDetailId);
    }

}
