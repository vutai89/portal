/**
 * 
 */
package com.mcredit.business.job.createCard;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import com.mcredit.business.customer.CustomerAggregate;
import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.createCard.dto.CreateCustomerResultDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.customer.entity.CustomerPersonalInfo;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.util.MessageTranslator;
import com.mcredit.util.StringUtils;

/**
 * @author anhdv.ho
 *
 */
@Transactional(rollbackOn = ISRestCoreException.class)
public class CreateCustomerOnCore extends CallBack {

	BasedHttpClient bs = new BasedHttpClient();
	GetPropertiesValues prop = new GetPropertiesValues();

	@Override
	public void execute(CreateCardDTO createcardDTO) throws Exception {

		if (null == createcardDTO || createcardDTO.getMessageLog().getRelationId().trim().isEmpty())
			throw new Exception("Input message malformed!");

		String[] path = { "CustomerInformation.BusinessType[1].Code", "CustomerInformation.Person.PersonalID.IDType",
				"CustomerInformation.Person.PersonalID.IDIssuePlace" };
		String[] categories = { "INDUSTRY", "IDTYP", "IDPLACE" };
		String[] AddressFileds = { "CustomerInformation.ContactInfo.Address.FullAddress" };

		String[] codeTableValue_systemDefineFields = { "CustomerInformation.Person.Gender" };
		String[] categories_systemDefineFields = { "GENDER" };
		String[] systemCode_systemDefineFields = { "T24" };

		String translatedRequest = MessageTranslator.translateCodeValue(createcardDTO.getMessageLog().getMsgRequest(),
				path, categories);
		translatedRequest = MessageTranslator.translateAddress(translatedRequest, AddressFileds);
		translatedRequest = MessageTranslator.translateSystemDefineFields(translatedRequest,
				codeTableValue_systemDefineFields, categories_systemDefineFields, systemCode_systemDefineFields);

		System.out.println("create_customer-" + createcardDTO.getMessageLog().getTransId() + "-payload:" +translatedRequest);
		createcardDTO.getMessageLog().setProcessTime(new Timestamp(new Date().getTime()));
		String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST)
				+ BusinessConstant.T24_CREATE_CUSSTOMER;
		ApiResult result = bs.doPost(url, translatedRequest, ContentType.Json, AcceptType.Json);
		createcardDTO.getMessageLog().setMsgResponse(result.getBodyContent());
		createcardDTO.getMessageLog().setResponseCode(String.valueOf(result.getCode()));
		createcardDTO.getMessageLog().setResponseTime(new Timestamp(new Date().getTime()));

		CreateCustomerResultDTO createCustomerOutput = bs.toObject(CreateCustomerResultDTO.class, result.getBodyContent(), BusinessConstant.ESB_RESULT_JSON);
		
