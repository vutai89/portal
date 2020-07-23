package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.SystemDefineFields;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.SystemDefineFieldsDTO;
import com.mcredit.model.enums.SessionType;
import com.mcredit.util.StringUtils;

public final class SystemDefineFieldsCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
	private List<SystemDefineFieldsDTO> systemDefineFieldsCache;
	private List<SystemDefineFieldsDTO> systemDefineFieldsCacheById;

	private static SystemDefineFieldsCacheManager instance;

	private SystemDefineFieldsCacheManager() {
		initCache();
	}

	public static synchronized SystemDefineFieldsCacheManager getInstance() {
		if (instance == null) {
			synchronized (SystemDefineFieldsCacheManager.class) {
				if (null == instance) {
					instance = new SystemDefineFieldsCacheManager();
				}
			}
		}
		return instance;
	}

	public void initCache() {
		
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<SystemDefineFields> systemDefineFieldsCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.systemDefineFieldsRepo().findActiveSystemDefineFields();
			});

			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<SystemDefineFields> systemDefineFieldsCacheByIds = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.systemDefineFieldsRepo().findActiveSystemDefineFieldsOrderById();
			});
			
			systemDefineFieldsCache = new ArrayList<SystemDefineFieldsDTO>();
			for (SystemDefineFields item : systemDefineFieldsCaches) {
				systemDefineFieldsCache.add(modelMapper.map(item, SystemDefineFieldsDTO.class));
			}

			systemDefineFieldsCacheById = new ArrayList<SystemDefineFieldsDTO>();
			for (SystemDefineFields item : systemDefineFieldsCacheByIds) {
				systemDefineFieldsCacheById.add(modelMapper.map(item, SystemDefineFieldsDTO.class));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SystemDefineFieldsDTO getSystemDefineFieldsById(Integer id) {
		if (systemDefineFieldsCache == null)
			initCache();

		SystemDefineFieldsDTO out = new SystemDefineFieldsDTO();

		if (!StringUtils.isNullOrEmpty(id.toString())) {
			for (SystemDefineFieldsDTO item : systemDefineFieldsCache) {
				if (id.equals(item.getId()))
					out = item;
			}
		}

		return out;
	}

	public SystemDefineFieldsDTO getSystemDefineFields(Integer codeTableId, String system) {
		if (systemDefineFieldsCache == null)
			initCache();

		SystemDefineFieldsDTO out = new SystemDefineFieldsDTO();

		if (!StringUtils.isNullOrEmpty(codeTableId.toString()) && !StringUtils.isNullOrEmpty(system)) {
			for (SystemDefineFieldsDTO item : systemDefineFieldsCache) {
				if (codeTableId.equals(item.getCodeTableId()) && system.equals(item.getSystem()))
					out = item;
			}
		}

		return out;
	}

	public void refresh() {
		initCache();
	}
}
