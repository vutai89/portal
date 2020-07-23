package com.mcredit.data.common.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.model.dto.CodeTableComboboxDTO;
import com.mcredit.model.dto.ProdHisDetailsDTO;
import com.mcredit.model.dto.ProductComboboxDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.ProductInterestDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.util.StringUtils;

public class ProductRepository extends BaseRepository implements IUpdateRepository<Product> {
	public ProductRepository(Session session) {
		super(session);
	}

	public void insert(Product item) {
		this.session.save(item);
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

	public Product findProductById(Long productId) {
		return this.session.get(Product.class, productId);
	}
	
	public Product getProductBy(String productCode) {
		Product item = null;
		List<?> lst = this.session.createQuery("from Product where productCode = :productCode and endEffDate >= :endEffDate", Product.class)
				.setParameter("productCode", productCode != null ? productCode.trim() : "")
				.setParameter("endEffDate", new Date()).list();
		if (lst != null && !lst.isEmpty())
			item = (Product) lst.get(0);
		return item;
	}
	
	public ProductInterestDTO getProductInterestBy(String productCode) {
		List<?> lst = this.session.createNamedQuery("getProductInterestBy").setParameter("p_product_code", productCode).list();
		if(!lst.isEmpty())
			return (ProductInterestDTO) transformList(lst, ProductInterestDTO.class).get(0);
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ProductComboboxDTO getProductByCode(String productCode) {
		ProductComboboxDTO result=null;
		Query query = this.session.getNamedQuery("getProductByCode")
				.setParameter("p_product_code", productCode);
		List<Object> lst = query.list();
		List<ProductComboboxDTO> products = transformList(lst, ProductComboboxDTO.class);
		if(products.size() > 0) result = products.get(0);
		return result;
	}

	public List<Product> getAllProduct() {
		return this.session.createQuery("from Product", Product.class).list();
	}

	public List<Product> findProductBy(Long productId, Integer schemeGroupId) {
		CriteriaBuilder cb = this.session.getCriteriaBuilder();
		CriteriaQuery<Product> cr = cb.createQuery(Product.class);
		Root<Product> root = cr.from(Product.class);
		List<Predicate> lstPredicates = new ArrayList<>();
		if(productId!=null) lstPredicates.add(cb.equal(root.get("id"),productId)); 
		if(schemeGroupId!=null) lstPredicates.add(cb.equal(root.get("schemeGroupId"),schemeGroupId));
		Predicate[] stringArray = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
		cr.select(root).where(stringArray);
		Query<Product> query = this.session.createQuery(cr);
		List<Product> results = query.getResultList();
		return results;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ProductComboboxDTO> findLstProductBy(String channelString, String productName) {
		/**/ Query query = this.session.getNamedQuery("getProductByName")
				.setParameter("p_channel", channelString);
		if(!StringUtils.isNullOrEmpty(productName)) query.setParameter("p_product_name", "%"+productName+"%");
		else query.setParameter("p_product_name", "-1");
		List<Object> lst = query.list();
		List<ProductComboboxDTO> products = transformList(lst, ProductComboboxDTO.class);
		return products;
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
	
	public Double getInterestYearlyById(Long id) {
		return (Double) this.session.getNamedNativeQuery("getInterestYearlyById")
				.setParameter("id", id)
				.addScalar("yearly_rate",DoubleType.INSTANCE)
				.uniqueResult();
	}

}