		if (!result.getStatus()) {
			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
			throw new Exception(createCustomerOutput.getReturnCode() + " - " + createCustomerOutput.getReturnMes());
		}

//		if (createCustomerOutput.getErrorStatus() != null
//				&& createCustomerOutput.getErrorStatus().equals(BusinessConstant.RESPONSE_OK + "")) {
		if(result.getStatus()) {
			createcardDTO.getMessageLog().setResponseErrorDesc("");
			createcardDTO.setCoreCustCode(createCustomerOutput.getCustomerid());
			createcardDTO.getMessageLog().setResponsePayloadId(createCustomerOutput.getCustomerid());

			updateCoreCustCodeToMcPortal(createcardDTO);
			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.SUCCESS.value());
		} else {
			createcardDTO.getMessageLog().setMsgStatus(MsgStatus.ERROR.value());
			createcardDTO.getMessageLog().setResponseErrorDesc("Payload Malformed!");
			throw new Exception("Payload Malformed!");
		}
	}

	public void updateCoreCustCodeToMcPortal(CreateCardDTO createcardDTO) throws Exception {

		try {
			if (createcardDTO != null) {
				if (!StringUtils.isNullOrEmpty(createcardDTO.getCoreCustCode())) {
					UnitOfWorkCustomer owk = new UnitOfWorkCustomer();
					owk.start();

					CustomerAggregate customerAggregate = new CustomerAggregate(owk);
					Long id = customerAggregate
							.findCustIdByRelationIdMessageLog(createcardDTO.getMessageLog().getRelationId());

					System.out.println("CustomerID:" + id.toString());
					customerAggregate.setPersonalInfo(new CustomerPersonalInfo());
					customerAggregate.getPersonalInfo().setId(id);
					customerAggregate.getPersonalInfo().setCoreCustCode(createcardDTO.getCoreCustCode());
					customerAggregate.updateCoreCustCode();
					owk.commit();

				}
			}
		} catch (Exception ex) {

			System.out.println("[Cron Job] CreateCustomerOnCore.updateCoreCustCodeToMcPortal.Ex: " + ex.toString());

			throw new Exception(ex.getMessage());
		}
		return;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(new
		// CreateCustomerOnCore().updateCoreCustCodeToMcPortal(null));
		String msgRequest = "{\r\n  \"CustomerInformation\": {\r\n    \"CustomerID\": \"\",\r\n    \"MnemonicName\": \"\",\r\n    \"CustomerStatus\": \"\",\r\n    \"CustomerName\": {\r\n      \"ShortName\": \"AAA\",\r\n      \"VietnameseName\": \"\",\r\n      \"EnglishName\": \"AAA\"\r\n    },\r\n    \"BusinessType\": [{\r\n      \"Type\": \"\",\r\n      \"Description\": \"\",\r\n      \"Code\": \"\"\r\n    },{\r\n      \"Type\": \"1\",\r\n      \"Description\": \"\",\r\n      \"Code\": \"\"\r\n    }],\r\n    \"ContactDate\": \"\",\r\n    \"Nationality\": \"1131313\",\r\n    \"Residence\": \"11221\",\r\n    \"RelationshipManager\": {\r\n      \"Type\": \"11321\",\r\n      \"ID\": \"2113\",\r\n      \"Name\": \"\"\r\n    },\r\n    \"CustomerRelationship\": {\r\n      \"RelationType\": \"\",\r\n      \"RelationName\": \"\",\r\n      \"RelationCustomerId\": \"\"\r\n    },\r\n    \"ContactInfo\": {\r\n      \"Address\": {\r\n        \"AddressTypeCode\": \"\",\r\n        \"FullAddress\": {\"Addr\":\"a\",\"Ward\":\"\",\"District\":\"\",\"Province\":\"\"}\r\n      },\r\n      \"EmailAddress\": \"\",\r\n      \"Phone\": {\r\n        \"PhoneType\": \"\",\r\n        \"PhoneNo\": \"\"\r\n      }\r\n    },\r\n    \"BranchInfo\": {\r\n      \"Code\": \"1\",\r\n      \"Name\": \"\",\r\n      \"Mnemonic\": \"2\"\r\n    },\r\n    \"Person\": {\r\n      \"Gender\": \"2\",\r\n      \"DateOfBirth\": \"1991-02-15T00:00:00\",\r\n      \"PersonalID\": {\r\n        \"IDType\": \"CMT\",\r\n        \"IDCode\": \"21131465465\",\r\n        \"IDIssuePlace\": \"HN\",\r\n        \"IDIssueDate\": \"2018-02-15T15:20:51.031\"\r\n      }\r\n    }\r\n  },\r\n  \"Action\": \"Validate\",\r\n  \"T24User\": \"\"\r\n}";
		// System.out.println(msgRequest);
		CreateCardDTO createcardDTO = new CreateCardDTO();
		MessageLog mslg = new MessageLog();
		mslg.setRelationId("8g9084230489sdfsdf0980");
		mslg.setMsgRequest(msgRequest);
		createcardDTO.setMessageLog(mslg);
		createcardDTO.setCoreCustCode("32423423423");
		createcardDTO.getMessageLog().setRelationId("App ID");
		CreateCustomerOnCore cs = new CreateCustomerOnCore();
		// cs.updateCoreCustCodeToMcPortal(createcardDTO);
		cs.execute(createcardDTO);
	}
}
