package com.mcredit.sharedbiz;

public class BusinessConstant {

	public static final String JSON_NAME_HEADER = "header";
	public static final String JSON_NAME_BODY = "body";
	public static final String JSON_NAME_VALIDATION = "validationMessage";
	public static final String JSON_NAME_ERROR = "error";
	public static final String MCP = "MCP";
	public static final String BPM = "BPM";

	public static final int RESPONSE_OK = 0;
	public static final int RESPONSE_CREATED = 201;
	public static final int BPM_RESPONSE_OK = 200;

	// Service name
	public static final String SERVICE_CREATECUSTOMER = "createCustomer";
	public static final String SERVICE_CREATECARD = "CreateCard";
	public static final String SERVICE_GET_ISSUED_CARD = "get_issue_credit_card";
	public static final String SERVICE_CREATE_ACCOUNT_LINK = "createAccountLink";

	// Api
	public static final String T24_CREATE_CUSSTOMER = "/api/v1.0/card/create_customer";
	public static final String T24_MAKE_TRANSFER = "/api/v1.0/card/maketransfer";

	public static final String T24_GET_NAPAS_BANK_LIST_MC = "/api/v1.0/t24/getNapasBankListMC";
	public static final String T24_GET_NAPAS_ACCOUNT_NAME_MC = "/api/v1.0/t24/getNapasAccountNameMC";
	public static final String T24_GET_MB_ACCOUNT_NAME_MC = "/api/v1.0/t24/getMBAccountNameMC";
	public static final String T24_GET_CITAD_BRANCH_LIST_MC = "/api/v1.0/t24/getCitadBranchListMC";
	public static final String T24_GET_CITAD_BANK_LIST_MC = "/api/v1.0/t24/getCitadBankListMC";
	public static final String T24_POST_UPDATE_CREDIT_CONTRACT_MC = "/api/v1.0/t24/updateCreditContractMC";

	public static final String T24_GET_CREATE_LOAN_DEPOSIT_CREDIT = "/api/v1.0/t24/createLoanDepositCredit";
	public static final String T24_GET_CREATE_LOAN_DEPOSIT_CREDIT_UPSERT = "/api/v1.0/t24/createLoanDepositCreditUpsert";

	// BPM
	public static final String BPM_DEPARTMENT_BATCH = "/api/v1.0/bpm/sales/departments/BATCH";
	public static final String BPM_DEPARTMENT_AREA = "/api/v1.0/bpm/sales/departments/AREA";
	public static final String BPM_DEPARTMENT_REGION = "/api/v1.0/bpm/sales/departments/REGION";
	public static final String BPM_DEPARTMENT_PAYMENT_INFO_UPDATE = "/api/v1.0/bpm/sales/payment_info/update";
	public static final String BPM_DEPARTMENT_HISTORY = "/api/v1.0/bpm/sales/history/departments";
	public static final String BPM_DEPARTMENT_SEARCH = "/api/v1.0/bpm/sales/search/departments";
	public static final String BPM_CASE_GET_LIST_FILE_UPLOAD = "/api/v1.0/bpm/cases/getListFileUpload/";

	// Contact center
	public static final String CONTACT_CENTER_CALL = "/api/v1.0/contact_center/dial";

	// Medusa
	public static final String MEDUSA_GET_CONTRACT_GETDATAENTRY = "/api/v1.0/medusa/contract/getDataEntry/?contractNumbers=";
	public static final String MEDUSA_GET_CONTRACT = "/api/v1.0/medusa/contract/searchContract";
	public static final String MEDUSA_GET_HISTORY_PAYMENT = "/api/v1.0/medusa/debtCollection/";
	public static final String MEDUSA_GET_HISTORY_PAYMENT_T24 = "/api/v1.0/medusa/paymentHistoryT24?contractNumber=";
	public static final String MEDUSA_GET_GOODS_INFOMATION = "/api/v1.0/medusa/contract/getGoodsInfomationByContract/";
	public static final String MEDUSA_GET_COMMEND = "/api/v1.0/medusa/commend/search";
	public static final String MEDUSA_GET_PAYMENT_SCHEDULE_IBANK = "/api/v2.0/payment_schedule/get/";
	public static final String MEDUSA_GET_PAYMENT_SCHEDULE_T24 = "/api/v1.0/t24/payment_schedule/";
	public static final String MEDUSA_GET_HISTORY_ASSIGN = "/api/v1.0/medusa/assign/historyAssign";
	public static final String MEDUSA_GET_USER_INFO = "/api/v1.0/medusa/user/name/";
	public static final String MEDUSA_POST_COMMEND_ADD = "/api/v1.0/medusa/commend/add";
	public static final String MEDUSA_POST_ADD_MEMO = "/api/v1.0/medusa/contract/addMemo";
	public static final String MEDUSA_GET_GET_MEMO = "/api/v1.0/medusa/contract/getMemo";
	public static final String MEDUSA_POST_GET_EARLY_REPAYMENT_INFO = "/api/v1.0/t24/getEarlyRepaymentInfo";
	public static final String MEDUSA_POST_SET_EARLY_REPAYMENT_SCHEDULE = "/api/v1.0/t24/setEarlyRepaymentSchedule";
	public static final String MEDUSA_GET_GET_MEMO_ACTION = "/api/v1.0/medusa/contract/getMemoAction";

