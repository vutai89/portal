package com.mcredit.sharedbiz.cache;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.model.alfresco.object.ACMShareObject;
import com.mcredit.model.alfresco.object.ECMShareDTO;
import com.mcredit.model.alfresco.object.ECMShareData;
import com.mcredit.model.alfresco.object.Entry;
import com.mcredit.model.alfresco.object.ExtendShare;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.ExtendDateValue;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;

public class ECMShareCacheManager {

	BasedHttpClient bs = new BasedHttpClient(CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT_TYPE),
			CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT));
	
	private ModelMapper modelMapper = new ModelMapper();
	 private String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_HOST) + BusinessConstant.ECM_GET_LIST_SHARED_LINKS + Integer.MAX_VALUE;
	 
	 String dateFormat = DateFormatTag.DATEFORMAT_ECM.value();

	
	private static ECMShareCacheManager instance;
	private List<ECMShareDTO> lstShareDTO  ;

	private ECMShareCacheManager() {
		initCache();
	}
	
	public static synchronized ECMShareCacheManager getInstance() {
		if (instance == null) {
			synchronized (ECMShareCacheManager.class) {
				if (null == instance) {
					instance = new ECMShareCacheManager();
				}
			}
		}
		return instance;
	}
	
	public void initCache() {
		lstShareDTO = new ArrayList<>();
		try {
			ECMShareData result = bs.doGet(url, ECMShareData.class, ContentType.Json, AcceptType.Json,"list");
			
			if(result!= null){
				List<Entry> out = result.getEntries() ;
				for (Entry entry : out) {
					try {
						ACMShareObject ecmShare = modelMapper.map(entry.getEntry(), ACMShareObject.class);
						ECMShareDTO ecmShareDTO = (modelMapper.map(ecmShare, ECMShareDTO.class));
						if(entry.getEntry().getExpiresAt() != null)
							ecmShareDTO.setExpiresAt(DateUtil.toDate(entry.getEntry().getExpiresAt(), dateFormat));
						if(entry.getEntry().getModifiedAt() != null)
							ecmShareDTO.setModifiedAt(DateUtil.toDate(entry.getEntry().getModifiedAt(), dateFormat));
						lstShareDTO.add(ecmShareDTO);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(" not DateFormat" + e);
					}
					
					
				}
			}
			
		} catch (ISRestCoreException e) {			
			e.printStackTrace();
		}
	}
	
	public ECMShareDTO getByObjectId(String objectId) throws ParseException{
		
		boolean isActive = false ;
		
		if (lstShareDTO == null || lstShareDTO.size() == 0) {
			initCache();
		}
		
		for (ECMShareDTO shareDTO : lstShareDTO) {
			if(shareDTO.getNodeId().equals(objectId)){
				if(shareDTO.getExpiresAt()!= null && shareDTO.getExpiresAt().compareTo(new Date()) < 0){
					lstShareDTO.remove(shareDTO);
					Entry result = extendShare(shareDTO.getNodeId());
					if(result != null){
						try {
						ACMShareObject ecmShare = modelMapper.map(result, ACMShareObject.class);
						ECMShareDTO ecmShareDTO = (modelMapper.map(ecmShare, ECMShareDTO.class));
						if(result.getEntry().getExpiresAt() != null)
							ecmShareDTO.setExpiresAt(DateUtil.toDate(result.getEntry().getExpiresAt(), dateFormat));
						if(result.getEntry().getModifiedAt() != null)
							ecmShareDTO.setModifiedAt(DateUtil.toDate(result.getEntry().getModifiedAt(), dateFormat));
						lstShareDTO.add(ecmShareDTO);
						return ecmShareDTO;
						} catch (Exception e) {
							System.out.println(" not DateFormat");
						}
						
						
					}
				}
				isActive = true ;
				
				return shareDTO ;
			}
		}
			if(!isActive){
				try {
					lstShareDTO.add(modelMapper.map(extendShare(objectId), ECMShareDTO.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		return null;
		
	}
	
	public Entry extendShare(String objectId) throws ParseException{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE,(ParametersCacheManager.getInstance().findParamValueAsInteger(ExtendDateValue.EXTEND_DATE_VIEW_DOC.stringValue())));
		
		ExtendShare payload = new ExtendShare(objectId,DateUtil.toString(c.getTime(), dateFormat));
		
		try {
			return bs.doPost(url, JSONConverter.toJSON(payload), ContentType.Json,Entry.class,"entry");
					
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		
		return null ;
	}
	
	
	public void refresh() {
		initCache();
	}
}
