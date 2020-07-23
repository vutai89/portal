package com.mcredit.product.manager;

import java.io.File;
import java.util.List;

import com.mcredit.data.product.entity.MappingHierarchy;
import com.mcredit.model.dto.PagingDTO;
import com.mcredit.model.dto.product.CommodityDTO;
import com.mcredit.product.aggregate.ProductAggregate;
import com.mcredit.product.dto.CommonDTO;
import com.mcredit.product.validation.ProductValidation;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

/**
 * @author manhnt1.ho
 * @since 06/2019
 */
public class ProductManager extends BaseManager {
	private ProductAggregate _agg;
	private UserDTO _user;
	private ProductValidation _productValidation;

	public ProductManager(UserDTO user) {
		_user = user;
		_agg = new ProductAggregate(this.uok,this._user);
		_productValidation = new ProductValidation();
	}

	public PagingDTO findCommodityDetails(Integer commodityId, Integer brandId, Integer channelSaleId, Integer pageIndex) throws Exception {
		return this.tryCatch(() -> {
			/// _productValidation.validateString(brand);
			return _agg.findCommodityDetail(commodityId, brandId, channelSaleId, pageIndex);
		});

	}

	public PagingDTO findCommodityDetailFull(Integer commodityId, Integer brandId, Integer channelSaleId, Integer pageIndex) throws Exception {
		return this.tryCatch(() -> {
			/// _productValidation.validateString(brand);
			return _agg.findCommodityDetailFull(commodityId, brandId, channelSaleId, pageIndex);
		});

	}
	
	
	public Object getHisProdPaging(Integer pageIndex) throws Exception {
		return this.tryCatch(() -> {
			return _agg.getHisProdPaging(pageIndex);
		});
	}

	public Object findHisUploadPading(Integer pageIndex) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findHisUploadPading(pageIndex);
		});
	}
	
	public List<MappingHierarchy> findProductConfig(Long productId) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findProductConfig(productId);
		});
	}

	public Object findLstProductBy(Integer categoryId, String productName) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findLstProductBy(categoryId, productName);
		});
	}
	
	public Object findCodeTableByParent(Integer productChannelId, String objectType, String status) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findCodeTableByParentId(productChannelId, objectType, status);
		});
	}

	public Object findCodeTableConfig(Integer productId, String objectType, Integer channelType) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findCodeTableConfig(productId, objectType, channelType);
		});
	}

	public Object findDataForBPM(CommodityDTO obj) throws Exception {
		return this.tryCatch(() -> {
			return _agg.findDataForBPM(obj);
		});
	}
	
	public String getFilePath(Long uplMasterId, String loginId) throws Exception {
		return this.tryCatch(()->{
			return _agg.getFilePath(uplMasterId, this._user.getLoginId());
		});
	}
	
	public Object findCodeTableDb(String codeGroup, String category, String status) throws Exception {
		if(StringUtils.isNullOrEmpty(codeGroup))
			throw new ValidationException("codeGroup is manatory !");
		if(StringUtils.isNullOrEmpty(category))
			throw new ValidationException("category is manatory !");
		return this.tryCatch(()->{
			return _agg.findCodeTableDb(codeGroup,category, status);
		});
	}
	
	public Object updateSchemeGroup(CommonDTO item) throws Exception {
		_productValidation.validateUpdateSchemeGroup(item);
		return this.tryCatch(() -> {
			return _agg.updateSchemeGroup(item, this._user.getLoginId());
		});

	}

	public Object insertCommodity(CommodityDTO commodityObj) throws Exception {
		_productValidation.validateInsertCommodity(commodityObj);
		return this.tryCatch(() -> {
			return _agg.insertCommodity(commodityObj, this._user.getLoginId(), true);
		});
	}

	public Object insertProductConfig(List<CommodityDTO> lstCommodityObj) throws Exception {
		_productValidation.validateInsertProductConfig(lstCommodityObj);
		return this.tryCatch(() -> {
			return _agg.insertProductConfig(lstCommodityObj, this._user.getLoginId());
		});

	}

	public Object insertLstCommodity(File fileUpload, String payload) throws Exception {
		_productValidation.validateInsertLstCommodity(fileUpload, payload);
		return this.tryCatch(() -> {
			return _agg.insertLstCommodity(fileUpload, payload, this._user.getLoginId());
		});

	}
	 
	public Object deleteProductGroup(CommonDTO item) throws Exception {
		_productValidation.validateDeleteProductGroup(item);
		return this.tryCatch(() -> {
			return _agg.deleteProductGroup(item, this._user.getLoginId());
		});
	}
}
