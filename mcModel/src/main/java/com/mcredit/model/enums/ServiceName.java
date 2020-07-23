package com.mcredit.model.enums;

public enum ServiceName {
    // Telesale
    GET_V1_0_Telesales_Prospect("GET_V1_0_Telesales_Prospect"),
    GET_V1_0_Telesales_Prospect_Call_CustProspectId("GET_V1_0_Telesales_Prospect_Call_CustProspectId"),
    POST_V1_0_Telesales_Prospect_Call("POST_V1_0_Telesales_Prospect_Call"),
    GET_V1_0_Telesales_Prospect_Customer_Permission_UplCusId("GET_V1_0_Telesales_Prospect_Customer_Permission_UplCusId"),
    PUT_V1_0_Telesales_Prospect_Customer("PUT_V1_0_Telesales_Prospect_Customer"),
    GET_V1_0_Telesales_Prospect_Customer_Id("GET_V1_0_Telesales_Prospect_Customer_Id"),
    POST_V1_0_Telesales_Prospect_Import("POST_V1_0_Telesales_Prospect_Import"),
    GET_V1_0_Telesales_Prospect_Import_UplDetailId("GET_V1_0_Telesales_Prospect_Import_UplDetailId"),
    GET_V1_0_Telesales_Prospect_Import("GET_V1_0_Telesales_Prospect_Import"),
    PUT_V1_0_Telesales_Prospect_Import_UplDetailId("PUT_V1_0_Telesales_Prospect_Import_UplDetailId"),
    DELETE_V1_0_Telesales_Prospect_Import_UplDetailId("DELETE_V1_0_Telesales_Prospect_Import_UplDetailId"),
    PUT_V1_0_Telesales_Prospect_Reassign("PUT_V1_0_Telesales_Prospect_Reassign"),
    GET_V1_0_Telesales_Campaigns("GET_V1_0_Telesales_Campaigns"),
    GET_V1_0_Telesales_QueryUploadMaster("GET_V1_0_Telesales_QueryUploadMaster"),
    GET_V1_0_Telesales_QueryAllocationDetail("GET_V1_0_Telesales_QueryAllocationDetail"),
    POST_V1_0_Telesales_AllocationCustomer("POST_V1_0_Telesales_AllocationCustomer"),
    GET_V1_0_Telesales_Teams("GET_V1_0_Telesales_Teams"),
    GET_V1_0_Telesales_Team_Member("GET_V1_0_Telesales_Team_Member"),
    GET_V1_0_Telesales_Team_Leader("GET_V1_0_Telesales_Team_Leader"),
    GET_V1_0_Telesales_Team_Isteamlead("GET_V1_0_Telesales_Team_Isteamlead"),
    GET_V1_0_Code_Table_Call_Status("GET_V1_0_Code_Table_Call_Status"),
    PUT_V1_0_Warehouse_updateStatusReturnDocument("PUT_V1_0_Warehouse_updateStatusReturnDocument"),
    GET_V1_0_WareHouse_seachCaseInput("GET_V1_0_WareHouse_seachCaseInput"),
    GET_V1_0_WareHouse_matrixInput("GET_V1_0_WareHouse_matrixInput"),
    GET_V1_0_Telesales_Search_User_Tsa("GET_V1_0_Telesales_Search_User_Tsa"),
    POST_V1_0_Telesales_Active_User_Tsa("POST_V1_0_Telesales_Active_User_Tsa"),
    POST_V1_0_Telesales_Inactive_User_Tsa("POST_V1_0_Telesales_Inactive_User_Tsa"),
    
