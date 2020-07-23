package com.mcredit.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import com.mcredit.common.JustOneLock;
import com.mcredit.data.UnitOfWorkCommon;
import com.mcredit.data.entity.Scheduler;

// java -cp mcApplication-0.0.1-SNAPSHOT-shaded.jar com.mcredit.scheduler.SchedulerMain "001" > scheduler.out &

public class SchedulerMain implements Observer {

	private static String schGroup;
	private static List<Scheduler> schedulers;
	private static List<SchedulerDTO> scheduleCache;
	private static List<ThreadManager> threads;
	private static List<SchedulerThread> workerThreads;
	private static boolean running = true;
	private static boolean longRun = false;

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		if(args.length < 1) {
			System.out.println("Please pass in scheduler group for this instance");
			return;
		}
		if((args.length == 2) && ("Y".equals(args[1]))) {
			longRun = true;
		}
		schGroup = args[0];
		// Check Scheduler already running
		JustOneLock ua = null;
		if(longRun) {
			ua = new JustOneLock("SchedulerLockingThread" + schGroup);
	        if (ua.isAppActive()) {
	            System.out.println("Scheduler already running. Exit now ...");
	            System.exit(1);
	        }
		}

		int idx = 0;
		//Handle signal
		//new SchedulerMain().new SchedulerShutdownHook().start();
		//Get all schedulers
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String timeStamp = dateFormat.format(cal.getTime());
		System.out.println("Scheduler group "+schGroup+" start at "+timeStamp);
		try {
			UnitOfWorkCommon uc = new UnitOfWorkCommon();
			uc.start();
			if(longRun) {
				scheduleCache = uc.schedulerRepository().getAllActiveScheduler(schGroup);
			} else {
				scheduleCache = uc.schedulerRepository().getAllDueScheduler(schGroup);
			}
			uc.commit();
			//Start all thread
			threads = new ArrayList<ThreadManager>();
			workerThreads = new ArrayList<SchedulerThread>();
			System.out.println("Number of job = "+ scheduleCache.size());
			for(idx=0;idx<scheduleCache.size();idx++) {
				if(!parseTime(scheduleCache.get(idx).getFrequency(), scheduleCache.get(idx).getTimeRange())) {
					continue;
				}
				ThreadManager tm = new ThreadManager(idx);
				tm.setLongRun(longRun);
				tm.setThreadName("Scheduler-"+scheduleCache.get(idx).getId());
				tm.setBusy(true);
				tm.setScheduler(scheduleCache.get(idx));
				threads.add(tm);
				SchedulerThread st = new SchedulerThread(tm);
				workerThreads.add(st);
				st.start();
			}
			//Loop to run schedule
			//processWork();
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}

			running = false;
			System.out.println("Scheduler waiting for all threads stop ...");
			//Send stop request to all thread
//			for(ThreadManager th : threads) {
//				th.setRequestStop(true);
//			}
			//Wait for all threads stop
			for(SchedulerThread st : workerThreads) {
				try {
					System.out.println("Waiting for thread "+st.getId()+" of instance "+timeStamp+" stop ...");
					st.join();
				} catch (Exception ex) {
					System.out.println("Stop thread error : "+ex.getMessage());
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Scheduler got exception. Error="+ex.getMessage());
			System.exit(2);
		}
		System.out.println("Scheduler group "+schGroup+" end at "+dateFormat.format(Calendar.getInstance().getTime())+" for starting at "+timeStamp);
		System.out.println("Scheduler Main exit !");
		System.exit(0);
	}

	private void catchSignal() {
		try {
			final SignalHandler sh = new SignalHandler();
	        sh.addObserver( this );
	        sh.handleSignal( "INT" );
	        sh.handleSignal( "TERM" );
	        Thread.sleep(IScheduler.SCHEDULER_MAIN_INTERVAL);
		} catch (Throwable x) {
			x.printStackTrace();
		}
	}

	  public void update( final Observable o, final Object arg )
	  {
		  // use the same method that the Timer employs to trigger a
	      // rotation, which ensures that signal and timer don't screw
	      // each other up.
	      System.out.println( "Received signal: "+arg );
	      running = false;
	      System.out.println( "running = false");
	  }

	public class SchedulerShutdownHook extends Thread {

