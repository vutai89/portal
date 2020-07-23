package com.mcredit.sharedbiz.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.SystemDefineFieldsDTO;
import com.mcredit.model.dto.TranslateAddressDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.SystemDefineFieldsCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;

public class MessageTranslator {

	private static Object get(JSONObject json, String key) throws Exception {
		String[] paths = key.split("\\.");
		Object value = json;
		for (String path : paths) {
			Pattern p = Pattern.compile("\\w*\\d*\\[\\d+\\]");
			Matcher m = p.matcher(path);
			if (m.matches()) {

				int startbracket = path.indexOf("[");
				int endbracket = path.indexOf("]");

				String keyValue = path.substring(0, startbracket);
				int index = Integer.parseInt(path.substring(startbracket + 1, endbracket));
				try {
					JSONObject jsonObject = (JSONObject) value;
					JSONArray jsonObjectList = jsonObject.getJSONArray(keyValue);
					value = jsonObjectList.get(index);
				} catch (Exception e) {
					throw new Exception("JSONObject has no key: " + key);
				}
				continue;
			}

			try {
				JSONObject jsonObject = (JSONObject) value;
				value = jsonObject.get(path);
			} catch (Exception e) {
				throw new Exception("JSONObject has no key: " + key);
			}

		}
		return value;
	}

	private static <T> T get(JSONObject json, String key, Class<T> targetObject) throws Exception {
		String[] paths = key.split("\\.");
		Object value = json;
		for (String path : paths) {
			try {
				JSONObject jsonObject = (JSONObject) value;
				value = jsonObject.get(path);
			} catch (Exception e) {
				throw new Exception("JSONObject has no key: " + key);
			}

		}
		Gson gson = new GsonBuilder().create();

		T targetVariable = gson.fromJson(value.toString(), targetObject);

		return targetVariable;
	}

	private static void putRecursive(JSONObject json, String[] paths, Object value) throws Exception {
		if (paths.length == 0)
			throw new Exception("Invalid key");
		if (paths.length == 1) {
			json.put(paths[0], value);
			return;
		}
		try {
			Pattern p = Pattern.compile("\\w*\\d*\\[\\d+\\]");
			Matcher m = p.matcher(paths[0]);
			if (m.matches()) {

				int startbracket = paths[0].indexOf("[");
				int endbracket = paths[0].indexOf("]");

				String keyValue = paths[0].substring(0, startbracket);
				int index = Integer.parseInt(paths[0].substring(startbracket + 1, endbracket));

				JSONArray jsonObjectList = json.getJSONArray(keyValue);
				putRecursive(jsonObjectList.getJSONObject(index), Arrays.copyOfRange(paths, 1, paths.length), value);
				return;
			}

			JSONObject json2 = json.getJSONObject(paths[0]);
			putRecursive(json2, Arrays.copyOfRange(paths, 1, paths.length), value);
			json.put(paths[0], json2);
		} catch (Exception e) {
			throw new Exception("JSONObject has no key: " + paths[0]);
		}
	}

	private static void put(JSONObject json, String key, Object codeValue) throws Exception {
		String[] paths = key.split("\\.");
		if (paths.length == 1) {
			json.put(paths[0], codeValue);
			return;
		}
		putRecursive(json, paths, codeValue);
	}

	private static String getDesc1(String codeValue1, String category) {
		CodeTableCacheManager codeTable = CodeTableCacheManager.getInstance();
		List<CodeTableDTO> codeTableCategory = codeTable.getCodeByCategory(category);
		String codeValue2 = "";
		for (CodeTableDTO codetableDTO : codeTableCategory)
			if (codetableDTO.getCodeValue1().equals(codeValue1)) {
				codeValue2 = codetableDTO.getDescription1();
				break;
			}
		// String codeValue = codeTable.getCodeById(id).getCodeValue1();
		return codeValue2;

	}

