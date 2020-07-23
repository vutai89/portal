
package com.mcredit.business.gendoc.aggregate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jodconverter.office.OfficeException;
import org.json.JSONObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.gendoc.bean.EntityBean;
import com.mcredit.data.util.JSONFactory;
import com.mcredit.model.enums.ParamGenDoc;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.gendoc.GendocRepaymentSchedule;
import com.mcredit.model.object.gendoc.GendocRepaymentScheduleRespone;
import com.mcredit.model.object.gendoc.GendocRepaymentScheduleView;
import com.mcredit.model.object.gendoc.GendocDataAppendix;
import com.mcredit.model.object.gendoc.GendocDataAppendixView;
import com.mcredit.model.object.gendoc.GendocVersion;
import com.mcredit.model.object.gendoc.VDataEntry;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TableVariable;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variable;
import pl.jsolve.templ4docx.variable.Variables;

public class GendocAggregate {

    public GendocAggregate() {
    }

    private static final String INSTALLMENT_LOAN = "InstallmentLoan";
    private static final String CASH_LOAN = "CashLoan";
    private static final String CONCENTRATING_DATA_ENTRY = "ConcentratingDataEntry";
    private static final String CONTRACT_CASHLOAN = "ContractCashLoan";
    private static final String CONTRACT_CASHLOAN_NO_INSURRANCE = "ContractCashLoanNoInsurrance";
    private static final String CONTRACT_INSTALLMENTLOAN = "ContractInstallmentLoan";
    private static final String CONTRACT_INSTALLMENTLOAN_NO_INSURRANCE = "ContractInstallLoanNoInsurrance";
    private static final String INSURRANCE_PAPER_MBAL = "InsurancePaperMBAL";
    private static final String INSURRANCE_PAPER_MBAL_OLD = "InsurancePaperMBALOld";
    private static final String OPERATION_CASHLOAN = "OperationCashLoan";
    private static final String OPERATION_INSTALLMENTLOAN = "OperationInstallLoan";
    private static final String LOANAMOUNT_FORM_HAS_INSURANCE = "LoanAmountFormHasInsurance";
    private static final String LOANAMOUNT_FORM_NO_INSURANCE = "LoanAmountFormNoInsurance";
    private static final String CONTRACT_INSTALLMENTLOAN_ENDOW = "ContractInstallmentLoanEndow";
    private static final String CONTRACT_INSTALLMENTLOAN_ENDOW_NO_INSURRANCE = "ContractInstallmentLoanEndowNoInsurance";
    private static final String OPERATION_INSTALLMENTLOAN_ENDOW = "OperationInstallLoanEndow";
    private static final String SUB_CONTRACT = "SubContract";

    private static final String date_format = "yyyy/MM/dd HH:mm:ss";
    private static final String endow_product_1 = "CD0%HITECH";
    private static final String endow_product_2 = "CD0%HOME";
    private static final String CONTRACT_APPENDIX_INSTALLMENTLOAN = "ContractAppendixInstallLoan";
    private static final String COMMITMENT_INFORMATION = "CommitmentInformation";
    private static final String SALE_PAPER = "PhieuBanHang_BM09";
    private static final String INFACT_CONFIRM_PAPER = "PhieuXacNhanGiaBan_BM10";
    private static final String CERTIFICATE_OF_INSTALLMENT_SALE_FOR_AGENT = "BienBanXacNhanBanHang_BM11";
    private static final String PROPOSAL_CANCEL_LOAN = "DonDeNghiHuyHoSoTinDung";

    private static final String REPAYMENT_SCHEDULE_IL = "RepaymentScheduleIL";
    private static final String REPAYMENT_SCHEDULE_CL = "RepaymentScheduleCL";
    private static final String date_format_ddMMyyyy = "dd/MM/yyyy";
    private static final String date_format_YYYYMMDD = "yyyy-MM-dd";
    private static final String V_DATA_ENTRY = "v_data_entry";
    private static final String V_DATA_ENTRY_GENDOC = "v_data_entry_gendoc";
    private static final String V_REPAYMENT_SCHEDULE = "v_repayment_schedule";
    private static final String V_REPAYMENT_DATE ="v_repayment_date";
    
    private static final NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
    
    private static final String CUSTOMER_INFORMATION = "CustomerInformation";
    private static final String CONTRACT_CASHLOAN_LIMIT_PROD = "ContractCashLoanLimitProd";
    private static final String CONTRACT_CASHLOAN_LIMIT_PROD_NO_INSURRANCE = "ContractCashLoanLimitProdNoInsurrance";
    private static final String INSURRANCE_PAPER_MBAL_LIMIT_PROD = "InsurancePaperMBALLimitPro";
    
    private String _esbHost = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST);
    
    private static final String GOOD_LABEL = "Loại hàng hóa ";
    private static final String BRAND_LABEL = "Nhãn hiệu ";
    private static final String MODEL_LABEL = "Mẫu mã ";
    private static final String OLD_TEXT_APPENDIX = "hiện tại trên Hợp đồng cho vay là: ";
    private static final String NEW_TEXT_APPENDIX = "thay thế trên Hợp đồng cho vay là: ";
    private static final String NEW_LINE_APPENDIX = "\n";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
