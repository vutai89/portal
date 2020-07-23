package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.mobile.dto.CategoryDTO;

public class CompanyCacheManager implements IDataCache {
	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
	private List<CategoryDTO> companyCache;
	
	private static CompanyCacheManager instance;
	
	private CompanyCacheManager() {
		initCache();
	}
	
	public static synchronized CompanyCacheManager getInstance() {
		if (instance == null) {
			synchronized (CompanyCacheManager.class) {
				if (null == instance) {
					instance = new CompanyCacheManager();
				}
			}
		}
		return instance;
	}

	@Override
	public void initCache() {
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<CategoryDTO> companyInfors = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.getCompanyInfoRepository().getListCompany();
			});
	
			companyCache = new ArrayList<CategoryDTO>();

			if (companyInfors != null && companyInfors.size() > 0) {
				for (CategoryDTO item : companyInfors) {
					companyCache.add(modelMapper.map(item, CategoryDTO.class));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public CategoryDTO checkCategoryById(String taxNumber) {
		try {
			if (companyCache == null)
				initCache();

				for (CategoryDTO item : companyCache) {
					if (taxNumber.equals(item.getCompanyTaxNumber())) {
						return item;
					}
			}	
		} catch (Exception e) {
			System.out.println("findCompanyByTaxNumber.ex: " + e.toString());
		}
		
		return null;
	}
	
	public List<CategoryDTO> getLstCompany() {
		return companyCache;
	}

	@Override
	public void refresh() {
		this.initCache();
		
	}

}
