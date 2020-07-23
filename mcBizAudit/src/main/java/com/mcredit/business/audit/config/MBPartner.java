package com.mcredit.business.audit.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.mcredit.business.audit.utils.CommonUtils;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.disaudit.AuditEnum;
import com.mcredit.model.enums.disaudit.FileFormat;
import com.mcredit.model.enums.disaudit.ThirdParty;
import com.mcredit.model.object.audit.Audit;
import com.mcredit.model.object.audit.DynamicFile;
import com.mcredit.sharedbiz.cache.CacheManager;

public class MBPartner extends PartnerBase {

	private final static String dateFormat = "ddMMyyyy";
	private final static SimpleDateFormat dfVNPOST = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat df = new SimpleDateFormat(dateFormat);

	@Override
	public HashMap<String, Audit> readDetailFile(String day, List<Audit> duplicateRecords) throws Exception {
		day = dfVNPOST.format(df.parse(day));

		HashMap<String, Audit> result = new HashMap<>();
		readConfig(CacheManager.Parameters().findParamValueAsString(ParametersName.AUDIT_MB_CONFIG).toString());
		HashMap<DynamicFile, Object> res = getFile(day);

		for (DynamicFile df : res.keySet()) {
			Object value = res.get(df);

			String[] lstRule = df.getConfig().split("\\|");
			String[] fieldName = new String[lstRule.length];
			Integer[] position = new Integer[lstRule.length];
			for (int i = 0; i < lstRule.length; i++) {
				String[] eachRule = lstRule[i].split(":");
				fieldName[i] = eachRule[0];
				position[i] = Integer.valueOf(eachRule[1]);
			}
			if (value instanceof String) { // duong dan file
				FileInputStream fstream = null;
				try {

					String path = (String) value;
					String type = "";
					if (path.contains(FileFormat.MB_CHI.value())) {
						type = AuditEnum.CHI.value();
					} else if (path.contains(FileFormat.MB_THU.value())) {
						type = AuditEnum.THU.value();
					}
					fstream = new FileInputStream(path);
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					// skip
					br.readLine();

					String strLine;
					// Read File Line By Line
					while ((strLine = br.readLine()) != null) {
						try {
							Audit audit = CommonUtils.convertData(strLine, fieldName, position,
									FileFormat.MB_SEPARATOR.value());
							audit.setType(type);
							audit.setPaymentChannelCode(ThirdParty.MB.value());
							audit.setAuditDate(audit.getAuditDate());
							audit.setStatus(AuditEnum.SUCCESS.value());
							audit.setTimeControl(AuditEnum.TIME_CONTROL_24h.value());
							if (result.containsKey(audit.getPartnerRefId())) {
								duplicateRecords.add(audit);
							} else {
								result.put(audit.getPartnerRefId(), audit);
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (null != fstream) {
						// Close the input stream
						fstream.close();
					}

				}

			}
			new File((String) value).deleteOnExit();
		}

		return result;

	}
}
