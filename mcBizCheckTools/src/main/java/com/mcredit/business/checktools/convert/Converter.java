package com.mcredit.business.checktools.convert;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.mcredit.business.checktools.object.ContractInfo;
import com.mcredit.business.checktools.utils.Constants;
import com.mcredit.model.dto.checktools.ConditionInitContract;
import com.mcredit.model.dto.pcb.request.RIReqInput;
import com.mcredit.model.dto.pcb.request.input.Address;
import com.mcredit.model.dto.pcb.request.input.Contract;
import com.mcredit.model.dto.pcb.request.input.Instalment;
import com.mcredit.model.dto.pcb.request.input.Main;
import com.mcredit.model.dto.pcb.request.input.Person;
import com.mcredit.model.dto.pcb.request.input.Subject;
import com.mcredit.model.enums.DuplicateContractDescription;
import com.mcredit.model.enums.RuleCode;
import com.mcredit.model.object.RuleObject;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class Converter {
	
	/**
	 * Lay trang thai cic pass or fail
	 * @param cicResult
	 * @return
	 */
	public static boolean getCICStatus(String cicResult) {
		switch(cicResult) {
		case "1":	// "Đang có dư nợ, không có nợ xấu hay nợ cần chú ý";
		case "4":	// "Có thông tin nhưng không có dự nợ";
		case "5":	// "Không có thông tin";
		case "6":	// "CIC lỗi";
			return true;
		
		case "2":	// "Đang có dư nợ, đang có nợ cần chú ý";
		case "3":	//"Đang có dư nợ, đang có nợ xấu";
			return false;
		
		default: 
			return true;
		}
	}
	
	/**
	 * Lay thong bao loi duplicate contract
	 * @param messageCode
	 * @return
	 */
	public static String getDuplicateContractMessage(String messageCode) {
		if (StringUtils.isNullOrEmpty(messageCode))
			return "Số CMND không đủ điều kiện vay";
		else if (DuplicateContractDescription.CASE_REJECT_IN_3_MONTHS.value().equals(messageCode))
			return "Số CMND đã bị từ chối cho vay - Không hợp lệ!";
		else if (DuplicateContractDescription.PAYMENT_TENOR_NOT_ENOUGH.value().equals(messageCode))
			return "Số CMND chưa đủ điều kiện được vay tiếp – Không hợp lệ!";
		else if (DuplicateContractDescription.CASE_PROCESSING.value().equals(messageCode))
			return "Số CMND có hồ sơ trên hệ thống - Không hợp lệ !";
		
		return null;
	}
	
	/**
	 * Lay thong bao canh bao duplicate contract
	 * @param listContractDuplicate
	 * @return
	 */
	public static String getDuplicateContractMessageFor3PaymentTenor(List<ContractInfo> listContractDuplicate) {
		String msg = null;
		for (ContractInfo ci : listContractDuplicate) {
			// chi can 1 TH co loi thi tra ve null, xac dinh co loi.
			if (StringUtils.isNullOrEmpty(ci.getDescription()) || DuplicateContractDescription.CASE_REJECT_IN_3_MONTHS.value().equals(ci.getDescription()) ||
					DuplicateContractDescription.PAYMENT_TENOR_NOT_ENOUGH.value().equals(ci.getDescription()) || DuplicateContractDescription.CASE_PROCESSING.value().equals(ci.getDescription()))
				return null;
			
			if (DuplicateContractDescription.PAYMENT_TENOR_EQUAL_3.value().equals(ci.getDescription()))
				msg = "Số CMND có khoản vay cũ = 3 kỳ, đề nghị upload phiếu nộp tiền tại checklist nếu đã thanh toán kỳ 4! ";
		}
		return msg;
	}
	
	/**
	 * Build object truy van thong tin tin dung tai PCB
	 * @param conditionInitContract
	 * @return
	 * @throws Exception
	 */
	public static RIReqInput getPCBReqInput(ConditionInitContract conditionInitContract) throws Exception {
		RIReqInput payload = new RIReqInput();
		Contract contract = new Contract();
		Subject subject = new Subject();
		
		Instalment instalment = new Instalment();
		instalment.setAmountFinancedCapital(conditionInitContract.getLoanAmount());
		instalment.setNumTotalInstalment(conditionInitContract.getLoanTenor());
		instalment.setCodPaymentPeriodicity("M");		// mac dinh chu ki thanh toan doc la M
		contract.setInstalment(instalment);
		
		String dateRequestContract = null;
		if (StringUtils.isNullOrEmpty(conditionInitContract.getDateRequestContract()))
			dateRequestContract = DateUtil.toString(new Date(), DateTimeFormat.ddMMyyyy.getDescription());
		else
			dateRequestContract = DateUtil.changeFormat(conditionInitContract.getDateRequestContract(), DateTimeFormat.dd_MM_yyyy.getDescription(), DateTimeFormat.ddMMyyyy.getDescription());
		contract.setDateRequestContract(dateRequestContract);
		contract.setOperationType(10);					// mac dinh loai hop dong la 10
		contract.setCodCurrency("VND");					// mac dinh ma tien te la VND
		
		
		Person person = new Person();
		person.setName(conditionInitContract.getCustomerName());
		person.setIDCard(conditionInitContract.getCitizenId());
//		person.setIDCard2("");
		person.setGender(conditionInitContract.getGender());
		person.setDateOfBirth(DateUtil.changeFormat(conditionInitContract.getDateOfBirth(), DateTimeFormat.dd_MM_yyyy.getDescription(), DateTimeFormat.ddMMyyyy.getDescription()));
//		person.getAddress().getMain().setFullAddress(conditionInitContract.getAddress());
		person.setCountryOfBirth("VN");					// mac dinh noi sinh la VN
		
		Address address = new Address();
		Main main = new Main();
		main.setFullAddress(conditionInitContract.getAddress());
		address.setMain(main);
		person.setAddress(address);
		subject.setPerson(person);
		
		payload.setContract(contract);
		payload.setSubject(subject);
		payload.setRole("A");		// mac dinh loai lien ket la A (A: applicant, C: Co-applicant, G: Guarantor)
		
		return payload;
	}
	
	public static RuleObject getRuleObjectCheckDtiPti(Double dti, Double pti, String productCode) {
		RuleObject object = new RuleObject();
		object.setRuleCode(RuleCode.CHECK_DTI_PTI.value());
		object.setDti(dti);
		object.setPti(pti);
		object.setProductCode(productCode);
		
		return object;
	}
	
	public static String buildErrorMsg(Set<String> listError) {
		if (listError.isEmpty())
			return null;
		
		StringBuilder sb = new StringBuilder();
        
		for (Iterator<String> iterator = listError.iterator(); iterator.hasNext();) {
        	String msg = (String) iterator.next();
        	if (sb.length() > 0 )
        		sb.append("\r\n");
        	sb.append(msg);
        }

		return sb.toString();
	}
	
	public static boolean checkCicWithLoanAmt(String cicResult, Long loanAmount) {
		String[] group1 = {"5", "2", "3"}; // 5-Không có thông tin, 2-Đang có dư nợ, đang có nợ cần chú ý; 3-Đang có dư nợ, đang có nợ xấu
		String[] group2 = {"4"}; // 4-Có thông tin nhưng không có dự nợ
		String[] group3 = {"4", "6"}; //1-Đang có dư nợ, không có nợ xấu hay nợ cần chú ý; 6-CIC lỗi
		String[] group4 = {"4", "1", "6"}; //4-Có thông tin nhưng ko có dư nợ; 1-Đang có dư nợ, không có nợ xấu hay nợ cần chú ý; 6-CIC lỗi
		if(Arrays.asList(group1).contains(cicResult) || (Arrays.asList(group2).contains(cicResult) && loanAmount < Constants.LOAN_AMOUNT_DEFAUT)) {
			return false;
		} else if((Arrays.asList(group3).contains(cicResult) && loanAmount < Constants.LOAN_AMOUNT_DEFAUT) 
				|| (Arrays.asList(group4).contains(cicResult) && loanAmount >= Constants.LOAN_AMOUNT_DEFAUT)) {
			return true;
		}
		return false;
	}
}
