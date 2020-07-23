package com.mcredit.alfresco.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.log4j.Logger;

import com.mcredit.alfresco.server.AlfrescoECMServerSession;
import com.mcredit.alfresco.utils.Constants;
import com.mcredit.model.object.ecm.LoanDocRequest;
import com.mcredit.model.object.ecm.LoanDocRespone;
import com.mcredit.util.StringUtils;


public class AlfrescoECMRestAPI {

	static Logger logger = Logger.getLogger(AlfrescoECMRestAPI.class.getName());

	public List<LoanDocRespone> doQuery(LoanDocRequest doc) {

		logger.info("****** doQuery doc with request is: " + doc.toString() + " *******");
		StringBuilder cql = new StringBuilder(
				" SELECT  cmis:name, cmis:versionSeriesId, mc:refNumber, mc:contractNumber,"
						+ " mc:productCode, mc:productName, mc:idCardNumber, mc:refName, mc:customerName, "
						+ "  cmis:contentStreamMimeType, mc:businessStep, cmis:objectId  "
						+ " FROM mc:loanDocs WHERE cmis:objectId is not null  ");
		if (!StringUtils.isNullOrEmpty(doc.getRefNumber())) {
			cql.append(" AND mc:refNumber = ");
			cql.append(StringUtils.toLiteral(doc.getRefNumber()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getContractNumber())) {
			cql.append(" AND mc:contractNumber = ");
			cql.append(StringUtils.toLiteral(doc.getContractNumber()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getProductCode())) {
			cql.append("AND  mc:productCode = ");
			cql.append(StringUtils.toLiteral(doc.getProductCode()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getProductName())) {
			cql.append(" AND mc:productName = ");
			cql.append(StringUtils.toLiteral(doc.getProductName()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getIdCardNumber())) {
			cql.append(" AND mc:idCardNumber = ");
			cql.append(StringUtils.toLiteral(doc.getIdCardNumber()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getRefName())) {
			cql.append(" AND mc:refName = ");
			cql.append(StringUtils.toLiteral(doc.getRefName()));

		}
		if (!StringUtils.isNullOrEmpty(doc.getCustomerName())) {
			cql.append(" AND  mc:customerName = ");
			cql.append(StringUtils.toLiteral(doc.getCustomerName()));
		}
		if (!StringUtils.isNullOrEmpty(doc.getBusinessStep())) {
			cql.append(" AND  mc:businessStep = ");
			cql.append(StringUtils.toLiteral(doc.getBusinessStep()));
		}

		List<LoanDocRespone> listResult = doQuery(cql.toString());
		logger.info("****** doQuery return number of doc: " + listResult.size() + " *******");
		return listResult;
	}

	public List<LoanDocRespone> doQueryByObjectId(String objectId) {

		logger.info("****** doQueryByObjectId doc with request is: " + objectId + " *******");
		StringBuilder cql = new StringBuilder(
				" SELECT  cmis:name, cmis:versionSeriesId, mc:refNumber, mc:contractNumber,"
						+ " mc:productCode, mc:productName, mc:idCardNumber, mc:refName, mc:customerName, "
						+ "  cmis:contentStreamMimeType, mc:businessStep, cmis:objectId  "
						+ " FROM mc:loanDocs WHERE cmis:objectId = " + StringUtils.toLiteral(objectId));

		List<LoanDocRespone> listResult = doQuery(cql.toString());
		logger.info("****** doQuery return number of doc: " + listResult.size() + " *******");
		return listResult;
	}

	public List<LoanDocRespone> doQuery(String cql) {
		//DownloadFile down = new DownloadFile();
		
		AlfrescoECMServerSession serverInstance = AlfrescoECMServerSession.getInstance();
		Session cmisSession = serverInstance.getSession();

		OperationContext oc = new OperationContextImpl();

		ItemIterable<QueryResult> results = cmisSession.query(cql, false, oc);
		LoanDocRespone doc;
		List<LoanDocRespone> listResult = new ArrayList<LoanDocRespone>();
		for (QueryResult result : results) {
			doc = new LoanDocRespone();
			for (PropertyData<?> prop : result.getProperties()) {
				mappingData(doc, prop);
				logger.info(prop.getQueryName() + ": " + prop.getFirstValue());
			}
			System.out.println(" cmisSession ------> " + cmisSession);
			//down.fileUrl(doc.getObjectUrl(), doc.getObjectName());
			
			listResult.add(doc);
		}
		
		serverInstance.returnSession(cmisSession);
		return listResult;
	}

	/**
	 * Khong the dung kieu reflection duoc vi trong property cua cmis co dau :
	 * (cmis:name, cmis:objectId, ...)
	 * 
	 * @param doc
	 * @param prop
	 */
	private void mappingData(LoanDocRespone doc, PropertyData<?> prop) {
		if (prop.getFirstValue() == null) {
			return;
		}
		switch (prop.getQueryName()) {
		case "cmis:name":
			doc.setObjectName(prop.getFirstValue().toString());
			break;

		case "cmis:versionSeriesId":
			doc.setObjectId(prop.getFirstValue().toString());
			doc.setObjectUrl(Constants.downloadLink.replace("{ID}", doc.getObjectId()));
			doc.setWorkSpace(Constants.workSpace + doc.getObjectId());
			break;
		case "mc:refNumber":
			doc.setRefNumber(prop.getFirstValue().toString());
			break;

		case "mc:contractNumber":
			doc.setContractNumber(prop.getFirstValue().toString());
			break;

		case "mc:productCode":
			doc.setProductCode(prop.getFirstValue().toString());
			break;

		case "mc:productName":
			doc.setProductName(prop.getFirstValue().toString());
			break;

		case "mc:idCardNumber":
			doc.setIdCardNumber(prop.getFirstValue().toString());
			break;

		case "mc:refName":
			doc.setRefName(prop.getFirstValue().toString());
			break;

		case "mc:customerName":
			doc.setCustomerName(prop.getFirstValue().toString());
			break;

		case "cmis:contentStreamMimeType":
			doc.setMimeType(prop.getFirstValue().toString());
			break;

		case "mc:businessStep":
			doc.setBusinessStep(prop.getFirstValue().toString());
			break;

//		case "alfcmis:nodeRef":
//			if(prop.getValues() != null) {
//				doc.setWorkSpace((String) prop.getValues().get(0));
//			}
//			break;

		}
	}

	public boolean deleteDoc(LoanDocRespone doc) {
		String output = "";
		logger.info("******* deleteDoc with doc id is " + doc.getObjectId() + " *******");
		try {
			AlfrescoECMServerSession serverInstance = AlfrescoECMServerSession.getInstance();
			Session cmisSession = serverInstance.getSession();
			CmisObject cmisObject = cmisSession.getObject(doc.getObjectId());
			cmisObject.delete();
			output = "************** Delete sucess object with id=: " + doc.getObjectId() + " ********";
			serverInstance.returnSession(cmisSession);
			return true;
		} catch (Exception e) {
			output = "************** Delete  object with id=: " + doc.getObjectId() + " occur Error ********";
			logger.error(output);
			logger.error(e.getMessage());
			return false;
		}

	}

}
