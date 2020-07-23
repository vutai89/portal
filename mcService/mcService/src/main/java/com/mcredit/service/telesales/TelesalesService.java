/**
 * 
 */
package com.mcredit.service.telesales;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.ap.manager.AssignPermissionsManager;
import com.mcredit.business.telesales.dto.UplCustomerHistoryDTO;
import com.mcredit.business.telesales.manager.SupervisorManager;
import com.mcredit.business.telesales.manager.TelesalesManager;
import com.mcredit.business.telesales.payload.ScoringDTO;
import com.mcredit.business.telesales.payload.ScoringCRM;
import com.mcredit.business.telesales.payload.ScoringPayload;
import com.mcredit.business.telesales.payload.ScoringTS;
import com.mcredit.business.telesales.payload.SendOTP;
import com.mcredit.model.dto.assign.SearchUserTsaDTO;
import com.mcredit.model.dto.telesales.ActiveUserTsaDTO;
import com.mcredit.model.dto.telesales.AllocationCustomerDTO;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;
import com.mcredit.util.StringUtils;

@Path("/v1.0/telesales")
public class TelesalesService extends BasedService {

	public TelesalesService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/campaigns")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActiveCampaigns(@QueryParam("step") String step, @QueryParam("asm") String asm,
			@QueryParam("xsm") String xsm, @QueryParam("ntb") String ntb,
			@QueryParam("qfpm") String queryForProspectManagement, @QueryParam("tsaLst") String tsaLst)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Campaigns, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getActiveCampaigns(StringUtils.nullToEmpty(step), this.currentUser.getId(),
						StringUtils.nullToEmpty(asm), StringUtils.nullToEmpty(xsm), StringUtils.nullToEmpty(ntb),
						StringUtils.nullToEmpty(queryForProspectManagement), StringUtils.nullToEmpty(tsaLst)));
			}
		});
	}

	@GET
	@Path("/queryUploadMaster")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryUploadMaster(@QueryParam("uplCode") String uplCode,
			// @QueryParam("uplSource") String uplSource,
			@QueryParam("uplType") String uplType, @QueryParam("uplSequence") String uplSequence,
			@QueryParam("uplOwner") String uplOwner, @QueryParam("asm") String asm, @QueryParam("xsm") String xsm)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_QueryUploadMaster, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.queryUploadMaster(uplCode, uplType, uplSequence, uplOwner,
						StringUtils.nullToEmpty(asm), StringUtils.nullToEmpty(xsm)));
			}
		});
	}

	@GET
	@Path("/queryAllocationDetail")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response queryAllocationDetail(@QueryParam("uplCode") String uplCode) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_QueryAllocationDetail, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.queryAllocationDetail(uplCode, this.currentUser.getId()));
			}
		});
	}

	@GET
	@Path("/findCustomerBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCustomerBy(@QueryParam("tsaCode") String tsaCode,
			@QueryParam("identityNumber") String identityNumber, @QueryParam("mobile") String mobile,
			@QueryParam("sendDateFrom") String sendDateFrom, @QueryParam("sendDateTo") String sendDateTo)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_QueryAllocationDetail, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.lstUplCusByTSACode(tsaCode, identityNumber, mobile, sendDateFrom, sendDateTo,
						this.currentUser.getLoginId()));
			}
		});
	}

	@GET
	@Path("/findCustomerSupervisor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCustomerSupervisor(@QueryParam("tsaCode") String tsaCode,
			@QueryParam("identityNumber") String identityNumber, @QueryParam("mobile") String mobile,
			@QueryParam("sendDateFrom") String sendDateFrom, @QueryParam("sendDateTo") String sendDateTo)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_QueryAllocationDetail, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.findCustomerSupervisor(tsaCode, identityNumber, mobile, sendDateFrom, sendDateTo,
						this.currentUser.getLoginId()));
			}
		});
	}

	@POST
	@Path("/allocationCustomer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response allocationCustomer(List<AllocationCustomerDTO> request, @QueryParam("asm") String asm,
			@QueryParam("xsm") String xsm) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Telesales_AllocationCustomer, () -> {
			try (SupervisorManager manager = new SupervisorManager()) {
				return ok(manager.allocationCustomer(request, this.currentUser, StringUtils.nullToEmpty(asm),
						StringUtils.nullToEmpty(xsm)));
			}
		});
	}

	@GET
	@Path("/teams")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTeams(@QueryParam("teamType") String teamType, @QueryParam("teamGroup") String teamGroup,
			@QueryParam("asm") String asm) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Teams, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getActiveTeams(StringUtils.nullToEmpty(this.currentUser.getEmpId()),
						StringUtils.nullToEmpty(teamGroup), StringUtils.nullToEmpty(asm)));
			}
		});
	}

	@GET
	@Path("/activeTeams")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activeTeams(@QueryParam("asm") String asm, @QueryParam("xsm") String xsm,
			@QueryParam("ntb") String ntb) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Teams, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getAllActiveTeams(StringUtils.nullToEmpty(asm), StringUtils.nullToEmpty(xsm),
						StringUtils.nullToEmpty(ntb)));
			}
		});
	}

	@GET
	@Path("/team/member")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTeamMember(@QueryParam("userId") Long userId, @QueryParam("type") String type,
			@QueryParam("findType") String findType, @QueryParam("campaignType") String campaignType) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Member, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getTeamMembers(userId, type, findType, campaignType));
			}
		});
	}

	@GET
	@Path("/team/member-allocated-number")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTeamMemberWithAllocatedNumber(@QueryParam("memberId") Long memberId,
			@QueryParam("prospectStatus") String prospectStatus, @QueryParam("campaignId") Long campaignId,
			@QueryParam("callStatus") Long callStatus, @QueryParam("callResult") Long callResult,
			@QueryParam("identityNumber") String identityNumber, @QueryParam("receiveDate") String receiveDate)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Member, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getTeamMembersAllocatedNumber(memberId, prospectStatus, campaignId, callStatus,
						callResult, identityNumber, receiveDate));
			}
		});
	}

	@GET
	@Path("/team/leader")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTeamLeaderBy(@QueryParam("memberId") Long memberId, @QueryParam("type") String type)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Leader, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getTeamLeaderBy(memberId));
			}
		});
	}

	@GET
	@Path("/team/isteamlead")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkIsTeamlead() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Isteamlead, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.checkIsTeamlead(this.currentUser.getId()));
			}
		});
	}

	
	/**
	 * @deprecated no longer used this from RQ1011
	 * @param object
	 * @return 
	 */
	@POST
	@Path("/send_otp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendOtp(SendOTP object) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Isteamlead, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.sendOtp(object));
			}
		});
	}

	/**
	 * @deprecated no longer used this from RQ1011
	 * @param scoringPayload
	 * @return
	 */
	@POST
	@Path("/scoring_ts")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoringTs(ScoringPayload scoringPayload) throws Exception {
		try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
			return ok(manager.scoringTs(scoringPayload));
		}
	}
	
	/**
	 * @return send OTP to customer
	 * @author kienvt.ho
	 */
	@POST
	@Path("/send_otp_v2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendOtp2(SendOTP object) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Isteamlead, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.sendOtpV2(object));
			}
		});
	}
	
	/**
	 * @return scoring to TS/CICdata
	 * @author kienvt.ho
	 */
	@POST
	@Path("/scoring_CRM_v2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoringTsV2(ScoringDTO scoringDTO) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Team_Member, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.scoringTsV2(scoringDTO));
			}
		});
	}
	
	/**
	 * @return send OTP for BPM site
	 * @author kienvt.ho
	 */
	@POST
	@Path("/send_otp_bpm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendOtpBPM(SendOTP object) throws Exception {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.sendOtpBPM(object));
			}
	}
	
	/**
	 * @return scoring for BPM site
	 * @author kienvt.ho
	 */
	@POST
	@Path("/scoring_bpm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoringBPM(ScoringDTO scoringBPM) throws Exception {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.scoringBPM(scoringBPM));
			}
	}

	/**
	 * @return reject scoring customer on CRM
	 * @author kienvt.ho
	 */
	@POST
	@Path("/reject_scoring_customer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rejectScoringCustomer(@QueryParam("identityNumber") String identityNumber, @QueryParam("phoneNumber") String phoneNumber) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.rejectScoringCustomer(identityNumber, phoneNumber));
			}
		});
	}
	
	/**
	 * @return reject scoring customer on CRM
	 * @author kienvt.ho
	 */
	@GET
	@Path("/get_uplcust_for_scoring/{upl_cust_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUplCustForScoring(@PathParam("upl_cust_id") Long upl_cust_id) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getUplCustForScoring(upl_cust_id));
			}
		});
	}

	@POST
	@Path("/update_upl_cus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCusHis(ScoringTS object) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.updateUplCus(object));
			}
		});
	}

	@GET
	@Path("/get_cus_his")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCusHis(@QueryParam("cusId") Long cusId, @QueryParam("refId") String refId) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getCusHis(cusId, refId));
			}
		});
	}

	@GET
	@Path("/get_upl_cus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUplCus(@QueryParam("id") Long id) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getUplCus(id));
			}
		});
	}

	@GET
	@Path("/check_cus_ntb")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCustomerNTB(@QueryParam("idUplCus") Long idUplCus) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.checkCustomerNTB(idUplCus));
			}
		});
	}

	@GET
	@Path("/check_send_otp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkSendOtp(@QueryParam("idCus") Long idCus) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.checkSendOtp(idCus));
			}
		});
	}

	@POST
	@Path("/scoringTsApi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response scoringApi(ScoringPayload payload) throws Exception {
		try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
			return ok(manager.scoringApi(payload.getPrimaryPhone(), payload.getNationalId(), payload.getVerificationCode(), payload.getAppNumber(), payload.getNationalIdOld()));
		}
	}

	@GET
	@Path("/getProdByScore")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProdByScore(@QueryParam("partner") String partner, @QueryParam("telcoCode") String telcoCode, @QueryParam("score") Integer score) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getProdByScore(partner, telcoCode, score));
			}
		});
	}

	@GET
	@Path("/getRoleCodeUserTls")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoleCodeUserTls() throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getRoleCodeUserTls());
			}
		});
	}

	@GET
	@Path("/getMemberNTB")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemberNTB() throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getMemberNTB());
			}
		});
	}

	@GET
	@Path("/getMemberByTeamLead")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMemberByTeamLead() throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getMemberByTeamLead());
			}
		});
	}
	
	/**
	 * @author catld.ho
	 * Create new case from mini CRM
	 * @return 200 if success
	 * @throws Exception
	 */
	@POST
	@Path("/create-case")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createCaseFromCRM(UploadCaseDTO newCase) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.createCaseFromCRM(newCase));
			}
		});
	}
	
	/**
	 * Get list hub/kios
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/kiosks")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getShops() throws Exception {

		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.geKiosks());
			}
		});
	}
	
	/**
	 * Get range loanTenor, loanAmount of product
	 * @param productCode
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/products-info")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductsInfo(@QueryParam("productCode") String productCode) throws Exception {

		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getProductInfo(productCode));
			}
		});
	}
	
	/**
	 * Get range LG product (RQ968)
	 * @author kienvt.ho
	 */
	@GET
	@Path("/getScoreLG")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getScoreLG(@QueryParam("mobilePhone1") String mobilePhone1, 
			@QueryParam("mobilePhone2") String mobilePhone2,
			@QueryParam("productCode") String productCode) throws Exception {

			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.getScoreLG(mobilePhone1, mobilePhone2, productCode));
			}
	}

	/**
	 * Inactive user TSA
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/inactive-user-tsa")
	@Produces(MediaType.APPLICATION_JSON)
	public Response inactiveUserTSA(ActiveUserTsaDTO activePayload) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Telesales_Inactive_User_Tsa, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.inactiveUserTsa(activePayload));
			}
		});
	}
	
	/**
	 * Active user TSA
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/active-user-tsa")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activeUserTSA(ActiveUserTsaDTO activePayload) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Telesales_Active_User_Tsa, () -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.activeUserTsa(activePayload));
			}
		});
	}
	
	/**
	 * Service tim kiem va hien thi users tsa
	 * 
	 * @author anhbv.ho
	 * @param
	 * @return return users tsa
	 * @throws Exception
	 */
	@GET
	@Path("/search-users-tsa")
	@Produces(MediaType.APPLICATION_JSON)
	public Response searchUsersTsa(@QueryParam("keyword") String keyword, @QueryParam("pageNumber") Integer pageNumber,
			@QueryParam("pageSize") Integer pageSize) throws Exception {	
		return this.authorize(ServiceName.GET_V1_0_Telesales_Search_User_Tsa, () -> {
			try (AssignPermissionsManager manager = new AssignPermissionsManager()) {
				SearchUserTsaDTO searchUserTsaDTO = new SearchUserTsaDTO(keyword, pageNumber, pageSize);
				return ok(manager.searchUsersTsa(searchUserTsaDTO));
			}
		});
	}
	
	@GET
	@Path("/check_renew")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkRenew(@QueryParam("idUplCus") Long idUplCus) throws Exception {
		return this.authorize(() -> {
			try (TelesalesManager manager = new TelesalesManager(this.currentUser)) {
				return ok(manager.checkRenew(idUplCus));
			}
		});
	}
}
