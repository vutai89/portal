package com.mcredit.business.audit.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.sharedbiz.cache.CacheManager;

public class VNPOST17Partner extends PartnerBase {

	private final static String dateFormat = "ddMMyyyy";
	private final static SimpleDateFormat dfVNPOST = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat df = new SimpleDateFormat(dateFormat);
	
	@Override
	public HashMap<String, Audit> readDetailFile(String day, List<Audit> duplicateRecords) throws Exception {
		day = dfVNPOST.format(df.parse(day));
		HashMap<String, Audit> result = new HashMap<>();

		readConfig(CacheManager.Parameters().findParamValueAsString(ParametersName.AUDIT_VNPOST_17H_CONFIG).toString());
		HashMap<DynamicFile, Object> res = getFile(day);

		for (DynamicFile df : res.keySet()) {
			Object value = res.get(df);
			String filePath = (String) value;

			// read zip file
			@SuppressWarnings("resource")
			ZipFile zip = new ZipFile(filePath);
			Enumeration<? extends ZipEntry> entries = zip.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				HashMap<String, Audit> lstAudit = CommonUtils.getTxtFiles(zip.getInputStream(entry), entry.getName(),
						AuditEnum.TIME_CONTROL_17h.value(), duplicateRecords);
				result.putAll(lstAudit);
			}
			new File((String) res.get(df)).deleteOnExit();
		}

		return result;
	}

}
