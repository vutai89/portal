package com.mcredit.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.mcredit.data.UnitOfWorkCommon;
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(Date.from(zdt1.toInstant()));
		return cal.before(Calendar.getInstance()) ? Calendar.getInstance().getTime() : Date.from(zdt1.toInstant());
	}

	private String callService(String httpUrl) {
		String res = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			URL url = new URL(httpUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(IScheduler.SCHEDULER_HTTP_METHOD);
			conn.setRequestProperty("Accept", "application/json");
			res = String.valueOf(conn.getResponseCode());
			br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			System.out.println("INFO Output from Restful ....");
			String output = "", line;
			while ((line = br.readLine()) != null) {
				output = output + line;
			}
			JSONObject jsonObj = new JSONObject(output);
			this.errMessage = jsonObj.toString();
		} catch (Exception ex) {
			System.out.println("ERROR message=" + ex.getMessage());
			ex.printStackTrace();
			this.errMessage = ex.toString();
			res = IScheduler.SCHEDULER_RESPONSE_CODE_EXCEPTION;
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException ioe) {
				System.out.println("ERROR: IO exception when close BufferedReader. Message=" + ioe.getMessage());
				ioe.printStackTrace();
			}
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
			System.out.println("ERROR message=" + ex.getMessage());
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
		SchedulerDTO scheduler = null;
		int threadIdx = this.threadManager.getThreadIndex();
		System.out.println("INFO Thread-" + threadIdx + " --- " + this.threadManager.getThreadName() + " started!");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			if(this.threadManager.isBusy()) {
				Date schDate = null;
				scheduler = this.threadManager.getScheduler();
				System.out.println("INFO ["+this.threadManager.getThreadName()+"] Got job " + scheduler.getScheduleName() + " to run");
				schDate = sdf.parse(scheduler.getNextScheduleTime());
				System.out.println("INFO ["+this.threadManager.getThreadName()+"] Schedule time=" + schDate);
				scheduler.setNumOfRun(scheduler.getNumOfRun()+1);
				scheduler.setStatus(IScheduler.SCHEDULER_STATUS_RUNNING);
				//Schedule instance set value here
				SchedulerInstance schIns = new SchedulerInstance();
				schIns.setSchedulerId(scheduler.getId());
				schIns.setStatus(IScheduler.SCHEDULER_STATUS_RUNNING);
				UnitOfWorkCommon uc = new UnitOfWorkCommon();
				uc.start();
				//uc.schedulerRepository().update(scheduler);
				uc.schedulerRepository().updateScheduler(scheduler);
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
					System.out.println("ERROR ["+this.threadManager.getThreadName()+"] Invalid execution target");
				}
				System.out.println("INFO ["+this.threadManager.getThreadName()+"] got Job response code : " + response);

				scheduler.setStatus(IScheduler.SCHEDULER_STATUS_STOP);
				//Update database
				UnitOfWorkCommon uc1 = new UnitOfWorkCommon();
				uc1.start();
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
				scheduler.setNextScheduleTime(sdf.format(getNextSchedule(schDate, scheduler.getFrequency(), scheduler.getInterval())));
				System.out.println("INFO ["+this.threadManager.getThreadName()+"] Next schedule time=" + scheduler.getNextScheduleTime());
				uc1.schedulerRepository().updateScheduler(scheduler);
				uc1.schedulerRepository().update(schIns);
				uc1.commit();
				this.threadManager.setBusy(false);
			}
			if(this.threadManager.isLongRun()) {
				Thread.sleep(IScheduler.SCHEDULER_THREAD_INTERVAL);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("ERROR ["+this.threadManager.getThreadName()+"] unknown exception message="+ex.getMessage());
			try {
				scheduler.setStatus(IScheduler.SCHEDULER_STATUS_STOP);
				UnitOfWorkCommon uc9 = new UnitOfWorkCommon();
				uc9.start();
				uc9.schedulerRepository().updateScheduler(scheduler);
				uc9.commit();
			} catch (Throwable th) {
				System.out.println("ERROR ["+this.threadManager.getThreadName()+"] update database message="+ex.getMessage());
			}
		}
		System.out.println("INFO Thread-" + threadIdx + " --- " + this.threadManager.getThreadName() + " stopped!");
	}
}
