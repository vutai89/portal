package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.Partner;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.PartnerDTO;
import com.mcredit.model.enums.SessionType;
import com.mcredit.util.StringUtils;

public final class PartnerCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
	private List<PartnerDTO> partnerCache;

	private static PartnerCacheManager instance;

	private PartnerCacheManager() {
		initCache();
	}

	public static synchronized PartnerCacheManager getInstance() {
		if (instance == null) {
			synchronized (PartnerCacheManager.class) {
				if (null == instance) {
					instance = new PartnerCacheManager();
				}
			}
		}
		return instance;
	}
	

	public void initCache() {
		
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<Partner> partnerCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.partnerRepo().findPartner();
			});

			partnerCache = new ArrayList<PartnerDTO>();
			
			if(partnerCaches!=null && partnerCaches.size()>0) {
				for (Partner item : partnerCaches) {
					partnerCache.add(modelMapper.map(item, PartnerDTO.class));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PartnerDTO getPartnerByPartnerCode(String partnerCode) {
		if (partnerCache == null)
			initCache();
		
		for (PartnerDTO pstDto : partnerCache) {
			if (!StringUtils.isNullOrEmpty(partnerCode) && partnerCode.equalsIgnoreCase(pstDto.getPartnerCode().trim()))
				return pstDto;
		}
		
		return null;
	}
	
	public PartnerDTO getPartnerByPartnerId(Long partnerId) {
		if (partnerCache == null)
			initCache();
		
		for (PartnerDTO pstDto : partnerCache) {
			if (partnerId==pstDto.getId())
				return pstDto;
		}
		
		return null;
	}
	
	
	public void refresh() {
		initCache();
	}
}
