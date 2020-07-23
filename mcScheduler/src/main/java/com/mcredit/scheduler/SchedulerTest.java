package com.mcredit.scheduler;

import java.util.HashMap;

import com.mcredit.data.entity.Scheduler;

public class SchedulerTest implements IScheduler {

	public HashMap<String, String> execute(Scheduler param) {
		// TODO Auto-generated method stub
		System.out.println("Hello World!");
		HashMap<String, String> ret = new HashMap<String, String>();
		ret.put("response", "000");
		return ret;
	}

}
