package com.mcredit.service.warehouse;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.business.warehouse.manager.WareHouseManager;
import com.mcredit.model.dto.warehouse.ApproveBorrowCavetDTO;
import com.mcredit.model.dto.warehouse.CavetDTO;
import com.mcredit.model.dto.warehouse.ChangeDocumentDTO;
import com.mcredit.model.dto.warehouse.ContractNumberScannerDTO;
import com.mcredit.model.dto.warehouse.DocumentsDTO;
import com.mcredit.model.dto.warehouse.DocumentsErrorsDTO;
import com.mcredit.model.dto.warehouse.QRCodeCheckDTO;
import com.mcredit.model.dto.warehouse.QRCodeDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverBorrowedDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHistoryDTO;
import com.mcredit.model.dto.warehouse.WareHousePayBackCavetDTO;
import com.mcredit.model.dto.warehouse.WhBorrowedDocumentDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.SearchCaseInput;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.RenewalDocumentDTO;
import com.mcredit.model.object.warehouse.WHAllocationDocInput;
import com.mcredit.model.object.warehouse.WareHousePayBackLetter;
import com.mcredit.service.BasedService;

@Path("/v1.0/warehouse")

public class WareHouseService extends BasedService {

    public WareHouseService(@Context HttpHeaders headers) {
        super(headers);
    }
    
    /** 
	  * This function applies GENDOC system to call when it wants to create QR CODE => no need to authorize for this method
	  *  
	  * @param input must have content.
	  * @return image file
	  * @author dongtd
	  */
    @POST
    @Path("/qr-code")
    @Consumes("application/json")
    @Produces("image/png")
    public Response getQR(QRCodeDTO input) throws Exception {
		return this.anonymous(() -> {
	        try (WareHouseManager manager = new WareHouseManager(null)) {
	            return file(manager.createQRCode(input), UUID.randomUUID().toString(), true);
	        }
		});
    }
    
