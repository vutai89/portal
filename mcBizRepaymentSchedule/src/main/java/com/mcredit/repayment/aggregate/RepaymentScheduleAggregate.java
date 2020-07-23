package com.mcredit.repayment.aggregate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.util.JSONFactory;
import com.mcredit.repayment.dto.RawDataDTO;
import com.mcredit.repayment.dto.RepaymentScheduleDTO;
import com.mcredit.repayment.dto.RepaymentSchedulePayload;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;

/**
 * @author manhnt1.ho
 *
 */
public class RepaymentScheduleAggregate {
	
	private UnitOfWork _uok = null;
	public Double firstAnnuityAmount;
	List<RawDataDTO> lstRawDataDTO = new ArrayList<RawDataDTO>();
	List<Date> lstDateOfDebtReceipt= new ArrayList<Date>();
	/**
	 * 
	 */
	public Double thePaymentAmountEachPeriod;
	
	public RepaymentScheduleAggregate(UnitOfWork _uok, UserDTO user) throws ValidationException {
		this._uok = _uok;
		//RepaymentSchedulePayload payload = new RepaymentSchedulePayload();
		//firstAnnuityAmount = getFirstAnnuityAmount(payload);
		///thePaymentAmountEachPeriod = getT09(payload);
		//lstDateOfDebtReceipt = this.getDateOfDebtReceipt(payload);
		/// this.repaymentScheduleData(payload);
		// System.out.println(thePaymentAmountEachPeriod);
		//this.rawData(payload);
	}

	public List<RawDataDTO> rawData(RepaymentSchedulePayload payload) throws ValidationException {
		lstRawDataDTO = new ArrayList<RawDataDTO>();
		for(int i =0; i <= payload.getTenor(); i++) {
			RawDataDTO rawDataDTO = new RawDataDTO();
			rawDataDTO.setTeror(i);
			rawDataDTO.setDateOfPayment(lstDateOfDebtReceipt.get(i));
			rawDataDTO.setPrincipalAndMonthlyInterest(this.getPrincipalAndMonthlyInterest(payload, i));
			rawDataDTO.setOriginalAmount(this.getOriginalAmount(payload, i));
			rawDataDTO.setProfitAmount(this.getProfitAmount(payload, i));
			rawDataDTO.setDebt(this.getDebt(payload, i));
			lstRawDataDTO.add(rawDataDTO);
		}
		if(lstRawDataDTO.get(1).getProfitAmount() > firstAnnuityAmount) throw new ValidationException(Messages.getString("repayment.schedule.validate",Labels.getString("label.repayment.schedule.validate")));
		return lstRawDataDTO; 
	} 
	
	public Double calculateTheDifference(RepaymentSchedulePayload payload) {
		return roundingValue(lstRawDataDTO.get(payload.getTenor()).getPrincipalAndMonthlyInterest() -lstRawDataDTO.get(payload.getTenor()-1).getPrincipalAndMonthlyInterest());
	}
	
	public Double roundingValue(Double dRounding) {
		return (double) Math.round(dRounding * 100 ) /100;
	}
	
