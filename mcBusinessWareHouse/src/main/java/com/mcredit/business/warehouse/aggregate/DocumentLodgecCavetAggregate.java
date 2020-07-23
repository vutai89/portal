package com.mcredit.business.warehouse.aggregate;

import java.util.Date;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.LodgeDocumentData;
import com.mcredit.model.object.warehouse.LodgeResponseDTO;

public class DocumentLodgecCavetAggregate extends DocumentLodgeAbstractAggregate implements IDocumentLodgeAggregate {

	public DocumentLodgecCavetAggregate(UnitOfWorkWareHouse uok,LodgeDocumentDTO lodgeDocument, String userLogin) {
		super(uok, lodgeDocument, userLogin);
	}

	@Override
	public LodgeResponseDTO lodge() {
		
		Long newStatus;
		CTCodeValue1 status;
		for (LodgeDocumentData obj : lodgeDocument.getLstData()) {

			newStatus = null;
			status = CTCodeValue1.valueOf(obj.getStatus().toUpperCase());

			if (CTCodeValue1.WH_WAIT_COMPLETE == status || CTCodeValue1.WH_LODGE_COMPLETE_BORROW == status) {
				newStatus = ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE , 0);
			}

			else if (CTCodeValue1.WH_WAIT_ERR_UPDATE == status || CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW == status) {
				newStatus = ctCache.getIdBy(CTCodeValue1.WH_LODGED_ERR_UPDATE, CTCat.WH_LODGE , 0);
			}

			else if (CTCodeValue1.WH_WAIT_RETURN == status) {
				newStatus = ctCache.getIdBy(CTCodeValue1.WH_LODGED_RETURN, CTCat.WH_LODGE , 0);
			}
			
			if (newStatus == null)
				return result;

			int updateStatus = unitOfWorkWareHouse.whDocumentRepo().lodge(newStatus, obj.getId());
			
			//Kiem tra xem cavet da co ma luu kho hay chua, neu co roi se khong tao them ma luu kho nua
			Boolean isWhCode =  checkExistWhCode(obj.getId());

			if (updateStatus > 0 && isWhCode) { // Neu update trang thai HD thanh cong
				
				if (CTCodeValue1.WH_WAIT_COMPLETE == status || CTCodeValue1.WH_WAIT_ERR_UPDATE == status || CTCodeValue1.WH_WAIT_RETURN == status) {// Xu ly sinh ma luu kho theo trang thai HD
					this.createLodgeCode("CV");
				} else // Voi CAVET co cac trang thai con lai
					lodgeCode = unitOfWorkWareHouse.whDocumentRepo().findLodgeCodeByDocId(obj.getId());

				CodeTableDTO ctDocType = ctCache.getBy(CTCodeValue1.WH_CAVET, CTCat.WH_DOC_TYPE);
				super.docType = ctDocType != null ? ctDocType.getId().longValue() : 0;
				
				this.update(newStatus, obj.getId(), 2);
			}
			
			unitOfWorkWareHouse.whDocumentChangeRepo().add(new WhDocumentChange(obj.getId(), changeType, null, userLogin, new Date(), newStatus));
			
			//Begin: Reset cavet da duoc tra lai trong bang wh_borrow_document
                        Long borrowCavetTypeStatus = ctCache.getIdBy(CTCodeValue1.WH_APP_BORROW, CTCat.WH_CHAN_TYPE);
			WhBorrowedDocument whBorrowedDocument = unitOfWorkWareHouse.wareHouseBorrowDocumentRepository().getLastestBorrowDocument(obj.getId(), borrowCavetTypeStatus);
			if(whBorrowedDocument != null ){
				whBorrowedDocument.setRecordStatus("C");
				unitOfWorkWareHouse.wareHouseBorrowDocumentRepository().upsert(whBorrowedDocument);
			}
			//End
		}

		return super.processResult();
	}
	
	protected Boolean checkExistWhCode(Long whId){
		WhDocument whDocument = unitOfWorkWareHouse.whDocumentRepo().findById(whId);
		return whDocument.getWhCodeId() == null ? true: false;
	}

}