    // WareHouse
    POST_V1_0_WareHouse_printLodgeCode("POST_V1_0_WareHouse_printLodgeCode"),
    GET_V1_0_WareHouse_returnCase("GET_V1_0_WareHouse_returnCase"),
    POST_V1_0_WareHouse_allocationDocument("POST_V1_0_WareHouse_allocationDocument"),
    GET_V1_0_WareHouse_remainDocument("GET_V1_0_WareHouse_remainDocument"),
    GET_V1_0_Telesales_contractInfo("GET_V1_0_Telesales_contractInfo"),
    GET_V1_0_Telesales_contractDuplicate("GET_V1_0_Telesales_contractDuplicate"),
    GET_V1_0_Telesales_LeadGenFindCustomerInfo("GET_V1_0_Telesales_LeadGenFindCustomerInfo"),
    GET_V1_0_WareHouse_listDocumentType("GET_V1_0_WareHouse_listDocumentType"),
    GET_V1_0_WareHouse_exportPaperReceipt("GET_V1_0_WareHouse_exportPaperReceipt"),
    POST_V1_0_WareHouse_lodgeDocument("POST_V1_0_WareHouse_lodgeDocument"),
    GET_V1_0_WareHouse_lisPayBackCavet("GET_V1_0_WareHouse_lisPayBackCavet"),
    GET_V1_0_WareHouse_lisPayBackLetter("GET_V1_0_WareHouse_lisPayBackLetter"),
    PUT_V1_0_WareHouse_saveDocument("PUT_V1_0_WareHouse_saveDocument"),
    PUT_V1_0_WareHouse_giveBackCavet("PUT_V1_0_WareHouse_giveBackCavet"),
    PUT_V1_0_WareHouse_changeStatus("PUT_V1_0_WareHouse_changeStatus"),
    POST_V1_0_WareHouse_renewalDocument("POST_V1_0_WareHouse_renewalDocument"),
    PUT_V1_0_WareHouse_updateDocumentsErrors("PUT_V1_0_WareHouse_updateDocumentsErrors"),
    PUT_V1_0_WareHouse_exportHandover("PUT_V1_0_WareHouse_exportHandover"),
    GET_V1_0_WareHouse_checkRecordsCavet("GET_V1_0_WareHouse_checkRecordsCavet"),    
    PUT_V1_0_WareHouse_change_document_type_err("PUT_V1_0_WareHouse_change_document_type_err"),
    PUT_V1_0_WareHouse_input_document("PUT_V1_0_WareHouse_input_document"),
    PUT_V1_0_WareHouse_change_doc_lodge("PUT_V1_0_WareHouse_change_doc_lodge"),
    GET_V1_0_WareHouse_getDocumentDetail("GET_V1_0_WareHouse_getDocumentDetail"),
	PUT_V1_0_WareHouse_approveGiveBackCavet("PUT_V1_0_WareHouse_approveGiveBackCavet"),
	PUT_V1_0_WareHouse_sendGiveBackCavet("PUT_V1_0_WareHouse_sendGiveBackCavet"),
	PUT_V1_0_WareHouse_renewalBorrowedDocument("PUT_V1_0_WareHouse_renewalBorrowedDocument"),
	POST_V1_0_WareHouse_addBorrowedCavet("POST_V1_0_WareHouse_addBorrowedCavet"),
	POST_V1_0_WareHouse_addBorrowedHSKV("POST_V1_0_WareHouse_addBorrowedHSKV"),
	GET_V1_0_WareHouse_matrix("GET_V1_0_WareHouse_matrix"),    
	DELETE_V1_0_WareHouse_deleteDocument("DELETE_V1_0_WareHouse_deleteDocument"),
	GET_V1_0_WareHouse_exportThankLetter("GET_V1_0_WareHouse_exportThankLetter"),    
    PUT_V1_0_WareHouse_updateCavetErrors("PUT_V1_0_WareHouse_updateCavetErrors"),
    GET_V1_0_WareHouse_qrcode("GET_V1_0_WareHouse_qrcode"),    
    PUT_V1_0_WareHouse_qrcode("PUT_V1_0_WareHouse_qrcode"),
    POST_V1_0_QRService_qrcode("PUT_V1_0_WareHouse_qrcode"),
    
    // XSell
    GET_V1_0_XSell_SearchFiles("GET_V1_0_XSell_SearchFiles"),
    GET_V1_0_XSell_SearchInfoBorrow("GET_V1_0_XSell_SearchInfoBorrow"),
    GET_V1_0_XSell_Download_file("GET_V1_0_XSell_Download_file"),
    
    
    //Debt home

    POST_V1_0_Assign_Debt_Home("POST_V1_0_Assign_Debt_Home"),
    GET_V1_0_Debt_Home_Lookup("GET_V1_0_Debt_Home_Lookup"),
    GET_V1_0_DebtHome_Zip_Document("GET_V1_0_DebtHome_Zip_Document"),
    GET_V1_0_Debt_Home_Download_File("GET_V1_0_Debt_Home_Download_File"),
    
       
    PUT_V1_0_XSell_ApprovalUpl("PUT_V1_0_XSell_ApprovalUpl"),
    DELETE_V1_0_XSell_DeleteUpl("DELETE_V1_0_XSell_DeleteUpl"),
	
	// T24
	POST_T24_V1_UpdateCreditContractMC("t24_update_credit_contract_mc"),
	
