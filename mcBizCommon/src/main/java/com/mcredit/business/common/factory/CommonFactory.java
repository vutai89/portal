package com.mcredit.business.common.factory;

import org.modelmapper.ModelMapper;

import com.mcredit.business.common.CommonAggregate;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.Commodities;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.dto.common.CommoditiesDTO;
import com.mcredit.model.dto.common.CommonDTO;
import com.mcredit.model.dto.common.MessageLogDTO;

public class CommonFactory {
	private static ModelMapper modelMapper = new ModelMapper();

	public static CommonAggregate getInstanceMessageLog(MessageLogDTO input, UnitOfWorkCommon uokCommon) {
		CommonAggregate item = new CommonAggregate(uokCommon);
		if (input != null)
			item.setMessageLog(modelMapper.map(input, MessageLog.class));
		return item;
	}

	public static CommonAggregate getInstanceCommodities(CommoditiesDTO input, UnitOfWorkCommon uokCommon) {
		CommonAggregate item = new CommonAggregate(uokCommon);
		if (input != null)
			item.setCommodities(modelMapper.map(input, Commodities.class));
		return item;
	}

	public static CommonAggregate getInstance(CommonDTO input, UnitOfWorkCommon uokCommon) {
		CommonAggregate item = new CommonAggregate(uokCommon);
		if (input.getCommoditiesDTO() != null)
			item.setCommodities(modelMapper.map(input.getCommoditiesDTO(), Commodities.class));
		if (input.getMessageLogDTO() != null)
			item.setMessageLog(modelMapper.map(input.getMessageLogDTO(), MessageLog.class));
		return item;
	}

	public static CommonAggregate getInstance(UnitOfWorkCommon uokCommon) {
		CommonAggregate item = new CommonAggregate(uokCommon);
		return item;
	}
}