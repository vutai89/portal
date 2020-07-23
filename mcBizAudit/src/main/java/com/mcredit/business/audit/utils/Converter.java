package com.mcredit.business.audit.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.mcredit.common.Labels;
import com.mcredit.data.audit.entity.AuditPaymentDebtCollection;
import com.mcredit.model.dto.audit.AuditDuplicateDTO;
import com.mcredit.model.dto.audit.ConsolidatePaymentDTO;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.util.StringUtils;

import scala.collection.mutable.StringBuilder;

public class Converter {
	private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

	public static AuditPaymentDebtCollection convertThirdParty(Audit audit) throws ParseException {
		AuditPaymentDebtCollection payment = new AuditPaymentDebtCollection();
		payment.setType(audit.getType());
		payment.setContractDate(df.parse(audit.getAuditDate()));
		payment.setTimeControl(audit.getTimeControl());
		payment.setTpContractFee(Long.valueOf(audit.getPaymentAmount()));
		payment.setTpPartnerRefId(audit.getPartnerRefId());
		payment.setTpStatus(audit.getStatus());
		payment.setResult(AuditEnum.DUPLICATE.value());
		payment.setPartnerId(Integer.valueOf(audit.getPaymentChannelCode()));
		payment.setTpContractId(audit.getContractId());
		return payment;
	}

	public static void changeValue(List<AuditDuplicateDTO> duplicates) throws ParseException {
		for (AuditDuplicateDTO dup : duplicates) {
			if (dup.getType().equals(AuditEnum.CHI.value())) {
				dup.setType("CHI");
			} else {
				dup.setType("THU");
			}
			if (dup.getTpStatus().equals(AuditEnum.SUCCESS.value())) {
				dup.setTpStatus(Labels.getString("label.audit.status.success"));
			} else {
				dup.setTpStatus(Labels.getString("label.audit.status.fail"));
			}
			dup.setResult(Labels.getString("label.audit.status.duplicate"));
		}
	}

	public static ConsolidatePaymentDTO convertEmptyMC(Audit audit, String thirdParty, String time_control) {
		ConsolidatePaymentDTO auditDiff = new ConsolidatePaymentDTO();
		// co trong thirdParty nhung khong trong mCredit
		auditDiff.setTpPartnerRefId(audit.getPartnerRefId());
		auditDiff.setType(audit.getType());
		if (!StringUtils.isNullOrEmpty(audit.getPaymentAmount())) {
			auditDiff.setTpContractFee(Long.valueOf(audit.getPaymentAmount()));
		}
		auditDiff.setContractDate(audit.getAuditDate());
		auditDiff.setThirdParty(String.valueOf(thirdParty));
		auditDiff.setResult(AuditEnum.NOT_EXIST_MC.value());
		auditDiff.setTpStatus(audit.getStatus());
		auditDiff.setTpContractId(audit.getContractId());
		auditDiff.setTimeControl(time_control);
		return auditDiff;
	}

	public static ConsolidatePaymentDTO convertEmptyTP(Audit audit, String thirdParty, String time_control) {
		ConsolidatePaymentDTO auditDiff = new ConsolidatePaymentDTO();
		auditDiff.setMcPartnerRefId(audit.getPartnerRefId());
		auditDiff.setType(audit.getType());
		if (!StringUtils.isNullOrEmpty(audit.getPaymentAmount())) {
			auditDiff.setMcContractFee(Long.valueOf(audit.getPaymentAmount()));
		}
		auditDiff.setContractDate(audit.getAuditDate());
		auditDiff.setThirdParty(thirdParty);
		auditDiff.setResult(AuditEnum.NOT_EXIST_THIRD_PARTY.value());
		auditDiff.setMcStatus(audit.getStatus());
		auditDiff.setContractId(audit.getContractId());
		auditDiff.setTimeControl(time_control);
		return auditDiff;
	}

	public static ConsolidatePaymentDTO convertCompare(Audit audit, Audit auditTP, String thirdParty,
			String timeControl) {
		ConsolidatePaymentDTO auditDiff = new ConsolidatePaymentDTO();
		StringBuilder res = new StringBuilder();

		// so sanh mcredit va thirdParty
		if (!auditTP.getAuditDate().equals(audit.getAuditDate())) {
			// khac ngay giao dich
			res.append(AuditEnum.NOT_EQUAL_AUDIT_DATE.value()).append("|");
		}
		if (!auditTP.getContractId().equals(audit.getContractId())) {
			res.append(AuditEnum.NOT_EQUAL_CONTRACT_ID.value()).append("|");
		}
		if (!auditTP.getType().equals(audit.getType())) {
			res.append(AuditEnum.NOT_EQUAL_TYPE.value()).append("|");
		}
		if (!auditTP.getPaymentAmount().equals(audit.getPaymentAmount())) {
			res.append(AuditEnum.NOT_EQUAL_MONEY.value()).append("|");
		}
		if ((!audit.getPaymentChannelCode().equals(ThirdParty.MB.value())
				&& !audit.getPaymentChannelCode().equals(ThirdParty.VNPOST.value()))
				|| audit.getType().equals(AuditEnum.THU.value())) {
			if (!auditTP.getStatus().equals(audit.getStatus())) {
				res.append(AuditEnum.NOT_EQUAL_STATUS.value()).append("|");
			}
		}
		if (res.size() == 0) {
			res.append(AuditEnum.EQUAL.value());
		} else {
			res = new StringBuilder(res.substring(0, res.size() - 1));
		}

		// luu ket qua
		auditDiff.setMcPartnerRefId(audit.getPartnerRefId());
		auditDiff.setContractId(audit.getContractId());
		auditDiff.setType(audit.getType());
		if (!StringUtils.isNullOrEmpty(audit.getPaymentAmount())) {
			auditDiff.setMcContractFee(Long.valueOf(audit.getPaymentAmount()));
		}
		auditDiff.setContractDate(audit.getAuditDate());
		auditDiff.setMcStatus(audit.getStatus());
		auditDiff.setResult(res.toString());
		auditDiff.setThirdParty(thirdParty);
		auditDiff.setWorkFlow(audit.getWorkFlow());
		auditDiff.setTimeControl(timeControl);
		if (!res.toString().equals(AuditEnum.EQUAL.value())) {
			auditDiff.setTpPartnerRefId(auditTP.getPartnerRefId());
			if (!StringUtils.isNullOrEmpty(auditTP.getPaymentAmount())) {
				auditDiff.setTpContractFee(Long.valueOf(auditTP.getPaymentAmount()));
			}
			auditDiff.setTpStatus(auditTP.getStatus());
			auditDiff.setTpContractId(auditTP.getContractId());
		}

		return auditDiff;
	}
}