	// EBS BPM Proxy
	GET_V1_0_Departments_Batch("GET_V1_0_Departments_Batch"),
	GET_V1_0_Departments_Area("GET_V1_0_Departments_Area"),
	GET_V1_0_Departments_Region("GET_V1_0_Departments_Region"),
	GET_V1_0_Departments_History("GET_V1_0_Departments_History"),
	GET_V1_0_Departments_searchDepartments("GET_V1_0_Departments_searchDepartments"),
	PUT_V1_0_Departments_updateDepartmnet("PUT_V1_0_Departments_updateDepartmnet"),
	
	// EBS T24 Proxy
	POST_V1_0_Departments_NapasBankListMC("POST_V1_0_Departments_NapasBankListMC"),
	POST_V1_0_Departments_NapasAccountNameMC("POST_V1_0_Departments_NapasAccountNameMC"),
	POST_V1_0_Departments_MBAccountNameMC("POST_V1_0_Departments_NapasBankListMC"),
	POST_V1_0_Departments_CitadBranchListMC("POST_V1_0_Departments_NapasBankListMC"),
	POST_V1_0_Departments_CitadBankListMC("POST_V1_0_Departments_NapasBankListMC"),
	
	// ESB Medusa
	GET_V1_0_Medusa_Contract_GetDataEntry("GET_V1_0_Medusa_Contract_GetDataEntry"),
	GET_V1_0_Medusa_Contract_GetContract("GET_V1_0_Medusa_Contract_GetContract"),
	GET_V1_0_Medusa_Contract_GetHistoryPayment("GET_V1_0_Medusa_Contract_GetHistoryPayment"),
	GET_V1_0_Medusa_Contract_GetHistoryPaymentT24("GET_V1_0_Medusa_Contract_GetHistoryPaymentT24"),
	GET_V1_0_Medusa_Contract_GetGoodsInfomation("GET_V1_0_Medusa_Contract_GetGoodsInfomation"),
	GET_V1_0_Medusa_Contract_GetCommend("GET_V1_0_Medusa_Contract_GetCommend"),
	GET_V1_0_Medusa_Contract_GetPaymentSchedule("GET_V1_0_Medusa_Contract_GetPaymentSchedule"),
	GET_V1_0_Medusa_Contract_GetHistoryAssign("GET_V1_0_Medusa_Contract_GetHistoryAssign"),
	GET_V1_0_Medusa_Contract_GetUser("GET_V1_0_Medusa_Contract_GetUser"),
	POST_V1_0_Medusa_Contract_AddCommend("POST_V1_0_Medusa_Contract_AddCommend"),
	POST_V1_0_Medusa_Contract_AddMemo("POST_V1_0_Medusa_Contract_AddMemo"),
	GET_V1_0_Medusa_Contract_GetMemo("GET_V1_0_Medusa_Contract_GetMemo"),
	GET_V1_0_Medusa_Contract_GetMemoAction("GET_V1_0_Medusa_Contract_GetMemoAction"),
	
	// ESB Contact Center
	POST_V1_0_Contact_Center_GetCallOutcome("POST_V1_0_Contact_Center_GetCallOutcome"),
	POST_V1_0_Contact_Center_GetBusinessOutcome("POST_V1_0_Contact_Center_GetBusinessOutcome"),
	POST_V1_0_Contact_Center_SetBusinessOutcome("POST_V1_0_Contact_Center_SetBusinessOutcome"),
	
	// Mobile For Sales
	PUT_V1_0_Mobile_getReport("PUT_V1_0_Mobile_getReport"),
    POST_V1_0_Xsell_Import("POST_V1_0_Xsell_Import"),
    PUT_V1_0_XSell_ChangeStatus("PUT_V1_0_XSell_ChangeStatus"),

