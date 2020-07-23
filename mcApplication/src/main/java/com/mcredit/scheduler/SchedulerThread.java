package com.mcredit.scheduler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import com.mcredit.common.MCLogger;
import com.mcredit.data.UnitOfWorkCommon;
import com.mcredit.data.entity.Scheduler;
import com.mcredit.data.entity.SchedulerInstance;

public class SchedulerThread extends Thread {

	private ThreadManager threadManager;
	private String errMessage;
	
	public SchedulerThread(ThreadManager thread) {
		this.threadManager = thread;
	}
	
	private Date getNextSchedule(Date curSchDate, String frequency, int interval) {
		ZonedDateTime zdt = curSchDate.toInstant().atZone(ZoneId.of(IScheduler.SCHEDULER_TIME_ZONE));
		LocalDateTime ldt = zdt.toLocalDateTime();
		LocalDateTime ldtRet = null;
		if(IScheduler.SCHEDULER_FREQ_HOUR.equals(frequency)) {
			ldtRet = ldt.plusHours(interval);
		} else if(IScheduler.SCHEDULER_FREQ_DAILY.equals(frequency)) {
			ldtRet = ldt.plusDays(interval);
		} else if(IScheduler.SCHEDULER_FREQ_WEEKLY.equals(frequency)) {
			ldtRet = ldt.plusWeeks(interval);
		} else if(IScheduler.SCHEDULER_FREQ_MONTHLY.equals(frequency)) {
			ldtRet = ldt.plusMonths(interval);
		} else if(IScheduler.SCHEDULER_FREQ_YEARLY.equals(frequency)) {
			ldtRet = ldt.plusYears(interval);
		} else if(IScheduler.SCHEDULER_FREQ_MINUTE.equals(frequency)) {
			ldtRet = ldt.plusMinutes(interval);
		} else if(IScheduler.SCHEDULER_FREQ_SECOND.equals(frequency)) {
			ldtRet = ldt.plusSeconds(interval);
		}

		ZonedDateTime zdt1 = ldtRet.atZone(ZoneId.of(IScheduler.SCHEDULER_TIME_ZONE));
		return Date.from(zdt1.toInstant());
	}

	private String callService(String httpUrl) {
		String res = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(httpUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(IScheduler.SCHEDULER_HTTP_METHOD);
			conn.setRequestProperty("Accept", "application/json");
			res = String.valueOf(conn.getResponseCode());
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			MCLogger.info(this, "Output from Restful .... \n");
			String output = "", line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			JSONObject jsonObj = new JSONObject(output);
			this.errMessage = jsonObj.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			MCLogger.error(this, ex.getMessage());
			this.errMessage = ex.toString();
			res = IScheduler.SCHEDULER_RESPONSE_CODE_EXCEPTION;
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		return res;
	}

	private String callProcedure(String procName) {
		String res = null;
		UnitOfWorkCommon uc = new UnitOfWorkCommon();
		try {
			uc.start();
			uc.schedulerRepository().executeProcedure(procName);
			this.errMessage = "";
			res = IScheduler.SCHEDULER_RESPONSE_OK;
		} catch (Exception ex) {
			ex.printStackTrace();
			MCLogger.error(this, ex.getMessage());
			this.errMessage = ex.toString();
			res = IScheduler.SCHEDULER_RESPONSE_CODE_EXCEPTION;
			uc.rollback();
		} finally {
			if(uc != null && IScheduler.SCHEDULER_RESPONSE_OK.equals(res)) {
				uc.commit();
			}
		}
		return res;
	}

	@Override
	public void run() {
		String response = null;
		int threadIdx = this.threadManager.getThreadIndex();
		MCLogger.info(this, threadIdx + "-Thread " + this.threadManager.getThreadName() + " started!");
		while(!this.threadManager.isRequestStop()) {
			try {
				if(this.threadManager.isBusy()) {
					Date schDate = null;
					Scheduler scheduler = this.threadManager.getScheduler();
					MCLogger.info(this, "Got job " + scheduler.getScheduleName() + " to run");
					if(scheduler.getNextScheduleTime() == null) {
						schDate = new Date(scheduler.getStartTime().getTime());
					} else {
						schDate = new Date(scheduler.getNextScheduleTime().getTime());
					}
					MCLogger.info(this, "Schedule time=" + schDate);
					scheduler.setNextScheduleTime(getNextSchedule(schDate, scheduler.getFrequency(), scheduler.getInterval()));
					scheduler.setNumOfRun(scheduler.getNumOfRun()+1);
					scheduler.setStatus(IScheduler.SCHEDULER_STATUS_RUNNING);
					MCLogger.info(this, "Next schedule time=" + scheduler.getNextScheduleTime());
					//Schedule instance set value here
					SchedulerInstance schIns = new SchedulerInstance();
					schIns.setSchedulerId(scheduler.getId());
					schIns.setStatus(IScheduler.SCHEDULER_STATUS_RUNNING);
					UnitOfWorkCommon uc = new UnitOfWorkCommon();
					uc.start();
					uc.schedulerRepository().update(scheduler);
					uc.schedulerRepository().add(schIns);
					uc.commit();
					//Start run
					//IScheduler schObject = (IScheduler) Class.forName(scheduler.getRequest()).getConstructor(null).newInstance();
					//schObject.execute(scheduler);
					if(IScheduler.SCHEDULER_EXEC_TARGET_SERVICE.equals(scheduler.getExecuteTarget())) {
						response = callService(scheduler.getRequest());
					} else if(IScheduler.SCHEDULER_EXEC_TARGET_PROCEDURE.equals(scheduler.getExecuteTarget())) {
						response = callProcedure(scheduler.getRequest());
					} else {
						response = IScheduler.SCHEDULER_RESPONSE_ERR_INVALID_EXEC_TARGET;
						this.errMessage = "Invalid execution target parameter";
						MCLogger.error(this, "Invalid execution target");
					}
					MCLogger.info(this, "Job response code : " + response);

					scheduler.setStatus(IScheduler.SCHEDULER_STATUS_STOP);
					//Update database
					UnitOfWorkCommon uc1 = new UnitOfWorkCommon();
					uc1.start();
					uc1.schedulerRepository().update(scheduler);
					schIns = uc1.get(schIns.getId());
					//Schedule instance update here
					Calendar cal = Calendar.getInstance();
					schIns.setEndTime(cal.getTime());
					schIns.setResponseCode(response);
					if(!IScheduler.SCHEDULER_RESPONSE_OK.equals(response)) {
						schIns.setStatus(IScheduler.SCHEDULER_STATUS_ERROR);
					} else {
						schIns.setStatus(IScheduler.SCHEDULER_STATUS_SUCCESS);
					}
					schIns.setMessage(this.errMessage.substring(0, (this.errMessage.length() > IScheduler.SCHEDULER_RESPONSE_MESSAGE_LENGTH ? IScheduler.SCHEDULER_RESPONSE_MESSAGE_LENGTH : this.errMessage.length())));
					uc1.schedulerRepository().update(schIns);
					uc1.commit();
					this.threadManager.setBusy(false);
				}
				Thread.sleep(IScheduler.SCHEDULER_THREAD_INTERVAL);
			} catch (Exception ex) {
				ex.printStackTrace();
				MCLogger.error(this, ex.getMessage());
			}
		}
		MCLogger.info(this, threadIdx + "-Thread " + this.threadManager.getThreadName() + " stopped!");
	}
}
