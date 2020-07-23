package com.mcredit.data.product.repository;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.model.dto.CodeTableComboboxDTO;
import com.mcredit.model.dto.PagingDTO;
import com.mcredit.model.dto.ProdHisDetailsDTO;
import com.mcredit.model.dto.ProdHisUploadDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.product.CommodityDetailsDTO;
import com.mcredit.util.StringUtils;

public class CommodityRepository extends BaseRepository implements IUpdateRepository<Product> {
	public CommodityRepository(Session session) {
		super(session);
	}

	public void update(Product item) {
		this.session.update(item);
	}

	public void upsert(Product item) {
		this.session.saveOrUpdate("Product", item);
	}

	public void remove(Product item) {
		this.session.delete("Product", item);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagingDTO findCommondityDetails(Integer commodityId,Integer brandId, Long channelSaleId, Integer pageIndex, Integer pageSize) {
		// Thiet lap cac thong so start, end , total rows
		Integer total = 0;
		Integer start = (pageIndex -1) * pageSize + 1;
		Integer end = start + (pageSize -1); 
		Query query = this.session.getNamedQuery("findCommodityDetail");
		if(commodityId !=  null) query.setParameter("p_commodity", commodityId); else query.setParameter("p_commodity", -1);
		if(brandId !=  null) query.setParameter("p_brand", brandId); else query.setParameter("p_brand", -1);
		query.setParameter("p_parent_id", channelSaleId);
		query.setParameter("p_start", -1);
		query.setParameter("p_end", -1);
		total = query.list().size();
		query.setParameter("p_start", start);
		query.setParameter("p_end", end);
		List<?> lst = query.list();
		List<Object> lstResult = transformList(lst, CommodityDetailsDTO.class);
		PagingDTO pageingDTO = new PagingDTO(total, start, end, lstResult);
		return pageingDTO;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PagingDTO findCommondityDetailsFull(Integer commodityId,Integer brandId, Long channelSaleId, Integer pageIndex, Integer pageSize) {
		// Thiet lap cac thong so start, end , total rows
		Integer total = 0;
		Integer start = (pageIndex -1) * pageSize + 1;
		Integer end = start + (pageSize -1); 
		Query query = this.session.getNamedQuery("findCommodityDetail");
		if(commodityId !=  null) query.setParameter("p_commodity", commodityId); else query.setParameter("p_commodity", -1);
		if(brandId !=  null) query.setParameter("p_brand", brandId); else query.setParameter("p_brand", -1);
		query.setParameter("p_parent_id", channelSaleId);
		query.setParameter("p_start", -1);
		query.setParameter("p_end", -1);
		List<?> lst = query.list();
		total = lst.size();
		List<Object> lstResult = transformList(lst, CommodityDetailsDTO.class);
		PagingDTO pageingDTO = new PagingDTO(total, start, end, lstResult);
		return pageingDTO;
	}
	
	public Product getProductBy(String productCode) {
		Product item = null;
		List<?> lst = this.session.createQuery("from Product where productCode = :productCode", Product.class)
				.setParameter("productCode", productCode != null ? productCode.trim() : "").list();
		if (lst != null && !lst.isEmpty())   
			item = (Product) lst.get(0);
		return item;
	}

	public List<Product> getAllProduct() {
		List<Product> lstProduct = this.session.createQuery("from Product", Product.class).list();
		return lstProduct;
	}

	@SuppressWarnings("unchecked")
	public List<Product> findProducts() {
		return this.session.getNamedQuery("findProducts").list();
	}

	@SuppressWarnings("unchecked")
	public List<ProductDTO> findEffectiveProducts() {
		List<Object> lst = this.session.getNamedNativeQuery("findEffectiveProducts").list();
		List<ProductDTO> products = transformList(lst, ProductDTO.class);
		return products;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CodeTableComboboxDTO> getModelForBpm(Long parentMapId, String status) {
		Query query = this.session.getNamedQuery("getModelForBPM");
		query.setParameter("parent_map_id", parentMapId); 
		if(!StringUtils.isNullOrEmpty(status)) query.setParameter("p_status", status);
		else query.setParameter("p_status", "-1");
		List<CodeTableComboboxDTO> lst = query.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lst, CodeTableComboboxDTO.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CodeTableComboboxDTO> getCodeTableMapId2ById1(Long commodityId, String mapType, String status) {
		Query query = this.session.getNamedQuery("getCodeTableMapId2ById1");
		query.setParameter("p_map_id1", commodityId); 
		query.setParameter("p_type_map", mapType);
		if(!StringUtils.isNullOrEmpty(status)) query.setParameter("p_status", status);
		else query.setParameter("p_status", "-1");
		List<CodeTableComboboxDTO> lst = query.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lst, CodeTableComboboxDTO.class);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CodeTable> getProComConfig(Long productId,String status) {
		Query query = this.session.getNamedQuery("selectProCommConfig");
		query.setParameter("product_id", productId); 
		if(!StringUtils.isNullOrEmpty(status)) query.setParameter("p_status", status);
		else query.setParameter("p_status", "-1");
		List<?> lst = query.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lst, CodeTableComboboxDTO.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CodeTableComboboxDTO> getProComBrandConfig(Long productId,Long comId) {
		Query query = this.session.getNamedQuery("selectProCommBrandConfig");
		query.setParameter("product_id", productId); 
		query.setParameter("p_comm_id", comId); 
		List<?> lst = query.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lst, CodeTableComboboxDTO.class);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CodeTable> getComByPrdGroup(Long product_group, String status) {
		Query query = this.session.getNamedQuery("getCommByPrdGroup");
		query.setParameter("product_group", product_group); 
		if(!StringUtils.isNullOrEmpty(status)) query.setParameter("p_status", status);
		else query.setParameter("p_status", "-1");
		List<?> lst = query.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lst, CodeTableComboboxDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	public PagingDTO  getHisProdUploadPaging(Integer pageIndex, Integer pageSize, Long urlTypeId) {
		Integer total = 0;
		Integer start = pageIndex == 0 || pageIndex == 1 ? 0 : (pageIndex -1)*pageSize;
		Integer end = start + pageSize + 1;
		Query<?> query = this.session.getNamedQuery("getHistoryUpload")
				.setParameter("p_upl_type", urlTypeId)
				.setParameter("p_start", -1)     
				.setParameter("p_end", -1);
		total = query.list().size();
		query = this.session.getNamedQuery("getHistoryUpload")
				.setParameter("p_upl_type", urlTypeId)
				.setParameter("p_start", start)  
				.setParameter("p_end", end);
		List<Object> lstResult = transformList(query.list(), ProdHisUploadDTO.class);
		PagingDTO pagingDTO = new PagingDTO(total, start, end, lstResult);
		return pagingDTO;
	}
}
