package com.mcredit.data.warehouse.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IMergeRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.warehouse.entity.WhCavetInfo;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.warehouse.CavetInfor;

public class WareHouseCavetInfoRepository  extends BaseRepository implements IRepository<WhCavetInfo>, IAddRepository<WhCavetInfo>, IMergeRepository<WhCavetInfo>  {

	public WareHouseCavetInfoRepository(Session session) {
		super(session);
	}

	@Override
	public void add(WhCavetInfo item) {
		this.session.save(item);
	}

	@Override
	public void merge(WhCavetInfo item) {
		this.session.merge(item);
	}
	
	public WhCavetInfo getById(Long id) {
		return this.session.find(WhCavetInfo.class, id);
	}
	
	public void update(WhCavetInfo item) {
		this.session.saveOrUpdate("WhCavetInfo", item);
	}

	public WhCavetInfo getCavetInfo(Long whId, Long type) {

		List<WhCavetInfo> result = new ArrayList<>();
		List<?> lst = this.session.createNativeQuery("select * from (select * from wh_cavet_info where wh_doc_id = :WH_DOC_ID and type = :TYPE ORDER BY VERSION DESC) where rownum = 1 ")
				.setParameter("WH_DOC_ID", whId).setParameter("TYPE", type).getResultList();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, WhCavetInfo.class));
			}
		}
		return !result.isEmpty() ?  result.get(0) : null;
		
	}
}
