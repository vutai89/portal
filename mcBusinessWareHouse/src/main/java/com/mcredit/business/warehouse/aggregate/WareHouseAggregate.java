package com.mcredit.business.warehouse.aggregate;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;
import org.modelmapper.ModelMapper;

import com.mcredit.business.warehouse.export.HandoverExport;
import com.mcredit.business.warehouse.export.IExportDocument;
import com.mcredit.business.warehouse.export.IExportPdf;
import com.mcredit.business.warehouse.export.IExportXLS;
import com.mcredit.business.warehouse.export.PaperReceiptExport;
import com.mcredit.business.warehouse.export.ThankLetterExport;
import com.mcredit.business.warehouse.export.WarehouseHistoryExport;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.credit.entity.CreditApplicationBPM;
import com.mcredit.data.credit.entity.CreditApplicationSignature;
import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.data.warehouse.entity.WhCavetInfo;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.data.warehouse.entity.WhDocumentErr;
import com.mcredit.data.warehouse.entity.WhExpectedDate;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.warehouse.ApproveBorrowCavetDTO;
import com.mcredit.model.dto.warehouse.CavetDTO;
import com.mcredit.model.dto.warehouse.CavetInfor;
import com.mcredit.model.dto.warehouse.CheckRecordsCavetDTO;
import com.mcredit.model.dto.warehouse.DocumentsDTO;
import com.mcredit.model.dto.warehouse.DocumentsErrorsDTO;
import com.mcredit.model.dto.warehouse.ErrorType;
import com.mcredit.model.dto.warehouse.GoodsDTO;
import com.mcredit.model.dto.warehouse.QRCodeCheckDTO;
import com.mcredit.model.dto.warehouse.QRCodeDTO;
import com.mcredit.model.dto.warehouse.ResponseCheckDocumentDTO;
import com.mcredit.model.dto.warehouse.ResponseUpdateCavetDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverBorrowedDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHistoryDTO;
import com.mcredit.model.dto.warehouse.WareHousePayBackCavetDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.MailCheckErrEnums;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.TemplateEnum;
import com.mcredit.model.enums.WHStep;
import com.mcredit.model.object.ListObjectResult;
import com.mcredit.model.object.Param;
import com.mcredit.model.object.SearchCaseInput;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.model.object.ecm.LoanDocRespone;
import com.mcredit.model.object.warehouse.CheckRecordsCavetInfo;
import com.mcredit.model.object.warehouse.Document;
import com.mcredit.model.object.warehouse.QRCodeInfo;
import com.mcredit.model.object.warehouse.RemainDocument;
import com.mcredit.model.object.warehouse.RenewalDocumentDTO;
import com.mcredit.model.object.warehouse.ResultsComtractDocumentInfo;
import com.mcredit.model.object.warehouse.ResultsDocumentInfo;
import com.mcredit.model.object.warehouse.ReturnDocumentInfo;
import com.mcredit.model.object.warehouse.WareHouseCodeTableCacheDTO;
import com.mcredit.model.object.warehouse.WareHouseEnum;
import com.mcredit.model.object.warehouse.WareHouseMatrix;
import com.mcredit.model.object.warehouse.WareHousePayBackCavet;
import com.mcredit.model.object.warehouse.WareHousePayBackLetter;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.model.object.warehouse.WhDocumentChangeDTO;
import com.mcredit.model.object.warehouse.WhDocumentDTO;
import com.mcredit.model.object.warehouse.WhDocumentErrDTO;
import com.mcredit.model.object.warehouse.WhLstErrPayBackCavet;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.cache.WHCodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.service.ThreadSendEmail;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.LongUtil;
import com.mcredit.util.QRUtil;
import com.mcredit.util.StringUtils;
import com.mcredit.util.security.SHA256;

public class WareHouseAggregate {

	private UnitOfWork unitOfWork = null;
	
	private ModelMapper modelMapper = new ModelMapper();
	private CodeTableCacheManager ctCache = CacheManager.CodeTable();

	private final Long LOAN_DOC_TYPE = 1L;
	private final Long CAVET_TYPE = 2L;
	
	private final Integer notProcessed =  0;
	/*private final Integer processed =  1;*/
	private final Integer notActive = 0;
	private final Integer active = 1;

	private final Integer notOriginal  = 0;	
	private final Integer original  = 1;	
	
	private final Integer notAppendixContract  = 0;	

