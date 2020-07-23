package com.mcredit.business.mobile.aggregate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.util.DateUtil;

public class ReportAggregate {
	
	private BasedHttpClient bs = new BasedHttpClient();

	public List<DataReport> getReport(String dateExport, UserDTO user)
			throws Exception {
		ApiResult resultApi = bs.doGet(initUrl(dateExport, BusinessConstant.DATA_REPORT_API), AcceptType.Json);
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.readValue(resultApi.getBodyContent().toString(), new TypeReference<List<DataReport>>() {});
		
	}

	public List<ApprovalReport> getApprovalReport(String dateExport,
			UserDTO user) throws Exception {
		ApiResult resultApi = bs.doGet(initUrl(dateExport, BusinessConstant.APPROVAL_REPORT_API), AcceptType.Json);
		ObjectMapper mapper = new ObjectMapper();
		return  mapper.readValue(resultApi.getBodyContent().toString(), new TypeReference<List<ApprovalReport>>() {});
	}
	
	private String initUrl(String dateExport, String resource) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTo = DateUtil.stringToDateReport(dateExport);
		Date dateFrom = DateUtil.cacularDate(dateTo, -7);
		String dateFromString = df.format(dateFrom);
		return CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ resource + "?dateFrom=" + dateFromString + "&dateTo=" + dateExport;
	}
}
