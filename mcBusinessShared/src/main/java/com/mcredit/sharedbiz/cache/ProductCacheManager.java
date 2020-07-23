package com.mcredit.sharedbiz.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.enums.SessionType;

public class ProductCacheManager implements IDataCache {
	private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWork uok = null;
	private List<ProductDTO> productCache;
	private List<ProductDTO> effectiveProductCache;
	private HashMap<Long, ProductDTO>  productHashCache;
	
	

	private static ProductCacheManager instance;

	private ProductCacheManager() {
		initCache();
	}

	public static synchronized ProductCacheManager getInstance() {
		if (instance == null) {
			synchronized (ProductCacheManager.class) {
				if (null == instance) {
					instance = new ProductCacheManager();
				}
			}
		}
		return instance;
	}

	public void initCache() {

		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<Product> products = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.productTaskRepo().findProducts();
			});
	
			productCache = new ArrayList<ProductDTO>();

			if (products != null && products.size() > 0) {
				for (Product item : products) {
					productCache.add(modelMapper.map(item, ProductDTO.class));
				}
			}

			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			effectiveProductCache = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.productTaskRepo().findEffectiveProducts();
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<ProductDTO> getEffectiveProducts() {
		return effectiveProductCache;
	}

	public ProductDTO findProductByCode(String productCode) {
		try {
			if (productCache == null)
				initCache();

			if (productCode == null || "".equals(productCode.trim())) {
				System.out.println("productCode cannot be null or empty !");
				return null;
			}

			if (productCache != null && productCache.size() > 0) {
				for (ProductDTO item : productCache) {
					if (productCode.equals(item.getProductCode())) {
						return item;
					}

				}
			}	
			
		} catch (Exception e) {
			System.out.println("findProductByCode.ex: " + e.toString());
		}
		
		return null;
	}
	
	public List<ProductDTO> findListProductByCode(String productCode) {
		List<ProductDTO> listProduct = new ArrayList<>();
		try {
			if (productCache == null)
				initCache();

			if (productCode == null || "".equals(productCode.trim())) {
				System.out.println("productCode cannot be null or empty !");
				return null;
			}

			if (productCache != null && productCache.size() > 0) {
				for (ProductDTO item : productCache) {
					if (productCode.equals(item.getProductCode())) {
						listProduct.add(item);
					}
				}
			}	
			
		} catch (Exception e) {
			System.out.println("findListProductByCode.ex: " + e.toString());
		}
		
		return listProduct;
	}
	
	public ProductDTO findProductById(Long id) {
		try {
			if (productCache == null)
				initCache();

				for (ProductDTO item : productCache) {
					if (id.equals(item.getId())) {
						return item;
					}
			}	
		} catch (Exception e) {
			System.out.println("findProductById.ex: " + e.toString());
		}
		
		return null;
	}
	
	
	public HashMap<Long, ProductDTO> getProductHashCache() {
		if(null == productHashCache){
			productHashCache = new HashMap<>();
			if(!productCache.isEmpty()){				
				for (ProductDTO ProductDTO : productCache) {
					if(null != ProductDTO && null != ProductDTO.getId()){
						productHashCache.put(ProductDTO.getId().longValue(), ProductDTO);
					}
				}
			}
		}
		return productHashCache;
	}

	public void setProductHashCache(HashMap<Long, ProductDTO> productHashCache) {
		this.productHashCache = productHashCache;
	}

	public void refresh() {
		initCache();
	}
	
}