	// Contact center
	public static final String CONTACT_CENTER_GET_EXT_NUMBER = "/api/v1.0/contact_center/extNumber/";
	public static final String CONTACT_CENTER_POST_DIAL = "/api/v1.0/contact_center/dial";
	public static final String CONTACT_CENTER_GET_CALL_OUTCOME = "/api/v1.0/contact_center/getCallOutcome";
	public static final String CONTACT_CENTER_GET_BUSINESS_OUTCOME = "/api/v1.0/contact_center/getBusinessOutcome";
	public static final String CONTACT_CENTER_SET_BUSINESS_OUTCOME = "/api/v1.0/contact_center/setBusinessOutcome";

	// Report
	public static final String DATA_REPORT_API = "/api/v1.0/mcportal/dataReportList";
	public static final String APPROVAL_REPORT_API = "/api/v1.0/mcportal/approvalReportList";

	public static final String ESB_SEND_SMS = "/api/v1.0/sms_queue";
	public static final String BPM_UPDATE_CARD_INFO = "/api/v1.0/card/updateBPMfromPortal/";
	public static final String WAY4_CREATE_CARD = "/api/v1.0/card/issuecreditcard";
	public static final String WAY4_GET_CARD = "/api/v1.0/card/getissuecreditcard";
	public static final String WAY4_PAYMENT_CREDIT_CARD = "/api/v1.0/card/paymentcreditcard";
	public static final String PORTAL_SRV_CUSTOMER = "mcServiceCustomer/service/v1.0/customer";

	public static final String PORTAL_SRV_PAYMENT = "mcServicePayment/service/v1.0/payment";
	public static final String PORTAL_SRV_JOB = "mcService/service/v1.0/job";
	public static final String ECM_SHARED_LINKS = "/api/-default-/public/alfresco/versions/1/shared-links";
	public static final String ECM_GET_LIST_SHARED_LINKS = "/api/-default-/public/alfresco/versions/1/shared-links?skipCount=0&maxItems=";

	// Json subject
	public static final String ESB_JSON_SUBJECT_PAYLOAD = "CancalCaseObject";
	public static final String CREATE_CUSTOMER_JSON_SUBJECT_PAYLOAD = "CancalCaseObject";
	public static final String CREATE_SENDSMS_JSON_SUBJECT_PAYLOAD = "SendSMSResultCollection";
	public static final String CREATE_SENDSMS_JSON_ERR = "CancalCaseObject";
	public static final Integer JOB_MAX_RETRY = 10;
	public static final String ESB_RESULT_JSON = "Result";

	// Hosts
	public static final String ESB_HOST = "ESB_HOST";
	public static final String PORTAL_HOST = "PORTAL_HOST";

	// PARAMETER
	public static final String PARAM_CARD_NOTIFY_DATE = "CARD_NOTIFY_DATE";
	public static final String CALENDAR_CARD_NOTIFY_DATE = "CARD_NOTIFY";
	public static final String NOTIFY_TEMPLATE_CARD_MIN_SMS = "NT-CARD-MIN-SMS";
	public static final String NOTIFY_TEMPLATE_CARD_DUE_SMS = "NT-CARD-DUE-SMS";
	public static final String PARAM_FILE_DIRECTORY = "FILE_DIRECOTRY";
	// MESSAGE Log
	public static final String MSG_TRANS_TYPE_SMS = "SMS";
	public static final String MSG_TO_CHANNEL_SMS_GATEWAY = "SMS";
	public static final String MSG_SERVICE_NAME_CARD_NOTIFY = "CARD_NOTIFY";

	// SMS
	public static final String SMS_TYPE_CARD_NOTIFY = "CardNotify";
	public static final String SMS_JSON_ROOT = "SendSMSRequest";

