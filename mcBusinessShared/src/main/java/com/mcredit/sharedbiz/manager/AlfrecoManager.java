package com.mcredit.sharedbiz.manager;

import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.ValidationException;

import org.apache.commons.io.IOUtils;

import com.mcredit.model.alfresco.object.ECMShareDTO;
import com.mcredit.model.alfresco.object.Entry;
import com.mcredit.model.alfresco.object.Error;
import com.mcredit.model.alfresco.object.ExtendShare;
import com.mcredit.model.alfresco.object.ResultLinkDTO;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.ExtendDateValue;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ECMShareCacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class AlfrecoManager extends BaseManager {

	BasedHttpClient bs = new BasedHttpClient(CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT_TYPE),CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT));
	
	 private String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_HOST) + BusinessConstant.ECM_GET_LIST_SHARED_LINKS + Integer.MAX_VALUE;
	 
	 String dateFormat = DateFormatTag.DATEFORMAT_ECM.value();
	
	public String contentFrame(String objectId) throws ValidationException {
		alfrecoValidate(objectId) ;
		
		try {
			URL url = new URL(objectId);
			System.out.println("URL=====>" + url);
			
			 URLConnection uCon = url.openConnection();
			
			uCon.setRequestProperty(CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT_TYPE),
					CacheManager.Parameters().findParamValueAsString(ParametersName.ECM_TOKENT));
			
			StringWriter writer = new StringWriter();
			IOUtils.copy(uCon.getInputStream(), writer,  StandardCharsets.UTF_8.name());
			return writer.toString();
			
			}catch (Exception e) {
				e.printStackTrace();
			}
		
		return null;
	}

	public Object getSharelink(String objectId) throws ValidationException, ParseException {
		alfrecoValidate(objectId);
		
		ECMShareDTO out = ECMShareCacheManager.getInstance().getByObjectId(objectId);
		if(out != null){
			String linkShare = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.WH_ECM_SHARE_LINK) +  out.getId();
			
			return new ResultLinkDTO(objectId,out.getExpiresAt(),out.getId(),linkShare,contentFrame(linkShare));
		}
		return new ValidationException("no data");
	}
	
	
	public void alfrecoValidate(String objectId) throws ValidationException{
		if(StringUtils.isNullOrEmpty(objectId))
			throw new ValidationException(" input objectId ");
	}

	public Object sharelink(String objectId) throws ParseException {
		String link  = ParametersCacheManager.getInstance().findParamValueAsString(ParametersName.WH_ECM_SHARE_LINK);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, (ParametersCacheManager.getInstance().findParamValueAsInteger(ExtendDateValue.EXTEND_DATE_VIEW_DOC.stringValue())));
		ExtendShare payload = new ExtendShare(objectId, DateUtil.toString(c.getTime(), dateFormat));
		
		ResultLinkDTO output = new ResultLinkDTO(objectId, null, "", "", null);
		
		try {
			ApiResult result = bs.doPost(url, JSONConverter.toJSON(payload), ContentType.Json, AcceptType.Json);
			if(result != null && result.getStatus()){
				Entry entry = JSONConverter.toObject(result.getBodyContent(),Entry.class);
				if(entry != null){
					output.setShareId(entry.getEntry().getId());
					output.setLinkShare(link + entry.getEntry().getId());
				}
			}else {
				Error error = bs.toJSONObject(Error.class, result.getBodyContent(),"error");
				if(error != null ){
					String[] temp = error.getErrorKey().split("\\[");
					if (temp.length > 1){
						String	shareId = temp[1].replace("]", "");
						output.setShareId(shareId);
						output.setLinkShare(link + shareId);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;
	}
	
}
