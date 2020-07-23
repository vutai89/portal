package com.mcredit.business.telesales.manager;

import java.util.List;

import com.mcredit.business.telesales.aggregate.ProspectManagerAggregate;
import com.mcredit.business.telesales.aggregate.TeamAggregate;
import com.mcredit.business.telesales.converter.Converter;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.business.telesales.object.ProspectSearch;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.telesales.CallResultDTO;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.model.dto.telesales.ProspectCallingPermissionDTO;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;
import com.mcredit.model.dto.warehouse.EmployeeDTO;
import com.mcredit.model.object.ProspectManagementData;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.PermissionException;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class ProspectManagerManager extends BaseManager{

	private UserDTO _user;
	
	public ProspectManagerManager(UserDTO user){
		_user = user;
	}
	
	public ProspectManagementData findProspect(ProspectSearch input, String campaignCodeLstOfSup) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getProspectManagerAggregateInstance(input, uok).findProspect(input, campaignCodeLstOfSup);			
		});
	}
	
	public boolean prospectReAssign(ProspectReAssignDTO input) throws Exception {
		return this.tryCatch(()->{		
			TelesalesFactory.getProspectReassignValidation().validateProspectReassign(input);
			TelesalesFactory.getProspectManagerAggregateInstance(input, uok).prospectReAssign(input);			
			return true;
		});
	}
	
	public String findCallResult(Long uplCusId) throws Exception{
		
		return this.tryCatch(()->{
			ProspectManagerAggregate item = TelesalesFactory.getProspectManagerAgg(uok);
			
			CustProspect obj = item.findCustProsptectByUPLCustId(uplCusId);
			Boolean isCustProspectExist = (obj != null);
			
			if (isCustProspectExist) {
				return JSONConverter.toJSON(Converter.convertFrom(item.findCallResult(obj.getId())));
			}
			
			return "[]";
			
		});
		
	}
	
	public Object insertProspectCall(CallResultDTO request) throws Exception{

		return this.tryCatch(()->{
			
			CodeTableDTO nextAction = TelesalesFactory.getProspectManagerValidation().validateInsertProspectCall(request);
			
			return TelesalesFactory.getProspectManagerAgg(uok).insertProspectCall(request, nextAction);
		});
	}
	
	public Object upsertCustomerProspect(CustProspectDTO CPDTO) throws Exception {

		return this.tryCatch(()->{
			TelesalesFactory.getProspectManagerValidation().validateUpsertCustomerProspect(CPDTO);
			
			ProspectManagerAggregate pmAgg = TelesalesFactory.getProspectManagerAgg(uok);
			
			this.hasReadWritePermission(CPDTO.getUplCustomerId());
			
			return pmAgg.upsertCustomerProspect(CPDTO, this._user);
			
		});
		
	}
	
	/*public Object getCustProspectById(Long custProspectId) throws Exception {
		
		return this.tryCatch(()->{
			
			if(custProspectId == null)
				throw new ValidationException("Cust Prospect Id is required.");
			
			return TelesalesFactory.getProspectManagerAgg(uok).getCustProspectById(custProspectId); 
		});
		
	}*/
	
	public CustProspectDTO getCustProspectByUplCusId(Long uplCusId) throws Exception {
		
		return this.tryCatch(()->{
			
			if(uplCusId == null)
				throw new ValidationException(Messages.getString("validation.field.madatory", "UPL Cus Id"));
			
			CustProspectDTO result = TelesalesFactory.getProspectManagerAgg(uok).getCustProspectByUplCusId(uplCusId);
			
			if(result == null)
				throw new ValidationException(Messages.getString("validation.field.invalidFormat", "UPL Cus Id"));
			
			return result; 
		});
	}
	
	public Object hasPermissionOnProspectCallingPage(Long uplCusId) throws Exception {
		
		return this.tryCatch(()->{
			
			String permission = "None";
			
			if(this._user.isSuperVisor())
				permission = "Read";
			
			if(this.hasReadPermission(uplCusId))
				permission = "Read";
			
			if(this.hasReadWritePermission(uplCusId))
				permission = "ReadWrite";
			
			if("None".equals(permission))
				throw new PermissionException("[loginId: " + this._user.getLoginId() + "] - [uplCusId: " + uplCusId + "] - " + Messages.getString("unauthorized"));
			 
			return new ProspectCallingPermissionDTO(permission);
		});
	}
	
	
	
	private boolean hasReadWritePermission(Long uplCusId) throws ValidationException{
		try {
			ProspectManagerAggregate pmAgg = TelesalesFactory.getProspectManagerAgg(uok);
			
			AllocationDetail ad = pmAgg.getAllocationDetailByUPLCustomerIdAndObjectId(uplCusId);
			if(ad == null || !ad.getObjectId().equals(this._user.getId()) )
				return false;
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean hasReadPermission(Long uplCusId) throws ValidationException{
		try {
			ProspectManagerAggregate pmAgg = TelesalesFactory.getProspectManagerAgg(uok);
		
			AllocationDetail ad = pmAgg.getAllocationDetailByUPLCustomerIdAndObjectId(uplCusId);
			if(ad == null)
				return false;
			
			TeamAggregate tAgg = new TeamAggregate(uok);
			List<Users> teamLeaders = tAgg.getTeamLeadersBy(ad.getObjectId());
			if(teamLeaders == null || teamLeaders.size() == 0)
				return false;
			
			for (Users teamLeader : teamLeaders) {
				if(teamLeader != null && teamLeader.getId().equals(this._user.getId()))
					return true;
			}
			
			return false;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	public Object getManagerByLoginId(UserDTO user) {
		EmployeeDTO employee = TelesalesFactory.getProspectManagerAgg(uok).getManagerByLoginId(user.getLoginId());
		return employee;
	}

}