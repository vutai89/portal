package com.mcredit.business.telesales.aggregate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.telesales.converter.Converter;
import com.mcredit.business.telesales.dto.TeamDTO;
import com.mcredit.business.telesales.dto.UplCustomerHistoryDTO;
import com.mcredit.business.telesales.object.BPMOTPResponse;
import com.mcredit.business.telesales.object.BPMScoringObject;
import com.mcredit.business.telesales.object.BPMScoringResponse;
import com.mcredit.business.telesales.object.CICDataResponseDTO;
import com.mcredit.business.telesales.object.SaveOTPObject;
import com.mcredit.business.telesales.object.SaveScoreObject;
import com.mcredit.business.telesales.object.ScoreLGResponse;
import com.mcredit.business.telesales.object.ScoringResponse;
import com.mcredit.business.telesales.object.ScoringTsResponse;
import com.mcredit.business.telesales.object.ScoringTsResponseFinal;
import com.mcredit.business.telesales.object.TSResponseDTO;
import com.mcredit.business.telesales.object.TokenResponse;
import com.mcredit.business.telesales.payload.ScoringDTO;
import com.mcredit.business.telesales.payload.ScoringPayload;
import com.mcredit.business.telesales.payload.ScoringTS;
import com.mcredit.business.telesales.payload.SendMarkESB;
import com.mcredit.business.telesales.payload.SendMarkTS;
import com.mcredit.business.telesales.payload.SendOTP;
import com.mcredit.business.telesales.util.ConstantTelesale;
import com.mcredit.business.telesales.util.EsbApi;
import com.mcredit.business.telesales.util.JSONFactory;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.telesale.entity.CustProspect;
import com.mcredit.data.telesale.entity.Team;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.KioskDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.ProductInfo;
import com.mcredit.model.dto.ProductInterestDTO;
import com.mcredit.model.dto.telesales.ScoreLGResult;
import com.mcredit.model.dto.telesales.UplCustomerScoring2DTO;
import com.mcredit.model.dto.telesales.UplCustomerScoringDTO;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.VendorCodeEnum;
import com.mcredit.model.object.RuleResult;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class TeamAggregate {

	/**************** Begin Property ***************/
	
//	private UnitOfWorkTelesale unitOfWorkTelesale = null;
	private static ModelMapper modelMapper = new ModelMapper();
	private CodeTableCacheManager ctCache = null;
	private ProductCacheManager productCache = null;
	private UnitOfWork _uok;
	
	public static final String STATUS_CODE_OK = "200";
	public static final String INSTALLMENT_LOAN = "InstallmentLoan";
	public static final String CASH_LOAN = "CashLoan";
	
	public static final int STATUS_ACTIVE_USER_TSA_SUCCESS = 200;
	public static final int STATUS_USER_TSA_HAS_DATA_XCELL = 422;
	private ParametersCacheManager _cParam = CacheManager.Parameters();
	private String _esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	private final String BEARER_TOKEN_TYPE = "Bearer";
	private final String TOKEN_FOR_TSAPI_REDIS_KEY = "TOKEN_FOR_TSAPI_REDIS_KEY";
	private final String TOKEN_FOR_CICDATAAPI_REDIS_KEY = "TOKEN_FOR_CICDATAAPI_REDIS_KEY";
	private RedisAggregate redisAgg = new RedisAggregate();
	private static SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**************** End Property ***************/

	/****************
	 * Begin Constructor
	 ***************/
	public TeamAggregate(UnitOfWork uok) {

		this._uok = uok;
		ctCache = CacheManager.CodeTable();
		productCache = ProductCacheManager.getInstance();
	}

	/*public ArrayList<TeamDTO> getActiveTeams(String teamType,String teamGroup) {

		List<Team> items = unitOfWorkTelesale.teamRepo().listTeam(teamType, teamGroup);
		ArrayList<TeamDTO> list = null;
		if (items != null && items.size() > 0) {
			list = new ArrayList<TeamDTO>();
			for (Team item : items) {
				list.add(modelMapper.map(item, TeamDTO.class));
			}
		}
		
		return list;
	}*/
	
	/*public ArrayList<TeamDTO> getActiveTeam1(String teamGroup, String isAsm) {

		List<?> items = unitOfWorkTelesale.teamRepo().listTeams(teamGroup, isAsm);
		
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
	}*/
	
	public ArrayList<TeamDTO> getAllActiveTeams(String asm, String xsm, String ntb) {

		List<?> items = _uok.telesale.teamRepo().listAllActiveTeams(asm, xsm, ntb);
		
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
	
	public ArrayList<TeamDTO> getActiveTeam1(String empId, String teamGroup, String isAsm) {

		List<?> items = _uok.telesale.teamRepo().listTeams(empId, teamGroup, isAsm);
		
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
	
	public UserDTO getTeamMembersAlloctedNumber(Long memberId, String prospectStatus, Long campaignId, Long callStatus, Long callResult, String identityNumber, String receiveDate) {
		
		Users items = _uok.telesale.teamMemberRepo().findTeamMemberAllocatedNumber(memberId, prospectStatus, campaignId, callStatus, callResult, identityNumber, receiveDate);
		
		if (items != null)
			return modelMapper.map(items, UserDTO.class);
		
		return new UserDTO();
	}
	
	public ArrayList<UserDTO> getTeamMembers(Long userId,String ownerOrLeader, String findType, String campaignType) {
		List<Users> items = null;
		
		if("owner".equalsIgnoreCase(ownerOrLeader))
			items = _uok.telesale.teamMemberRepo().findTeamMemberByOnwerId(userId);
		
		if("leader".equalsIgnoreCase(ownerOrLeader))
			items = _uok.telesale.teamMemberRepo().findTeamMemberByTeamLeadId(userId, StringUtils.nullToEmpty(findType), StringUtils.nullToEmpty(campaignType));
		
		ArrayList<UserDTO> list = null;
		if (items != null && items.size() > 0) {
			list = new ArrayList<UserDTO>();
			for (Users item : items) {
				list.add(modelMapper.map(item, UserDTO.class));
			}
		}
		
		return list;
	}
	
	public UserDTO getTeamLeaderBy(Long memberId) {
		try {
			Users item = _uok.telesale.teamMemberRepo().findTeamLeader(memberId);
			return modelMapper.map(item, UserDTO.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<Users> getTeamLeadersBy(Long memberId) {
		List<Users> items = _uok.telesale.teamMemberRepo().findTeamLeaders(memberId);
		return items;
	}

	/*public TeamMember getTeamMenberByTeamId(Long memberId) {
		TeamMember tMember = _uok.telesale.teamMemberRepo().findTeamMenberByTeamId(memberId);
		return tMember;
	}*/

	public List<Team> getTeamByTeamleadLoginID(Long managerId) {
		List<Team> teams = _uok.telesale.teamRepo().getTeamByTeamleadLoginID(managerId);
		return teams;
	}
	
	public List<UplCustomerScoringDTO> lstUplCusByTSACode(String tsaCode, String identityNumber, String mobile,String sendDateFrom,String sendDateTo,String loginId) {
		List<UplCustomerScoringDTO> teams = _uok.telesale.teamRepo().lstUplCusByTSACode(tsaCode, identityNumber, mobile, sendDateFrom, sendDateTo, loginId);
		return teams;
	}
	
	public List<UplCustomerScoringDTO> findCustomerSupervisor(String tsaCode, String identityNumber, String mobile,String sendDateFrom,String sendDateTo,String loginId) {
		List<UplCustomerScoringDTO> teams = _uok.telesale.teamRepo().findCustomerSupervisor(tsaCode, identityNumber, mobile,sendDateFrom,sendDateTo, loginId);
		return teams;
	}

	public Object sendOtp(SendOTP object) throws Exception {
		// TODO Auto-generated method stub
		EsbApi esb = new EsbApi();
		ApiResult apiResult = (ApiResult) esb.sendOTP(object);
		return apiResult;
	}
	
	public Object sendMarkEsb(ScoringPayload object) throws Exception {
		// TODO Auto-generated method stub
		SendMarkESB sendMarkESB = new SendMarkESB(object.getPrimaryPhone(), object.getNationalId(), object.getVerificationCode(), object.getVersion().intValue());
		// Kiem tra dieu kien neu cham diem qua 30 ngay thi ko duoc phep cham diem lai. Gia tri udf03 luu ngay cham diem 
		///isAllowGrading(object);
		ApiResult apiResult = (ApiResult) new EsbApi().sendMarkEsb(sendMarkESB);
		System.out.println(JSONFactory.toJson(apiResult));
		return apiResult;  
	}
	
	/**
	 * @deprecated no longer used this from RQ1011
	 * @param scoringPayload
	 * @return Thuc hien cham diem TS. Se co 2 buoc cham diem la Compute Scoring va CheckScore API
	 * @throws Exception
	 */
	public Object scoringTs(ScoringPayload scoringPayload) throws Exception {
		// 1. Kiem tra dieu kien co duoc phep cham diem hay khong ( Ngay Cham diem khong duoc qua 30 ngay )
		UplCustomer uplCustomer = _uok.telesale.uplCustomerRepo().getUplCustomerbyID(scoringPayload.getIdUplCustomer());
		if(uplCustomer.getUdf07() !=null) {
			Date scoringTsDate = DateUtil.stringToDateByForm(uplCustomer.getUdf07(), "yyyy-MM-dd'T'HH:mm:ssXXX");
			Integer count = DateUtil.subtract(new Date(), scoringTsDate);
			if(count <= 30) throw new ValidationException("Ng\u00E0y ch\u1EA5m \u0111i\u1EC3m v\u1EABn c\u00F2n hi\u1EC7u l\u1EF1c !");
		}
		// Tim kiem UplCust de update
		List<Long> lstIdUplCust = this._uok.telesale.telesaleRepository().getUplCustProspect(scoringPayload.getPrimaryPhone(),scoringPayload.getNationalId());
		List<UplCustomer> lstUplCust = new ArrayList<>(); 
		if (lstIdUplCust == null || lstIdUplCust.isEmpty()) { 
			// 1.1 Neu khong co bao loi luon 
			throw new ValidationException("D\u1EEF li\u1EC7u kh\u00F4ng t\u1ED3n t\u1EA1i !");
		} else {
			for(Long longItem: lstIdUplCust) {
				UplCustomer upCustomer = this._uok.telesale.uplCustomerRepo().getUplCustomerbyID(longItem);
				if(upCustomer !=null) lstUplCust.add(upCustomer);
			} 
		}
		// 2. Thuc hien cham diem buoc 1 tai computer_score api
		ScoringPayload computeScorePayload = new ScoringPayload(scoringPayload.getPrimaryPhone(),scoringPayload.getNationalId(), scoringPayload.getVerificationCode(),null);
		ApiResult resultScoringEsb =  (ApiResult) this.sendMarkEsb(computeScorePayload);  
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultScoringEsb.getBodyContent().replace("\\", ""), ScoringResponse.class);
		ScoringTsResponse scoringCompute = (ScoringTsResponse) com.mcredit.data.util.JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringDTO.getResult()), ScoringTsResponse.class);
		ScoringTsResponse responseScoringFinal = new ScoringTsResponse();
		if(ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(scoringCompute.getReturnCode())) {
		// Goi tiep vao cham diem TS 
			ScoringPayload scoringPayloadTs = new ScoringPayload("", "", "", scoringCompute.getApiRequestId());
			ApiResult resultScoringTs = (ApiResult) sendMarkTs(scoringPayloadTs);   
			ScoringResponse scoringTsDTO = (ScoringResponse) JSONFactory.fromJSON(resultScoringTs.getBodyContent().replace("\\", ""), ScoringResponse.class);
			ScoringTsResponse resultCheckScore = (ScoringTsResponse) JSONFactory.fromJSON(JSONFactory.toJson(scoringTsDTO.getResult()), ScoringTsResponse.class);
			responseScoringFinal = resultCheckScore;
			// Lay ket qua resultCheckScore de thuc hien update upl customer va upl customer hist
			// 3.1 Lay them ban ghi co o trong upl Cus nhung ko co o cus spopec
			List<Long> lstUplCustAdd = _uok.telesale.telesaleRepository().getListUplCustBy(scoringPayload.getPrimaryPhone(),scoringPayload.getNationalId());
/*				for(UplCustomer item : lstUplCustAdd) {
				// Tim neu ko thay trong CUST_PROSPECT thi moi them vao 
				List<UplCustomer> lstFilter = lstUplCust;
				lstFilter = (List<UplCustomer>) lstFilter.stream().filter(itemSearch -> itemSearch.getId().equals(item.getId())).collect(Collectors.toList());
				if(lstFilter.size() == 0) 
					lstUplCust.add(item);
			}*/
			if(lstUplCustAdd!=null && lstUplCustAdd.size()!=0) {
				for(Long longItem:lstUplCustAdd) {
					UplCustomer uplCustomerCheck = this._uok.telesale.uplCustomerRepo().findById(longItem);
					boolean check = contains(lstUplCust, uplCustomerCheck.getId());
					if(!check) lstUplCust.add(uplCustomerCheck);
				}
			}
			
			for(UplCustomer item : lstUplCust) {
				item = this.updateUplCustomerBpm(item, resultCheckScore, scoringPayload.getAppNumber());
				String messageInsert = ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(resultCheckScore.getReturnCode()) ? resultCheckScore.getScores()+"@@"+resultCheckScore.getVerifyInfo() : resultCheckScore.getReturnMes();
				UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(item.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_TS, resultCheckScore.getReturnCode(), messageInsert);
				_uok.telesale.uplCustomerHistoryRepo().add(uplCustomerHistory);
			}
			responseScoringFinal= resultCheckScore;
			responseScoringFinal.setType(ConstantTelesale.CUS_HIS_REFID_MARK_TS);
		} else {
			// 3.1 Lay them ban ghi co o trong upl Cus nhung ko co o cus spopec
			List<Long> lstUplCustAdd = _uok.telesale.telesaleRepository()
					.getListUplCustBy(scoringPayload.getPrimaryPhone(), scoringPayload.getNationalId());
			if(lstUplCustAdd!=null && lstUplCustAdd.size()!=0) {
				for(Long longItem:lstUplCustAdd) {
					UplCustomer uplCustomerCheck = this._uok.telesale.uplCustomerRepo().findById(longItem);
					boolean check = contains(lstUplCust, uplCustomerCheck.getId());
					if(!check) lstUplCust.add(uplCustomerCheck);
				}
			}
			for(UplCustomer item : lstUplCust) {
				UplCustomer itemInsert = item;
				itemInsert = this.updateUplCustomerBpm(itemInsert, scoringCompute, scoringPayload.getAppNumber());
				UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(item.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_ESB, scoringCompute.getReturnCode(), scoringCompute.getReturnMes());
				_uok.telesale.uplCustomerHistoryRepo().add(uplCustomerHistory);
				item = null;
			}
			responseScoringFinal = scoringCompute;
			responseScoringFinal.setType(ConstantTelesale.CUS_HIS_REFID_MARK_ESB);
		}
		lstUplCust = null;
		return responseScoringFinal;
	}
	
	public boolean isAllowGradingOTP(Long idUplCus) throws ValidationException{
		boolean check =true;
		UplCustomer uplCus = _uok.telesale.uplCustomerRepo().findById(idUplCus);
		if(uplCus!=null  && uplCus.getMinScore() !=null &&  !StringUtils.isNullOrEmpty(uplCus.getUdf06())) {
			Date dateMarkTs = DateUtil.stringToDateByForm(uplCus.getUdf06());
			Integer dateCompate = DateUtil.subtract(new Date(), dateMarkTs);
			if(dateCompate <= 30) 
				check = false;
		}
		return check;
	}
	
	public Object sendMarkTs(ScoringPayload object) throws Exception {
		// TODO Auto-generated method stub 
		SendMarkTS sendMarkTS = new SendMarkTS(object.getApiRequestId());  
		ApiResult apiResult = (ApiResult) new EsbApi().sendMarkTs(sendMarkTS);
		return apiResult;
	} 
	
	public Object insertCusHis(UplCustomerHistoryDTO object) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		UplCustomerHistory _cusHisObj = modelMapper.map(object, UplCustomerHistory.class);
		UplCustomer uplCustomer = _uok.telesale.uplCustomerRepo().findById(object.getUplCustomerId());
		/*Note: Trong trÃ†Â°Ã¡Â»ï¿½ng hÃ¡Â»Â£p khÃƒÂ¡ch hÃƒÂ ng Ã„â€˜ÃƒÂ£ Ã„â€˜Ã†Â°Ã¡Â»Â£c chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m vÃƒÂ  user tiÃ¡ÂºÂ¿n hÃƒÂ nh chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m lÃ¡ÂºÂ¡i:
			-	NÃ¡ÂºÂ¿u chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m thÃƒÂ nh cÃƒÂ´ng: TrÃ†Â°Ã¡Â»ï¿½ng KÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m, Note hiÃ¡Â»Æ’n thÃ¡Â»â€¹ theo kÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m mÃ¡Â»â€ºi nhÃ¡ÂºÂ¥t
			-	NÃ¡ÂºÂ¿u chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m khÃƒÂ´ng thÃƒÂ nh cÃƒÂ´ng: TrÃ†Â°Ã¡Â»ï¿½ng KÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m, Note giÃ¡Â»Â¯ nguyÃƒÂªn theo kÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m thÃƒÂ nh cÃƒÂ´ng cÃ…Â© (gÃ¡ÂºÂ§n nhÃ¡ÂºÂ¥t trÃ†Â°Ã¡Â»â€ºc lÃ¡ÂºÂ§n chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m thÃ¡ÂºÂ¥t bÃ¡ÂºÂ¡i)
			-	NÃ¡ÂºÂ¿u chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m khÃƒÂ´ng thÃƒÂ nh cÃƒÂ´ng, cÃƒÂ¡c lÃ¡ÂºÂ§n trÃ†Â°Ã¡Â»â€ºc chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m khÃƒÂ´ng thÃƒÂ nh cÃƒÂ´ng : TrÃ†Â°Ã¡Â»ï¿½ng KÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m, Note Ã„â€˜Ã†Â°Ã¡Â»Â£c cÃ¡ÂºÂ­p nhÃ¡ÂºÂ­t theo kÃ¡ÂºÂ¿t quÃ¡ÂºÂ£ chÃ¡ÂºÂ¥m Ã„â€˜iÃ¡Â»Æ’m mÃ¡Â»â€ºi nhÃ¡ÂºÂ¥t.*/
		if(ConstantTelesale.CUS_HIS_REFID_MARK_TS.equals(object.getRefId())) {
			if(ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(object.getResponseCode())) {
				saveUplCusHis(_cusHisObj);
			} else {
				// Neu ket qua cham diem cu la thanh cong thi khong lam gi. nguoc lai thi thuc hien update lai
				if(uplCustomer.getMinScore() == null)
					saveUplCusHis(_cusHisObj);
			} 
		} else _uok.telesale.uplCustomerHistoryRepo().saveOrUpdate(_cusHisObj);
		return _cusHisObj;
	}
	
	public void saveUplCusHis(UplCustomerHistory uplCustomerHistory) {
		_uok.telesale.uplCustomerHistoryRepo().saveOrUpdate(uplCustomerHistory);
	}
	
	public Object updateUplCus(ScoringTS obj) throws Exception {
		// Tim kiem uplCus
		UplCustomer uplCustomer = (UplCustomer) _uok.telesale.uplCustomerRepo().get(UplCustomer.class, obj.getId());
		uplCustomer.setMinScore(obj.getMinScore());
		uplCustomer.setUdf05(obj.getVerifyInfo());
		if(obj.getDateMarkTs()!=null) uplCustomer.setUdf06(DateUtil.dateToStringVN(obj.getDateMarkTs()));
		if(obj.getDateMarkTsStr()!=null) uplCustomer.setUdf07(obj.getDateMarkTsStr());
		_uok.telesale.uplCustomerRepo().upsert(uplCustomer); 
		return uplCustomer;       
	}
	
	public Object getCusHis(Long cusId, String refId) throws Exception {
		// Tim kiem uplCus
		return _uok.telesale.uplCustomerHistoryRepo().getBy(cusId, refId);
	}
	
	public Object getUplCus(Long id) throws Exception {
		// Tim kiem uplCus
		return _uok.telesale.uplCustomerRepo().findById(id);  
	}
	   
	public Object checkCustomerNTB(Long idUplCus) throws Exception {
		// Tim kiem uplCus
		return _uok.telesale.uplCustomerRepo().checkCustomerNTB(idUplCus);
	}
	  
	public Object checkSendOtp(Long idUplCus) throws Exception {
		// Tim kiem uplCusHis voi loai bang Send TS
		boolean isSendOtp = true;
		isSendOtp = isAllowGradingOTP(idUplCus);
		return isSendOtp;
	}
	
	public Object searchScoring(String mobile, String identityNumber, String identityNumberOld) {
		// 1.1 Tim kiem trong bang cust_prospect
		ScoringTsResponse responseScoringFinal = new ScoringTsResponse();
		UplCustomerHistory uplCustHist1 = (UplCustomerHistory) this._uok.telesale.telesaleRepository().getClosestSuccessScore(mobile, identityNumber, identityNumberOld);
		
		// 1.2 Neu khong co thi tim kiem trong uplCus voi fromsource = BPM
		UplCustomerHistory uplCustHist2 = (UplCustomerHistory) this._uok.telesale.telesaleRepository().getClosestSuccessScoreSourceBPM(mobile, identityNumber, identityNumberOld);
		
		// 1.3 Lay record co LAST_UPDATED_DATE moi nhat
		UplCustomerHistory uplCustHist = null;
		if (uplCustHist1 == null && uplCustHist2 == null) {
			uplCustHist = null;
		} else if (uplCustHist1 == null) {
			uplCustHist = uplCustHist2;
		} else if (uplCustHist2 == null) {
			uplCustHist = uplCustHist1;
		} else {
			uplCustHist = uplCustHist1;
			if (uplCustHist2.getLastUpdatedDate().after(uplCustHist1.getLastUpdatedDate())) {
				uplCustHist = uplCustHist2;
			}
		}
			
		//2. Xu ly doi voi lst nhan duoc de tra ve ket qua diem cho BPM
		/// lstUplCust = lstUplCust.stream().filter(item -> item.getMinScore()!=null).collect(Collectors.toList());
		///uplCustomerResult = lstUplCust.isEmpty() ? null : lstUplCust.get(lstUplCust.size()-1);
		if(uplCustHist != null) {
			String score = "";
			String verifyInfo = "";
			if(uplCustHist.getMessage().split("@@")!=null) { 
				score = uplCustHist.getMessage().split("@@")[0];
				verifyInfo = uplCustHist.getMessage().split("@@")[1];
			}
			responseScoringFinal.setScores(new Long(score));
			responseScoringFinal.setVerifyInfo(verifyInfo);
			responseScoringFinal.setReturnCode(uplCustHist.getResponseCode());
			responseScoringFinal.setReturnMes(uplCustHist.getMessage());
			// Kiem tra ngay hien tai > ngay nhan diem 30 ngay thi tra ra loi tuong ung
			Integer dateSubtract =  DateUtil.subtract(new Date(), uplCustHist.getLastUpdatedDate());  
			if(dateSubtract > 30) responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_VALID_801);
			else responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_INVALID_800);
			// responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_VALID_801);
			// Xu ly doi voi date time 
			responseScoringFinal.setDateTime(DateUtil.dateToString(uplCustHist.getLastUpdatedDate(), "yyyy-MM-dd HH:mm:ss"));
		}
		// Bien doi sang cai moi 
		modelMapper = new ModelMapper();
		ScoringTsResponseFinal scoringTsResponseFinal = modelMapper.map(responseScoringFinal, ScoringTsResponseFinal.class);
		if(StringUtils.isNullOrEmpty(scoringTsResponseFinal.getReturnCode())) scoringTsResponseFinal.setStatus(ConstantTelesale.SCORING_STATUS_FAIL_NOT_FOUND_802);
		return scoringTsResponseFinal;
		//return responseScoringFinal;
	}   
	
	public UplCustomer updateUplCustomerBpm(UplCustomer uplCustomerUpdate, ScoringTsResponse resultCheckScore, String appNumber) {
		uplCustomerUpdate.setMinScore(resultCheckScore.getScores());
		uplCustomerUpdate.setUdf05(resultCheckScore.getVerifyInfo());
		uplCustomerUpdate.setUdf03(appNumber);
		if(ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(resultCheckScore.getReturnCode())) {
			uplCustomerUpdate.setUdf07(resultCheckScore.getDateTime());
			uplCustomerUpdate.setUdf06(DateUtil.dateToString(new Date(), "dd/MM/yyyy H:mm:ss"));
			// Cot udf03 se luu them case number
			uplCustomerUpdate = _uok.telesale.uplCustomerRepo().mergeObject(uplCustomerUpdate);
		} else {
			if(uplCustomerUpdate.getId()!=null) {
				uplCustomerUpdate.setMinScore(null);
				uplCustomerUpdate.setUdf05(null);
				uplCustomerUpdate.setUdf03(null);
				uplCustomerUpdate.setUdf06(null);
				uplCustomerUpdate.setUdf07(null);
				uplCustomerUpdate = _uok.telesale.uplCustomerRepo().mergeObject(uplCustomerUpdate);
			}
		}
		return uplCustomerUpdate;   
	}
	
	public Object scoringTsSourceBPM(String mobile, String identityNumber, String otp, String appNumber, String identityNumberOld) throws Exception {
		// Khoi tao 1 Object de chua thong tin tra ra cho BPM : 
		ScoringTsResponse responseScoringFinal = new ScoringTsResponse();
		// Tim kiem UplCust de update
		List<Long> lstLongUplCust = this._uok.telesale.telesaleRepository().getUplCustProspect(mobile,
				identityNumber);
		if (lstLongUplCust==null || lstLongUplCust.isEmpty()) {
			// 1.1 Neu khong co thi tim kiem trong uplCus voi fromsource = BPM
			lstLongUplCust = this._uok.telesale.telesaleRepository().getUplCustFromBPM(mobile, identityNumber);
		}
		// Thuc hien thien convert sang dang uplCustomer
		List<UplCustomer> lstUplCust = new ArrayList<>();
		for(Long itemLong: lstLongUplCust) {
			UplCustomer uplCustomer = this._uok.telesale.uplCustomerRepo().findById(itemLong);
			if(uplCustomer!=null) lstUplCust.add(uplCustomer);
		}
		// Check ngay qua 30 ngay 
		if(!lstUplCust.isEmpty()) {
			List<UplCustomer> lstUplCustCheckValid = lstUplCust.stream().filter(item -> (item.getMinScore() !=null && !StringUtils.isNullOrEmpty(item.getUdf07()))).collect(Collectors.toList());
			UplCustomer uplCustomerCheck = lstUplCustCheckValid.isEmpty() ? null :  lstUplCustCheckValid.get(lstUplCustCheckValid.size() - 1);
			if(uplCustomerCheck !=null) {
				Date scoringTsDate = DateUtil.stringToDateByForm(uplCustomerCheck.getUdf07(), "yyyy-MM-dd'T'HH:mm:ssXXX");
				Integer count = DateUtil.subtract(new Date(), scoringTsDate);
				if(count <= 30) throw new ValidationException("Ng\u00E0y ch\u1EA5m \u0111i\u1EC3m v\u1EABn c\u00F2n hi\u1EC7u l\u1EF1c !");
			}
		}
		// 1. Thuc hien cham diem buoc 1 tai computer_score api
		ScoringPayload scoringPayload = new ScoringPayload(mobile, identityNumber, otp, null);   
		scoringPayload.setAppNumber(appNumber);
		ApiResult resultScoringEsb =  (ApiResult) this.sendMarkEsb(scoringPayload);  
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultScoringEsb.getBodyContent().replace("\\", ""), ScoringResponse.class);
		ScoringTsResponse scoringCompute = (ScoringTsResponse) com.mcredit.data.util.JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringDTO.getResult()), ScoringTsResponse.class);
		responseScoringFinal = scoringCompute;
		if(scoringCompute.getReturnCode().equals("success")) {   
			// Xu ly buoc tiep theo
			// Goi tiep vao cham diem TS 
			ScoringPayload scoringPayloadTs = new ScoringPayload("", "", "", scoringCompute.getApiRequestId());
			ApiResult resultScoringTs = (ApiResult) sendMarkTs(scoringPayloadTs);   
			ScoringResponse scoringTsDTO = (ScoringResponse) JSONFactory.fromJSON(resultScoringTs.getBodyContent().replace("\\", ""), ScoringResponse.class);
			ScoringTsResponse resultCheckScore = (ScoringTsResponse) JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringTsDTO.getResult()), ScoringTsResponse.class);
			// 2. Neu ca 2 nguon deu ko co thi thuc hien tao moi voi source la BPM
			if(lstUplCust==null || lstUplCust.isEmpty()) {
				// Lay code cua from source 
				Long fromSourceId = _uok.telesale.codeTableRepository().findCodeTableBy(CTGroup.MISC.value(), CTCat.UPL_SRC.value(), "BPM","").get(0).getId();
				Long uplDetailId = _uok.telesale.uplDetailRepo().getIdUplDetailSourceBPM();
				if(uplDetailId== null) throw new Exception("Co loi xay ra !");
				// Thuc hien tao moi upl cus
				UplCustomer uplCustomerInsert = new UplCustomer();
				uplCustomerInsert.setMobile(mobile);
				uplCustomerInsert.setIdentityNumber(identityNumber);
				uplCustomerInsert.setUplDetailId(uplDetailId); 
				// Cap nhat thong tin cham diem
				uplCustomerInsert = this.updateUplCustomerBpm(uplCustomerInsert, resultCheckScore, scoringPayload.getAppNumber());
				// Them moi vao bang upl_cus_hist
				// _uok.telesale.uplCustomerRepo().add(uplCustomerInsert);
				String messageInsert = ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(resultCheckScore.getReturnCode()) ? resultCheckScore.getScores()+"@@"+resultCheckScore.getVerifyInfo()+"@@"+appNumber : resultCheckScore.getReturnMes()+"@@"+appNumber; 
				UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(uplCustomerInsert.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_TS, resultCheckScore.getReturnCode(), messageInsert);
				_uok.telesale.uplCustomerHistoryRepo().merge(uplCustomerHistory);
			} else {
				// 3. Neu tim thay thi thuc hien update all vao bang upl cus va upl cus hist
				// 3.1 Lay them ban ghi co o trong upl Cus nhung ko co o cus spopec
				List<Long> lstUplCustAdd = _uok.telesale.telesaleRepository().getListUplCustBy(mobile, identityNumber);
/*				for(UplCustomer item : lstUplCustAdd) {
					// Tim neu ko thay trong CUST_PROSPECT thi moi them vao 
					List<UplCustomer> lstFilter = lstUplCust;
					lstFilter = (List<UplCustomer>) lstFilter.stream().filter(itemSearch -> itemSearch.getId().equals(item.getId())).collect(Collectors.toList());
					if(lstFilter.size() == 0) 
						lstUplCust.add(item);
				}*/
				
				if(lstUplCustAdd!=null && lstUplCustAdd.size()!=0) {
					for(Long longItem:lstUplCustAdd) {
						UplCustomer uplCustomerCheck = this._uok.telesale.uplCustomerRepo().findById(longItem);
						boolean check = contains(lstUplCust, uplCustomerCheck.getId());
						if(!check) lstUplCust.add(uplCustomerCheck);
					}
				}
				
				//lstUplCust.addAll(lstUplCustAdd);
				for(UplCustomer item : lstUplCust) { 
					item = this.updateUplCustomerBpm(item, resultCheckScore,scoringPayload.getAppNumber()); 
					String returnMessage = resultCheckScore.getReturnMes()+"@@"+appNumber;
					if(("success").equals(resultCheckScore.getReturnCode()))
						returnMessage = resultCheckScore.getScores()+"@@"+resultCheckScore.getVerifyInfo()+"@@"+appNumber;
					UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(item.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_TS, resultCheckScore.getReturnCode(), returnMessage);
					_uok.telesale.uplCustomerHistoryRepo().add(uplCustomerHistory);
				}
			}
			responseScoringFinal = resultCheckScore;
		} else {
			// 2. Neu ca 2 nguon deu ko co thi thuc hien tao moi voi source la BPM
			if(lstUplCust==null || lstUplCust.isEmpty()) {
				// Lay code cua from source
				Long fromSourceId = _uok.telesale.codeTableRepository().findCodeTableBy(CTGroup.MISC.value(), CTCat.UPL_SRC.value(), "BPM","").get(0).getId();
				Long uplDetailId = _uok.telesale.uplDetailRepo().getIdUplDetailSourceBPM();
				if(uplDetailId== null) throw new Exception("Co loi xay ra !");
				// Thuc hien tao moi upl cus
				UplCustomer uplCustomerInsert = new UplCustomer();
				uplCustomerInsert.setMobile(mobile);
				uplCustomerInsert.setIdentityNumber(identityNumber);
				uplCustomerInsert.setUplDetailId(uplDetailId);
				uplCustomerInsert.setUdf03(appNumber);
				// Them moi vao bang upl_cus_hist
				_uok.telesale.uplCustomerRepo().add(uplCustomerInsert);
				String messageInsert = ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(scoringCompute.getReturnCode()) ? scoringCompute.getScores()+"@@"+scoringCompute.getVerifyInfo()+"@@"+appNumber : scoringCompute.getReturnMes()+"@@"+appNumber;
				UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(uplCustomerInsert.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_TS, scoringCompute.getReturnCode(), messageInsert);
				_uok.telesale.uplCustomerHistoryRepo().add(uplCustomerHistory);
			} else {
				// 3. Neu tim thay thi thuc hien update all vao bang upl cus va upl cus hist   
				List<Long> lstUplCustAdd = _uok.telesale.telesaleRepository().getListUplCustBy(mobile,
						identityNumber);
				/*
				 * for(UplCustomer item : lstUplCustAdd) { // Tim neu ko thay
				 * trong CUST_PROSPECT thi moi them vao List<UplCustomer>
				 * lstFilter = lstUplCust; lstFilter = (List<UplCustomer>)
				 * lstFilter.stream().filter(itemSearch ->
				 * itemSearch.getId().equals(item.getId())).collect(Collectors.
				 * toList()); if(lstFilter.size() == 0) lstUplCust.add(item); }
				 */

				if (lstUplCustAdd != null && lstUplCustAdd.size() != 0) {
					for (Long longItem : lstUplCustAdd) {
						UplCustomer uplCustomerCheck = this._uok.telesale.uplCustomerRepo().findById(longItem);
						boolean check = contains(lstUplCust, uplCustomerCheck.getId());
						if (!check)
							lstUplCust.add(uplCustomerCheck);
					}
				}
				for(UplCustomer item : lstUplCust) {
					UplCustomerHistory uplCustomerHistory = new UplCustomerHistory(item.getId(), new Long(0), ConstantTelesale.CUS_HIS_REFID_MARK_ESB, scoringCompute.getReturnCode(), scoringCompute.getReturnMes()+"@@"+appNumber);
					_uok.telesale.uplCustomerHistoryRepo().add(uplCustomerHistory);
				}
			}
		}
		// Bien doi 1 chut de tra ve cho ben BPM voi cac output theo tai lieu 
		if(ConstantTelesale.CUS_HIS_CODE_SUCCESS.equals(responseScoringFinal.getReturnCode())) {
			responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_VALID_801);
		} else {
			// Tim kiem diem thanh cong gan nhat tren CRM
			// Trong cust_pro
			UplCustomerHistory uplCustomerHistory = (UplCustomerHistory) _uok.telesale.telesaleRepository().getClosestSuccessScore(mobile, identityNumber, null);
			if(uplCustomerHistory==null) 
				uplCustomerHistory = (UplCustomerHistory) _uok.telesale.telesaleRepository().getClosestSuccessScoreSourceBPM(mobile, identityNumber, null);
			if(uplCustomerHistory == null) {
				responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_FAIL_ORTHER_803);
			}
			else {
				String score = "";
				String verifyInfo = "";
				if(uplCustomerHistory.getMessage().split("@@")!=null) {
					score = uplCustomerHistory.getMessage().split("@@")[0];
					verifyInfo = uplCustomerHistory.getMessage().split("@@")[1];
				}
				responseScoringFinal.setReturnCode(uplCustomerHistory.getResponseCode());
				responseScoringFinal.setReturnMes(uplCustomerHistory.getMessage());
				responseScoringFinal.setScores(new Long(score));
				responseScoringFinal.setVerifyInfo(verifyInfo); 
				responseScoringFinal.setDateTime(DateUtil.dateToString(uplCustomerHistory.getLastUpdatedDate(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
				// Kiem tra ngay hien tai > ngay nhan diem 30 ngay thi tra ra loi tuong ung
				Integer dateSubtract =  DateUtil.subtract(new Date(), uplCustomerHistory.getLastUpdatedDate());  
				if(dateSubtract > 30) responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_VALID_801);
				else responseScoringFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_INVALID_800);
			}
		}
