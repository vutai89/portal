/**
 * 
 */
package com.mcredit.service.telesales;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.telesales.manager.ProspectManagerManager;
import com.mcredit.business.telesales.manager.UploadManager;
import com.mcredit.business.telesales.object.ProspectSearch;
import com.mcredit.model.dto.telesales.CallResultDTO;
import com.mcredit.model.dto.telesales.CustProspectDTO;
import com.mcredit.model.dto.telesales.ProspectReAssignDTO;
import com.mcredit.model.dto.telesales.UploadFileDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.BusinessLogs;
import com.mcredit.util.StringUtils;
import com.sun.jersey.multipart.FormDataParam;

/**
 * @author anhdv.ho
 *
 */
@Path("/v1.0/telesales/prospect")
public class ProspectService extends BasedService {
	
	public ProspectService(@Context HttpHeaders headers){
		super(headers);
	}
	
	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findProspect(@QueryParam("uplMasterId") String uplMasterId, @QueryParam("mobile") String mobile, @QueryParam("nextAction") String nextAction
			, @QueryParam("callResult") String callResult, @QueryParam("custName") String custName, @QueryParam("fromDate") String fromDate
			, @QueryParam("toDate") String toDate, @QueryParam("callStatus") String callStatus, @QueryParam("prospectStatus") String prospectStatus
			, @QueryParam("identityNumber") String identityNumber , @QueryParam("page") String page, @QueryParam("rowPerPage") String rowPerPage
			, @QueryParam("userId") String userId, @QueryParam("isSuperVisor") boolean isSuperVisor, @QueryParam("asm") String asm
			, @QueryParam("isTeamLead") boolean isTeamLead, @QueryParam("provinceId") String provinceId, @QueryParam("campaignCodeLst") String campaignCodeLstOfSup
			, @QueryParam("productName") String productName, @QueryParam("dataSource") String dataSource
			, @QueryParam("leadSource") String leadSource, @QueryParam("tsaReceiveId") Long tsaReceiveId, @QueryParam("isMark") Integer isMark
			, @QueryParam("resultOTP") Integer resultOTP, @QueryParam("resultTS") Integer resultTS, @QueryParam("provinceTextList") String provinceTextList
			, @QueryParam("newMobile") String newMobile)
					throws Exception {
		
		return this.authorize(ServiceName.GET_V1_0_Telesales_Prospect,()->{
			try( ProspectManagerManager manager = new ProspectManagerManager(this.currentUser) ) {
				return ok(manager.findProspect(new ProspectSearch(
												uplMasterId, mobile, nextAction , callResult, custName, fromDate, toDate, callStatus
												, prospectStatus, identityNumber, page, rowPerPage, userId, isSuperVisor, isTeamLead, provinceId
												, productName, dataSource, leadSource, tsaReceiveId, isMark, resultOTP, resultTS, provinceTextList
												, newMobile
						), StringUtils.nullToEmpty(campaignCodeLstOfSup)));
			}
		});
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	// FIXME: Change custProspectId to uplCustomerId Note: Update permision service
	@Path("/call/{custProspectId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCallResult(@PathParam("custProspectId") Long custProspectId) throws Exception{
		return this.authorize(ServiceName.GET_V1_0_Telesales_Prospect_Call_CustProspectId,() -> {
			try (ProspectManagerManager manager = new ProspectManagerManager(this.currentUser)){
				return ok(manager.findCallResult(custProspectId));
			}
		});
	}
	
	
	@POST
	@Path("/call")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insertCallResult(CallResultDTO CallResult) throws Exception{
		return this.authorize(ServiceName.POST_V1_0_Telesales_Prospect_Call,() -> {
			try (ProspectManagerManager manager = new ProspectManagerManager(this.currentUser)){
				return ok(manager.insertProspectCall(CallResult));
			}
		});
	}
	
	@GET
	@Path("/customer/permission/{uplCusId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response hasPermissionOnProspectCallingPage(@PathParam("uplCusId") Long uplCusId) throws Exception{
		return this.authorize(ServiceName.GET_V1_0_Telesales_Prospect_Customer_Permission_UplCusId,() -> {
			try (ProspectManagerManager manager = new ProspectManagerManager(this.currentUser)){
				return ok(manager.hasPermissionOnProspectCallingPage(uplCusId));
			}
		});
	}
	
	@PUT
	@Path("/customer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upsertCustProspect(CustProspectDTO CPDTO) throws Exception{
		return this.authorize(ServiceName.PUT_V1_0_Telesales_Prospect_Customer,() -> {
			try (ProspectManagerManager manager = new ProspectManagerManager(this.currentUser)){
				return ok(manager.upsertCustomerProspect(CPDTO));
			}
		});
	}
	
	@GET
	@Path("/customer/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustProspect(@PathParam("id") Long Id) throws Exception{
		return this.authorize(ServiceName.GET_V1_0_Telesales_Prospect_Customer_Id,() -> {
			try (ProspectManagerManager manager = new ProspectManagerManager(this.currentUser)){
				return ok(manager.getCustProspectByUplCusId(Id));
			}
		});
	}
	

	
	@POST
	@Path("/import")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response importProspect(
            @FormDataParam("userFileName") String userFileName,
            @FormDataParam("uplCode") String uplCode,
            @FormDataParam("uplType") String uplType,
            @FormDataParam("ownerId") Long ownerId,
            @FormDataParam("fileContent") InputStream fileContent,
            @FormDataParam("mbMis") String mbMis
//			BufferedInMultiPart bimp
			) throws Exception{

		return this.authorize(ServiceName.POST_V1_0_Telesales_Prospect_Import,()->{
			try (UploadManager manager = new UploadManager(this.currentUser)){
				UploadFileDTO UFDTO = new UploadFileDTO();
				UFDTO.setUserFileName(userFileName);
				UFDTO.setFileContent(fileContent);
				UFDTO.setUplCode(uplCode);
				UFDTO.setUplType(uplType);
				UFDTO.setOwnerId(ownerId);
				UFDTO.setMbMis(mbMis);
				return accepted(manager.importCusomterProspect(UFDTO));
			}
		});
	}
	


	@GET
	@Path("/import")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkImportStatusByUser() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Telesales_Prospect_Import,()->{
			try (UploadManager manager = new UploadManager(this.currentUser)){
				return ok(manager.importStatus());
			}
		});
	}

	@PUT
	@Path("/import/{uplDetailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmImport(@PathParam("uplDetailId") Long uplDetailId) throws Exception {
		
		return this.authorize(ServiceName.PUT_V1_0_Telesales_Prospect_Import_UplDetailId,()->{
			
			try (UploadManager manager = new UploadManager(this.currentUser)){
				
				Object response = manager.confirmUpload(uplDetailId);
				
				this.updateData(uplDetailId);
				
				return ok(response);
			}
		});
	}
	
	private void updateData(Long uplDetailId) throws Exception {
		
		try (UploadManager manager = new UploadManager(this.currentUser)){
			
			manager.updateData(uplDetailId);
		}
	}

	@DELETE
	@Path("/import/{uplDetailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteImport(@PathParam("uplDetailId") Long uplDetailId ) throws Exception {
		
		return this.authorize(ServiceName.DELETE_V1_0_Telesales_Prospect_Import_UplDetailId,()->{
			
			try (UploadManager manager = new UploadManager(this.currentUser)){
				
				Object response = manager.deleteUpload(uplDetailId);
				
				this.updateData(uplDetailId);
				
				return ok(response);
			}
		});
	}

	
	@PUT
	@Path("/reassign")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response prospectReAssignNew(ProspectReAssignDTO request) throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_Telesales_Prospect_Reassign,()->{
			try( ProspectManagerManager manager = new ProspectManagerManager(this.currentUser) ) {
				return ok(manager.prospectReAssign(request));
			}
		});
	}
	
	@GET
	@Path("/getManagerByLoginId")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getManagerByLoginId() throws Exception {
		return this.authorize(()->{
			try( ProspectManagerManager manager = new ProspectManagerManager(this.currentUser) ) {
				return ok(manager.getManagerByLoginId(this.currentUser));
			}
		});
	}
	
	@POST
	@Path("/import-tsa")
	@Consumes("multipart/form-data")
	@Produces(MediaType.APPLICATION_JSON)
	public Response importProspectTsa(
            @FormDataParam("userFileName") String userFileName,
            @FormDataParam("uplCode") String uplCode,
            @FormDataParam("uplType") String uplType,
            @FormDataParam("ownerId") Long ownerId,
            @FormDataParam("fileContent") InputStream fileContent,
            @FormDataParam("mbMis") String mbMis
			) throws Exception{

		return this.authorize(()->{
			try (UploadManager manager = new UploadManager(this.currentUser)){
				System.out.println(new BusinessLogs().writeURN("/v1.0/telesales/prospect/import-tsa", this.currentUser.getLoginId()));
				UploadFileDTO UFDTO = new UploadFileDTO();
				UFDTO.setUserFileName(userFileName);
				UFDTO.setFileContent(fileContent);
				UFDTO.setUplCode(uplCode);
				UFDTO.setUplType(uplType);
				UFDTO.setOwnerId(ownerId);
				UFDTO.setMbMis(mbMis);
				return accepted(manager.importProspectTsa(UFDTO));
			}
		});
	}
	
	@GET
	@Path("/import-tsa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkImportStatusTsa() throws Exception {
		return this.authorize(()->{
			try (UploadManager manager = new UploadManager(this.currentUser)){
				return ok(manager.importStatus());
			}
		});
	}
	
	@PUT
	@Path("/import-tsa/{uplDetailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmImportTsa(@PathParam("uplDetailId") Long uplDetailId) throws Exception {
		
		return this.authorize(()->{
			
			try (UploadManager manager = new UploadManager(this.currentUser)){
				
				Object response = manager.confirmUploadTsa(uplDetailId);
				
				this.updateData(uplDetailId);
				
				return ok(response);
			}
		});
	}
	
	@DELETE
	@Path("/import-tsa/{uplDetailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteImportTsa(@PathParam("uplDetailId") Long uplDetailId ) throws Exception {
		
		return this.authorize(()->{
			
			try (UploadManager manager = new UploadManager(this.currentUser)){
				
				Object response = manager.deleteUploadTsa(uplDetailId);
				
				this.updateData(uplDetailId);
				
				return ok(response);
			}
		});
	}
	
	@DELETE
	@Path("/confirmNextImportTSA/{uplDetailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirmNextImportTSA(@PathParam("uplDetailId") Long uplDetailId ) throws Exception {
		return this.authorize(()->{
			try (UploadManager manager = new UploadManager(this.currentUser)){
				return ok(manager.confirmNextImportTSA(uplDetailId));
			}
		});
	}
	
	
}
