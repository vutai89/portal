package com.mcredit.service.system;

import java.net.URLEncoder;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import com.mcredit.model.enums.ServiceName;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.service.BasedService;
import com.mcredit.sharedbiz.esb.ContactCenterProxy;
import com.mcredit.sharedbiz.esb.MedusaProxy;
import com.mcredit.util.RegexHelper;

@Path("/v1.0/medusa")
public class MedusaService extends BasedService {
	BasedHttpClient bs = new BasedHttpClient();

	public MedusaService(@Context HttpHeaders headers) {
		super(headers);
	}

	@GET
	@Path("/getDataEntry")
	public Response getDataEntry(@QueryParam("contractNumbers") String contractNumbers) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetDataEntry, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getDataEntry(URLEncoder.encode(contractNumbers, "UTF-8"));
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getContract")
	public Response getContract(@QueryParam("fromDateRepayment") String fromDateRepayment,
			@QueryParam("toDateRepayment") String toDateRepayment, @QueryParam("fromDateAssign") String fromDateAssign,
			@QueryParam("toDateAssign") String toDateAssign,
			@QueryParam("fromNextActionDate") String fromNextActionDate,
			@QueryParam("toNextActionDate") String toNextActionDate, @QueryParam("isCAB") String isCAB,
			@QueryParam("isConnect") String isConnect, @QueryParam("isDateAssign") String isDateAssign,
			@QueryParam("contractNumber") String contractNumber, @QueryParam("assignedUser") String assignedUser,
			@QueryParam("groupName") String groupName, @QueryParam("identityCard") String identityCard,
			@QueryParam("cif") String cif, @QueryParam("fromDPD") Integer fromDPD, @QueryParam("toDPD") Integer toDPD,
			@QueryParam("bucket") String bucket, @QueryParam("typeOfLoan") String typeOfLoan,
			@QueryParam("isActionTime") String isActionTime, @QueryParam("statusPayment") String statusPayment,
			@QueryParam("fromTerm") Integer fromTerm, @QueryParam("toTerm") Integer toTerm,
			@QueryParam("actionCode") String actionCode, @QueryParam("orderby") String orderby,
			@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetContract, () -> {
			String queryWhere = "";

			if (contractNumber != null && contractNumber != "")
				queryWhere += " AND COALESCE(contract.contractNumber,'') LIKE concat('%','" + contractNumber + "','%')";
			if (assignedUser != null && assignedUser != "")
				queryWhere += " AND caseassigned.assignedUser = '" + assignedUser + "'";
			if (identityCard != null && identityCard != "")
				queryWhere += " AND COALESCE(contract.identityCard,'') LIKE concat('%','" + identityCard + "','%')";
			if (cif != null && cif != "")
				queryWhere += " AND COALESCE(contract.CIF,'') LIKE concat('%','" + cif + "','%')";
			if (fromDPD != null && toDPD != null)
				queryWhere += " AND contract.DPD BETWEEN " + fromDPD + " AND " + toDPD;
			if (cif != bucket && bucket != "")
				queryWhere += " AND COALESCE(contract.bucket,'') = '" + bucket + "' ";
			if (fromDateRepayment != null && fromDateRepayment != "" && toDateRepayment != null
					&& toDateRepayment != "")
				queryWhere += " AND DATE(contract.dateRepayment) BETWEEN DATE('" + fromDateRepayment + "') AND DATE('"
						+ toDateRepayment + "')";
			if (typeOfLoan != null && typeOfLoan != "")
				queryWhere += " AND COALESCE(contract.typeOfLoan,'') LIKE concat('%','" + typeOfLoan + "','%')";
			if (statusPayment != null && statusPayment != "")
				queryWhere += " AND COALESCE(contract.statusPayment,'') LIKE concat('%','" + statusPayment + "','%')";
			if (fromTerm != null && toTerm != null)
				queryWhere += " AND contract.term BETWEEN " + fromTerm + " AND " + toTerm;
			if (actionCode != null && actionCode != "")
				queryWhere += " AND COALESCE(commend.actionCode,'') LIKE concat('%','" + actionCode + "','%')";
			if (fromDateAssign != null && fromDateAssign != "" && toDateAssign != null && toDateAssign != "")
				queryWhere += " AND (DATE(caseassigned.dateAssign) BETWEEN DATE('" + fromDateAssign + "') AND DATE('"
						+ toDateAssign + "') OR COALESCE(ISNULL(caseassigned.dateAssign),'') LIKE concat('%','"
						+ isDateAssign + "','%'))";

			if (fromNextActionDate != null && fromNextActionDate != "" && toNextActionDate != null
					&& toNextActionDate != "") {
				queryWhere += " AND DATE(commend.nextActionDate) BETWEEN DATE('" + fromNextActionDate + "') AND DATE('"
						+ toNextActionDate + "')";
			}

			if (isCAB != null) {
				switch (isCAB) {
				case "CAB00":
					break;
				case "CAB01":
					queryWhere += " AND commend.nextActionDate < NOW() ";
					break;
				case "CAB02":
					queryWhere += " AND commend.nextActionDate < NOW() AND DATE(commend.nextActionDate) = DATE(NOW()) ";
					break;
				case "CAB03":
					queryWhere += " AND TIMESTAMPDIFF(MINUTE,commend.nextActionDate,NOW()) >= 0 AND TIMESTAMPDIFF(MINUTE,commend.nextActionDate,NOW()) <= 60";
					break;
				case "CAB04":
					queryWhere += " AND TIMESTAMPDIFF(MINUTE,NOW(),commend.nextActionDate) <= 30 AND TIMESTAMPDIFF(MINUTE,NOW(),commend.nextActionDate) >= 0";
					break;
				case "CAB05":
					queryWhere += " AND TIMESTAMPDIFF(MINUTE,NOW(),commend.nextActionDate) <= 60 AND TIMESTAMPDIFF(MINUTE,NOW(),commend.nextActionDate) >= 0";
					break;
				case "CAB06":
					queryWhere += " AND DATE(commend.nextActionDate) = DATE(NOW()) ";
					break;
				}
			}

			if (isConnect == "Statu02")
				queryWhere += " AND countRemark > countRemarkNobody ";

			if (isConnect == "Statu03")
				queryWhere += " AND countRemark <= countRemarkNobody ";

			if (orderby != null && orderby != "") {
				// item.CountRemark > item.CountRemarkNobody ? "Da co ket noi" : "Chua co ket
				// noi
				if (orderby.toLowerCase().contains("isconnect")) {
					queryWhere += " AND countRemark <= countRemarkNobody ";
				} else {
					queryWhere += " AND countRemark > countRemarkNobody ";
				}
			}

			if (queryWhere != null && queryWhere != "") {
				queryWhere = queryWhere.substring(4, queryWhere.length());
				queryWhere = " WHERE " + queryWhere;
			}

			queryWhere += " GROUP BY contract.contractNumber ";

			String queryHaving = "";
			if (isActionTime != null && isActionTime != "")
				queryHaving += " AND isActionTime LIKE concat('%','" + isActionTime + "','%')";

			if (groupName != null && groupName != "")
				queryHaving += " AND groupName LIKE concat('%','" + groupName + "','%')";

			if (queryHaving != null && queryHaving != "") {
				queryWhere += " HAVING " + queryHaving.substring(4, queryHaving.length());
			}

			String queryWhereTotal = queryWhere;

			if (orderby != null && orderby != "") {
				if (!orderby.toLowerCase().contains("isconnect"))
					queryWhere += " ORDER BY " + orderby;
			}

			if (offset != null && limit != null) {
				queryWhere += " LIMIT " + offset + "," + limit;
			}

			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getContract(URLEncoder.encode(queryWhere, "UTF-8"),
						URLEncoder.encode(queryWhereTotal, "UTF-8"));
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getHistoryPayment")
	public Response getHistoryPayment(@QueryParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetHistoryPayment, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getHistoryPayment(contractNumber);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getHistoryPaymentT24")
	public Response getHistoryPaymentT24(@QueryParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetHistoryPaymentT24, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getHistoryPaymentT24(contractNumber);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getGoodsInfomation")
	public Response historyPayment(@QueryParam("contractNumber") String contractNumber) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetGoodsInfomation, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getGoodsInfomation(contractNumber);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getCommend")
	public Response getCommend(@QueryParam("contractNumber") String contractNumber,
			@QueryParam("commendUser") String commendUser, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate, @QueryParam("offset") String offset, @QueryParam("limit") String limit)
			throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetCommend, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getCommend(contractNumber, commendUser, fromDate, toDate, offset, limit);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getPaymentSchedule")
	public Response getPaymentSchedule(@QueryParam("contractNumber") String contractNumber,
			@QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate,
			@QueryParam("offset") String offset, @QueryParam("limit") String limit) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetPaymentSchedule, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = new ApiResult();
				if (RegexHelper.ValidateContractNumber10001(contractNumber))
					result = manager.getPaymentScheduleIbank(contractNumber, fromDate, toDate, offset, limit);
				else
					result = manager.getPaymentScheduleT24(contractNumber);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getHistoryAssign")
	public Response getHistoryAssign(@QueryParam("contractNumber") String contractNumber,
			@QueryParam("offset") String offset, @QueryParam("limit") String limit) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetHistoryAssign, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getHistoryAssign(contractNumber, offset, limit);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getUser")
	public Response getUser(@QueryParam("user") String user) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetUser, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getUser(user);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/addCommend")
	public Response addCommend(String request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Medusa_Contract_AddCommend, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.addCommend(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@PUT
	@Path("/addMemo")
	public Response addMemo(String request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Medusa_Contract_AddMemo, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.addMemo(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getMemo")
	public Response getMemo(@QueryParam("contractNumbers") String contractNumbers) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetMemo, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getMemo(contractNumbers);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/getMemoAction")
	public Response getMemoAction(@QueryParam("contractNumbers") String contractNumbers) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Medusa_Contract_GetMemoAction, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getMemoAction(contractNumbers);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/getCallOutcome")
	public Response getCallOutcome(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Contact_Center_GetCallOutcome, () -> {
			try (ContactCenterProxy manager = new ContactCenterProxy()) {
				ApiResult result = manager.getCallOutcome(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/getBusinessOutcome")
	public Response getBusinessOutcome(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Contact_Center_GetBusinessOutcome, () -> {
			try (ContactCenterProxy manager = new ContactCenterProxy()) {
				ApiResult result = manager.getBusinessOutcome(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/setBusinessOutcome")
	public Response setBusinessOutcome(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Contact_Center_SetBusinessOutcome, () -> {
			try (ContactCenterProxy manager = new ContactCenterProxy()) {
				ApiResult result = manager.setBusinessOutcome(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@GET
	@Path("/extNumber")
	public Response getExtNumber(@QueryParam("username") String username) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_Contact_Center_ExtNumber, () -> {
			try (ContactCenterProxy manager = new ContactCenterProxy()) {
				ApiResult result = new ApiResult();
				result = manager.getExtNumber(username);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/dial")
	public Response postDial(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_Contact_Center_Dial, () -> {
			try (ContactCenterProxy manager = new ContactCenterProxy()) {
				ApiResult result = manager.postDial(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/getEarlyRepaymentInfo")
	public Response getEarlyRepaymentInfo(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_GET_EARLY_REPAYMENT_INFO, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.getEarlyRepaymentInfo(request);
				return ok(result.getBodyContent());
			}
		});
	}

	@POST
	@Path("/setEarlyRepaymentSchedule")
	public Response getEarlyRepaymentSchedule(Object request) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_SET_EARLY_REPAYMENT_SCHEDULE, () -> {
			try (MedusaProxy manager = new MedusaProxy()) {
				ApiResult result = manager.setEarlyRepaymentSchedule(request);
				return ok(result.getBodyContent());
			}
		});
	}
}
