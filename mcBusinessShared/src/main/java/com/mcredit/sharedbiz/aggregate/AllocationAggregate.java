package com.mcredit.sharedbiz.aggregate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;

import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.AllocateHistory;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.telesale.entity.Team;
import com.mcredit.data.telesale.entity.TeamMember;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.telesales.AllocateDTO;
import com.mcredit.model.dto.telesales.AllocationCustomerDTO;
import com.mcredit.model.dto.telesales.AllocationMasterDTO;
import com.mcredit.model.enums.AllocatedToTag;
import com.mcredit.model.enums.TelesaleTag;
import com.mcredit.model.object.ListObjectResult;
import com.mcredit.model.object.warehouse.WHAllocationDocInput;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.TeamDTO;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class AllocationAggregate {

	protected UnitOfWorkCommon unitOfWorkcommon = null;
	protected UnitOfWorkTelesale unitOfWorkTelesale = null;
	protected UnitOfWorkCustomer unitOfWorkCustomer = null;
	protected static ModelMapper modelMapper = new ModelMapper();
	protected CodeTableCacheManager ctCache = CacheManager.CodeTable();
	
	CodeTableCacheManager cacheManager =  CodeTableCacheManager.getInstance() ;

	/**************** End Property ***************/
	
	public AllocationAggregate(UnitOfWorkTelesale unitOfWorkTelesale, UnitOfWorkCommon unitOfWorkcommon, UnitOfWorkCustomer unitOfWorkCustomer) {
		
		this.unitOfWorkTelesale = unitOfWorkTelesale;
		this.unitOfWorkcommon = unitOfWorkcommon ;
		this.unitOfWorkCustomer = unitOfWorkCustomer;
	}
	
	public AllocationAggregate(UnitOfWorkTelesale unitOfWorkTelesale, UnitOfWorkCustomer unitOfWorkCustomer) {
		
		this.unitOfWorkTelesale = unitOfWorkTelesale;
		this.unitOfWorkCustomer = unitOfWorkCustomer;
	}
	
	public AllocationAggregate(UnitOfWorkTelesale unitOfWorkTelesale, UnitOfWorkCommon unitOfWorkcommon) {
		
		this.unitOfWorkTelesale = unitOfWorkTelesale;
		this.unitOfWorkcommon = unitOfWorkcommon ;
	}
	
	public AllocationAggregate(UnitOfWorkTelesale unitOfWorkTelesale) {
		
		this.unitOfWorkTelesale = unitOfWorkTelesale;
	}

	private AllocationMasterDTO allocationMasterDTO;

	public AllocationMasterDTO getAllocationMasterDTO() {
		return allocationMasterDTO;
	}

	public void setAllocationMasterDTO(AllocationMasterDTO allocationMasterDTO) {
		this.allocationMasterDTO = allocationMasterDTO;
	}

	/****************
	 * Begin Behavior
	 * 
	 * @throws Exception
	 ***************/

	public AllocationMasterDTO createAllocationMaster(UplMaster uplMaster, AllocationCustomerDTO request, String user) throws Exception {
		AllocationMaster aMaster = new AllocationMaster();
		aMaster = modelMapper.map(request, AllocationMaster.class);
		
		UplMaster upMaster = unitOfWorkTelesale.uplMasterRepo().findUplMasterbyID(request.getUplMasterId());
		
		aMaster.setId(null);
		aMaster.setCreatedBy(user);
		aMaster.setRecordStatus("A"); /*TODO*/
		aMaster.setCreatedDate(new Date());/*TODO*/
		aMaster.setLastUpdatedDate(new Date());/*TODO*/
		aMaster.setRelatedAllocation(0);/*TODO*/
		List<AllocationMaster> aList = unitOfWorkcommon.allocationMasterRepo().findAllocationMasterByuplMasterIdAllocatedType(request.getUplMasterId(),request.getAllocatedType());
		// FOR OWNER Campains
			if (request.isOwner == true && CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "PC").getId().equals(upMaster.getUplType())) {
				aMaster.setAllocatedTo(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TLP", "OWNER_TEAMLEAD").getId().longValue());
			}
			
			else  if(request.isOwner == false && ( CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "GC").getId().equals(upMaster.getUplType()) ||
					CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "CX").getId().equals(upMaster.getUplType()))) {
				if(request.getObjectId() != null && request.getObjectId().size() > 0 ){
					aMaster.setAllocatedTo(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TLG", "ALL_TEAMLEAD_EXCEPT").getId().longValue());	
				} else {
					aMaster.setAllocatedTo(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TLG", "ALL_TEAMLEAD").getId().longValue());	
				}
			} else {
				throw new Exception(" Data not corect");
			}
		
			
		
		if (aList.size() > 0) {
			aMaster.setAllocatedSeq(unitOfWorkcommon.allocationMasterRepo().buidAllocatedSeq(request.getUplMasterId(),request.getAllocatedType()).intValue() + 1);
		} else {
			aMaster.setAllocatedSeq(1);
		}		
		unitOfWorkcommon.allocationMasterRepo().upsert(aMaster);
		setAllocationMasterDTO(modelMapper.map(aMaster, AllocationMasterDTO.class));
		
		/*if(request.getAllocatedType().equals(AllocatedToTag.SUPERVISOR.stringValue())){
			updateUplMaterForSupervisor( uplMaster, Integer.parseInt(request.getAllocatedNumber()),request.getUplMasterId().longValue());	
		}*/
			
		return allocationMasterDTO;
	}

	
	public List<AllocationDetail> createAllocationDetail(String empId, AllocationCustomerDTO request, Long allocationMasterId
															, Long onwerId, Long fromSource, String isAsm, String xsm) throws Exception {

		List<AllocationDetail> lAllocationDetails = new ArrayList<>();		
			
		Integer allocatedNumber = Integer.valueOf(request.getAllocatedNumber());

		CodeTableDTO ct = CodeTableCacheManager.getInstance().getbyID(fromSource.intValue());
		
		List<UplDetail> uplDetails = unitOfWorkTelesale.uplDetailRepo().findUPLDetailByUplMasterIdStatusVP(request.getUplMasterId(), StringUtils.nullToEmpty(ct.getCodeValue1()), isAsm, xsm);
		
		if (uplDetails == null || uplDetails.size() == 0)
			throw new Exception("  not UplDetail for  :  " + request.getUplMasterId());
				
		List<Long> teams = new ArrayList<>();	
		
		if (request.isOwner == true) {
			
			if (request.getAllocatedType().equals(AllocatedToTag.SUPERVISOR.stringValue())) {
				
				TeamMember tm = unitOfWorkTelesale.teamMemberRepo().findTeamMenberByTeamId(onwerId);
				
				if (null != tm) // if onwerId is manager of n team Equal to
					teams.add(tm.getTeamId());
				else {
					
					List<Team> ts = unitOfWorkTelesale.teamRepo().getTeamByTeamleadLoginID(onwerId);
					
					if(ts != null && ts.size() > 0) {
						for (Team team : ts) {
							teams.add(team.getId());
						}
					}
				}
			}
		}else {
			
			//List<TeamDTO> lts = getActiveTeam(empId, TeamTag.TEAM_GROUP_TELESALE.value(), isAsm);
			List<TeamDTO> lts = getAllActiveTeams("asm", "xsm", "ntb");
			
			if ( request.isSpecifyTeam ) { //Dich danh teamLead
				if (request.getObjectId() != null && request.getObjectId().size() > 0) {
					for (TeamDTO team : lts) {
						for (Long teamId : request.getObjectId()) {
							if (team.getId() == teamId)
								teams.add(teamId);
						}
					}
				}
			}else { //Tat ca teamLead va ngoai tru teamLead
				for (TeamDTO team : lts) {
					teams.add(team.getId());
				}
				
				if (request.getObjectId() != null && request.getObjectId().size() > 0) {
					for (TeamDTO team : lts) {
						for (Long teamId : request.getObjectId()) {
							if (team.getId() == teamId)
								teams.remove(teamId);
						}
					}
				}
			}
		}
				
		int totalTeam = 0;
		
		HashMap<Long, Integer> teamNumberAllocate = new HashMap<>();

		if (teams != null & teams.size() > 0)
			totalTeam = teams.size();
		else
			throw new Exception(" Team is null ");

		List<Integer> allocateNumbers = new ArrayList<>();
		int temAlocation = 0;
		for (UplDetail uplDetail : uplDetails) {
			if ((allocatedNumber - temAlocation) > (uplDetail.getImported() - uplDetail.getTotalAllocated())) {
				allocateNumbers.add(uplDetail.getImported() - uplDetail.getTotalAllocated());
				temAlocation = temAlocation + (uplDetail.getImported() - uplDetail.getTotalAllocated());
			} else {
				allocateNumbers.add((allocatedNumber - temAlocation));
				break;
			}
		}

		System.out.println(" allocateNumbers : " + JSONConverter.toJSON(allocateNumbers));

		int positionTeamBlance = 0;

		for (int i = 0; i < allocateNumbers.size(); i++) {
			System.out.println(" positionTeamBlance: " + positionTeamBlance);

			for (Long teamId : teams) {
				teamNumberAllocate.put(teamId, allocateNumbers.get(i) / totalTeam);
			}

			System.out.println(" teamNumberAllocate  0 : " + positionTeamBlance + JSONConverter.toJSON(teamNumberAllocate));

			int tmpBalance = allocateNumbers.get(i) % totalTeam;

			if (positionTeamBlance + tmpBalance <= totalTeam - 1) {
				for (int ia = 0; ia < tmpBalance; ia++) {
					teamNumberAllocate.put(teams.get(positionTeamBlance), teamNumberAllocate.get(teams.get(positionTeamBlance)) + 1);
					positionTeamBlance = positionTeamBlance + 1;
				}

				System.out.println(" teamNumberAllocate 1 : " + positionTeamBlance + JSONConverter.toJSON(teamNumberAllocate));

				if (positionTeamBlance == totalTeam - 1)
					positionTeamBlance = 0;

			} else {
				for (int ia = positionTeamBlance; ia < totalTeam; ia++) {
					teamNumberAllocate.put(teams.get(positionTeamBlance),
							teamNumberAllocate.get(teams.get(positionTeamBlance)) + 1);
					positionTeamBlance = positionTeamBlance + 1;
				}

				System.out.println(" teamNumberAllocate 1 : " + positionTeamBlance + JSONConverter.toJSON(teamNumberAllocate));

				if (positionTeamBlance == totalTeam - 1)
					positionTeamBlance = 0;

				for (int ib = tmpBalance - totalTeam; ib > 0; ib--) {
					teamNumberAllocate.put(teams.get(positionTeamBlance),
							teamNumberAllocate.get(teams.get(positionTeamBlance)) + 1);
					positionTeamBlance = positionTeamBlance + 1;
				}

				System.out.println(" teamNumberAllocate  2 : " + positionTeamBlance + JSONConverter.toJSON(teamNumberAllocate));

				if (positionTeamBlance == totalTeam - 1)
					positionTeamBlance = 0;
			}
			// update Upl_Master
			if(uplDetails.get(i).getImported() < uplDetails.get(i).getTotalAllocated() + allocateNumbers.get(i))
				throw new Exception(" loi qua trinh phan bo So luong da phan bo li loi > so luong import");				
			
			uplDetails.get(i).setTotalAllocated(uplDetails.get(i).getTotalAllocated() + allocateNumbers.get(i));
			
			if(uplDetails.get(i).getImported() - uplDetails.get(i).getTotalAllocated() == 0)
				uplDetails.get(i).setStatus(TelesaleTag.UPLDETAIL_STATUS_A.stringValue());
			
			if(uplDetails.get(i).getImported() - uplDetails.get(i).getTotalAllocated() > 0)
				uplDetails.get(i).setStatus(TelesaleTag.UPLDETAIL_STATUS_P.stringValue());
			
			// insertDB AllocationDetail
			for (Map.Entry<Long, Integer> entry : teamNumberAllocate.entrySet()) {
				
				if(entry.getValue()!= null && ( entry.getValue() > 0 )){
					AllocationDetail allocationDetail = new AllocationDetail();
					allocationDetail.setAllocatedNumber(entry.getValue());
					allocationDetail.setAllocationMasterId(allocationMasterId);
					allocationDetail.setObjectType("T");
					allocationDetail.setObjectId(entry.getKey());
					allocationDetail.setStatus(new Long(cacheManager.getCodeByCategoryCodeValue1(TelesaleTag.ALLOCATIONDETAIL_ALCTYPE_SP.stringValue(), TelesaleTag.UPLDETAIL_STATUS_N.stringValue()).getId()));
					allocationDetail.setUplCustomerId(null);
					allocationDetail.setUplDetailId(uplDetails.get(i).getId());

					unitOfWorkcommon.allocationDetailRepo().upsert(allocationDetail);

					lAllocationDetails.add(allocationDetail);
				}
			}
			//TODO: kiem tra lai ham nay
			unitOfWorkTelesale.uplDetailRepo().merge(uplDetails.get(i));

		}
		
		System.out.println("*** lAllocationDetails.size: " + lAllocationDetails.size());
		
		return lAllocationDetails;

	}
	
	public ArrayList<TeamDTO> getAllActiveTeams(String asm, String xsm, String ntb) {

		List<?> items = unitOfWorkTelesale.teamRepo().listAllActiveTeams(asm, xsm, ntb);
		
		ArrayList<TeamDTO> list = new ArrayList<>();
		
		if (items != null && items.size() > 0) {
			
			for (Object obj : items) {
				
				if( obj!=null ) {
					Object[] o = (Object[]) obj;
					TeamDTO teams = new TeamDTO() ;
					teams.setId(((BigDecimal) o[0]).longValue());
					teams.setRecordStatus((String )o[1]);
					teams.setCreatedDate((Date)o[2]);
					teams.setLastUpdatedDate((Date)o[3]);
					teams.setCreatedBy((String) o[4]);
					teams.setLastUpdatedBy((String) o[5]);
					teams.setManagerId(((BigDecimal) o[6]).longValue());
					teams.setTeamType((String) o[7]);
					teams.setTeamGroup((String) o[8]);
					teams.setTeamCode((String) o[9]);
					teams.setTeamName((String) o[10]);
					teams.setStatus((String) o[11]);
					teams.setTeamLeadName((String) o[12]);
					list.add(teams);
					
				}
			}
		}
		
		return list;
	}
	
	public List<Long> createAllocationUser(AllocationCustomerDTO request , Long currentUserId) throws Exception {
		List<Long> result = new ArrayList<>();
		
		List<Team> teams = unitOfWorkTelesale.teamRepo().getTeamByTeamleadLoginID(currentUserId);
		
		if (teams == null || teams.size() < 1  )
			throw new Exception(" B\u1EA1n kh\u00F4ng ph\u1EA3i team lead ");
		
		for (Team team : teams) {
			String listMember = "" ;
			Integer  alocateTo = null ;
			
			UplMaster upMaster = unitOfWorkcommon.allocationMasterRepo().queryAllocationDetail( null , team.getId() , "allocateTeam" , request.getUplMasterId());
			
			if( upMaster.getCase_received()==null || upMaster.getAllocationMasterId()==null )
				continue;
			
			Integer tatalUnallocated = upMaster.getCase_received() - (upMaster.getTotal_allocated() != null ? upMaster.getTotal_allocated() : 0);
			
			if(Integer.valueOf(request.getAllocatedNumber()) > tatalUnallocated.intValue())
				throw new Exception(" S\u1ED1 l\u01B0\u1EE3ng c\u1EA7n ph\u00E2n b\u1ED5 kh\u00F4ng ch\u00EDnh s\u00E1c  : AllocatedNumber = " + request.getAllocatedNumber() + " >  TotalUnallocated = "  + upMaster.getTotalUnallocated() );
		
			if (request.isOwner == true) {
				if(team.getManagerId() == upMaster.getOwnerId()) //for exception allocate for owner when owner campain is team lead
					throw new Exception( " Kh\u00F4ng chia ng\u01B0\u1EDDi s\u1EDF h\u1EEFu l\u00E0 ch\u00EDnh teamLead ");
				
				if (CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "PC").getId().equals(upMaster.getUplType()) ) {
					if( request.isSpecifyMembers ) //Dich danh team member
						alocateTo = CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM").getId();
					else
						alocateTo = CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMP", "OWNER").getId();
				}else
					throw new Exception( "Campaign  chung kh\u00F4ng th\u1EC3 chia cho ng\u01B0\u1EDDi s\u1EDF h\u1EEFu");
				
			} else {
				if(upMaster.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "PC").getId())){
					if(request.getObjectId() != null && request.getObjectId().size() > 0 ) {
						
						if( request.isSpecifyMembers ) //Dich danh team member
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM").getId();
						else
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMP", "ALL_TEAMMEM_EXCEPT").getId();
						
						listMember = JSONConverter.toJSON(request.getObjectId());
					}else {
						if( request.isSpecifyMembers ) //Dich danh team member
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM").getId();
						else
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMP", "ALL_TEAMMEM").getId();
					}
				}
				if(upMaster.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "GC").getId()) 
						|| upMaster.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "CX").getId())){
					if(request.getObjectId() != null && request.getObjectId().size() > 0 ) {
						
						if( request.isSpecifyMembers ) //Dich danh team member
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM").getId();
						else
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "ALL_TEAMMEM_EXCEPT").getId();
						
						listMember = JSONConverter.toJSON(request.getObjectId());
						
					}else {
						if( request.isSpecifyMembers ) //Dich danh team member
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "INCLUDE_TEAMMEM").getId();
						else
							alocateTo =  CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALC2TMG", "ALL_TEAMMEM").getId();
					}
				}
			}
		
			System.out.println(request.getUplMasterId() + " : " + Integer.valueOf(request.getAllocatedNumber())  + " : " +  alocateTo  + " : " +  team.getId()  + " : " +  listMember);
			unitOfWorkcommon.allocationDetailRepo().updateAllocationDetaiByStore(request.getUplMasterId(),Integer.valueOf(request.getAllocatedNumber()),alocateTo , team.getId(), listMember);
			
		}			
		
		return result ;
	}
	
	public void createAllocationDetailOld(AllocationCustomerDTO request,Long userId) throws Exception {
		
		
		Integer allocNumber = request.getAllocatedNumber()!= null ? Integer.valueOf(request.getAllocatedNumber()) : 0 ;
		List<Long>  lstUserId = new ArrayList<>() ;
		
		if(request.isOwner == true){
			lstUserId.add(unitOfWorkTelesale.uplMasterRepo().findUplMasterbyID(request.getUplMasterId()).getOwnerId());
		}else {
			 lstUserId = request.getObjectId();
		}
		
		
		//Select Ban gi da duoc phan bo cho team
		List<AllocationDetail> lsDetails = new ArrayList<>();
		
		if(request.getRelatedAllocation() != null  && request.getRelatedAllocation() > 0){
			List<UplCustomer> uplCusts = unitOfWorkTelesale.uplCustomerRepo().getListCustumerTeamLeadId(request.getRelatedAllocation(),userId);
			
			
			if(uplCusts != null  && uplCusts.size() > 0) {
				if(Integer.valueOf(request.getAllocatedNumber()) > uplCusts.size())
					throw new Exception(" S\u1ED1 l\u01B0\u1EE3ng c\u1EA7n ph\u00E2n b\u1ED5 l\u1EDBn h\u01A1n s\u1ED1 l\u01B0\u1EE3ng c\u00F3 s\u1EB5n.  ");
					
				for(int i = 0 ; i < allocNumber ; i ++ ) {
					AllocationDetail aDetail = new AllocationDetail();
					aDetail.setAllocatedNumber(1);
					aDetail.setAllocationMasterId(request.getRelatedAllocation());
					aDetail.setObjectType("U");
					aDetail.setStatus(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("ALCTYPE_SP", "New").getId().longValue());
					aDetail.setUplCustomerId(uplCusts.get(i).getId());
					aDetail.setUplDetailId(uplCusts.get(i).getUplDetailId());
					aDetail.setUplCustomerId(0L);/*TODO*/
					if(i < lstUserId.size() ){
						aDetail.setObjectId(lstUserId.get(i));
					}else {
						// user thu i  = i%lstUserId.size()
						aDetail.setObjectId(lstUserId.get(i%lstUserId.size()));
					}
					
					unitOfWorkcommon.allocationDetailRepo().upsert(aDetail);					
					lsDetails.add(aDetail);
					
				}
				
			}else {
				throw new Exception("not Custumer with campain for Team ");
			}
			
		}else
			throw new Exception("RelatedAllocation is null");
	}	
	
	public Long updateUplMaterForSupervisor(UplMaster uplMaster, Integer allcatenumber , Long UplMaterid){
		
		if(uplMaster != null ){
			uplMaster.setTotalAllocated((uplMaster.getTotalAllocated() != null ? uplMaster.getTotalAllocated() : 0) + allcatenumber) ;
			unitOfWorkTelesale.uplMasterRepo().upsert(uplMaster);
		}
		
		System.out.println("*** uplMaster.getId(): " + uplMaster.getId() + " - uplMaster.getTotalAllocated: " + uplMaster.getTotalAllocated());
		
		return uplMaster.getId();
		
	}

	public Long updateuplDetailSupervisor( Integer allcatenumber , Long UplMaterid){
		UplDetail  item = unitOfWorkTelesale.uplDetailRepo().findUplDetailbyID(UplMaterid);
		
		if(item != null ){
			item.setTotalAllocated((item.getTotalAllocated() != null ? item.getTotalAllocated() : 0) + allcatenumber);
					 
			 if(item.getImported() - item.getTotalAllocated() == 0)
				 item.setStatus(TelesaleTag.UPLDETAIL_STATUS_A.stringValue());  
			 else if(item.getTotalAllocated() == 0)
				 item.setStatus(TelesaleTag.UPLDETAIL_STATUS_P.stringValue()); 
			 					 
			 unitOfWorkTelesale.uplDetailRepo().upsert(item);
		}
		
		return item.getId();
	}
	
	public AllocationMaster getAllocationMasterByUplMasterIdTeamId(Long uplMasterId, String allocatedType) {		
		return unitOfWorkcommon.allocationDetailRepo().getAllocationDetailByUplMsterIdTeamId(uplMasterId,allocatedType);
	}
	
	public AllocateDTO queryAllocationDetail(String uplCode, Long teamId) {
		
		UplMaster lts = unitOfWorkcommon.allocationMasterRepo().queryAllocationDetail(uplCode, teamId , null , null);
		
		AllocateDTO out = new AllocateDTO();
		if(lts != null ){
			
			if(lts.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "GC").getId())){
				lts.setOwnerTeamLead(null);
				lts.setOwnerId(null);
				lts.setOwnerTeamLead(null);
				lts.setOwnerName(null);
			}
			if(lts.getUplType().equals(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("UPL_TYPE", "PC").getId())){//for show teamlead name when teamlead is owner
				if(lts.getOwnerName().trim() != null && StringUtils.isNullOrEmpty(lts.getOwnerTeamLead()))
					lts.setOwnerTeamLead(lts.getOwnerName());
			}
			
			out = modelMapper.map(lts, AllocateDTO.class) ;
			out.setImported(lts.getCase_received());
			// work around for allException TotalUnallocated < 0 relate = 0 ;
			
			if( out != null  && out.getImported() !=null && lts !=null && lts.getTotal_allocated()!=null )
			    out.setTotalUnallocated((out.getImported() - (lts.getTotal_allocated() != null ? lts.getTotal_allocated() : 0)) > 0 ? (out.getImported() - (lts.getTotal_allocated() != null ? lts.getTotal_allocated() : 0)) : 0 );
		    else
			    out.setTotalUnallocated(0);
			
			return out;
		}
		
				
		return null;
	}

	public List<TeamDTO> getActiveTeam(String empId, String teamGroup, String isAsm) {

		List<?> items = unitOfWorkTelesale.teamRepo().listTeams(empId, teamGroup, isAsm);
		
		List<TeamDTO> list = new ArrayList<>();
		
		if (items != null && items.size() > 0) {
			
			for (Object obj : items) {
				
				if( obj!=null ) {
					Object[] o = (Object[]) obj;
					TeamDTO teams = new TeamDTO() ;
					teams.setId(((BigDecimal) o[0]).longValue());
					teams.setRecordStatus((String )o[1]);
					teams.setCreatedDate((Date)o[2]);
					teams.setLastUpdatedDate((Date)o[3]);
					teams.setCreatedBy((String) o[4]);
					teams.setLastUpdatedBy((String) o[5]);
					teams.setManagerId(((BigDecimal) o[6]).longValue());
					teams.setTeamType((String) o[7]);
					teams.setTeamGroup((String) o[8]);
					teams.setTeamCode((String) o[9]);
					teams.setTeamName((String) o[10]);
					teams.setStatus((String) o[11]);
					teams.setTeamLeadName((String) o[12]);
					list.add(teams);
					
				}
			}
		}
		
		return list;
	}
	
	public List<TeamDTO> getActiveTeam(String teamGroup, String isAsm) {

		List<?> items = unitOfWorkTelesale.teamRepo().listTeams(teamGroup, isAsm);
		
		List<TeamDTO> list = new ArrayList<>();
		
		if (items != null && items.size() > 0) {
			
			for (Object obj : items) {
				
				if( obj!=null ) {
					Object[] o = (Object[]) obj;
					TeamDTO teams = new TeamDTO() ;
					teams.setId(((BigDecimal) o[0]).longValue());
					teams.setRecordStatus((String )o[1]);
					teams.setCreatedDate((Date)o[2]);
					teams.setLastUpdatedDate((Date)o[3]);
					teams.setCreatedBy((String) o[4]);
					teams.setLastUpdatedBy((String) o[5]);
					teams.setManagerId(((BigDecimal) o[6]).longValue());
					teams.setTeamType((String) o[7]);
					teams.setTeamGroup((String) o[8]);
					teams.setTeamCode((String) o[9]);
					teams.setTeamName((String) o[10]);
					teams.setStatus((String) o[11]);
					teams.setTeamLeadName((String) o[12]);
					list.add(teams);
					
				}
			}
		}
		
		return list;
	}
	
	public ListObjectResult wHAllocationAssign(WHAllocationDocInput allocationDocumnetInputs,String loginId) throws Exception {
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();
		Long statusAssign = Long.valueOf(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("WH_ASS_TYPE", "WH_ONE").getId());
		Long allocatedTo = Long.valueOf(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("EM_POS_AL", "WH_ALLOC").getId()); 
		List<Object> uploadConfig =  unitOfWorkcommon.allocationMasterRepo().getuploadConfig();
		Long uplmaterId = Long.valueOf(uploadConfig.size() > 1 ? uploadConfig.get(0).toString().trim() : "0");
		
		List<AllocationMaster> aList = unitOfWorkcommon.allocationMasterRepo().findAllocationMasterByuplMasterIdAllocatedType(uplmaterId,"U");
		
		System.out.println(JSONConverter.toJSON(uploadConfig));
		try{
			
			AllocationMaster allocationMaster = new AllocationMaster();
			if (aList.size() > 0) {
				allocationMaster.setAllocatedSeq(unitOfWorkcommon.allocationMasterRepo().buidAllocatedSeq(uplmaterId,"U").intValue() + 1);
			} else { allocationMaster.setAllocatedSeq(1); }	
			allocationMaster.setAllocatedNumber(allocationDocumnetInputs.getLstwhDocId().size());
			allocationMaster.setUplMasterId(Long.valueOf(uploadConfig.size() == 2 ? uploadConfig.get(0).toString().trim() : "0"));
			allocationMaster.setAllocatedTo(allocatedTo); 
			allocationMaster.setAllocatedType("U");
			allocationMaster.setRelatedAllocation(0);
			
			allocationMaster.setCreatedBy(loginId);
			allocationMaster.setCreatedDate(new Date());
			
			allocationMaster.setLastUpdatedBy(loginId);
			allocationMaster.setLastUpdatedDate(new Date());
			
			allocationMaster.setRecordStatus("A");		
			
			 unitOfWorkcommon.allocationMasterRepo().upsert(allocationMaster);
			 
			 for (Long whId  : allocationDocumnetInputs.getLstwhDocId()) {
				 if (whId != null) {
					 try{
						 AllocationDetail allocationDetail = new AllocationDetail() ;
						 allocationDetail.setUplDetailId(Long.valueOf(uploadConfig.size()  == 2  ? uploadConfig.get(1).toString().trim(): "0"));
						 allocationDetail.setAllocatedNumber(1);
						 allocationDetail.setAllocationMasterId(allocationMaster.getId());
						 allocationDetail.setObjectId(allocationDocumnetInputs.getUserId());
						 allocationDetail.setObjectType("U");
						 allocationDetail.setStatus(statusAssign);
						 allocationDetail.setUplCustomerId(whId);
						 
						 allocationDetail.setCreatedBy(loginId);
						 allocationDetail.setCreatedDate(new Date());
						 
						 allocationDetail.setLastUpdatedBy(loginId);
						 allocationDetail.setLastUpdatedDate(new Date());
						 
						 unitOfWorkcommon.allocationDetailRepo().upsert(allocationDetail); 
						 
						 AllocateHistory allocateHistory = new AllocateHistory();
						 allocateHistory.setAllocationDetailId(allocationDetail.getId());
						 allocateHistory.setCreatedBy(loginId);
						 allocateHistory.setCreatedDate(new Date());
						 allocateHistory.setObjectId(allocationDocumnetInputs.getUserId());
						 allocateHistory.setStatus(statusAssign);
						 allocateHistory.setNote(allocationDocumnetInputs.getReason());
						 
						 unitOfWorkcommon.allocateHistoryRepo().upsert(allocateHistory); 
						 
						 sucsessUpdate.add(whId);

					}catch (Exception e) {
						errorUpdate.add(whId);
                        e.printStackTrace();
					}
				 }
			}
			 
		}catch (Exception e) {
			throw new Exception(" L\u1ED7i trong qu\u00E1 tr\u00ECnh ph\u00E2n b\u1ED5.  ");
		}
		
		return  new ListObjectResult(errorUpdate, sucsessUpdate);
	}
	
	public ListObjectResult wHAllocationReAssign(WHAllocationDocInput allocationDocumnetInputs, String loginId) {
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();	
		Long statusReAssign = Long.valueOf(CodeTableCacheManager.getInstance().getCodeByCategoryCodeValue1("WH_ASS_TYPE", "WH_MANY").getId());
		 for (Long whId  : allocationDocumnetInputs.getLstwhDocId()) {
			 try{
				 AllocationDetail allocationDetail = unitOfWorkcommon.allocationDetailRepo().findByuplCustomerId(whId);
				 
				 allocationDetail.setObjectId(allocationDocumnetInputs.getUserId());
				 allocationDetail.setStatus(statusReAssign);// chua ro update status
				 allocationDetail.setNote(allocationDocumnetInputs.getReason());	
				 allocationDetail.setLastUpdatedBy(loginId);
				 allocationDetail.setLastUpdatedDate(new Date());
				 AllocateHistory allocateHistory = new AllocateHistory();
				 
				 allocateHistory.setAllocationDetailId(allocationDetail.getId());
				 allocateHistory.setCreatedBy(loginId);
				 allocateHistory.setCreatedDate(new Date());
				 allocateHistory.setObjectId(allocationDocumnetInputs.getUserId());
				 allocateHistory.setStatus(statusReAssign);
				 allocateHistory.setNote(allocationDocumnetInputs.getReason());
				 
				 unitOfWorkcommon.allocationDetailRepo().upsert(allocationDetail); 
				 unitOfWorkcommon.allocateHistoryRepo().upsert(allocateHistory); 
				 
				 sucsessUpdate.add(allocationDocumnetInputs.getUserId());
			}catch (Exception e) {
				errorUpdate.add(allocationDocumnetInputs.getUserId());
			}
		}
		return  new ListObjectResult(errorUpdate, sucsessUpdate);
	}

	public List<AllocationDetail> validateUplDetailWH(List<Long> lstwhDocId) {
		return  unitOfWorkcommon.allocationDetailRepo().getListWhId(lstwhDocId);
	}

	public Integer getWhUplDetailId() {
		return unitOfWorkcommon.allocationDetailRepo().getWhUplDetailId();
	}

	/****************
	 * End Behavior
	 * 
	 * @throws Exception
	 ***************/
}
