package com.mcredit.business.payment;

import com.mcredit.business.payment.validation.PaymentValidation;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.data.payment.UnitOfWorkPayment;

public class PaymentFactory {
	
	public static PaymentService getInstance( UnitOfWorkPayment uokPayment ,UnitOfWorkCredit unitOfWorkCredit) {
		return new PaymentService(uokPayment , unitOfWorkCredit);
	}
	
	public static PaymentValidation createPaymentValidation() {
		
		return new PaymentValidation();
	}
}