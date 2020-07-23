package com.mcredit.data.check_vehicle_price;

import org.hibernate.Session;

import com.mcredit.data.BaseUnitOfWork;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.check_vehicle_price.entity.MotorPrice;
import com.mcredit.data.check_vehicle_price.repository.MotorPriceRepository;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.pcb.repository.CreditBureauDataRepository;
import com.mcredit.data.repository.IRepository;

public class UnitOfWorkCheckVehiclePrice extends BaseUnitOfWork {
	
	public UnitOfWorkCheckVehiclePrice() {
		super();
	}

	public UnitOfWorkCheckVehiclePrice(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
	}
	
	private IRepository<MotorPrice> _motorPriceRepository = null;
	
	public MotorPriceRepository createMotorPriceRepository() {
		if (_motorPriceRepository == null) {
			_motorPriceRepository = (IRepository<MotorPrice>) new MotorPriceRepository(this.session);
		}
		return (MotorPriceRepository) _motorPriceRepository;
	}
	
}
