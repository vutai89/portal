package com.mcredit.business.job.cancelCaseBPM;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.mcredit.util.PartitionListHelper;

public class TestParallel {

	public void doAction(List<String> input) {
		System.out.println(input.toString());
		System.out.println(" Start send case : " + new Date().getTime());
		ArrayList<List<String>> tmp = new ArrayList<List<String>>(PartitionListHelper.partition(input, 2));
		List<String> outPut = new ArrayList<>();
		for (int i = 0; i < tmp.size(); i++) {
			ExecutorService executor = Executors.newSingleThreadExecutor();
			for (String cases : tmp.get(i)) {
				
				Callable<String> callable = () -> {
					return cancelCaseBPM(cases);
				};
				try {
					outPut.add(executor.submit(callable).get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			executor.shutdown();
		}
		System.out.println( " -- Out -- " + outPut);
	}

	private String cancelCaseBPM(String cases) {
		System.out.println(" Do test " + cases + "  : time " + new Date().getTime());
		return " Do test " + cases + "  : time " + new Date().getTime();
	}

	private List<String> newlstTest() {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < 13; i++) {
			result.add("a-" + i);
		}
		return result;

	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TestParallel t = new TestParallel();

		t.doAction(t.newlstTest());
		System.out.println(" Finish " + new Date().getTime());
	}
	

}
