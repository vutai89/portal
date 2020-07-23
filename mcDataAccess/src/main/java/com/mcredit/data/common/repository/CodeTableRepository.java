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
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.CodeTableComboboxDTO;
import com.mcredit.model.enums.product.MapType;
import com.mcredit.util.StringUtils;

public class CodeTableRepository extends BaseRepository implements IRepository<CodeTable> {

	public CodeTableRepository(Session session) {
		super(session);
	}

	public void add(CodeTable item) {
	}

	public void update(CodeTable item) {
	}

	public void upsert(CodeTable item) {
		this.session.saveOrUpdate("CodeTable", item);
	}

	public void remove(CodeTable item) {
		this.session.delete("CodeTable", item);
	}
	
	public List<CodeTable> findActiveCodeTable() {
		return this.session.createQuery(" FROM " + CodeTable.class.getName() + " where recordStatus = 'A' and status = 'A' ORDER by category, productCatId, productGroupId, productId, codeValue1 ", CodeTable.class).list();
		//return this.session.getNamedQuery("findActiveCodeTable").list();
	}	

	public CodeTable getById(Long id) {		
		return this.session.find(CodeTable.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CodeTable> findCodeTableBy(String codeGroup, String category, String status) {
		CriteriaBuilder cb = this.session.getCriteriaBuilder();
		CriteriaQuery<CodeTable> cr = cb.createQuery(CodeTable.class);
		Root<CodeTable> root = cr.from(CodeTable.class);
		List<Predicate> lstPredicates = new ArrayList<>();
		if(!StringUtils.isNullOrEmpty(codeGroup))  lstPredicates.add(cb.equal(root.get("codeGroup"),codeGroup));   
		if(!StringUtils.isNullOrEmpty(category)) lstPredicates.add(cb.equal(root.get("category"),category));
		if(!StringUtils.isNullOrEmpty(status)) lstPredicates.add(cb.equal(root.get("status"),status));   
		Predicate[] stringArray = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
		cr.select(root).where(stringArray);
		cr.select(root).orderBy(cb.asc(root.get("description1")));
		Query query = this.session.createQuery(cr);
		List<CodeTable> results = query.getResultList();
		return results;
	}
	
	public List<CodeTable> findCodeTableBy(String codeGroup, String category, String codeValue1, String status) {
		String querySelect = " FROM " + CodeTable.class.getName() + " where codeGroup = '"+codeGroup+"' and category = '"+category+"' and codeValue1='"+codeValue1+"' ";
		if(!StringUtils.isNullOrEmpty(status))
			querySelect+=" and status='"+status+"' ";
		querySelect+= " ORDER by description1";
		List<CodeTable> lstResult = this.session.createQuery(querySelect, CodeTable.class).setHibernateFlushMode(FlushMode.ALWAYS).list();
		return lstResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<CodeTableComboboxDTO> findCommodityByChannel(Integer channelId) {
		List<?> lstResult = this.session.getNamedQuery("getCommodityByChannel").
				setParameter("p_channel", channelId).setHibernateFlushMode(FlushMode.ALWAYS).list();
		return transformList(lstResult, CodeTableComboboxDTO.class);
	}   
	
	@SuppressWarnings("unchecked")
	public List<CodeTableComboboxDTO> findCodeTableConfig(Integer productId, String sMapType) {
		List<?> lstResult = this.session.getNamedQuery("getCodeTableConfig").setParameter("p_map_type", sMapType).setParameter("p_map_id1",productId).list();
		return transformList(lstResult, CodeTableComboboxDTO.class);
	}
	
	public int insertHqlQuery(CodeTable item) {
		// pCategory,:pCodeGroup,:pName
		return this.session.getNamedNativeQuery("insertCodeTable")
				.setParameter("pCategory", item.getCategory())
				.setParameter("pCodeGroup",item.getCodeGroup())
				.setParameter("pName", item.getDescription1()).executeUpdate();
	}
	
	
	public void insertTypeCommodity(CodeTable item, String userName) {
		item.setProductCatId(0);
		item.setProductGroupId(0);
		item.setRecordStatus("A");
		item.setStatus("A");
		item.setCreatedDate( new Date());
		item.setCreatedBy(userName);
		item.setLastUpdatedDate(new Date());
		item.setLastUpdatedBy(userName);
		item.setProductId(0);  
		item.setStartEffDate(new Date());
		this.session.saveOrUpdate("CodeTable", item);
	}
	
	
	public List<CodeTable> findActiveCodeTableOrderById() {
		return this.session.createQuery(" FROM " + CodeTable.class.getName() + " where recordStatus = 'A' and status = 'A' ORDER by id ", CodeTable.class).list();
		//return this.session.getNamedQuery("findActiveCodeTableOrderById").list();
	}	
	
	/*public List<CodeTable> findCallStatus() {
		return this.session.createNativeQuery(" select * from CODE_TABLE where CODE_GROUP='CALL' and CATEGORY in ('CALL_STAT', 'CALL_RSLT') ", CodeTable.class).list();
	}*/
	
	@SuppressWarnings("unchecked")
	public List<CodeTableComboboxDTO> getByMapping(Long mapId1, String sMapType) {
		List<?> lstResult = this.session.getNamedQuery("getCodeTableByMapping").
				setParameter("p_map_id1", mapId1)
				.setParameter("p_map_type",MapType.Product_ProductGroup.value()).list();
		return transformList(lstResult, CodeTableComboboxDTO.class);
	}
}