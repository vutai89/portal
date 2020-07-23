package com.mcredit.sharedbiz.esb;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class MedusaProxy extends BaseManager {
    BasedHttpClient bs = new BasedHttpClient();

    public ApiResult getDataEntry(String contractNumbers) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_CONTRACT_GETDATAENTRY + contractNumbers;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getContract(String where, String whereTotal) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_CONTRACT + "?where=" + where + "&whereTotal=" + whereTotal;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getHistoryPayment(String contractNumber) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_HISTORY_PAYMENT + contractNumber;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getHistoryPaymentT24(String contractNumber) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_HISTORY_PAYMENT_T24 + contractNumber;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getGoodsInfomation(String contractNumber) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_GOODS_INFOMATION + contractNumber;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getCommend(String contractNumber, String commendUser, String fromDate, String toDate,
            String offset, String limit) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_COMMEND + "?contractNumber=" + contractNumber + "&commendUser="
                + commendUser + "&fromDate=" + fromDate + "&toDate=" + toDate + "&offset=" + offset + "&limit=" + limit;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getPaymentScheduleIbank(String contractNumber, String fromDate, String toDate, String offset,
            String limit) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_PAYMENT_SCHEDULE_IBANK + fromDate + "/" + toDate + "/" + offset + "/"
                + limit + "/" + contractNumber;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getPaymentScheduleT24(String contractNumber) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_PAYMENT_SCHEDULE_T24 + contractNumber;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getHistoryAssign(String contractNumber, String offset, String limit) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_HISTORY_ASSIGN + "?contractNumber=" + contractNumber + "&offset=" + offset
                + "&limit=" + limit;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getUser(String user) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_USER_INFO + user;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult addCommend(String request) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_POST_COMMEND_ADD;
        ApiResult result = bs.doPost(url, request, ContentType.Xml, AcceptType.NotSet);
        return result;
    }

    public ApiResult addMemo(String request) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_POST_ADD_MEMO;
        ApiResult result = bs.doPut(url, request, AcceptType.NotSet, ContentType.Xml);
        return result;
    }

    public ApiResult getMemo(String contractNumbers) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_GET_GET_MEMO + "?contractNumbers=" + contractNumbers;
        ApiResult result = bs.doGet(url, ContentType.NotSet);
        return result;
    }

    public ApiResult getEarlyRepaymentInfo(Object request) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_POST_GET_EARLY_REPAYMENT_INFO;
        ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.NotSet);
        return result;
    }

    public ApiResult setEarlyRepaymentSchedule(Object request) throws Exception {
        String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
                + BusinessConstant.MEDUSA_POST_SET_EARLY_REPAYMENT_SCHEDULE;
        ApiResult result = bs.doPost(url, JSONConverter.toJSON(request), ContentType.Json, AcceptType.NotSet);       
        return result;
    }
    
	public ApiResult getMemoAction(String contractNumbers) throws Exception {
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.MEDUSA_GET_GET_MEMO_ACTION + "?contractNumbers=" + contractNumbers;
		ApiResult result = bs.doGet(url, ContentType.NotSet);
		return result;
	}
}
