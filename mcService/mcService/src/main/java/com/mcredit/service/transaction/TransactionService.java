package com.mcredit.service.transaction;


import com.mcredit.business.transaction.manager.TransactionManager;
import com.mcredit.model.dto.transaction.AppActiveInactiveProductDTO;
import com.mcredit.model.dto.transaction.TransactionDTO;
import com.mcredit.model.enums.ServiceName;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.service.BasedService;

@Path("/v1.0/transactions")

public class TransactionService extends BasedService {

    public TransactionService(@Context HttpHeaders headers) {
        super(headers);
    }
/**
 * get list product
 * @return
 * @throws Exception 
 */
    @GET
    @Path("/lst-product")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLstProduct() throws Exception {
        return this.authorize(ServiceName.GET_V1_0_Transactions_LstProduct, () -> {
        try (TransactionManager manager = new TransactionManager(this.currentUser)) {
            return ok(manager.getLstProduct());
        }
        });
    }
/**
 * create request setup calendar active/inactive products
 * @param transactionDTO
 * @return
 * @throws Exception 
 */
    @POST
    @Path("/add-transactions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addScheduleProduct(TransactionDTO transactionDTO) throws Exception {
        return this.authorize(ServiceName.POST_V1_0_Transactions, () -> {
        try (TransactionManager manager = new TransactionManager(this.currentUser)) {
            return ok(manager.addTransactions(transactionDTO));
        }
        });
    }
/**
 * search information schedule product
 * @param productId
 * @param ticketCode
 * @param effectDateFrom
 * @param effectDateTo
 * @param statusRequest
 * @param pageSize
 * @param pageNum
 * @return
 * @throws Exception 
 */
    @GET
    @Path("/seach-transactions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response seachTransactions(
            @QueryParam("productId") Long productId,
            @QueryParam("ticketCode") String ticketCode,
            @QueryParam("effectDateFrom") String effectDateFrom,
            @QueryParam("effectDateTo") String effectDateTo,
            @QueryParam("statusRequest") String statusRequest,
            @QueryParam("pageSize") Integer pageSize,
            @QueryParam("pageNum") Integer pageNum)
            throws Exception {
        return this.authorize(ServiceName.GET_V1_0_Transactions_Search, () -> {
        try (TransactionManager manager = new TransactionManager(this.currentUser)) {
            TransactionDTO input = new TransactionDTO(productId, ticketCode, effectDateFrom, effectDateTo, statusRequest);
            return ok(manager.seachTransactions(input, pageSize, pageNum));
        }
        });
    }
/**
 * Approval for ticket refusal 
 * @param aspdto
 * @return
 * @throws Exception 
 */
    @PUT
    @Path("/approve-active-inactive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveActiveInactiveProduct(AppActiveInactiveProductDTO aspdto)
            throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_Transactions_Approve, () -> {
        try (TransactionManager manager = new TransactionManager(this.currentUser)) {
            return ok(manager.approveActiveInactiveProduct(aspdto));
        }
		});
    }

}
