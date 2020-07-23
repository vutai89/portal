package com.mcredit.data.common.repository;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.AuditDataChange;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.PagingDTO;
import com.mcredit.model.dto.ProdHisDetailsDTO;

public class AuditDataChangeRepository extends BaseRepository implements IRepository<AuditDataChange> {

	public AuditDataChangeRepository(Session session) {
		super(session);
	}

	public List<AuditDataChange> getAll() {
		return this.session.createQuery("from AuditDataChange", AuditDataChange.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public PagingDTO  getHisProdPaging(Integer pageIndex, Integer pageSize) throws Exception {
		BigDecimal total;
		Integer start = pageIndex == 0 || pageIndex == 1 ? 0 : (pageIndex -1)*pageSize;
		Integer end = start + pageSize + 1;
		Query<?> query = this.session.getNamedQuery("getHistoryDetailsCount");
		total = (BigDecimal) query.list().get(0);
		query = this.session.getNamedQuery("getHistoryDetails")
				.setParameter("p_start", start)
				.setParameter("p_end", end);
		List<ProdHisDetailsDTO> lstResult = transformList(query.list(), ProdHisDetailsDTO.class);
		for(ProdHisDetailsDTO item : lstResult) {
			String result = item.getContent() != null ? item.getContent().getSubString(1, (int) item.getContent().length()) : "";
			item.setsContent(result);
			item.setContent(null);
		}
		PagingDTO pagingDTO = new PagingDTO(total.intValue(), start, end, lstResult);
		return pagingDTO;   
	}
	
	public void add(AuditDataChange item) {
	}

	public void insert(AuditDataChange item) {
		this.session.save("AuditDataChange", item);
	}

	public void update(AuditDataChange item) {
	}

	public void upsert(AuditDataChange item) {   
		this.session.saveOrUpdate("AuditDataChange", item);
	}

	public void remove(AuditDataChange item) {
		this.session.delete("AuditDataChange", item);
	}
	
	public List<AuditDataChange> findActiveAuditDataChange() {
		return this.session.createQuery(" FROM " + AuditDataChange.class.getName() + " where recordStatus = 'A' and status = 'A' ORDER by category, productCatId, productGroupId, productId, codeValue1 ", AuditDataChange.class).list();
	}	
}