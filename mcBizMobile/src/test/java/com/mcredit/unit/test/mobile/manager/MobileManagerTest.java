package com.mcredit.unit.test.mobile.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.Product;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.model.object.mobile.dto.TestListCaseNoteDTO;
import com.mcredit.sharedbiz.cache.CompanyCacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.unit.test.mobile.aggregate.MobileAggregateTest;
import com.mcredit.unit.test.mobile.factory.MobileFactoryTest;

public class MobileManagerTest extends BaseManager {

	private MobileAggregateTest _mobileAggTest = null;
	
	public MobileManagerTest() {
		_mobileAggTest =  MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest);
	}
	
	/**
	 * Function set null for Imei
	 * 
	 * @author sonhv.ho
	 * @param 
	 * @return Boolean
	 * @throws Exception
	 */
	public Boolean setNullForImei(String loginId) throws Exception {
		return this.tryCatch( () -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).setNullForImei(loginId);
		});
	}
	
	public List<Long> addUplCreditAppRequest(UplCreditAppRequest uplTrue_zero, UplCreditAppRequest uplTrue_one, UplCreditAppRequest uplFalse) throws Exception {
		return this.tryCatch(() -> {
			Long idTrue_zero = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(uplTrue_zero);
			Long idTrue_one = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(uplTrue_one);
			Long idFalse = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(uplFalse);
			List<Long> res = new ArrayList<Long>(Arrays.asList(idTrue_zero, idTrue_one, idFalse));
			return res;
		});
	}
	
	public List<Long> addUplCreditAppRequest(UplCreditAppRequest uplTrue, UplCreditAppRequest uplFalse) throws Exception {
		return this.tryCatch(() -> {
			Long idTrue = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(uplTrue);
			Long idFalse = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(uplFalse);
			List<Long> res = new ArrayList<Long>(Arrays.asList(idTrue, idFalse));
			return res;
		});
	}
	
	public Long addUplCreditAppRequest(UplCreditAppRequest upl) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addUplCreditAppRequest(upl);
		});
	}
	
	public int removeUplCreditAppRequest(HashSet<Long> lstRemoveId) throws Exception {
		return this.tryCatch(() -> {
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).deleteUplCreditAppRequest(lstRemoveId);
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).deleteUplCreditAppFiles(lstRemoveId);
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).deleteMessageLog(lstRemoveId);
			
			return 1;
		});
	}
	
	public UplCreditAppRequest getReturnedCaseAfterUploaded(Long idTrue) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getUplCreditAppRequest(idTrue);
		});
	}
	
	public CategoryDTO getCompanyInfo(String taxNumber) {
		return CompanyCacheManager.getInstance().checkCategoryById(taxNumber);
	}
	
	public void reInstance() {
		this.uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
	}
	
	public Users getUserByLoginId(String loginId) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getUserByLoginId(loginId);
		});
	}
	
	public MessageLog createDataForCreateCaseBPMJob(String loginId) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).createDataForCreateCaseBPMJob(loginId);
		});
	}
	
	public Boolean deleteDataForCreateCaseBPMJob(MessageLog messageLog) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).deleteDataForCreateCaseBPMJob(messageLog);
		});
	}

	public MessageLog getMessageLogCancelCase(Long idTrue) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getMessageLog(idTrue);
		});
	}
	
	public MessageLog getMessageLogNotification(Long id) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getMessageLogNotification(id);
		});
	}

	public List<Long> addProducts(Product pTrue, Product pFalse) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addProducts(pTrue, pFalse);
		});
		
	}
	
	public TestListCaseNoteDTO getAppNumberForTestGetCase() throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getDateForTestGetCaseNote();
		});
	}
	
	public List<MessageLog> createDataForAbortCase() throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).createDataForAbortCaseBPMJob();
		});
		
	}
	
	public int removeDataForAbortCase(List<String> lstId) throws Exception {
		return this.tryCatch(() -> {
			 MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).removeMessageLogById(lstId);
			 return 1;
		});
		
	}
	
	public List<Integer> addProducts() throws Exception {
		return this.tryCatch(() -> {
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addProducts(1000000,100,"C0000004","ccy");
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addProducts(1000001,100,"M0000001","ccy");
			List<Integer> res = new ArrayList<Integer>(Arrays.asList(1000000, 1000001));
			return res;
		});
		
	}

	public int removeProducts(List<Integer> lstRemoveProduct) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).removeProducts(lstRemoveProduct);
		});
		
	}

	public String getShopCode() throws Exception {
		return this.tryCatch(() -> {
			String shopCode = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getShopCode();
			
			return shopCode;
		});
	}
	
	public String getCompanyTaxNumber() throws Exception {
		return this.tryCatch(() -> {
			String companyTaxNumber = MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getCompanyTaxNumber();
			
			return companyTaxNumber;
		});
	}
	
	public Long addMessageLog(MessageLog msgLog) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).addMessageLog(msgLog);
		});
	}
	
	public int createDataForSyncFile() throws Exception {
		return this.tryCatch(() -> {
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).createDataForTestSyncFile();
			return 1;
		});
	}
	
	public int removeDataForSyncFile() throws Exception {
		return this.tryCatch(() -> {
			MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).removeTestSyncFile();
			return 2;
		});
	}
	
	public Long getSaleId(String loginId) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getSaleId(loginId);
		});
	}

	public Object[] getEmpCodeEmpId(String loginId) throws Exception {
		return this.tryCatch(() -> {
			return MobileFactoryTest.getMobileAggregateTest(this.uok.mobileTest).getEmpCodeEmpId(loginId);
		});
	}
	
}