    GET_V1_0_MOBILE_GET_CASES("GET_V1_0_MOBILE_GET_CASES"),
    GET_V1_0_MOBILE_GET_DASHBOARD_INFO("GET_V1_0_MOBILE_GET_DASHBOARD_INFO"),
    GET_V1_0_MOBILE_DOWNLOAD_PDF("GET_V1_0_MOBILE_DOWNLOAD_PDF"),
    GET_V1_0_MOBILE_GET_PRODUCTS("GET_V1_0_MOBILE_GET_PRODUCTS"),
    GET_V1_0_MOBILE_GET_KIOSKS("GET_V1_0_MOBILE_GET_KIOSKS"),
    GET_V1_0_NOTIFICATIONS("GET_V1_0_NOTIFICATIONS"),
    GET_V1_0_MOBILE_CHECK_CATEGORY("GET_V1_0_MOBILE_CHECK_CATEGORY"),
    GET_V1_0_MOBILE_LIST_CASE_NOTE("GET_V1_0_MOBILE_LIST_CASE_NOTE"),
    GET_V1_0_MOBILE_SEND_CASE_NOTE("GET_V1_0_MOBILE_SEND_CASE_NOTE"),
    GET_V1_0_MOBILE_DAILY_REPORT("GET_V1_0_MOBILE_DAILY_REPORT"),
    GET_V1_0_MOBILE_APPROVAL_REPORT("GET_V1_0_MOBILE_APPROVAL_REPORT"),
    GET_V1_0_MOBILE_CHECK_LIST("GET_V1_0_MOBILE_CHECK_LIST"),
    GET_V1_0_MOBILE_UPLOAD_DOCUMENT("GET_V1_0_MOBILE_UPLOAD_DOCUMENT"),
    GET_V1_0_MOBILE_CANCEL_CASE("GET_V1_0_MOBILE_CANCEL_CASE"),
	GET_V1_0_MOBILE_UPDATE_NOTI_ID("GET_V1_0_MOBILE_UPDATE_NOTI_ID"),

    //Warehouse f2
	PUT_V1_0_WareHouse_exportHandoverBorrow("PUT_V1_0_WareHouse_exportHandoverBorrow"),
	PUT_V1_0_WareHouse_exportHistory("PUT_V1_0_WareHouse_exportHistory"),
	
    // Contact center
    GET_V1_0_Contact_Center_ExtNumber("GET_V1_0_Contact_Center_ExtNumber"),
    POST_V1_0_Contact_Center_Dial("POST_V1_0_Contact_Center_Dial"),
	
	// Biz Admin
	GET_V1_0_AP_GET_USER_ROLES("GET_V1_0_AP_GET_USER_ROLES"), 
	GET_V1_0_AP_GET_ALL_ROLES("GET_V1_0_AP_GET_ALL_ROLES"), 
	GET_V1_0_AP_POST_SET_PERMISSIONS("GET_V1_0_AP_POST_SET_PERMISSIONS"), 
	GET_V1_0_AP_GET_ALL_MENUS("GET_V1_0_AP_GET_ALL_MENUS"), 
	GET_V1_0_AP_GET_ROLE_MENUS("GET_V1_0_AP_GET_ROLE_MENUS"), 
	GET_V1_0_AP_POST_SET_MENUS_ROLE("GET_V1_0_AP_POST_SET_MENUS_ROLE"),
	GET_V1_0_BA_GET_CACHE_TAGS("GET_V1_0_BA_GET_CACHE_TAGS"), 
	GET_V1_0_BA_REFRESH_CACHE("GET_V1_0_BA_REFRESH_CACHE"),
	
	GET_V1_0_MANAGE_PERMISION_SERVICES("GET_V1_0_MANAGE_PERMISION_SERVICES"),
	GET_V1_0_MANAGE_PERMISION_ROLES_SERVICES("GET_V1_0_MANAGE_PERMISION_ROLES_SERVICES"),
	SET_V1_0_MANAGE_PERMISION_ROLES_SERVICES("SET_V1_0_MANAGE_PERMISION_ROLES_SERVICES"),
	GET_V1_0_MANAGE_PERMISION_SEARCH_USERS("GET_V1_0_MANAGE_PERMISION_SEARCH_USERS"),
	PUT_V1_0_MANAGE_PERMISION_CHANGE_STATUS("PUT_V1_0_MANAGE_PERMISION_CHANGE_STATUS"),
	PUT_V1_0_MANAGE_PERMISION_UPDATE_USER("PUT_V1_0_MANAGE_PERMISION_UPDATE_USER"),
	PUT_V1_0_MANAGE_PERMISION_ADD_USER("PUT_V1_0_MANAGE_PERMISION_ADD_USER"),
	GET_V1_0_AP_GET_ALL_FUNCTIONS("GET_V1_0_AP_GET_ALL_FUNCTIONS"),
	GET_V1_0_AP_GET_ROLE_FUNCTIONS("GET_V1_0_AP_GET_ROLE_FUNCTIONS"),

    //ScheduleProduct
    GET_V1_0_Transactions_LstProduct("GET_V1_0_Transactions_LstProduct"),
    POST_V1_0_Transactions("POST_V1_0_Transactions"),
    GET_V1_0_Transactions_Search("GET_V1_0_Transactions_Search"),
    PUT_V1_0_Transactions_Approve("PUT_V1_0_Transactions_Approve"),
	