	private final Long newDocStatus = ctCache.getBy(CTCodeValue1.WH_NEW_DOCUMENT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE).getId().longValue();
	private final Long newWaitStatus = ctCache.getBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE).getId().longValue();
	
	private final Long docTypeLoan = ctCache.getBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE).getId().longValue();
	private final Long docTypeCavet = ctCache.getBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE).getId().longValue();
	private final Long docTypeErrUpdate = ctCache.getBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE, CTGroup.WARE_HOUSE).getId().longValue();

	public WareHouseAggregate(UnitOfWork uok) {
		this.unitOfWork = uok;
	}

	public List<Long> upsertDocument(DocumentsDTO payloadSaveDocuments, UserDTO user) throws Exception {

		CTCodeValue1 eCodeValue1 = CTCodeValue1.from(payloadSaveDocuments.getCodeValue1().toString());

		if (eCodeValue1 == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.codeValue1")));
		}

		List<Document> documents = payloadSaveDocuments.getDocuments();
		List<Long> returnIds = new ArrayList<Long>();
		Long waitReturn = ctCache.getIdBy(CTCodeValue1.WH_WAIT_RETURN, CTCat.WH_LODGE);

		String strReturnDocNotReceive = CTCodeValue1.WH_RETURN_DOC_NOT_RECEIVE.value();
		String strLodgedErrUpdateBorrow = CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.value();
		String strLodgedCompleteBorrow = CTCodeValue1.WH_LODGE_COMPLETE_BORROW.value();
		String strLodgedErrUpdateReturn = CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN.value();
		String strLodgedCompleteReturn = CTCodeValue1.WH_LODGED_COMPLETE_RETURN.value();

		Long batch_id = LongUtil.uniqueId();
		Long whId = 0L;
		/* List<WhDocument> arrCheckDup = new ArrayList<WhDocument>(); */

		for (Document document : documents) {

			List<WhDocument> checkDoc = this.unitOfWork.warehouse.whDocumentRepo()
					.getCheckDoc(document.getCreditAppId(), document.getDocType());

			// WhDocument newDocumentByContractCavetType =
			// this.unitOfWork.warehouse.whDocumentRepo().getDocumentByCreditAppId(document.getCreditAppId(),
			// document.getDocType(), null);

			Long currentVersion = this.unitOfWork.warehouse.whDocumentRepo()
					.getVersionDocumentByAppId(document.getCreditAppId(), document.getDocType());
			Long version = currentVersion != null ? currentVersion + 1 : 1;

			Long currentOrderBy = this.unitOfWork.warehouse.whDocumentRepo()
					.getCurrentOrderBy(document.getCreditAppId(), document.getDocType());
			Long orderBy = currentOrderBy != null ? currentOrderBy + 1 : 1;

			WhDocument doc = modelMapper.map(document, WhDocument.class);

			if (eCodeValue1 == CTCodeValue1.WH_NEW_DOCUMENT) {

				Boolean isNewDoc = false;

				if (checkDoc != null && !checkDoc.isEmpty()) {

					for (WhDocument whDocument : checkDoc) {

						whDocument.setBatchId(batch_id);

						if (whDocument.getStatus().equals(newDocStatus)) {

							isNewDoc = true;

							if (!whDocument.getCreatedBy().equals(user.getLoginId())) {
								throw new ValidationException("Document is processing!");
							}

							whDocument.setContractCavetType(document.getContractCavetType());

						}

						this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);

					}

				}

				if (isNewDoc == false) {

					WhDocument newDoc = new WhDocument(null, null, document.getCreditAppId(), document.getDocType(),
							batch_id, orderBy, document.getEstimateDate(), null, null, new Date(), user.getLoginId(),
							null, null, document.getContractCavetType(), newDocStatus, null, notProcessed,
							document.getBillCode(), document.getDeliveryError(), null, notActive, notOriginal,
							notAppendixContract, null);

					this.unitOfWork.warehouse.whDocumentRepo().upsert(newDoc);

				}

			}

			if (eCodeValue1 == CTCodeValue1.WH_NEW_WAIT) {

				if (checkDoc.size() > 0) {

					HashMap<String, WhDocument> hasCheckOriginal = new HashMap<>();
					List<String> lstParamWithoutVersion = new ArrayList<String>();
					//Integer count = 0;
					
					/*for (WhDocument whDocument : checkDoc) {

						if (whDocument.getIsOriginal() == original) {
							hasCheckOriginal.put(whDocument.getCreditAppId() + whDocument.getDocType() + "IsOriginal",
									whDocument);
							
							count = this.unitOfWork.warehouse.whDocumentRepo().getCountDocumentByLoadBatchAndOriginal(
									whDocument.getCreatedBy(), whDocument.getDocType(), whDocument.getCreditAppId());
						} else {
							count = this.unitOfWork.warehouse.whDocumentRepo().getCountDocumentByLoadBatchAndActive(
									whDocument.getCreatedBy(), whDocument.getDocType(), whDocument.getCreditAppId());
						}

					}*/

					for (WhDocument whDocument : checkDoc) {	
						
						Integer count = this.unitOfWork.warehouse.whDocumentRepo()
								.getCountDocumentByLoadBatch(whDocument.getDocType(), whDocument.getCreditAppId());
						
						if (whDocument.getVersion() == null) {

							if (count > 1) {

								if (document.getVersion() != null && (document.getStatusCode()
										.equals(strReturnDocNotReceive)
										|| (document.getDocType().equals(docTypeCavet) && (document.getStatusCode()
												.equals(strLodgedErrUpdateBorrow)
												|| document.getStatusCode().equals(strLodgedCompleteBorrow)
												|| document.getStatusCode().equals(strLodgedErrUpdateReturn)
												|| document.getStatusCode().equals(strLodgedCompleteReturn))))) {

									lstParamWithoutVersion.add(whDocument.getId().toString());

									this.unitOfWork.warehouse.whDocumentRepo().deleteDocument(lstParamWithoutVersion);

								} else {
									
									if (!whDocument.getCreatedBy().equals(user.getLoginId())
											&& whDocument.getVersion() == null) {
										throw new ValidationException("Document is processing!");
									}

									if (whDocument.getCreditAppId().equals(document.getCreditAppId())
											&& whDocument.getDocType().equals(document.getDocType())
											&& document.getVersion() != null) {

										WhDocument docInfo = this.unitOfWork.warehouse.whDocumentRepo()
												.findById(document.getId());

										docInfo.setIsActive(notActive);
										this.unitOfWork.warehouse.whDocumentRepo().upsert(docInfo);

										whDocument.setContractCavetType(document.getContractCavetType());
									}

									whDocument.setVersion(version);
									whDocument.setBatchId(batch_id);
									whDocument.setCreatedDate(new Date());
									whDocument.setCreatedBy(user.getLoginId());
									whDocument.setStatus(newWaitStatus);
									whDocument.setIsActive(active);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
									whId = whDocument.getId();

								}

							} else {
								
								if (!whDocument.getCreatedBy().equals(user.getLoginId())
										&& whDocument.getVersion() == null) {
									throw new ValidationException("Document is processing!");
								}

								if (whDocument.getCreditAppId().equals(document.getCreditAppId())
										&& whDocument.getDocType().equals(document.getDocType())) {

									WhDocument docInfo = this.unitOfWork.warehouse.whDocumentRepo()
											.findById(document.getId());

									docInfo.setIsActive(notActive);
									this.unitOfWork.warehouse.whDocumentRepo().upsert(docInfo);

								}

								whDocument.setVersion(version);
								whDocument.setBatchId(batch_id);
								whDocument.setCreatedDate(new Date());
								whDocument.setCreatedBy(user.getLoginId());
								whDocument.setContractCavetType(document.getContractCavetType());
								whDocument.setStatus(newWaitStatus);
								whDocument.setIsActive(active);

								this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
								whId = whDocument.getId();

							}

						} else {

							if (document.getDocType().equals(docTypeCavet)) {
								
								if (!whDocument.getCreatedBy().equals(user.getLoginId())
										&& whDocument.getVersion() == null) {
									throw new ValidationException("Document is processing!");
								}

								if (document.getStatusCode().equals(strLodgedErrUpdateBorrow)
										|| document.getStatusCode().equals(strLodgedCompleteBorrow)) {

									whDocument.setBatchId(batch_id);
									whDocument.setCreatedDate(new Date());
									whDocument.setCreatedBy(user.getLoginId());
									whDocument.setProcessStatus(notProcessed);
									whDocument.setProcessDate(null);
									whDocument.setIsActive(active);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
									whId = whDocument.getId();

									deleteAllocation(whDocument.getId());

								} else if (document.getStatusCode().equals(strLodgedErrUpdateReturn)
										|| document.getStatusCode().equals(strLodgedCompleteReturn)) {

									whDocument.setBatchId(batch_id);
									whDocument.setCreatedDate(new Date());
									whDocument.setCreatedBy(user.getLoginId());
									whDocument.setLastUpdatedBy(null);
									whDocument.setLastUpdatedDate(null);
									whDocument.setStatus(waitReturn);
									whDocument.setIsActive(active);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
									whId = whDocument.getId();

								}  else if (document.getStatusCode().equals(strReturnDocNotReceive)) {

									if (!whDocument.getCreatedBy().equals(user.getLoginId())
											&& whDocument.getVersion() == null) {
										throw new ValidationException("Document is processing!");
									}
									
									// whDocument.setBatchId(batch_id);
									whDocument.setWhCodeId(null);
									whDocument.setWhLodgeBy(null);
									whDocument.setCreatedDate(new Date());
									whDocument.setCreatedBy(user.getLoginId());
									whDocument.setLastUpdatedBy(null);
									whDocument.setLastUpdatedDate(null);
									whDocument.setStatus(newWaitStatus);
									whDocument.setWhLodgeBy(null);
									whDocument.setProcessStatus(notProcessed);
									whDocument.setBillCode(document.getBillCode());
									whDocument.setDeliveryError(document.getDeliveryError());
									whDocument.setProcessDate(null);
									whDocument.setIsActive(active);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
									whId = whDocument.getId();

									deleteAllocation(whDocument.getId());

								} else {
									
									if (!whDocument.getCreatedBy().equals(user.getLoginId())
											&& whDocument.getVersion() == null) {
										throw new ValidationException("Document is processing!");
									}

									whDocument.setIsActive(notActive);
									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);

									WhDocument newDoc = new WhDocument(null, version, document.getCreditAppId(),
											document.getDocType(), batch_id, orderBy, document.getEstimateDate(), null,
											null, new Date(), user.getLoginId(), null, null,
											document.getContractCavetType(), newWaitStatus, null, notProcessed,
											document.getBillCode(), document.getDeliveryError(), null, active,
											notOriginal, notAppendixContract, null);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(newDoc);
									whId = newDoc.getId();

								}

							} else if (document.getStatusCode().equals(strReturnDocNotReceive)) {

								if (!whDocument.getCreatedBy().equals(user.getLoginId())
										&& whDocument.getVersion() == null) {
									throw new ValidationException("Document is processing!");
								}
								
								// whDocument.setBatchId(batch_id);
								whDocument.setWhCodeId(null);
								whDocument.setWhLodgeBy(null);
								whDocument.setCreatedDate(new Date());
								whDocument.setCreatedBy(user.getLoginId());
								whDocument.setLastUpdatedBy(null);
								whDocument.setLastUpdatedDate(null);
								whDocument.setStatus(newWaitStatus);
								whDocument.setWhLodgeBy(null);
								whDocument.setProcessStatus(notProcessed);
								whDocument.setBillCode(document.getBillCode());
								whDocument.setDeliveryError(document.getDeliveryError());
								whDocument.setProcessDate(null);
								whDocument.setIsActive(active);

								this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
								whId = whDocument.getId();

								deleteAllocation(whDocument.getId());

							} else {

								if (count <= 1) {
									
									if (!whDocument.getCreatedBy().equals(user.getLoginId())
											&& whDocument.getVersion() == null) {
										throw new ValidationException("Document is processing!");
									}

									whDocument.setIsActive(notActive);
									this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);

									WhDocument newDoc = new WhDocument(null, version, document.getCreditAppId(),
											document.getDocType(), batch_id, orderBy, document.getEstimateDate(), null,
											null, new Date(), user.getLoginId(), null, null,
											document.getContractCavetType(), newWaitStatus, null, notProcessed,
											document.getBillCode(), document.getDeliveryError(), null, active,
											notOriginal, notAppendixContract, null);

									this.unitOfWork.warehouse.whDocumentRepo().upsert(newDoc);
									whId = newDoc.getId();

								}

							}

						}

					}

				} else {

					WhDocument newDoc = new WhDocument(null, version, document.getCreditAppId(), document.getDocType(),
							batch_id, orderBy, document.getEstimateDate(), null, null, new Date(), user.getLoginId(),
							null, null, document.getContractCavetType(), newWaitStatus, null, notProcessed,
							document.getBillCode(), document.getDeliveryError(), null, active, notOriginal,
							notAppendixContract, null);

					this.unitOfWork.warehouse.whDocumentRepo().upsert(newDoc);
					whId = newDoc.getId();

				}

				// Insert documentChange
				Long type = ctCache.getIdBy(CTCodeValue1.from(document.getType().toString()), CTCat.WH_CHAN_TYPE,
						CTGroup.WARE_HOUSE);

				Long idCodeTable = ctCache.getIdBy(CTCodeValue1.from(document.getIdCodeTable().toString()),
						CTCat.WH_LODGE, CTGroup.WARE_HOUSE);

				WhDocumentChange whDocChange = new WhDocumentChange(whId, type, document.getNote(), user.getLoginId(),
						new Date(), idCodeTable, document.getCreditAppId());

				this.unitOfWork.warehouse.whDocumentChangeRepo().add(whDocChange);

			}

			returnIds.add(doc.getId());
		}

		/*if (arrCheckDup.size() > 0) {
			for (int j = 0; j < arrCheckDup.size(); j++) {
				this.unitOfWork.warehouse.whDocumentRepo().upsert(arrCheckDup.get(j));
			}
		}*/

		return returnIds;

	}

	private void deleteAllocation(Long whId) {

		AllocationDetail allocationDetail = this.unitOfWork.common.allocationDetailRepo().findByuplCustomerId(whId);

		if (allocationDetail != null) {

			this.unitOfWork.common.allocationMasterRepo().deleteForWh(allocationDetail.getAllocationMasterId());
			this.unitOfWork.common.allocationDetailRepo().remove(allocationDetail);

		}
		
	}


	public ResponseCheckDocumentDTO updateDocumentErrors(DocumentsErrorsDTO documentsErrors, UserDTO user) throws Exception {

		// Begin: Neu la giay to update loi se chuyen sang update thong tin  cho hskv
		WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().getById(documentsErrors.getWhDocumentId());
		Long beforeId =  documentsErrors.getWhDocumentId();
		List<WhDocument> whDocumnetList = this.getWhDocumentListByCreditId(whDocument.getCreditAppId());
		
		// Handle update status wh document
		boolean isSave = documentsErrors.isSave();
		boolean isSaveAndNext = !isSave;
		List<Integer> errorIds = new ArrayList<Integer>();
		List<ErrorType> errorTypes = null;
		Long idCodeTable = null;

		// String errorNote = ""; ????????
		
		if (isSaveAndNext) {		
			            
	        boolean isNotOrPass = false;
			
			errorTypes = documentsErrors.getErrorTypes();
			for (ErrorType errorType : errorTypes) {

				idCodeTable = ctCache.getIdBy(CTCodeValue1.from(errorType.getCodeValue()), CTCat.WH_STATUS_DOC,
						CTGroup.WARE_HOUSE);

                                if (errorType.getUpdateRequest() == 1){
                                    isNotOrPass = true;
                                    errorIds.add(idCodeTable.intValue());
                                }
			}
			
			for( WhDocument whDocumentElement: whDocumnetList) {
				if((whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE)) 
						|| (whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE)) && whDocumentElement.getContractCavetType().equals(LOAN_DOC_TYPE))))
				{
					this.unitOfWork.warehouse.whDocumentErrRepo().deleteLstErr(whDocumentElement.getId());
					for (ErrorType errorTypeElement : errorTypes) {
						Long idCodeTableElement = ctCache.getIdBy(CTCodeValue1.from(errorTypeElement.getCodeValue()), CTCat.WH_STATUS_DOC,CTGroup.WARE_HOUSE);
                                                WhDocumentErr documentErr = new WhDocumentErr(whDocumentElement.getId(), user.getLoginId(), new Date(), idCodeTableElement, errorTypeElement.getUpdateRequest());
                                                this.unitOfWork.warehouse.whDocumentErrRepo().add(documentErr);

					}
				}
			}
			
			this.handleUpdateReturnDocuments(user, false, isNotOrPass, beforeId, errorTypes.get(0).getNote(), 0);
			
			//Begin:  Danh dau ho so da duoc bam luu va chuyen o man hinh kiem tra cavet/hskv
			Long idCodetableMark = 0L;
			if(isNotOrPass){
				idCodetableMark = ctCache.getIdBy(CTCodeValue1.WH_WAIT_ERR_UPDATE, CTCat.WH_LODGE);
			} else {
				idCodetableMark = ctCache.getIdBy(CTCodeValue1.WH_WAIT_COMPLETE, CTCat.WH_LODGE);
			}
			this.markSaveAndMoveInCheck(beforeId, user.getLoginId(),idCodetableMark);
			
			Long type1 = ctCache.getIdBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);
			WhDocumentChange whDocumentChange = new WhDocumentChange(beforeId, type1, "",
					user.getLoginId(), new Date(), idCodetableMark);
			this.unitOfWork.warehouse.whDocumentChangeRepo().add(whDocumentChange);
                        
			//End: Danh dau ho so da duoc bam luu va chuyen o man hinh kiem tra cavet/hskv
                        
                        // TO SEND EMAIL
                        if (isNotOrPass) {
                            this.handleSendEmail(documentsErrors.getWhDocumentId(), errorIds, errorTypes.get(0).getNote(), user);
                        }
		} else {
			
			//Neu chi bam luu
			// Delete cac loai loi cua WH trong bang WhDocumentErr
                        this.unitOfWork.warehouse.whDocumentErrRepo().deleteLstErr(beforeId);
			errorTypes = documentsErrors.getErrorTypes();
			for (ErrorType errorType : errorTypes) {
				idCodeTable = ctCache.getIdBy(CTCodeValue1.from(errorType.getCodeValue()), CTCat.WH_STATUS_DOC,CTGroup.WARE_HOUSE);                                        
                                WhDocumentErr documentErr = new WhDocumentErr(beforeId, user.getLoginId(), new Date(), idCodeTable, errorType.getUpdateRequest());
                                this.unitOfWork.warehouse.whDocumentErrRepo().add(documentErr);
			}
                       
                       whDocument.setNote(errorTypes.get(0).getNote());
                       this.unitOfWork.warehouse.whDocumentRepo().update(whDocument);
		}
		

		
		ResponseCheckDocumentDTO response = new ResponseCheckDocumentDTO();
		response.setErrorIds(errorIds);
		return response;
	}

	// ===== Check cavet
	public ResponseUpdateCavetDTO updateCavet(CavetDTO cavet, UserDTO user) throws Exception {
		
	ResponseUpdateCavetDTO response = new ResponseUpdateCavetDTO();
	//Begin : Neu giay to ve la giay to update loi thi lay thong tin cua ho so goc.
	WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().getById(cavet.getWhDocId());
	Long beforeId =  cavet.getWhDocId();
	Long errType =  ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE);
	//End : Neu giay to ve la giay to update loi thi lay thong tin cua ho so goc.
	
	List<WhDocument> whDocumnetList = this.getWhDocumentListByCreditId(whDocument.getCreditAppId());
	boolean isSaveAndNext = !cavet.isSave();

	// String errorSendMailMsg = ""; ???	
        List<ErrorType> errorTypes = cavet.getErrorTypes();
	Long typeCavetInfo= ctCache.getIdBy(CTCodeValue1.WH_IN_CAVET, CTCat.WH_CAVET_TYPE);
	Long typeAppendix = ctCache.getIdBy(CTCodeValue1.WH_IN_APPENDIX, CTCat.WH_CAVET_TYPE);
	
	List<Integer> errorIds = new ArrayList<Integer>();
	
	//Neu bam luu va chuyen
	if (isSaveAndNext) {
		
		boolean isHassErrorCavet = false;
		for (ErrorType errorType : errorTypes) {
			Long idError = ctCache.getIdBy(CTCodeValue1.from(errorType.getCodeValue()), CTCat.WH_C_LODGE_CV, CTGroup.WARE_HOUSE);
                        if (errorType.getUpdateRequest() == 1){
                            errorIds.add(idError.intValue());
                            isHassErrorCavet = true;
                        }
		}
		
		//Update thong tin trong bang cavetinfo, cac loai loi trong bang change cho ho so con va cha
		//Neu la giay to update loi cho cavet
		if(whDocument.getDocType().equals(errType) && whDocument.getContractCavetType().equals(CAVET_TYPE)){
			for (WhDocument whDocumentElement : whDocumnetList){
				if((whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE)) 
						|| (whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE)) && whDocumentElement.getContractCavetType().equals(CAVET_TYPE))))
				{
					this.updateCavetInfo(whDocumentElement.getId(), typeAppendix, cavet.getAppendixInfo(), user.getLoginId());					
					this.updateCavetErrors(errorTypes, whDocumentElement.getId(), user.getLoginId());                                       
				}
			}
		}
		
		//Neu la cavet
		if(whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
			
			this.updateCavetInfo(beforeId, typeCavetInfo, cavet.getCavetInfo(), user.getLoginId());
			for (WhDocument whDocumentElement : whDocumnetList){
				if((whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE)) 
						|| (whDocumentElement.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE)) && whDocumentElement.getContractCavetType().equals(CAVET_TYPE))))
				{
                                        this.updateCavetErrors(errorTypes, whDocumentElement.getId(), user.getLoginId());
				}
			}
		}
		
		this.handleUpdateReturnDocuments(user, true, isHassErrorCavet, beforeId, errorTypes.get(0).getNote(), cavet.getAppendixContract());
		
		//Begin:  Danh dau ho so da duoc bam luu va chuyen o man hinh kiem tra cavet/hskv
		Long idCodetableMark = 0L;
		if(isHassErrorCavet){
			idCodetableMark = ctCache.getIdBy(CTCodeValue1.WH_WAIT_ERR_UPDATE, CTCat.WH_LODGE);
		} else {
			idCodetableMark = ctCache.getIdBy(CTCodeValue1.WH_WAIT_COMPLETE, CTCat.WH_LODGE);
		}
		this.markSaveAndMoveInCheck(beforeId, user.getLoginId(), idCodetableMark);
		
		Long type1 = ctCache.getIdBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);
		WhDocumentChange whDocumentChange = new WhDocumentChange(beforeId, type1, "",
				user.getLoginId(), new Date(), idCodetableMark);
		this.unitOfWork.warehouse.whDocumentChangeRepo().add(whDocumentChange);                
                
		//End: Danh dau ho so da duoc bam luu va chuyen o man hinh kiem tra cavet/hskv

               // TO SEND EMAIL
                if (isHassErrorCavet) {
                    this.handleSendEmail(cavet.getWhDocId(), errorIds, errorTypes.get(0).getNote(), user);
                }
		
	} else {
		//Neu chi bam luu : thi chi luu cho chinh ho so do , ko luu cho ho so cha.
		//Neu la giay to update loi cho cavet
		if(whDocument.getDocType().equals(errType) && whDocument.getContractCavetType().equals(CAVET_TYPE)){
			this.updateCavetInfo(beforeId, typeAppendix, cavet.getAppendixInfo(), user.getLoginId());
                        this.updateCavetErrors(errorTypes, beforeId, user.getLoginId());
			}
		
		//Neu la cavet
		if(whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
			this.updateCavetInfo(beforeId, typeCavetInfo, cavet.getCavetInfo(), user.getLoginId());
                        this.updateCavetErrors(errorTypes, beforeId, user.getLoginId());
		}               
               
               whDocument.setAppendixContract(cavet.getAppendixContract());
               whDocument.setNote(errorTypes.get(0).getNote());
               this.unitOfWork.warehouse.whDocumentRepo().update(whDocument);
	}
		
	return response;
	}

	public List<Long> upsertCavetInfo(CavetDTO cavet, UserDTO user) {
		List<Long> cavetIdLst = new ArrayList<>();
		Long cavetId = null;
		Long idType;
		WhCavetInfo whCavetInfo = null;

		if (!StringUtils.isNullOrEmpty(cavet.getCavetInfo().getBrand())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getModelCode())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getColor())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getEngine())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getChassis())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getnPlate())
				|| !StringUtils.isNullOrEmpty(cavet.getCavetInfo().getCavetNumber())) {

			cavetId = (cavet.getCavetInfo().getId() != null) ? cavet.getCavetInfo().getId() : 0L;
			idType = CacheManager.CodeTable().getBy(CTCodeValue1.WH_IN_CAVET, CTCat.WH_CAVET_TYPE, CTGroup.WARE_HOUSE)
					.getId().longValue();

			whCavetInfo = new WhCavetInfo(cavetId, cavet.getWhDocId(), cavet.getCavetInfo().getBrand(),
					cavet.getCavetInfo().getModelCode(), cavet.getCavetInfo().getnPlate(),
					cavet.getCavetInfo().getCavetNumber(), idType, cavet.getCavetInfo().getColor(),
					cavet.getCavetInfo().getEngine(), cavet.getCavetInfo().getChassis());

		}

		if (!StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getBrand())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getModelCode())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getColor())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getEngine())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getChassis())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getnPlate())
				|| !StringUtils.isNullOrEmpty(cavet.getAppendixInfo().getCavetNumber())) {

			cavetId = (cavet.getAppendixInfo().getId() != null) ? cavet.getAppendixInfo().getId() : 0L;
			idType = CacheManager.CodeTable()
					.getBy(CTCodeValue1.WH_IN_APPENDIX, CTCat.WH_CAVET_TYPE, CTGroup.WARE_HOUSE).getId().longValue();

			whCavetInfo = new WhCavetInfo(cavetId, cavet.getWhDocId(), cavet.getAppendixInfo().getBrand(),
					cavet.getAppendixInfo().getModelCode(), cavet.getAppendixInfo().getnPlate(),
					cavet.getAppendixInfo().getCavetNumber(), idType, cavet.getAppendixInfo().getColor(),
					cavet.getAppendixInfo().getEngine(), cavet.getAppendixInfo().getChassis());

		}

		WhCavetInfo oldWhCavetInfo = (WhCavetInfo) this.unitOfWork.warehouse.WareHouseCavetInfoRepository().get(WhCavetInfo.class,
				cavetId);
		boolean isExistWhCavetInfo = (oldWhCavetInfo != null);

		whCavetInfo.setCreatedBy(isExistWhCavetInfo ? oldWhCavetInfo.getCreatedBy() : user.getLoginId());
		whCavetInfo.setCreatedDate(isExistWhCavetInfo ? oldWhCavetInfo.getCreatedDate() : new Date());
		whCavetInfo.setVersion(isExistWhCavetInfo ? oldWhCavetInfo.getVersion() + 1 : 1L);

		if (isExistWhCavetInfo) {
			whCavetInfo.setLastUpdatedBy(user.getLoginId());
			whCavetInfo.setLastUpdatedDate(new Date());
			this.unitOfWork.warehouse.WareHouseCavetInfoRepository().merge(whCavetInfo);
		} else {
			this.unitOfWork.warehouse.WareHouseCavetInfoRepository().add(whCavetInfo);
		}
		cavetIdLst.add(whCavetInfo.getId());

		return cavetIdLst;
	}

	public boolean updateCavetErrors(List<ErrorType> errorTypes, Long whId, String userLogin){
                this.unitOfWork.warehouse.whDocumentErrRepo().deleteLstErr(whId);
		for (ErrorType errorType : errorTypes) {
			Long idError = ctCache.getIdBy(CTCodeValue1.from(errorType.getCodeValue()), CTCat.WH_C_LODGE_CV, CTGroup.WARE_HOUSE);
                        WhDocumentErr documentErr = new WhDocumentErr(whId, userLogin, new Date(), idError, errorType.getUpdateRequest());
                        this.unitOfWork.warehouse.whDocumentErrRepo().add(documentErr);
		}
		
		return true;
	}

	// ===== End check cavet

	// ==== Update return document for hskv and cavet
	public void handleUpdateReturnDocuments(UserDTO user, boolean checkCavet, boolean isNotOrPass, Long beforeId, String note, int appendixContract) throws ValidationException {

		List<Long> listWhId = this.getLstWhIdReturnDocument(beforeId, checkCavet);
		List<ReturnDocumentInfo> returnDocuments = this.getLstDoc(listWhId);
                List<ReturnDocumentInfo> documentInfos = new ArrayList<>();
                List<ReturnDocumentInfo> infos = new ArrayList<>();
               boolean checkIsOriginal = false;
                for (int i = 0; i < returnDocuments.size(); i++) {
                    if (returnDocuments.get(i).getIsOriginal() == 1){
                        checkIsOriginal = true;
                    }
                }
                if (checkIsOriginal == false){
                    for (int i = 0; i < returnDocuments.size(); i++) {
                        if(returnDocuments.get(i).getWhId().equals(beforeId)){
                            documentInfos.add(returnDocuments.get(i));
                        } else {
                            infos.add(returnDocuments.get(i));
                        }
                    }
                    documentInfos.addAll(infos);
                } else {
                    documentInfos.addAll(returnDocuments);
                }
                
		ReturnDocumentInfo originalDocument = documentInfos.get(0);

		CTCodeValue1 newCodetableStatus = null;

		ReturnDocumentInfo lastDocument = documentInfos.get(documentInfos.size() - 1);
		Boolean isCavet = lastDocument.getDocTypeCode().equals(CTCodeValue1.WH_CAVET.toString());
		Boolean isUpdateErorForCavet = (lastDocument.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.toString())
				&& lastDocument.getContractCavetType().equals(CAVET_TYPE));
		Boolean isLoanDoc = lastDocument.getDocTypeCode().equals(CTCodeValue1.WH_LOAN_DOC.toString());
		Boolean isUpdateErrorForLoanDoc = (lastDocument.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.toString())
				&& lastDocument.getContractCavetType().equals(LOAN_DOC_TYPE));

		Boolean isOriginalDocumentNotLodged = originalDocument.getStatusCode()
				.equals(CTCodeValue1.WH_NEW_WAIT.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_WAIT_COMPLETE.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_WAIT_ERR_UPDATE.toString());
                
		Boolean isOriginalDocumentLodgedComplete = (originalDocument.getStatusCode().equals(CTCodeValue1.WH_LODGED_COMPLETE.toString()));			
                
		Boolean isOriginalDocumentLodgedErrUpdate = (originalDocument.getStatusCode().equals(CTCodeValue1.WH_LODGED_ERR_UPDATE.toString()));
                
		Boolean isOriginalDocumentLodgedBorrow = (originalDocument.getStatusCode()
				.equals(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.toString()));

		if (isOriginalDocumentNotLodged) {
			if (isNotOrPass) {
				newCodetableStatus = CTCodeValue1.WH_WAIT_ERR_UPDATE;
			} else {
				newCodetableStatus = CTCodeValue1.WH_WAIT_COMPLETE;
			}
		} else if (isOriginalDocumentLodgedComplete) {
			if (isNotOrPass) {                            
                            throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet")));
			} else {
				newCodetableStatus = CTCodeValue1.WH_LODGED_COMPLETE;
			}
		} else if (isOriginalDocumentLodgedErrUpdate) {
			if (isNotOrPass) {
				newCodetableStatus = CTCodeValue1.WH_LODGED_ERR_UPDATE;
			} else {
				newCodetableStatus = CTCodeValue1.WH_WAIT_COMPLETE;
			}
                } else if (isOriginalDocumentLodgedBorrow) {
                        if (isNotOrPass) {
				newCodetableStatus = CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW;
			} else {
				newCodetableStatus = CTCodeValue1.WH_LODGE_COMPLETE_BORROW;
			}
                        
		} else {
                        throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet.Original")));
		}

		this.updateStatusDocuments(documentInfos, newCodetableStatus, user, isCavet,
				isLoanDoc, isUpdateErorForCavet, isUpdateErrorForLoanDoc, checkCavet, isNotOrPass, beforeId, note, appendixContract);

		/*
		 * if (isCavet) { this.updateStatusDocuments(new
		 * ArrayList<>(Arrays.asList(lastDocument)), newCodetableStatus, user,
		 * isReturnDocumentHasError, isCavet, isLoanDoc, isUpdateErorForCavet,
		 * isUpdateErrorForLoanDoc); } else {
		 * this.updateStatusDocuments(returnDocuments, newCodetableStatus, user,
		 * isReturnDocumentHasError, isCavet, isLoanDoc, isUpdateErorForCavet,
		 * isUpdateErrorForLoanDoc); }
		 */
	}

	public void updateStatusDocuments(List<ReturnDocumentInfo> documents, CTCodeValue1 codeTableStatus, UserDTO user,
			Boolean isCavet, Boolean isLoanDoc, Boolean IsUpdateErrorForCavet,
			Boolean isUpdateErrorForLoanDoc, boolean checkCavet, boolean isNotOrPass, Long beforeId, String note, int appendixContract) throws ValidationException {

		ReturnDocumentInfo originalDocument = documents.get(0);
		String whCode = originalDocument.getWhCode();
		Long whCodeId = this.unitOfWork.warehouse.whCodeRepos().getIdByWHCode(whCode);
                WhDocument whBefore = (WhDocument) this.unitOfWork.warehouse.whDocumentRepo().get(WhDocument.class, beforeId);
                if (whBefore.getContractCavetType() != null){
                    WhDocument wd = (WhDocument) this.unitOfWork.warehouse.whDocumentRepo().get(WhDocument.class, documents.get(0).getWhId());
                    if (checkCavet) {
                        if (!wd.getId().equals(beforeId) && wd.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                            throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.checkCavet")));
                        }
                    } else {
                        if (!wd.getId().equals(beforeId) && wd.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                            throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.checkLoanDoc")));
                        }
                    }
                } 

		for (int i = 0; i < documents.size(); i++) {
			Long whId = documents.get(i).getWhId();
			WhDocument whDocument = (WhDocument) this.unitOfWork.warehouse.whDocumentRepo().get(WhDocument.class, whId);			
			Long idCodeTableStatus = ctCache.getIdBy(codeTableStatus, CTCat.WH_LODGE, CTGroup.WARE_HOUSE);

			whDocument.setLastUpdatedBy(user.getLoginId());
			whDocument.setLastUpdatedDate(new Date());                        
                        if(whId.equals(beforeId)){
                            whDocument.setIsActive(1);//set la giay to ve sau cung
                            whDocument.setProcessStatus(1);//set giay to da duoc xu ly 
                            whDocument.setProcessDate(new Date());                           
                            //Luu ngay len loi
                                Long idCodeTableExpectedDate = null;
                                if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE))
                                    || (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE)) && !checkCavet)) {
                                    idCodeTableExpectedDate = ctCache.getIdBy(CTCodeValue1.WH_EXPECTED_LOAN_DOC, CTCat.WH_HIS_EXPECTED_DATE, CTGroup.WARE_HOUSE);
                                } else if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))
                                    || (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE)) && checkCavet)) {
                                    idCodeTableExpectedDate = ctCache.getIdBy(CTCodeValue1.WH_EXPECTED_CAVET, CTCat.WH_HIS_EXPECTED_DATE, CTGroup.WARE_HOUSE);
                                }
                                
                                int checkExitWhExpectedDate =  this.unitOfWork.warehouse.whExpectedDateRepo().checkExitExpectedDate(whDocument.getCreditAppId(), idCodeTableExpectedDate);    
                                if (isNotOrPass == false) {
                                    this.unitOfWork.warehouse.whExpectedDateRepo().deleteExpectedDate(whDocument.getCreditAppId(), idCodeTableExpectedDate);    
                                } else if(isNotOrPass == true && checkExitWhExpectedDate == 0) {
                                    WhExpectedDate wed = new WhExpectedDate(whDocument.getCreditAppId(), idCodeTableExpectedDate, new Date());
                                    this.unitOfWork.warehouse.whExpectedDateRepo().upsert(wed);
                                }

                            
                        } else {
                            whDocument.setIsActive(0);
                        }
                        whDocument.setAppendixContract(appendixContract);//danh dau lam phu luc HD va luu ghi chu
                        whDocument.setNote(note);

			if (isLoanDoc || isUpdateErrorForLoanDoc) {
				whDocument.setWhCodeId(whCodeId);
			}

			if (i == 0) {
                            //xu ly cho thang cha
                            whDocument.setIsOriginal(1);//danh dau la giay to goc
                            processRoot(whDocument, isNotOrPass, idCodeTableStatus, originalDocument);                            
                            
			} else if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE))) {
                            if (checkCavet) {
                                //update loi cho Cavet
                                processCavet(whId, whDocument, isNotOrPass, beforeId, originalDocument);                                 
                            } else {
                                //update loi cho HSKV
                                processLoanDoc(documents.get(0).getWhId(), whDocument, isNotOrPass, beforeId, idCodeTableStatus);
                            }                        
                            
			} else if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
                                //xu ly cavet con                             
                               processCavet(whId, whDocument, isNotOrPass, beforeId, originalDocument);
                                
			} else if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE))) {
                                //xu ly HSKV con 
                                processLoanDoc(documents.get(0).getWhId(), whDocument, isNotOrPass, beforeId, idCodeTableStatus);
			}
			this.unitOfWork.warehouse.whDocumentRepo().merge(whDocument);

		}
	}
	// ==== End update return document for hskv and cavet

	// ===== Handle send email
	public void handleSendEmail(Long whId, List<Integer> errorIds, String errorNote, UserDTO user) throws Exception {
		// get infomation
		CheckRecordsCavetDTO recordInfo = this.getCheckRecordsCavet(whId);
		recordInfo.setLstReturnDocument(this.getLstReturnDocument(whId));

		String saleEmail = recordInfo.getSaleEmail();
		if (StringUtils.isNullOrEmpty(saleEmail)) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.check.email")));
		}
		
		String bdsEmail = recordInfo.getBsdEmail();
		
		NotificationTemplate notiTemplate = this.unitOfWork.common.getNotificationTemplateRepository().findByCode(TemplateEnum.WH_CHECK_ERROR_OP_2.toString());
		
		String subject = notiTemplate.getNotificationName();
		subject = subject.replaceAll(":contractNumber ", recordInfo.getMcContractNumber());
		subject = subject.replaceAll(":custName", recordInfo.getCustName());

		String body = this.getEmailBody(notiTemplate.getNotificationTemplate(), recordInfo, errorIds, errorNote);

		// FIXME: It now not working because update error before send email
		String existOperation2Email = "";
		if (!StringUtils.isNullOrEmpty(recordInfo.getOperator2())) {
			boolean isExistOperatin2DifferentCurrentuser = !recordInfo.getOperator2().equals(user.getLoginId());
			if (isExistOperatin2DifferentCurrentuser) {
				existOperation2Email = this.unitOfWork.common.getUserCommonRepository().getEmailByLoginId(recordInfo.getOperator2());
			}
		}

		String currentUserEmail = this.unitOfWork.common.getUserCommonRepository().getEmailByLoginId(user.getLoginId());

		ArrayList<String> ccEmails = new ArrayList<String>();
		if (!StringUtils.isNullOrEmpty(bdsEmail))
			ccEmails.add(bdsEmail);
                    

		if (!StringUtils.isNullOrEmpty(currentUserEmail))
			ccEmails.add(currentUserEmail);

		if (!StringUtils.isNullOrEmpty(existOperation2Email))
			ccEmails.add(existOperation2Email);
		
		// send email
		SendEmailDTO emailInfo = new SendEmailDTO();
		emailInfo.setTo(new ArrayList<String>(Arrays.asList(saleEmail)));
		
		if (ccEmails.size() > 0)
			emailInfo.setCc(ccEmails);

		emailInfo.setSubject(subject);
		emailInfo.setBody(body);
		
		
		Long idSendEmailType = ctCache.getIdBy(CTCodeValue1.WH_OP_2, CTCat.EMAIL_TYPE, CTGroup.EMAIL);
		ThreadSendEmail sendEmail = new ThreadSendEmail(emailInfo, idSendEmailType, user);
		sendEmail.send();

	}

	public String getEmailBody(String emailTemplate, CheckRecordsCavetDTO recordInfo, List<Integer> errorIds, String errorNote)
			throws ValidationException, ParseException {
		boolean isUpdateError = false;
		boolean isCavet = false;

		List<ReturnDocumentInfo> returnDocuments = recordInfo.getLstReturnDocument();
		ReturnDocumentInfo lastDocument = returnDocuments.get(returnDocuments.size() - 1);

		// FIXME: Add check is update error ContractCavetType = 2
		isUpdateError = (lastDocument.statusCode.equals(CTCodeValue1.WH_NEW_WAIT.toString()) && lastDocument.getWhCode() == null);

		if (!isUpdateError) {
			isCavet = lastDocument.getDocTypeCode().equals(CTCodeValue1.WH_CAVET.toString());
		}

		String body = emailTemplate;

		Date createdDate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(recordInfo.getCreatedDate());
		body = body.replaceAll(":mailTo", MailCheckErrEnums.MAIL_TO.value());

		String createdDateStr = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(createdDate);
		body = body.replaceAll(":DateInputCase", createdDateStr);
		body = body.replaceAll(":CustName", recordInfo.getCustName());
		body = body.replaceAll(":ContractNumber", recordInfo.getMcContractNumber());

		if (isUpdateError) {
			body = body.replaceAll(":DocType", MailCheckErrEnums.GET_DOC_ERR_UPDATE.value());
		} else if (isCavet) {
			body = body.replaceAll(":DocType", MailCheckErrEnums.CAVET.value());
		} else {
			body = body.replaceAll(":DocType", MailCheckErrEnums.CONTRACT.value());
		}

		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		Date tomorrow = c.getTime();
		String tomorrowStr = new SimpleDateFormat("dd/MM/yyyy").format(tomorrow);

		c.add(Calendar.DATE, 9);
		Date dayAfter10 = c.getTime();
		String dayAfter10Str = new SimpleDateFormat("dd/MM/yyyy").format(dayAfter10);

		body = body.replaceAll(":Tomorrow", tomorrowStr);
		body = body.replaceAll(":DateExpire", dayAfter10Str);

		List<String> errors = this.getErrorByIds(errorIds);
		String errorHTML = "<ul>";
		for (int i = 0; i < errors.size(); i++) {
			String strError = "<li>" + errors.get(i) + "</li>";
			errorHTML += strError;
		}
		errorHTML += "</ul>";

		if (!StringUtils.isNullOrEmpty(errorNote)) {
			errorHTML += "<p><b>Ghi chú lỗi</b>: " + errorNote + "</p>";
		}
		
		body = body.replaceAll(":ErrorList", errorHTML);

		return body;
	}

	public List<String> getErrorByIds(List<Integer> errorIds) throws ValidationException {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < errorIds.size(); i++) {
			Integer id = errorIds.get(i);
			CodeTableDTO ct = ctCache.getbyID(id);
			if (ct.getDescription1() != null) {
				result.add(ct.getDescription1());
			}
		}
		return result;
	}

	// ===== End handle send email

	public List<RemainDocument> findRemainDocumentAllocation() {
		return this.unitOfWork.warehouse.whDocumentRepo().findRemainDocumentAllocation();
	}

	public String getLstPaperReceipt(String contractNumber, String loginId) throws IOException {
		try (IExportDocument document = new PaperReceiptExport(this.unitOfWork.warehouse)) {
			return document.export(contractNumber, loginId);
		}
	}

	public String getLstThankLetter(String contractNumber, int typeExport, String loginId) throws IOException {
		try (IExportDocument document = new ThankLetterExport(this.unitOfWork.warehouse)) {
			return document.exportThankLetter(contractNumber, typeExport, loginId);
		}
	}

	public String getHandoverExport(List<WareHouseExportHandoverDTO> lstHandoverDTO) throws IOException {
		try (IExportPdf document = new HandoverExport()) {
			return document.exportPdf(lstHandoverDTO);
		}
	}

	public List<WareHousePayBackCavet> getLstPayBackCavet(WareHousePayBackCavetDTO cavetDTO) {
		List<WareHousePayBackCavet> result =  this.unitOfWork.warehouse.whDocumentRepo().getLstSearchPayBackCavet(cavetDTO);
		
         List<Long> whIdlist = result.parallelStream().map(e -> e.getIdWhDocument()) .filter(idWhDocument -> idWhDocument != null).collect(Collectors.toList());
         HashMap<Long, List<WhLstErrPayBackCavet>> hashListError = this.unitOfWork.warehouse.whDocumentErrRepo().getLstErrPayBackCavet(whIdlist);
         for (WareHousePayBackCavet obj : result) {
             obj.setLstErrPayBackCavets(hashListError.get(obj.getIdWhDocument()));
         }
		
		return result ;
	}

	public List<WareHousePayBackLetter> getLstPayBackLetter(WareHousePayBackLetter letterDTO) {
		return this.unitOfWork.warehouse.whDocumentRepo().getLstSearchPayBackLetter(letterDTO);
	}

	public Object giveBackCavet(List<Long> listWhDocumentId, String userLogin) {

		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();
		CodeTableCacheManager codeTable = CacheManager.CodeTable();

		Long cavetType = codeTable.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE);
		Long okReturnStatus = codeTable.getIdBy(CTCodeValue1.WH_OK_RETURN, CTCat.WH_RE_STATUS);
		Long giveBackStatus1 = codeTable.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE_RETURN, CTCat.WH_LODGE);
		Long giveBackStatus2 = codeTable.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN, CTCat.WH_LODGE);
		Long returnCavetTypeStatus = codeTable.getIdBy(CTCodeValue1.WH_APP_RETURN, CTCat.WH_CHAN_TYPE);
		Long whChangeStatus = codeTable.getIdBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);
		Long errLodgeStatus = codeTable.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE, CTCat.WH_LODGE);
		Long approveStatus = codeTable.getIdBy(CTCodeValue1.WH_RETURN, CTCat.WH_RE_STATUS);

		for (Long id : listWhDocumentId) {
			try {
				WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);
				Long idCodeTbl = this.unitOfWork.warehouse.whDocumentChangeRepo().getLatestStatus(id, returnCavetTypeStatus);

				if (whDocument.getDocType().equals(cavetType) && !whDocument.getStatus().equals(giveBackStatus1)
						&& !whDocument.getStatus().equals(giveBackStatus2) && okReturnStatus.equals(idCodeTbl)) {

					Long newStatus = 0L;
					Long whCodeId = whDocument.getWhCodeId();
					newStatus = whDocument.getStatus().equals(errLodgeStatus) ? giveBackStatus2 : giveBackStatus1;

					whDocument.setStatus(newStatus);
					whDocument.setWhCodeId(null);
                                        whDocument.setWhLodgeDate(null);
                                        whDocument.setWhLodgeBy(null);
					this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
					sucsessUpdate.add(id);

					this.unitOfWork.warehouse.whDocumentChangeRepo().add(new WhDocumentChange(id, whChangeStatus, "Gave back cavet",
							userLogin, new Date(), newStatus));

					// Update table whCode
					this.unitOfWork.warehouse.whCodeRepos().updateStatusBy(whCodeId, newStatus);
                                        //update returnDate
                                        this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().updateReturnDate(id, approveStatus);

					// temporary commit to database
					this.unitOfWork.warehouse.flush();

				} else {
					errorUpdate.add(id);
				}
			} catch (Exception e) {
				errorUpdate.add(id);
			}
		}

		return new ListObjectResult(errorUpdate, sucsessUpdate);
	}

	public Object approveGiveBackCavet(List<ApproveBorrowCavetDTO> approveBorrowCavetDtoList, String status,
			String userLogin) {

		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();
		CodeTableCacheManager codeTable = CacheManager.CodeTable();
		Long cavetType = codeTable.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE);
		Long waitReturnStatus = codeTable.getIdBy(CTCodeValue1.WH_WAIT_RETURN, CTCat.WH_RE_STATUS);
		Long waitBorrowStatus = codeTable.getIdBy(CTCodeValue1.WH_WAIT, CTCat.WH_APR_STATUS);
		Long approveBorrowStatus = codeTable.getIdBy(CTCodeValue1.WH_OK, CTCat.WH_APR_STATUS);
		Long rejectBorrowStatus = codeTable.getIdBy(CTCodeValue1.WH_REJECT, CTCat.WH_APR_STATUS);
		Long approveGiveBackCavetStatus = codeTable.getIdBy(CTCodeValue1.WH_OK_RETURN, CTCat.WH_RE_STATUS);
		Long rejectGiveBackCavetStatus = codeTable.getIdBy(CTCodeValue1.WH_REJECT_RETURN, CTCat.WH_RE_STATUS);
