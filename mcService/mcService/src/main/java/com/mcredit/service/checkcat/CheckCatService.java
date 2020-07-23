package com.mcredit.service.checkcat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

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

import org.jboss.logging.Param;

import com.mcredit.business.checkcat.manager.CheckCatManager;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.CustCompanyCheckDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.dto.check_cat.UpdateTopCompDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.service.BasedService;
import com.mcredit.util.StringUtils;

import scala.Array;

/**
 * @author cuongvt.ho
 *
 */
@Path("/v1.0/check-cat")
public class CheckCatService extends BasedService{
	
	public CheckCatService(@Context HttpHeaders headers) {
		super(headers);
	}
	
	
	
	/**
	 * Lookup company by sales
	 * 
	 * @author hoanx.ho
	 * @param taxNumber company tax number
	 * @param compName company name
	 * @param catTypeId category type 
	 * @return List Company list company was searched
	 * @throws Exception
	 */
	@GET
	@Path("/sale-lookup-company")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saleLookupCompany(@QueryParam("taxNumber") String taxNumber, @QueryParam("compName") String compName, @QueryParam("catTypeId") Long catTypeId) throws Exception {

		return this.authorize(ServiceName.GET_V1_0_CheckCAT_Sale_Lookup_Company, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.saleLookupCompany(taxNumber, compName, catTypeId));
			}
		});
	}
	
	/**
	 * Lookup company by risk
	 * 
	 * @author hoanx.ho
	 * @param taxNumbers company tax number
	 * @param compName company name
	 * @param establishDate establish date of company
	 * @param cicInfo CIC information
	 * @param dateCheckCat date was checked category
	 * @param top500_1000 is top 500-1000 company
	 * @param top100_1000_branch is top 500-1000 branch
	 * @param companyType company type
	 * @param economicType economic of company type
	 * @param multinationalCompany is multinational company
	 * @param operationStatus operation status
	 * @param catType category type
	 * @param pageNumber paging number
	 * @param pageSize paging size
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/risk-lookup-company")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response riskLookupCompany(@QueryParam("taxNumbers") String taxNumbers, @QueryParam("compName") String compName,
			@QueryParam("establishDate") String establishDate, @QueryParam("cicInfo") Long cicInfo, @QueryParam("dateCheckCat") String dateCheckCat, 
			@QueryParam("top500_1000") String top500_1000, @QueryParam("top100_1000_branch") String top100_1000_branch, @QueryParam("companyType") Long companyType, 
			@QueryParam("economicType") Long economicType, @QueryParam("multinationalCompany") String multinationalCompany,
			@QueryParam("operationStatus") String operationStatus, @QueryParam("catType") Long catType, 
			@QueryParam("pageNumber") Integer pageNumber, @QueryParam("pageSize") Integer pageSize
			) throws Exception {

		return this.authorize(ServiceName.GET_V1_0_CheckCAT_Risk_Lookup_Company, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				
				List<String> lstTaxNumber = new ArrayList<>();
				if (!StringUtils.isNullOrEmpty(taxNumbers)) {
					String[] arrTaxNumbers = taxNumbers.split(",");
					lstTaxNumber = Arrays.asList(arrTaxNumbers);
				}
				
				SearchCompanyDTO searchDTO = new SearchCompanyDTO(compName, lstTaxNumber, establishDate, cicInfo, dateCheckCat, 
						top500_1000, top100_1000_branch, companyType, economicType,
						multinationalCompany, operationStatus, catType, pageNumber, pageSize
				);
				return ok(manager.riskLookupCompany(searchDTO));
			}
		});
	}
	
	
	/**
	 * Check company by risk
	 * 
	 * @author hoanx.ho
	 * @param companies List CustCompanyCheckDTO
	 * @return List CustCompanyCheckDTO
	 * @throws Exception
	 */
	@POST
	@Path("/check-companies")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCompanies(List<CustCompanyCheckDTO> companies) throws Exception {

		return this.authorize(ServiceName.POST_V1_0_CheckCAT_Sale_Check_Company, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.saleCheckCompanies(companies));
			}
		});
	}
	
	
	/**
	 * Check process status
	 * 
	 * @author hoanx.ho
	 * @param companies List CustCompanyCheckDTO 
	 * @return ResponseIds
	 * @throws Exception
	 */
	@PUT
	@Path("/process-status")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeProcessStatus(List<CustCompanyCheckDTO> companies) throws Exception {

		return this.authorize(ServiceName.PUT_V1_0_CheckCAT_Sale_Change_Status_Check_CAT, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.changeProcessStatus(companies, this.currentUser.getLoginId()));
			}
		});
	}
	
	/**
	 * Get list company will be checked cat
	 * @author cuongvt.ho
	 * @return List company for check category
	 * @throws Exception
	 */
	@GET
	@Path("/comp-check-cat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllCompCheckCat() throws Exception {
		return this.authorize(ServiceName.GET_V1_0_CheckCAT_Risk_Comp_Check_CAT, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.getAllCompForCheckCat());
			}
		});
	}
	
	
	/**
	 * Remove companies
	 * @author cuongvt.ho
	 * @param taxNumbers list company need remove
	 * @return ResultRemoveCompDTO: list errors remove, count remove success
	 * @throws Exception
	 */
	@DELETE
	@Path("/delete-list-company")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateListComp(@QueryParam("taxNumbers") String taxNumbers) throws Exception {
		return this.authorize(ServiceName.DELETE_V1_0_CheckCAT_Risk_Delete_Company, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.removeListComp(taxNumbers));
			}
		});

	}
	
	/**
	 * Update list top company
	 * @author cuongvt.ho
	 * @param dto list UpdateTopCompDTO include list company for removed and list company for insert
	 * @return ResultUpdateListCompDTO: count remove company, count insert company, and list errors.
	 * @throws Exception
	 */
	@POST
	@Path("/update-top-company")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateTopComp(UpdateTopCompDTO dto) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_CheckCAT_Risk_Update_Top_Company, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.updateTopComp(dto, this.currentUser.getLoginId()));
			}
		});

	}
	
	/**
	 * Check company type
	 * @author cuongvt.ho
	 * @param lstComp list company
	 * @return ResponseCompTypeDTO: list company was checked company type, and list errors
	 * @throws Exception
	 */
	@POST
	@Path("/check-company-type")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCompType(List<CompanyDTO> lstComp) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_CheckCAT_Risk_Check_Company_Type, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.checkCompType(lstComp));
			}
		});

	}
	
	/**
	 * Check category
	 * @author cuongvt.ho
	 * @param lstComp list company
	 * @return List<CompanyDTO>: list company were checked category
	 * @throws Exception
	 */
	@POST
	@Path("/check-cat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkCatResult(List<CompanyDTO> lstComp) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_CheckCAT_Risk_Check_CAT, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.checkCatResult(lstComp));
			}
		});

	}
	
	/**
	 * Save check cat
	 * @author cuongvt.ho
	 * @param lstComp list company were checked category
	 * @return List<CompanyDTO>: list company were checked category
	 * @throws Exception
	 */
	@POST
	@Path("/save-cat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveCatResult(List<CompanyDTO> lstComp) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_CheckCAT_Risk_Save_CAT, () -> {
			try (CheckCatManager manager = new CheckCatManager()) {
				return ok(manager.saveCatResult(lstComp));
			}
		});
		
	}
	
	/**
	 * Check user in one screen
	 * @author hoanx.ho
	 * @return String checked user in screen
	 * @throws Exception
	 */
//	@PUT
//	@Path("/get-and-set-user-session-screen03/{loginId}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getStatusOnPageRequestCat(@PathParam("taxNumbers") String loginId) throws Exception {
//		return this.authorize(ServiceName.CheckCat_Check_User_OnPage, () -> {
//			try (CheckCatManager manager = new CheckCatManager()) {
//				return ok(manager.getAndSetCatUserSession(loginId));
//			}
//		});
//	}
	
	
	/**
	 * Check status on page request category
	 * @author hoanx.ho
	 * @return String check status on page request category
	 * @throws Exception
	 */
//	@PUT
//	@Path("/status-on-page-request-cat")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response changeStatusOnPageRequestCat(String value) throws Exception {
//		return this.authorize(ServiceName.CheckCat_Check_User_OnPage, () -> {
//			try (CheckCatManager manager = new CheckCatManager()) {
//				return ok(manager.changeStatusOnPageRequestCat(value));
//			}
//		});	
//	}
}
