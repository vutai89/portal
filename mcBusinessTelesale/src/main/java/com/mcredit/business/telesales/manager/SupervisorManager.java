package com.mcredit.business.telesales.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.telesales.AllocationCustomerDTO;
import com.mcredit.model.dto.telesales.AllocationMasterDTO;
import com.mcredit.model.dto.telesales.ErrorAllocateDTO;
import com.mcredit.model.enums.AllocatedToTag;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.factory.AllocationFactory;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class SupervisorManager extends BaseManager  {
		
	public ErrorAllocateDTO allocationCustomer(List<AllocationCustomerDTO> request, UserDTO currentUser, String isAsm, String xsm) throws Exception {
		
		return this.tryCatch(() -> {
        	
			System.out.println(" [BEGIN] List<AllocationCustomerDTO>  -----------> " + JSONConverter.toJSON(request));
		    
			TelesalesFactory.getSupervisorValidation().validateSupervisor(request);
			
			ErrorAllocateDTO errorDto = new ErrorAllocateDTO();
			List<Long> ltsError = new ArrayList<>();
			
			List<Integer> listItemDuplicated = checkDuplicateMterId(request);
			if(listItemDuplicated.size() > 0 )
				throw new ValidationException(Messages.getString("validation.field.duplicate"," uplMasterId " ) +   " : " + JSONConverter.toJSON(listItemDuplicated));	
			
			for (AllocationCustomerDTO aCustomerDTO : request) {
							
				if (aCustomerDTO.getAllocatedNumber() == null || !StringUtils.isNumeric(aCustomerDTO.getAllocatedNumber()) || Integer.valueOf(aCustomerDTO.getAllocatedNumber())  <=  0 )
					throw new Exception(Messages.getString("ts.allocation.number.not.valid") + ": " +  aCustomerDTO.getAllocatedNumber());
				
				try{
					
					UplMaster uplMaster = TelesalesFactory.getUploadAggregateInstance(uok.telesale).findUplMasterbyID(aCustomerDTO.getUplMasterId().longValue());
					if (uplMaster != null) {
						if (AllocatedToTag.SUPERVISOR.stringValue().equals(aCustomerDTO.getAllocatedType())) {
			
							if (Integer.parseInt(aCustomerDTO.getAllocatedNumber()) <= uplMaster.getImported() - uplMaster.getTotalAllocated()) {
								AllocationMasterDTO allocationMasterDTO = new AllocationMasterDTO();
								try {
									allocationMasterDTO = AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationMaster(uplMaster, aCustomerDTO, currentUser.getLoginId());
									if (allocationMasterDTO != null) {
										
										System.out.println("*** allocationMasterDTO.id: " + allocationMasterDTO.getId());
										
										AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationDetail(currentUser.getEmpId(), aCustomerDTO, allocationMasterDTO.getId(),uplMaster.getOwnerId(), uplMaster.getFromSource(), isAsm, xsm);
										
										if(aCustomerDTO.getAllocatedType().equals(AllocatedToTag.SUPERVISOR.stringValue())){
											AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).updateUplMaterForSupervisor( uplMaster, Integer.parseInt(aCustomerDTO.getAllocatedNumber()),aCustomerDTO.getUplMasterId().longValue());	
										}
										
									}else
										System.out.println("*** allocationMasterDTO is null");
									
								} catch (Exception e) {
									e.printStackTrace();
									ltsError.add(aCustomerDTO.getUplMasterId());
								}
							} else {
								ltsError.add(aCustomerDTO.getUplMasterId());
							}
							
							
						}
						
						if (AllocatedToTag.TEAM.stringValue().equals(aCustomerDTO.getAllocatedType())) {
							try {
								AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationUser(aCustomerDTO, currentUser.getId());
							} catch (Exception e) {
								ltsError.add(aCustomerDTO.getUplMasterId());
								e.printStackTrace();
							}
						}						
					} else {
						throw new Exception(Messages.getString("validation.field.notcorect", Labels.getString("label.common.telesale.uplMasterId")) + " uplMasterId : " + aCustomerDTO.uplMasterId);
					}
				}catch(Exception ex){
					ltsError.add(aCustomerDTO.getUplMasterId());
					ex.printStackTrace();
				}			
			}
			errorDto.setErrorIds(ltsError);
			
			System.out.println(" [END] List<AllocationCustomerDTO>  -----------> " + JSONConverter.toJSON(request));
			
			return errorDto;
        });
	}
	
	/*public ErrorAllocateDTO allocationCustomer(List<AllocationCustomerDTO> request, UserDTO currentUser, String isAsm, String xsm) throws Exception {
		
		try {
		    
		    System.out.println(" [BEGIN] List<AllocationCustomerDTO>  -----------> " + JSONConverter.toJSON(request));
		    
			TelesalesFactory.getSupervisorValidation().validateSupervisor(request);
			
			ErrorAllocateDTO errorDto = new ErrorAllocateDTO();
			List<Long> ltsError = new ArrayList<>();
			
			//AllocationFactory.getSupervisorValidation();
			
			List<Integer> listItemDuplicated = checkDuplicateMterId(request);
			if(listItemDuplicated.size() > 0 )
				throw new ValidationException(Messages.getString("validation.field.duplicate"," uplMasterId " ) +   " : " + JSONConverter.toJSON(listItemDuplicated));	
			
			for (AllocationCustomerDTO aCustomerDTO : request) {
							
				if (aCustomerDTO.getAllocatedNumber() == null || !StringUtils.isNumeric(aCustomerDTO.getAllocatedNumber()) || Integer.valueOf(aCustomerDTO.getAllocatedNumber())  <=  0 )
					throw new Exception(" S\u00F4 l\u01B0\u1EE3ng ph\u00E2n b\u1ED5 kh\u00F4ng h\u1EE3p l\u1EC7 :  " +  aCustomerDTO.getAllocatedNumber());
				
				try{
					//return this.tryCath(uok, ()->{
						if( !uok.isActive() )
							uok = new UnitOfWork();
						
						uok.start();
						
						UplMaster uplMaster = TelesalesFactory.getUploadAggregateInstance(uok.telesale).findUplMasterbyID(aCustomerDTO.getUplMasterId().longValue());
						if (uplMaster != null) {
							if (AllocatedToTag.SUPERVISOR.stringValue().equals(aCustomerDTO.getAllocatedType())) {
				
								if (Integer.parseInt(aCustomerDTO.getAllocatedNumber()) <= uplMaster.getImported() - uplMaster.getTotalAllocated()) {
									AllocationMasterDTO allocationMasterDTO = new AllocationMasterDTO();
									try {
										allocationMasterDTO = AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationMaster(uplMaster, aCustomerDTO, currentUser.getLoginId());
										if (allocationMasterDTO != null) {
											
											System.out.println("*** allocationMasterDTO.id: " + allocationMasterDTO.getId());
											
											AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationDetail(currentUser.getEmpId(), aCustomerDTO, allocationMasterDTO.getId(),uplMaster.getOwnerId(), uplMaster.getFromSource(), isAsm, xsm);
											
											if(aCustomerDTO.getAllocatedType().equals(AllocatedToTag.SUPERVISOR.stringValue())){
												AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).updateUplMaterForSupervisor( uplMaster, Integer.parseInt(aCustomerDTO.getAllocatedNumber()),aCustomerDTO.getUplMasterId().longValue());	
											}
											
											uok.commit();
										}else
											System.out.println("*** allocationMasterDTO is null");
										
									} catch (Exception e) {
										e.printStackTrace();
										uok.rollback();
										ltsError.add(aCustomerDTO.getUplMasterId());
										//throw e;
									}
								} else {
									ltsError.add(aCustomerDTO.getUplMasterId());
								}
								
								
							}
							
							if (AllocatedToTag.TEAM.stringValue().equals(aCustomerDTO.getAllocatedType())) {
								try {
									AllocationFactory.getSupervisorAgg(uok.telesale, uok.common).createAllocationUser(aCustomerDTO, currentUser.getId());
									uok.commit();
								} catch (Exception e) {
									uok.rollback();
									ltsError.add(aCustomerDTO.getUplMasterId());
									e.printStackTrace();
									//throw e;
								}
							}						
						} else {
							throw new Exception(Messages.getString("validation.field.notcorect", Labels.getString("label.common.telesale.uplMasterId")) + " uplMasterId : " + aCustomerDTO.uplMasterId);
						}
					//});
				}catch(Exception ex){
					ltsError.add(aCustomerDTO.getUplMasterId());
					uok.rollback();
					ex.printStackTrace();
				}			
			}
			errorDto.setErrorIds(ltsError);
			
			System.out.println(" [END] List<AllocationCustomerDTO>  -----------> " + JSONConverter.toJSON(request));
			
			return errorDto;
		} catch (Exception e) {
			this.uok.rollback();
			throw e;
		}
	}*/
	
	private List<Integer> checkDuplicateMterId(List<AllocationCustomerDTO> input){
		List<Integer> listItemDuplicated = new ArrayList<>() ;
		
		List<Integer> masterId = new ArrayList<>() ;
		
		for (AllocationCustomerDTO ojb : input) {
			masterId.add(ojb.getUplMasterId().intValue());
		}
		
		Set<Integer> appeared = new HashSet<>();
		  for (int item : masterId) {
		    if (!appeared.add(item)) {
		    	listItemDuplicated.add(item);
		    }
		  }
		
		return listItemDuplicated;
	}
	
	
	/*public static void main(String[] args) throws Exception {

		AllocationCustomerDTO allocationCustomerDTO = new AllocationCustomerDTO();
		List<AllocationCustomerDTO> request = new ArrayList<>();
		
		allocationCustomerDTO.setUplMasterId(62L);
		allocationCustomerDTO.setAllocatedNumber("1");
		allocationCustomerDTO.setAllocatedTo(13289L);
		allocationCustomerDTO.setAllocatedType("S");
		allocationCustomerDTO.setOwner(true);
		//allocationCustomerDTO.setRelatedAllocation(13289L);
		//allocationCustomerDTO.setObjectType("U");
		List<Long> ls = new ArrayList<Long>();
		ls.add( 232134L);
		ls.add(232135L);
		//allocationCustomerDTO.setObjectId(ls);
		
		request.add(allocationCustomerDTO);
		
		System.out.println(" -----------------------------------------");
		System.out.println(JSONConverter.toJSON(allocationCustomerDTO));
		System.out.println(" -----------------------------------------");
		
		SupervisorManager sp = new SupervisorManager();
		UserDTO userInfo = new UserDTO();
		userInfo.setLoginId("phaonx.ho");
		System.out.println(JSONConverter.toJSON(sp.allocationCustomer(request,userInfo,"ASM","XSM")));
		
		System.exit(0);
	}*/
}
