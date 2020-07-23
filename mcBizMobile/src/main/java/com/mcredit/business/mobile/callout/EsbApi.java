package com.mcredit.business.mobile.callout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.model.object.mobile.dto.BasicAuthenBPMDTO;
import com.mcredit.model.object.mobile.dto.FcmAndroidDataFormatDTO;
import com.mcredit.model.object.mobile.dto.FcmAndroidMessFormatDTO;
import com.mcredit.model.object.mobile.dto.FcmIOSMessDataFormatDTO;
import com.mcredit.model.object.mobile.dto.FcmIOSMessFormatDTO;
import com.mcredit.model.object.mobile.dto.FcmIOSNotificationFormatDTO;
import com.mcredit.model.object.mobile.dto.RefreshTokenMobileDTO;
import com.mcredit.model.object.mobile.dto.SendNoteDTO;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}

	/**
	 * Đẩy luồng trên BPM
	 * 
	 * @author dongtd.ho
	 * @param accessToken:
	 *            mã định danh khi truy cập vào BPM
	 * @param relationId:là
	 *            app_id trên hệ thống BPM
	 * @return ApiResult
	 * @throws Exception
	 */
	public ApiResult routeCaseBPM(String accessToken, String relationId) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient("Bearer", accessToken)) {
			return bs.doPut(this._esbHost + BusinessConstant.MFS_API_ROUTE_CASE_BPM + relationId,
					com.mcredit.util.StringUtils.Empty, AcceptType.Json, ContentType.Json);
		}
	}

	/**
	 * Đăng nhập vào hệ thống BPM lấy thông tin access_token lưu lại trong hệ thống
	 * mobile
	 * 
	 * @author dongtd.ho
	 * @param ExternalUserMapping:
	 *            chứa thông tin xác thực của hệ thống BPM gồm Grant_type, Scope,
	 *            Client_id, Client_secret, Username, Password
	 * @return ApiResult: trả ra kết quả bao gồm các thông tin sau: bodyContent,
	 *         status, code (HttpStatusCode)
	 * @throws Exception
	 */
	public ApiResult authorized(ExternalUserMapping eum) throws Exception {

		BasicAuthenBPMDTO basicAuthenBPMDTO = new BasicAuthenBPMDTO();
		basicAuthenBPMDTO.setGrant_type("password");
		basicAuthenBPMDTO.setScope("*");
		basicAuthenBPMDTO.setClient_id(_cParam.findParamValueAsString(ParametersName.MFS_CLIENT_ID));
		basicAuthenBPMDTO.setClient_secret(_cParam.findParamValueAsString(ParametersName.MFS_CLIENT_SECRET));
		basicAuthenBPMDTO.setUsername(eum.getUserName());
		basicAuthenBPMDTO.setPassword(StringUtils.newStringUtf8(Base64.decodeBase64(eum.getPassword())));

		try (BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doPost(this._esbHost + BusinessConstant.MFS_REFRESH_TOKEN_MOBILE_BPM,
					JSONConverter.toJSON(basicAuthenBPMDTO).toString(), ContentType.Json);
		}
	}

	/**
	 * Gia hạn thời gian hiệu lực của access_token
	 * 
	 * @author dongtd.ho
	 * @param refreshToken:
	 *            thông tin refresh_token của tài khoản cần gia hạn
	 * @return ApiResult: trả ra kết quả bao gồm các thông tin sau: bodyContent,
	 *         status, code (HttpStatusCode)
	 * @throws Exception
	 */
	public ApiResult refreshToken(String refreshToken) throws Exception {
		RefreshTokenMobileDTO refreshTokenMobileDTO = new RefreshTokenMobileDTO();
		refreshTokenMobileDTO.setGrant_type("refresh_token");
		refreshTokenMobileDTO.setScope("*");
		refreshTokenMobileDTO.setClient_id(_cParam.findParamValueAsString(ParametersName.MFS_CLIENT_ID));
		refreshTokenMobileDTO.setClient_secret(_cParam.findParamValueAsString(ParametersName.MFS_CLIENT_SECRET));
		refreshTokenMobileDTO.setRefresh_token(refreshToken);

		try (BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doPost(this._esbHost + BusinessConstant.MFS_REFRESH_TOKEN_MOBILE_BPM,
					JSONConverter.toJSON(refreshTokenMobileDTO).toString(), ContentType.Json);
		}
	}

	/**
	 * Lấy thông tin ghi chú của Case trên BPM
	 * 
	 * @author dongtd.ho
	 * @param appId:
	 *            app_id trên hệ thống BPM.
	 * @return String: nội dung ghi chú của Case
	 * @throws Exception
	 */
	public String getCaseNote(String appId) throws Exception {
		ApiResult result = null;
		try (BasedHttpClient bs = new BasedHttpClient()) {
			result = bs.doGet(this._esbHost + BusinessConstant.MFS_API_GET_NOTE_BPM + appId, AcceptType.Json);
		}

		if (!result.getStatus())
			throw new Exception(result.getBodyContent());

		return result.getBodyContent();
	}

	/**
	 * Tạo mới ghi chú cho Case dựa trên App Id lên trên BPM
	 * 
	 * @author dongtd.ho
	 * @param accessToken:
	 *            token để định danh trên BPM.
	 * @param appId:
	 *            mã số case trên hệ thống BPM.
	 * @param content:
	 *            nội dung ghi chú.
	 * @throws Exception
	 */
	public void addCaseNote(String accessToken, String appId, String content) throws Exception {
		SendNoteDTO sendDto = new SendNoteDTO(content, 0);
		ApiResult result = null;
		try (BasedHttpClient bsToken = new BasedHttpClient("Bearer", accessToken)) {
			result = bsToken.doPost(this._esbHost + BusinessConstant.MFS_API_SEND_CASE_NOTE + appId,JSONConverter.toJSON(sendDto).toString(), ContentType.Json);
		}

		if (!result.getStatus())
			throw new Exception(result.getBodyContent());
	}

	/**
	 * Tạo mới case trên BPM
	 * 
	 * @author dongtd.ho
	 * @param accessToken:
	 *            token để định danh trên BPM.
	 * @param msgRequest:
	 *            toàn bộ thông tin về Case sẽ được tạo trên hệ thống BPM.
	 * @return ApiResult: trả ra kết quả bao gồm các thông tin sau: bodyContent,
	 *         status, code (HttpStatusCode)
	 * @throws Exception
	 */
	public ApiResult createCase(String accessToken, String msgRequest) throws Exception {
		ApiResult resultApi = null;
		try (BasedHttpClient bs = new BasedHttpClient("Bearer", accessToken)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.MFS_API_SYNC_CASE_BPM, msgRequest, ContentType.Json);
		}
		return resultApi;
	}

	/**
	 * Lấy toàn bộ check list của sản phẩm
	 * 
	 * @author dongtd.ho
	 * @param mobileSchemaProductCode:
	 *            mã sản phẩm.
	 * @param msgRequest:
	 *            toàn bộ thông tin về Case sẽ được tạo trên hệ thống BPM.
	 * @return ApiResult: trả ra kết quả bao gồm các thông tin sau: bodyContent,
	 *         status, code (HttpStatusCode)
	 * @throws Exception
	 */
	public ApiResult getCheckList(String mobileSchemaProductCode, String mobileTemResidence,ParametersName processId, ParametersName taskId) throws Exception {
		// hot fix bug
		if (mobileTemResidence.equals("1")) {
			mobileTemResidence = "2"; // co trung
		} else {
			mobileTemResidence = "1"; // khong trung
		}
	String url = this._esbHost + BusinessConstant.MFS_API_CHECK_LIST
			+ _cParam.findParamValueAsString(processId) + "/"
			+ _cParam.findParamValueAsString(taskId) + "/" + mobileSchemaProductCode + "/"
			+ mobileTemResidence;

	try (BasedHttpClient bs = new BasedHttpClient()) {
		return bs.doGet(url, AcceptType.Json);
	}
}

	/**
	 * Hủy case trên BPM
	 * 
	 * @author dongtd.ho
	 * @param accessToken:
	 *            token để định danh trên BPM.
	 * @param app_id:
	 *            mã số Case trên hệ thống BPM.
	 * @param msgRequest:
	 *            toàn bộ thông tin về Case.
	 * @return ApiResult: trả ra kết quả bao gồm các thông tin sau: bodyContent,
	 *         status, code (HttpStatusCode)
	 * @throws Exception
	 */
	public ApiResult abortCase(String accessToken, String app_id, String msgRequest) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient("Bearer", accessToken)) {
			return bs.doPut(this._esbHost + BusinessConstant.MFS_API_ABORT_CASE + app_id, msgRequest, AcceptType.Json,
					ContentType.Json);
		}
	}

	/**
	 * Gửi tin nhắn xuống điện thoại.
	 * 
	 * @author dongtd.ho
	 * @param notificationId:
	 *            id của điện thoại.
	 * @param message:
	 *            nội dung tin nhắn.
	 * @param title:
	 *            tiêu đề của tin nhắn
	 * @return ApiResult
	 * @throws Exception
	 */
	public ApiResult notify(String notificationId, String title, String message, String osType) throws Exception {
		String keyServer = _cParam.findParamValueAsString(ParametersName.MFS_AUTH_KEY_FCM);
		try (BasedHttpClient bs = new BasedHttpClient("key=", keyServer)) {
			Object messFormat = null;
			if("ANDROID".equals(osType)) {
				messFormat = new FcmAndroidMessFormatDTO(new FcmAndroidDataFormatDTO(title, message, "message"), notificationId);
			} else {
				messFormat = new FcmIOSMessFormatDTO(notificationId, true, new FcmIOSMessDataFormatDTO(""), new FcmIOSNotificationFormatDTO(title, message, ""));
			}
			return bs.doPost(this._esbHost + BusinessConstant.MFS_API_SEND_NOTI, messFormat, ContentType.Json);
		}
	}
	
	public ApiResult postThirdParty(String param, String json) throws Exception {
		try(BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doPost("http://dev-esb.mcredit.com.vn:8280" + param, json, ContentType.Json);
		}
	}

	/**
	 * Get Report cho admin
	 * 
	 * @author cuongvt.ho
	 * @param dateExport
	 *            ngày chọn report
	 * @param user
	 *            user đăng nhập
	 * @return List Data report for admin
	 * @throws Exception
	 */
	public List<DataReport> getReport(String dateExport, UserDTO user) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient()) {
			ApiResult result = bs.doGet(initUrl(dateExport, BusinessConstant.DATA_REPORT_API), AcceptType.Json);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(result.getBodyContent().toString(), new TypeReference<List<DataReport>>() {
			});
		}
	}

	/**
	 * Báo cáo phê duyệt
	 * 
	 * @author cuongvt.ho
	 * @param dateExport
	 *            ngày chọn report
	 * @param user
	 *            user đăng nhập
	 * @return Danh sách báo cáo phê duyệt
	 * @throws Exception
	 */
	public List<ApprovalReport> getApprovalReport(String dateExport, UserDTO user) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient()) {
			ApiResult result = bs.doGet(initUrl(dateExport, BusinessConstant.APPROVAL_REPORT_API), AcceptType.Json);
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(result.getBodyContent().toString(), new TypeReference<List<ApprovalReport>>() {
			});
		}
	}

	/**
	 * Hàm khởi tạo URL cho ESB
	 * 
	 * @author cuongvt.ho
	 * @param dateExport
	 *            ngày chọn report
	 * @param resource
	 *            đường dẫn link esb
	 * @return Chuỗi URL cho ESB
	 */
	private String initUrl(String dateExport, String resource) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dateTo = DateUtil.stringToDateReport(dateExport);
		Date dateFrom = DateUtil.cacularDate(dateTo, -7);
		String dateFromString = df.format(dateFrom);
		return CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST) + resource + "?dateFrom="
				+ dateFromString + "&dateTo=" + dateExport;
	}
}
