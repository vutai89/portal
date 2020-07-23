package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.Partner;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.data.payment.entity.PostingConfiguration;
import com.mcredit.model.dto.PostingConfigurationDTO;
import com.mcredit.model.enums.SessionType;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public final class PostingConfigurationCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
	private List<PostingConfigurationDTO> postingConfigurationCache;

	private static PostingConfigurationCacheManager instance;

	private PostingConfigurationCacheManager() {
		initCache();
	}

	public static synchronized PostingConfigurationCacheManager getInstance() {
		if (instance == null) {
			synchronized (PostingConfigurationCacheManager.class) {
				if (null == instance) {
					instance = new PostingConfigurationCacheManager();
				}
			}
		}
		return instance;
	}
	
	public void initCache() {
		
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<PostingConfiguration> postingConfigurationCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				return  uok.payment.postingConfigurationRepo().findPostingConfiguration();
			});

			postingConfigurationCache = new ArrayList<PostingConfigurationDTO>();
			
			if(postingConfigurationCaches!=null && postingConfigurationCaches.size()>0) {
				for (PostingConfiguration item : postingConfigurationCaches) {
					postingConfigurationCache.add(modelMapper.map(item, PostingConfigurationDTO.class));
				}
			}
			
			System.out.println( " -------- postingConfigurationCache ----------    " + JSONConverter.toJSON(postingConfigurationCache));	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<PostingConfigurationDTO> getPostingConfigurationBy(String postingGroup) {
		if (postingConfigurationCache == null) {
			initCache();
		}

		List<PostingConfigurationDTO> tmp = new ArrayList<PostingConfigurationDTO>();
		for (PostingConfigurationDTO pstDto : postingConfigurationCache) {
			if (!StringUtils.isNullOrEmpty(postingGroup)) {
				if (postingGroup.equals(pstDto.getPostingGroup())) {
					tmp.add(pstDto);
				}
			}
		}
		return tmp;
	}
	
	public List<PostingConfigurationDTO> getPostConfigByPartnerIdAndPostGroup(Integer partnerId, String postingGroup) {
		if (postingConfigurationCache == null)
			initCache();

		List<PostingConfigurationDTO> tmp = new ArrayList<PostingConfigurationDTO>();
		
		 System.out.println( " -------- postingConfigurationCache ----------    " + JSONConverter.toJSON(postingConfigurationCache));
		 
		for (PostingConfigurationDTO pstDto : postingConfigurationCache) {
			 System.out.println( " -------- PostingConfigurationDTO ----------    " + JSONConverter.toJSON(pstDto));
			if(!StringUtils.isNullOrEmpty(postingGroup) && partnerId!=null
					&& (postingGroup.equals(pstDto.getPostingGroup()) && partnerId.equals(pstDto.getPartnerId())))
				tmp.add(pstDto);
		}
		return tmp;
	}
	
	
	
	
	public void refresh() {
		initCache();
	}
}
