package com.mcredit.business.payment;

import java.math.BigDecimal;
import java.util.List;

import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.model.dto.CustomerDTO;
import com.mcredit.model.dto.PostingConfigurationDTO;
import com.mcredit.model.dto.PostingInstanceDTO;
import com.mcredit.model.dto.card.PaymentDTO;
import com.mcredit.model.dto.card.PaymentResultDTO;
import com.mcredit.model.enums.PaymentAmountTag;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.service.CustomerService;
import com.mcredit.sharedbiz.service.MessageTaskService;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class PaymentManager extends BaseManager {

	public PaymentResultDTO createPayment(PaymentDTO request) throws Exception {
		
		return this.tryCatch(()->{
			
			PaymentResultDTO paymentResultDTO = new PaymentResultDTO();
			PaymentFactory.createPaymentValidation().validatePayment(request);
			
			CustomerDTO cust = new CustomerService(this.uok.customer).findCustIdByCardId(request.getCardId());
			if (cust == null)
				throw new ValidationException("CardId is invalid");

			PaymentService paymentSrv = PaymentFactory.getInstance(this.uok.payment, this.uok.credit);

			List<PostingConfigurationDTO> lst = paymentSrv.createPayment(request.getPartnerRefId(), cust.getId(),request.getPartnerCode(), request.getPostingGroup(), request.getAmount(), request.getCardId());

			List<PostingInstanceDTO> lInstanceDTOs = paymentSrv.pInstances;

			System.out.println(" -------- lInstanceDTOs ----------    " + JSONConverter.toJSON(lInstanceDTOs));

			if (lst != null && lst.size() > 0) {
				BigDecimal feeCollectATM = null;

				/* For check partnerRefId duplicate */
				List<MessageTask> lTasks = this.uok.common.messageTaskRepo().findPostingConfiguration(
						request.getPartnerCode() + "-" + request.getPostingGroup() + "-" + request.getPartnerRefId());

				if (lTasks.size() > 0)
					throw new ValidationException("400.12", "Partner Ref Id is duplicated.");

				MessageTaskService msgTaskSrv = new MessageTaskService(this.uok.common);

				MessageTask msTask = msgTaskSrv.createMessageTask(request.getPartnerCode(), request.getPostingGroup(),
						request.getPartnerRefId());

				paymentResultDTO.setRelationId(msTask.getRelationId());
				for (PostingConfigurationDTO pst : lst) {
					if (pst.getAmountTag().equals(PaymentAmountTag.FEE_COLLECT_AMT.value())) {
						feeCollectATM = pst.getAmount(); // nay Amount FEE_COLLECT_AMT
						break;
					}
				}
				int i = 0;
				for (PostingConfigurationDTO obj : lst) {
					
					System.out.println(" -------- PostingConfigurationDTO  ----------  :  " + i + " : ------  "
							+ JSONConverter.toJSON(obj));
					System.out.println(" -------- InstanceDTO ----------    " + i + " : ------  "
							+ JSONConverter.toJSON(lInstanceDTOs.get(i)));
					
					msgTaskSrv.createMessageLog(obj.getTransactionTypeId(), obj.getPostingSystem(),obj.getPostingOrder(),
							paymentSrv.buidMessageLogBody(cust.getCoreCustCode(), feeCollectATM, obj,
									request.getPartnerRefId(), request.getCardId(), request.getAmount(),
									cust.getCustName(), paymentSrv.pInstances.get(i)),
							msTask.getId(), msTask.getRelationId(), paymentSrv.pInstances.get(i).getId(),
							obj.getCreditOwner());
					i++;
				}

				// MsgLog for SMS send
				/*
				 * msgTaskSrv.createMessageLogSMS(MsgTransType.SMS.toString(),
				 * MsgChannel.MC_PORTAL_APPLICATION.value(), MsgOrder.NINETY_NINE.value(),
				 * paymentSrv.buidMessageLogBodyForSMS(cust.getMobile(),
				 * request.getAmount().toString(), request.getCardId()), msTask.getId(),
				 * msTask.getRelationId(), (long) 0, "");
				 */

				// For update Amount in CreditApplicationOutstanding
			}
			
			return paymentResultDTO;
		});
	}

	/*
	 * public static void main(String[] args) throws Exception { PaymentDTO paymDto
	 * = new PaymentDTO(); paymDto.setAmount(new BigDecimal("754495"));
	 * paymDto.setPostingGroup("THH"); paymDto.setPartnerCode("VIETTEL");
	 * paymDto.setCardId("8274447_1"); paymDto.setPartnerRefId("phaoxxX1");
	 * 
	 * System.out.println(JSONConverter.toJSON(paymDto));
	 * 
	 * System.out.println(new
	 * PaymentManager().createPayment(paymDto).getRelationId()); }
	 */
}
