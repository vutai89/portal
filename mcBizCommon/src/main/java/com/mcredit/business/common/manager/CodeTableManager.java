package com.mcredit.business.common.manager;

import java.io.Closeable;
import java.util.Hashtable;
import java.util.List;

import com.mcredit.business.common.factory.CodeTableFactory;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.CodeTableEliteDTO;
import com.mcredit.model.dto.CodeTableInput;
import com.mcredit.model.dto.CodeTableV2EliteDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.object.CodeTableQuery;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CodeTableManager implements Closeable {
	private CodeTableCacheManager codeTableCacheManager = CodeTableCacheManager.getInstance();

	public String findCodeTable(String codeGroup, String category) throws Exception {

		CodeTableFactory.createCodeTableValidation().validateQuerryCodetable(codeGroup, category);

		try {
			if(!StringUtils.isNullOrEmpty(category) && category.indexOf(",") != -1) 
			{
				String[] array = category.split(",");
				Hashtable<String, List<CodeTableDTO>> result = new Hashtable<String, List<CodeTableDTO>>();
				for (String s : array) {
					List<CodeTableDTO> items = codeTableCacheManager.getByGroupAndCategory(codeGroup, s);
					result.put(s, items);
				}
				
				return JSONConverter.toJSON(result);
			}
			return JSONConverter.toJSON(codeTableCacheManager.getByGroupAndCategory(codeGroup, category));

		} catch (Exception ex) {

			throw new ValidationException(ex.toString());
		}

	}
	
	public String findCodeTableV2(String codeGroup, String category) throws Exception {

		CodeTableFactory.createCodeTableValidation().validateQuerryCodetable(codeGroup, category);

		try {
			if(!StringUtils.isNullOrEmpty(category) && category.indexOf(",") != -1) {
				
				String[] array = category.split(",");
				
				Hashtable<String, List<CodeTableV2EliteDTO>> result = new Hashtable<String, List<CodeTableV2EliteDTO>>();
				
				for (String s : array) {
					List<CodeTableV2EliteDTO> items = codeTableCacheManager.getByGroupAndCategoryV2(codeGroup, s);
					result.put(s, items);
				}
				
				return JSONConverter.toJSON(result);
			}
			
			return JSONConverter.toJSON(codeTableCacheManager.getByGroupAndCategoryV2(codeGroup, category));

		} catch (Exception ex) {

			throw new ValidationException(ex.toString());
		}

	}
	
	public String findCodeTableByCategory(CodeTableInput category) throws Exception {

		CodeTableFactory.createCodeTableValidation().validateQuerryCodetable(category);

		try {
			
			Hashtable<String, List<CodeTableEliteDTO>> result = new Hashtable<String, List<CodeTableEliteDTO>>();
			
			if( category.getCategory().size() > 1 ) {
				
				for ( String s : category.getCategory() ) {
					
					List<CodeTableEliteDTO> items = codeTableCacheManager.getByCategory(s, category.getDate(), category.getSwapDescription());
					
					result.put(s, items);
				}
				
				return JSONConverter.toJSON(result);
				
			} else {
				
				result.put(category.getCategory().get(0), codeTableCacheManager.getByCategory(category.getCategory().get(0), category.getDate(), category.getSwapDescription()));
				
				return JSONConverter.toJSON(result);
			}

		} catch (Exception ex) {

			throw new ValidationException(ex.toString());
		}

	}
	
	public String findCodeTableByCategory(String category) throws Exception {

		CodeTableFactory.createCodeTableValidation().validateQuerryCodetable(category);

		try {
			
			Hashtable<String, List<CodeTableEliteDTO>> result = new Hashtable<String, List<CodeTableEliteDTO>>();
			
			if(!StringUtils.isNullOrEmpty(category) && category.indexOf(",") != -1) {
				
				String[] array = category.split(",");
				
				for (String s : array) {
					List<CodeTableEliteDTO> items = codeTableCacheManager.getByCategory(s);
					result.put(s, items);
				}
				
				return JSONConverter.toJSON(result);
				
			} else {
				
				result.put(category, codeTableCacheManager.getByCategory(category));
				
				return JSONConverter.toJSON(result);
			}

		} catch (Exception ex) {

			throw new ValidationException(ex.toString());
		}

	}
	
	public List<CodeTableDTO> findCallStatus() throws Exception {

		return CacheManager.CodeTable().findCallStatus();
	}
	
	public List<CodeTableDTO> findLocation() throws Exception {

		return CacheManager.CodeTable().findLocation();
	}
	
	public Object getCodeListByCriteria(CodeTableQuery input) throws Exception {
		ProductDTO productId = null ;
		CodeTableDTO productGroupId = null ;
		CodeTableDTO productCategoryId = null ;
		
		if(StringUtils.isNullOrEmpty(input.getCategory()))
			throw new ValidationException("category is manatory !");
		
		if(!StringUtils.isNullOrEmpty(input.getProductCode())){
			 productId = ProductCacheManager.getInstance().findProductByCode(input.getProductCode());
			if(productId.getId() != null || productId.getId() != 0L ){
				throw new ValidationException("productCode is not found !");
			}
		}
		
		if(!StringUtils.isNullOrEmpty(input.getProductGroup())){
			 productGroupId = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue("PRD_GROUP", input.getProductGroup());
			if(productGroupId.getId() != null || productGroupId.getId() != 0L ){
				throw new ValidationException("productGroup is not found !");
			}
		}
		
		/*if(!StringUtils.isNullOrEmpty(input.getProductCategory())){
			 productCategoryId = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue("PRD_CAT", input.getProductCategory());
			if(productCategoryId.getId() != null || productCategoryId.getId() != 0L ){
				throw new ValidationException("productGroup is not found !");
			}
		}	*/ // chua co logic getProductCategoryId
		
		return codeTableCacheManager.getCodeListByCriteria(input.getCategory() , productId != null ? productId.getId().intValue() : null , productGroupId != null ? productGroupId.getId().intValue() : null , productCategoryId != null ? productCategoryId.getId().intValue() : null , null);
	}
	
	public Object findCodeTableByCatProCode(String category, String productCode) throws Exception {
		ProductDTO productId  = null ;
		if(StringUtils.isNullOrEmpty(category))
			throw new ValidationException("category is manatory !");
		if(!StringUtils.isNullOrEmpty(productCode))			
			productId = ProductCacheManager.getInstance().findProductByCode(productCode);		
			return codeTableCacheManager.getCodeListByCriteria(category , productId != null ? productId.getId().intValue() : null  , null , null , null);
	}

	public void close() {

	}
}
