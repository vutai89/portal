package com.mcredit.alfresco.threads;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Session;

import com.google.common.collect.Lists;
import com.mcredit.alfresco.server.AlfrescoECMServerSession;
import com.mcredit.alfresco.utils.Constants;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;

public class ThreadManager {

	List<DocumentToEcmDTO> dataList = new ArrayList<DocumentToEcmDTO>();
	private LinkedList<ThreadWorker> threadQueue = new LinkedList<ThreadWorker>();

	public void initialize(List<DocumentToEcmDTO> dataList) {
		setDataList(dataList);
	}

	private void allocateThread() {
		// chia nho danh sach can process
		List<List<DocumentToEcmDTO>> listOfPartition = Lists.partition(dataList, Constants.partitionSize);
		ThreadWorker worker = null;
		int i=1;
		System.out.println("***** ThreadManager - Number of patition to allocate = " + listOfPartition.size());
		for (List<DocumentToEcmDTO> lst : listOfPartition) {
			Session session = AlfrescoECMServerSession.getInstance().getSession();
			session.clear();
			System.out.println("***** ThreadManager -allocateThread get session success **** " );
			worker = new ThreadWorker();
			worker.setIndex(i);
			worker.setCmisSession(session);
			worker.setDataList(lst);
			// dua task vu vao pool de xu ly dan
			threadQueue.add(worker);
			i++;
		}

	}

	public List<DocumentToEcmDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<DocumentToEcmDTO> dataList) {
		this.dataList = dataList;
	}

	public void execute() {
		try {
			allocateThread();
			runThread();

		} catch (Exception e) {
			System.out.println(" ERROR Exception at :=  " + getClass() + "; method := run;  message :=  " + e.getMessage());
		}

	}

	private void runThread() {
		for (ThreadWorker worker : threadQueue) {
			System.out.println(" ThreadWorker - index=" + worker.getIndex() + " starting ...");
			worker.run();
			AlfrescoECMServerSession.getInstance().returnSession(worker.getCmisSession());
		}
	}
	
}
