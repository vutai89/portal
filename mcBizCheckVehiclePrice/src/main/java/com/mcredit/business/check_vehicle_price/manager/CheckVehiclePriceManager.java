package com.mcredit.business.check_vehicle_price.manager;
import java.io.InputStream;

import com.mcredit.business.check_vehicle_price.aggregate.CheckVehiclePriceAggregate;
import com.mcredit.business.check_vehicle_price.validation.CheckVehiclePriceValidation;
import com.mcredit.model.dto.vehicle_price.MotorPriceDataDTO;
import com.mcredit.model.dto.vehicle_price.MotorPriceDroplistSearch;
import com.mcredit.sharedbiz.manager.BaseManager;

public class CheckVehiclePriceManager extends BaseManager{

	private CheckVehiclePriceAggregate _agg;
	private CheckVehiclePriceValidation _validation;
	
	public CheckVehiclePriceManager() {
		_agg = new CheckVehiclePriceAggregate(this.uok);
		_validation = new CheckVehiclePriceValidation();
	}
	
	public MotorPriceDroplistSearch droplistSearch() throws Exception {
		return this.tryCatch(()->{
			return _agg.droplistSearch();			
		});
	}
	
	public MotorPriceDataDTO findMotorPrice(String brand, String motorCode, String motorType, Integer page, Integer rowPerPage) throws Exception {
		return this.tryCatch(()->{
			_validation.searchMotorPrice(page, rowPerPage);
			return _agg.findMotorPrice(brand, motorCode, motorType, page, rowPerPage);			
		});
	}
	
	public Object uploadVehiclePrice(InputStream fileContent, String userName) throws Exception {
		return this.tryCatch(() -> {
			_validation.uploadVehiclePrice(fileContent);
			return _agg.uploadVehiclePrice(fileContent, userName);
		});
	}
}