	// Check CAT
	GET_V1_0_CheckCAT_Sale_Lookup_Company("GET_V1_0_CheckCAT_Sale_Lookup_Company"),
	GET_V1_0_CheckCAT_Risk_Lookup_Company("GET_V1_0_CheckCAT_Risk_Lookup_Company"),
	POST_V1_0_CheckCAT_Sale_Check_Company("POST_V1_0_CheckCAT_Sale_Check_Company"),
	PUT_V1_0_CheckCAT_Sale_Change_Status_Check_CAT("PUT_V1_0_CheckCAT_Sale_Change_Status_Check_CAT"),
	GET_V1_0_CheckCAT_Risk_Comp_Check_CAT("GET_V1_0_CheckCAT_Risk_Comp_Check_CAT"),
	DELETE_V1_0_CheckCAT_Risk_Delete_Company("DELETE_V1_0_CheckCAT_Risk_Delete_Company"),
	POST_V1_0_CheckCAT_Risk_Update_Top_Company("POST_V1_0_CheckCAT_Risk_Update_Top_Company"),
	POST_V1_0_CheckCAT_Risk_Check_Company_Type("POST_V1_0_CheckCAT_Risk_Check_Company_Type"),
	POST_V1_0_CheckCAT_Risk_Check_CAT("POST_V1_0_CheckCAT_Risk_Check_CAT"),
	POST_V1_0_CheckCAT_Risk_Save_CAT("POST_V1_0_CheckCAT_Risk_Save_CAT"),
	CheckCat_Check_User_OnPage("CheckCat_Check_User_OnPage"),
	
	// PCB
	GET_V1_0_PCB_SHOW_INFO("GET_V1_0_PCB_SHOW_INFO"),
	POST_V1_0_GET_EARLY_REPAYMENT_INFO("POST_V1_0_GET_EARLY_REPAYMENT_INFO"),
	POST_V1_0_SET_EARLY_REPAYMENT_SCHEDULE("POST_V1_0_SET_EARLY_REPAYMENT_SCHEDULE"),


	// Product Manager 
	GET_V1_0_PRD_HISTORY("GET_V1_0_PRD_HISTORY"),
	GET_V1_0_PRD_LST_PRD_CONFIG("GET_V1_0_PRD_LST_PRD_CONFIG"),
	GET_V1_0_PRD_LST_PRD_BY("GET_V1_0_PRD_LST_PRD_BY"),
	GET_V1_0_PRD_CODE_TABLE_BY("GET_V1_0_PRD_CODE_TABLE_BY"),
	GET_V1_0_PRD_CODE_TABLE_BY_CONFIG("GET_V1_0_PRD_CODE_TABLE_BY_CONFIG"),
	GET_V1_0_PRD_COMMODITY_DETAILS("GET_V1_0_PRD_COMMODITY_DETAILS"),
	POST_V1_0_PRD_UPDATE("POST_V1_0_PRD_UPDATE"),
	POST_V1_0_PRD_DELETE_SCHEME_GR("POST_V1_0_PRD_DELETE_SCHEME_GR"),
	POST_V1_0_PRD_INS_PRD_CONFIG("POST_V1_0_PRD_INS_PRD_CONFIG"),
	POST_V1_0_PRD_INS_COMM("POST_V1_0_PRD_INS_COMM"),
	POST_V1_0_PRD_INS_EXCEL("POST_V1_0_PRD_INS_EXCEL"),
	
	// Check CIC
	GET_V1_0_REQUEST_CHECK_CIC("GET_V1_0_REQUEST_CHECK_CIC"),
	GET_V1_0_SEARCH_CIC_RESULT("GET_V1_0_SEARCH_CIC_RESULT"),
	GET_V1_0_CLAIM_REQUEST("GET_V1_0_CLAIM_REQUEST"),
	POST_V1_0_UPDATE_CIC_RESULT("POST_V1_0_UPDATE_CIC_RESULT"),
	
	// Lookup old contract
	GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT("GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT"),
	GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_GET_PCB("GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_GET_PCB"),
	GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_FILE("GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_FILE"),
	GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_FILE("GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_FILE"),
	GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_ALL_FILE("GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_ALL_FILE"),
	
	//Blacklist
	POST_V1_0_SAVE_BLACKLIST("POST_V1_0_SAVE_BLACKLIST")
	;

    private Object value;

    ServiceName(Object value) {
        this.value = value;
    }

    public String stringValue() {
        return this.value.toString();
    }

}