//		Long returnCavetTypeStatus = codeTable.getIdBy(CTCodeValue1.WH_APP_CAVET_R, CTCat.WH_CHAN_TYPE);
//		Long borrowCavetTypeStatus = codeTable.getIdBy(CTCodeValue1.WH_APP_CAVET_BR, CTCat.WH_CHAN_TYPE);
		Long returnCavetTypeStatus = codeTable.getIdBy(CTCodeValue1.WH_APP_RETURN, CTCat.WH_CHAN_TYPE);
		Long borrowCavetTypeStatus = codeTable.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);
		boolean approveStatus = "approve".equals(status) ? true : false;

		for (ApproveBorrowCavetDTO approveBorrowCavetDto : approveBorrowCavetDtoList) {
			Long id = approveBorrowCavetDto.getId();
			String message = approveStatus ? "" : approveBorrowCavetDto.getReasonReject();
			try {
				WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);
				Long idCodeTblReturnCavet = this.unitOfWork.warehouse.whDocumentChangeRepo().getLatestStatus(id, returnCavetTypeStatus);
				Long idCodeTbleBorrowCavet = this.unitOfWork.warehouse.whDocumentChangeRepo().getLatestStatus(id, borrowCavetTypeStatus);
				
				if (whDocument.getDocType().equals(cavetType) && (waitReturnStatus.equals(idCodeTblReturnCavet) || waitBorrowStatus.equals(idCodeTbleBorrowCavet))) {

					// Approve/Reject give back cavet
					if (waitReturnStatus.equals(idCodeTblReturnCavet)) {
                                                WhBorrowedDocument borrowedDocument = this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository()
								.getLastestBorrowDocument(id, returnCavetTypeStatus);
						borrowedDocument.setApproveStatus(approveStatus ? approveGiveBackCavetStatus : rejectGiveBackCavetStatus);
						borrowedDocument.setApproveBy(userLogin);
						borrowedDocument.setApproveDate(new Date());
						borrowedDocument.setRejectReason(message);
						this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().upsert(borrowedDocument);	
                                                this.unitOfWork.warehouse.whDocumentChangeRepo()
								.add(new WhDocumentChange(id, returnCavetTypeStatus, message, userLogin, new Date(),
										approveStatus ? approveGiveBackCavetStatus : rejectGiveBackCavetStatus));
					}

					if (waitBorrowStatus.equals(idCodeTbleBorrowCavet)) {
						WhBorrowedDocument borrowedDocument = this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository()
								.getLastestBorrowDocument(id, borrowCavetTypeStatus);
						borrowedDocument.setApproveStatus(approveStatus ? approveBorrowStatus : rejectBorrowStatus);
						borrowedDocument.setApproveBy(userLogin);
						borrowedDocument.setApproveDate(new Date());
						borrowedDocument.setRejectReason(message);
						this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().upsert(borrowedDocument);
						this.unitOfWork.warehouse.whDocumentChangeRepo().add(new WhDocumentChange(id, borrowCavetTypeStatus, message,
								userLogin, new Date(), approveStatus ? approveBorrowStatus : rejectBorrowStatus));
					}

					sucsessUpdate.add(id);

					// temporary commit to database
					this.unitOfWork.warehouse.flush();
				} else {
					errorUpdate.add(id);
				}
			} catch (Exception e) {
				errorUpdate.add(id);
			}
		}

		return new ListObjectResult(errorUpdate, sucsessUpdate);
	}

	public List<WareHouseMatrix> getWareHouseMatrix(String contractStatus, String cavetStatus, String cavetErr,
			String letterStatus, int docType) {
		return this.unitOfWork.warehouse.whDocumentRepo().getOutputValidation(contractStatus, cavetStatus, cavetErr, letterStatus,
				docType);
	}

	public Object renewalDocument(List<RenewalDocumentDTO> request, String typeDocument, String userLogin)
			throws Exception {
		// List<WhBorrowedDocument> listEntity = new
		// ArrayList<WhBorrowedDocument>();

		ListObjectResult result = new ListObjectResult();
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();

		long idCodetale=typeDocument.equals("WH_CAVET")?ctCache.getIdBy(CTCodeValue1.WH_APP_RE_BORROW_CV, CTCat.WH_CHAN_TYPE):ctCache.getIdBy(CTCodeValue1.WH_APP_RE_BORROW_HD, CTCat.WH_CHAN_TYPE);
		Long borrowCavetType = ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);
		
		
		for (RenewalDocumentDTO document : request) {
			try {

				WhBorrowedDocument borrowDb = this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().getBorrowDocument(document, borrowCavetType);
				borrowDb.setExtensionDate(DateUtil.toDate(document.getRenewalDate(), DateFormatTag.DATEFORMAT_dd_MM_yyyy.value()));
				if(document.getObjectTo()!=null){
					borrowDb.setObjectTo(document.getObjectTo());
					borrowDb.setDepartmentName(document.getDepartmentName());
				}
				this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().upsert(borrowDb);

				sucsessUpdate.add(document.getWhDocId());

				// listEntity.add(borrowDb);
				this.unitOfWork.warehouse.whDocumentChangeRepo().add(new WhDocumentChange(document.getWhDocId(), ctCache.getBy(CTCodeValue1.WH_APP_RE_BORROW, CTCat.WH_CHAN_TYPE).getId().longValue(), userLogin, userLogin, new Date(), idCodetale));

				// temporary commit to database
				this.unitOfWork.warehouse.flush();

			} catch (Exception e) {
				errorUpdate.add(document.getWhDocId());
			}

		}
		result.setErrorUpdate(errorUpdate);
		result.setSucsessUpdate(sucsessUpdate);
		return result;
	}

	public Object approveReturnCavet(List<WhDocumentChange> listDocChange) {
		ListObjectResult result = new ListObjectResult();
		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();
		if (listDocChange.size() > 0) {
			for (int i = 0; i < listDocChange.size(); i++) {
				try {
				
					WhDocumentChange whDocChange = listDocChange.get(i);
					
					this.unitOfWork.warehouse.whDocumentChangeRepo().add(whDocChange);
					sucsessUpdate.add(whDocChange.getWhDocId());
				
					// temporary commit to database
					this.unitOfWork.warehouse.flush();
				} catch (Exception e) {
					errorUpdate.add(listDocChange.get(i));
				}

			}
		}
		result.setErrorUpdate(errorUpdate);
		result.setSucsessUpdate(sucsessUpdate);
		return result;
	}

	public Object addDocumentBorrowed(List<WhBorrowedDocument> listDocument, List<WhDocumentChange> docChanges,
			boolean isHskv) {
		ListObjectResult results = new ListObjectResult();
		List<Object> error = new ArrayList<>();
		List<Object> sucsess = new ArrayList<>();
		for (int i = 0; i < listDocument.size(); i++) {
			try {
//				WhBorrowedDocument borrowedDocument = this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository()
//						.getWhDocument(listDocument.get(i));
				WhBorrowedDocument objRoot = listDocument.get(i);
//				if (borrowedDocument != null) {
//					borrowedDocument.setAppointmentDate(objRoot.getAppointmentDate());
//					borrowedDocument.setObjectTo(objRoot.getObjectTo());
//					borrowedDocument.setType(objRoot.getType());
//					borrowedDocument.setRejectReason("");
//					borrowedDocument.setDepartmentName(objRoot.getDepartmentName());
//					borrowedDocument.setCreatedDate(new Date());
//					borrowedDocument.setExtensionDate(null);
//					if (!isHskv)
//						borrowedDocument.setApproveStatus(objRoot.getApproveStatus());
//					else{
//					    borrowedDocument.setApproveDate(new Date());
//					}
//					this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().add(borrowedDocument);
//				} else {
					if (isHskv){
						objRoot.setApproveStatus(null);
                        objRoot.setApproveDate(new Date());	
					}
					
					System.out.println( " kien ------------> " + JSONConverter.toJSON(objRoot));
				this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().add(objRoot);
//				}
				this.unitOfWork.warehouse.whDocumentChangeRepo().add(docChanges.size() > 0 ? docChanges.get(i) : null);
				// temporary commit to database
				this.unitOfWork.warehouse.flush();

				sucsess.add(listDocument.get(i));
			} catch (Exception e) {
				error.add(e);
			}
		}
		results.setErrorUpdate(error);
		results.setSucsessUpdate(sucsess);
		return results;
	}

	public void updateDocStatus(Long idDoc, Long status) throws ValidationException {
		try {
			this.unitOfWork.warehouse.whDocumentRepo().updateStatusReturnDoc(idDoc, status);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}

	public static <T> Collector<T, ?, T> toSingleton() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			if (list.size() != 1) {
				throw new IllegalStateException();
			}
			return list.get(0);
		});
	}

	public List<Long> distinctWHDocument(List<Long> idDocs) {
		List<Long> tempList = new ArrayList<>(idDocs);
		List<Long> listIdInDB = this.unitOfWork.warehouse.whDocumentRepo().checkIdWHDocument(idDocs);
		if (listIdInDB != null && listIdInDB.size() > 0) {
			for (Long o : idDocs) {
				if (!listIdInDB.contains(o))
					tempList.remove(o);
			}
		}
		return tempList;
	}

	public void renewalDocumentBorrowed(Date date, Long idDoc, Long appointStatus, Long typeBorrowed) {
		this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().renewal(date, idDoc, appointStatus, typeBorrowed);
	}

	public void upsertDocChange(WhDocumentChange whDocumentChange) {
		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(whDocumentChange);
	}

	public String getAppointDateDocumentBorrowed(Long idDoc) {
		return StringUtils.nullToEmpty(this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().getAppointDate(idDoc));
	}

	@SuppressWarnings("unchecked")
	public List<WareHouseSeachObject> seachCase(WHStep _step, SearchCaseInput seachCase, UserDTO userInfo,WareHouseCodeTableCacheDTO wareHouseCodeTableID,Integer pageSize, Integer pageNum) throws ValidationException {
		
		String batchId = null;
		HashMap<String, Long> hashLodge = new HashMap<>(), hashNewWait = new HashMap<>();
		HashMap<Long, Date> sendThankLater = new HashMap<>(), dateErrCavet = new HashMap<>(), dateErrLoanDoc = new HashMap<>();

		if (WHStep.LOAD_BATCH == _step) {
			batchId = getCurrentBatchId(userInfo);
			if (batchId == null || batchId.isEmpty())
				return new ArrayList<>();
		}
		
		if(WHStep.ALLOCATION == _step && seachCase.getAssignType() != null && !seachCase.getAssignType().equals(0)){
			CodeTableDTO ctAssignType = CodeTableCacheManager.getInstance().getbyID(seachCase.getAssignType());
			if (ctAssignType.getCodeValue1().equalsIgnoreCase(CTCodeValue1.WH_NOT.value())) {			
				seachCase.setAssignType(0);
			}
		}
		
		List<WareHouseSeachObject> lstSeach = (List<WareHouseSeachObject>) this.unitOfWork.warehouse.whDocumentRepo().searchCaseInput(batchId, false, _step, seachCase, userInfo.getLoginId(), wareHouseCodeTableID, pageSize, pageNum);
		
		/*For check show in InputCase*/
		if(WHStep.INPUTCASE == _step || WHStep.LOAD_BATCH ==_step){
			HashMap< String, WareHouseSeachObject>  listShow = new HashMap<>();
			List<WareHouseSeachObject> lstSeachNew  = new ArrayList<>();
			for (WareHouseSeachObject wareHouseSeachObject : lstSeach) {
				if(null != wareHouseSeachObject && null != wareHouseSeachObject.getIsOriginal() && 1 == wareHouseSeachObject.getIsOriginal()){
					listShow.put(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsOriginal", wareHouseSeachObject);
				}else if(null != wareHouseSeachObject && null != wareHouseSeachObject.getIsActive() && 1 == wareHouseSeachObject.getIsActive()) {
					listShow.put(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsActive", wareHouseSeachObject);
				}else{
					listShow.put(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "New", wareHouseSeachObject);
				}
			}
			
			for (WareHouseSeachObject wareHouseSeachObject : lstSeach) {
				if(listShow.get(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsOriginal") != null){
					lstSeachNew.add(listShow.get(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsOriginal"));
				}else if (listShow.get(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsActive") != null) {
					lstSeachNew.add(listShow.get(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "IsActive"));
				}else{
					lstSeachNew.add(listShow.get(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode() + "New"));
				}
			}
			lstSeach =   lstSeachNew.parallelStream().distinct().collect(Collectors.toList());
		}
		
		for (WareHouseSeachObject wareHouseSeachObject : lstSeach) {

			CTCodeValue1 statusCode = CTCodeValue1.from(wareHouseSeachObject.getStatusWHCode().toString());

			if ((wareHouseSeachObject.getWhCodeId() != null && !wareHouseSeachObject.getWhCodeId().equals(0l)
					&& (wareHouseSeachObject.getStatusWHCode() != null && (statusCode == CTCodeValue1.WH_LODGED_COMPLETE
							|| statusCode == CTCodeValue1.WH_LODGED_ERR_UPDATE
							|| statusCode == CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW
							|| statusCode == CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN
							|| statusCode == CTCodeValue1.WH_LODGED_RETURN
							|| statusCode == CTCodeValue1.WH_LODGE_COMPLETE_BORROW)))
					|| (statusCode == CTCodeValue1.WH_WAIT_COMPLETE || statusCode == CTCodeValue1.WH_WAIT_ERR_UPDATE)) {
				hashLodge.put(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode(), wareHouseSeachObject.getWhId());
			}

			if ((wareHouseSeachObject.getWhCodeId() == null
					&& (wareHouseSeachObject.getStatusWHCode() != null && statusCode == CTCodeValue1.WH_NEW_WAIT))) {
				hashNewWait.put(wareHouseSeachObject.getCreditAppId() + wareHouseSeachObject.getDocTypeCode(), wareHouseSeachObject.getWhId());
			}
		}
		 
		 /*Original for WH_ERR_UPDATE */
		 List<Long> creditAppIdLst = lstSeach.parallelStream().map(e -> e.getCreditAppId()) .filter(creditAppId -> creditAppId != null).collect(Collectors.toList());
		 
		 HashMap<String, WhDocumentDTO> hashOriginal = new HashMap<>();
		 List<WhDocumentDTO> listOriginal = null;
		 
		 /* Danh sach loi */
		 List<Long> listWhdocId = lstSeach.parallelStream().map(e -> e.getWhId()) .filter(whId -> whId != null).collect(Collectors.toList());
		 HashMap<Long, List<WhDocumentErrDTO>> hashListError = new HashMap<>();
		 
		 if (WHStep.LOAD_BATCH == _step || WHStep.INPUTCASE == _step ||  WHStep.FOLLOWCASE == _step) {
			 
			 if(creditAppIdLst != null && creditAppIdLst.size() > 0)
			   listOriginal = this.unitOfWork.warehouse.whDocumentRepo().getOriginalByCreditAppId(creditAppIdLst);
			
			if(listOriginal != null && listOriginal.size() > 0){
				for (WhDocumentDTO whDoc : listOriginal) {
					WhDocumentDTO original = hashOriginal.get(whDoc.getCreditAppId() + whDoc.getDocType());
					hashOriginal.put("" + whDoc.getCreditAppId() + whDoc.getDocType(), original);
					listWhdocId.add(whDoc.getId());
				}				
			}
		 }		 
		 listWhdocId.parallelStream().distinct().collect(Collectors.toList());
		 
		 /*List Date SendThanklater , DateErrCavet , DateErrLoanDoc */
		 if(WHStep.LOAD_BATCH == _step || WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step){
			 sendThankLater = getSendThankLetterByCreditAppId(creditAppIdLst);
			 dateErrLoanDoc = getDateErrLoanDocByCreditAppId(creditAppIdLst);
			 dateErrCavet = getDateErrCavetByCreditAppId(creditAppIdLst);
		 }
		
		 if( WHStep.INPUTCASE == _step || WHStep.LOAD_BATCH == _step || WHStep.OPERATOR_TWO == _step|| WHStep.LODGE_CAVET == _step|| WHStep.BORROW_CAVET == _step || WHStep.RETURN_CAVET == _step || WHStep.OPERATOR_TWO == _step || WHStep.APPROVE == _step) {
			if(!listWhdocId.isEmpty()){
				hashListError = this.unitOfWork.warehouse.whDocumentErrRepo().getListWareHouseErrorListCase(listWhdocId);
			}
		 }
		 
		for (Map.Entry<String, WhDocumentDTO> entry : hashOriginal.entrySet()) {
			if(entry.getValue()!= null && ( entry.getValue() != null )){
				 entry.getValue().setLstWHCaseError(docTypeLoan.equals(entry.getValue().getDocType()) ? hashListError.get(entry.getValue().getId()) : null );
				 entry.getValue().setLstWHCavetError(docTypeCavet.equals(entry.getValue().getDocType()) ? hashListError.get(entry.getValue().getId()) : null );
				 hashOriginal.put(entry.getKey(), (WhDocumentDTO) entry);
			}
		}
		 
		if (lstSeach != null && lstSeach.size() > 0) {
			for (WareHouseSeachObject ojb : lstSeach) {	
				CTCodeValue1 docTypeTMP = CTCodeValue1.from(ojb.getDocTypeCode());				
				if(( WHStep.INPUTCASE == _step || WHStep.LOAD_BATCH == _step || WHStep.OPERATOR_TWO == _step|| WHStep.LODGE_CAVET == _step|| WHStep.BORROW_CAVET == _step || WHStep.RETURN_CAVET == _step || WHStep.OPERATOR_TWO == _step || WHStep.APPROVE == _step)){
					if (!CTCodeValue1.WH_NEW_WAIT.value().equals(ojb.getStatusWHCode())){ /* neu ho so la moi nhan cho kiem tra thi khong sau danh sach loi*/
						if (CTCodeValue1.WH_LOAN_DOC == docTypeTMP ) 
							ojb.setLstWHCaseError(hashListError.get(ojb.getWhId()));
		
						if (CTCodeValue1.WH_CAVET == docTypeTMP)
							ojb.setLstWHCavetError(hashListError.get(ojb.getWhId()));
						
						if (CTCodeValue1.WH_ERR_UPDATE == docTypeTMP){
							if(WareHouseEnum.CONTRACTCAVETTYPE_CONTRACT.longValue().equals(ojb.getContractCavetType())){
								ojb.setLstWHCaseError(hashListError.get(ojb.getWhId()));
							}
							if(WareHouseEnum.CONTRACTCAVETTYPE_CAVET.longValue().equals(ojb.getContractCavetType())){
								ojb.setLstWHCavetError(hashListError.get(ojb.getWhId()));
							}
						}					
					}
				}
				
				if(ojb.getCreditAppId() != null && (WHStep.LOAD_BATCH == _step || WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step)){
					ojb.setThankAndkErrorDate(ojb,sendThankLater.get(ojb.getCreditAppId()),dateErrCavet.get(ojb.getCreditAppId()),dateErrLoanDoc.get(ojb.getCreditAppId()));
				}
					
				if (ojb.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.value())) {
					if (ojb.getContractCavetType() != null && ojb.getContractCavetType().equals(1l)) {
						if (hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC) != null) {
							ojb.setIdCheckContractCavet(hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC));
						} else {
							ojb.setIdCheckContractCavet(hashNewWait.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC));
						}
					} else {
						if (hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET) != null) {
							ojb.setIdCheckContractCavet(hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET));
						} else {
							ojb.setIdCheckContractCavet(hashNewWait.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET));
						}
					}
				}

				if (ojb.getDocTypeCode().equals(CTCodeValue1.WH_CAVET.value())) {
					if (hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET) != null) {
						ojb.setIdCheckContractCavet(hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET));
					} else {
						ojb.setIdCheckContractCavet(hashNewWait.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_CAVET));
					}
				}

				if (ojb.getDocTypeCode().equals(CTCodeValue1.WH_LOAN_DOC.value())) {
					if (hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC) != null) {
						ojb.setIdCheckContractCavet(hashLodge.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC));
					} else {
						ojb.setIdCheckContractCavet(hashNewWait.get(ojb.getCreditAppId().toString() + CTCodeValue1.WH_LOAN_DOC));
					}					
				}

				if (!ojb.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.value())
						&& ojb.getIdCheckContractCavet() == null) {
					ojb.setIdCheckContractCavet(ojb.getWhId());
				}
			
				if (WHStep.LOAD_BATCH == _step) {

					HashMap<String, List<WareHouseSeachObject>> hashLoadBatchCase = new HashMap<>();
					StringBuilder contractNum = new StringBuilder();

					for (WareHouseSeachObject wareHouseSeachObject : lstSeach) {
						List<WareHouseSeachObject> listWHObject = hashLoadBatchCase.get(wareHouseSeachObject.getContractNum());

						if (listWHObject == null) {
							listWHObject = new ArrayList<>();
						}
						listWHObject.add(wareHouseSeachObject);
						hashLoadBatchCase.put(wareHouseSeachObject.getContractNum(), listWHObject);

						if (wareHouseSeachObject.getDocTypeCode().equals(CTCodeValue1.WH_ERR_UPDATE.value())) {
							contractNum.append(wareHouseSeachObject.getContractNum());
						}
					}

					if (WHStep.LOAD_BATCH == _step || WHStep.INPUTCASE == _step || WHStep.OPERATOR_TWO == _step || WHStep.LODGE_CONTRACT == _step || WHStep.LODGE_CAVET == _step || WHStep.BORROW_CAVET == _step) {
						if (docTypeErrUpdate.equals(ojb.getDocTypeId())) {
							List<WhDocumentDTO> lstWHOriginal = new ArrayList<>();
							lstWHOriginal.add(hashOriginal.get(ojb.getWhId() + docTypeLoan));
							lstWHOriginal.add(hashOriginal.get(ojb.getWhId() + docTypeCavet));
						}
					}
				}
			}
		}

		return lstSeach;
	}

	private HashMap<Long, Date> getDateErrCavetByCreditAppId(List<Long> creditAppIdLst) {
		return this.unitOfWork.warehouse.whExpectedDateRepo().getDateErrCavetByCreditAppId(creditAppIdLst);
	}

	private HashMap<Long, Date> getDateErrLoanDocByCreditAppId(List<Long> creditAppIdLst) {
		return this.unitOfWork.warehouse.whExpectedDateRepo().getDateErrLoanDocByCreditAppId(creditAppIdLst);
	}

	private HashMap<Long, Date> getSendThankLetterByCreditAppId(List<Long> creditAppIdLst) {
		return this.unitOfWork.warehouse.whExpectedDateRepo().getSendThankLetterByCreditAppId(creditAppIdLst);
	}

	public Integer countSeachCase(WHStep _step, SearchCaseInput seachCase, UserDTO userInfo,WareHouseCodeTableCacheDTO wareHouseCodeTableID) {
		return (Integer) this.unitOfWork.warehouse.whDocumentRepo().searchCaseInput(null,true, _step, seachCase, userInfo.getLoginId(),wareHouseCodeTableID, null, null);
	}

	public String getCurrentBatchId(UserDTO userInfo) {
		return (String) this.unitOfWork.warehouse.whDocumentRepo().getCurrentBatchId(userInfo.getLoginId());
	}

	public Object findRemainDocument(WHStep _step, List<Long> caseReturn, UserDTO userInfo) {
 		List<Object> errorUpdate = new ArrayList<>();
		List<Object> sucsessUpdate = new ArrayList<>();
		Long allocationStatus = ctCache.getIdBy(CTCodeValue1.WH_RETURN_DOC_NOT_RECEIVE, CTCat.WH_LODGE);
		Long ctDocChangeType = ctCache.getIdBy(CTCodeValue1.WH_STATUS_CHANGE, CTCat.WH_CHAN_TYPE);
		Long ctReturnDocNotReceive = ctCache.getIdBy(CTCodeValue1.WH_RETURN_DOC_NOT_RECEIVE, CTCat.WH_LODGE);
		for (Long i : caseReturn) {
			try {
				WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(i);
				if (WHStep.ALLOCATION == _step)
					whDocument.setStatus(allocationStatus);

				if (WHStep.OPERATOR_TWO == _step)
					whDocument.setStatus(allocationStatus);

				WhDocument whDocumentActive = this.unitOfWork.warehouse.whDocumentRepo().getDocumentByActive(whDocument.getCreditAppId(), whDocument.getDocType());
				whDocumentActive.setIsActive(notActive);
				this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocumentActive);
				
				whDocument.setIsActive(active);
				whDocument.setLastUpdatedBy(userInfo.getLoginId());
				whDocument.setLastUpdatedDate(new Date());
				whDocument.setStatus(ctReturnDocNotReceive);
				this.unitOfWork.warehouse.whDocumentRepo().update(whDocument);

				this.unitOfWork.warehouse.whDocumentChangeRepo()
						.add(new WhDocumentChange(whDocument.getId(), ctDocChangeType, "", userInfo.getLoginId(),
								new Date(), ctReturnDocNotReceive, whDocument.getCreditAppId()));

				sucsessUpdate.add(i);
			} catch (Exception e) {
				errorUpdate.add(i);
				
				e.printStackTrace();
			}
		}

		return new ListObjectResult(errorUpdate, sucsessUpdate);
	}

	public void updateAllocationStatus(Long i, Long statusAssign, Long idTypeAssign, String note, String loginId) {

		WhDocumentChange whDocChange = new WhDocumentChange(i, idTypeAssign, note, loginId, new Date(), statusAssign);
		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(whDocChange);
	}

	public CheckRecordsCavetDTO getCheckRecordsCavet(Long whId) {
		WareHouseCodeTableCacheDTO wareHouseCodeTableID = WHCodeTableCacheManager.getInstance().getWareHouseCodeTableID();
		CheckRecordsCavetInfo out = this.unitOfWork.warehouse.whDocumentRepo().getCheckRecordsCavet(whId, wareHouseCodeTableID.getWhUplDetailId());
		return modelMapper.map(out, CheckRecordsCavetDTO.class);
	}

	public void changeStatus(Long id, Long changeStatus, String loginId, String note, Long type) {

		if (changeStatus != null) {
			WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);
			whDocument.setStatus(changeStatus);
			whDocument.setLastUpdatedBy(loginId);
			whDocument.setLastUpdatedDate(new Date());

			this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);
		}

		WhDocumentChange wChange = new WhDocumentChange(id, type, note, loginId, new Date(), changeStatus);

		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(wChange);
	}

	public void changeDocumentType(Long id, String loginId, Long documentType) {

		WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);

		whDocument.setDocType(documentType);
		whDocument.setLastUpdatedBy(loginId);
		whDocument.setLastUpdatedDate(new Date());

		this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);

	}

	public void saveDocHistory(Long id) {
		WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);
		whDocument.setVersion(whDocument.getVersion() + 1);
		// WhDocumentChange whDocumentChange = new WhDocumentChange(id, type,
		// note, createdBy, createdDate, null, null);
		this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocument);

	}

	/*
	 * public void changeDocumentType(Long id, Long documentType, String
	 * loginId, Long contractCavetType, Long documengChangeType) { WhDocument
	 * whDocument = _uok.whDocumentRepo().findById(id);
	 * 
	 * whDocument.setDocType(documentType);
	 * whDocument.setLastUpdatedBy(loginId); whDocument.setLastUpdatedDate(new
	 * Date()); whDocument.setContractCavetType(contractCavetType);
	 * 
	 * WhDocumentChange wChange = new WhDocumentChange(id, documengChangeType,
	 * null, loginId, new Date(), documentType, null);
	 * 
	 * _uok.whDocumentChangeRepo().upsert(wChange);
	 * _uok.whDocumentRepo().upsert(whDocument); }
	 */

	public void changeDocumentType(Long id, Long documentType, String loginId, Long contractCavetType,
			Long documengChangeType) {

		WhDocument whDocument = this.unitOfWork.warehouse.whDocumentRepo().findById(id);
		whDocument.setId(null);
		this.unitOfWork.warehouse.whDocumentRepo().clone(whDocument);

		whDocument.setDocType(documentType);
		// whDocument.setLastUpdatedBy(loginId);
		whDocument.setCreatedBy(null);
		whDocument.setLastUpdatedDate(new Date());
		whDocument.setContractCavetType(contractCavetType);

		this.unitOfWork.warehouse.whDocumentRepo().add(whDocument);

		WhDocumentChange wChange = new WhDocumentChange(whDocument.getId(), documengChangeType, null, loginId,
				new Date(), documentType);
		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(wChange);
	}

	public WhDocument getById(Long id) {
		return this.unitOfWork.warehouse.whDocumentRepo().getById(id);
	}

	public void cloneWhDocument(WhDocument clone, String loginId) {		
		clone.setId(null);
		clone.setCreatedDate(new Date());
		clone.setCreatedBy(loginId);
		clone.setLastUpdatedBy(null);
		clone.setLastUpdatedDate(null);
		clone.setVersion(clone.getVersion() != null ? (clone.getVersion() + 1) : 1);
		clone.setWhCodeId(null);
		clone.setStatus(newWaitStatus);
		clone.setWhLodgeDate(null);
		clone.setWhLodgeBy(null);
		clone.setContractCavetType(null);
		
		this.unitOfWork.warehouse.whDocumentRepo().clone(clone);
	}
	
	public void changeDocLodge(WhDocument obj, String billCode, String deliveryError, String loginId, Long type) {
		Long whId = null;

		/*WhDocument cloneDoc = SerializationUtils.clone(obj);*/
		Long currentVersion = this.unitOfWork.warehouse.whDocumentRepo().getVersionDocumentByAppId(obj.getCreditAppId(),
				ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE));
		Long version = currentVersion != null ? currentVersion + 1 : 1;
		
		Long currentOrderBy = this.unitOfWork.warehouse.whDocumentRepo().getCurrentOrderBy(obj.getCreditAppId(),
				ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE));
		Long orderBy = currentOrderBy != null ? currentOrderBy + 1 : 1;

		/*WhDocument newDocument = new WhDocument(null, version, cloneDoc.getCreditAppId(),
				ctCache.getIdBy(CTCodeValue1.WH_ERR_UPDATE, CTCat.WH_DOC_TYPE), cloneDoc.getBatchId(),
				cloneDoc.getOrderBy(), cloneDoc.getStatus(), cloneDoc.getEstimateDate(), cloneDoc.getWhCodeId(),
				cloneDoc.getWhLodgeBy(), cloneDoc.getWhLodgeDate(), LOAN_DOC_TYPE,
				loginId, new Date(), null, null);*/
		
		try {
			WhDocument whErrUpdateActive = this.unitOfWork.warehouse.whDocumentRepo().getDocumentByActive(obj.getCreditAppId(), docTypeErrUpdate);
			whErrUpdateActive.setIsActive(notActive);
			this.unitOfWork.warehouse.whDocumentRepo().upsert(whErrUpdateActive);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		WhDocument newDocument = new WhDocument(null, version, obj.getCreditAppId(), docTypeErrUpdate, obj.getBatchId(),
				orderBy, obj.getEstimateDate(), obj.getWhCodeId(), obj.getWhLodgeDate(), new Date(), loginId, null,
				null, LOAN_DOC_TYPE, obj.getStatus(), obj.getWhLodgeBy(), notProcessed, billCode, deliveryError, null,
				active, notOriginal, notAppendixContract, null);

		this.unitOfWork.warehouse.whDocumentRepo().upsert(newDocument);
		whId = newDocument.getId();
		
		WhDocumentChange whChange = new WhDocumentChange();
		whChange.setWhDocId(whId);
		whChange.setNote("t\u00E0i li\u1EC7u v\u1EC1 l\u1EA7n " + obj.getVersion());
		whChange.setType(type);
		whChange.setCreatedBy(loginId);
		whChange.setCreatedDate(new Date());
		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(whChange);
	}

	public void changeDocLodge1(WhDocument obj, String billCode, String deliveryError, String loginId, Long type) {
		Long whId = null;

		WhDocument cloneDoc = SerializationUtils.clone(obj);
		Long currentVersion = this.unitOfWork.warehouse.whDocumentRepo().getVersionDocumentByAppId(obj.getCreditAppId(),
				obj.getDocType());
		Long version = currentVersion != null ? currentVersion + 1 : 1;

		/*WhDocument newDocument = new WhDocument(null, version, cloneDoc.getCreditAppId(), cloneDoc.getDocType(),
				cloneDoc.getBatchId(), cloneDoc.getOrderBy(), cloneDoc.getStatus(), cloneDoc.getEstimateDate(),
				cloneDoc.getWhCodeId(), cloneDoc.getWhLodgeBy(), cloneDoc.getWhLodgeDate(),
				cloneDoc.getContractCavetType(), loginId, new Date(), null, null);*/
		
		WhDocument newDocument = new WhDocument(null, version, cloneDoc.getCreditAppId(), cloneDoc.getDocType(),
				cloneDoc.getBatchId(), cloneDoc.getOrderBy(), cloneDoc.getEstimateDate(), cloneDoc.getWhCodeId(),
				cloneDoc.getWhLodgeDate(), new Date(), loginId, null, null, cloneDoc.getContractCavetType(),
				cloneDoc.getStatus(), cloneDoc.getWhLodgeBy(), notProcessed, billCode, deliveryError, null, active,
				notOriginal, notAppendixContract, null);

		this.unitOfWork.warehouse.whDocumentRepo().upsert(newDocument);
		whId = newDocument.getId();
		
		try {
			WhDocument whDocumentActive = this.unitOfWork.warehouse.whDocumentRepo().getDocumentByActive(cloneDoc.getCreditAppId(), cloneDoc.getDocType());
			whDocumentActive.setIsActive(notActive);
			this.unitOfWork.warehouse.whDocumentRepo().upsert(whDocumentActive);
		} catch (Exception e) {
			e.printStackTrace();
		}

		WhDocumentChange whChange = new WhDocumentChange();
		whChange.setWhDocId(whId);
		whChange.setNote("t\u00E0i li\u1EC7u v\u1EC1 l\u1EA7n " + obj.getVersion());
		whChange.setType(type);
		whChange.setCreatedBy(loginId);
		whChange.setCreatedDate(new Date());
		this.unitOfWork.warehouse.whDocumentChangeRepo().upsert(whChange);
	}

	public List<ReturnDocumentInfo> getLstReturnDocument(Long whId) {
		List<ReturnDocumentInfo> resultsDocumentInfos = this.unitOfWork.warehouse.whDocumentRepo().getLstReturnDocument(whId);
		return resultsDocumentInfos;
	}

	public List<ResultsComtractDocumentInfo> getLstResultsContractDocumentInfo(Long whId,
			List<LoanDocRespone> lstLoanDoc) {

		HashMap<String, ResultsComtractDocumentInfo> hashReContractDocument = new HashMap<>();

		List<ResultsDocumentInfo> resultsDocumentInfos = this.unitOfWork.warehouse.whDocumentChangeRepo().getResultsDocuments(whId);

		for (ResultsDocumentInfo resultsDocumentInfo : resultsDocumentInfos) {
			ResultsComtractDocumentInfo lstReContractDoc = hashReContractDocument
					.get(resultsDocumentInfo.getObjectId());
			if (lstReContractDoc != null)
				lstReContractDoc.getErrorList().add(resultsDocumentInfo);
			else {
				List<ResultsDocumentInfo> listError = new ArrayList<>();
				listError.add(resultsDocumentInfo);
				lstReContractDoc = new ResultsComtractDocumentInfo(listError);
			}

			hashReContractDocument.put(resultsDocumentInfo.getObjectId(), lstReContractDoc);
		}

		HashMap<String, LoanDocRespone> hashDocumentInfo = new HashMap<>();

		if (lstLoanDoc != null && !lstLoanDoc.isEmpty()) {
			for (LoanDocRespone lstLoan : lstLoanDoc) {
				hashDocumentInfo.put(lstLoan.getObjectId(), lstLoan);
			}
		}

		for (Map.Entry<String, LoanDocRespone> entry : hashDocumentInfo.entrySet()) {

			ResultsComtractDocumentInfo resout = hashReContractDocument.get(entry.getKey()) == null
					? new ResultsComtractDocumentInfo(null) : hashReContractDocument.get(entry.getKey());

			resout.setScanId(entry.getValue().getObjectId());
			resout.setScanName(entry.getValue().getObjectName());
			resout.setUrlDocument(entry.getValue().getObjectUrl());

			hashReContractDocument.put(entry.getKey(), resout);
		}

		List<ResultsComtractDocumentInfo> listContracDoc = new ArrayList<>();
		for (Map.Entry<String, ResultsComtractDocumentInfo> entry : hashReContractDocument.entrySet()) {
			listContracDoc.add(hashReContractDocument.get(entry.getKey()));
		}

		return listContracDoc;
	}

	public List<ResultsDocumentInfo> getLstResultsCavetDocumentInfo(Long whId, List<LoanDocRespone> lstLoanDoc) {

		return this.unitOfWork.warehouse.whDocumentChangeRepo().getResultsDocuments(whId);

	}

	public void deleteDocument(String whIds) {
		String[] str = whIds.trim().split(",");
		List<String> lstParam = new ArrayList<String>();
		for (int i = 0; i < str.length; i++) {
			lstParam.add(str[i]);

			WhDocument documentInfo = this.unitOfWork.warehouse.whDocumentRepo().getById(Long.valueOf(str[i]));
			if (documentInfo.getVersion() != null) {
				this.unitOfWork.warehouse.whDocumentRepo().updateActive(documentInfo.getCreditAppId(),
						documentInfo.getDocType());
			}
		}
		Long batch_id = LongUtil.uniqueId();

		this.unitOfWork.warehouse.whDocumentRepo().updateBatch(lstParam, batch_id);
		this.unitOfWork.warehouse.whDocumentRepo().deleteDocument(lstParam);
	}

	public void deleteDocumentChangeByWhIdAndType(Long whId, Long type) {
		this.unitOfWork.warehouse.whDocumentChangeRepo().deleteErrById(whId, type);
	}

	public AllocationDetail checkPemisionData(Long whDocumentId, Long userId) {
		return this.unitOfWork.common.allocationDetailRepo().checkPemisionData(whDocumentId, userId);
	}

	public List<Long> getLstDocId(Long whId) {
		return this.unitOfWork.warehouse.whDocumentRepo().getLstIdDoc(whId);
	}

	public List<Long> getLstWhIdReturnDocument(Long whId, boolean isCavet) {
		if (isCavet)
			return this.unitOfWork.warehouse.whDocumentRepo().getLstIdCavet(whId);
		else
			return this.unitOfWork.warehouse.whDocumentRepo().getLstIdDoc(whId);
	}

	public List<ReturnDocumentInfo> getLstDoc(List<Long> lstWhId) {
		List<ReturnDocumentInfo> resultsDocumentInfos = this.unitOfWork.warehouse.whDocumentRepo().getLstReturnDoc(lstWhId);
		return resultsDocumentInfos;
	}

	public Integer sliptWareHousepramaeter(String codeValue1) {
		return ParametersCacheManager.getInstance()
				.findParamValueAsInteger(WareHouseEnum.SUB_DAYS_BORROW_.stringValue() + codeValue1);
	}

	public List<WhDocumentChangeDTO> getLstResultsContractDocumentInfo(Long whId) {
		return this.unitOfWork.warehouse.whDocumentChangeRepo().getListWareHouseErrorCase(whId);
	}
	
	public CreditApplicationBPM getCreditApplicationBPMBy(String bpmAppId) {
		return this.unitOfWork.credit.creditApplicationBPMRepo().getBy(bpmAppId);
	}	
	
	public String insertQRCode(QRCodeDTO data,CreditApplicationBPM bpm) throws Exception {
		
		//get max version
		Integer version = unitOfWork.credit.creditApplicationSignatureRepo().getLatestVersionBy(data.getSoHopDong());
		version = version == null ? 1 : version + 1;
		
		data.setVersion(version.toString());
		String signature = SHA256.create(data.toString());
		
		CreditApplicationSignature item = new CreditApplicationSignature(data.getCreatedBy(),new Date(),bpm.getCreditAppId(),"A",version.longValue(),data.getSoHopDong(),signature,JSONConverter.toJSON(data));
		
		//insert into QRCode Database
		unitOfWork.credit.creditApplicationSignatureRepo().add(item);
		
		QRCodeInfo qrInfo = new QRCodeInfo(data.getSoHopDong(),data.getAppId(),data.getAppNumber(),version,signature);
		
		String rootPath = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY);
		return QRUtil.createImage(JSONConverter.toJSON(qrInfo),200,200,rootPath);

	}
	
	public CreditApplicationSignature updateQRCode(QRCodeCheckDTO qrCode,String loginId) throws Exception {
		
		CreditApplicationSignature item = unitOfWork.credit.creditApplicationSignatureRepo().getBy(qrCode.getContractNumber(), qrCode.getVersion().toString(), qrCode.getSignature(), qrCode.getAppID(), qrCode.getAppNumber());
		if(item == null)
			throw new Exception("Invalid QR Code");

		return item;
	}
       
    public String createLodgeCode(String value) {
        List<Param> params = new ArrayList<Param>();
        Param param = new Param();
        param.setTypeData("string");
        param.setValue(value);
        params.add(param);
        String[] lodgeCodeArr = StringUtils.nullToEmpty(unitOfWork.warehouse.whCodeRepos().callFunctionReturnSingleRow("GET_CODE_SAVE_WH", params, "string")).split("###");
        return lodgeCodeArr[0].trim();
    }
	
    //Lay ra ho so goc cua giay to update loi
    public Long getOriginWhDocument(Long idErr) {
        WhDocument whErr = this.unitOfWork.warehouse.whDocumentRepo().getById(idErr);

        Long originWhId = this.unitOfWork.warehouse.whDocumentRepo().getOriginWhDocument(whErr).get(0);

        return originWhId;

    }
    
    // Danh dau ho so da duoc bam luu va chuyen o man hinh kiem tra cavet/hskv
	public boolean markSaveAndMoveInCheck(Long id, String userLogin, Long idCodetable){
		Long changeStatus = ctCache.getIdBy(CTCodeValue1.WH_SAVE_AND_MOVE, CTCat.WH_CHAN_TYPE);
		this.unitOfWork.warehouse.whDocumentChangeRepo().deleteErrById(id, changeStatus);
		WhDocumentChange item = new WhDocumentChange(id, changeStatus, idCodetable,
				userLogin, new Date(), "");
		this.unitOfWork.warehouse.whDocumentChangeRepo().add(item);
		return true;
	}
        
    public void processRoot(WhDocument whDocument, boolean isNotOrPass, Long idCodeTableStatus, 
            ReturnDocumentInfo originalDocument) throws ValidationException {
        if (whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
            if (isNotOrPass == false) {
                if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))){
                    whDocument.setStatus(chanStatusCavet(originalDocument, isNotOrPass));
                } else {
                    whDocument.setStatus(idCodeTableStatus);
                }
            }
        } else if ((whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE_RETURN, CTCat.WH_LODGE, CTGroup.WARE_HOUSE)))) {
            if (isNotOrPass == true) {
                throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet")));
            }
        } else if(whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                  || whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGE_COMPLETE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))){
            if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))){
                whDocument.setStatus(chanStatusCavet(originalDocument, isNotOrPass));
            } else {
                whDocument.setStatus(idCodeTableStatus);
            }   
        } else if (whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))){
                whDocument.setStatus(idCodeTableStatus);
        }        
    }   
        
    public void processCavet(Long whId, WhDocument whDocument, boolean isNotOrPass, Long beforeId,
            ReturnDocumentInfo originalDocument) throws ValidationException {
        HashMap<String, WhDocument> maplstRoot = this.unitOfWork.warehouse.whDocumentRepo().getRootUpdateErr(whId);
        WhDocument whDocumentLoanDocRoot = maplstRoot.get("rootLoanDoc");
        WhDocument whDocumentCavetRoot = maplstRoot.get("rootCavet");
        Long statusCavetId = chanStatusCavet(originalDocument, isNotOrPass);
        if (whDocumentCavetRoot != null) {
            if (whDocument.getId().equals(beforeId) && whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                if (whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                    whDocumentCavetRoot.setStatus(statusCavetId);
                    if (whDocumentLoanDocRoot != null) {
                        if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
                            whDocument.setWhCodeId(whDocumentCavetRoot.getWhCodeId());
                        } else {
                            whDocument.setWhCodeId(whDocumentLoanDocRoot.getWhCodeId());
                        }
                    }
                    whDocument.setStatus(statusCavetId);
                } else if ((whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGE_COMPLETE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                        || whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE_RETURN, CTCat.WH_LODGE, CTGroup.WARE_HOUSE)))) {
                    if (isNotOrPass == false) {
                        whDocument.setStatus(whDocumentCavetRoot.getStatus());
                        if (whDocumentLoanDocRoot != null) {
                            if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
                                whDocument.setWhCodeId(whDocumentCavetRoot.getWhCodeId());
                            } else {
                                whDocument.setWhCodeId(whDocumentLoanDocRoot.getWhCodeId());
                            }
                        }
                    } else {
                        throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet")));
                    }
                }

            } else if (!whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                if (isNotOrPass == false) {
                    whDocument.setStatus(whDocumentCavetRoot.getStatus());
                    if (whDocumentLoanDocRoot != null) {
                        if (whDocument.getDocType().equals(ctCache.getIdBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE))) {
                            whDocument.setWhCodeId(whDocumentCavetRoot.getWhCodeId());
                        } else {
                            whDocument.setWhCodeId(whDocumentLoanDocRoot.getWhCodeId());
                        }
                    }
                }
            }
            this.unitOfWork.warehouse.whDocumentRepo().merge(whDocumentCavetRoot);

        } 
