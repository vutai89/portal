package com.mcredit.alfresco.utils;

import java.util.HashMap;
import java.util.Map;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;

public class Constants {
public static final String ecmHost = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_HOST);

	public static final String ConcentratingDataEntry = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PROCESSID_DE);
	public static final String CashLoan = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PROCESSID_CL);
	public static final String InstallmentLoan = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PROCESSID_IL);
	public static final String loadDocumentPath = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOADDOCUMENTPATH);
	public static final int threadPoolSize = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_THREADPOOLSIZE);
	public static final int partitionSize = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_PARTITIONSIZE);
	
	/** session pool config **/
	public static final int session_pool_maxIdle =ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_SESSION_POOL_MAXIDLE);
	public static final int session_pool_maxTotal = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_SESSION_POOL_MAXTOTAL);
	
	
	/** thread pool config **/
	public static final int thread_pool_maxIdle =  ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_THREAD_POOL_MAXIDLE);
	public static final int thread_pool_maxTotal =  ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_THREAD_POOL_MAXTOTAL);
	
	public static  String buildVersion = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_BUILDVERSION);
	
	public static  String username = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_USERNAME);
	public static  String password = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PASSWORD);
	
	public static  final String localRootFolder =  ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOCALROOTFOLDER);
	public static  final String downloadLink = ecmHost +  "/api/-default-/public/cmis/versions/1.1/atom/content?id={ID}";
	public static  final String workSpace = ecmHost +  "/share/page/site/alfresco-loandocuments-sites/document-details?nodeRef=workspace://SpacesStore/";
	
	public static final String D_MC_LOAN_DOCS = "D:mc:loanDocs";
	public static final String MODEL_RUN_ONCE = "MODEL_RUN_ONCE";
	public static final String MODEL_RUN_DAILY = "MODEL_RUN_DAILY";
	
	public static final String MILITARYPRODUCT = "2";
	public static final String DOT = ".";

	public static final String TYPE_OF_LOAN_DATA_CENTRALIZE = "ConcentratingDataEntry";
	public static final String TYPE_OF_LOAN_INSTALLMENT = "InstallmentLoan";

	public static final String DEFAULT_DOCUMENT_TYPE = "defaultDocumentType";
	public static final String DATA_CENTRALIZE_OPERATION_DOCUMENT_TYPE = "Image";
	public static final String DATA_CENTRALIZE_DEFAULT_FOLDER_NAME = "DataCheckAndOperation";
	public static final String DATA_CENTRALIZE_DEFAULT_DOCUMENT_NAME = "document";

//	defaultDocumentType=defaultDocumentType
//	dcOpDocumentType=Image
//	dcOpFolder=DataCheckAndOperation
//	dcOpDefaultDocumentName=document
	
	public static final String prefixFileServerBPM = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PREFIX_SERVER_BPM_FILE);
	public static final String prefixFileServerDE = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PREFIX_SERVER_DE_FILE);
	public static final String prefixFileServerDC = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PREFIX_SERVER_DEBT_COL);
	public static final String prefixFileServerFAS = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_PREFIX_SERVER_FAS);

	public static final String localDEBasePath = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOCAL_DE_BASE_PATH);

	public static  final String deFtpRemoteUser = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_FTP_REMOTE_USER);
	public static  final String deFtpRemotePassword = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_FTP_REMOTE_PASSWORD);
	public static  final String deFtpRemoteServer = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_FTP_REMOTE_SERVER);
	public static  final int deFtpRemotePort = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_DE_FTP_REMOTE_PORT);
		
	public static  final String deBPMRemoteUser = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_BPM_FTP_REMOTE_USER);
	public static  final String deBPMRemotePassword = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_BPM_REMOTE_PASSWORD);
	public static  final String deBPMRemoteServer = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DE_BPM_FTP_REMOTE_SERVER);
	public static  final int deBPMRemotePort = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_DE_BPM_FTP_REMOTE_PORT);
	public static  final String deBPMRemoteRootFolder = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_REMOTE_DE_BASE_PATH);

	public static final String localILBasePath = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOCAL_IL_BASE_PATH);
	public static  final String ilRemoteUser = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_IL_FTP_REMOTE_USER);
	public static  final String ilRemotePassword = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_IL_FTP_REMOTE_PASSWORD);
	public static  final String ilRemoteServer = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_IL_FTP_REMOTE_SERVER);
	public static  final int ilRemotePort = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_IL_FTP_REMOTE_PORT);
	public static  final String ilRemoteRootFolder = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_REMOTE_IL_BASE_PATH);

	public static final String localFASBasePath = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOCAL_FAS_BASE_PATH);
	public static  final String fasRemoteUser = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_FTP_REMOTE_USER);
	public static  final String fasRemotePassword = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_FTP_REMOTE_PASSWORD);
	public static  final String fasRemoteServer = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_FTP_REMOTE_SERVER);
	public static  final int fasRemotePort = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_FAS_FTP_REMOTE_PORT);
	public static  final String fasRemoteRootFolder = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_REMOTE_FAS_BASE_PATH);
	public static  final String fasFileTestConnection = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_FILE_TEST_CONNECTION);
	public static  final String fasDocumentType = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_DOCUMENT_TYPE);
	public static  final String fasFolder = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_FAS_CONTENT_FOLDER);

	public static final String localDCBasePath = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_LOCAL_DC_BASE_PATH);
	public static  final String dcRemoteUser = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DC_FTP_REMOTE_USER);
	public static  final String dcRemotePassword = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DC_FTP_REMOTE_PASSWORD);
	public static  final String dcRemoteServer = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_DC_FTP_REMOTE_SERVER);
	public static  final int dcRemotePort = ParametersCacheManager.getInstance().findParamValueAsInteger(ParametersName.ECM_DC_FTP_REMOTE_PORT);
	public static  final String dcRemoteRootFolder = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.ECM_REMOTE_DC_BASE_PATH);

	public static final Map<String, String> mapProcessId = new HashMap<String,String>();
	static {
		mapProcessId.put("InstallmentLoan", InstallmentLoan);
		mapProcessId.put("CashLoan", CashLoan);
		mapProcessId.put("ConcentratingDataEntry", ConcentratingDataEntry);
	}
}