	// Mobile4Sales
	public static final String MFS_API_ROUTE_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/route-case/";
	public static final String MFS_API_SYNC_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/create";
	public static final String MFS_REFRESH_TOKEN_MOBILE_BPM = "/api/v1.0/processmaker_phase2/oauth2/token";
	public static final String MFS_API_GET_NOTE_BPM = "/api/v1.0/mobile_4_sale_phase2/app_notes?appUID=";
	public static final String MFS_API_SEND_CASE_NOTE = "/api/v1.0/mobile_4_sale_phase2/app_notes/note/";
	public static final String MFS_API_CHECK_LIST = "/api/v1.0/processmaker_phase2/product/documents/checklist/";
	public static final String MFS_API_ABORT_CASE = "/api/v1.0/mobile_4_sale_phase2/variable/";
	public static final String MFS_API_SEND_NOTI = "/api/v1.0/mobile_4_sale_phase2/notification";

	// Assign Permissions
	public static final String BIZ_ADMIN_REFRESH_KEY = "BIZ_ADMIN_REFRESH_KEY";

	// Debt Home
	public static final String DEBT_HOME_LOOKUP_FILES = "/api/v1.0/mcportal/debt-home/lookup?appNumber=";
	public static final String DEBT_HOME_GET_FILE_PATH = "/api/v1.0/mcportal/debt-home/files?fileId=";

	// PCB
	public static final String PCB_CHECK_RI_REQ = "/api/v1.0/pcb/RI_Req_Input";
	public static final String PCB_CHECK_CI_REQ = "/api/v1.0/pcb/CI_Req_Input";

	// Gendoc
	public static final String GEN_DOC_API_TABLE_GENDOCS = "/api/v1.0/mcportal/gendocs?";
	public static final String GEN_DOC_API_TABLE_DATA_ENTRY_APPNUMBER_DE = "/api/v1.0/mcportal/data-entry/customde/appNumber?";
	public static final String GEN_DOC_API_TABLE_DATA_ENTRY_APPNUMBER = "/api/v1.0/mcportal/data-entry/custom/appNumber?";
	public static final String GEN_DOC_API_TABLE_DATA_ENTRY_APPID_DE = "/api/v1.0/mcportal/data-entry/customde/appId?";
	public static final String GEN_DOC_API_TABLE_DATA_ENTRY_APPID = "/api/v1.0/mcportal/data-entry/custom/appId?";
	public static final String GEN_DOC_API_TABLE_GENDOC_VERSION = "/api/v1.0/mcportal/gendoc-version?";
	public static final String GEN_DOC_API_UPDATE_DATA_ENTRY = "/api/v1.0/mcportal/contract/updateDataEntry";
	public static final String GEN_DOC_API_QUERY_PARAM_TABLE = "/api/v1.0/mcportal/gendoc-version?";
	public static final String GEN_DOC_API_QUERY_PARAM_TABLE_DE = "/api/v1.0/mcportal/gendoc-version?";
	public static final String GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC = "/api/v1.0/mcportal/gendoc/dynamicDeGendoc?";
	public static final String GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY = "/api/v1.0/mcportal/contact/data-entry?";
	public static final String GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_DE = "/api/v1.0/mcportal/contact/data-entry-de?";
	public static final String GEN_DOC_API_GET_REPAYMENT_SCHEDULE = "/api/v1.0/mcportal/repaymentSchedule/repaymentSchedule";
	public static final String GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC_EDIT = "/api/v1.0/mcportal/gendoc/dynamicDeGendocEdit?";
	public static final String GEN_DOC_API_QUERY_PARAM_V_DATA_ENTRY_GENDOC_DE = "/api/v1.0/mcportal/gendoc/customde/getDataEntry?";

	public static final String QR_CODE_GET_ACCESS_TOKEN_BPM = "/api/v1.0/processmaker_phase2/oauth2/token";

	// active,iactive
	public static final String ACTIVE_PRODUCT = "/api/v1.0/mcportal/product/active";
	public static final String INACTIVE_PRODUCT = "/api/v1.0/mcportal/product/inActive";

	// check rule
	public static final String ESB_CHECK_RULE = "/api/v1.0/mcportal/rule/checkRule";
	public static final String ESB_CHECK_RULES = "/api/v1.0/mcportal/rule/checkRules";

	// Telesale
	public static final String ESB_SEND_OTP = "/api/v2.0/trusting-social/verify";
	public static final String ESB_MARK_ESB = "/api/v2.0/trusting-social/compute_score";
	public static final String ESB_MARK_TS = "/api/v2.0/trusting-social/check_score";
	public static final String ESB_INSERT_CUS_HIS = "/api/v2.0/trusting-social/check_score";
	public static final String ESB_CREATE_CIC = "/api/v1.0/mcportal/check-cic/create?citizenID={0}&customerName={1}&userName={2}";
	public static final String ESB_SEND_LEAD_REPORT = "/api/v2.0/trusting-social/leads/report";
	public static final String ESB_UPDATE_QUALIFY_STATUS = "/api/v2.0/cic_data/update_qualify_status";
	
