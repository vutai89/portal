package com.mcredit.business.warehouse.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.mcredit.alfresco.factory.DocumentFactory;
import com.mcredit.business.warehouse.aggregate.WareHouseAggregate;
import com.mcredit.business.warehouse.convertor.Convertor;
import com.mcredit.business.warehouse.factory.WareHouseFactory;
import com.mcredit.business.warehouse.validation.WareHouseValidation;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.credit.entity.CreditApplicationBPM;
import com.mcredit.data.credit.entity.CreditApplicationSignature;
import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.data.warehouse.entity.WhCavetInfo;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.warehouse.ApproveBorrowCavetDTO;
import com.mcredit.model.dto.warehouse.CavetDTO;
import com.mcredit.model.dto.warehouse.ChangeDocumentDTO;
import com.mcredit.model.dto.warehouse.CheckRecordsCavetDTO;
import com.mcredit.model.dto.warehouse.ContractNumberScannerDTO;
import com.mcredit.model.dto.warehouse.DocumentsDTO;
import com.mcredit.model.dto.warehouse.DocumentsErrorsDTO;
import com.mcredit.model.dto.warehouse.QRCodeCheckDTO;
import com.mcredit.model.dto.warehouse.QRCodeDTO;
import com.mcredit.model.dto.warehouse.ResponseCheckDocumentDTO;
import com.mcredit.model.dto.warehouse.ResponseUpdateCavetDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverBorrowedDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHistoryDTO;
import com.mcredit.model.dto.warehouse.WareHousePayBackCavetDTO;
import com.mcredit.model.dto.warehouse.WhBorrowedDocumentDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.PagingTag;
import com.mcredit.model.enums.WHStep;
import com.mcredit.model.object.CreditApplicationSignatureOBJ;
import com.mcredit.model.object.ListObjectResult;
import com.mcredit.model.object.SearchCaseInput;
import com.mcredit.model.object.warehouse.DocumentTypeObject;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.LodgeResponseDTO;
import com.mcredit.model.object.warehouse.RemainDocument;
import com.mcredit.model.object.warehouse.RenewalDocumentDTO;
import com.mcredit.model.object.warehouse.ResultsDocumentInfo;
import com.mcredit.model.object.warehouse.WHAllocationDocInput;
import com.mcredit.model.object.warehouse.WareHouseCodeTableCacheDTO;
import com.mcredit.model.object.warehouse.WareHouseEnum;
import com.mcredit.model.object.warehouse.WareHouseMatrix;
import com.mcredit.model.object.warehouse.WareHousePayBackCavet;
import com.mcredit.model.object.warehouse.WareHousePayBackLetter;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.sharedbiz.aggregate.QRCodeKafkaProduceAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.cache.WHCodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.factory.AllocationFactory;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;

public class WareHouseManager extends BaseManager {

	private static ModelMapper _modelMapper = new ModelMapper();
	private WareHouseAggregate _whAgg = null;
	private CodeTableCacheManager _ctCache = CacheManager.CodeTable();
	private UserDTO _user;
	private QRCodeKafkaProduceAggregate _qrCodeAgg = null;
	private WareHouseValidation _whValidate = new WareHouseValidation();
	private WareHouseCodeTableCacheDTO wareHouseCodeTableID = WHCodeTableCacheManager.getInstance().getWareHouseCodeTableID();
	
	public WareHouseManager(UserDTO user) {
		_user = user;
		this._whAgg = new WareHouseAggregate(uok);
		_qrCodeAgg = new QRCodeKafkaProduceAggregate();
	}
	
	
	public ResponseSuccess updateQRCode(QRCodeCheckDTO qrCode) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.insertQRCode(qrCode);

			CreditApplicationSignature item = this._whAgg.updateQRCode(qrCode,this._user.getLoginId());
			
			CreditApplicationSignatureOBJ obj = _modelMapper.map(item, CreditApplicationSignatureOBJ.class);
			obj.setScannedBy(this._user.getLoginId());
			
			//send message to kafka
			_qrCodeAgg.pruduce(JSONConverter.toJSON(obj));
			