//		// Xu ly doi voi date time 
//		if(!StringUtils.isNullOrEmpty(responseScoringFinal.getDateTime())) {
//			Date dateResponse = DateUtil.stringToDateByForm(responseScoringFinal.getDateTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
//			responseScoringFinal.setDateTime(DateUtil.dateToString(dateResponse, "yyyy-MM-dd HH:mm:ss"));
//		}
		//responseScoringFinal.setDateTime(responseScoringFinal.);
		// Bien doi sang cai moi 
		modelMapper = new ModelMapper();
		ScoringTsResponseFinal scoringTsResponseFinal = modelMapper.map(responseScoringFinal, ScoringTsResponseFinal.class);
		if("success".equals(scoringTsResponseFinal.getReturnCode())) {
			Integer dateSubtract =  DateUtil.subtract(new Date(), DateUtil.stringToDateByForm(scoringTsResponseFinal.getDateTime(), "yyyy-MM-dd'T'HH:mm:ssXXX"));  
			if(dateSubtract > 30) scoringTsResponseFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_VALID_801);
			else scoringTsResponseFinal.setStatus(ConstantTelesale.SCORING_STATUS_SUCCESS_INVALID_800);
		}
		return scoringTsResponseFinal;
	}
	
	/**
	 * @param  mobile, identityNumber
	 * @return Submit Scoring to BPM 
	 * @throws Exception
	 */
	public Object scoringApi(String mobile, String identityNumber, String otp, String appNumber, String identityNumberOld) throws Exception {
		// ntmanh edit : 01.10.19
		// Thay doi api chuyen sang sang cho bpm.
		// 1. Truong hop tra cuu opt = null
		// 2. Truong hop cham diem otp <> null 
		if(StringUtils.isNullOrEmpty(otp)) return this.searchScoring(mobile, identityNumber, identityNumberOld);
		else return this.scoringTsSourceBPM(mobile, identityNumber, otp,appNumber, identityNumberOld);
	}

	public ProductInterestDTO getProdByScore(String partner, String telcoCode, Integer score) throws Exception {
		EsbApi esbApi = new EsbApi();
		ApiResult apiResult = null;
		RuleResult ruleResult = null;

		String productCode = "";
		ProductInterestDTO productResut = new ProductInterestDTO();
		
		/** CHECK RULES **/
		Map<String, String> inputCheckRule = new HashMap<String, String>();
		inputCheckRule.put("ruleCode", "LEADGEN_SCORE");
		inputCheckRule.put("typeScore", "2");
		inputCheckRule.put("partner", this.getPartnerFromText(partner));
		inputCheckRule.put("telcoCode", this.getTelcoCodeFromText(telcoCode));
		inputCheckRule.put("minScore", String.valueOf(score));

		apiResult = esbApi.checkRule(inputCheckRule);

		if (apiResult == null || !apiResult.getStatus())
			throw new ValidationException("Không có sản phẩm phù hợp");

		ruleResult = JSONConverter.toObject(apiResult.getBodyContent(), RuleResult.class);
		if (ruleResult == null)
			throw new ValidationException(Messages.getString("Ket qua check rule qua ESB khong kha dung"));

		if (!ruleResult.getReturnCode().contains("200"))
			throw new ValidationException("Không có sản phẩm phù hợp");
		
		productCode = ruleResult.getMultiValue();
		
		if(!StringUtils.isNullOrEmpty(productCode))     
			productResut= _uok.telesale.productRepository().getProductInterestBy(productCode);
		
		return productResut;
	}

	public Object getRoleCodeUserTls(String loginId) {
		return this._uok.telesale.telesaleRepository().getRoleCodeUserTls(loginId);
	}

	public Object getMemberNTB() {
		// TODO Auto-generated method stub
		return this._uok.telesale.telesaleRepository().getMemberNTB();
	}
	
	public Object getMemberByTeamLead(String loginId) {
		// TODO Auto-generated method stub
		return this._uok.telesale.telesaleRepository().getMemberByTeamLead(loginId);
	}
	
	public Object createCaseFromCRM(UploadCaseDTO newCase, UserDTO _user) throws Exception {
		System.out.println("createCaseFromCRM: " + JSONConverter.toJSON(newCase));
		// Lay product id
		ProductDTO product = productCache.findProductByCode(newCase.getProductCode());
		if (null == product)
			throw new Exception(Messages.getString("miniCrm.product.notfound") + newCase.getProductCode());
		
		// Kiem tra thong tin old app number upload len Xsell. Khong ap dung tao case tu CRM cho du lieu da upload len truoc thoi diem golive (truong app_number null)
		UplCustomer upl = this._uok.telesale.uplCustomerRepo().findById(newCase.getUplCustomerId());
		if (null == upl || null == upl.getAppNumber() || upl.getAppNumber() <= 0)
			throw new Exception(Messages.getString("miniCrm.createCase.invalid") + newCase.getUplCustomerId());
		
		// Kiem tra xem hop dong da ton tai chua
		checkDuplicate(newCase, product.getId());
		
		// check cmnd
		checkCitizenId(newCase.getCitizenId(), newCase.getLoanAmount().longValue(), null);
		
		// Tao ban ghi moi trong upl credit app request. Job create-case, route-case tren mobile se day case sang bpm
		UplCreditAppRequest ucar = Converter.getNewCase(newCase, product.getId(), _user);
		this._uok.telesale.uplCreditAppRequestRepository().add(ucar);
		
		// Tao create-case request trong message log
		CustProspect cp = this._uok.telesale.prospectManagerRepo().findCustProsptectByUPLCustId(upl.getId());
		String newMobile = (null == cp?null:cp.getNewMobile());
		MessageLog mgsLog = Converter.getCreateCaseRequest(newCase, ucar, _user, this._uok.telesale.employeeRepository(), String.valueOf(upl.getAppNumber()), newMobile, product);
		this._uok.telesale.messageLogRepository().add(mgsLog);
		
		return new Result(STATUS_CODE_OK, "Sucess");
	}
	
	public void checkDuplicate(UploadCaseDTO newCase, Long productId) throws ValidationException, ParseException {
		UplCreditAppRequest ucar = this._uok.telesale.uplCreditAppRequestRepository().checkDuplicate(newCase, productId);
		if (null != ucar)
			throw new ValidationException(Messages.getString("miniCrm.createCase.duplicate"));
		
	}
	
	public Result checkCitizenId(String citizenID, Long loanAmount, String appNumber) throws ValidationException, Exception {
		if (null == loanAmount)
			loanAmount = 0L;
		
		if (StringUtils.isNullOrEmpty(appNumber))
			appNumber = "0";
		
		EsbApi esb = new EsbApi();
		ObjectMapper mapper = new ObjectMapper();
		
		ApiResult api = esb.checkCitizenId(citizenID, CASH_LOAN, String.valueOf(loanAmount), appNumber);
		Result response = null;
		
		if (null == api) {
			System.out.println("Service check citizenId error: " + citizenID);
			throw new Exception("Service check citizenId error");
		}
		
		if (!api.getStatus()) {
			System.out.println("Service check citizenId error http code=" + api.getCode());
			throw new Exception("Service check citizenId error http code=" + api.getCode() + " with content: " + api.getBodyContent());
		}
		
		response = mapper.readValue(api.getBodyContent(), new TypeReference<Result>() {});
		if (null == response)
			throw new Exception("Service check citizenId mapping result error");
		
		if (!"200".equals(response.getReturnCode())) {
			System.out.println("Check citizenId invalid, citizenId=" + citizenID + ", description=" + response.getReturnMes());
			throw new ValidationException(response.getReturnMes());
		}
		
		return response;
	}
	
	public List<KioskDTO> getKiosks() {
		return ctCache.getKosks();
	}
	
	public ProductInfo getProductInfo(String productCode) throws ValidationException {
		
		List<ProductDTO> lstProducts = productCache.findListProductByCode(productCode);
		if (null == lstProducts || lstProducts.isEmpty())
			throw new ValidationException(Messages.getString("miniCrm.product.notfound") + productCode);
		
		for (ProductDTO p : lstProducts) {
			if (p.getMinLoanAmount() != null && p.getMaxLoanAmount() != null && p.getMinTenor() != null && p.getMaxTenor() != null)
				return new ProductInfo(p.getMaxLoanAmount().longValue(), p.getMinLoanAmount().longValue(), p.getMaxTenor().longValue(),  p.getMinTenor().longValue());
		}

		throw new ValidationException(Messages.getString("miniCrm.product.notfound") + productCode);
	}
	
	public boolean contains(final List<UplCustomer> array, final Long v) {

        boolean result = false;

        for(UplCustomer item : array){
            if(item.getId().equals(v)){
                result = true;
                break;
            }
        }

        return result;
    }
	
	/**
	 * Kiểm tra user TSA quản lý Data XCell
	 * 
	 * @param userId
	 * @return
	 */
	public boolean checkUserTsaHasDataXcell(Long userId){
		return this._uok.telesale.telesaleRepository().checkUserTsaHasDataXcell(userId);
	}
	
	/**
	 * Kiểm tra hạn ngày của data XCell
	 * 
	 * @param userId
	 * @return
	 */
	public boolean checkUserTsaHasDataXcellExpired(Long userId){
		return this._uok.telesale.telesaleRepository().checkUserTsaHasDataXcellExpired(userId);
	}
	
	/**
	 * Kiểm tra user TSA thuộc Prospect Status Fresh và WIP
	 * 
	 * @param userId
	 * @return
	 */
	public boolean checkUserTsaDontHaveDataXcellValidProspectStatus(Long userId){
		return this._uok.telesale.telesaleRepository().checkUserTsaDontHaveDataXcellValidProspectStatus(userId);
	}
	
	/**
	 * Inactive user TSA
	 * 
	 * @param userId
	 * @return
	 */
	public ApiResult inactiveUserTsa(Long userId){
		ApiResult result = new ApiResult();
		
		boolean checkDataExpired = checkUserTsaHasDataXcellExpired(userId);
		boolean checkUserValidProspectStatus = checkUserTsaDontHaveDataXcellValidProspectStatus(userId);
		
		if(checkDataExpired || checkUserValidProspectStatus){
			result.setCode(STATUS_USER_TSA_HAS_DATA_XCELL);
			result.setBodyContent("UNPROCESSABLE ENTITY");
		} else {
			updateStatusUserTSA(userId, false);
			result.setCode(STATUS_ACTIVE_USER_TSA_SUCCESS);
			result.setBodyContent("SUCCESS");
		}
		return result;
	}
	
	/**
	 * Active user TSA
	 * 
	 * @param userId
	 * @return
	 */
	public ApiResult activeUserTsa(Long userId){
		ApiResult result = new ApiResult();
		updateStatusUserTSA(userId, true);
		result.setCode(STATUS_ACTIVE_USER_TSA_SUCCESS);
		result.setBodyContent("SUCCESS");
		return result;
	}
	
	/**
	 * Thay đổi trạng thái của user TSA (true: active, false : inactive)
	 * 
	 * @param userId
	 * @param isActive
	 * @return
	 */
	public void updateStatusUserTSA(Long userId, boolean isActive){
		this._uok.telesale.telesaleRepository().changeStatusUserTsa(userId, isActive);
		this._uok.telesale.telesaleRepository().changeStatusUserTsaInTeam(userId, isActive);
	}
	
	// RQ968
	public Object getScoreLG(String mobilePhone1, String mobilePhone2, String productCode) {
		// trim()
		mobilePhone1 = mobilePhone1.trim();
		productCode = productCode.trim();
		
		ScoreLGResult uplCustomer1 = _uok.telesale.uplCustomerRepo().getScoreLG(mobilePhone1, productCode);
		
		if (uplCustomer1 != null) {
			if (StringUtils.isNullOrEmpty(uplCustomer1.getScoreRange())) return new ScoreLGResponse(802, "score is null", "", "", "");
			return new ScoreLGResponse(800, "success", getMin(uplCustomer1.getScoreRange()), getMax(uplCustomer1.getScoreRange()), _simpleDateFormat.format(uplCustomer1.getLastUpdatedDate()));
		}
		
		if (!StringUtils.isNullOrEmpty(mobilePhone2)) {
			// trim()
			mobilePhone2 = mobilePhone2.trim();
			
			ScoreLGResult uplCustomer2 = _uok.telesale.uplCustomerRepo().getScoreLG(mobilePhone2, productCode);
			
			if (uplCustomer2 != null) {
				if (StringUtils.isNullOrEmpty(uplCustomer2.getScoreRange())) return new ScoreLGResponse(802, "score is null", "", "", "");
				return new ScoreLGResponse(800, "success", getMin(uplCustomer2.getScoreRange()), getMax(uplCustomer2.getScoreRange()), _simpleDateFormat.format(uplCustomer2.getLastUpdatedDate()));
			} 	
		}
		
		return new ScoreLGResponse(801, "failure", "", "", "");
	}
	
	private String getMin(String scoreRange) {
		if (StringUtils.isNullOrEmpty(scoreRange)) return "";
		String[] arr = scoreRange.split("-");
		return arr[0];
	}
	private String getMax(String scoreRange) {
		if (StringUtils.isNullOrEmpty(scoreRange)) return "";
		String[] arr = scoreRange.split("-");
		if(arr.length == 1) return arr[0];
		else return arr[1];
	}
	// RQ968 END
	
	// RQ1011
	public Object sendOtpV2(SendOTP object, Long userId) throws Exception {
		TSResponseDTO tsApiResult = null;
		CICDataResponseDTO cicApiResult = null;
		Long creditBureauDataId = null;
		
		// Priority Vendor for TS
		String vendorCode = VendorCodeEnum.TS.value();
		// Calling to TS first
		tsApiResult = (TSResponseDTO) this.sendOTPTS(object, ConstantTelesale.CRM, userId);
		
		// ResultCode is 'unsupported_telco' -> Calling to CICData
		if ("unsupported_telco".equals(tsApiResult.getReturnCode())) {
			vendorCode = VendorCodeEnum.CICDATA.value();
			// Call send otp to CICDATA
			cicApiResult = (CICDataResponseDTO) this.sendOTPCICData(object, ConstantTelesale.CRM, userId);
		}
		
		if (VendorCodeEnum.TS.value().equals(vendorCode)) {
			// Save to creditBureauData
			creditBureauDataId = this.saveOTPInfo(tsApiResult.getReturnCode(), vendorCode, tsApiResult.getRequestId(), tsApiResult.getTelcoCode(), 
					object.getNationalId(), object.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
			
			this.updateTransIdAsCreditBureauDataId(tsApiResult.getMessageLogId(), creditBureauDataId);
			
			return tsApiResult;
		} else {
			if ("0".equals(cicApiResult.getReturnCode())) {
				// Save to creditBureauData
				creditBureauDataId = this.saveOTPInfo("success", vendorCode, cicApiResult.getId(), ConstantTelesale.VINAPHONE_TELCOCODE, // Vinaphone is default for CICData
						object.getNationalId(), object.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
			} else {
				// Save to creditBureauData
				creditBureauDataId = this.saveOTPInfo(cicApiResult.getReturnMes(), vendorCode, cicApiResult.getId(), ConstantTelesale.VINAPHONE_TELCOCODE,
						object.getNationalId(), object.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
			}
			
			this.updateTransIdAsCreditBureauDataId(cicApiResult.getMessageLogId(), creditBureauDataId);
			
			return cicApiResult;
		}
		
	}
	
	public Object scoringTsV2(ScoringDTO scoringDTO, Long userId) throws Exception {
		String vendorCode = "";
		TSResponseDTO tsResult = null;
		CICDataResponseDTO cicDataResult = null;
		Long creditBureauDataId = null;
		
		// Get CreditBureauData record
		CreditBureauData otpRecord = _uok.pcb.creditBureauDataRepository().getScoreByKey("OTP", scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, ConstantTelesale.CRM_CBTYPE, null);
		
		if (otpRecord == null) {
			throw new ValidationException("Khong ton tai otp thanh cong hop le");
		}
		
		// Check VendorCode
		SaveOTPObject detailObj = (SaveOTPObject) JSONFactory.fromJSON(otpRecord.getCbDataDetail(), SaveOTPObject.class);
		vendorCode = detailObj.getVendorCode();
		if (VendorCodeEnum.TS.value().equals(vendorCode)) {
			CreditBureauData tsScoreRecord = _uok.pcb.creditBureauDataRepository().getScoreByKey("MARK_"+VendorCodeEnum.TS.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, ConstantTelesale.CRM_CBTYPE, null);
			
			// Check 30 days
			if (tsScoreRecord != null) {
				SaveScoreObject scoreDetailObj = (SaveScoreObject) JSONFactory.fromJSON(tsScoreRecord.getCbDataDetail(), SaveScoreObject.class);
				if ("success".equals(scoreDetailObj.getCode()) && !checkAfter30days(tsScoreRecord.getLastUpdatedDate())) {
					throw new ValidationException("Điểm Khách hàng vẫn còn hiệu lực !");
				}
			}
			
			// Get RequestId
			String requestId = detailObj.getRequestID();
			scoringDTO.setRequestId(requestId);
			
			// Call verify
			tsResult = this.verifyTS(scoringDTO, ConstantTelesale.CRM, userId);
			if (!"success".equals(tsResult.getReturnCode())) {

				// Save errors to creditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.TS.value(), tsResult.getReturnCode(), tsResult.getScore(), tsResult.getReturnMes(), 
						tsResult.getTelcoCode(), tsResult.getVerify(),
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
				
				this.updateTransIdAsCreditBureauDataId(tsResult.getMessageLogId(), creditBureauDataId);
				
				return tsResult;
			}
			
			// Call Scoring
			tsResult = (TSResponseDTO) this.scoringTS(scoringDTO, ConstantTelesale.CRM, userId);
			
			// Save to CreditBureauData
			creditBureauDataId = this.saveScore(VendorCodeEnum.TS.value(), tsResult.getReturnCode(), tsResult.getScore(), tsResult.getReturnMes(),
					tsResult.getTelco_code(), tsResult.getVerify(),
					scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
			
			// Update product to upl_customer
			if ("success".equals(tsResult.getReturnCode())) {
				Product product = new Product();
				try {
					ProductInterestDTO p = this.getProdByScore(VendorCodeEnum.TS.value(), tsResult.getTelcoCode(), Integer.parseInt(tsResult.getScore()));
					product = _uok.telesale.productRepository().getProductBy(p.getProductCode());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				this.updateUplCustomer(scoringDTO.getAppNumber(), product.getId());
			}
			
			this.updateTransIdAsCreditBureauDataId(tsResult.getMessageLogId(), creditBureauDataId);
			
			return tsResult;
		} else if (VendorCodeEnum.CICDATA.value().equals(vendorCode)) {
			CreditBureauData cicdataScore = _uok.pcb.creditBureauDataRepository().getScoreByKey("MARK_"+VendorCodeEnum.CICDATA.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, ConstantTelesale.CRM_CBTYPE, null);
			
			// Check 30 days
			if (cicdataScore != null) {
				SaveScoreObject scoreDetailObj = (SaveScoreObject) JSONFactory.fromJSON(cicdataScore.getCbDataDetail(), SaveScoreObject.class);
				if ("success".equals(scoreDetailObj.getCode()) && !checkAfter30days(cicdataScore.getLastUpdatedDate())) {
					throw new ValidationException("Điểm Khách hàng vẫn còn hiệu lực !");
				}
			}
			
			// Call Scoring
			cicDataResult = (CICDataResponseDTO) this.scoringCICDATA(scoringDTO, ConstantTelesale.CRM, userId);
			
			if ("0".equals(cicDataResult.getReturnCode())) {
				// Save to CreditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.CICDATA.value(), "success", cicDataResult.getCredit_score(), cicDataResult.getReturnMes(), 
						ConstantTelesale.VINAPHONE_TELCOCODE, null,
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
				
				// Update product to upl_customer
				Product product = new Product();
				try {
					ProductInterestDTO p = this.getProdByScore(VendorCodeEnum.CICDATA.value(), ConstantTelesale.VINAPHONE_TELCOCODE, Integer.parseInt(cicDataResult.getCredit_score()));
					product = _uok.telesale.productRepository().getProductBy(p.getProductCode());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				this.updateUplCustomer(scoringDTO.getAppNumber(), product.getId());
			} else {
				String error_code = "cicdata_fail";
				if ("1".equals(cicDataResult.getReturnCode())) {
					error_code ="Invalid phone number";
				} else if ("2".equals(cicDataResult.getReturnCode())) {
					error_code = "Not supported telecom";
				} else if ("3".equals(cicDataResult.getReturnCode())) {
					error_code = "Dupplicate request";
				} else if ("4".equals(cicDataResult.getReturnCode())) {
					error_code = "Invalid OTP";
				}
				
				// Save to CreditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.CICDATA.value(), error_code, cicDataResult.getCredit_score(), cicDataResult.getReturnMes(), 
						ConstantTelesale.VINAPHONE_TELCOCODE, null,
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.CRM_CBTYPE);
			}
			
			this.updateTransIdAsCreditBureauDataId(cicDataResult.getMessageLogId(), creditBureauDataId);
			
			return cicDataResult;
		}
		
		return null;
	}
	
	public Object sendOtpForBPM(SendOTP object) throws Exception {
		TSResponseDTO tsApiResult = null;
		CICDataResponseDTO cicApiResult = null;
		BPMOTPResponse response = null;
		
		if(object.getVendorCode().equals(VendorCodeEnum.TS.value())) {
			// Call send OTP
			tsApiResult = (TSResponseDTO) this.sendOTPTS(object, ConstantTelesale.BPM, null);
			
			if ("success".equals(tsApiResult.getReturnCode())) {
				response = new BPMOTPResponse("success", "Gui thanh cong yeu cau OTP den TS");
				response.setTelcoCode(tsApiResult.getTelcoCode());
				response.setRequestId(tsApiResult.getRequestId());
				response.setTime(_simpleDateFormat.format(new Date()));
				
			} else {
				response = new BPMOTPResponse(tsApiResult.getReturnCode(), tsApiResult.getReturnMes());
				response.setTelcoCode(tsApiResult.getTelcoCode());
				response.setRequestId(tsApiResult.getRequestId());
				response.setTime(_simpleDateFormat.format(new Date()));
				
			}
			
			return response;
			
		} else if(object.getVendorCode().equals(VendorCodeEnum.CICDATA.value())) {
			// Call send OTP
			cicApiResult = (CICDataResponseDTO) this.sendOTPCICData(object, ConstantTelesale.BPM, null);
			
			if ("0".equals(cicApiResult.getReturnCode())) {
				response = new BPMOTPResponse("success", "Gui thanh cong yeu cau OTP den CICData");
				response.setOtp(cicApiResult.getOtp());
				response.setTime(_simpleDateFormat.format(new Date()));
				
			} else {
				response = new BPMOTPResponse("", "");
				response.setTime(_simpleDateFormat.format(new Date()));
				
				if ("1".equals(cicApiResult.getReturnCode())) {
					response.setReturnCode("Invalid phone number");
					response.setReturnMes(cicApiResult.getReturnMes());
				} else if ("2".equals(cicApiResult.getReturnCode())) {
					response.setReturnCode("Not supported telecom");
					response.setReturnMes(cicApiResult.getReturnMes());
				} else {
					response.setReturnCode("CICData fail");
					response.setReturnMes("CICData " + cicApiResult.getReturnMes());
				} 
			}
			
			return response;
		} 
		
		throw new ValidationException("unsupport_vendor_code", "Khong ho tro nha cung cap tren");
	}
	
	public Object scoringBPM (ScoringDTO scoringDTO) throws Exception {
		BPMScoringResponse bpmResponse = new BPMScoringResponse();
		Long creditBureauDataId = null;
		
		// Get score list from Portal If OTP is null
		if (StringUtils.isNullOrEmpty(scoringDTO.getVerificationCode())) {
			
			bpmResponse.setReturnCode("success");
			bpmResponse.setReturnMes("Lay diem thanh cong tu Portal");
			bpmResponse.setScoreList(this.getScoreFromPortal(scoringDTO));
			
			return bpmResponse;
		}
		
		// Get credit bureauData record
		CreditBureauData creditBureauData = _uok.pcb.creditBureauDataRepository()
				.getScoreByKey("MARK_"+scoringDTO.getVendorCode(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, null, "success");
		
		if (creditBureauData != null) {
			// Check 30 days
			SaveScoreObject detailObj = (SaveScoreObject) JSONFactory.fromJSON(creditBureauData.getCbDataDetail(), SaveScoreObject.class);
			if ("success".equals(detailObj.getCode()) && !checkAfter30days(creditBureauData.getLastUpdatedDate())) {
				throw new ValidationException("Ng\u00E0y ch\u1EA5m \u0111i\u1EC3m v\u1EABn c\u00F2n hi\u1EC7u l\u1EF1c !");
			}
		}
		
		// Scoring
		if (VendorCodeEnum.TS.value().equals(scoringDTO.getVendorCode())) {
			TSResponseDTO tsResult = null;
			
			// Call verify TS
			TSResponseDTO verifyResult = this.verifyTS(scoringDTO, ConstantTelesale.BPM, null);
			if(!"success".equals(verifyResult.getReturnCode())) {
				bpmResponse.setReturnCode(verifyResult.getReturnCode());
				bpmResponse.setReturnMes(verifyResult.getReturnMes());
				
				//Save score to creditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.TS.value(), verifyResult.getReturnCode(), verifyResult.getScore(), verifyResult.getReturnMes(), 
						verifyResult.getTelco_code(), verifyResult.getVerify(),
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.BPM_CBTYPE);
				
				this.updateTransIdAsCreditBureauDataId(verifyResult.getMessageLogId(), creditBureauDataId);
				
				// ReGet score list at last
				bpmResponse.setScoreList(this.getScoreFromPortal(scoringDTO));
				
				return bpmResponse;
			}

			// Call to Scoring
			tsResult = (TSResponseDTO) this.scoringTS(scoringDTO, ConstantTelesale.BPM, null);
			
			if ("success".equals(tsResult.getReturnCode()) ) {
				bpmResponse.setReturnCode(tsResult.getReturnCode());
				bpmResponse.setReturnMes("Cham diem thanh cong tu doi tac TS");
			} else {
				bpmResponse.setReturnCode(tsResult.getReturnCode());
				bpmResponse.setReturnMes(tsResult.getReturnMes());
			}
			
			// Save to Score
			creditBureauDataId = this.saveScore(VendorCodeEnum.TS.value(), tsResult.getReturnCode(), tsResult.getScore(), tsResult.getReturnMes(), 
					tsResult.getTelco_code(), tsResult.getVerify(),
					scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.BPM_CBTYPE);
			
			this.updateTransIdAsCreditBureauDataId(tsResult.getMessageLogId(), creditBureauDataId);
			
		} else if (VendorCodeEnum.CICDATA.value().equals(scoringDTO.getVendorCode())) {
			// Call scoring CICDATA
			CICDataResponseDTO cicDataResult = (CICDataResponseDTO) this.scoringCICDATA(scoringDTO, ConstantTelesale.BPM, null);
			
			if ("0".equals(cicDataResult.getReturnCode())) {
				bpmResponse.setReturnCode("success");
				bpmResponse.setReturnMes("Cham diem thanh cong tu doi tac CICData");
				
				// Save score to creditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.CICDATA.value(), "success", cicDataResult.getCredit_score(), cicDataResult.getReturnMes(), // Vinaphone is default for CICData
						ConstantTelesale.VINAPHONE_TELCOCODE, null,	
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.BPM_CBTYPE);
				
			} else {
				if ("1".equals(cicDataResult.getReturnCode())) {
					bpmResponse.setReturnCode("Invalid phone number");
					bpmResponse.setReturnMes(cicDataResult.getReturnMes());
				} else if ("2".equals(cicDataResult.getReturnCode())) {
					bpmResponse.setReturnCode("Not supported telecom");
					bpmResponse.setReturnMes(cicDataResult.getReturnMes());
				} else if ("3".equals(cicDataResult.getReturnCode())) {
					bpmResponse.setReturnCode("Dupplicate request");
					bpmResponse.setReturnMes(cicDataResult.getReturnMes());
				} else if ("4".equals(cicDataResult.getReturnCode())) {
					bpmResponse.setReturnCode("Invalid OTP");
					bpmResponse.setReturnMes(cicDataResult.getReturnMes());
				} else {
					bpmResponse.setReturnCode("CICData fail");
					bpmResponse.setReturnMes("CICData " + cicDataResult.getReturnMes());
				}
				
				// Save score to creditBureauData
				creditBureauDataId = this.saveScore(VendorCodeEnum.CICDATA.value(), bpmResponse.getReturnCode(), cicDataResult.getCredit_score(), cicDataResult.getReturnMes(), // Vinaphone is default for CICData
						ConstantTelesale.VINAPHONE_TELCOCODE, null,	
						scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), ConstantTelesale.BPM_CBTYPE);
			}
			
			this.updateTransIdAsCreditBureauDataId(cicDataResult.getMessageLogId(), creditBureauDataId);
			
		} else {
			throw new ValidationException("unsupport_vendor_code", "Khong ho tro nha cung cap tren");
		}
		
		// ReGet score list at last
		bpmResponse.setScoreList(this.getScoreFromPortal(scoringDTO));
		
		return bpmResponse;
	}
	
	// ===================== ESB Calling =========================
	
	/**
	 * @author kienvt.ho
	 * @return get token from Trusting social
	 * @throws Exception 
	 */	
	private String getTokenTS() throws Exception {
		ApiResult resultApi = null;
		TokenResponse tokenResponseDTO = null;
		MessageLog messageLog = createMessageLog(0, null, null, null, null, 
				null, null, VendorCodeEnum.TS.value(), null, null, null);
		
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_GET_TOKEN_TS, null, ContentType.Json);
			
			// LOG:
			this.updateMessageLog(messageLog, resultApi);
			_uok.common.messageLogRepo().upsert(messageLog);
			System.out.println("[RQ1011 LOG] [ESB_GET_TOKEN_TS] " + messageLog.toString());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (resultApi.getStatus() == true) {
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tokenResponseDTO = (TokenResponse) com.mcredit.data.util.JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringDTO.getResult()), TokenResponse.class);
		} else {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tokenResponseDTO.getAccess_token();
	}
	
	/**
	 * @return get token from redis, If it is't exist get from ts
	 * @throws Exception
	 */
	private String getTokenForTSAPI() throws Exception {
		String token = this.redisAgg.get(this.TOKEN_FOR_TSAPI_REDIS_KEY);
		if (token != null) {
			return token;
		} else {
			token = this.getTokenTS();
			this.redisAgg.set(this.TOKEN_FOR_TSAPI_REDIS_KEY, token, 86000); // TS token will be expired after 24 hours 
		}
		return token;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Send OTP to Trusting social
	 */
	public Object sendOTPTS(SendOTP obj, String caller, Long userId) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		String payload = "{\"phoneNumber\": \"{phoneNumber}\"}";
		payload = payload.replace("{phoneNumber}", obj.getPrimaryPhone());
		
		MessageLog messageLog = createMessageLog(1, payload, null, null, obj.getNationalId()+"-"+obj.getPrimaryPhone(), 
				obj.appNumber, caller, VendorCodeEnum.TS.value(), null, null, userId);
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SEND_OTP_TS, payload, ContentType.Json);
			
			// Parse Result to Object
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tsResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
			
			// LOG:
			this.updateMessageLog(messageLog, resultApi);
			messageLog.setResponsePayloadId(tsResponseDTO.getRequestId());
			_uok.common.messageLogRepo().upsert(messageLog);
			tsResponseDTO.setMessageLogId(messageLog.getId());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (!(resultApi.getStatus() == true || resultApi.getCode() == 400)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tsResponseDTO;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Verify request_id + OTP
	 */
	public TSResponseDTO verifyTS(ScoringDTO obj, String caller, Long userId) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		String payload = "{\"otp\": \"{otp}\",\"request_id\": \"{request_id}\"}";
		payload = payload.replace("{otp}", obj.getVerificationCode());
		payload = payload.replace("{request_id}", obj.getRequestId());
		
		MessageLog messageLog = createMessageLog(2, payload, null, null, obj.getNationalId()+"-"+obj.getPrimaryPhone(), 
				obj.getAppNumber(), caller, VendorCodeEnum.TS.value(), null, null, userId);
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_VERIFY_TS, payload, ContentType.Json);
			
			// Parse Result to Object
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tsResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
			
			// LOG
			this.updateMessageLog(messageLog, resultApi);
			this.saveMessageLog(messageLog);
			tsResponseDTO.setMessageLogId(messageLog.getId());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		return tsResponseDTO;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Call Scoring TS
	 * @throws Exception 
	 */
	public Object scoringTS(ScoringDTO obj, String call_system, Long userId) throws Exception {
		ApiResult resultApi = null;
		TSResponseDTO tsResponseDTO = null;
		String token = this.getTokenForTSAPI();
		
		String payload = "{\"phoneNumber\": \"{phoneNumber}\",\"identityNumber\": \"{identityNumber}\"}";
		payload = payload.replace("{phoneNumber}", obj.getPrimaryPhone());
		payload = payload.replace("{identityNumber}", obj.getNationalId());
		
		MessageLog messageLog = createMessageLog(3, payload, null, null, obj.getNationalId()+"-"+obj.getPrimaryPhone(), 
				obj.getAppNumber(), call_system, "TS", null, null, userId);
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SCORING_TS, payload, ContentType.Json);
			
			// Parse Result to Object
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tsResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
			
			// Log message
			this.updateMessageLog(messageLog, resultApi);
			messageLog.setResponsePayloadId(tsResponseDTO.getRequest_id());
			this.saveMessageLog(messageLog);
			tsResponseDTO.setMessageLogId(messageLog.getId());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (!(resultApi.getStatus() == true || resultApi.getCode() == 400)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tsResponseDTO;
	}
	
	/**
	 * @author kienvt.ho
	 * @return get token from CICData
	 * @throws Exception 
	 */	
	private String getTokenCICData() throws Exception {
		ApiResult resultApi = null;
		TokenResponse tokenResponseDTO = null;
		MessageLog messageLog = createMessageLog(0, null, null, null, null, 
				null, null, "CID", null, null, null);
		
		try (BasedHttpClient bs = new BasedHttpClient()) {
			resultApi = bs.doGet(this._esbHost + BusinessConstant.ESB_GET_TOKEN_CICDATA);
			
			// LOG:
			this.updateMessageLog(messageLog, resultApi);
			_uok.common.messageLogRepo().upsert(messageLog);
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (resultApi.getStatus() == true) {
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			tokenResponseDTO = (TokenResponse) com.mcredit.data.util.JSONFactory.fromJSON(com.mcredit.data.util.JSONFactory.toJson(scoringDTO.getResult()), TokenResponse.class);
		} else {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return tokenResponseDTO.getAccess_token();
	}
	
	/**
	 * @return get token from redis, If it is't exist get from CICData
	 * @throws Exception
	 */
	private String getTokenForCICDataAPI() throws Exception {
		String token = this.redisAgg.get(this.TOKEN_FOR_CICDATAAPI_REDIS_KEY);
		if (token != null) {
			return token;
		} else {
			token = this.getTokenCICData();
			this.redisAgg.set(this.TOKEN_FOR_CICDATAAPI_REDIS_KEY, token, 1800); // CICData will be expired after 30 minutes
		}
		return token;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Send OTP to CICData
	 */
	public Object sendOTPCICData(SendOTP dto, String call_system, Long userId) throws Exception {
		ApiResult resultApi = null;
		CICDataResponseDTO cicResponseDTO = null;
		String token = this.getTokenForCICDataAPI();
		
		String payload = "{\"phoneNumber\": \"{phoneNumber}\"}";
		payload = payload.replace("{phoneNumber}", dto.getPrimaryPhone());
		
		MessageLog messageLog = createMessageLog(1, payload, null, null, dto.getNationalId()+"-"+dto.getPrimaryPhone(), 
				dto.getAppNumber(), call_system, "CID", null, null, userId);
		
		// Call to CICData
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SEND_OTP_CICDATA, payload, ContentType.Json);
			
			// Parse Result to Object
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			cicResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), CICDataResponseDTO.class);
			
			// LOG:
			this.updateMessageLog(messageLog, resultApi);
			messageLog.setResponsePayloadId(cicResponseDTO.getId());
			this.saveMessageLog(messageLog);
			cicResponseDTO.setMessageLogId(messageLog.getId());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (!(resultApi.getStatus() == true)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return cicResponseDTO;
	}
	
	/**
	 * @author kienvt.ho
	 * @return Call Scoring CICDATA
	 * @throws Exception 
	 */
	public Object scoringCICDATA(ScoringDTO obj, String call_system, Long userId) throws Exception {
		ApiResult resultApi = null;
		CICDataResponseDTO cicResponseDTO = null;
		String token = this.getTokenForCICDataAPI();
		String payload = "{\"phoneNumber\":\"{phoneNumber}\",\"otp\":\"{otp}\"}";
		payload = payload.replace("{phoneNumber}", obj.getPrimaryPhone());
		payload = payload.replace("{otp}", obj.getVerificationCode());
		
		MessageLog messageLog = createMessageLog(1, payload, null, null, obj.getNationalId()+"-"+obj.getPrimaryPhone(), 
				obj.getAppNumber(), call_system, "CID", null, null, userId);
		
		// Call to TS
		try (BasedHttpClient bs = new BasedHttpClient(this.BEARER_TOKEN_TYPE, token)) {
			resultApi = bs.doPost(this._esbHost + BusinessConstant.ESB_SCORING_CICDATA, payload, ContentType.Json);
			
			// Parse Result to Object
			ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
			cicResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), CICDataResponseDTO.class);
			
			// Log message
			this.updateMessageLog(messageLog, resultApi);
			messageLog.setResponsePayloadId(cicResponseDTO.getId());
			this.saveMessageLog(messageLog);
			cicResponseDTO.setMessageLogId(messageLog.getId());
		} catch (Exception e) {
			throw new Exception("Kết nối ESB không khả dụng: " +  e.getMessage());
		}
		
		if (!(resultApi.getStatus() == true || resultApi.getCode() == 400)) {
			throw new Exception(resultApi.getBodyContent());
		}
		
		return cicResponseDTO;
	} 
	
	// =================== Private Function ====================
	
	private MessageLog createMessageLog(Integer msgOrder, String msgRequest, String msgResponse, String msgStatus, String relationId, 
			String serviceName, String fromChannel, String toChannel, Long transId, String responsePayloadId,
			Long userId) {
		Date now = new Date();
		Timestamp atTheMoment = new Timestamp(now.getTime());
		/**
		 * msgStatus: S (Success), E (Error)
		 * */
		MessageLog messageLog = new MessageLog(fromChannel, msgOrder, msgRequest, msgStatus, "R",
				atTheMoment, relationId, serviceName, toChannel, transId, 
				"CHECK_SCORE", userId);
		messageLog.setResponsePayloadId(responsePayloadId);
		messageLog.setResponseTime(atTheMoment);
		messageLog.setMsgResponse(msgResponse);
		
		return messageLog;
	}
	
	private MessageLog updateMessageLog(MessageLog messageLog, ApiResult resultApi) {
		messageLog.setResponseTime(new Timestamp((new Date()).getTime()));
		messageLog.setResponseCode(String.valueOf(resultApi.getCode()));
		messageLog.setMsgResponse(resultApi.getBodyContent());
		if(resultApi.getStatus() || resultApi.getCode() == 400) {
			messageLog.setMsgStatus("S");
		} else {
			messageLog.setMsgStatus("E");
		}
		ScoringResponse scoringDTO = (ScoringResponse) com.mcredit.data.util.JSONFactory.fromJSON(resultApi.getBodyContent().replace("\\", ""), ScoringResponse.class);
		TSResponseDTO tsResponseDTO = TeamAggregate.modelMapper.map(scoringDTO.getResult(), TSResponseDTO.class);
		messageLog.setResponsePayloadId(tsResponseDTO.getRequest_id());
		
		return messageLog;
	}
	
	private boolean saveMessageLog(MessageLog messageLog) {
		_uok.common.messageLogRepo().upsert(messageLog);
		return true;
	}
	
	private Long saveOTPInfo(String responseCode, String vendorCode, String requestID, String telcoCode, 
			String identityNumber, String phoneNumber, String source) {
		
		CreditBureauData creditBureauData = new CreditBureauData();
		
		creditBureauData.setRecordStatus("A");
		creditBureauData.setCreatedDate(new Date());
		creditBureauData.setLastUpdatedDate(new Date());
		creditBureauData.setBirthDate(new Date());
		creditBureauData.setCbSource(ConstantTelesale.SCORE_CB_SOURCE);
		creditBureauData.setCbType(source);
		creditBureauData.setCbkey("OTP");
		creditBureauData.setCbCode(responseCode);
		
		SaveOTPObject detailObj = new SaveOTPObject(responseCode, vendorCode, requestID, telcoCode);
		creditBureauData.setCbDataDetail(JSONFactory.toJson(detailObj));
		creditBureauData.setCustIdentityNumber(identityNumber.trim());
		creditBureauData.setCustMobile(phoneNumber.trim());
		
		_uok.pcb.creditBureauDataRepository().upsert(creditBureauData);
		
		return creditBureauData.getId();
	}
	
	private Long saveScore(String vendorCode, String code, String score, String message, String telcoCode, String verifyInfo,
			String identityNumber, String phoneNumber, String source_cbType) {
		Date now = new Date();
		
		// Create new
		CreditBureauData creditBureauData = new CreditBureauData(now, now, null, null, null, null, null, null);
		
		creditBureauData.setCbkey("MARK_" + vendorCode);
		creditBureauData.setCbSource(ConstantTelesale.SCORE_CB_SOURCE);
		creditBureauData.setCbType(source_cbType);
		creditBureauData.setBirthDate(now);
		SaveScoreObject saveScoreObj = new SaveScoreObject(code, score, message, telcoCode, verifyInfo);
		creditBureauData.setCbDataDetail(JSONFactory.toJson(saveScoreObj));
		creditBureauData.setCustIdentityNumber(identityNumber.trim());
		creditBureauData.setCustMobile(phoneNumber.trim());
		creditBureauData.setCbCode(code);
		
		_uok.pcb.creditBureauDataRepository().upsert(creditBureauData);
		
		return creditBureauData.getId();
	}
	
	private List<BPMScoringObject> getScoreFromPortal(ScoringDTO scoringDTO) {
		List<BPMScoringObject> scoreList = new ArrayList<BPMScoringObject>();
		
		// Get score TS
		CreditBureauData scoreTS = null;
		CreditBureauData scoreTSContainError = null;
		if (!StringUtils.isNullOrEmpty(scoringDTO.getNationalIdOld())) {
			scoreTS = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.TS.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), scoringDTO.getNationalIdOld(), null, "success");
			scoreTSContainError = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.TS.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), scoringDTO.getNationalIdOld(), null, null);
		} else {
			scoreTS = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.TS.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, null, "success");
			scoreTSContainError = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.TS.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, null, null);
		}
		
		if (scoreTS != null) {
			SaveScoreObject detailObj = (SaveScoreObject) JSONFactory.fromJSON(scoreTS.getCbDataDetail(), SaveScoreObject.class);
		
			BPMScoringObject score = new BPMScoringObject(VendorCodeEnum.TS.value(), detailObj.getScore(), 800, _simpleDateFormat.format(scoreTS.getLastUpdatedDate()));
			score.setTelcoCode(detailObj.getTelcoCode());
			score.setVerifyInfo(detailObj.getVerifyInfo());
			
			// Check 30 days
			if (checkAfter30days(scoreTS.getLastUpdatedDate())) {
				score.setStatus(801);
			}
			
			scoreList.add(score);
		} else {
			if (scoreTSContainError != null) {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.TS.value(), "", 803, null));
			} else {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.TS.value(), "", 802, null));
			}
		}
		
		// Get score CICData
		CreditBureauData scoreCICData = null;
		CreditBureauData scoreCICDataContainError = null;
		if (!StringUtils.isNullOrEmpty(scoringDTO.getNationalIdOld())) {
			scoreCICData = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.CICDATA.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), scoringDTO.getNationalIdOld(), null, "success");
			scoreCICDataContainError = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.CICDATA.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), scoringDTO.getNationalIdOld(), null, null);
		} else {
			scoreCICData = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.CICDATA.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, null, "success");
			scoreCICDataContainError = _uok.pcb.creditBureauDataRepository()
					.getScoreByKey("MARK_"+VendorCodeEnum.CICDATA.value(), scoringDTO.getNationalId(), scoringDTO.getPrimaryPhone(), null, null, null);
		}
		
		if (scoreCICData != null) {
			SaveScoreObject detailObj = (SaveScoreObject) JSONFactory.fromJSON(scoreCICData.getCbDataDetail(), SaveScoreObject.class);
			
			// Check 30 days
			if (!checkAfter30days(scoreCICData.getLastUpdatedDate())) {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.CICDATA.value(), detailObj.getScore(), 800, _simpleDateFormat.format(scoreCICData.getLastUpdatedDate())));
			} else {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.CICDATA.value(), detailObj.getScore(), 801, _simpleDateFormat.format(scoreCICData.getLastUpdatedDate())));
			}
		} else {
			if (scoreCICDataContainError != null) {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.CICDATA.value(), "", 803, null));
			} else {
				scoreList.add(new BPMScoringObject(VendorCodeEnum.CICDATA.value(), "", 802, null));
			}
			
		}
		
		return scoreList;
	}
	// RQ1011 END
	
	// RQ1015
	public Object rejectScoringCustomer(String identityNumber, String phoneNumber) {
		CreditBureauData creditBureauData = new CreditBureauData();
		
		creditBureauData.setRecordStatus("A");
		creditBureauData.setCreatedDate(new Date());
		creditBureauData.setLastUpdatedDate(new Date());
		creditBureauData.setBirthDate(new Date());
		creditBureauData.setCbSource("T");
		creditBureauData.setCbType("C");
		creditBureauData.setCbkey("OTP");
		
		SaveOTPObject detailObj = new SaveOTPObject("reject", "", "", "");
		creditBureauData.setCbDataDetail(JSONFactory.toJson(detailObj));
		creditBureauData.setCustIdentityNumber(identityNumber.trim());
		creditBureauData.setCustMobile(phoneNumber.trim());
	
		_uok.pcb.creditBureauDataRepository().upsert(creditBureauData);
		
		return null;
	}
	
	public Object getUplCustForScoring (Long upl_cust_id) {
		UplCustomerScoring2DTO result = _uok.telesale.teamRepo().getUplCustForScoring(upl_cust_id);
		
		return result;
	}
	// RQ1015 END
	
	private String getTelcoCodeFromText (String text) {
		String temp = text.toLowerCase();
		
		if (temp.equals(ConstantTelesale.VIETTEL_TELCOCODE.toLowerCase()))
			return ConstantTelesale.VIETTEL_TELCOCODE;
		
		if (temp.equals(ConstantTelesale.VINAPHONE_TELCOCODE.toLowerCase()) || temp.equals(ConstantTelesale.VINAPHONE_TELCOCODE2))
			return ConstantTelesale.VINAPHONE_TELCOCODE;
		
		if (temp.equals(ConstantTelesale.MOBIFONE_TELCOCODE.toLowerCase()))
			return ConstantTelesale.MOBIFONE_TELCOCODE;
		
		return text;
	}
	
	private String getPartnerFromText(String text) {
		String temp = text.toLowerCase();
		
		if (temp.equals(ConstantTelesale.CICDATA_PARTNER.toLowerCase()))
			return ConstantTelesale.CICDATA_PARTNER;
		
		if (temp.equals(ConstantTelesale.TS_PARTNER.toLowerCase()))
			return ConstantTelesale.TS_PARTNER;
		
		return text;
	}
	
	private void updateTransIdAsCreditBureauDataId (Long messageLogId, Long creditBureauDataId) {
		if (messageLogId == null || creditBureauDataId == null)	return;
		
		MessageLog messageLog = _uok.common.messageLogRepo().getMessageByMessId(messageLogId);
		if (messageLog == null) return;
		
		messageLog.setTransId(creditBureauDataId);
		_uok.common.messageLogRepo().upsert(messageLog);
	}
	
	private static boolean checkAfter30days(Date checker) {
		// add 30days
		checker = DateUtil.addDay(checker, 30);
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(checker);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		
		if (calendar2.after(calendar1)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void updateUplCustomer(String uplCustomerId, Long productId) throws ValidationException {
		UplCustomer uplCustomer = _uok.telesale.uplCustomerRepo().findById(Long.parseLong(uplCustomerId));
		if (uplCustomer == null) {
			throw new ValidationException("UplCustomerID khong ton tai !");
		}
		
		uplCustomer.setProductId(productId);
		
		_uok.telesale.uplCustomerRepo().upsert(uplCustomer);
	}
	
	public Object checkRenew(Long idUplCus) throws Exception {
		return _uok.telesale.uplCustomerRepo().checkRenew(idUplCus);
	}
}
