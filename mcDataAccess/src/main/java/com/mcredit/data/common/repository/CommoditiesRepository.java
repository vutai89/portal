package com.mcredit.data.common.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.common.entity.Commodities;
import com.mcredit.data.repository.IRepository;

public class CommoditiesRepository extends BaseRepository implements
		IRepository<Commodities> {

	public CommoditiesRepository(Session session) {
		super(session);
	}

	public void add(CodeTable item, String addType) {
		
//		return this.session.getNamedNativeQuery("insertCodeTable")
//				.setParameter("pCategory",item.getCategory())
//				.setParameter("pCodeGroup", item.getCodeGroup())
//				.setParameter("pName", ccy)
//				.executeUpdate();

	}

	public void update(Commodities item) {
	}

	public void upsert(Commodities item) {
		this.session.saveOrUpdate("Commodities", item);
	}

	public void remove(Commodities item) {
		this.session.delete("Commodities", item);
	}
}
