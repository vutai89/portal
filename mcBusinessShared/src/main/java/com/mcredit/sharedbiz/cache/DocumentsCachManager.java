package com.mcredit.sharedbiz.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcredit.common.Labels;
import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.mobile.dto.DocumentDTO;
import com.mcredit.model.object.mobile.dto.DocumentTypeDTO;
import com.mcredit.model.object.mobile.enums.DocumentCodeEnum;
import com.mcredit.model.object.mobile.enums.SubFolderEnum;

public class DocumentsCachManager implements IDataCache {
	private UnitOfWork uok = null;
	private Map<String, DocumentTypeDTO> mapDocumentCache;
	
	private static DocumentsCachManager instance = null;
	
	private DocumentsCachManager() {
		initCache();
	}
	
	public static synchronized DocumentsCachManager getInstance() {
		if (instance == null) {
			synchronized (DocumentsCachManager.class) {
				if (null == instance) {
					instance = new DocumentsCachManager();
				}
			}
		}
		return instance;
	}

	@Override
	public void initCache() {
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<DocumentDTO> lstDoc = UnitOfWorkHelper.tryCatch(uok, ()->{
				return uok.common.getDocumentRepository().listDocument();
			});

			mapDocumentCache = new HashMap<String, DocumentTypeDTO>();

			if(lstDoc!= null && lstDoc.size() > 0){
				for (DocumentDTO item : lstDoc) {
					DocumentCodeEnum docCode = DocumentCodeEnum.from(item.getDocumentCode());
					DocumentTypeDTO docType;
					if (DocumentCodeEnum.AppointDecision == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.QDBNTCCT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BankAccountStatement == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GKS"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BirthCertificate == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.QDBNTCCT"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.BusinessConfirmation == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.XNBQL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BusinessFeeInvoice == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDNPBQL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BusinessLicense == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.DKKD"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BusinessLocationPhoto == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HADDKD"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BusinessPlaceProof == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GCMQSDSCDKD"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.BusinessTaxReceipt == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.CTNT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.CableTelevisionBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDTHC"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.CivicIdentity == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.CCCD"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.CustomerInformationSheet == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.PTTKH"), SubFolderEnum.CreditDocuments.value());
					else if (DocumentCodeEnum.DrivingLicense == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GPLX"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.ElectricBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDTD"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.EmployeConfirmation == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.XNCT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.FacePhoto == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HAKH"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.FamilyBook == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.SHK"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.HealthInsuranceCard == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.TBHYT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InsuranceCard == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.TBHYT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.HomeMortgageContract == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDTC"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.HomeOwnershipCertification == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GCNQSDD"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.HomePhoneBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDDTCD"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.IdentityCard == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.CMND"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.IncreaseSalaryDecision == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.QDNL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InsuranceFeeInvoice == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDDPBH"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InsuranceFeeOtherDocument == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GTDPBHK"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InsuranceFeePaymentConfirmation == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.XNDP"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InsuranceFeeReceipt == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.BLDPBH"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.InternetBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDI"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.JobCofirmationInfo == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GXNTTVL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.LabourContract == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDLD"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.LifeInsuranceContract == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDBHNT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.MarriageCertificate == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.DKKH"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.MilitaryIdentity == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.CMQD"),SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.MobilePhoneBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDDTDDTS"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.NotarizedHomeSalesContract == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDMBCN"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.Other == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.KHAC"), SubFolderEnum.OtherDocuments.value());
					else if (DocumentCodeEnum.OtherProofOfIncome == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.KHACTN"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.OtherProofOfWork == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.KHACCV"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.Passport == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HC"), SubFolderEnum.CustomerIdentification.value());
					else if (DocumentCodeEnum.PromotionDecision == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.QDTC"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.ResidenceConfirmationOfHeadUnit == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GXNCTCTTDV"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.SalaryConfirmation == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.XNL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.SalarySlip == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.PLBL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.SalaryStatement == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.SKTKL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.SalaryStatementWithPayeeName == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.SKTKLCTDVCL"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.StatementPaymentAccount == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.SKTKTT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.TaxRegistrationCertificate == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GCNDKT"), SubFolderEnum.ProofOfIncome.value());
					else if (DocumentCodeEnum.TemporaryResidenceBook == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.STT"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.TemporaryResidenceCard == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.TTT"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.TemporaryResidenceConfirmation == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.GXNTT"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.WaterBill == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.HDN"), SubFolderEnum.ProofOfAddress.value());
					else if (DocumentCodeEnum.WorkTransferDecision == docCode)
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.QDBC"), SubFolderEnum.ProofOfAddress.value());
					else
						docType = new DocumentTypeDTO(item.getId(), item.getDocumentCode(),Labels.getString("lable.mfs.doctype.KHAC"), SubFolderEnum.OtherDocuments.value());
					mapDocumentCache.put(item.getDocumentCode(), docType);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Long getDocumentIdByCode(String docCode) {
		try {
			if (mapDocumentCache == null)
				initCache();
				for (Map.Entry<String, DocumentTypeDTO> entry : mapDocumentCache.entrySet()) {
					if(entry.getKey().equals(docCode))
						return entry.getValue().getId();
				}
		} catch (Exception e) {
			System.out.println("findDocument.ex: " + e.toString());
		}
		
		return null;
	}
	
	public DocumentTypeDTO getDocumentByCode(String docCode) {
		try {
			if (mapDocumentCache == null)
				initCache();
				for (Map.Entry<String, DocumentTypeDTO> entry : mapDocumentCache.entrySet()) {
					if(entry.getKey().equals(docCode))
						return entry.getValue();
				}
		} catch (Exception e) {
			System.out.println("findDocument.ex: " + e.toString());
		}
		
		return null;
	}
	
	public String getDocumentCodeById(Long id) {
		try {
			if (mapDocumentCache == null)
				initCache();
				for (Map.Entry<String, DocumentTypeDTO> entry : mapDocumentCache.entrySet()) {
					if(entry.getValue().getId().equals(id))
						return entry.getKey();
				}
		} catch (Exception e) {
			System.out.println("findDocument.ex: " + e.toString());
		}
		
		return null;
	}
	
	public DocumentTypeDTO groupDocument(String docCode){
		try {
			if (mapDocumentCache == null)
				initCache();
				for (Map.Entry<String, DocumentTypeDTO> entry : mapDocumentCache.entrySet()) {
					if(entry.getKey().equals(docCode))
						return entry.getValue();
				}
		} catch (Exception e) {
			System.out.println("findDocument.ex: " + e.toString());
		}
		return null;
	}
	
	
	public HashMap<String, Long> hashDocumentIdByLstCode(List<String> docCode) {
		try {
			
			if (mapDocumentCache == null)
				initCache();
			
			HashMap<String, Long> out = new HashMap<>();
			
			for (String string : docCode) {
				DocumentTypeDTO tmp  = mapDocumentCache.get(string);
				if(null != tmp ){
					out.put(string, tmp.getId());
				}
			}
			
			return out ;

		} catch (Exception e) {
			System.out.println("findDocument.ex: " + e.toString());
		}
		
		return null;
	}

	@Override
	public void refresh() {
		this.initCache();
	}
}

