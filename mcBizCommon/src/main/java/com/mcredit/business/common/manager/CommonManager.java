package com.mcredit.business.common.manager;

import java.io.File;

import com.mcredit.business.common.CommonAggregate;
import com.mcredit.business.common.factory.CommonFactory;
import com.mcredit.business.common.validation.CommonValidation;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.common.CommoditiesDTO;
import com.mcredit.model.dto.common.CommonDTO;
import com.mcredit.model.dto.common.MessageLogDTO;
import com.mcredit.sharedbiz.manager.BaseManager;

public class CommonManager extends BaseManager {

	public IdDTO upsertCommon(CommonDTO request, String updateId) throws Exception {

		return this.tryCatch(() -> {

			IdDTO obj = new IdDTO();
			CommonValidation validate = new CommonValidation();
			validate.validateCommon(request, updateId);

			if (!validate.isValid())
				throw new Exception(validate.buildValidationMessage());

			this.uok.common.start();

			if (updateId != null)
				request.getCommoditiesDTO().setId(Long.parseLong(updateId));

			request.getMessageLogDTO().setId(Long.parseLong(updateId));
			CommonAggregate item = CommonFactory.getInstance(request, this.uok.common);

			item.upsertCommom();
			obj.setId(item.getCommodities().getId());

			return obj;
		});
	}

	public IdDTO upsertCommodities(CommoditiesDTO request, String updateId) throws Exception {

		return this.tryCatch(() -> {
			IdDTO obj = new IdDTO();

			// Validation check
			CommonValidation validate = new CommonValidation();
			validate.validateCommodities(request, updateId);

			if (!validate.isValid())
				throw new Exception(validate.buildValidationMessage());

			if (updateId != null)
				request.setId(Long.parseLong(updateId));

			CommonAggregate item = CommonFactory.getInstanceCommodities(request, this.uok.common);

			item.upsertCommom();
			obj.setId(item.getCommodities().getId());

			return obj;
		});
	}

	public IdDTO upsertMessageLog(MessageLogDTO request, String updateId) throws Exception {

		return this.tryCatch(() -> {
			IdDTO result = new IdDTO();

			// Validation check
			CommonValidation validate = new CommonValidation();
			validate.validateMessageLog(request, updateId);

			if (!validate.isValid())
				throw new Exception(validate.buildValidationMessage());

			if (updateId != null)
				request.setId(Long.parseLong(updateId));

			CommonAggregate item = CommonFactory.getInstanceMessageLog(request, this.uok.common);

			item.upsertMessageLog();

			result.setId(item.getMessageLog().getId());
			return result;
		});
	}

	public File downloadFile(String appId, String docType) throws Exception {
		return this.tryCatch(() -> {
			CommonValidation validation = new CommonValidation();
			validation.validate(appId, docType);

			if (!validation.isValid())
				throw new Exception(validation.buildValidationMessage());

			CommonAggregate item = CommonFactory.getInstance(this.uok.common);
			return item.downloadFileBPM(appId, docType);
		});
	}
}