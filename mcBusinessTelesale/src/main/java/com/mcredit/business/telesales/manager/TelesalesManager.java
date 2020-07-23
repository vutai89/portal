package com.mcredit.business.telesales.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.business.telesales.aggregate.TeamAggregate;
import com.mcredit.business.telesales.dto.TeamDTO;
import com.mcredit.business.telesales.dto.UplCustomerHistoryDTO;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.business.telesales.payload.ScoringDTO;
import com.mcredit.business.telesales.payload.ScoringCRM;
import com.mcredit.business.telesales.payload.ScoringPayload;
import com.mcredit.business.telesales.payload.ScoringTS;
import com.mcredit.business.telesales.payload.SendMarkESB;
import com.mcredit.business.telesales.payload.SendOTP;
import com.mcredit.business.telesales.validation.TelesaleValidation;
import com.mcredit.common.Messages;
import com.mcredit.data.telesale.entity.Team;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.KioskDTO;
import com.mcredit.model.dto.ProductInfo;
import com.mcredit.model.dto.telesales.ActiveUserTsaDTO;
import com.mcredit.model.dto.telesales.AllocateDTO;
import com.mcredit.model.dto.telesales.TeamLeadDTO;
import com.mcredit.model.dto.telesales.UplCustomerScoringDTO;
import com.mcredit.model.dto.telesales.UplMasterDTO;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.factory.AllocationFactory;
import com.mcredit.sharedbiz.factory.UserFactory;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class TelesalesManager extends BaseManager{

	private static ModelMapper modelMapper = new ModelMapper();
	private TelesaleValidation _telesaleValidation;
	private UserDTO _user;
	public TelesalesManager(UserDTO user) {
		// TODO Auto-generated constructor stub
		this._telesaleValidation = new TelesaleValidation();
		_user = user;
	}
	
	public String getActiveCampaigns(String step, Long userId, String isAsm, String xsm, String ntb, String queryForProspectManagement, String tsaLst) throws Exception {
		
		return this.tryCatch(()->{
			
			ArrayList<UplMasterDTO> items = TelesalesFactory.getUploadAggregateInstance(uok.telesale).getActiveCampaigns(step.trim(), userId, isAsm
																						, xsm, ntb, "Y".equals(queryForProspectManagement)?true:false, tsaLst);
						
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});
		
	}
	
	/*public String getActiveTeams(String teamGroup, String isAsm) throws Exception {
		
		return this.tryCatch(()->{
			
			if(StringUtils.isNullOrEmpty(teamType))
				throw new ValidationException("Team Type khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng.");
			
			if(StringUtils.isNullOrEmpty(teamGroup))
				throw new ValidationException("Team Group khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng.");
			
			ArrayList<TeamDTO> items = TelesalesFactory.getTeamAggregateInstance(uok).getActiveTeam1(teamGroup, isAsm);
			
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});
	}*/
	
	public String getActiveTeams(String empId, String teamGroup, String isAsm) throws Exception {
		
		return this.tryCatch(()->{
			
			if( "".equals(empId) )
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.sup.missing.empId")));
			
			if( "".equals(teamGroup) )
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.missing.teamGroup")));
			
			ArrayList<TeamDTO> items = TelesalesFactory.getTeamAggregateInstance(uok).getActiveTeam1(empId, teamGroup, isAsm);
			
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});
	}
	
	public String getAllActiveTeams(String asm, String xsm, String ntb) throws Exception {
		
		return this.tryCatch(()->{
			
			ArrayList<TeamDTO> items = TelesalesFactory.getTeamAggregateInstance(uok).getAllActiveTeams(asm, xsm, ntb);
			
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});
	}
	
	public String getTeamMembers(Long userId, String type, String findType, String campaignType) throws Exception {
		
		return this.tryCatch(()->{
			
			if(userId == null)
				throw new ValidationException(Messages.getString("validation.field.madatory", "User ID"));
						
			if(StringUtils.isNullOrEmpty(type))
				throw new ValidationException(Messages.getString("validation.field.madatory", "Type"));
			
			if(!"owner".equalsIgnoreCase(type) && !"leader".equalsIgnoreCase(type))
				throw new ValidationException(Messages.getString("validation.field.invalidFormat", "Type"));
			
			ArrayList<UserDTO> items = TelesalesFactory.getTeamAggregateInstance(uok).getTeamMembers(userId, type, findType, campaignType);
			
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});	
	}
	
	public String getTeamMembersAllocatedNumber(Long memberId, String prospectStatus, Long campaignId, Long callStatus, Long callResult, String identityNumber, String receiveDate) throws Exception {
		
		return this.tryCatch(()->{
			
			if(memberId == null)
				throw new ValidationException(Messages.getString("validation.field.madatory", "member ID"));
						
			/*if(StringUtils.isNullOrEmpty(prospectStatus))
				throw new ValidationException(Messages.getString("validation.field.madatory", "Prospect Status"));
			
			if(campaignId == null)
				throw new ValidationException(Messages.getString("validation.field.madatory", "Campaign ID"));*/
			
			UserDTO items = TelesalesFactory.getTeamAggregateInstance(uok).getTeamMembersAlloctedNumber(memberId, prospectStatus, campaignId, callStatus, callResult, identityNumber, receiveDate);
			
			return items == null ? "[]" : JSONConverter.toJSON(items);
		});	
	}
	
	public UserDTO getTeamLeaderBy(Long memberId) throws Exception {
		return this.tryCatch(()->{
			
			if(memberId == null)
				throw new ValidationException(Messages.getString("validation.field.madatory", "Member ID"));
			
			TeamAggregate tAgg = TelesalesFactory.getTeamAggregateInstance(uok);
			UserDTO item = tAgg.getTeamLeaderBy(memberId);
			
			if(item == null){
				List<Team> team = tAgg.getTeamByTeamleadLoginID(memberId);
				if(team != null && team.size() > 0){
					Users u = UserFactory.getUserAgg(this.uok).findUserByUserId(memberId);
					if(u != null)
						item = modelMapper.map(u, UserDTO.class);
				}
			}
			
			if(item == null)
				throw new ValidationException(Messages.getString("validation.not.exists", "Team Leader"));
			
			return item;
		});	
	}
		
	public AllocateDTO queryUploadMaster(String uplCode, String uplType, String uplSequence, String uplOwner, String isAsm,String xsm) throws Exception {
		return this.tryCatch(()->{
			
			if (StringUtils.isNullOrEmpty(uplCode))
				throw new ValidationException(Messages.getString("validation.field.madatory", "Upl Code"));
			
			/*if (StringUtils.isNullOrEmpty(uplSource))
				throw new ValidationException("uplSource khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng.");*/			
			
			AllocateDTO queryUploadMaster = TelesalesFactory.getUploadAggregateInstance(uok.telesale).queryUploadMaster(uplCode, uplType, uplSequence, uplOwner, isAsm, xsm);
			
			if(queryUploadMaster == null)
				throw new ValidationException(Messages.getString("validation.not.exists", "Upload Master"));

			return queryUploadMaster;
		});

	}
	
	public Object queryAllocationDetail(String uplCode ,Long userId) throws Exception {
		return this.tryCatch(()->{
					
			if(StringUtils.isNullOrEmpty(uplCode))
			throw new ValidationException(Messages.getString("validation.field.madatory", "Upl Code"));
			
			List<Team> teams = new ArrayList<>() ;
			teams = TelesalesFactory.getTeamAggregateInstance(uok).getTeamByTeamleadLoginID(userId);
		
			List<AllocateDTO> list = new ArrayList<>() ;
					
			if(teams != null && teams.size() > 0) {
				
				for (Team t : teams) {
					
					AllocateDTO queryAllocationDetail = AllocationFactory.getSupervisorAgg(uok.telesale,uok.common).queryAllocationDetail(uplCode, t.getId());
					
					if(queryAllocationDetail == null)
						throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.campaign.invalid.for.team")));
					
					if( queryAllocationDetail.getAllocationMasterId() != null && queryAllocationDetail.getImported() != null )
						list.add(queryAllocationDetail);
				}
			}else
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.not.teamLeader")));
			
			return list;
		});
		
	}
	
	public Object checkIsTeamlead(Long userId)  throws Exception {
		return this.tryCatch(()->{
			TeamLeadDTO result = new TeamLeadDTO();
			List<Team> team = TelesalesFactory.getTeamAggregateInstance(uok).getTeamByTeamleadLoginID(userId);	
			
			if(team != null && team.size() > 0)
				result.setTeamLead(true); 
			return result ;
		});
	}

	public Object lstUplCusByTSACode(String tsaCode, String identityNumber, String mobile, 
			String sendDateFrom, String sendDateTo, String loginId) throws Exception {
		return this.tryCatch(()->{ 
			List<UplCustomerScoringDTO> team = TelesalesFactory.getTeamAggregateInstance(uok).lstUplCusByTSACode(tsaCode, identityNumber, mobile, sendDateFrom, sendDateTo, loginId);
			return team ;
		});
	}
	
	public Object findCustomerSupervisor(String tsaCode, String identityNumber, String mobile,
			String sendDateFrom, String sendDateTo, String loginId) throws Exception {
		return this.tryCatch(()->{ 
			List<UplCustomerScoringDTO> team = TelesalesFactory.getTeamAggregateInstance(uok).findCustomerSupervisor(tsaCode, identityNumber, mobile, sendDateFrom, sendDateTo, loginId);
			return team ;
		});
	}
	

	/**
	 * @deprecated no longer used it
	 */
	public Object sendOtp(SendOTP object) throws Exception {
		this._telesaleValidation.validateSendOTP(object);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).sendOtp(object);	
		});
	}
	
	/**
	 * @deprecated no longer used it
	 */
	public Object scoringTs(ScoringPayload scoringPayload) throws Exception {
		this._telesaleValidation.validateSendESB(scoringPayload);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).scoringTs(scoringPayload);	
		});
	}
	
	// RQ1011, 1013, 1015
	public Object sendOtpV2(SendOTP object) throws Exception {
		this._telesaleValidation.validateSendOTP(object);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).sendOtpV2(object, this._user.getId());
		});
	}
	
	public Object scoringTsV2(ScoringDTO scoringDTO) throws Exception {
		this._telesaleValidation.validateScoringCRM(scoringDTO);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).scoringTsV2(scoringDTO, this._user.getId());
		});
	}
	
	public Object sendOtpBPM(SendOTP object) throws Exception {
		this._telesaleValidation.validateSendOTP(object);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).sendOtpForBPM(object);	
		});
	}
	
	public Object scoringBPM(ScoringDTO scoringDTO) throws Exception {
		this._telesaleValidation.validateScoringBPM(scoringDTO);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).scoringBPM(scoringDTO);
		});
	}
	// RQ1011, 1013, 1015 END
	
	// RQ1015
	public Object getUplCustForScoring (Long upl_cust_id) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getUplCustForScoring(upl_cust_id);
		});
	}
	
	public Object getProdByScore(String partner, String telcoCode, Integer score) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getProdByScore(partner, telcoCode, score);
		});
	}
	
	public Object rejectScoringCustomer(String identityNumber, String phoneNumber) throws Exception {
		// Validate
		if(StringUtils.isNullOrEmpty(identityNumber)) {
			throw new ValidationException("Thieu CMND");
		}
		if(StringUtils.isNullOrEmpty(phoneNumber)) {
			throw new ValidationException("Thieu so dien thoai");
		}
		
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).rejectScoringCustomer(identityNumber, phoneNumber);	
		});
	}
	// RQ1015 END
	
	public Object updateUplCus(ScoringTS object) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).updateUplCus(object);	
		});
	}
	
	public Object getCusHis(Long cusId, String refId) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getCusHis(cusId, refId);	
		});
	}
	
	public Object getUplCus(Long id) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getUplCus(id);	
		});
	}
	
	public Object checkCustomerNTB(Long idUplCus) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).checkCustomerNTB(idUplCus);	
		});
	}
	
	public Object checkSendOtp(Long idUplCus) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).checkSendOtp(idUplCus);	
		});
	}
	
	public Object scoringApi(String mobile, String identityNumber, String otpCode, String appNumber, String identityNumberOld) throws Exception{
		this._telesaleValidation.validateScoringTsApi(mobile, identityNumber, appNumber);
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).scoringApi(mobile, identityNumber, otpCode, appNumber, identityNumberOld);
		});
	}

	public Object getRoleCodeUserTls() throws Exception {
		// TODO Auto-generated method stub
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getRoleCodeUserTls(_user.getLoginId());	
		});
	}

	public Object getMemberNTB() throws Exception {
		// TODO Auto-generated method stub
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getMemberNTB();	
		});
	}
	
	public Object getMemberByTeamLead() throws Exception {
		// TODO Auto-generated method stub
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).getMemberByTeamLead(this._user.getLoginId());	
		});
	}
	
	public Object createCaseFromCRM(UploadCaseDTO newCase) throws Exception {
		// TODO Auto-generated method stub
		return this.tryCatch(()->{
			this._telesaleValidation.createCaseFromCRM(newCase, this._user);
			return TelesalesFactory.getTeamAggregateInstance(uok).createCaseFromCRM(newCase, this._user);	
		});
	}
	
	public List<KioskDTO> geKiosks() throws Exception {
		return this.tryCatch(() -> {
			return TelesalesFactory.getTeamAggregateInstance(uok).getKiosks();
		});
	}
	
	public ProductInfo getProductInfo(String productCode) throws Exception {
		return this.tryCatch(() -> {
			this._telesaleValidation.getProductInfo(productCode);
			return TelesalesFactory.getTeamAggregateInstance(uok).getProductInfo(productCode);
		});
	}
	
	// RQ968
	public Object getScoreLG(String mobilePhone1, String mobilePhone2, String productCode) throws Exception {
		return this.tryCatch(() -> {
			this._telesaleValidation.getScoreLG(mobilePhone1, mobilePhone2, productCode);
			return TelesalesFactory.getTeamAggregateInstance(uok).getScoreLG(mobilePhone1, mobilePhone2, productCode);
		});
	}
	
	public ApiResult inactiveUserTsa(ActiveUserTsaDTO payload) throws Exception{
		return this.tryCatch(() -> {
			return TelesalesFactory.getTeamAggregateInstance(uok).inactiveUserTsa(payload.getUserId());
		});
	}
	
	public ApiResult activeUserTsa(ActiveUserTsaDTO payload) throws Exception{
		return this.tryCatch(() -> {
			return TelesalesFactory.getTeamAggregateInstance(uok).activeUserTsa(payload.getUserId());
		});
	}
	
	//RQ1046
	public Object checkRenew(Long idUplCus) throws Exception {
		return this.tryCatch(()->{
			return TelesalesFactory.getTeamAggregateInstance(uok).checkRenew(idUplCus);	
		});
	}
	
	
}