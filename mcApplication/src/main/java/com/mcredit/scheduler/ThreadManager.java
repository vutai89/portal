package com.mcredit.scheduler;

import java.lang.management.ThreadInfo;

import com.mcredit.data.entity.Scheduler;

public class ThreadManager {

	private long threadId;
	private String threadName;
	private boolean busy;
	private Scheduler scheduler;
	private int threadIndex;
	private boolean requestStop;

	public ThreadManager() {
		this.busy = false;
		this.requestStop = false;
	}

	public ThreadManager(int thIdx) {
		this.busy = false;
		this.requestStop = false;
		this.threadIndex = thIdx;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean isBusy) {
		this.busy = isBusy;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public int getThreadIndex() {
		return threadIndex;
	}

	public void setThreadIndex(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	public boolean isRequestStop() {
		return requestStop;
	}

	public void setRequestStop(boolean requestStop) {
		this.requestStop = requestStop;
	}

}