	public static String translate(String inputString, String[] fields) throws Exception {
		CodeTableCacheManager codeTable = CodeTableCacheManager.getInstance();

		if (fields == null || fields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}
		for (String field : fields) {
			Integer id;

			try {
				id = (Integer) get(json, field);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

			String codeValue = codeTable.getCodeById(id).getCodeValue1();
			put(json, field, codeValue);
		}
		return json.toString();
	}

	public static String translateAccent(String inputString, String[] fields) throws Exception {

		if (fields == null || fields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}
		for (String field : fields) {
			String value;

			try {
				value = (String) get(json, field);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

			String nonAccentValue = removeAccent(value);
			put(json, field, nonAccentValue);
		}
		return json.toString();
	}

	public static String RemoveSpace(String inputString, String[] fields) throws Exception {

		if (fields == null || fields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}
		for (String field : fields) {
			String value;

			try {
				value = (String) get(json, field);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

			String nonSpaceValue = value.replace(" ", "");
			put(json, field, nonSpaceValue);
		}
		return json.toString();
	}

	public static String translateCodeValue(String inputString, String[] fields, String[] categories) throws Exception {
		CodeTableCacheManager codeTable = CodeTableCacheManager.getInstance();

		if (fields == null || fields.length <= 0 || categories == null || fields.length != categories.length)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String category = categories[i];
			String codeValue1;

			try {
				codeValue1 = get(json, field).toString();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			List<CodeTableDTO> codeTableCategory = codeTable.getCodeByCategory(category);
			String codeValue2 = null;
			for (CodeTableDTO codetableDTO : codeTableCategory)
				if (codetableDTO.getCodeValue1().equals(codeValue1)) {
					codeValue2 = codetableDTO.getCodeValue2();
					break;
				}
			if (codeValue2 != null)
				put(json, field, codeValue2);
		}
		return json.toString();
	}
	
	public static String translateSystemDefineFields(String inputString, String[] fields, String[] categories, String[] systemCode) throws Exception {
		CodeTableCacheManager codeTable = CodeTableCacheManager.getInstance();
		SystemDefineFieldsCacheManager systemDefineFieldsCacheManager = SystemDefineFieldsCacheManager.getInstance();

		if (fields == null || fields.length <= 0 || systemCode == null || fields.length != systemCode.length)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String category = categories[i];
			String system = systemCode[i];
			
			String codeValue1;

			try {
				codeValue1 = get(json, field).toString();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			
			List<CodeTableDTO> codeTableCategory = codeTable.getCodeByCategory(category);
			Integer codeTableId = null;
			for (CodeTableDTO codetableDTO : codeTableCategory)
				if (codetableDTO.getCodeValue1().equals(codeValue1)) {
					codeTableId = codetableDTO.getId();
					break;
				}			
			
			if(codeTableId != null)
			{
				SystemDefineFieldsDTO systemDefineFieldsDTO = systemDefineFieldsCacheManager.getSystemDefineFields(codeTableId, system);
				
				if (systemDefineFieldsDTO != null)
					put(json, field, systemDefineFieldsDTO.getSystemValue());	
			}
		}
		return json.toString();
	}

	public static String SetUpper(String inputString, String[] fields) throws Exception {

		if (fields == null || fields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}

		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			String nonUperField;

			try {
				nonUperField = get(json, field).toString();
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

			put(json, field, nonUperField.toUpperCase());
		}
		return json.toString();
	}

	public static String translateAddress(String inputString, String[] Addressfields) throws Exception {

		if (Addressfields == null || Addressfields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}
		for (String field : Addressfields) {
			TranslateAddressDTO Address;

			try {
				Address = get(json, field, TranslateAddressDTO.class);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}

			String Ward = getDesc1(Address.getWard(), "WARD");
			String District = getDesc1(Address.getDistrict(), "DISTRICT");
			String Province = getDesc1(Address.getProvince(), "PROVINCE");
			String FullAddress = Address.getAddr() + " " + Ward + " " + District + " " + Province;

			put(json, field, FullAddress);
		}
		return json.toString();
	}
	
	public static String translateAddressW4(String inputString, String[] Addressfields) throws Exception {

		if (Addressfields == null || Addressfields.length <= 0)
			return null;
		JSONObject json;
		try {
			json = new JSONObject(inputString);
		} catch (Exception e) {
			throw new Exception("Not a valid JSON");
		}
		for (String field : Addressfields) {
			TranslateAddressDTO Address;

			try {
				Address = get(json, field, TranslateAddressDTO.class);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
			
			String Ward = removeAccent(getDesc1(Address.getWard(), "WARD"));
			String District = removeAccent(getDesc1(Address.getDistrict(), "DISTRICT"));
			String Province = removeAccent(getDesc1(Address.getProvince(), "PROVINCE"));
			String FullAddress = removeAccent(Address.getAddr()) + " " + Ward + " " + District + " " + Province;

			put(json, field, FullAddress);
		}
		return json.toString();
	}

	/**
	 * Bo dau 1 chuoi
	 * 
	 * @param s
	 * @return
	 */
	public static String removeAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replace('\u0111','d').replace('\u0110','D');
	}

	public static Integer TransformCodeValue1(String codeValue1, String Category) throws ValidationException {
		if (codeValue1 == null || codeValue1.trim().equals("")) return null;
		List<CodeTableDTO> catCode = CacheManager.CodeTable().getCodeByCategory(Category);
		Integer result = null;
		for (CodeTableDTO CTDTO : catCode) 
			if (codeValue1.equals(CTDTO.getCodeValue1())) {result = CTDTO.getId(); break;}
		if (result == null) throw new ValidationException("There is no value " + codeValue1 + " in " + Category);
		return result;
		
	}
	
	public static void main(final String[] args) throws Exception {
		// //MSG_LOG_1
		// String jsonString =
		// "{\"CustomerInformation\":{\"CustomerID\":\"\",\"CustomerName\":{\"ShortName\":\"shortCustName\",\"VietnameseName\":\"custName\",\"EnglishName\":\"shortCustName\"},\"BusinessType\":[{\"Type\":\"SECTOR\",\"Code\":\"8000\"},{\"Type\":\"INDUSTRY\",\"Code\":1},{\"Type\":\"TARGET\",\"Code\":\"999\"}],\"ContactDate\":\"2018-05-26\",\"Nationality\":\"VN\",\"Residence\":\"VN\",\"CustomerRelationship\":[{\"RelationName\":\"refFullName1\"},{\"RelationName\":\"refFullName2\"}],\"ContactInfo\":{\"Address\":{\"AddressTypeCode\":\"CURRENT\",\"FullAddress\":{\"Addr\":\"10\",\"Ward\":\"32228\",\"District\":\"968\",\"Province\":\"91\"}},\"EmailAddress\":\"email@gmail.com\",\"Phone\":{\"PhoneType\":\"MOBILE\",\"PhoneNo\":\"0983404766\"}},\"Person\":{\"Gender\":\"2\",\"DateOfBirth\":\"1990-06-09\",\"PersonalID\":{\"IDType\":\"CMT\",\"IDCode\":\"101893531\",\"IDIssuePlace\":\"2\",\"IDIssueDate\":\"1990-06-06\"}}},\"Action\":\"CREATE\",\"T24User\":\"\"}";
		// String[] path = {"CustomerInformation.Person.Gender"};
		// String[] categories = {"GENDER"};
		// String[] AddressFileds =
		// {"CustomerInformation.ContactInfo.Address.FullAddress"};
		// String translatedRequest =
		// MessageTranslator.translateCodeValue(jsonString,path,categories);
		// translatedRequest =
		// MessageTranslator.translateAddress(translatedRequest,AddressFileds);
		// System.out.println(translatedRequest);
		//
		// //MSG_LOG_2
		// String jsonString2 =
		// "{\"RequestChannel\":\"MCR\",\"FunctionCode\":\"MCR_SetIssueCreditCard\",\"UserId\":\"Mcredit\",\"IssueType\":\"NEW\",\"ClientType\":\"PR\",\"IssueBranch\":\"VN0010888\",\"RequiredBranch\":\"VN0010001\",\"CardReceivedMethod\":\"TN\",\"StatementReceivedMethod\":\"Dia
		// chi
		// email\",\"RequestId\":\"35aa5dc0-2775-a432-721a-4f39b0939891\",\"BatchId\":\"MCR_20180704\",\"ClientNumber\":\"\",\"ClientName\":\"custName\",\"ClientRegNumber\":\"101893531\",\"ClientRegDate\":\"19900606\",\"ClientRegPlace\":\"2\",\"ClientPhoneNumber\":\"0983404766\",\"ClientEmail\":\"email@gmail.com\",\"ClientBirthDate\":\"19900609\",\"ClientGender\":\"2\",\"ClientAddress\":{\"Addr\":\"10\",\"Ward\":\"32228\",\"District\":\"968\",\"Province\":\"91\"},\"CardClass\":\"MBMC_JCB_CREDIT\",\"CardLevel\":\"CLASSIC\",\"CardType\":\"MAIN\",\"CreditLimit\":10000000,\"EmbossedName\":\"abc123\",\"IssueFee\":1,\"AnnualFee\":10,\"InsureFee\":10000,\"InsureFeeAmount\":100000,\"RmMain\":\"\",\"RmSub\":\"\",\"CardReceivedAddress\":{\"Addr\":\"012541502\",\"Ward\":\"32228\",\"District\":\"968\",\"Province\":\"91\"},\"StatementDebitAccNo\":\"\",\"ChannelData\":\"SellerId=1\"}";
		// String[] path2 =
		// {"ClientRegPlace","ClientGender","IssueFee","AnnualFee","InsureFee"};
		// String[] categories2 =
		// {"IDPLACE","GENDER","BOOLEAN","BOOLEAN","BOOLEAN"};
		// String[] addressFields2 = {"CardReceivedAddress","ClientAddress"};
		// String translatedRequest2 =
		// MessageTranslator.translateCodeValue(jsonString2,path2,categories2);
		// translatedRequest2 =
		// MessageTranslator.translateAddress(translatedRequest2,addressFields2);
		// System.out.println(translatedRequest2);

		// String key= "abc123[0]";
		// Pattern p = Pattern.compile("\\w+\\d+\\[\\d+\\]");
		// Matcher m = p.matcher("fsdf3423[0]");
		// boolean b = m.matches();
		//
		// int startbracket = key.indexOf("[");
		// int endbracket = key.indexOf("]");
		//
		// String keyValue = key.substring(0, startbracket);
		// int index = Integer.parseInt(key.substring(startbracket+1,
		// endbracket));
		//
		// System.out.println(keyValue);
		// System.out.println(index);

		String jsonString = "{\"Action\":\"CREATE\",\"T24User\":\"\",\"CustomerInformation\":{\"BusinessType\":[{\"Type\":\"SECTOR\",\"Code\":\"8000\"},{\"Type\":\"INDUSTRY\",\"Code\":\"M\"},{\"Type\":\"TARGET\",\"Code\":\"999\"}],\"CustomerRelationship\":[{\"RelationName\":\"refFullName1\"},{\"RelationName\":\"refFullName2\"}],\"Residence\":\"VN\",\"ContactInfo\":{\"Address\":{\"FullAddress\":{\"Ward\":\"32228\",\"District\":\"968\",\"Addr\":\"10\",\"Province\":\"91\"},\"AddressTypeCode\":\"CURRENT\"},\"Phone\":{\"PhoneType\":\"MOBILE\",\"PhoneNo\":\"0983404766\"},\"EmailAddress\":\"email@gmail.com\"},\"CustomerID\":\"\",\"CustomerName\":{\"VietnameseName\":\"custName\",\"ShortName\":\"shortCustName\",\"EnglishName\":\"shortCustName\"},\"ContactDate\":\"2018-05-26\",\"Person\":{\"DateOfBirth\":\"1994-06-01\",\"PersonalID\":{\"IDIssuePlace\":\"2\",\"IDCode\":\"101795526\",\"IDIssueDate\":\"1990-06-06\",\"IDType\":\"CMT\"},\"Gender\":\"2\"},\"Nationality\":\"VN\"}}";
		String[] paths = { "CustomerInformation.BusinessType[1].Code" };
		String[] cat = { "INDUSTRY" };
		System.out.println(get(new JSONObject(jsonString), paths[0]));
		System.out.println(translateCodeValue(jsonString, paths, cat));
		String test = "ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰự";
		System.out.println(removeAccent(test));
	}
}
