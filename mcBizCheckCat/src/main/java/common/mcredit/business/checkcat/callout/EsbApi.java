package common.mcredit.business.checkcat.callout;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.RuleObject;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.util.JSONConverter;

public class EsbApi {
	
	private String _esbHost = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST);
	
	public ApiResult getRuleApi(RuleObject dto) throws Exception {
		try (BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doPost(this._esbHost + BusinessConstant.CAT_TOOL_RULES, JSONConverter.toJSON(dto).toString(), ContentType.Json);
		}
	}

}