    @PUT
    @Path("/qr-code")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertQRCode(QRCodeCheckDTO qrCode) throws Exception {

        return this.authorize(ServiceName.PUT_V1_0_WareHouse_qrcode, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.updateQRCode(qrCode));
            }
        });
    }
    
    @PUT
    @Path("/contract-number/scan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response scanContractNumber(ContractNumberScannerDTO input) throws Exception {

        return this.authorize(ServiceName.PUT_V1_0_WareHouse_qrcode, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.scanContractNumber(input));
            }
        });
    }

    @PUT
    @Path("/documents")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response upsertCustProspect(DocumentsDTO payloadSaveDocuments) throws Exception {

        return this.authorize(ServiceName.PUT_V1_0_WareHouse_saveDocument, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.upsertDocument(payloadSaveDocuments));
            }
        });
    }
    
    
    
	@DELETE
	@Path("/delete-documents")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteDocument(@QueryParam("whIds") String whIds) throws Exception {

		return this.authorize(ServiceName.DELETE_V1_0_WareHouse_deleteDocument, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.deleteDocument(whIds));
			}
		});
	}

    @PUT
    @Path("/documents/changes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeDocuments(DocumentsErrorsDTO documentsErrors) throws Exception {

        return this.authorize(ServiceName.PUT_V1_0_WareHouse_updateDocumentsErrors, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.updateDocumentErrors(documentsErrors));
            }
        });
    }

    @PUT
    @Path("/documents/cavet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCavet(CavetDTO cavet) throws Exception {
        return this.authorize(ServiceName.PUT_V1_0_WareHouse_updateCavetErrors, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.updateCavet(cavet));
            }
        });      
    }

    @GET
    @Path("/seach-case/{screen}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response seachCase(@PathParam("screen") String screen, @QueryParam("docTypeId") Integer docTypeId,
			@QueryParam("identityNum") String identityNum, @QueryParam("contractNum") String contractNum,
			@QueryParam("caseNum") String appNum, @QueryParam("frameNum") String frameNum,
			@QueryParam("serialNum") String serialNum, @QueryParam("numPlate") String numPlate,
			@QueryParam("assigneeId") Integer assigneeId, @QueryParam("assignType") Integer assignType,
			@QueryParam("statusProcess") Integer statusProcess, @QueryParam("storageStatus") String storageStatus,
			@QueryParam("receiveDateFrom") String receiveDateFrom, @QueryParam("receiveDateTo") String receiveDateTo,
			@QueryParam("assignDateFrom") String assignDateFrom, @QueryParam("assignDateTo") String assignDateTo,
			@QueryParam("processDateFrom") String processDateFrom, @QueryParam("processDateTo") String processDateTo,
			@QueryParam("lodgeDateFrom") String lodgeDateFrom, @QueryParam("statuAllocate") Integer statuAllocate,
			@QueryParam("lodgeDateTo") String lodgeDateTo, @QueryParam("borrowDateFrom") String borrowDateFrom,
			@QueryParam("borrowDateTo") String borrowDateTo,
			@QueryParam("borrowApproveDateFrom") String borrowApproveDateFrom,
			@QueryParam("toApproveDateTo") String toApproveDateTo,
			@QueryParam("borrowApproveDateTo") String borrowApproveDateTo,
			@QueryParam("toApproveDateFrom") String toApproveDateFrom,
			@QueryParam("productGroupId") String productGroupId, @QueryParam("pageSize") Integer pageSize,
			@QueryParam("pageNum") Integer pageNum, @QueryParam("workFlow") String workFlow,
			@QueryParam("whdId") Long whdId, @QueryParam("statusBorrow") String statusBorrow,
			@QueryParam("importer") String importer, @QueryParam("statusAppPayBack") String statusAppPayBack,
			@QueryParam("appPayBackDateFrom") String appPayBackDateFrom,
			@QueryParam("appPayBackDateTo") String appPayBackDateTo ,
			@QueryParam("appDateFrom") String appDateFrom ,
			@QueryParam("appDateTo") String appDateTo) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_WareHouse_seachCaseInput, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				SearchCaseInput input = new SearchCaseInput(docTypeId, identityNum, contractNum, appNum, frameNum,
						serialNum, numPlate, receiveDateFrom, receiveDateTo, assignDateFrom, assignDateTo, assigneeId,
						assignType, statusProcess, storageStatus, processDateFrom, processDateTo, statuAllocate,
						lodgeDateFrom, lodgeDateTo, borrowDateFrom, borrowDateTo, borrowApproveDateFrom,
						borrowApproveDateTo, toApproveDateFrom, toApproveDateTo, productGroupId, workFlow, whdId,
						statusBorrow, importer, statusAppPayBack, appPayBackDateFrom, appPayBackDateTo , appDateFrom, appDateTo);
				return ok(manager.seachCase(screen, input, this.currentUser, pageSize, pageNum));

			}
		});
	}

    @PUT
    @Path("/allocation/{allocation}")
    public Response allocationDocument(@PathParam("allocation") String allocation,
            WHAllocationDocInput allocationDocumnetInputs) throws Exception {
        return this.authorize(ServiceName.POST_V1_0_WareHouse_allocationDocument, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.allocationDocument(allocation, allocationDocumnetInputs, this.currentUser));
            }
        });
    }

    @GET
    @Path("/export-paper-receipt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/msword")
    public Response exportPaperReceipt(@QueryParam("contractNumber") String contractNumber) throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_exportPaperReceipt, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {             
                return file(manager.exportPaperReceipt(contractNumber, this.currentUser.getLoginId()),"GiayBienNhan.doc", true);
            }
        });
    }

    @GET
    @Path("/export-thank-letter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/msword")
    public Response exportThankLetter(@QueryParam("contractNumber") String contractNumber,@QueryParam("formType") int typeExport) throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_exportThankLetter, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return file(manager.exportThankLetter(contractNumber, typeExport,this.currentUser.getLoginId()),"ThuCamOn.doc", true);
            }
        });
    }

    @PUT
    @Path("/export-handover")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/zip")
    public Response exportHandover(List<WareHouseExportHandoverDTO> lstHandoverDTO) throws Exception {
        return this.authorize(ServiceName.PUT_V1_0_WareHouse_exportHandover,() -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return file(manager.exportHandover(lstHandoverDTO),"BB_ban_giao_vh2.zip", true);
            }
        });
    }

	@POST
	@Path("/renewal/{documentType}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response renewalDocument(@PathParam("documentType") String documentType,
			List<RenewalDocumentDTO> borrowDocumentInputs) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_WareHouse_renewalDocument, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.renewalDocument(borrowDocumentInputs, documentType, this.currentUser.getLoginId()));
			}
		});

	}

    @GET
    @Path("/document-allocation/remain-document")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRemainDocumentAllocation() throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_remainDocument, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.findRemainDocumentAllocation());
            }
        });
    }

    @PUT
    @Path("/return/{step}")
    public Response returnCase(@PathParam("step") String step, List<Long> caseReturn) throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_returnCase, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.findRemainDocument(step, caseReturn, this.currentUser));
            }
        });
    }

    @POST
    @Path("/lodge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response lodgeDocument(LodgeDocumentDTO lodgeDocument) throws Exception {
        return this.authorize(ServiceName.POST_V1_0_WareHouse_lodgeDocument, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.lodgeDocument(lodgeDocument, this.currentUser.getLoginId()));
            }
        });
    }

    @GET
    @Path("/document-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listDocumentType(@QueryParam("codeGroup") String codeGroup, @QueryParam("category") String category)
            throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_listDocumentType, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.listDocumentType(codeGroup, category));
            }
        });
    }

    @GET
    @Path("/matrix")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response matrixWareHouse(@QueryParam("contractStatus") String contractStatus,
            @QueryParam("cavetStatus") String cavetStatus, @QueryParam("cavetErr") String cavetErr, @QueryParam("letterStatus") String letterStatus,
            @QueryParam("docType") int docType) throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_matrix, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.validateInput(contractStatus, cavetStatus, cavetErr, letterStatus, docType));
            }
        });
    }

    // man hinh cho muon ho so -> gui phe duyet
    @POST
    @Path("/add-borrowed-cavet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBorrowedCavet(List<WhBorrowedDocumentDTO> objRequest) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_WareHouse_addBorrowedCavet, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.addBorrowedCAVET(objRequest, this.currentUser));
			}
		});
	}

    // man hinh cho muon ho so -> gui phe duyet
    @POST
    @Path("/add-borrowed-hskv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBorrowedHSKV(List<WhBorrowedDocumentDTO> objRequest) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_WareHouse_addBorrowedHSKV, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.addBorrowedHSKV(objRequest, this.currentUser));
			}
		});
	}

    @GET
    @Path("/seach-pay-back-cavet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response seachPayBackCavet(@QueryParam("contractNumber") String contractNumber,
            @QueryParam("statusAppPayBack") String statusAppPayBack,
            @QueryParam("appPayBackDateFrom") String appPayBackDateFrom,
            @QueryParam("appPayBackDateTo") String appPayBackDateTo, 
            @QueryParam("chassis") String chassis,
            @QueryParam("engine") String engine,
            @QueryParam("caseNum") Integer caseNum,
            @QueryParam("statusWhCode") String statusWhCode, @QueryParam("flow") String flow,
            @QueryParam("fwAppPayBackDateFrom") String fwAppPayBackDateFrom,
            @QueryParam("fwAppPayBackDateTo") String fwAppPayBackDateTo, @QueryParam("typeScreen") int typeScreen)
            throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_lisPayBackCavet, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                WareHousePayBackCavetDTO input = new WareHousePayBackCavetDTO(contractNumber, statusAppPayBack,
                        appPayBackDateTo, appPayBackDateFrom, chassis, engine, caseNum, statusWhCode, flow, fwAppPayBackDateTo, fwAppPayBackDateFrom, typeScreen);
                return ok(manager.searchResulPayBackCavet(input));
            }
        });
    }

    @GET
    @Path("/seach-pay-back-letter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response seachPayBackLetter(
    		@QueryParam("contractNumber") String contractNumber,
            @QueryParam("identityNumber") String identityNumber,
            @QueryParam("caseNum") Integer caseNum)
            throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_lisPayBackLetter, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                WareHousePayBackLetter input = new WareHousePayBackLetter(contractNumber, identityNumber, caseNum);
                return ok(manager.searchResulPayBackLetter(input));
            }
        });
    }

    // Button xuat tra cavet
    @PUT
    @Path("/give-back-cavet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response giveBackCavet(List<Long> listWhDocumentId) throws Exception {
         return this.authorize(ServiceName.PUT_V1_0_WareHouse_giveBackCavet,
         () -> {
        try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
            return ok(manager.giveBackCavet(listWhDocumentId, this.currentUser.getLoginId()));
        }
         });
    }

    @PUT
    @Path("/change-document-type-err")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeDocumentTypErr(ChangeDocumentDTO changeDocument) throws Exception {
        return this.authorize(ServiceName.PUT_V1_0_WareHouse_change_document_type_err, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.changeDocumentTypErr(changeDocument, this.currentUser));
            }
        });
    }

    @GET
    @Path("/check-records-cavet")
    @Consumes(MediaType.APPLICATION_JSON)

    @Produces(MediaType.APPLICATION_JSON)
    public Response getCheckRecordsCavet(@QueryParam("whId") Long whId) throws Exception {
        return this.authorize(ServiceName.GET_V1_0_WareHouse_checkRecordsCavet, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.getCheckRecordsCavet(whId));
            }
        });
    }

    @PUT
    @Path("/input-document")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inputDocument(ChangeDocumentDTO changeDocument) throws Exception {
        return this.authorize(ServiceName.PUT_V1_0_WareHouse_input_document, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.inputDocument(changeDocument, this.currentUser));
            }
        });
    }
    
    @PUT
    @Path("/change-doc-lodge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeDocLodge(ChangeDocumentDTO changeDocument, @QueryParam("loginId") String loginId) throws Exception {
        // return this.authorize(ServiceName.PUT_V1_0_WareHouse_change_doc_lodge, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.changeDocLodge(changeDocument, loginId));
            }
        // });
    }
    
    @PUT
    @Path("/change-doc-lodge1")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeDocLodge1(ChangeDocumentDTO changeDocument, @QueryParam("loginId") String loginId) throws Exception {
        // return this.authorize(ServiceName.PUT_V1_0_WareHouse_change_doc_lodge, () -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return ok(manager.changeDocLodge1(changeDocument, loginId));
            }
        // });
    }

    @PUT
    @Path("/renewal-borrowed-document")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response renewalBorrowedDocument(List<WhBorrowedDocumentDTO> objRequest) throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_WareHouse_renewalBorrowedDocument, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.renewalBorrowedDocument(objRequest, this.currentUser));
			}
		});
	}

    // Button Tu Choi, Phe Duyet xuat tra/ cho muon cavet
    @PUT
    @Path("/approve-give-back-cavet/{status}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response approveGiveBackCavet(@PathParam("status") String status, List<ApproveBorrowCavetDTO> approveBorrowCavetDtoList)
			throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_WareHouse_approveGiveBackCavet, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.approveGiveBackCavet(approveBorrowCavetDtoList, status, this.currentUser.getLoginId()));
			}
		});
	}

    // Gui phe duyet xuat tra cavet
    @PUT
    @Path("/send-give-back-cavet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendGiveBackCavet(List<Long> listWhDocumentId) throws Exception {
		return this.authorize(ServiceName.PUT_V1_0_WareHouse_sendGiveBackCavet, () -> {
			try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
				return ok(manager.approveReturnCavet(listWhDocumentId));
			}
		});
	}
    
    @PUT
    @Path("/export-handover-borrow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/zip")
    public Response exportHandoverBorrow(List<WareHouseExportHandoverBorrowedDTO> lstHandoverBorrowDTO) throws Exception {
       // return this.authorize(ServiceName.PUT_V1_0_WareHouse_exportHandoverBorrow,() -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return file(manager.exportHandoverBorrow(lstHandoverBorrowDTO),"BB_ban_giao.zip", true);
            }
     //   });
    }
    
    @PUT
    @Path("/export-history")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public Response exportHistory(List<WareHouseExportHistoryDTO> lstHistory) throws Exception {
//        return this.authorize(ServiceName.PUT_V1_0_WareHouse_exportHistory,() -> {
            try (WareHouseManager manager = new WareHouseManager(this.currentUser)) {
                return file(manager.exportWareHouseHistory(lstHistory),"lich_su_giay_to.xlsx", true);
            }
//        });
    }

}
