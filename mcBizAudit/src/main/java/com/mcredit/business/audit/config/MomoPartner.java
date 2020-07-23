package com.mcredit.business.audit.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.sharedbiz.cache.CacheManager;

public class MomoPartner extends PartnerBase {

	@Override
	public HashMap<String, Audit> readDetailFile(String day, List<Audit> duplicateRecords) throws Exception {
		HashMap<String, Audit> result = new HashMap<>();

		readConfig(CacheManager.Parameters().findParamValueAsString(ParametersName.AUDIT_MOMO_CONFIG).toString());
		HashMap<DynamicFile, Object> res = getFile(day);
		
		for (DynamicFile key : res.keySet()) {
			result.putAll(readExcelFile(key, res.get(key), 1, ThirdParty.MOMO.value(), AuditEnum.THU.value(), duplicateRecords));
			new File((String) res.get(key)).deleteOnExit();
		}

		return result;
	}

}