	public List<RepaymentScheduleDTO> repaymentScheduleData(RepaymentSchedulePayload payload) throws ValidationException {
		// Tinh toan lan dau 
		lstDateOfDebtReceipt = this.getDateOfDebtReceipt(payload);
		firstAnnuityAmount = getFirstAnnuityAmount(payload);
		rawData(payload);
		// Tinh chenh lech
		Double CL1 = calculateTheDifference(payload); 
		// Tinh lan chay tiep theo
		//		=ROUND(D13+D28/F8,2) : firstAnnuityAmount + CL/Teror
		firstAnnuityAmount = roundingValue(firstAnnuityAmount + CL1/payload.getTenor());
		lstRawDataDTO = rawData(payload);
		Double CL2 = calculateTheDifference(payload);
		while (Math.abs(CL2)  < Math.abs(CL1)) {
			// Tinh lan chay tiep theo
			//		=ROUND(D13+D28/F8,2) : firstAnnuityAmount + CL/Teror
			CL1 = CL2;
			firstAnnuityAmount = roundingValue(firstAnnuityAmount + CL1/payload.getTenor());
			lstRawDataDTO = rawData(payload);
			CL2 = calculateTheDifference(payload);
		}
		/// System.out.println(JSONFactory.toJson(aggregate.lstRawDataDTO));
		
		List<RepaymentScheduleDTO> lstRepaymentSchedule = new ArrayList<RepaymentScheduleDTO>();
		for(RawDataDTO item:lstRawDataDTO) {
			if(item.getTeror() == 0) continue;
			RepaymentScheduleDTO repaymentScheduleDTO = new RepaymentScheduleDTO();
			repaymentScheduleDTO.setTeror(item.getTeror());
			repaymentScheduleDTO.setDateOfPayment(DateUtil.dateToString(item.getDateOfPayment(), "yyyy-MM-dd") );
			repaymentScheduleDTO.setPrincipalAndMonthlyInterest(Math.ceil(item.getPrincipalAndMonthlyInterest()/1000.0) * 1000);
			repaymentScheduleDTO.setOriginalAmount((double) (Math.round(item.getOriginalAmount()/1000) * 1000) );
			repaymentScheduleDTO.setProfitAmount(repaymentScheduleDTO.getPrincipalAndMonthlyInterest() - repaymentScheduleDTO.getOriginalAmount());
			repaymentScheduleDTO.setCollectionServiceFee(new Double(12000));
			repaymentScheduleDTO.setPayablesMonthly(repaymentScheduleDTO.getPrincipalAndMonthlyInterest() + repaymentScheduleDTO.getCollectionServiceFee());
			lstRepaymentSchedule.add(repaymentScheduleDTO);
		}
		if(lstRepaymentSchedule.get(0).getProfitAmount() > lstRepaymentSchedule.get(0).getPrincipalAndMonthlyInterest()) 
			throw new ValidationException(Messages.getString("repayment.schedule.validate",
				Labels.getString("label.repayment.schedule.validate")));
		System.out.println(JSONFactory.toJson(lstRepaymentSchedule));
		return lstRepaymentSchedule;
	}
	
	
	/**
	 * @param payload
	 * @return lay bien so T09
	 */
	public Double getFirstAnnuityAmount(RepaymentSchedulePayload payload){
		List<Date> lstDateOfDebtReceipt = getDateOfDebtReceipt(payload);
		Double P = payload.getCurrentOutstanding();
		int dateSubtract = DateUtil.subtract(lstDateOfDebtReceipt.get(1),lstDateOfDebtReceipt.get(0));
		Double interestAmount = P*(new Double(dateSubtract))*(payload.getIntRate())/(new Double(365));
		Integer tenor= payload.getTenor();
		Integer residualPrincipal=0;
		double r= payload.getIntRate()*366/(365*tenor);
		/*Cong thuc =(J8*((J4+J5)*((J8+1)^(J6-1))-J7))/(((J8+1)^F8-1))*/
		/*(J4+J5)*/
		Double calculate1 = P + interestAmount;
		/*((J4+J5)*((J8+1)^(J6-1))-J7))*/
		Double calculate2 = Math.pow((r + (new Double(1))), tenor-1) -residualPrincipal ;
		/*(J8+1)^F8-1)*/
		Double calculate3 = Math.pow((r+1), tenor)-1 ;
		Double calculateTotal = r*(calculate1 * calculate2)/calculate3;
		return roundingValue(calculateTotal);
	}
	
	
	/**
	 * @param payload
	 * @param teror
	 * @return Khoan goc va lai hang thang
	 */
	public Double getPrincipalAndMonthlyInterest(RepaymentSchedulePayload payload, Integer terorHandling){
		Double result;
		if(terorHandling == 0) result = -payload.getCurrentOutstanding();
		else if(terorHandling >0 && terorHandling < payload.getTenor()) result= firstAnnuityAmount;
		else result = this.getOriginalAmount(payload, terorHandling)+this.getProfitAmount(payload, terorHandling);
		result = (double) (Math.round(result * 100))/100;
		System.out.println("So tien Khoan goc va lai hang thang tai ky : " + terorHandling +" la :  " + result);
		return result;
	}
	
	
	/**
	 * @param payload
	 * @param teror
	 * @return So tien goc
	 */
	public Double getOriginalAmount(RepaymentSchedulePayload payload, Integer terorHandling){
		Double result;
		if(terorHandling == 0) result = -payload.getCurrentOutstanding();
		else if(terorHandling > 0 && terorHandling < payload.getTenor())  {
			// Khoan goc va lai hang thang(T09) - So tien lai trong ky ( T11)
			result = this.getPrincipalAndMonthlyInterest(payload,terorHandling) - this.getProfitAmount(payload, terorHandling);
		} else result = -this.lstRawDataDTO.get(terorHandling -1).getDebt(); 
		result = (double) (Math.round(result * 100))/100;
		return result;
	}

