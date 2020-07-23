package com.mcredit.scheduler;

import java.util.HashMap;

import com.mcredit.data.entity.Scheduler;

public interface IScheduler {

	public static final String SCHEDULER_STATUS_STOP = "S";
	public static final String SCHEDULER_STATUS_RUNNING = "R";
	public static final String SCHEDULER_STATUS_ERROR = "E";
	public static final String SCHEDULER_STATUS_SUCCESS = "S";
	public static final String SCHEDULER_YES = "Y";
	public static final String SCHEDULER_NO = "N";
	public static final String SCHEDULER_FREQ_ONCE = "O";
	public static final String SCHEDULER_FREQ_SECOND = "S";
	public static final String SCHEDULER_FREQ_MINUTE = "N";
	public static final String SCHEDULER_FREQ_HOUR = "H";
	public static final String SCHEDULER_FREQ_DAILY = "D";
	public static final String SCHEDULER_FREQ_WEEKLY = "W";
	public static final String SCHEDULER_FREQ_MONTHLY = "M";
	public static final String SCHEDULER_FREQ_YEARLY = "Y";
	public static final String SCHEDULER_EXEC_TARGET_SERVICE = "S";
	public static final String SCHEDULER_EXEC_TARGET_PROCEDURE = "P";

	public static final String SCHEDULER_HTTP_METHOD = "GET";
	public static final String SCHEDULER_HTTP_RESPONSE_FIELD = "responseMessage";

	public static final String SCHEDULER_RESPONSE_CODE_KEY = "responseCode";
	public static final String SCHEDULER_ERROR_CODE_KEY = "errorCode";
	public static final String SCHEDULER_ERROR_MESSAGE_KEY = "errorMessage";
	public static final String SCHEDULER_RESPONSE_OK = "200";
	public static final String SCHEDULER_RESPONSE_ERR_HTTP = "100";
	public static final String SCHEDULER_RESPONSE_CODE_EXCEPTION = "999";
	public static final String SCHEDULER_RESPONSE_ERR_EXCEPTION = "ERR999";
	public static final String SCHEDULER_RESPONSE_ERR_INVALID_EXEC_TARGET = "101";
	public static final int SCHEDULER_RESPONSE_MESSAGE_LENGTH = 254;

	public static final String SCHEDULER_TIME_ZONE = "Asia/Ho_Chi_Minh";
	
	public static final String HTTP_RESPONSE_OK = "200";
	
	public static final int SCHEDULER_MAIN_INTERVAL = 1000;
	public static final int SCHEDULER_THREAD_INTERVAL = 1000;
	//Return resCode, errCode and errMessage
	public HashMap<String, String> execute(Scheduler param);

}
