package com.mcredit.sharedbiz.manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.mcredit.model.dto.appbpm.AppReasonBPMDTO;
import com.mcredit.model.dto.appbpm.AppStatusBPMDTO;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.aggregate.ApplicationBPMAggregate;
import com.mcredit.sharedbiz.validation.AppBPMValidation;
import com.mcredit.util.JSONConverter;

public class AppBPMManager extends BaseManager{
	
	public static final String STATUS_CODE_OK = "200";
	public static final String STATUS_CODE_500 = "500";

	private ApplicationBPMAggregate _agg;
	private AppBPMValidation _appValid = new AppBPMValidation();
	
	
	public AppBPMManager() {
		_agg = new ApplicationBPMAggregate(this.uok);
	}

	/**
	 * Call procedure to update app status
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	public Result updateAppStatus(AppStatusBPMDTO payload) throws Exception {
		return this.tryCatch( () -> {
			this._appValid.validateAppStatusBPM(payload);
			_agg.updateAppStatus(payload);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			System.out.println("Update app status end " + sdf.format(Calendar.getInstance().getTime()) +"==== appid=" + payload.getAppId() + " - status="+payload.getAppStatus());
			return new Result(STATUS_CODE_OK, "Success");
		});

	}  
	
	/**
	 * Call procedure to update app reason
	 * @param payload
	 * @return
	 * @throws Exception 
	 */
	public Result updateAppReason(List<AppReasonBPMDTO> payload) throws Exception {
		return this.tryCatch( () -> {
			this._appValid.validateAppReasonBPM(payload);
			_agg.updateAppReason(payload);
			return new Result(STATUS_CODE_OK, "Success"); 
		});
	}

}