	/**
	 * @param payload
	 * @param teror
	 * @return So tien lai
	 */
	public Double getProfitAmount(RepaymentSchedulePayload payload, Integer terorHandling){
		Double result;
		if(terorHandling == 0) result = new Double(0);
		else if(terorHandling > 0 && terorHandling < payload.getTenor()){
			// Lay bien so T06 
			/*Double duNoKyTruoc =  this.getDebt(payload, terorHandling-1);*/
			Double duNoKyTruoc =  this.lstRawDataDTO.get(terorHandling-1).getDebt();
			/*= - T06*(T08 -T02)*Int Rate/365*/
			Integer subtractDate = DateUtil.subtract(lstDateOfDebtReceipt.get(terorHandling), lstDateOfDebtReceipt.get(terorHandling-1));
			result = -duNoKyTruoc * subtractDate * payload.getIntRate() / 365;
			if(terorHandling == 6) 
				System.out.println("terorHandling");
		} else 
			result =  -this.lstRawDataDTO.get(terorHandling-1).getDebt() * (DateUtil.subtract(lstDateOfDebtReceipt.get(terorHandling), lstDateOfDebtReceipt.get(terorHandling-1)))*payload.getIntRate()/365;
		result = (double) (Math.round(result * 100))/100;
		System.out.println("So tien lai tai ky : " + terorHandling +" la :  " + result);
		return result;
	}
	
	
	/**
	 * @param payload
	 * @param teror
	 * @return Du no
	 */
	public Double getDebt(RepaymentSchedulePayload payload, Integer terorHandling){
		Double result;
		if(terorHandling == 0) result = -payload.getCurrentOutstanding();
		else if(terorHandling > 0 && terorHandling < payload.getTenor()){
			// T06 + T10 
			 result = this.lstRawDataDTO.get(terorHandling-1).getDebt() + this.getOriginalAmount(payload, terorHandling); 
			 if(terorHandling == 6) 
					System.out.println("terorHandling");
		}
		else result = (double) 0;
		result = (double) (Math.round(result * 100))/100;
		
		System.out.println("So tien du no tai ky : " + terorHandling +" la :  " + result);
		return result;
	}
	
	/**
	 * @param payload
	 * @return Ngay thanh toan hang thang
	 */
	public List<Date> getDateOfDebtReceipt(RepaymentSchedulePayload payload) {
		Date maturityDateFinal;
		Date ngayNhanNo = payload.getValueDate();
		Date ngayDaoHanDau = ngayNhanNo;
		Integer teror = payload.getTenor();
		Calendar cal = Calendar.getInstance();
		if(ngayNhanNo.getDate() <=20) {
		    cal.setTime(ngayNhanNo);
		    cal.add(Calendar.MONTH, teror);
		    maturityDateFinal = cal.getTime();  
		} else {
		    cal.setTime(ngayNhanNo);
		    cal.add(Calendar.DATE, -20);
		    cal.add(Calendar.MONTH, teror+1);
		    maturityDateFinal = cal.getTime();
		}
		List<Date> lstDateOfDebtReceipt = new ArrayList<Date>();
		lstDateOfDebtReceipt.add(payload.getValueDate());
		// Xac dinh ngay dao han dau tien T08
		if(ngayNhanNo.getDate() <=20) {
			cal.setTime(ngayNhanNo);
			cal.set(Calendar.MONTH, ngayNhanNo.getMonth()+1);
			cal.set(Calendar.DATE, maturityDateFinal.getDate());
			ngayDaoHanDau = cal.getTime();
		} else {
			cal.setTime(ngayNhanNo);
			cal.set(Calendar.MONTH, ngayNhanNo.getMonth()+2);
			cal.set(Calendar.DATE, maturityDateFinal.getDate());
			ngayDaoHanDau = cal.getTime();
		}
		lstDateOfDebtReceipt.add(ngayDaoHanDau);
		for(int i = 2; i<teror;i++) {
			cal.setTime(lstDateOfDebtReceipt.get(i-1));
			cal.set(Calendar.MONTH, lstDateOfDebtReceipt.get(i-1).getMonth()+1);
			lstDateOfDebtReceipt.add(cal.getTime());
		}
		// Ngay nhan no cuoi bang ngay maturityDateFinal
		lstDateOfDebtReceipt.add(maturityDateFinal);
		this.lstDateOfDebtReceipt = lstDateOfDebtReceipt;
		JSONFactory.toJson(lstDateOfDebtReceipt);
		return lstDateOfDebtReceipt;
	}

	public Double getThePaymentAmountEachPeriod() {
		return thePaymentAmountEachPeriod;
	}

	public void setThePaymentAmountEachPeriod(Double thePaymentAmountEachPeriod) {
		this.thePaymentAmountEachPeriod = thePaymentAmountEachPeriod;
	}
	
	
}