//    private static final String[] arrEndowProducts = {"E0000010", "E0000011", "M0000025", "M0000026", "M0000024", "M0000023", "E0000016", "E0000015"};
    private static String endowProductStr = CacheManager.Parameters().findParamValueAsString(ParametersName.GENDOC_ENDOW_PRODUCT_LIST);
    private static String limitProductStr = CacheManager.Parameters().findParamValueAsString(ParametersName.GENDOC_LIMIT_PRODUCT_LIST);
    private static String contractDateStr = CacheManager.Parameters().findParamValueAsString(ParametersName.GENDOC_CONTRACT_DATE);
    private static String repaymentDateStr = CacheManager.Parameters().findParamValueAsString(ParametersName.GENDOC_PERIOD_REPAYMENT_DATE);
    private static List<String> lstEndowProducts = Arrays.asList(endowProductStr.split(COMMA));
    private static List<String> lstLimitProducts = Arrays.asList(limitProductStr.split(COMMA));
    private static List<String> lstContractDate = Arrays.asList(contractDateStr.split(COMMA));
    private static List<String> lstRepaymentDate = Arrays.asList(repaymentDateStr.split(COMMA));

    private BasedHttpClient bs = new BasedHttpClient();
    @Autowired
    MessageSource messageSource;

    public MessageSource messageSource() {
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setBasename("messages");
        messageSource = rbms;
        return messageSource;
    }

    public Map<String, String> genDocProvider(String appId, String typeOfDocument, String typeOfLoans, String userName, String appNum) throws Exception {
        Map<String, String> mapFileGendoc = new HashMap<>();
        VDataEntry dataEntrySelected = null;
        messageSource();
        //kiem tra token va lay config gendocs
        int tam = Integer.parseInt(appNum);
//            Gendocs gendocs = bs.toObjectJson(Gendocs.class, getBodyJson(ParamGenDoc.GENDOCS, getConfigParam(tam, appId))); //bo lay tu bang gendoc
//            if (gendocs != null) {
            int portGendoc = genport(tam);
//                gendocs.setToken(getToken(new Date().toString() + Math.random(), "afterExport"));
//                appLogsService.updateGendocs(gendocs);
            //neu la nhap lieu tap trung thi query ben database nhap lieu tap trung, nguoc lai query ben installmentloan and cashloan
            GendocVersion gen = getFileVersion(Integer.parseInt(appNum), typeOfDocument, typeOfLoans);
            if (gen != null) {
                if (typeOfLoans != null && typeOfLoans.equals(messageSource.getMessage("gendocs.lable.typeOfLoan.ConcentratingDataEntry", null, Locale.getDefault()))) {
//                        dataEntrySelected = vDataEntryService.findByAppDEID(appID);                         
                    dataEntrySelected = bs.toObjectJson(VDataEntry.class, getBodyJson(ParamGenDoc.V_DATA_ENTRY_APPID_DE, getConfigParam(0, appId)));
                } else {
//                        dataEntrySelected = vDataEntryService.findByAppID(appID);
                    dataEntrySelected = bs.toObjectJson(VDataEntry.class, getBodyJson(ParamGenDoc.V_DATA_ENTRY_APPID, getConfigParam(0, appId)));
                    if (dataEntrySelected != null) {
                        String creditContract = messageSource.getMessage("gendocs.lable.creditContract", null, Locale.getDefault());
                        if (creditContract.equals(typeOfDocument)) {
                            updateSignContractDate(appId, dataEntrySelected);
                        }
                    }
                }
                InputStream inputQr = null;
                String creditContract = messageSource.getMessage("gendocs.lable.creditContract", null, Locale.getDefault());
                if (typeOfDocument.equals(creditContract)) {
                    inputQr = getQrCode(dataEntrySelected, userName);
                }

                if (dataEntrySelected != null) {
                    genFileProcess(portGendoc, gen, dataEntrySelected, inputQr, mapFileGendoc);
                }
            }
//            }
        return mapFileGendoc;
    }

    public String getBodyJson(ParamGenDoc hostName, String param) throws Exception {
        ApiResult result = null;
        try {
            switch (hostName) {
                case GENDOCS:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_GENDOCS + param, AcceptType.Json);
                    break;
                case V_DATA_ENTRY_APPNUMBER_DE:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_DATA_ENTRY_APPNUMBER_DE + param, AcceptType.Json);
                    break;
                case V_DATA_ENTRY_APPNUMBER:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_DATA_ENTRY_APPNUMBER + param, AcceptType.Json);
                    break;
                case V_DATA_ENTRY_APPID_DE:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_DATA_ENTRY_APPID_DE + param, AcceptType.Json);
                    break;
                case V_DATA_ENTRY_APPID:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_DATA_ENTRY_APPID + param, AcceptType.Json);
                    break;
                case GENDOC_VERSION:
                    result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_TABLE_GENDOC_VERSION + param, AcceptType.Json);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!result.getStatus()) {
            throw new Exception(result.getBodyContent());
        }

        return result.getBodyContent();
    }

    public String getConfigParam(int appNumber, String appId) {
        String paramGendocs = "";
        if (appNumber != 0) {
            paramGendocs = "appNumber=" + appNumber;
        }
        if (appId != null && !appId.trim().isEmpty()) {
            paramGendocs = (paramGendocs.equals("")) ? paramGendocs : paramGendocs + "&";
            paramGendocs = paramGendocs + "appId=" + appId;
        }
        return paramGendocs;
    }

    public GendocVersion getFileVersion(int appNumber, String typeOfDocument, String typeOfLoan) throws Exception {
        VDataEntry dataEntrySelected;
        String creditContract = messageSource.getMessage("gendocs.lable.creditContract", null, Locale.getDefault());    
        String insurancePaper = messageSource.getMessage("gendocs.lable.insurancePaper", null, Locale.getDefault());
        String operationExport = messageSource.getMessage("gendocs.lable.operationExport", null, Locale.getDefault());
        String loanAmountForm = messageSource.getMessage("gendocs.lable.loanAmountForm", null, Locale.getDefault());
        String SubContract = messageSource.getMessage("gendocs.lable.SubContract", null, Locale.getDefault());
        String creditContractAppendix = messageSource.getMessage("gendocs.lable.creditContract.appendix", null, Locale.getDefault());
        String commitmentInformation = messageSource.getMessage("gendocs.lable.commitment.information", null, Locale.getDefault());
        String yes = messageSource.getMessage("has.insurrance.yes", null, Locale.getDefault());
        String salePaper = messageSource.getMessage("gendocs.lable.sale.paper", null, Locale.getDefault());
        String infactConfirmPaper = messageSource.getMessage("gendocs.lable.infact.confirm.paper", null, Locale.getDefault());
        String certificateOfInstallmentSaleForAgent = messageSource.getMessage("gendocs.lable.certificate.of.installment.sale.for.agent", null, Locale.getDefault());
        String proposalCancelLoan = messageSource.getMessage("gendocs.lable.proposal.cancel.loan", null, Locale.getDefault());
        String repaymentSchedule = messageSource.getMessage("gendocs.lable.repayment.schedule", null, Locale.getDefault());
        String customerInformation = messageSource.getMessage("gendocs.lable.customerInformation", null, Locale.getDefault());

        if (typeOfLoan.equals(CONCENTRATING_DATA_ENTRY)) {
//            dataEntrySelected = vDataEntryDEDAO.findByAppNumber(appNumber);
        	// Bước này: Get thông tin dataEntrySelected nhằm lấy thông tin getTypeOfLoan, getHasInsurrance => lấy ra version của bản gendoc
        	// Hiện tại dữ liệu luồng CONCENTRATING_DATA_ENTRY (customdbde) đồng bộ luôn sang bên customdb => goi chung một api.
            dataEntrySelected = bs.toObjectJson(VDataEntry.class, getBodyJson(ParamGenDoc.V_DATA_ENTRY_APPNUMBER, getConfigParam(appNumber, null)));
        } else {
//            dataEntrySelected = vDataEntryDao.findByAppNumber(appNumber);
            dataEntrySelected = bs.toObjectJson(VDataEntry.class, getBodyJson(ParamGenDoc.V_DATA_ENTRY_APPNUMBER, getConfigParam(appNumber, null)));
            System.out.println(JSONFactory.toJson(dataEntrySelected));
        }
        if (dataEntrySelected != null) {
            String typeSearch = "";
            switch (dataEntrySelected.getTypeOfLoan()) {
                case CASH_LOAN:
                    if (typeOfDocument.equals(creditContract)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                            typeSearch = CONTRACT_CASHLOAN;
                        } else {
                            typeSearch = CONTRACT_CASHLOAN_NO_INSURRANCE;
                        }
                    } else if (typeOfDocument.equals(insurancePaper)) {
                        typeSearch = INSURRANCE_PAPER_MBAL;
                    } else if (typeOfDocument.equals(operationExport)) {
                        typeSearch = OPERATION_CASHLOAN;
                    } else if (typeOfDocument.equals(loanAmountForm)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                            typeSearch = LOANAMOUNT_FORM_HAS_INSURANCE;
                        } else {
                            typeSearch = LOANAMOUNT_FORM_NO_INSURANCE;
                        }
                    } else if (typeOfDocument.equals(SubContract)) {
                        typeSearch = SUB_CONTRACT;
                    }
                    break;
                case INSTALLMENT_LOAN:
                    if (typeOfDocument.equals(creditContract)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                        	if(lstEndowProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                        		typeSearch = CONTRACT_INSTALLMENTLOAN_ENDOW;
                        	} else {
                                typeSearch = CONTRACT_INSTALLMENTLOAN;
                            }
                        } else {
                        	if(lstEndowProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                        		typeSearch = CONTRACT_INSTALLMENTLOAN_ENDOW_NO_INSURRANCE;
                        	} else {
                                typeSearch = CONTRACT_INSTALLMENTLOAN_NO_INSURRANCE;
                            }
                        }
                    } else if (typeOfDocument.equals(insurancePaper)) {
                        typeSearch = INSURRANCE_PAPER_MBAL;
                    } else if (typeOfDocument.equals(operationExport)) {
                    	if(lstEndowProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                            typeSearch = OPERATION_INSTALLMENTLOAN_ENDOW;
                        } else {
                            typeSearch = OPERATION_INSTALLMENTLOAN;
                        }
                    } else if (typeOfDocument.equals(loanAmountForm)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                            typeSearch = LOANAMOUNT_FORM_HAS_INSURANCE;
                        } else {
                            typeSearch = LOANAMOUNT_FORM_NO_INSURANCE;
                        }
                    } else if (typeOfDocument.equals(SubContract)) {
                        typeSearch = SUB_CONTRACT;
                    } else if (typeOfDocument.equals(creditContractAppendix)) {
                        typeSearch = CONTRACT_APPENDIX_INSTALLMENTLOAN;
                    } else if (typeOfDocument.equals(salePaper)) {
                        typeSearch = SALE_PAPER;
                    } else if (typeOfDocument.equals(infactConfirmPaper)) {
                        typeSearch = INFACT_CONFIRM_PAPER;
                    } else if (typeOfDocument.equals(certificateOfInstallmentSaleForAgent)) {
                        typeSearch = CERTIFICATE_OF_INSTALLMENT_SALE_FOR_AGENT;
                    } else if (typeOfDocument.equals(proposalCancelLoan)) {
                        typeSearch = PROPOSAL_CANCEL_LOAN;
                    } else if (typeOfDocument.equals(repaymentSchedule)) {
                        typeSearch = REPAYMENT_SCHEDULE_IL;
                    }
                    break;
                case CONCENTRATING_DATA_ENTRY:
                    if (typeOfDocument.equals(creditContract)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                        	if(lstLimitProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                        		typeSearch = CONTRACT_CASHLOAN_LIMIT_PROD;
                        	} else {
                                typeSearch = CONTRACT_CASHLOAN;
                            }
                        } else {
                        	if(lstLimitProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                        		typeSearch = CONTRACT_CASHLOAN_LIMIT_PROD_NO_INSURRANCE;
                        	} else {
                                typeSearch = CONTRACT_CASHLOAN_NO_INSURRANCE;
                            }
                        }
                    } else if (typeOfDocument.equals(insurancePaper)) {
                    	if(lstLimitProducts.contains(dataEntrySelected.getSchemeProductCode())) {
                    		if(dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                    			typeSearch = INSURRANCE_PAPER_MBAL_LIMIT_PROD;
                    		} else {
                    			throw new ValidationException("KH không mua bảo hiểm, không xuất được hợp đồng bảo hiểm sinh mạng");
                    		}
                    	} else {
                    		typeSearch = INSURRANCE_PAPER_MBAL;
                    	}
                    } else if (typeOfDocument.equals(operationExport)) {
                        typeSearch = OPERATION_CASHLOAN;
                    } else if (typeOfDocument.equals(loanAmountForm)) {
                        if (dataEntrySelected.getHasInsurrance() != null && dataEntrySelected.getHasInsurrance().trim().equals(yes)) {
                            typeSearch = LOANAMOUNT_FORM_HAS_INSURANCE;
                        } else {
                            typeSearch = LOANAMOUNT_FORM_NO_INSURANCE;
                        }
                    } else if (typeOfDocument.equals(SubContract)) {
                        typeSearch = SUB_CONTRACT;
                    } else if (typeOfDocument.equals(commitmentInformation)) {
                        typeSearch = COMMITMENT_INFORMATION;
                    } else if (typeOfDocument.equals(repaymentSchedule)) {
                        typeSearch = REPAYMENT_SCHEDULE_CL;
                    } else if (typeOfDocument.equals(customerInformation)) {
		                typeSearch = CUSTOMER_INFORMATION;
		            }
                    break;
                default:
                    break;
            }
            System.out.println("typeSearch .... : " + typeSearch);
            if (typeSearch.trim().isEmpty()) {
                return null;
            }
            String param = "";
            GendocVersion gendocVersion;
            String dateSearch = "";
            if(typeSearch.equals(CUSTOMER_INFORMATION)) {
            	dateSearch = dataEntrySelected.getCreatedDate() == null ? DateUtil.toString(new Date(), "dd/MM/yyyy") :  dataEntrySelected.getCreatedDate();
            	param = "effectDate=" + dateSearch;
                param = param + "&expiredDate=" + dateSearch;
                param = param + "&typeOfDocument=" + typeSearch;
                System.out.println("===getFileVersion===param:" + param);
                gendocVersion = bs.toObjectJson(GendocVersion.class, getBodyJson(ParamGenDoc.GENDOC_VERSION, param));
                return gendocVersion;
            }
            if(INSTALLMENT_LOAN.equals(dataEntrySelected.getTypeOfLoan()) && 
            		(typeOfLoan.equals(CONTRACT_INSTALLMENTLOAN) || typeOfLoan.equals(CONTRACT_INSTALLMENTLOAN_NO_INSURRANCE) 
            				|| typeOfLoan.equals(CONTRACT_INSTALLMENTLOAN_ENDOW) || typeOfLoan.equals(CONTRACT_INSTALLMENTLOAN_ENDOW_NO_INSURRANCE))) {
            	// Luong tra gop thi thuc hien lay theo ngay day len van hanh
            	dateSearch = dataEntrySelected.getDateGoUpOperation() == null ? DateUtil.toString(new Date(), "dd/MM/yyyy") :  dataEntrySelected.getDateGoUpOperation();
            	param = "effectDate=" + dateSearch;
                param = param + "&expiredDate=" + dateSearch;
                param = param + "&typeOfDocument=" + typeSearch;
            } else {
        		dateSearch = dataEntrySelected.getSignContractDate() == null ? DateUtil.toString(new Date(), "dd/MM/yyyy") :  dataEntrySelected.getSignContractDate();
        		param = "effectDate=" + dateSearch;
                param = param + "&expiredDate=" + dateSearch;
                param = param + "&typeOfDocument=" + typeSearch;
            }
            System.out.println("===getFileVersion===param:" + param);
            gendocVersion = bs.toObjectJson(GendocVersion.class, getBodyJson(ParamGenDoc.GENDOC_VERSION, param));
            System.out.println("===getFileVersion===fileName:" + gendocVersion.getFileName());
            return gendocVersion;
        }
        return null;
    }

    private void updateSignContractDate(String appId, VDataEntry dataEntry) throws Exception {
//        VDataEntry dataEntry = vDataEntryService.findByAppID(appID);
//        VDataEntry dataEntry = bs.toObjectJson(VDataEntry.class, getBodyJson(ParamGenDoc.V_DATA_ENTRY_APPID, getConfigParam(0, appID)));
        String installmentLoan = messageSource.getMessage("gendocs.lable.typeOfLoan.InstallmentLoan", null, Locale.getDefault());
        String cashLoan = messageSource.getMessage("gendocs.lable.typeOfLoan.CashLoan", null, Locale.getDefault());
        if (installmentLoan.equals(dataEntry.getTypeOfLoan())) {
            // Neu la lan dau day len hoac tra ve thay doi ngay hop dong =>
            // reset lai ngay hop dong = thoi diem xuat file
            if (StringUtils.isNullOrEmpty(dataEntry.getPosSubmitTime())
            		|| ("Return".equals(dataEntry.getOperationDecision()) && "1".equals(dataEntry.getChangingDateContract()))) {
//                dataEntryService.updateSignContractDate(now, appID);                
                bs.doPost(this._esbHost + BusinessConstant.GEN_DOC_API_UPDATE_DATA_ENTRY, "{\"appId\": \"" + appId + "\"}", ContentType.Json, AcceptType.Json);
                dataEntry.setSignContractDate(DateUtil.dateToStringVN(new Date()));
            }
        } else if (cashLoan.equals(dataEntry.getTypeOfLoan())) {
            // Neu la lan dau day len hoac tra ve thay doi ngay hop dong =>
            // reset lai ngay hop dong = thoi diem xuat file
            if (StringUtils.isNullOrEmpty(dataEntry.getOperationDecision()) || ("Return".equals(dataEntry.getOperationDecision()) && "1".equals(dataEntry.getChangingDateContract()))
                    || ("Return".equals(dataEntry.getOperationApproveDecision()) && "1".equals(dataEntry.getChangingDateContractApprove()))) {
//                dataEntryService.updateSignContractDate(now, appID);
                bs.doPost(this._esbHost + BusinessConstant.GEN_DOC_API_UPDATE_DATA_ENTRY, "{\"appId\": \"" + appId + "\"}", ContentType.Json, AcceptType.Json);
                dataEntry.setSignContractDate(DateUtil.dateToStringVN(new Date()));
            }
        }
    }

    public InputStream getQrCode(VDataEntry dataEntrySelected, String createdBy) {

        InputStream inputStream = null;
        DefaultHttpClient client = new DefaultHttpClient();
        String urlQrcode = CacheManager.Parameters().findParamValueAsString(ParametersName.GET_QRCODE_URL);
        HttpPost post = new HttpPost(urlQrcode);
//		HttpClient client = HttpClientBuilder.create().build();
//		HttpPost post = new HttpPost("http://sit-mcportal.mcredit.com.vn/mcService/service/v1.0/qr-code");
        post.setHeader("Content-Type", "application/json");
        JSONObject jsonpostData = new JSONObject();
        jsonpostData.put("appNumber", dataEntrySelected.getAppNumber());
        jsonpostData.put("appId", dataEntrySelected.getAppID());
        jsonpostData.put("soHopDong", dataEntrySelected.getContractNumber());
        jsonpostData.put("ngayKy", dataEntrySelected.getSignContractDate());
        jsonpostData.put("tenKhachHang", dataEntrySelected.getCustomerName());
        jsonpostData.put("soCmnd", dataEntrySelected.getCitizenID());
        jsonpostData.put("ngayCapCmnd", dataEntrySelected.getIssueDateCitizenID());
        jsonpostData.put("noiCapCmnd", dataEntrySelected.getIssuePlaceCitizenID());
        jsonpostData.put("diaChiThuongTru", dataEntrySelected.getPermanentResidenceSum());
        jsonpostData.put("dienThoai", dataEntrySelected.getMobilePhone());
        jsonpostData.put("giaBanTheoHoaDonGtgt", dataEntrySelected.getGoodsPrice());
        jsonpostData.put("soTienTraTruoc", dataEntrySelected.getOwnedCapitalApprove());
        jsonpostData.put("soTienVayBangSo", dataEntrySelected.getLoanAmountApprover());
        jsonpostData.put("soTienVayBangChu", dataEntrySelected.getLoanAmountApproverInWord());
        jsonpostData.put("thoiHanVay", dataEntrySelected.getLoanTenorApprover());
        jsonpostData.put("laiSuatChoVayTrongHan", dataEntrySelected.getYearInterest());
        jsonpostData.put("soTienGiaiNgan", dataEntrySelected.getLoanAmountAfterInsurranceApprove());
        jsonpostData.put("soTienPhiBaoHiem", dataEntrySelected.getInsurranceFeeApprove());
        jsonpostData.put("soTaiKhoanGiaiNgan", dataEntrySelected.getAccountNumber());
        jsonpostData.put("daiLyChiHoMC", dataEntrySelected.getChannelName());
        jsonpostData.put("tenHangHoa", dataEntrySelected.getTypeOfGoods());
        jsonpostData.put("tenNhanHieuMauMa", dataEntrySelected.getBrand() + " - " + dataEntrySelected.getModel());
        jsonpostData.put("soKhungSoMay", dataEntrySelected.getNoOfFrame() + " - " + dataEntrySelected.getNumberOfSerial());
        jsonpostData.put("createdBy", createdBy);
        System.out.println("=========jsonpostData===============" + jsonpostData.toString());
        try {
            StringEntity postingString = new StringEntity(jsonpostData.toString(), "UTF-8");
            post.setEntity(postingString);
            HttpResponse response = client.execute(post);
            inputStream = response.getEntity().getContent();
            System.out.println("=========inputQr===============" + response.getEntity().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private void genFileProcess(int ports, GendocVersion gen, VDataEntry vde, InputStream inputQr, Map mapFileGendoc) throws Exception {
        /*co the sua khong tim file trong application ma theo duong dan file trong gendocversion co path cua file de read file docx*/
        String url = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "gendoc_temp" + File.separator;
//            File filess = new File(gen.getFilePath());
        File filess = new File(url + gen.getFileName());
        InputStream fis = new FileInputStream(filess);
        Docx docx = new Docx(fis);
        docx.setVariablePattern(new VariablePattern("${", "}"));
        System.out.println("RS: [" + ( org.apache.commons.lang3.StringUtils.isNoneBlank((String) null) ) + "]");
        List<String> docVariables = docx.findVariables();
        Variables variables = new Variables();
        Map<String, EntityBean> map = buildEntiyFromVariableNames(vde.getAppID(), docVariables);
        Iterator<Entry<String, EntityBean>> it = map.entrySet().iterator();
        if (vde.getTypeOfLoan().equals(messageSource.getMessage("gendocs.lable.typeOfLoan.ConcentratingDataEntry", null, Locale.getDefault()))) {
            variables = genVariablesFromEntityDE(it, vde);
        } else {
            variables = genVariablesFromEntity(it, vde);
        }
        if (inputQr != null) {
            XWPFParagraph par = docx.getXWPFDocument().getLastParagraph();
            XWPFRun run = par.createRun();
            run.addBreak();
            System.out.println("=========inputQr===============" + inputQr);
            run.addPicture(inputQr, XWPFDocument.PICTURE_TYPE_PNG, "qr.png", Units.toEMU(60), Units.toEMU(60));
            run.addBreak(BreakType.TEXT_WRAPPING);
            inputQr.close();
            CTDrawing drawing = run.getCTR().getDrawingArray(0);
            CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();
            CTAnchor anchor = getAnchorWithGraphic(graphicalobject, "qr.png",
                    Units.toEMU(60), Units.toEMU(60),
                    Units.toEMU(0), Units.toEMU(0));
            drawing.setAnchorArray(new CTAnchor[]{anchor});
            drawing.removeInline(0);
        }
        // fill template
        docx.fillTemplate(variables);
        String responeFileName = getResponeFileName(gen, vde);
        // prepare file name            
        Calendar cal = Calendar.getInstance();
        String currentTime = "" + cal.get(Calendar.HOUR) + cal.get(Calendar.MINUTE) + cal.get(Calendar.SECOND) + cal.get(Calendar.MILLISECOND);
        String fileNameTempPdf = responeFileName + "_" + currentTime + ".pdf";
        String fileNameDocTemp = responeFileName + "_" + currentTime + ".docx";
        // save to sever
        String realFileNameDocTemp = getRealPathToExport(fileNameDocTemp);
        docx.save(realFileNameDocTemp);

        // re read file
        File fileTempDoc = new File(realFileNameDocTemp);
        String realFileNameTempPdf = getRealPathToExport(fileNameTempPdf);
        File fileTempPdf = new File(realFileNameTempPdf);
        System.out.println("=========DEBUG=PORT=GEN=DOC===============" + ports);
        if (portAvailable(ports)) {
            gendocV2(fileTempDoc, fileTempPdf, ports);
        }
        mapFileGendoc.put("filePath", realFileNameTempPdf);
        mapFileGendoc.put("fileName", fileNameTempPdf);
    }

    private static Map<String, EntityBean> buildEntiyFromVariableNames(String appID, List<String> docVariables) {
        String temp;
        String table;
        String field;
        String variableName;
        EntityBean obj;
        Map<String, EntityBean> map = new HashMap<String, EntityBean>();
        boolean isRepeatTable = false;
        for (String str : docVariables) {
            variableName = str;
            if (variableName.contains("RE_")) {
                isRepeatTable = true;
                temp = str.replace("RE_", "");
            } else {
                temp = str;
                isRepeatTable = false;
            }
            temp = temp.substring(2, temp.length() - 1);
            table = temp.split(":")[0];
            table = table.trim();
            table = "v_" + table;
            field = temp.split(":")[1];
            field = field.trim();
            if (map.containsKey(table)) {
                obj = map.get(table);
                if (!obj.getFieldList().contains(field)) {
                    obj.addField(field);
                }
                obj.getMapVariableName().put(field, variableName);
                if (isRepeatTable) {
                    obj.getMapVariableColum().put(field, new ArrayList<Variable>());
                }

            } else {
                obj = new EntityBean();
                obj.setTableName(table);
                if (!obj.getFieldList().contains(field)) {
                    obj.addField(field);
                }
                obj.getMapVariableName().put(field, variableName);
                if (isRepeatTable) {
                    obj.getMapVariableColum().put(field, new ArrayList<Variable>());
                    obj.setFieldCondition("appID");
                    obj.setFieldConditionValue(appID);
                } else {
                    obj.setFieldCondition("appID");
                    obj.setFieldConditionValue(appID);
                }

                obj.setRepeatTable(isRepeatTable);
                map.put(table, obj);

            }
        }
        return map;
    }

    private static CTAnchor getAnchorWithGraphic(CTGraphicalObject graphicalobject,
            String drawingDescr, int width, int height,
            int left, int top) throws Exception {

        String anchorXML
                = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
                + "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"1\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
                + "<wp:simplePos x=\"0\" y=\"0\"/>"
                + "<wp:positionH relativeFrom=\"column\"><wp:posOffset>" + left + "</wp:posOffset></wp:positionH>"
                + "<wp:positionV relativeFrom=\"paragraph\"><wp:posOffset>" + top + "</wp:posOffset></wp:positionV>"
                + "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
                + "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
                + "<wp:wrapTight wrapText=\"bothSides\">"
                + "<wp:wrapPolygon edited=\"0\">"
                + "<wp:start x=\"0\" y=\"0\"/>"
                + "<wp:lineTo x=\"0\" y=\"21600\"/>" //Square polygon 21600 x 21600 leads to wrap points in fully width x height
                + "<wp:lineTo x=\"21600\" y=\"21600\"/>"// Why? I don't know. Try & error ;-).
                + "<wp:lineTo x=\"21600\" y=\"0\"/>"
                + "<wp:lineTo x=\"0\" y=\"0\"/>"
                + "</wp:wrapPolygon>"
                + "</wp:wrapTight>"
                + "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + drawingDescr + "\"/><wp:cNvGraphicFramePr/>"
                + "</wp:anchor>";

        CTDrawing drawing = CTDrawing.Factory.parse(anchorXML);
        CTAnchor anchor = drawing.getAnchorArray(0);
        anchor.setGraphic(graphicalobject);
        return anchor;
    }

    private String getResponeFileName(GendocVersion gen, VDataEntry vde) {
        return gen.getTypeOfDocument() + "_" + vde.getAppNumber() + "_" + vde.getContractNumber();
    }

    /**
     *
     *
     * @param exporteds
     * @param request
     * @param fileName
     * @return
     */
    public String getRealPathToExport(String fileName) {

        Calendar cal = Calendar.getInstance();
        String serverFilename = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_EXPORT_GENDOC);

        String relativePathToFile = serverFilename + cal.get(Calendar.YEAR)
                + File.separator + (cal.get(Calendar.MONTH) + 1) + File.separator + cal.get(Calendar.DAY_OF_MONTH)
                + File.separator + fileName;
        Path pathToFile = Paths.get(relativePathToFile);
        try {
            File file = new File(relativePathToFile);
            if (file.exists() && !file.isDirectory()) {
                file.delete();
            }
            Files.createDirectories(pathToFile.getParent());
            Files.createFile(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativePathToFile;

    }

    private boolean portAvailable(int port) {
        java.net.ServerSocket ss = null;
        java.net.DatagramSocket ds = null;
        try {
            System.out.println("portAvailable()");
            ss = new java.net.ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new java.net.DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            Logger.getLogger(GendocAggregate.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (ds != null) {
                ds.close();
            }
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
            System.out.println("com.mcredit.business.gendoc.aggregate.GendocAggregate.portAvailable()");
        }
        return false;
    }

    public void gendocV2(File inp, File outp, int port) {
        org.jodconverter.office.LocalOfficeManager officeManager = null;
        try {
            final org.jodconverter.office.LocalOfficeManager.Builder builder = org.jodconverter.office.LocalOfficeManager.builder();
            builder.portNumbers(port);
            builder.killExistingProcess(true);
            builder.maxTasksPerProcess(500);
            officeManager = builder.build();
            System.out.println("gendocV2 start========");
            System.out.println("Url file doc:" + inp);
            officeManager.start();
            long startTime = System.currentTimeMillis();
            new org.jodconverter.OfficeDocumentConverter(officeManager).convert(inp, outp);            
            System.out.println("Successful conversion:" + (System.currentTimeMillis() - startTime));            
        } catch (OfficeException e) {
            Logger.getLogger(GendocAggregate.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (officeManager != null){
                try {
                    officeManager.stop();
                } catch (OfficeException ex) {
                    Logger.getLogger(GendocAggregate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }

    public Variables genVariablesFromEntity(Iterator<Entry<String, EntityBean>> it, VDataEntry vde) throws Exception {
        Variables variables = new Variables();

        TableVariable tableVariable;
        TextVariable textVariable;
        
        // loop through iterator
        while (it.hasNext()) {
            Entry<String, EntityBean> pair = it.next();
            EntityBean entity = (EntityBean) pair.getValue();
//            String sql = entity.buildQuery();            
            String tableName = entity.getTableName();
            String fieldQuery = entity.fieldQuery();
            String clauseWhere = entity.clauseWhere();
            // execute Query
//			SQLQuery query = sessionFactoryUtils.getSession().createSQLQuery(sql);
            ApiResult result = null;
            ObjectMapper mapper = new ObjectMapper();
            List<Object> objList = new ArrayList<>();
            String responseBody = new String();
            if (tableName.equalsIgnoreCase(V_DATA_ENTRY)) {
//                result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY + "columns=" + fieldQuery + "&table=" + tableName + "&where=" + clauseWhere);
            	result = bs.doGet(this._esbHost + BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY + "appId=" + vde.getAppID());
//                responseBody = replaceCharacter(result.getBodyContent());
                Object obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<Object>() {});
//                Object obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<Object>() {
//                });
                objList.add(obj);
                System.out.println("com.mcredit.business.gendoc.aggregate.GendocAggregate.genVariablesFromEntity()" + result.getBodyContent());
            } else if (tableName.equalsIgnoreCase(V_DATA_ENTRY_GENDOC)) {
                try {
                	String urlGendoc = this._esbHost + BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC + "columns=" + fieldQuery + "&table=" + tableName + "&where=" + clauseWhere;
                    result = bs.doGet(urlGendoc);
                    objList = mapper.readValue(result.getBodyContent(), new TypeReference<List<Object>>() {
                    });
                } catch (Exception e) {
                    Object obj = mapper.readValue(result.getBodyContent(), new TypeReference<Object>() {
                    });
                    objList.add(obj);
                }
                System.out.println("com.mcredit.business.gendoc.aggregate.GendocAggregate.genVariablesFromEntity()" + result.getBodyContent());
            } else if(tableName.equalsIgnoreCase(V_REPAYMENT_SCHEDULE)) {
            	try {
                    GendocRepaymentSchedule repaymentSchedule = new GendocRepaymentSchedule();
                    Date valueDate = DateUtil.toDate(vde.getSignContractDate(), date_format_ddMMyyyy);
                    repaymentSchedule.setValueDate(DateUtil.dateToString(valueDate, date_format_YYYYMMDD));
                    repaymentSchedule.setTenor(Integer.parseInt(vde.getLoanTenorApprover().trim()));
                    repaymentSchedule.setIntRate(Double.parseDouble(vde.getYearInterest())/100);
                    repaymentSchedule.setCurrentOutstanding(vde.getLoanAmount().replaceAll(",", "").trim());
                    
            		List<GendocRepaymentScheduleRespone> response = new ArrayList<GendocRepaymentScheduleRespone>();
                    String repaymentUrl = this._esbHost + BusinessConstant.GEN_DOC_API_GET_REPAYMENT_SCHEDULE;
                    System.out.println("===RepaymentPayload===" + JSONConverter.toJSON(repaymentSchedule));
                    result = bs.doPost(repaymentUrl, repaymentSchedule, ContentType.Json);
                    if(!result.getStatus()) {
            			Result resultApi = mapper.readValue(result.getBodyContent(), new TypeReference<Result>() {});
            			throw new ValidationException(resultApi.getReturnMes());
                    }
                    response = mapper.readValue(result.getBodyContent().toString(), new TypeReference<List<GendocRepaymentScheduleRespone>>() {
                    });
                    for(GendocRepaymentScheduleRespone obj : response) {
                    	GendocRepaymentScheduleView repayView = new GendocRepaymentScheduleView();
                    	repayView.setTeror(obj.getTeror());
                    	repayView.setCollectionServiceFee(formatter.format(obj.getCollectionServiceFee().intValue()));
                    	repayView.setOriginalAmount(formatter.format(obj.getOriginalAmount().intValue()));
                    	repayView.setPayablesMonthly(formatter.format(obj.getPayablesMonthly().intValue()));
                    	repayView.setPrincipalAndMonthlyInterest(formatter.format(obj.getPrincipalAndMonthlyInterest().intValue()));
                    	repayView.setProfitAmount(formatter.format(obj.getProfitAmount().intValue()));
                    	repayView.setDateOfPayment(DateUtil.dateToStringVN(DateUtil.toDate(obj.getDateOfPayment(), date_format_YYYYMMDD)));
                    	Object newObj = mapper.readValue(JSONConverter.toJSON(repayView), new TypeReference<Object>() {
                        });
                    	objList.add(newObj);
                    }
                } catch (Exception e) {
                	if(!result.getStatus()) {
                		Result resultApi = mapper.readValue(result.getBodyContent(), new TypeReference<Result>() {});
	        			throw new ValidationException(resultApi.getReturnMes());
	                }
                	GendocRepaymentScheduleRespone obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<GendocRepaymentScheduleRespone>() {
                    });
                	GendocRepaymentScheduleView repayView = new GendocRepaymentScheduleView();
                	repayView.setTeror(obj.getTeror());
                	repayView.setCollectionServiceFee(formatter.format(obj.getCollectionServiceFee().intValue()));
                	repayView.setOriginalAmount(formatter.format(obj.getOriginalAmount().intValue()));
                	repayView.setPayablesMonthly(formatter.format(obj.getPayablesMonthly().intValue()));
                	repayView.setPrincipalAndMonthlyInterest(formatter.format(obj.getPrincipalAndMonthlyInterest().intValue()));
                	repayView.setProfitAmount(formatter.format(obj.getProfitAmount().intValue()));
                	repayView.setDateOfPayment(DateUtil.dateToStringVN(DateUtil.toDate(obj.getDateOfPayment(), date_format_YYYYMMDD)));
                	Object newObj = mapper.readValue(JSONConverter.toJSON(repayView), new TypeReference<Object>() {
                    });
                	objList.add(newObj);
                }
                System.out.println("===RepaymentSchedule===" + result.getBodyContent());
            } else if(tableName.equalsIgnoreCase("v_data_entry_gendoc_edit")) {
            	String urlGendocEdit = this._esbHost + BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC_EDIT + "table=" + "v_data_entry_gendoc" + "&where=" + clauseWhere;
            	result = bs.doGet(urlGendocEdit);
            	GendocDataAppendixView resultGendocDataApendix = new GendocDataAppendixView(); 
            	resultGendocDataApendix = mapper.readValue(result.getBodyContent().toString(), new TypeReference<GendocDataAppendixView>() {});
            	
                HashMap<String, GendocDataAppendix> lstDataAppend = new HashMap<String, GendocDataAppendix>();
                GendocDataAppendixView appendixView = new GendocDataAppendixView();
                
                if(StringUtils.isNullOrEmpty(resultGendocDataApendix.getDataAppendix())) {
                	appendixView.setDataAppendix(StringUtils.Empty);
                	Object obj = mapper.readValue(JSONConverter.toJSON(appendixView), new TypeReference<Object>() {
                    });
                    objList.add(obj);
                } else {
                	lstDataAppend = mapper.readValue(resultGendocDataApendix.getDataAppendix(), new TypeReference<HashMap<String, GendocDataAppendix>>() {
                    });
                    
                    StringBuilder strAppend = new StringBuilder();
                    if(lstDataAppend.size() == 0) {
                    	appendixView.setDataAppendix(StringUtils.Empty);
                    	Object obj = mapper.readValue(JSONConverter.toJSON(appendixView), new TypeReference<Object>() {
                        });
                        objList.add(obj);
                    } else {
                    	if(resultGendocDataApendix.getTotalTypeOfGood() == 1) {
                    		strAppend = concatAppendix(lstDataAppend);
                    	} else {
                    		strAppend = concatAppendixCombo(lstDataAppend);
                    	}                    	
                    	appendixView.setDataAppendix(strAppend.toString()); 
                    	System.out.println("===GendocDataAppendixView."+ appendixView.getDataAppendix());
                    	Object obj = mapper.readValue(JSONConverter.toJSON(appendixView), new TypeReference<Object>() {
                        });
                        objList.add(obj);
                    }
                }
            }

            Variable varItem;
            if (objList != null && !objList.isEmpty()) {
                for (Object obj : objList) {
                    LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
                    lhm = (LinkedHashMap<String, String>) obj;
                    String value = "";
                    for (String str : entity.getFieldList()) {
                        for (Map.Entry<String, String> entry : lhm.entrySet()) {
                            if (entry.getKey().trim().equalsIgnoreCase(str)) {
                                value = String.valueOf(entry.getValue());
                            }
                        }

                        if (entity.isRepeatTable()) {
                            varItem = new TextVariable(entity.getMapVariableName().get(str), setNotNull(value));
                            entity.getMapVariableColum().get(str).add(varItem);
                        } else {
                            entity.getMapVariableValue().put(str, setNotNull(value));
                        }

                    }
                }
            }
            if (entity.isRepeatTable()) {
                tableVariable = new TableVariable();
                for (String str : entity.getFieldList()) {
                    tableVariable.addVariable(entity.getMapVariableColum().get(str)); 
                }
                variables.addTableVariable(tableVariable);
            } else {
                for (String str : entity.getFieldList()) {
                    textVariable = new TextVariable(entity.getMapVariableName().get(str),
                            entity.getMapVariableValue().get(str));
                    variables.addTextVariable(textVariable);
                }
            }
        }
        return variables;
    }

    private Variables genVariablesFromEntityDE(Iterator<Entry<String, EntityBean>> it, VDataEntry vde) throws Exception {
        Variables variables = new Variables();

        TableVariable tableVariable;
        TextVariable textVariable;
        
        // loop through iterator
        while (it.hasNext()) {
            Entry<String, EntityBean> pair = it.next();
            EntityBean entity = (EntityBean) pair.getValue();
//            String sql = entity.buildQuery();   
            String tableName = entity.getTableName();
            String fieldQuery = entity.fieldQuery();
            String clauseWhere = entity.clauseWhere();

            ApiResult result = null;
            ObjectMapper mapper = new ObjectMapper();
            List<Object> objList = new ArrayList<>();
            String responseBody = new String();
            if (tableName.equalsIgnoreCase(V_DATA_ENTRY)) {
//            	String getGendocDataEntryDEUrl = BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_DE + "columns=" + fieldQuery + "&table=" + tableName + "&where=" + clauseWhere;
            	String getGendocDataEntryDEUrl = BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_DE + "appId=" + vde.getAppID();
            	System.out.println("===genVariablesFromEntityDEUrl===" + getGendocDataEntryDEUrl);
                result = bs.doGet(this._esbHost + getGendocDataEntryDEUrl);
//                responseBody = replaceCharacter(result.getBodyContent());
                Object obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<Object>() {
                });
                objList.add(obj);
                System.out.println("com.mcredit.business.gendoc.aggregate.GendocAggregate.genVariablesFromEntityDE()" + result.getBodyContent());
            } else if (tableName.equalsIgnoreCase(V_DATA_ENTRY_GENDOC)) {
                try {
                	String getGendocDataEntryUrl = BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC + "columns=" + fieldQuery + "&table=" + tableName + "&where=" + clauseWhere;
                	System.out.println("===genVariablesFromEntityDEUrl===" + getGendocDataEntryUrl);
                    result = bs.doGet(getGendocDataEntryUrl);
                    objList = mapper.readValue(result.getBodyContent(), new TypeReference<List<Object>>() {
                    });
                } catch (Exception e) {
                    Object obj = mapper.readValue(result.getBodyContent(), new TypeReference<Object>() {
                    });
                    objList.add(obj);
                }
                System.out.println("com.mcredit.business.gendoc.aggregate.GendocAggregate.genVariablesFromEntityDE()" + result.getBodyContent());
            } else if (tableName.equalsIgnoreCase(V_REPAYMENT_SCHEDULE)) {
                try {
                    GendocRepaymentSchedule repaymentSchedule = new GendocRepaymentSchedule();
                    Date valueDate = DateUtil.toDate(vde.getSignContractDate(), date_format_ddMMyyyy);
                    repaymentSchedule.setValueDate(DateUtil.dateToString(valueDate, date_format_YYYYMMDD));
                    repaymentSchedule.setTenor(Integer.parseInt(vde.getLoanTenorApprover().trim()));
                    repaymentSchedule.setIntRate(Double.parseDouble(vde.getYearInterest())/100);
                    repaymentSchedule.setCurrentOutstanding(vde.getLoanAmount().replaceAll(",", "").trim());
                    
            		List<GendocRepaymentScheduleRespone> response = new ArrayList<GendocRepaymentScheduleRespone>();
                    String repaymentUrl = this._esbHost + BusinessConstant.GEN_DOC_API_GET_REPAYMENT_SCHEDULE;
                    System.out.println("===RepaymentPayloadDE===" + JSONConverter.toJSON(repaymentSchedule));
                    result = bs.doPost(repaymentUrl, repaymentSchedule, ContentType.Json);
                    if(!result.getStatus()) {
            			Result resultApi = mapper.readValue(result.getBodyContent(), new TypeReference<Result>() {});
            			throw new ValidationException(resultApi.getReturnMes());
                    }
                    response = mapper.readValue(result.getBodyContent().toString(), new TypeReference<List<GendocRepaymentScheduleRespone>>() {
                    });
                    for(GendocRepaymentScheduleRespone obj : response) {
                    	GendocRepaymentScheduleView repayView = new GendocRepaymentScheduleView();
                    	repayView.setTeror(obj.getTeror());
                    	repayView.setCollectionServiceFee(formatter.format(obj.getCollectionServiceFee().intValue()));
                    	repayView.setOriginalAmount(formatter.format(obj.getOriginalAmount().intValue()));
                    	repayView.setPayablesMonthly(formatter.format(obj.getPayablesMonthly().intValue()));
                    	repayView.setPrincipalAndMonthlyInterest(formatter.format(obj.getPrincipalAndMonthlyInterest().intValue()));
                    	repayView.setProfitAmount(formatter.format(obj.getProfitAmount().intValue()));
                    	repayView.setDateOfPayment(DateUtil.dateToStringVN(DateUtil.toDate(obj.getDateOfPayment(), date_format_YYYYMMDD)));
                    	Object newObj = mapper.readValue(JSONConverter.toJSON(repayView), new TypeReference<Object>() {
                        });
                    	objList.add(newObj);
                    }
                } catch (Exception e) {
                	if(!result.getStatus()) {
            			Result resultApi = mapper.readValue(result.getBodyContent(), new TypeReference<Result>() {});
            			throw new ValidationException(resultApi.getReturnMes());
                    }
                	GendocRepaymentScheduleRespone obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<GendocRepaymentScheduleRespone>() {
                    });
                	GendocRepaymentScheduleView repayView = new GendocRepaymentScheduleView();
                	repayView.setTeror(obj.getTeror());
                	repayView.setCollectionServiceFee(formatter.format(obj.getCollectionServiceFee().intValue()));
                	repayView.setOriginalAmount(formatter.format(obj.getOriginalAmount().intValue()));
                	repayView.setPayablesMonthly(formatter.format(obj.getPayablesMonthly().intValue()));
                	repayView.setPrincipalAndMonthlyInterest(formatter.format(obj.getPrincipalAndMonthlyInterest().intValue()));
                	repayView.setProfitAmount(formatter.format(obj.getProfitAmount().intValue()));
                	repayView.setDateOfPayment(DateUtil.dateToStringVN(DateUtil.toDate(obj.getDateOfPayment(), date_format_YYYYMMDD)));
                	if(!result.getStatus()) {
            			Result resultApi = mapper.readValue(result.getBodyContent(), new TypeReference<Result>() {});
            			throw new ValidationException(resultApi.getReturnMes());
                    }
                	Object newObj = mapper.readValue(JSONConverter.toJSON(repayView), new TypeReference<Object>() {
                    });
                	objList.add(newObj);
                }
                System.out.println("===RepaymentScheduleDE===" + result.getBodyContent());
            } else if(tableName.equalsIgnoreCase("v_data_entry_de")) {
            	String getDateEntryDe = this._esbHost + BusinessConstant.GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC_DE +"appID=" + vde.getAppID();
                result = bs.doGet(getDateEntryDe, AcceptType.Json);
                Object obj = mapper.readValue(result.getBodyContent().toString(), new TypeReference<Object>() {
                });
                objList.add(obj);
            } else if(tableName.equalsIgnoreCase(V_REPAYMENT_DATE)) {
            	List<String> resultRepayment =  getRepaymentDate(vde.getSignContractDate());
            	textVariable =  new TextVariable("${repayment_date: day}", resultRepayment.get(0));
            	variables.addTextVariable(textVariable);
            	variables.addTextVariable(new TextVariable("${repayment_date: date}", resultRepayment.get(1)));
            }
            Variable varItem;
            if (objList != null && !objList.isEmpty()) {
                for (Object obj : objList) {
                    LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
                    lhm = (LinkedHashMap<String, String>) obj;
                    String value = "";
                    for (String str : entity.getFieldList()) {
                        for (Map.Entry<String, String> entry : lhm.entrySet()) {
                            if (entry.getKey().trim().equalsIgnoreCase(str)) {
                                value = String.valueOf(entry.getValue());
                            }
                        }

                        if (entity.isRepeatTable()) {
                            varItem = new TextVariable(entity.getMapVariableName().get(str), setNotNull(value));
                            entity.getMapVariableColum().get(str).add(varItem);
                        } else {
                            entity.getMapVariableValue().put(str, setNotNull(value));
                        }
                    }
                }
            }
            if (entity.isRepeatTable()) {
                tableVariable = new TableVariable();
                for (String str : entity.getFieldList()) {
                    tableVariable.addVariable(entity.getMapVariableColum().get(str));
                }
                variables.addTableVariable(tableVariable);
            } else {
                for (String str : entity.getFieldList()) {
                	if(entity.getTableName().equalsIgnoreCase(V_REPAYMENT_DATE)) {
                		break;
                	}
                    textVariable = new TextVariable(entity.getMapVariableName().get(str),
                            entity.getMapVariableValue().get(str));
                    variables.addTextVariable(textVariable);
                }
            }
        }
        return variables;
    }

    private String setNotNull(String value) {
        if (value.contains("nil") || value.equalsIgnoreCase("null")) {
            return "";
        }
        return value.toString();
    }
    
    private int genport(int appNumber) {
		if(appNumber<10000 ) {
			return appNumber+10000;
		}else if( appNumber>65000){
			int tam2 = appNumber % 65000;
			if(tam2 <10000) {
				return tam2+10000;
			}else {
				return tam2;
			}
		}else {
			return appNumber;
		}
	}
    
    private StringBuilder concatAppendixCombo(HashMap<String, GendocDataAppendix> hmAppendix) {
    	StringBuilder result = new StringBuilder();
    	Set<String> lstIndex = hmAppendix.keySet();
    	lstIndex.forEach(index -> {
    		GendocDataAppendix appendix = hmAppendix.get(index);
    		if(!StringUtils.isNullOrEmpty(appendix.getGoodCode())) {
        		result.append(GOOD_LABEL + index + SPACE + OLD_TEXT_APPENDIX + appendix.getGoodNameOld() + NEW_LINE_APPENDIX);
        		result.append(GOOD_LABEL + index + SPACE + NEW_TEXT_APPENDIX + appendix.getGoodName() + NEW_LINE_APPENDIX);
        	}
        	if(!StringUtils.isNullOrEmpty(appendix.getBrandCode())) {
        		result.append(BRAND_LABEL + index + SPACE + OLD_TEXT_APPENDIX + appendix.getBrandNameOld() + NEW_LINE_APPENDIX);
        		result.append(BRAND_LABEL + index + SPACE + NEW_TEXT_APPENDIX + appendix.getBrandName() + NEW_LINE_APPENDIX);
        	}
        	if(!StringUtils.isNullOrEmpty(appendix.getModelCode())) {
        		result.append(MODEL_LABEL + index + SPACE + OLD_TEXT_APPENDIX + appendix.getModelNameOld() + NEW_LINE_APPENDIX);
        		result.append(MODEL_LABEL + index + SPACE + NEW_TEXT_APPENDIX + appendix.getModelName() + NEW_LINE_APPENDIX);
        	}
    	});
    	
    	return result;
    }
    
    private StringBuilder concatAppendix(HashMap<String, GendocDataAppendix> hmAppendix) {
    	StringBuilder result = new StringBuilder();
    	Set<String> lstIndex = hmAppendix.keySet();
    	lstIndex.forEach(index -> {
    		GendocDataAppendix appendix = hmAppendix.get(index);
    		if(!StringUtils.isNullOrEmpty(appendix.getGoodCode())) {
        		result.append(GOOD_LABEL + OLD_TEXT_APPENDIX + appendix.getGoodNameOld() + NEW_LINE_APPENDIX);
        		result.append(GOOD_LABEL + NEW_TEXT_APPENDIX + appendix.getGoodName() + NEW_LINE_APPENDIX);
        	}
        	if(!StringUtils.isNullOrEmpty(appendix.getBrandCode())) {
        		result.append(BRAND_LABEL + OLD_TEXT_APPENDIX + appendix.getBrandNameOld() + NEW_LINE_APPENDIX);
        		result.append(BRAND_LABEL + NEW_TEXT_APPENDIX + appendix.getBrandName() + NEW_LINE_APPENDIX);
        	}
        	if(!StringUtils.isNullOrEmpty(appendix.getModelCode())) {
        		result.append(MODEL_LABEL + OLD_TEXT_APPENDIX + appendix.getModelNameOld() + NEW_LINE_APPENDIX);
        		result.append(MODEL_LABEL + NEW_TEXT_APPENDIX + appendix.getModelName() + NEW_LINE_APPENDIX);
        	}
    	});
    	
    	return result;
    }
    
	private List<String> getRepaymentDate(String contractDate) {
    	List<String> result = new ArrayList<String>();
    	Calendar cal = Calendar.getInstance();
    	Calendar cal2 = Calendar.getInstance();
	    cal.setTime(DateUtil.stringToDateByForm(contractDate, date_format_ddMMyyyy));
    	String repaymentDay = lstRepaymentDate.get(lstContractDate.indexOf(String.valueOf(cal.get(Calendar.DAY_OF_MONTH))));
    	cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(repaymentDay));
	    if(cal.get(Calendar.DAY_OF_MONTH) >=1 && cal.get(Calendar.DAY_OF_MONTH) <= 20) {
	    	cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
	    	cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 3);
    	} else if(cal.get(Calendar.DAY_OF_MONTH) >=21 && cal.get(Calendar.DAY_OF_MONTH) <= 31) {
    		if(cal.get(Calendar.MONTH) == 12) {
    	    	cal2.set(Calendar.MONTH, 1);
    	    	cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 4);
    		} else {
    	    	cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
    	    	cal2.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 3);		
    		}
    	}
    	String repaymentDate = DateUtil.dateToStringVN(cal2.getTime());
    	result.add(repaymentDay);
    	result.add(repaymentDate);
    	return result;
    }
    
}
