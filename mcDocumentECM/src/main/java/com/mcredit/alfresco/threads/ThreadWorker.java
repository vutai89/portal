package com.mcredit.alfresco.threads;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;

import com.mcredit.alfresco.aggregate.DocumentUpload;
import com.mcredit.alfresco.api.AlfrescoECMUploadAPI;
import com.mcredit.data.ecm.UnitOfWorkECM;
import com.mcredit.model.enums.ECMUploadStatus;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;

public class ThreadWorker implements Runnable {

	private List<DocumentToEcmDTO> dataList = new ArrayList<DocumentToEcmDTO>();
	private Session cmisSession;
	private int index;

	public List<DocumentToEcmDTO> getDataList() {
		return dataList;
	}

	public void setDataList(List<DocumentToEcmDTO> dataList) {
		this.dataList = dataList;
	}

	public Session getCmisSession() {
		return cmisSession;
	}

	public void setCmisSession(Session cmisSession) {
		this.cmisSession = cmisSession;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("***** ThreadWorker - index=" + index + " - threadId=" + Thread.currentThread().getId() + " run get session success **** " );
		AlfrescoECMUploadAPI ccde = new AlfrescoECMUploadAPI();
		List<Document> listOfResult = new ArrayList<Document>();
		List<Document> listOfDoc = null;

		try {
			UnitOfWorkECM uwe = DocumentUpload.getInstance(SessionType.JTA);
			uwe.start();
			ccde.setUowECM(uwe);
			for (DocumentToEcmDTO model : dataList) {
				if(ECMUploadStatus.GOT_FILE.value().equals(model.getStatus())) {
					listOfDoc = ccde.doMigrateItem(model, cmisSession);
					if (listOfDoc != null && !listOfDoc.isEmpty()) {
						listOfResult.addAll(listOfDoc);
					}
				}
			}
			uwe.commit();
			uwe.clearCache();
			uwe.close();
		} catch (Throwable th) {
			System.out.println("ThreadWorker run exception " + th.getMessage());
		}

		System.out.println("############## This is result in Thread: " + this + " ############");
		System.out.println("		Number of document uploaded: " + listOfResult.size());
		//displayResult(listOfResult);
		// release resource
		//cmisSession = null;

	}

}
