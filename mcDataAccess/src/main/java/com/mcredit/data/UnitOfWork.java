package com.mcredit.data;

import org.hibernate.Session;

import com.mcredit.data.appbpm.UnitOfWorkAppBPM;
import com.mcredit.data.appraisal.UnitOfWorkAppraisal;
import com.mcredit.data.assign.UnitOfWorkManagePermision;
import com.mcredit.data.audit.UnitOfWorkAudit;
import com.mcredit.data.black_list.UnitOfWorkBlackList;
import com.mcredit.data.cancelcasebpm.UnitOfWorkUplAppAbort;
import com.mcredit.data.check_vehicle_price.UnitOfWorkCheckVehiclePrice;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.debt_home.UnitOfWorkDebtHome;
import com.mcredit.data.ecm.UnitOfWorkECM;
import com.mcredit.data.mobile.UnitOfWorkMobile;
import com.mcredit.data.mobile.UnitOfWorkMobileTest;
import com.mcredit.data.payment.UnitOfWorkPayment;
import com.mcredit.data.pcb.UnitOfWorkPCB;
import com.mcredit.data.rule.UnitOfWorkRule;
import com.mcredit.data.send_mail.UnitOfWorkSendMail;
import com.mcredit.data.transactions.UnitOfWorkTransactions;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.user.UnitOfWorkUser;
import com.mcredit.data.warehouse.UnitOfWorkWareHouse;

public class UnitOfWork extends BaseUnitOfWork {

	public UnitOfWorkUser user;
	public UnitOfWorkTelesale telesale;
	public UnitOfWorkPayment payment;
	public UnitOfWorkCustomer customer;
	public UnitOfWorkCredit credit;
	public UnitOfWorkCommon common;
	public UnitOfWorkRule rule;
	public UnitOfWorkWareHouse warehouse;
	public UnitOfWorkMobile mobile;
	public UnitOfWorkMobileTest mobileTest;
	public UnitOfWorkManagePermision managePermision;
	public UnitOfWorkDebtHome debtHome;
	public UnitOfWorkPCB pcb;
	public UnitOfWorkAudit audit;
	public UnitOfWorkTransactions transactions;
    public UnitOfWorkAppraisal appraisal;
    public UnitOfWorkAppBPM appBPM;
	public UnitOfWorkECM ecm;
	public UnitOfWorkUplAppAbort uplAppAbort;
	public UnitOfWorkCheckVehiclePrice checkVehicle;
	public UnitOfWorkBlackList blackList;
	public UnitOfWorkSendMail sendMail;

	public UnitOfWork() {
		super();
		this.init(this.hibernateBase, this.session);
	}

	public UnitOfWork(HibernateBase hibernateBase, Session session) {
		super(hibernateBase, session);
		this.init(hibernateBase, session);
	}

	private void init(HibernateBase hibernateBase, Session session) {
		this.user = new UnitOfWorkUser(hibernateBase, session);
		this.telesale = new UnitOfWorkTelesale(hibernateBase, session);
		this.payment = new UnitOfWorkPayment(hibernateBase, session);
		this.customer = new UnitOfWorkCustomer(hibernateBase, session);
		this.credit = new UnitOfWorkCredit(hibernateBase, session);
		this.common = new UnitOfWorkCommon(hibernateBase, session);
		this.rule = new UnitOfWorkRule(hibernateBase, session);
		this.warehouse = new UnitOfWorkWareHouse(hibernateBase, session);
		this.mobile = new UnitOfWorkMobile(hibernateBase, session);
		this.mobileTest =  new UnitOfWorkMobileTest(hibernateBase, session);
		this.managePermision =  new UnitOfWorkManagePermision(hibernateBase, session);
		this.debtHome =  new UnitOfWorkDebtHome(hibernateBase, session);
		this.pcb = new UnitOfWorkPCB(hibernateBase, session);
		this.audit = new UnitOfWorkAudit(hibernateBase, session);
		this.transactions = new UnitOfWorkTransactions(hibernateBase, session);
        this.appraisal = new UnitOfWorkAppraisal(hibernateBase, session);
        this.appBPM = new UnitOfWorkAppBPM(hibernateBase, session);
		this.ecm = new UnitOfWorkECM(hibernateBase, session);
		this.uplAppAbort = new UnitOfWorkUplAppAbort(hibernateBase, session);
		this.checkVehicle = new UnitOfWorkCheckVehiclePrice(hibernateBase, session);
		this.blackList = new UnitOfWorkBlackList(hibernateBase, session);
		this.sendMail = new UnitOfWorkSendMail(hibernateBase, session);
	}
}
