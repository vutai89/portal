package com.mcredit.sharedbiz.cache;

import java.io.Closeable;

import com.mcredit.common.Messages;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.CacheTag;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CacheManager extends AbstractValidation implements Closeable , Runnable {

	public static PostingConfigurationCacheManager PostingConfiguration() {
		return PostingConfigurationCacheManager.getInstance();
	}

	public static ParametersCacheManager Parameters() {
		return ParametersCacheManager.getInstance();
	}

	public static PartnerCacheManager Partner() {
		return PartnerCacheManager.getInstance();
	}

	public static CodeTableCacheManager CodeTable() {
		return CodeTableCacheManager.getInstance();
	}

	public static ProductCacheManager Product() {
		return ProductCacheManager.getInstance();
	}

	public static SystemDefineFieldsCacheManager SystemDefineFields() {
		return SystemDefineFieldsCacheManager.getInstance();
	}
	
	public static UserPermissionCacheManager UserPermission() {
		return UserPermissionCacheManager.getInstance();
	}
	
	public static ECMShareCacheManager ECMShare() {
		return ECMShareCacheManager.getInstance();
	}
	
	public static TokenCacheManager Token() {
		return TokenCacheManager.getInstance();
	}
	public static WHCodeTableCacheManager whCodeTable() {
		return WHCodeTableCacheManager.getInstance();
	}
	
	public static CompanyCacheManager CompanyCache() {
		return CompanyCacheManager.getInstance();
	}
	
	public static DocumentsCachManager DocumentCache() {
		return DocumentsCachManager.getInstance();
	}
	
	public static NotificationTemplateCacheManager NotificationTemplateCacheManager() {
		return NotificationTemplateCacheManager.getInstance();
	}
	
	
	private CacheTag[] tags = null;
	
	public CacheManager(){}
	
	public CacheManager(CacheTag[] tags){
		this.tags = tags;
	};		
	
	
	public ResponseSuccess refeshCache(String cacheName) throws Exception {

		validate(cacheName);

		try {
			if (!StringUtils.isNullOrEmpty(cacheName)) {
				String[] input = cacheName.split(",");
				for (String string : input) {
					CacheTag enums = CacheTag.from(string.trim());

					switch (enums) {
					case CODE_TABLE:
						CodeTableCacheManager.getInstance().refresh();
						break;
					case PARAMETERS:
						ParametersCacheManager.getInstance().refresh();
						break;
					case PARTNER:
						PartnerCacheManager.getInstance().refresh();
						break;
					case POSTING_CONFIGURATION:
						PostingConfigurationCacheManager.getInstance().refresh();
						break;
					case PRODUCTS:
						ProductCacheManager.getInstance().refresh();
						break;
					case SYSTEM_DEFINE_FIELDS:
						SystemDefineFieldsCacheManager.getInstance().refresh();
						break;
					case USER_PERMISSION:
						UserPermissionCacheManager.getInstance().refresh();
						UserPermissionCacheManager.getInstance().refreshServicePermission();
						break;
					case DOCUMENTS:
						DocumentsCachManager.getInstance().refresh();
						break;
					case COMPANY:
						CompanyCacheManager.getInstance().refresh();
						break;
					case ALL:
						/*CodeTable*/
						CodeTableCacheManager.getInstance().refresh();
						/*Config*/
						ParametersCacheManager.getInstance().refresh();
						SystemDefineFieldsCacheManager.getInstance().refresh();
						/*Partner*/
						PartnerCacheManager.getInstance().refresh();
						/*CreditCard*/
						PostingConfigurationCacheManager.getInstance().refresh();
						/*proceduct*/
						ProductCacheManager.getInstance().refresh();
						/*Menu*/
						UserPermissionCacheManager.getInstance().refresh();
						/*WareHouse*/
						WHCodeTableCacheManager.getInstance().refresh();						
						//ECMShareCacheManager.getInstance().refresh();
						/*Moblibe*/
						CompanyCacheManager.getInstance().refresh();
						DocumentsCachManager.getInstance().refresh();
						break;
					}
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return new ResponseSuccess();

	}
	
	public ResponseSuccess refeshCache(CacheTag cacheName) throws Exception {
		return this.refeshCache(cacheName.value());
	}

	public void validate(String cacheName) throws ValidationException {

		if (StringUtils.isNullOrEmpty(cacheName)) {
			getMessageDes().add(Messages.getString("validation.field.madatory", "cacheName"));
			throw new ValidationException(this.buildValidationMessage());
		}

		String[] input = cacheName.split(",");
		for (String string : input) {
			if (!CacheTag.contains(string.trim()))
				getMessageDes().add(Messages.getString("validation.field.notcorect", string.trim()));
		}

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	public void close() {
	}

	@Override
	public void run() {
		try {
			if(tags == null)
				this.refeshCache("ALL");
			else
				for (int i = 0; i < tags.length; i++)
					this.refeshCache(tags[i]);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
