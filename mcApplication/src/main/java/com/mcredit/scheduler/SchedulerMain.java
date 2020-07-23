package com.mcredit.scheduler;

import java.util.ArrayList;
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
	private static List<ThreadManager> threads;
	private static List<SchedulerThread> workerThreads;
	private static boolean running = true;

	public static void main(final String[] args) {
		// TODO Auto-generated method stub
		if(args.length < 1) {
			System.out.println("Please pass in scheduler group for this instance");
			return;
		}
		schGroup = args[0];
		// Check Scheduler already running
		JustOneLock ua = new JustOneLock("SchedulerLockingThread" + schGroup);
        if (ua.isAppActive()) {
            System.out.println("Scheduler already running. Exit now ...");
            System.exit(1);
        }
		
		int idx = 0;
		//Handle signal
		//new SchedulerMain().new SchedulerShutdownHook().start();
		//Get all schedulers
		try {
			UnitOfWorkCommon uc = new UnitOfWorkCommon();
			uc.start();
			schedulers = uc.schedulerRepository().getAllActiveScheduler(Scheduler.class, schGroup);
			uc.commit();
			//Start all thread
			threads = new ArrayList<ThreadManager>();
			workerThreads = new ArrayList<SchedulerThread>();
			System.out.println("Number of job = "+ schedulers.size());
			for(idx=0;idx<schedulers.size();idx++) {
				ThreadManager tm = new ThreadManager(idx);
				threads.add(tm);
				SchedulerThread st = new SchedulerThread(tm);
				workerThreads.add(st);
				st.start();
			}
			//Loop to run schedule
			processWork();
			try {
				Thread.sleep(2000);
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println(ex.getMessage());
			}

			running = false;
			System.out.println("Scheduler stopping ...");
			//Send stop request to all thread
			for(ThreadManager th : threads) {
				th.setRequestStop(true);
			}
			//Wait for all threads stop
			for(SchedulerThread st : workerThreads) {
				try {
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

	private static void processWork() {
		ThreadManager tm;
		int i = 0;
		for(i=0;i<schedulers.size();i++) {
			Scheduler sch = schedulers.get(i);
			//Get a scheduler need to run
			if(isSchedulerReady(sch)) {
				//Get a free thread
				int freeThread = getFreeThread();
				if(freeThread < 0) {
					//No free thread, start a new thread
					tm = new ThreadManager(threads.size());
					threads.add(tm);
					new SchedulerThread(tm).start();
				} else {
					//Acquire a free thread
					tm = threads.get(freeThread);
				}
				//Assign task for thread to run
				tm.setScheduler(sch);
				tm.setBusy(true);
			}
		}
	}
}
