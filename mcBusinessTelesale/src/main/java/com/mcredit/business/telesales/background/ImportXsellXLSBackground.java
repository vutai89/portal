package com.mcredit.business.telesales.background;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.common.ErrorImportDTO;
import com.mcredit.model.dto.telesales.ImportStatusXsellDTO;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.object.telesales.CheckDupIdentity;
import com.mcredit.util.JSONConverter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ImportXsellXLSBackground extends Thread {

    private static final String IMPORT_XSELL = "IMPORT_XSELL";
    private static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    private static final String IDENTITY_NUMBER = "IDENTITY_NUMBER";
    private static final String IDENTITY_NUMBER_ARMY = "IDENTITY_NUMBER_ARMY";
    private static final String BIRTH_DATE = "BIRTH_DATE";
    private static final String PRE_PRODUCT_NAME = "PRE_PRODUCT_NAME";
    private static final String PRE_PRODUCT_CODE = "PRE_PRODUCT_CODE";
    private static final String PRE_NUMBER_CASH_APP = "PRE_NUMBER_CASH_APP";
    private static final String PRE_INTEREST_RATE = "PRE_INTEREST_RATE";
    private static final String PRE_TERM_LOAN = "PRE_TERM_LOAN";
    private static final String PRE_RELEASE_DATE = "PRE_RELEASE_DATE";
    private static final String PRE_RECKONING_DATE = "PRE_RECKONING_DATE";
    private static final String PRE_EMI = "PRE_EMI";
    private static final String APP_PRODUCT_NAME = "APP_PRODUCT_NAME";
    private static final String APP_PRODUCT_CODE = "APP_PRODUCT_CODE";
    private static final String APP_LOAN_LIMIT_MIN = "APP_LOAN_LIMIT_MIN";
    private static final String APP_LOAN_LIMIT_MAX = "APP_LOAN_LIMIT_MAX";
    private static final String APP_TERM_LOAN_MIN = "APP_TERM_LOAN_MIN";
    private static final String APP_TERM_LOAN_MAX = "APP_TERM_LOAN_MAX";
    private static final String APP_EMI_MIN = "APP_EMI_MIN";
    private static final String APP_EMI_MAX = "APP_EMI_MAX";
    private static final String MERCHANDISE_CODE = "MERCHANDISE_CODE";
    private static final String DATA_SOURCE = "DATA_SOURCE";
    private static final String LEAD_SOURCE = "LEAD_SOURCE";
    private static final String APP_NUMBER = "APP_NUMBER";
    private final static String VALIDATE_DATA_DUP_EXEL = "import.validate.data.dup.exel";
    private final static String VALIDATE_DATA_DUP_DB = "import.validate.data.dup.db";
    private final static String VALIDATE_MAXLENGTH = "import.validate.maxlength";
    private final static String VALIDATE_MINLENGTH = "import.validate.minlength";
    private final static String VALIDATE_REQUIRE = "import.validate.require";
    private final static String VALIDATE_DATA_TYPE = "import.validate.data_type";
    private final static String VALIDATE_DATA_DATE = "import.validate.date";

    private UnitOfWork uok = null;
    private final UplDetail uplDetail;
    private final UplMaster uplMaster;
    private final Sheet sheet;
    private final Document doc;
    private final Map<String, List<Long>> uplCodeLst = new HashMap<String, List<Long>>();

    public ImportXsellXLSBackground(UplDetail UD, UplMaster UM, Sheet sheet, Document doc) {
        this.uplDetail = UD;
        this.uplMaster = UM;
        this.sheet = sheet;
        this.doc = doc;
    }

    private List<ErrorImportDTO> validateImportMIS(Sheet sheet, Document doc) throws Exception {

        List<ErrorImportDTO> lstResult = new ArrayList<ErrorImportDTO>();
        doc.getDocumentElement().normalize();

        // Lay ten bang trong file import config
        Node rootTable = doc.getElementsByTagName(IMPORT_XSELL).item(0);

        int posIdentityNumber = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, IDENTITY_NUMBER,
                FileImportUtils.POS));
        int posDateSource = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, DATA_SOURCE,
                FileImportUtils.POS));
        int posAppProductCode = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_PRODUCT_CODE,
                FileImportUtils.POS));
        int posIdentityNumberArmy = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, IDENTITY_NUMBER_ARMY,
                FileImportUtils.POS));

        int sheetRowsCount = sheet.getRows();
        String stIdentityNumber = "";
        String stIdentityNumberArmy = "";
        List<String> lstProductCode = this.uok.telesale.uplMasterRepo().getLstProductCode();
        List<CheckDupIdentity> lstIdentityDB = this.uok.telesale.uplMasterRepo().checkDupIdentity(uplDetail.getDateFrom());
        List<CheckDupIdentity> lstIdentityArmyDB = this.uok.telesale.uplMasterRepo().checkDupIdentityArmy(uplDetail.getDateFrom());
        HashSet<String> hashKey = new HashSet<String>();
        for (int i = 3; i < sheetRowsCount; i++) {
            Cell[] cell = sheet.getRow(i);
            if (FileImportUtils.checkRowNotBlank(cell)) {
                FileImportUtils.checkRequire(cell, rootTable, lstResult, i + 1);
                FileImportUtils.checkMaxLength(cell, rootTable, lstResult, i + 1);
                FileImportUtils.checkMinLength(cell, rootTable, lstResult, i + 1);
                FileImportUtils.checkDataType(cell, rootTable, lstResult, i + 1);
                FileImportUtils.checkDataDate(cell, rootTable, lstResult, i + 1);

                //check trung du lieu trong file cmnd, cmqd
                stIdentityNumber = sheet.getCell(posIdentityNumber, i).getContents().trim();
                if (FileImportUtils.checkDuplicateFileExel(sheet, i, posIdentityNumber, stIdentityNumber) && !stIdentityNumber.equals("")) {
                    String errDescr = VALIDATE_DATA_DUP_EXEL;
                    importErr(i + 1, posIdentityNumber + 1, errDescr, lstResult, stIdentityNumber, null);
                }
                stIdentityNumberArmy = sheet.getCell(posIdentityNumberArmy, i).getContents().trim();
                if (FileImportUtils.checkDuplicateFileExel(sheet, i, posIdentityNumberArmy, stIdentityNumberArmy) && !stIdentityNumberArmy.equals("")) {
                    String errDescr = VALIDATE_DATA_DUP_EXEL;
                    importErr(i + 1, posIdentityNumberArmy + 1, errDescr, lstResult, stIdentityNumberArmy, null);
                }
                //check cmnd, cmqd co ton tai DB chua
                 if (!stIdentityNumber.equals("") && lstIdentityDB != null && !lstIdentityDB.isEmpty()) {
                    checkExitIdentity(lstIdentityDB, stIdentityNumber, i + 1, hashKey);
                }
                if (!stIdentityNumberArmy.equals("") && lstIdentityArmyDB != null && !lstIdentityArmyDB.isEmpty()) {
                    checkExitIdentity(lstIdentityArmyDB, stIdentityNumberArmy, i + 1, hashKey);
                }
                //check nguon data co hop le khong
                String stDataSource = sheet.getCell(posDateSource, i).getContents().trim();
                if (!stDataSource.equalsIgnoreCase("New") && !stDataSource.equalsIgnoreCase("RMN")) {
                	System.out.println("----- XLS VALIDATE_REQUIRE stDataSource " + stDataSource);
                    String errDescr = VALIDATE_REQUIRE;
                    importErr(i + 1, posDateSource + 1, errDescr, lstResult, null, null);
                }
                //check ma san pham cua thong tin khoan vay hien tai/da tat toan
                String stAppProductCode = sheet.getCell(posAppProductCode, i).getContents().trim().toUpperCase();
                if (!lstProductCode.contains(stAppProductCode)) {
                	System.out.println("----- XLS VALIDATE_REQUIRE stAppProductCode " + stAppProductCode);
                    String errDescr = VALIDATE_REQUIRE;
                    importErr(i + 1, posAppProductCode + 1, errDescr, lstResult, null, null);
                }
                //check cmtnd phai co do dai =9 hoac =12                
                if (stIdentityNumber != null && !stIdentityNumber.equals("")) {
                    if (stIdentityNumber.length() > 9 && stIdentityNumber.length() < 12) {
                    	System.out.println("----- XLS VALIDATE_REQUIRE stIdentityNumber " + stIdentityNumber);
                        String errDescr = VALIDATE_REQUIRE;
                        importErr(i + 1, posIdentityNumber + 1, errDescr, lstResult, null, null);
                    }
                }
                //check bat buoc nhap 1 trong 2 truong cmt hoac cmtqd
                stIdentityNumberArmy = sheet.getCell(posIdentityNumberArmy, i).getContents().trim();
                if (stIdentityNumber.equals("") && stIdentityNumberArmy.equals("")) {
                	System.out.println("----- XLS VALIDATE_REQUIRE stIdentityNumberArmy " + stIdentityNumberArmy);
                    String errDescr = VALIDATE_REQUIRE;
                    importErr(i + 1, posIdentityNumber + 1, errDescr, lstResult, null, null);
                }

            }
        }
        return lstResult;
    }

    private void inserImportMIS(Sheet sheet, Document doc, Long uplDetailId) throws Exception {
        doc.getDocumentElement().normalize();
        Node rootTable = doc.getElementsByTagName(IMPORT_XSELL).item(0);

        int poscustomerName = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, CUSTOMER_NAME,
                FileImportUtils.POS));
        int posIdentityNumber = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, IDENTITY_NUMBER,
                FileImportUtils.POS));
        int posIdentityNumberArmy = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, IDENTITY_NUMBER_ARMY,
                FileImportUtils.POS));
        int posBirthDate = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, BIRTH_DATE,
                FileImportUtils.POS));
        int posAppProductName = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_PRODUCT_NAME,
                FileImportUtils.POS));
        int posAppProductCode = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_PRODUCT_CODE,
                FileImportUtils.POS));
        int posAppLoanApprovedAmt = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_NUMBER_CASH_APP,
                FileImportUtils.POS));
        int posAppIntRate = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_INTEREST_RATE,
                FileImportUtils.POS));
        int posAppTermLoan = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_TERM_LOAN,
                FileImportUtils.POS));
        int posDisbursementDate = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_RELEASE_DATE,
                FileImportUtils.POS));
        int posMatDate = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_RECKONING_DATE,
                FileImportUtils.POS));
        int posAppEmi = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, PRE_EMI,
                FileImportUtils.POS));
        int posPreProductName = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_PRODUCT_NAME,
                FileImportUtils.POS));
        int posPreProductCode = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_PRODUCT_CODE,
                FileImportUtils.POS));
        int posPreMinLimit = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_LOAN_LIMIT_MIN,
                FileImportUtils.POS));
        int posPreMaxLimit = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_LOAN_LIMIT_MAX,
                FileImportUtils.POS));
        int posPreMinTenor = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_TERM_LOAN_MIN,
                FileImportUtils.POS));
        int posPreMaxTenor = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_TERM_LOAN_MAX,
                FileImportUtils.POS));
        int posPreMinEmi = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_EMI_MIN,
                FileImportUtils.POS));
        int posPreMaxEmi = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_EMI_MAX,
                FileImportUtils.POS));
        int posCommoditiesCode = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, MERCHANDISE_CODE,
                FileImportUtils.POS));
        int posDateSource = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, DATA_SOURCE,
                FileImportUtils.POS));
        int posLeadSource = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, LEAD_SOURCE,
                FileImportUtils.POS));
        int posAppNumber = Integer.parseInt(FileImportUtils.getAttributeChildByParentNode(rootTable, APP_NUMBER,
                FileImportUtils.POS));

        int sheetRowsCount = sheet.getRows();
        UplCustomer UC;
        int count = 0;
        for (int i = 3; i < sheetRowsCount; i++) {
            Cell[] cell = sheet.getRow(i);
            String stCustomerName = sheet.getCell(poscustomerName, i).getContents().trim();
            String stIdentityNumber = sheet.getCell(posIdentityNumber, i).getContents().trim();
            String stIdentityNumberArmy = sheet.getCell(posIdentityNumberArmy, i).getContents().trim();
            String stposBirthDate = sheet.getCell(posBirthDate, i).getContents().trim();
            String stAppProductName = sheet.getCell(posAppProductName, i).getContents().trim();
            String stAppProductCode = sheet.getCell(posAppProductCode, i).getContents().trim();
            String stAppLoanApprovedAmt = sheet.getCell(posAppLoanApprovedAmt, i).getContents().trim();
            String stAppIntRate = sheet.getCell(posAppIntRate, i).getContents().trim();
            String stAppTermLoan = sheet.getCell(posAppTermLoan, i).getContents().trim();
            String stDisbursementDate = sheet.getCell(posDisbursementDate, i).getContents().trim();
            String stMatDate = sheet.getCell(posMatDate, i).getContents().trim();
            String stAppEmi = sheet.getCell(posAppEmi, i).getContents().trim();
            String stPreProductName = sheet.getCell(posPreProductName, i).getContents().trim();
            String stPreProductCode = sheet.getCell(posPreProductCode, i).getContents().trim();
            String stPreMinLimit = sheet.getCell(posPreMinLimit, i).getContents().trim();
            String stPreMaxLimit = sheet.getCell(posPreMaxLimit, i).getContents().trim();
            String stPreMinTenor = sheet.getCell(posPreMinTenor, i).getContents().trim();
            String stPreMaxTenor = sheet.getCell(posPreMaxTenor, i).getContents().trim();
            String stPreMinEmi = sheet.getCell(posPreMinEmi, i).getContents().trim();
            String stPreMaxEmi = sheet.getCell(posPreMaxEmi, i).getContents().trim();
            String stCommoditiesCode = sheet.getCell(posCommoditiesCode, i).getContents().trim();
            String stDataSource = sheet.getCell(posDateSource, i).getContents().trim();
            String stLeadSource = sheet.getCell(posLeadSource, i).getContents().trim();
            String stAppNumber = sheet.getCell(posAppNumber, i).getContents().trim();
            Date dateMatDate = null;

            if (FileImportUtils.checkRowNotBlank(cell)) {
                Long custId = null;
                if (!stIdentityNumber.equals("")) {
                    custId = this.uok.telesale.uplMasterRepo().getCustId(stposBirthDate, stIdentityNumber);
                }
                if (custId == null && !stIdentityNumberArmy.equals("")) {
                    custId = this.uok.telesale.uplMasterRepo().getCustId(stposBirthDate, stIdentityNumberArmy);
                }
                if (!stMatDate.equals("")) {
                    dateMatDate = DateFormatTag.DATEFORMAT_dd_MM_yyyy.formatter().parse(stMatDate);
                }
                UC = new UplCustomer(uplDetailId, stposBirthDate, stIdentityNumber, "OK", stIdentityNumberArmy, stAppProductName, stAppProductCode,
                        Long.valueOf(stAppLoanApprovedAmt), Double.valueOf(stAppIntRate.replaceAll(",", ".")), Long.valueOf(stAppTermLoan),
                        DateFormatTag.DATEFORMAT_dd_MM_yyyy.formatter().parse(stDisbursementDate), dateMatDate,
                        Long.valueOf(stAppEmi), stCommoditiesCode, stPreProductName, stPreProductCode, Long.valueOf(stPreMinLimit), Long.valueOf(stPreMaxLimit),
                        Long.valueOf(stPreMinTenor), Long.valueOf(stPreMaxTenor), Long.valueOf(stPreMinEmi), Long.valueOf(stPreMaxEmi), stDataSource, stLeadSource, custId, stCustomerName, Long.valueOf(stAppNumber));
                ++count;
                TelesalesFactory.getUploadAggregateInstance(this.uok.telesale).insertUplCustomerXsell(UC, count);
            }

        }
    }

    private void importErr(int row, int col, String descr, List<ErrorImportDTO> lstResultErr, String valueDupExel, String valueDupDB) throws Exception {
        ErrorImportDTO errBO = new ErrorImportDTO();
        errBO.setColumn(col);
        errBO.setRow(row);
        errBO.setDescrption(descr.toString());
//        errBO.setCellErr(FileImportUtils.toNameCellErr(col) + row);
        errBO.setValueDupExel(valueDupExel);
        errBO.setValueDupDB(valueDupDB);
        lstResultErr.add(errBO);
    }
 private void checkExitIdentity(List<CheckDupIdentity> lstIdentity, String identityNumber, int row, HashSet<String> hashKey) {
        List<Long> lstRow = new ArrayList<>();
        for (int j = 0; j < lstIdentity.size(); j++) {
            if (lstIdentity.get(j).getIdentity().equalsIgnoreCase(identityNumber)) {
                if (uplCodeLst.isEmpty()) {
                    lstRow.add(Long.valueOf(row));
                    uplCodeLst.put(lstIdentity.get(j).getUplCode(), lstRow);
                    hashKey.add(lstIdentity.get(j).getUplCode());
                    lstRow = new ArrayList<>();
                } else {
                    if (!hashKey.contains(lstIdentity.get(j).getUplCode())) {
                        lstRow.add(Long.valueOf(row));
                        uplCodeLst.put(lstIdentity.get(j).getUplCode(), lstRow);
                        hashKey.add(lstIdentity.get(j).getUplCode());
                        lstRow = new ArrayList<>();
                    } else {
                        lstRow = uplCodeLst.get(lstIdentity.get(j).getUplCode());
                        if (!lstRow.contains(row)) {                                
                                uplCodeLst.get(lstIdentity.get(j).getUplCode()).add(Long.valueOf(row));                                
                            }
                        lstRow = new ArrayList<>();
                    }
                }

            }
        }
    }

    private ImportStatusXsellDTO getErrMessageImport(Sheet sheet, Document doc) throws Exception {
        ImportStatusXsellDTO isxdto = null;
        List<ErrorImportDTO> lstResultErr = validateImportMIS(sheet, doc);
        List<String> lstDupDB = new ArrayList<>();
        if (lstResultErr != null && lstResultErr.size() > 0) {
            isxdto = new ImportStatusXsellDTO();
            StringBuilder dupIdentityFileExel = new StringBuilder();
            String valueDupExel = "";
            String validateRequire = "";
            String validateMaxLength = "";
            String validateMinLength = "";
            String validateDataType = "";
            String validateDataDate = "";
            List<String> lstDupFile = new ArrayList<>();
            List<String> lstInvalidData = new ArrayList<>();
            List<String> lstValueDupFile = new ArrayList<>();
            HashSet<Long> lstRowInvalidRequire = new HashSet<Long>();
            HashSet<Long> lstRowInvalidMaxLenght = new HashSet<Long>();
            HashSet<Long> lstRowInvalidMinLenght = new HashSet<Long>();
            HashSet<Long> lstRowInvalidDataType = new HashSet<Long>();
            HashSet<Long> lstRowInvalidDataDate = new HashSet<Long>();
            for (int i = 0; i < lstResultErr.size(); i++) {
                if (lstResultErr.get(i).getDescrption().equals(VALIDATE_DATA_DUP_EXEL)
                        && !lstValueDupFile.contains(lstResultErr.get(i).getValueDupExel())) {
                    valueDupExel = lstResultErr.get(i).getValueDupExel();
                    lstValueDupFile.add(valueDupExel);
                    for (int j = 0; j < lstResultErr.size(); j++) {
                        if (lstResultErr.get(j).getDescrption().equals(VALIDATE_DATA_DUP_EXEL)
                                && lstResultErr.get(j).getValueDupExel().equalsIgnoreCase(valueDupExel)) {
                            dupIdentityFileExel.append(lstResultErr.get(j).getRow()+ ", ");
                        }
                    }
                    lstDupFile.add(dupIdentityFileExel.toString().substring(0, dupIdentityFileExel.toString().length() - 2));
                    dupIdentityFileExel = new StringBuilder();
                } else if (lstResultErr.get(i).getDescrption().equals(VALIDATE_REQUIRE) && !lstRowInvalidRequire.contains(Long.valueOf(lstResultErr.get(i).getRow()))) {
                    validateRequire = validateRequire + lstResultErr.get(i).getRow() + ", ";
                    lstRowInvalidRequire.add(Long.valueOf(lstResultErr.get(i).getRow()));

                } else if (lstResultErr.get(i).getDescrption().equals(VALIDATE_MAXLENGTH) && !lstRowInvalidMaxLenght.contains(Long.valueOf(lstResultErr.get(i).getRow()))) {
                    validateMaxLength = validateMaxLength + lstResultErr.get(i).getRow() + ", ";
                    lstRowInvalidMaxLenght.add(Long.valueOf(lstResultErr.get(i).getRow()));

                } else if (lstResultErr.get(i).getDescrption().equals(VALIDATE_MINLENGTH) && !lstRowInvalidMinLenght.contains(Long.valueOf(lstResultErr.get(i).getRow()))) {
                    validateMinLength = validateMinLength + lstResultErr.get(i).getRow() + ", ";
                    lstRowInvalidMinLenght.add(Long.valueOf(lstResultErr.get(i).getRow()));

                } else if (lstResultErr.get(i).getDescrption().equals(VALIDATE_DATA_TYPE) && !lstRowInvalidDataType.contains(Long.valueOf(lstResultErr.get(i).getRow()))) {
                    validateDataType = validateDataType + lstResultErr.get(i).getRow() + ", ";
                    lstRowInvalidDataType.add(Long.valueOf(lstResultErr.get(i).getRow()));

                } else if (lstResultErr.get(i).getDescrption().equals(VALIDATE_DATA_DATE) && !lstRowInvalidDataDate.contains(Long.valueOf(lstResultErr.get(i).getRow()))) {
                    validateDataDate = validateDataDate + lstResultErr.get(i).getRow() + ", ";
                    lstRowInvalidDataDate.add(Long.valueOf(lstResultErr.get(i).getRow()));

                }

            }
            
            if (validateRequire != null && !validateRequire.equals("")) {
                lstInvalidData.add("Truong bat buoc nhap/khong hop le: " + validateRequire.substring(0, validateRequire.toString().length() - 2));
            }
            if (validateMaxLength != null && !validateMaxLength.equals("")) {
                lstInvalidData.add("Truong vuot qua do dai: " + validateMaxLength.substring(0, validateMaxLength.toString().length() - 2));
            }
            if (validateMinLength != null && !validateMinLength.equals("")) {
                lstInvalidData.add("Truong chua du do dai: " + validateMinLength.substring(0, validateMinLength.toString().length() - 2));
            }
            if (validateDataType != null && !validateDataType.equals("")) {
                lstInvalidData.add("Truong chua dung kieu du lieu: " + validateDataType.substring(0, validateDataType.toString().length() - 2));
            }
            if (validateDataDate != null && !validateDataDate.equals("")) {
                lstInvalidData.add("Truong chua dung dinh dang ngay thang (dd/mm/yyyy): " + validateDataDate.substring(0, validateDataDate.toString().length() - 2));
            }

            if (!lstDupFile.isEmpty()) {
                isxdto.setLstIdentityNumberDupFile(lstDupFile);
            }
            if (!lstInvalidData.isEmpty()) {
                isxdto.setLstInvalidData(lstInvalidData);
            }
            
        }
        
        if (!uplCodeLst.isEmpty()) {
            String validateDup = "";
            for (Map.Entry<String, List<Long>> entry : uplCodeLst.entrySet()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    validateDup = validateDup + entry.getValue().get(i) + ", ";
                }
                lstDupDB.add(entry.getKey() + " : " + validateDup.substring(0, validateDup.toString().length() - 2));
                validateDup = "";
            }
        }
        if (!lstDupDB.isEmpty()) {
            if (isxdto == null) {
                isxdto = new ImportStatusXsellDTO();
            }
            isxdto.setLstIdentityNumberDupDB(lstDupDB);
        }
        return isxdto;

    }

    @Override
    public void run() {
        uok = new UnitOfWork();
        try {
            Long Start = System.currentTimeMillis();

            uok.start();
            System.out.println("**************Start Excel Processing*****************");

            ImportStatusXsellDTO isxdto = getErrMessageImport(sheet, doc);
            if (isxdto != null) {
                uplDetail.setErrorMessage(JSONConverter.toJSON(isxdto));
                if (isxdto.getLstInvalidData() == null) {
                    inserImportMIS(sheet, doc, uplDetail.getId());
                }
            } else {
                inserImportMIS(sheet, doc, uplDetail.getId());
            }
            uplDetail.setStatus("V");
            int quantityRecord = FileImportUtils.getRecordFileExel(sheet, 3);
            uplDetail.setImported(quantityRecord);
            this.uok.telesale.uplDetailRepo().upsert(uplDetail);

            uplMaster.setImported(Long.valueOf(quantityRecord));
            TelesalesFactory.getUploadAggregateInstance(this.uok.telesale).upsertUplMaster(uplMaster);

            this.uok.commit();

            System.out.println("DONE! Number of Records: " + uplDetail.getImported());
            Long Stop = System.currentTimeMillis();
            System.out.println("Execution Time: " + (Stop - Start) / 1000 + "s");

        } catch (Exception ex) {
            this.uok.rollback();
            try {
                this.uok.reset();
                this.uok.start();
                uplDetail.setImported(0);
                uplDetail.setStatus("E");
                String ErrorMessage = "";
                Exception cloneEx = ex;
                while (cloneEx != null) {
                    ErrorMessage += cloneEx.getMessage() + "\n";
                    cloneEx = (Exception) cloneEx.getCause();
                }
                uplDetail.setErrorMessage(ErrorMessage);
                this.uok.telesale.uplDetailRepo().upsert(uplDetail);
                uplMaster.setImported(0L);
                TelesalesFactory.getUploadAggregateInstance(this.uok.telesale).upsertUplMaster(uplMaster);
                this.uok.commit();
                ex.getCause().getMessage();
            } catch (Exception e) {
                this.uok.rollback();
                e.printStackTrace();
            }
            ex.printStackTrace();

        }

    }

}