			return new ResponseSuccess();
		});
		
	}
	
	public ResponseSuccess scanContractNumber(ContractNumberScannerDTO input) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.scanContractNumber(input);

			CreditApplicationSignatureOBJ obj = new CreditApplicationSignatureOBJ();
			obj.setScannedBy(this._user.getLoginId());
			obj.setMcContractNumber(input.getContractNumber());
			
			//send message to kafka
			_qrCodeAgg.pruduce(JSONConverter.toJSON(obj));
			
			return new ResponseSuccess();
		});
		
	}
	
	public String createQRCode(QRCodeDTO data) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.createQRCode(data);
			
			CreditApplicationBPM bpm = this._whAgg.getCreditApplicationBPMBy(data.getAppId());
			
			if(bpm == null || bpm.getCreditAppId() == null)
				throw new ValidationException("BPM App Id is invalid.");
			
			return this._whAgg.insertQRCode(data,bpm);
		});
	}

	public List<Long> upsertDocument(DocumentsDTO payloadSaveDocuments) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateDocumentsDTO(payloadSaveDocuments);

			for (int i = 0; i < payloadSaveDocuments.getDocuments().size(); i++) {
				_whValidate.validateDocument(payloadSaveDocuments.getDocuments().get(i));
			}

			return this._whAgg.upsertDocument(payloadSaveDocuments, this._user);
		});
	}

	public ResponseCheckDocumentDTO updateDocumentErrors(DocumentsErrorsDTO documentsErrors) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.updateDocumentErrors(documentsErrors);
			
			if(documentsErrors.getWhDocumentId() != null){
				if(this._whAgg.checkPemisionData(documentsErrors.getWhDocumentId(), this._user.getId())== null){
					throw new ValidationException(Messages.getString("validate.field.pemision"));
				}
				
			}
			return this._whAgg.updateDocumentErrors(documentsErrors, this._user);
		});
	}

	public ResponseUpdateCavetDTO updateCavet(CavetDTO cavet) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateCavet(cavet);
			return this._whAgg.updateCavet(cavet, this._user);
		});
	}

	public String exportPaperReceipt(String contractNumber, String loginId) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateExportContract(contractNumber);
			return this._whAgg.getLstPaperReceipt(contractNumber, loginId);
		});
	}

	public String exportThankLetter(String contractNumber, int typeExport,String loginId) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateExportContract(contractNumber);
			return this._whAgg.getLstThankLetter(contractNumber, typeExport,loginId);
		});
	}

	public String exportHandover(List<WareHouseExportHandoverDTO> lstHandoverDTO) throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.getHandoverExport(lstHandoverDTO);
		});
	}

	public List<WareHousePayBackCavet> searchResulPayBackCavet(WareHousePayBackCavetDTO backCavet) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateSearchPayBackCavet(backCavet);
			return this._whAgg.getLstPayBackCavet(backCavet);
		});
	}

	public List<WareHousePayBackLetter> searchResulPayBackLetter(WareHousePayBackLetter letterDTO) throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.getLstPayBackLetter(letterDTO);
		});
	}

	public Object giveBackCavet(List<Long> listWhDocumentId, String userLogin) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateValidList(listWhDocumentId);
			return this._whAgg.giveBackCavet(listWhDocumentId, userLogin);
		});
	}

	public Object approveGiveBackCavet(List<ApproveBorrowCavetDTO> approveBorrowCavetDtoList, String status, String loginId) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateValidList(approveBorrowCavetDtoList);
			return this._whAgg.approveGiveBackCavet(approveBorrowCavetDtoList, status, loginId);
		});
	}

	public List<WareHouseMatrix> validateInput(String contractStatus, String cavetStatus, String cavetErr, String letterStatus, int docType)
			throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.getWareHouseMatrix(contractStatus, cavetStatus, cavetErr, letterStatus, docType);
		});
	}

	public Object seachCase(String step, SearchCaseInput seachCase, UserDTO userInfo, Integer size, Integer num) throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateSeachCaseInput(seachCase, step);
			
			Integer totalCount = null;
			boolean paging = false;
			List<WareHouseSeachObject> result = null;

			Integer pageSize = size != null ? size : PagingTag.PAGESIZE_DEFAULT_10.intValue();
			Integer pageNum = num != null ? num : PagingTag.PAGENUM_DEFAULT_1.intValue();

			WHStep _step = WHStep.from(step.toUpperCase());

			if (WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.LOAD_BATCH == _step || WHStep.LODGE_CAVET == _step || WHStep.RETURN_CAVET == _step 
					|| WHStep.APPROVE == _step|| WHStep.ALLOCATION == _step  ||  WHStep.BORROW_CAVET == _step ||  WHStep.THANK_LETTER == _step  ||  WHStep.RETURN_CAVET == _step ||   WHStep.APPROVE == _step  ) {
				result = this._whAgg.seachCase(_step, seachCase, userInfo,wareHouseCodeTableID, null, null);

				// for FOLLOWCASE
				if (WHStep.FOLLOWCASE == _step) {
					return Convertor.convertFrom(ManagerHelper.followCase(result), totalCount, pageSize, pageNum, false);
				}
			}

			if (WHStep.OPERATOR_TWO == _step || WHStep.LODGE_CONTRACT == _step || WHStep.BORROW_CONTRACT == _step) {
				paging = true;
				totalCount = this._whAgg.countSeachCase(_step, seachCase, userInfo,wareHouseCodeTableID);
				result = this._whAgg.seachCase(_step, seachCase, userInfo,wareHouseCodeTableID, pageSize, pageNum);
			}

			return Convertor.convertFrom(result, totalCount, pageSize, pageNum, paging);

		});
	}

	public Object renewalDocument(List<RenewalDocumentDTO> renewaldocumentInputs, String typeDocument, String userLogin)
			throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateRenewalDocument(renewaldocumentInputs, typeDocument);

			return this._whAgg.renewalDocument(renewaldocumentInputs, typeDocument, userLogin);
		});
	}

	public List<RemainDocument> findRemainDocumentAllocation() throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.findRemainDocumentAllocation();
		});
	}

	public LodgeResponseDTO lodgeDocument(LodgeDocumentDTO lodgeDocument, String userLogin) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateLodgeDocument(lodgeDocument);

			return WareHouseFactory.getDocumentLodgeAgg(uok.warehouse, lodgeDocument, userLogin).lodge();
		});
	}

	public Object findRemainDocument(String step, List<Long> caseReturn, UserDTO userInfo) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateFindRemainDocument(step, caseReturn);
			
			WHStep _step = WHStep.from(step.toUpperCase());

			return this._whAgg.findRemainDocument(_step, caseReturn, userInfo);
		});
	}

	public Object allocationDocument(String allocation, WHAllocationDocInput allocationDocumnetInputs, UserDTO userInfo)
			throws Exception {

		return this.tryCatch(() -> {
			_whValidate.validateAllocationDocument(allocation, allocationDocumnetInputs);

			ListObjectResult result = null;
			List<AllocationDetail> listAssign = AllocationFactory.getSupervisorAgg(null, uok.common)
					.validateUplDetailWH(allocationDocumnetInputs.lstwhDocId);

			if (allocation.equalsIgnoreCase("assign")) {

				_whValidate.validateListAssign(allocation, listAssign, allocationDocumnetInputs);

				result = AllocationFactory.getSupervisorAgg(null, uok.common)
						.wHAllocationAssign(allocationDocumnetInputs, userInfo.getLoginId());
			}

			if (allocation.equalsIgnoreCase("reassign")) {
				
				_whValidate.validateListAssign(allocation, listAssign, allocationDocumnetInputs);

				result = AllocationFactory.getSupervisorAgg(null, uok.common)
						.wHAllocationReAssign(allocationDocumnetInputs, userInfo.getLoginId());
			}

			if (result == null) {
				throw new ValidationException(Messages.getString("validation.field.notcorect", "result"));
			}

			Long statusAssign = _ctCache.getIdBy(CTCodeValue1.WH_DONE, CTCat.WH_ASS_STATUS);
			// assign
			Long idTypeAssign = _ctCache.getIdBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);

			List<Object> errorUpdate = result.getErrorUpdate();
			List<Object> sucsessUpdate = result.getSucsessUpdate();
			for (Object i : result.getSucsessUpdate()) {

				try {
					if (i != null)
						this._whAgg.updateAllocationStatus((long) i, statusAssign, idTypeAssign, allocation,
								userInfo.getLoginId());
				} catch (Exception e) {
					sucsessUpdate.remove(i);
					errorUpdate.add(i);
                    e.printStackTrace();
				}
			}
			return new ListObjectResult(errorUpdate, sucsessUpdate);
		});
	}

	public Object listDocumentType(String codeGroup, String category) throws Exception {
		return this.tryCatch(() -> {
			
			CTGroup group = CTGroup.from(codeGroup);
			CTCat cat = CTCat.from(category);
			
			_whValidate.validateListDocumentType(codeGroup, category);

			List<CodeTableDTO> ltsCode = _ctCache.getBy(group, cat);

			List<DocumentTypeObject> out = new ArrayList<>();
			if (ltsCode == null || ltsCode.size() < 1) {
				throw new ValidationException(Messages.getString("validation.field.mainMessage",
						Labels.getString("label.warehouse.check.data")));
			}

			ParametersCacheManager cache = CacheManager.Parameters();
			for (CodeTableDTO codeTableDTO : ltsCode) {
				DocumentTypeObject obj = _modelMapper.map(codeTableDTO, DocumentTypeObject.class);

				obj.setExtraDays(cache.findParamValueAsInteger(
						WareHouseEnum.SUB_DAYS_.stringValue() + (codeTableDTO.getCodeValue1())));
				out.add(obj);
			}

			return out;
		});
	}

	@SuppressWarnings("unused")
	public Object addBorrowedCAVET(List<WhBorrowedDocumentDTO> listBorrowedDocument, UserDTO user) throws Exception {
		return this.tryCatch(() -> {

			_whValidate.validateValidList(listBorrowedDocument);

			for (WhBorrowedDocumentDTO whBorrowedDocumentDTO : listBorrowedDocument) {
				_whValidate.validateAddWHBrrowedDocument(whBorrowedDocumentDTO);
			}

			List<Long> listId = new ArrayList<>();
                        int CAVET = 0;                        
			for (WhBorrowedDocumentDTO wbd : listBorrowedDocument) {
				listId.add(wbd.getWhDocId());
			}

			if (listBorrowedDocument != null) {
				List<WhBorrowedDocument> listBorrowd = Convertor.convertFrom(listBorrowedDocument);
				List<WhDocumentChange> listDocChange = new ArrayList<>();

				long approveStatus = _ctCache.getIdBy(CTCodeValue1.WH_WAIT, CTCat.WH_APR_STATUS);
				long idType = _ctCache.getIdBy(CTCodeValue1.WH_APP_CAVET_BR, CTCat.WH_CHAN_TYPE);
				long idTypeBorrow = _ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);

				for (int i = 0; i < listBorrowd.size(); i++) {
					listBorrowd.set(i, ManagerHelper.addAttributeBorrowedDoc(listBorrowd.get(i), approveStatus, idTypeBorrow, CAVET));
					WhDocumentChange whDocumentChange = ManagerHelper.createWHDocumentChange(listBorrowd.get(i).getWhDocId(),
							approveStatus, idType, user);
					listDocChange.add(whDocumentChange);
				}

				return this._whAgg.addDocumentBorrowed(listBorrowd, listDocChange,false);
			} else {
				return new ListObjectResult(); // why not return null ?
			}
		});

	}

	@SuppressWarnings("unused")
	public Object addBorrowedHSKV(List<WhBorrowedDocumentDTO> listBorrowedDocument, UserDTO user) throws Exception {
		return this.tryCatch(() -> {

			_whValidate.validateValidList(listBorrowedDocument);
			for (WhBorrowedDocumentDTO whBorrowedDocumentDTO : listBorrowedDocument) {
				_whValidate.validateAddWHBrrowedDocument(whBorrowedDocumentDTO);
			}

			if (listBorrowedDocument != null) {
				List<WhBorrowedDocument> listBorrowd = Convertor.convertFrom(listBorrowedDocument);
				List<WhDocumentChange> listDocChange = new ArrayList<>();
                                int DOCUMENT = 1;
				long idType = _ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW_HD, CTCat.WH_CHAN_TYPE);
				long id_code_table_doc_change = _ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);
				List<CodeTableDTO> arrStatusAppoint = _ctCache.getBy(CTGroup.WARE_HOUSE, CTCat.WH_LODGE);
				for (int i = 0; i < listBorrowd.size(); i++) {
					WhBorrowedDocument whBorrowedDoc = listBorrowd.get(i);
					CodeTableDTO getCodeValue = _ctCache.getbyID(whBorrowedDoc.getType().intValue());

					long approveStatus = ManagerHelper.getRenewalAppointStatus(getCodeValue, arrStatusAppoint);
					listBorrowd.set(i, ManagerHelper.addAttributeBorrowedDoc(listBorrowd.get(i), approveStatus, id_code_table_doc_change,DOCUMENT));
					WhDocumentChange whDocumentChange = ManagerHelper.createWHDocumentChange(listBorrowd.get(i).getWhDocId(),
							id_code_table_doc_change, idType, user);
					listDocChange.add(whDocumentChange);

					this._whAgg.updateDocStatus(whBorrowedDoc.getWhDocId(), whBorrowedDoc.getApproveStatus());
				}
				return (this._whAgg.addDocumentBorrowed(listBorrowd, listDocChange,true));
			} else {
				return new ListObjectResult();// why not return null ?
			}
		});

	}

	public Object renewalBorrowedDocument(List<WhBorrowedDocumentDTO> listIdDocument, UserDTO user) throws Exception {
		return this.tryCatch(() -> {

			ListObjectResult result = new ListObjectResult();
			List<Object> success = new ArrayList<>();

			_whValidate.validateValidList(listIdDocument);
			List<CodeTableDTO> arrStatusAppoint = _ctCache.getBy(CTGroup.WARE_HOUSE, CTCat.WH_LODGE);
			Long typeDocChanged = _ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW_CV, CTCat.WH_CHAN_TYPE);			
			long id_code_table_doc_change = _ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);
			for (int i = 0; i < listIdDocument.size(); i++) {
				WhBorrowedDocumentDTO whBorrowDoc = listIdDocument.get(i);
				CodeTableDTO getCodeValue = _ctCache.getbyID(whBorrowDoc.getType().intValue());

				long id_code_table = ManagerHelper.getRenewalAppointStatus(getCodeValue, arrStatusAppoint);
				WhDocumentChange whDocumentChange = ManagerHelper.createWHDocumentChange(whBorrowDoc.getWhDocId(), id_code_table_doc_change,
						typeDocChanged, user);

				this._whAgg.renewalDocumentBorrowed(
						DateUtil.addDay(new Date(), 10), whBorrowDoc.getWhDocId(),
						id_code_table, id_code_table_doc_change);
				this._whAgg.updateDocStatus(whBorrowDoc.getWhDocId(), id_code_table);
				this._whAgg.upsertDocChange(whDocumentChange);

				//Xoa cac trang thai da xu ly o man hinh van hanh 2 trong bang documentchange
				//Begin
				//Trang thai danh dau da xu ly hay chua
//				Long processStatus = ctCache.getIdBy(CTCodeValue1.WH_SAVE_AND_MOVE, CTCat.WH_CHAN_TYPE);
//				this._whAgg.deleteDocumentChangeByWhIdAndType(whBorrowDoc.getWhDocId(), processStatus);
				//End
				
				success.add(listIdDocument.get(i));
				result.setSucsessUpdate(success);
			}
			return result;
		});

	}

	public Object changeDocumentTypErr(ChangeDocumentDTO changeDocument, UserDTO currentUser) throws Exception {
		return this.tryCatch(() -> {
			List<Object> errorUpdate = new ArrayList<>();
			List<Object> sucsessUpdate = new ArrayList<>();

			Long documentType = _ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE);
			Long documentChangeType = _ctCache.getIdBy(CTCodeValue1.WH_DOC_TYPE_CHANGE, CTCat.WH_CHAN_TYPE);

			for (Long ojb : changeDocument.getLstData()) {
				try {
					this._whAgg.changeDocumentType(ojb, documentType, currentUser.getLoginId(),
							changeDocument.getContractCavetType(), documentChangeType);
					sucsessUpdate.add(ojb);

				} catch (Exception e) {
					e.printStackTrace();
					errorUpdate.add(ojb);
				}
			}
			return new ListObjectResult(errorUpdate, sucsessUpdate);
		});
	}

	public Object inputDocument(ChangeDocumentDTO changeDocument, UserDTO currentUser) throws Exception {
		return this.tryCatch(() -> {
			List<Object> errorUpdate = new ArrayList<>();
			List<Object> sucsessUpdate = new ArrayList<>();

			for (Long id : changeDocument.getLstData()) {
				try {
					WhDocument obj = this._whAgg.getById(id);

					if (obj == null) {
						throw new ValidationException(Messages.getString("validation.field.mainMessage",
								Labels.getString("label.warehouse.check.id")));
					}

					this._whAgg.cloneWhDocument(obj, currentUser.getLoginId());

					sucsessUpdate.add(id);
				} catch (Exception e) {
					errorUpdate.add(id);
				}
			}

			return new ListObjectResult(errorUpdate, sucsessUpdate);

		});
	}
	
	public Object changeDocLodge(ChangeDocumentDTO changeDocument, String loginId) throws Exception {
		return this.tryCatch(() -> {
			List<Object> errorUpdate = new ArrayList<>();
			List<Object> sucsessUpdate = new ArrayList<>();
			Long type = CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.WH_SAVE_AND_MOVE, CTCat.WH_CHAN_TYPE);
			
			for (Long id : changeDocument.getLstData()) {
				try {
					WhDocument obj = this._whAgg.getById(id);

					if (obj == null) {
						throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.check.id")));
					}

					this._whAgg.changeDocLodge(obj, changeDocument.getBillCode(), changeDocument.getDeliveryError(), loginId, type);

					sucsessUpdate.add(id);
				} catch (Exception e) {
					errorUpdate.add(id);
				}
			}

			return new ListObjectResult(errorUpdate, sucsessUpdate);

		});
	}
	
	public Object changeDocLodge1(ChangeDocumentDTO changeDocument, String loginId) throws Exception {
		return this.tryCatch(() -> {
			List<Object> errorUpdate = new ArrayList<>();
			List<Object> sucsessUpdate = new ArrayList<>();
			Long type = CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.WH_SAVE_AND_MOVE, CTCat.WH_CHAN_TYPE);
			
			for (Long id : changeDocument.getLstData()) {
				try {
					WhDocument obj = this._whAgg.getById(id);

					if (obj == null) {
						throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.check.id")));
					}

					this._whAgg.changeDocLodge1(obj, changeDocument.getBillCode(), changeDocument.getDeliveryError(), loginId,type);

					sucsessUpdate.add(id);
				} catch (Exception e) {
					errorUpdate.add(id);
				}
			}

			return new ListObjectResult(errorUpdate, sucsessUpdate);

		});
	}

	public Object approveReturnCavet(List<Long> listWhDocumentID) throws Exception {
		return this.tryCatch(() -> {

			_whValidate.validateValidList(listWhDocumentID);
			List<WhDocumentChange> listDocChange = new ArrayList<>();
			List<WhBorrowedDocument> listBorrowd = new ArrayList<>();

			Long id_code_table = _ctCache.getIdBy(CTCodeValue1.WH_WAIT_RETURN, CTCat.WH_RE_STATUS);
			Long type = _ctCache.getIdBy(CTCodeValue1.WH_APP_CAVET_R, CTCat.WH_CHAN_TYPE);
			Long typeReturn = _ctCache.getIdBy(CTCodeValue1.WH_APP_RETURN, CTCat.WH_CHAN_TYPE);

			for (int i = 0; i < listWhDocumentID.size(); i++) {
				WhDocumentChange whDocumentChange = new WhDocumentChange(listWhDocumentID.get(i), type, "", "", new Date(), id_code_table);
				listDocChange.add(whDocumentChange);
                                
				WhBorrowedDocument borrowedDocument = new WhBorrowedDocument(listWhDocumentID.get(i), id_code_table, typeReturn, new Date(),"A");
				listBorrowd.add(borrowedDocument);
                                
			}
                        this._whAgg.upsertWhBorrowedDocument(listBorrowd);
			return this._whAgg.approveReturnCavet(listDocChange);
                        
		});
	}

	public CheckRecordsCavetDTO getCheckRecordsCavet(Long whId) throws Exception {
		return this.tryCatch(() -> {
			_whValidate.validateGetCheckRecordsCavet(whId);
			
			//Begin : Neu giay to ve la giay to update loi thi lay thong tin cua ho so goc.
			WhDocument whDocument = new WhDocument();
			whDocument = this._whAgg.getById(whId);
			Long originId = whId;

			Long whIdTemp = this._whAgg.getWhIdByOriginal(whDocument.getCreditAppId(), whDocument.getDocType());
			if (whIdTemp != null) {
				originId = whIdTemp;
			}
			//End : Neu giay to ve la giay to update loi thi lay thong tin cua ho so goc.
			
			//Begin : Kiem tra giay to da duoc bam luu va chuyen hay chua
			Boolean checkSaveAndTrans = this._whAgg.getWhIdByStatusProcess(whDocument.getCreditAppId(), whDocument.getDocType());
			//End : Kiem tra giay to da duoc bam luu va chuyen hay chua
			
			
			//Begin : Check thong tin con da ton tai hay chua , neu da ton tai thi lay cua con , chua ton tai thi lay cua cha.
			boolean isSave = this._whAgg.isSave(whId);
			//End : Check thong tin con da ton tai hay chua , neu da ton tai thi lai cua con , chua ton tai thi lay cua cha.
			
			CheckRecordsCavetDTO obj = null;
			if(isSave) {
				 obj = this._whAgg.getCheckRecordsCavet(whId);
				 CheckRecordsCavetDTO objOrigin = this._whAgg.getCheckRecordsCavet(originId);
				 
				 //Begin: Lay thong tin cavet goc set lai cho update loi
				 
				 if(whDocument.getDocType().equals(_ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE))){
					 obj.setCavetBrand(objOrigin.getCavetBrand());
					 obj.setCavetModelCode(objOrigin.getCavetModelCode());
					 obj.setCavetColor(objOrigin.getCavetColor());
					 obj.setCavetEngine(objOrigin.getCavetEngine());
					 obj.setCavetChassis(objOrigin.getCavetChassis());
					 obj.setCavetNPlate(objOrigin.getCavetNPlate());
					 obj.setCavetCavetNumber(objOrigin.getCavetCavetNumber());
				 }
			
				//End: Lay thong tin cavet goc set lai cho update loi
				 
				 //Ma loi cua con
				 obj.setErrorList(this._whAgg.getLstResultsContractDocumentInfo(whId));
				 
				 List<ResultsDocumentInfo> listResultsDocumentInfoOrigin = this._whAgg.getLstResultsCavetDocumentInfo(originId, obj.getLstLoanDoc());
				 
				 obj.setLstResultsDocument(listResultsDocumentInfoOrigin);
				 
				 if (obj.getDocTypeCode() == CTCodeValue1.WH_LOAN_DOC.toString()) {
					 obj.setLstGoods(this._whAgg.getLstGoods(whId));
				 }
				 
			} else {
				obj = this._whAgg.getCheckRecordsCavet(originId);
				
				obj.setErrorList(this._whAgg.getLstResultsContractDocumentInfo(originId));
				obj.setLstResultsDocument(this._whAgg.getLstResultsCavetDocumentInfo(originId, obj.getLstLoanDoc()));
				if (obj.getDocTypeCode().equals(CTCodeValue1.WH_LOAN_DOC.toString())) {
					obj.setLstGoods(this._whAgg.getLstGoods(originId));
				}
			}
			
			if (checkSaveAndTrans) {
				obj = this._whAgg.getCheckRecordsCavet(originId);
				
				obj.setErrorList(this._whAgg.getLstResultsContractDocumentInfo(originId));
				obj.setLstResultsDocument(this._whAgg.getLstResultsCavetDocumentInfo(originId, obj.getLstLoanDoc()));
				
				Long typeAppendix = _ctCache.getIdBy(CTCodeValue1.WH_IN_APPENDIX, CTCat.WH_CAVET_TYPE);
				WhCavetInfo cavetInforOringin = this._whAgg.getCavetInfo(originId, typeAppendix);
				
				 //Begin: Lay thong tin tren phu luc set lai cho update loi
				if (cavetInforOringin !=  null) {
					obj.setAppendixBrand(cavetInforOringin.getBrand());
					obj.setAppendixCavetNumber(cavetInforOringin.getCavetNumber());
					obj.setAppendixChassis(cavetInforOringin.getChassis());
					obj.setAppendixColor(cavetInforOringin.getColor());
					obj.setAppendixEngine(cavetInforOringin.getEngine());
					//obj.setAppendixId(cavetInforOringin.getId());
					obj.setAppendixModelCode(cavetInforOringin.getModelCode());
					obj.setAppendixNPlate(cavetInforOringin.getnPlate());
				} 
				
				//Neu la cavet thu 2 thi lay thong tin cua chinh no
				if (whDocument.getDocType().equals(_ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
					Long typeCavetInfo = _ctCache.getIdBy(CTCodeValue1.WH_IN_CAVET, CTCat.WH_CAVET_TYPE);
					WhCavetInfo infoCurrent = this._whAgg.getCavetInfo(whId, typeCavetInfo);

					if (infoCurrent != null) {
						obj.setCavetBrand(infoCurrent.getBrand());
						obj.setCavetModelCode(infoCurrent.getModelCode());
						obj.setCavetColor(infoCurrent.getColor());
						obj.setCavetEngine(infoCurrent.getEngine());
						obj.setCavetChassis(infoCurrent.getChassis());
						obj.setCavetNPlate(infoCurrent.getnPlate());
						obj.setCavetCavetNumber(infoCurrent.getCavetNumber());
					}
				}
				//End: Lay thong tin tren phu luc set lai cho update loi
			}

			obj.setProcessStatus(whDocument.getProcessStatus());
			CheckRecordsCavetDTO objChild = this._whAgg.getCheckRecordsCavet(whId);
			obj.setOperator2(objChild.getOperator2());
			
			obj.setCheckSaveAndTrans(checkSaveAndTrans);
			// obj.setTypeCurrentDocument(typeCurrentDocument);
			
			//fix loi lay ra tat ca danh sach giay to ve
			/*if (obj.getDocTypeCode().equals(CTCodeValue1.WH_CAVET.value()) || (obj.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.value()) && (obj.getContractCavetType().equals(2l)))) {
				obj.setLstReturnDocument(this._whAgg.getLstDoc(this._whAgg.getLstWhIdReturnDocument(originId, true)));
			} else if (obj.getDocTypeCode().equals(CTCodeValue1.WH_LOAN_DOC.value())  || (obj.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.value()) && (obj.getContractCavetType().equals(1l)))) {
				obj.setLstReturnDocument(this._whAgg.getLstDoc(this._whAgg.getLstWhIdReturnDocument(originId, false)));
			}*/
			
			obj.setLstReturnDocument(this._whAgg.getLstReturnDocument2(whId));

			obj.setLstLoanDoc(DocumentFactory.getDocumentAggregateInstance().queryDoc(Integer.valueOf(obj.getBpmAppNumber())));
			
//			obj.setErrorList(this._whAgg.getLstResultsContractDocumentInfo(originId));

			CTCodeValue1 docType = CTCodeValue1.from(obj.getDocTypeCode());
			if (docType == CTCodeValue1.WH_LOAN_DOC && (obj.getLstLoanDoc() == null || obj.getLstLoanDoc().isEmpty())) {
				return obj;
			}

			if (docType == CTCodeValue1.WH_LOAN_DOC) {
				obj.setLstResultsDocument(this._whAgg.getLstResultsContractDocumentInfo(originId, obj.getLstLoanDoc()));
			}

			if (docType == CTCodeValue1.WH_CAVET) {
				obj.setLstResultsDocument(this._whAgg.getLstResultsCavetDocumentInfo(originId, obj.getLstLoanDoc()));
			}

			return obj;
		});
	}

	public Object deleteDocument(String whIds) throws Exception {
		return this.tryCatch(() -> {

			_whValidate.validateDeleteDocument(whIds);

			this._whAgg.deleteDocument(whIds);
			return null;
		});
	}
        
        public String exportHandoverBorrow(List<WareHouseExportHandoverBorrowedDTO> lstHandoverBorrowDTO) throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.getHandoverBorrowExport(lstHandoverBorrowDTO);
		});
	}
        
        public String exportWareHouseHistory(List<WareHouseExportHistoryDTO> lstHistory) throws Exception {
		return this.tryCatch(() -> {
			return this._whAgg.getExportHistory(lstHistory);
		});
	}
	
	public static void main(String[] args) throws Exception {
		
	/*	new CacheManager().refeshCache("ALL");
		WareHouseManager manager = new WareHouseManager(null);   
                List<WareHouseExportHistoryDTO> lstHistory = new ArrayList<WareHouseExportHistoryDTO>();
                WareHouseExportHistoryDTO dTO = new WareHouseExportHistoryDTO();
                dTO.setContractNumber("1000116120053411");
                dTO.setDocTypeName("Update lỗi");
                dTO.setWhId(1771L);
                lstHistory.add(dTO);
                WareHouseExportHistoryDTO dTO1 = new WareHouseExportHistoryDTO();
                dTO1.setContractNumber("1000116120053411");
                dTO1.setDocTypeName("Cavet");
                dTO1.setWhId(1952L);
                lstHistory.add(dTO1);
                
                manager.exportWareHouseHistory(lstHistory);*/
		
//		QRCodeDTO item = new QRCodeDTO();
//		item.setAppId("111");
//		item.setAppNumber("3243243");
//		item.setSoHopDong("2131231232");
//	
//		manager = new WareHouseManager(null);
//		manager.createQRCode(item);
		
//		manager = new WareHouseManager(null);
//		QRCodeCheckDTO ite = new QRCodeCheckDTO();
//		ite.setAppID("9053550825b90fd856b35c8032004909");
//		ite.setAppNumber("4985");
//		ite.setContractNumber("1000318090000161");
//		ite.setVersion(1);
//		ite.setSignature("436980c229462ae349ff8cea9af973961912b7cabe97669a880fbe890803505a");
		
	}

	
	
	
}
