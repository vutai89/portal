package com.mcredit.data.common.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.FlushMode;
import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.product.entity.MappingHierarchy;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.enums.product.ActionData;

public class MappingHierarchyRepository extends BaseRepository implements IRepository<MappingHierarchy> {

	public MappingHierarchyRepository(Session session) {
		super(session);
	}

	public void insert(MappingHierarchy item, String userName) {
		item.setStatus(ActionData.Active.value());
		item.setRecordStatus(ActionData.Active.value());
		item.setCreatedBy(userName);
		item.setCreatedDate(new Date());
		item.setLastUpdatedBy(userName);
		this.session.save(item);
	}
	
	public void update(MappingHierarchy item) {
	}

	public void upsert(MappingHierarchy item) {
		this.session.saveOrUpdate("CodeTable", item);
	}

	public void remove(MappingHierarchy item) {
		this.session.delete("CodeTable", item);
	}
	
	public List<CodeTable> findActiveCodeTable() {
		return this.session.createQuery(" FROM " + CodeTable.class.getName() + " where recordStatus = 'A' and status = 'A' ORDER by category, productCatId, productGroupId, productId, codeValue1 ", CodeTable.class).list();
	}	

	@SuppressWarnings("unchecked")
	public List<MappingHierarchy> findLstMappingHierarchy(Long mapId1, Long mapId2, Long parentId, String mapType) {	
		CriteriaBuilder cb = this.session.getCriteriaBuilder();
		CriteriaQuery<MappingHierarchy> cr = cb.createQuery(MappingHierarchy.class);
		Root<MappingHierarchy> root = cr.from(MappingHierarchy.class);
		List<Predicate> lstPredicates = new ArrayList<>();
		if(mapId1 !=null) lstPredicates.add(cb.equal(root.get("mapId1"),mapId1));   
		if(mapId2 !=null) lstPredicates.add(cb.equal(root.get("mapId2"),mapId2));   
		if(parentId!=null)
			lstPredicates.add(cb.equal(root.get("parentId"),parentId));   
		lstPredicates.add(cb.equal(root.get("mapType"),mapType));   
		Predicate[] stringArray = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
		cr.select(root).where(stringArray);
		cr.select(root).orderBy(cb.asc(root.get("mapId1")));
		Query query = this.session.createQuery(cr).setHibernateFlushMode(FlushMode.ALWAYS);
		List<MappingHierarchy> results = query.getResultList();
		return results;
	}
	
	public MappingHierarchy findMappingHierarchy(Long mapId1, Long mapId2, Long parentId, String mapType) {	
		List<MappingHierarchy> lstResult = this.findLstMappingHierarchy(mapId1, mapId2, parentId, mapType);
		if(lstResult.size() > 0) return lstResult.get(0);
		else return null;
	}	
	
	public int deletePrdComQuery(Long productId) {
		return this.session.getNamedNativeQuery("deleteProCommConfig")
				.setParameter("product_id", productId).executeUpdate();
	}
	
	public int deletePrdComBrandQuery(Long productId) {
		return this.session.getNamedNativeQuery("deleteProCommBrandConfig")
				.setParameter("product_id", productId).executeUpdate();
	}
	
	public int deleteById1AndType(Long mapId1, String mapType) {
		return this.session.getNamedNativeQuery("deleteMappingByMapId1")
				.setParameter("map_id1", mapId1)
				.setParameter("map_type", mapType)
				.executeUpdate();
	}
	
	public List<CodeTable> findActiveCodeTableOrderById() {
		return this.session.createQuery(" FROM " + CodeTable.class.getName() + " where recordStatus = 'A' and status = 'A' ORDER by id ", CodeTable.class).list();
	}	
}