//        else if (whDocumentCavetRoot != null) {
//            if (!whDocumentCavetRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
//                if (whDocument.getId().equals(beforeId) && whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
//                    whDocument.setStatus(statusCavetId);
//                } else if (!whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
//                    whDocument.setStatus(statusCavetId);
//                }
//            }
//        }
    }
    
    public void processLoanDoc(Long whIdRoot, WhDocument whDocument, boolean isNotOrPass, Long beforeId, Long idCodeTableStatus) throws ValidationException {
        WhDocument whDocumentRoot = (WhDocument) this.unitOfWork.warehouse.whDocumentRepo().get(WhDocument.class, whIdRoot);

        if (whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_RETURN, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
            if (whDocument.getId().equals(beforeId) && whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                if (isNotOrPass == false) {
                    whDocumentRoot.setStatus(idCodeTableStatus);
                } 
                whDocument.setStatus(idCodeTableStatus);
            } else if (!whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                whDocument.setStatus(idCodeTableStatus);
            }

        } else if (whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_WAIT_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                || whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
            if (isNotOrPass == false) {
                //update trang thai cua tat ca thang con theo cha tru trang thai moi nhan cho kiem tra
                if (whDocument.getId().equals(beforeId)
                        && whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                    whDocument.setStatus(whDocumentRoot.getStatus());
                } else if (!whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                    whDocument.setStatus(whDocumentRoot.getStatus());
                }
                if (whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {                       
                    if (whDocument.getId().equals(beforeId)
                        && whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                        whDocument.setWhCodeId(whDocumentRoot.getWhCodeId());
                    } else if (!whDocument.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_NEW_WAIT, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))) {
                        whDocument.setWhCodeId(whDocumentRoot.getWhCodeId());
                    }
                }

            } else {
                throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet")));
            }
        } else if (whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))
                   || whDocumentRoot.getStatus().equals(ctCache.getIdBy(CTCodeValue1.WH_LODGE_COMPLETE_BORROW, CTCat.WH_LODGE, CTGroup.WARE_HOUSE))){
            throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.HSKV.borrow")));
        } else {
            whDocument.setStatus(idCodeTableStatus);
        }
        this.unitOfWork.warehouse.whDocumentRepo().merge(whDocumentRoot);
    }
    
    public Long chanStatusCavet(ReturnDocumentInfo originalDocument, Boolean isReturnDocumentHasError) throws ValidationException{
        CTCodeValue1 newCodetableStatus = null;
        Boolean isOriginalDocumentNotLodged = originalDocument.getStatusCode()
				.equals(CTCodeValue1.WH_NEW_WAIT.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_WAIT_COMPLETE.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_WAIT_ERR_UPDATE.toString());
		Boolean isOriginalDocumentLodged = (originalDocument.getStatusCode()
				.equals(CTCodeValue1.WH_LODGED_COMPLETE.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_LODGED_ERR_UPDATE.toString()));
		Boolean isOriginalDocumentLodgedBorrow = (originalDocument.getStatusCode()
				.equals(CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW.toString())
				|| originalDocument.getStatusCode().equals(CTCodeValue1.WH_LODGE_COMPLETE_BORROW.toString()));

		if (isOriginalDocumentNotLodged) {
			if (isReturnDocumentHasError) {
				newCodetableStatus = CTCodeValue1.WH_WAIT_ERR_UPDATE;
			} else {
				newCodetableStatus = CTCodeValue1.WH_WAIT_COMPLETE;
			}
		} else if (isOriginalDocumentLodged) {
			if (isReturnDocumentHasError) {
				newCodetableStatus = CTCodeValue1.WH_LODGED_ERR_UPDATE;
			} else {
				newCodetableStatus = CTCodeValue1.WH_LODGED_COMPLETE;
			}
		} else if (isOriginalDocumentLodgedBorrow) {
			if (isReturnDocumentHasError) {
				newCodetableStatus = CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW;
			} else {
				newCodetableStatus = CTCodeValue1.WH_LODGE_COMPLETE_BORROW;
			}
		} else {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.status.checkCavetOrDocumnet.Original")));
		}
                return ctCache.getIdBy(newCodetableStatus, CTCat.WH_LODGE, CTGroup.WARE_HOUSE);
    }
    
    public boolean isSave(Long whId) {
    	Long type = ctCache.getIdBy(CTCodeValue1.WH_ERR, CTCat.WH_CHAN_TYPE, CTGroup.WARE_HOUSE);
    	return this.unitOfWork.warehouse.whDocumentChangeRepo().getLatestStatus(whId, type) != null ? true: false;
    }
    
    public String noteSave(Long whId) {
    	Long type = ctCache.getIdBy(CTCodeValue1.WH_ERR, CTCat.WH_CHAN_TYPE, CTGroup.WARE_HOUSE);
    	WhDocumentChange whDocumentChange = new WhDocumentChange();
    	whDocumentChange = this.unitOfWork.warehouse.whDocumentChangeRepo().getWhDocumentChangeByWhIdAndType(whId, type);
    	return whDocumentChange.getNote();
    }
    
    public WhCavetInfo getCavetInfo(Long whId, Long type) {
    	return this.unitOfWork.warehouse.WareHouseCavetInfoRepository().getCavetInfo(whId, type);
    	
    }
    
    public void updateCavetInfo(Long whId, Long type, CavetInfor cavetInfor, String userLogin) {
    	WhCavetInfo whCavetInfo = this.unitOfWork.warehouse.WareHouseCavetInfoRepository().getCavetInfo(whId, type);
    	if (whCavetInfo != null) {
    		whCavetInfo.setBrand(cavetInfor.getBrand());
        	whCavetInfo.setCavetNumber(cavetInfor.getCavetNumber());
        	whCavetInfo.setChassis(cavetInfor.getChassis());
        	whCavetInfo.setColor(cavetInfor.getColor());
        	whCavetInfo.setCreatedBy(userLogin);
        	whCavetInfo.setEngine(cavetInfor.getEngine());
        	whCavetInfo.setLastUpdatedBy(userLogin);
        	whCavetInfo.setLastUpdatedDate(new Date());
        	whCavetInfo.setModelCode(cavetInfor.getModelCode());
        	whCavetInfo.setnPlate(cavetInfor.getnPlate());
        	whCavetInfo.setWhDocId(whId);
        	whCavetInfo.setType(type);
                
        	this.unitOfWork.warehouse.WareHouseCavetInfoRepository().merge(whCavetInfo);
    	} else {
    		whCavetInfo =  new WhCavetInfo(userLogin, new Date(), userLogin, whId, cavetInfor.getBrand(), cavetInfor.getModelCode(), cavetInfor.getColor(), 
                        cavetInfor.getEngine(), cavetInfor.getChassis(), cavetInfor.getnPlate(), cavetInfor.getCavetNumber(), type);
        	this.unitOfWork.warehouse.WareHouseCavetInfoRepository().add(whCavetInfo);
    	}
    }
    
    public List<WhDocument> getWhDocumentListByCreditId (Long creAppId) {
    	List<WhDocument> whDocumentList = this.unitOfWork.warehouse.whDocumentRepo().getWhDocumentListByCreAppId(creAppId);
    	return whDocumentList;
    }
    
    public String getHandoverBorrowExport(List<WareHouseExportHandoverBorrowedDTO> lstHandoverBorrowDTO) throws IOException {
		try (IExportPdf document = new HandoverExport()) {
			return document.exportPdfBorrowed(lstHandoverBorrowDTO);
		}
	}
    public String getExportHistory(List<WareHouseExportHistoryDTO> lstHistory) throws IOException {
		try (IExportXLS document = new WarehouseHistoryExport(this.unitOfWork.warehouse)) {
			return document.exportWarehouseHistory(lstHistory);
		}
	}

    public Long getWhIdByOriginal(Long creditAppId, Long docType) {
        return this.unitOfWork.warehouse.whDocumentRepo().getWhIdByOriginal(creditAppId, docType);
    }
    
    public Boolean getWhIdByStatusProcess(Long creditAppId, Long docType) {
        return this.unitOfWork.warehouse.whDocumentRepo().getWhIdByStatusProcess(creditAppId, docType);
    }
    
    public List<GoodsDTO> getLstGoods(Long whId) {
		return this.unitOfWork.warehouse.whDocumentRepo().getLstGoods(whId);
	}
    
    public List<ReturnDocumentInfo> getLstReturnDocument2(Long whId) {
		List<ReturnDocumentInfo> resultsDocumentInfos = this.unitOfWork.warehouse.whDocumentRepo().getLstReturnDocument2(whId);
		return resultsDocumentInfos;
	}
    public void upsertWhBorrowedDocument(List<WhBorrowedDocument> listBorrowd) {
        for (int i = 0;i < listBorrowd.size(); i++){
            this.unitOfWork.warehouse.wareHouseBorrowDocumentRepository().add(listBorrowd.get(i));    
        }        
    }
}