		@Override
		public void run() {
			System.out.println("=== scheduler shutdown hook activated");
			while(running) {
				try {
					catchSignal();
				} catch (Exception e) {
					System.out.println("scheduler shutdown hook exception : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	private static int getFreeThread() {
		for(ThreadManager th : threads) {
			if(!th.isBusy()) {
				return th.getThreadIndex();
			}
		}
		return -1;
	}

	private static boolean parseTime(String frequency, String input) {
		LocalDateTime ldt = LocalDateTime.now();
		int min = -1, max = -1;
		List<Integer> intList = new ArrayList<Integer>();

		if(input == null) {
			return true;
		}
		
		if(input.trim().isEmpty() || "0".equals(input.trim())) {
			return true;
		}

		String[] split = input.split("-");
		try {
			if((split.length == 2) && (input.contains("-"))) {
				min = Integer.parseInt(split[0].trim());
				max = Integer.parseInt(split[1].trim());
				if(max >= min) {
					if(IScheduler.SCHEDULER_FREQ_HOUR.equals(frequency)) {
						if((min < 0) || (max > 23) || (max < 0) || (min > 23)) {
							System.out.println("parseTime ERROR. Invalid hour range. From 0 to 23");
							return false;
						}
						if((ldt.getHour() >= min) && (ldt.getHour() <= max)) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_DAILY.equals(frequency)) {
						if((min < 1) || (max > 31) || (max < 1) || (min > 31)) {
							System.out.println("parseTime ERROR. Invalid day range. From 1 to 31");
							return false;
						}
						if((ldt.getDayOfMonth() >= min) && (ldt.getDayOfMonth() <= max)) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_WEEKLY.equals(frequency)) {
						if((min < 1) || (max > 7) || (max < 1) || (min > 7)) {
							System.out.println("parseTime ERROR. Invalid weekday range. From 1(Monday) to 7(Sunday)");
							return false;
						}
						if((ldt.getDayOfWeek().getValue() >= min) && (ldt.getDayOfWeek().getValue() <= max)) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_MONTHLY.equals(frequency)) {
						if((min < 1) || (max > 12) || (max < 1) || (min > 12)) {
							System.out.println("parseTime ERROR. Invalid month range. From 1 to 12");
							return false;
						}
						if((ldt.getMonthValue() >= min) && (ldt.getMonthValue() <= max)) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_MINUTE.equals(frequency)) {
						if((min < 0) || (max > 59) || (max < 0) || (min > 59)) {
							System.out.println("parseTime ERROR. Invalid minute range. From 0 to 59");
							return false;
						}
						if((ldt.getMinute() >= min) && (ldt.getMinute() <= max)) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_SECOND.equals(frequency)) {
						if((min < 0) || (max > 59) || (max < 0) || (min > 59)) {
							System.out.println("parseTime ERROR. Invalid second range. From 0 to 59");
							return false;
						}
						if((ldt.getSecond() >= min) && (ldt.getSecond() <= max)) {
							return true;
						}
					}
				} else {
					System.out.println("parseTime ERROR. Invalid value. From value <= To value");
					return false;
				}
			} else {
				split = input.split(",");
				if(split.length > 0) {
					for(String s : split) {
						intList.add(Integer.parseInt(s.trim()));
					}
					// Verify time is up
					if(IScheduler.SCHEDULER_FREQ_HOUR.equals(frequency)) {
						if(intList.contains(ldt.getHour())) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_DAILY.equals(frequency)) {
						if(intList.contains(ldt.getDayOfMonth())) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_WEEKLY.equals(frequency)) {
						if(intList.contains(ldt.getDayOfWeek().getValue())) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_MONTHLY.equals(frequency)) {
						if(intList.contains(ldt.getMonthValue())) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_MINUTE.equals(frequency)) {
						if(intList.contains(ldt.getMinute())) {
							return true;
						}
					} else if(IScheduler.SCHEDULER_FREQ_SECOND.equals(frequency)) {
						if(intList.contains(ldt.getSecond())) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("parseTime ERROR. Message="+ex.getMessage());
			return false;
		}
		return false;
	}

	private static boolean isSchedulerRunning(Scheduler scheduler) {
		for(ThreadManager th : threads) {
			if(th.isBusy() &&  (scheduler.getId() == th.getScheduler().getId())) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSchedulerReady(Scheduler scheduler) {
		Date current = new Date();
		Date schDate;
		if(IScheduler.SCHEDULER_FREQ_ONCE.equals(scheduler.getFrequency()) && scheduler.getNumOfRun() >= 1) {
			return false;
		}
		if(scheduler.getNextScheduleTime() == null) {
			schDate = new Date(scheduler.getStartTime().getTime());
		} else {
			schDate = new Date(scheduler.getNextScheduleTime().getTime());
		}
		if(current.after(schDate)) {
			if(IScheduler.SCHEDULER_YES.equals(scheduler.getAllowOverlap())) {
				return true;
			} else {
				if(isSchedulerRunning(scheduler)) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

//	private static void processWork() {
//		ThreadManager tm;
//		int i = 0;
//		for(i=0;i<schedulers.size();i++) {
//			Scheduler sch = schedulers.get(i);
//			//Get a scheduler need to run
//			if(isSchedulerReady(sch)) {
//				//Get a free thread
//				int freeThread = getFreeThread();
//				if(freeThread < 0) {
//					//No free thread, start a new thread
//					tm = new ThreadManager(threads.size());
//					threads.add(tm);
//					new SchedulerThread(tm).start();
//				} else {
//					//Acquire a free thread
//					tm = threads.get(freeThread);
//				}
//				//Assign task for thread to run
//				tm.setScheduler(sch);
//				tm.setBusy(true);
//			}
//		}
//	}
}