	// Get score v2
	public static final String ESB_GET_TOKEN_TS = "/api/v3.0/trusting-social/auth/users/login";
	public static final String ESB_SEND_OTP_TS = "/api/v3.0/trusting-social/score/otp_sms_requests/create";
	public static final String ESB_VERIFY_TS = "/api/v3.0/trusting-social/score/otp_sms_requests/verify";
	public static final String ESB_SCORING_TS = "/api/v3.0/trusting-social/score/credit_score_requests/create";
	public static final String ESB_GET_TOKEN_CICDATA = "/api/v2.0/cic_data/token";
	public static final String ESB_SEND_OTP_CICDATA = "/api/v2.0/cic_data/credit/otp";
	public static final String ESB_SCORING_CICDATA = "/api/v2.0/cic_data/credit/query";
	
	// Authen
	public static final String ESB_GET_TOKEN = "/api/v1.0/mcportal/authorization";

	/* CancelCaseBPM */
	public static final String BPM_AUTO_ABOUT_REFESH_TOKEN = "/api/v1.0/processmaker_phase2/oauth2/token";
	public static final String BPM_AUTO_ABOUT_CLAIM_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/claim";
	public static final String BPM_AUTO_ABOUT_ROUTE_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/route-case/{caseId}";
	public static final String BPM_AUTO_ABOUT_REASSIGN_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/reassign";
	public static final String BPM_AUTO_ABOUT_VARIABLE_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/variable";
	public static final String BPM_AUTO_ABOUT_CONTACT_CANCEL_CASE_BPM = "/api/v1.0/processmaker_phase2/contract/cancel";
	public static final String BPM_AUTO_ABOUT_DELETE_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/delete/{caseId}";
	public static final String BPM_AUTO_ABOUT_GET_INSTALLMENTLOAN_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/proCancelCaseIL?pageSize={pageSize}&rowBegin={rowBegin}";
	public static final String BPM_AUTO_ABOUT_GET_CONCENTRATINGDATAENTRY_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/proCancelCaseDE?pageSize={pageSize}&rowBegin={rowBegin}";
	public static final String BPM_AUTO_ABOUT_GET_DELINDEX_CASE_BPM = "/api/v1.0/processmaker_phase2/case/appInfo?appNumber={ltsAppNumber}";
	public static final String BPM_AUTO_ABOUT_CANCEL_CONTRACT_DISBURSEMENT = "/api/v1.0/processmaker_phase2/contract/cancel";
	public static final String BPM_AUTO_ABOUT_CANCEL_CONTRACT_BPM = "/api/v1.0/processmaker_phase2/cases/autoAbort";
	public static final String BPM_AUTO_ABOUT_GET_DONE_CONCENTRATINGDATAENTRY_CASE_BPM = "/api/v1.0/processmaker_phase2/cases/proCancelCaseDEDone?pageSize={pageSize}&rowBegin={rowBegin}";
	public static final String BPM_AUTO_ABOUT_GET_INTEGATION_SERVICES_DISBURSEMENT = "/api/v1.0/processmaker_phase2/contract/disbursement/pending?contractNumbers={contractNumbers}";
	public static final String BPM_AUTO_ABOUT_UDATE_TABLE_CANCELCASE_SERVICES = "/api/v1.0/processmaker_phase2/insert-cancal-case";
	public static final String BPM_AUTO_ABOUT_DELETE_CASE_TRANSACTION_BPM = "/api/v1.0/processmaker_phase2/cases/autoAbort";

	// Rule
	public static final String CAT_TOOL_RULES = "/api/v1.0/mcportal/rule/checkRule";
	
	// Check CIC
	public static final String CIC_CLAIM_NEW_IDENTITY_FROM_BPM = "/api/v1.0/mcportal/check-cic/claim-new-identity?processId={0}&fromDate={1}&toDate={2}";
	
	// Check totalDebt ESB
	public static final String ESB_DISBURSEMENT_TOTAL_PAYMENT = "/api/v1.0/disbursement/totalPaymentAmount?identityNumber={identityNumber}";
	
	// Check Tools
	// Check citizenId
	public static final String ESB_API_CHECK_IDENTIFIER = "/api/v1.0/mcportal/check-identifier?citizenId={0}&productGroup={1}&loanAmount={2}&appNumber={3}";
	// Check duplicate (in check citizenId)
	public static final String ESB_DUPLICATE_CONTRACT = "/api/v1.0/mcportal/duplicateContract?appNumber={0}&citizenId={1}";
	public static final String ESB_GET_EMI_CONTRACT_APPROVED = "/api/v1.0/mcportal/getTotalAMTLoanStatus?identityNumber={0}";
	
	// Get total Debt in MC
	public static final String ESB_TOTAL_DEBT_MC = "/api/v1.0/disbursement/totalOSBalance?identityNumber={0}&militaryId={1}&identityNumberOld={2}";

}
