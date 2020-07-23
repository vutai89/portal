package com.mcredit.data.common.repository;

import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.common.entity.Calendar;
import com.mcredit.data.repository.IRepository;

public class CalendarRepository extends BaseRepository implements IRepository<Calendar> {
	public CalendarRepository(Session session) {
		super(session);
	}

	public void add(Calendar item) {
	}
	public void update(Calendar item) {
	}
	public void upsert(Calendar item) {
		this.session.saveOrUpdate("Calendar", item);
	}
	public void remove(Calendar item) {
		this.session.delete("Calendar", item);
	}

	public List<Calendar> findCalendarByCategory(String Category) {
		return this.session.createNamedQuery("findCalendarByCategory", Calendar.class)
				.setParameter("category", Category).list();
		//return this.session.getNamedQuery("findActiveCodeTable").list();
	}	

}
