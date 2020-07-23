package com.mcredit.business.telesales.factory;

import com.mcredit.business.telesales.aggregate.ProspectManagerAggregate;
import com.mcredit.business.telesales.aggregate.TeamAggregate;
import com.mcredit.business.telesales.aggregate.UploadAggregate;
import com.mcredit.business.telesales.aggregate.XSellAggregate;
import com.mcredit.business.telesales.object.ProspectSearch;
import com.mcredit.business.telesales.validation.ProspectManagerValidation;
import com.mcredit.business.telesales.validation.ProspectReassignValidation;
import com.mcredit.business.telesales.validation.SupervisorValidation;
import com.mcredit.business.telesales.validation.UploadValidation;
import com.mcredit.business.telesales.validation.XSellValidation;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;

public class TelesalesFactory {

	public static UploadAggregate getUploadAggregateInstance(UnitOfWorkTelesale uok) {

		return new UploadAggregate(uok);
	}

	public static TeamAggregate getTeamAggregateInstance(UnitOfWork uok) {

		return new TeamAggregate(uok);
	}
	
	public static UploadAggregate getUploadAggregateInstanceCustom(UnitOfWorkCommon uok) {

		return new UploadAggregate(uok);
	}
	
	/*public static UploadBackground getUploadBackgroundInstance(UplDetail uplDetail) {

		return new UploadBackground(uplDetail);
	}*/

	public static ProspectManagerAggregate getProspectManagerAggregateInstance(ProspectSearch input, UnitOfWork uok) {


		ProspectManagerAggregate item = null;

		if (input != null && uok != null)
			item = new ProspectManagerAggregate(uok.telesale,uok.common);

		return item;
	}
	
	public static ProspectManagerAggregate getProspectManagerAggregateInstance(ProspectReAssignDTO input, UnitOfWork uok) {

		ProspectManagerAggregate item = null;

		if (input != null && uok != null)
			item = new ProspectManagerAggregate(uok.telesale,uok.common);

		return item;
	}


	public static ProspectManagerAggregate getProspectManagerAgg(
			UnitOfWork uok) {

		ProspectManagerAggregate item = null;

		if (uok != null)
			item = new ProspectManagerAggregate(uok.telesale,uok.common);

		return item;
	}

	/*public static ProspectAggregate getProspectAggregateInstance(ProspectDTO input, UnitOfWorkTelesale uok) {

		ProspectAggregate item = null;

		if (input != null && uok != null) {

			item = new ProspectAggregate(uok);

			if (input.getUplMasterDTO() != null)
				item.setUplMaster(modelMapper.map(input.getUplMasterDTO(),
						UplMaster.class));

		}
		return item;
	}*/

	public static SupervisorValidation getSupervisorValidation() {
		return new SupervisorValidation();
	}
	
	public static ProspectReassignValidation getProspectReassignValidation() {

		return new ProspectReassignValidation();
	}

	/*public static TeamAggregate getTeamAggregateInstance1(UnitOfWorkTelesale uokTelesale) {
		return new TeamAggregate(uokTelesale);
	}*/
	
	public static ProspectManagerValidation getProspectManagerValidation() {
		return new ProspectManagerValidation();
	}
	
	public static UploadValidation getUploadValidation() {
		return new UploadValidation();
	}
	
	/*public static ProspectManagerAggregate getProspectManagerAggregateInstance(List<ReAssignDTO> input,UnitOfWork uok) {
		ProspectManagerAggregate item = null;

		if (input != null && uok != null)
			item = new ProspectManagerAggregate(uok.telesale,uok.common);

		return item;
	}*/
	
	public static XSellAggregate getXSellAggregate(UnitOfWorkTelesale uokTelesale) {
		return new XSellAggregate(uokTelesale);
	}
	public static XSellValidation getXSellValidation() {
		return new XSellValidation();
	}
}
