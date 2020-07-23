
package com.mcredit.data.ecm.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.common.DataUtils;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.ecm.entity.DocumentToECM;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.enums.ECMSourceEnum;
import com.mcredit.model.object.ecm.DocumentToEcmDTO;
import com.mcredit.model.object.ecm.InputUploadECM;
import com.mcredit.util.JSONConverter;

public class DocumentToECMRepository extends BaseRepository
	implements IUpsertRepository<DocumentToECM> {

	public DocumentToECMRepository(Session session) {
		super(session);
	}

	public void upsert(DocumentToECM item) {
		this.session.saveOrUpdate("DocumentToECM", item);
	}

	public void insert(DocumentToECM item) {
		this.session.save("DocumentToECM", item);
	}

	public DocumentToECM getById(Long id) {
		if (id == null) return null;
		@SuppressWarnings("rawtypes")
		Query query = this.session.createQuery("from "+DocumentToECM.class.getName()+ " where id = :pid");
		query.setParameter("pid", id);
		DocumentToECM result = (DocumentToECM) query.uniqueResult();
		return result;
	}

	public void remove(DocumentToECM item) {
		this.session.delete("DocumentToECM", item);
	}
	
    public Long findNextSeq() {
        @SuppressWarnings("rawtypes")
        Query query = this.session.getNamedNativeQuery("DocumentToECM.nextSeq");
        BigDecimal result = (BigDecimal) query.uniqueResult();
        if(result == null)
            return null;
        return result.longValue();
    }
    
	public List<DocumentToECM> getDocumentsByAppNumber(Integer appNumber) {
		return (List<DocumentToECM>) this.session.createQuery("from DocumentToECM where bpmAppNumber = :appNumber", DocumentToECM.class)
				.setParameter("appNumber", appNumber)
				.list();
	}
	
	public int updateDocumentToEcm(DocumentToEcmDTO docEcm) {
		return this.session.getNamedNativeQuery("updateDocumentToEcm")
			.setParameter("docStatus", docEcm.getStatus())
			.setParameter("objectId", docEcm.getEcmObjectId())
			.setParameter("errMessage", docEcm.getErrorMessage())
			.setParameter("docId", docEcm.getId())
			.executeUpdate();
	}

	public Integer updateDocumentToProcess() {
		return this.session.getNamedNativeQuery("updateDocumentToProcess")
			.executeUpdate();
	}

	public List<DocumentToEcmDTO> getDocumentsToUpload() {
		List<Object[]> objList = this.session.getNamedNativeQuery("getDocumentsToUpload").list();
		List<DocumentToEcmDTO> retList = new ArrayList<DocumentToEcmDTO>();
		for(Object[] objects : objList) {
			DocumentToEcmDTO docED = new DocumentToEcmDTO();
			docED = (DocumentToEcmDTO) DataUtils.bindingData(docED, objects);
			retList.add(docED);
		}
		
		return retList;
	}

	public DocumentToECM findByDocuument(InputUploadECM inputUploadECM, Integer documentId , Integer productId) {
		DocumentToECM doccumner = null ;
		try {
			
			ECMSourceEnum ecmEnum = ECMSourceEnum.from(inputUploadECM.getDocumentSource());
			HashMap<String, Object> parameterList = new HashMap<>();
			Query<DocumentToECM> query = null  ;
			
			StringBuilder sql =   new StringBuilder( "from DocumentToECM where documentId = :documentId  ");
			parameterList.put("documentId", documentId);
			
			if(ECMSourceEnum.COLLECTION  == ecmEnum ){
				sql.append( " and ldNumber  = :ldNumber");
				parameterList.put("ldNumber", inputUploadECM.getLdNumber());
			}
			
			if(ECMSourceEnum.CASH_FROM_BPM  == ecmEnum  || ECMSourceEnum.INSTALLMENT_FROM_BPM == ecmEnum || ECMSourceEnum.FAS == ecmEnum ){
				sql.append( " and productId  = :productId and bpmAppId = :bpmAppId and bpmAppNumber = :bpmAppNumber ");
				parameterList.put("productId", productId);
				parameterList.put("bpmAppId", inputUploadECM.getAppId());
				parameterList.put("bpmAppNumber", inputUploadECM.getAppNumber());
				
			}
			
			query = this.session.createQuery(sql.toString()+ " ORDER BY versionNumber DESC ", DocumentToECM.class);
			
			for (String key : parameterList.keySet()) {
				query.setParameter(key, parameterList.get(key));
			}
			System.out.println( " parameterList findByDocuument ---> " + JSONConverter.toJSON(parameterList));
			 doccumner = (DocumentToECM) query.setFirstResult(0).setMaxResults(1).getSingleResult();
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doccumner;
	}
	
	public List<InputUploadECM> getListInforECMByAppId(List<String> listAppId) {
		List<Object[]> objList = this.session.getNamedNativeQuery("getDocumentForFas")
				.setParameterList("appList", listAppId)
                .getResultList();
		
		List<InputUploadECM> retList = new ArrayList<InputUploadECM>();
		for(Object[] objects : objList) {
			InputUploadECM input = new InputUploadECM();
			input = (InputUploadECM) DataUtils.bindingData(input, objects);
			retList.add(input);
		}

		return retList;
	}
	
	public List<InputUploadECM> getListInforECMByContractLD(List<String> listLD) {
		List<Object[]> objList = this.session.getNamedNativeQuery("getDocumentForCollection")
				.setParameterList("contractList", listLD)
                .getResultList();
		
		List<InputUploadECM> retList = new ArrayList<InputUploadECM>();
		for(Object[] objects : objList) {
			InputUploadECM input = new InputUploadECM();
			input = (InputUploadECM) DataUtils.bindingData(input, objects);
			retList.add(input);
		}

		return retList;
	}
	
}
