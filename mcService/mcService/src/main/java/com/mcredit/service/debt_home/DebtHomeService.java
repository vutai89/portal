package com.mcredit.service.debt_home;

import java.io.File;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import com.mcredit.business.debt_home.manager.DebtHomeManager;
import com.mcredit.model.dto.debt_home.DebtHomeAssignDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;
import com.mcredit.util.StringUtils;

@Path("/v1.0/debt-home")

public class DebtHomeService extends BasedService {

	public DebtHomeService(@Context HttpHeaders headers) {
		super(headers);
	}

	/**
	 * Function assign user to contract
	 * 
	 * @param list
	 *            assign
	 * @return count record insert
	 * @author hoanx.ho
	 */
	@POST
	@Path("/assign")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignDebtHome(List<DebtHomeAssignDTO> debtHomeAssigns) throws Exception {

		return this.authorize(ServiceName.POST_V1_0_Assign_Debt_Home, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.assignDebtHome(debtHomeAssigns, this.currentUser.getLoginId()));
			}
		});
	}

	/**
	 * Function search files by contract number
	 * 
	 * @param contractNumber
	 * @return files
	 * @author hoanx.ho
	 */
	@GET
	@Path("/lookup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response assignDebtHome(@QueryParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Debt_Home_Lookup, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.lookup(contractNumber, this.currentUser.getLoginId(), true, true));
			}
		});
	}

	/**
	 * Get all file document by contract number and zip it to *.zip file, then send
	 * it to client immediately.
	 * 
	 * @param contractNumber
	 * @return zip file
	 * @author anhdv.ho
	 */
	@GET
	@Path("/downloadZipDocument/{contractNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadZipDocument(@PathParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DebtHome_Zip_Document, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.zipDocument(contractNumber, this.currentUser.getLoginId(), true));
			}
		});
	}

	/**
	 * Drop zip document file when client actually saved data file to their disk.
	 * 
	 * @param contractNumber
	 * @return void
	 * @author anhdv.ho
	 */
	@DELETE
	@Path("/dropZipDocument/{contractNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dropZipDocument(@PathParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DebtHome_Zip_Document, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.dropDocument(contractNumber, this.currentUser.getLoginId()));
			}
		});
	}

	/**
	 * Function download file
	 * 
	 * @param filePath
	 * @return File
	 * @author hoanx.ho
	 */
	@GET
	@Path("/download-file")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@QueryParam("fileId") Long fileId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Debt_Home_Download_File, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				String filePath = manager.getFilePath(fileId, this.currentUser.getLoginId());
				File f = new File(filePath);
				return responseFileDownload(f);
			}
		});
	}
	

	/**
	 * Get contract information
	 * @param contractNumber
	 * @param appNumber
	 * @param custMobilePhone
	 * @param identityNumber
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/lookup-contract")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response lookupContract(@QueryParam("contractNumber") String contractNumber, @QueryParam("appNumber") String appNumber,
			@QueryParam("mobilePhone") String custMobilePhone, @QueryParam("identityNumber") String identityNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.lookupContract(contractNumber, appNumber, custMobilePhone, identityNumber));
			}
		});
	}
	
	/**
	 * Get list file contract info by appNumber
	 * @throws Exception 
	 */
	@GET
	@Path("/lookup-file")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response lookupFile(@QueryParam("appNumber") String appNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_FILE, () -> {
			try(DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.lookupFile(appNumber));
			}
		});
	}
	
	/**
	 * Download/ View file contract info
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/download-file-contract")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFileContract(@QueryParam("fileId") Long fileId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_FILE, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				String filePath = manager.downloadFileContract(fileId);
				File f = new File(filePath);
				return responseFileDownload(f);
			}
		});
	}
	
	/**
	 * Download/ View all file contract info
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/downloadZipFileContract/{appNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadAllFileContract(@PathParam("appNumber") String appNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_ALL_FILE, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.downloadAllFileContract(appNumber));
			}
		});
	}
	
	/**
	 * Drop zip document file contract when client actually saved data file to their disk.
	 * 
	 * @param appNumber
	 * @return void
	 */
	@DELETE
	@Path("/dropZipFileContract/{appNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dropZipFileContract(@PathParam("appNumber") String appNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_DOWNLOAD_ALL_FILE, () -> {
			try (DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.dropZipFileContract(appNumber));
			}
		});
	}
	
	/**
	 * Look up file pcb where appnumber, citizenId
	 * @return
	 * @throws Exception 
	 */
	@GET
	@Path("/lookup-pcb")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response lookupPcb(@QueryParam("appNumber") String appNumber, @QueryParam("citizenId") String citizenId, @QueryParam("oldCitizenId") String oldCitizenID) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_DEBT_HOME_LOOKUP_CONTRACT_GET_PCB, () -> {
			try(DebtHomeManager manager = new DebtHomeManager()) {
				return ok(manager.getPcbByCitizenId(StringUtils.nullToEmpty(appNumber), citizenId, oldCitizenID));
			}
		});
	}

}
