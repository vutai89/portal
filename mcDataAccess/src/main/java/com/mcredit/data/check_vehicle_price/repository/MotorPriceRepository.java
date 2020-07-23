package com.mcredit.data.check_vehicle_price.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.check_vehicle_price.entity.MotorPrice;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.vehicle_price.MotorPriceDataDTO;
import com.mcredit.model.dto.vehicle_price.MotorPriceDataDetailDTO;
import com.mcredit.util.StringUtils;

public class MotorPriceRepository extends BaseRepository implements IRepository<MotorPrice> {
	public MotorPriceRepository(Session session) {
		super(session);
	}
	
	public void save(MotorPrice item) {
		this.session.save("MotorPrice", item);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getDroplistSearch(String column) {
		String sql = "select distinct(" + column + ") from motor_price";
		return this.session.createNativeQuery(sql).getResultList();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public MotorPriceDataDTO findMotorPrice(String brand, String motorCode, String motorType, Integer page, Integer rowPerPage) throws DataException {
		MotorPriceDataDTO results = new MotorPriceDataDTO();

		String condition = "";

		if (!StringUtils.isNullOrEmpty(brand))
			condition += " and upper(a.BRAND) like upper('%" + brand + "%')";

		if (!StringUtils.isNullOrEmpty(motorCode))
			condition += " and upper(a.MOTOR_CODE) like upper('%" + motorCode + "%')";

		if (!StringUtils.isNullOrEmpty(motorType))
			condition += " and upper(a.MOTOR_TYPE) like upper('%" + motorType + "%')";
		
		if (!StringUtils.isNullOrEmpty(condition)) 
			condition = " where 1=1 " + condition;
		
		String sqlCount = "select count(a.id) from motor_price a " + condition;
		
		results.setTotalRows((BigDecimal) this.session.createNativeQuery(sqlCount).uniqueResult());
		
		String sql = "select * from motor_price a " + condition + " order by a.ID ASC";

		NativeQuery query = this.session.createNativeQuery(sql)
				.setFirstResult((page - 1) * rowPerPage)
				.setMaxResults(rowPerPage);
		
		List lst = query.list();
		if (lst != null && lst.size() > 0)
			results.setDataRows(transformList(lst, MotorPriceDataDetailDTO.class));

		return results;
	}
	
	public void delete() {
		String stringQuery = "delete from MotorPrice";
		this.session.createQuery(stringQuery)
			.executeUpdate();
	}
}
