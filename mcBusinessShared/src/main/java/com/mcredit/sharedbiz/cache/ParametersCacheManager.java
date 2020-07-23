package com.mcredit.sharedbiz.cache;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.Parameters;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.ParametersDTO;
import com.mcredit.model.enums.ParametersDataType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.SessionType;
import com.mcredit.sharedbiz.factory.ParametersFactory;
import com.mcredit.util.StringUtils;

public final class ParametersCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
//	private List<ParametersDTO> paramsCache;
	private HashMap<String, ParametersDTO> paramsCache;

	private static ParametersCacheManager instance;

	private ParametersCacheManager() {
		initCache();
	}

	public static synchronized ParametersCacheManager getInstance() {
		if (instance == null) {
			synchronized (ParametersCacheManager.class) {
				if (null == instance) {
					instance = new ParametersCacheManager();
				}
			}
		}
		return instance;
	}
	
	public void initCache() {
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<Parameters> paramCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.parametersRepo().findParameters();
			});

			paramsCache = new HashMap<>();
			
			ParametersDTO pdto = null;
			if(paramCaches!=null && paramCaches.size()>0) {
				for (Parameters item : paramCaches) {
					pdto = modelMapper.map(item, ParametersDTO.class);
					if (pdto != null && !StringUtils.isNullOrEmpty(pdto.getParamName()))
						paramsCache.put(pdto.getParamName().toUpperCase(), pdto);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private Object findParamValue(String paramName, String parameterType) {
		ParametersDTO item = null;
		try {
			if(paramsCache == null)
				initCache();
			
			if(paramsCache!=null && paramsCache.size()>0 && !StringUtils.isNullOrEmpty(paramName)) {
				item = paramsCache.get(paramName.toUpperCase());
				if(!StringUtils.isNullOrEmpty(item.getParamValue())) {
					if (!StringUtils.isNullOrEmpty(parameterType))
						return ParametersFactory.createDataTypeStrategy(parameterType).getValue(item.getParamValue());
					else
						return ParametersFactory.createDataTypeStrategy(item.getParamDataType().toUpperCase()).getValue(item.getParamValue());
				}
			}
		}catch(Exception ex) {
			System.out.println("findParamValue.ex: " + ex.toString());
		}
		if(!StringUtils.isNullOrEmpty(parameterType)) 
			return ParametersFactory.createDataTypeStrategy(parameterType.toUpperCase()).getDefaultValue();
		
		return null;
	}
	
	public Object findParamValue(ParametersName paramName) {
		return findParamValue(paramName.value(), null);
	}
	
	public String findParamValueAsString(ParametersName paramName) {
		return (String) findParamValue(paramName.value(), ParametersDataType.STRING.value());
	}
	
	public Integer findParamValueAsInteger(ParametersName paramName) {
		return (Integer)findParamValue(paramName.value(), ParametersDataType.INTERGER.value());
	}
	public Integer findParamValueAsInteger(String paramName) {
		return (Integer)findParamValue(paramName, ParametersDataType.INTERGER.value());
	}
	
	public Long findParamValueAsLong(ParametersName paramName) {
		return (Long)findParamValue(paramName.value(), ParametersDataType.LONG.value());
	}
	
	public BigDecimal findParamValueAsBigDecimal(ParametersName paramName) {
		return (BigDecimal)findParamValue(paramName.value(), ParametersDataType.BIGDECIMAL.value());
	}
	
	public Date findParamValueAsDate(ParametersName paramName) {
		return (Date)findParamValue(paramName.value(), ParametersDataType.DATE.value());
	}
	
	public Time findParamValueAsTime(ParametersName paramName) {
		return (Time)findParamValue(paramName.value(), ParametersDataType.TIME.value());
	}
	
	public void refresh() {
		initCache();
	}
}
