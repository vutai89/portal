package com.mcredit.business.warehouse.aggregate;

import java.util.Date;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.LodgeDocumentData;
import com.mcredit.model.object.warehouse.LodgeResponseDTO;

public class DocumentLodgecThankLetterAggregate extends DocumentLodgeAbstractAggregate implements IDocumentLodgeAggregate {

	public DocumentLodgecThankLetterAggregate(UnitOfWorkWareHouse uok, LodgeDocumentDTO lodgeDocument, String userLogin) {
		super(uok, lodgeDocument, userLogin);
	}

	@Override
	public LodgeResponseDTO lodge() {
		
		Long newStatus;
		CTCodeValue1 status;
		for( LodgeDocumentData obj : lodgeDocument.getLstData() ) {
			
			newStatus = null;
			status = CTCodeValue1.valueOf(obj.getStatus().toUpperCase());
			if( CTCodeValue1.WH_WAIT_COMPLETE == status) {
				newStatus = ctCache.getIdBy(CTCodeValue1.WH_LODGED_COMPLETE, CTCat.WH_LODGE , 0);
			}
			
			if( newStatus != null ) {
				int updateStatus = unitOfWorkWareHouse.whDocumentRepo().lodge(newStatus, obj.getId());
				if(updateStatus > 0) { //Neu update trang thai HD thanh cong
					
					this.createLodgeCode("TCO");
					
					CodeTableDTO ctDocType = ctCache.getBy(CTCodeValue1.WH_THANKS_LETTER, CTCat.WH_DOC_TYPE);
					super.docType = ctDocType != null ? ctDocType.getId().longValue() : 0;
					
					this.update(newStatus, obj.getId(), 0);					
					unitOfWorkWareHouse.whDocumentChangeRepo().add(new WhDocumentChange(obj.getId(), changeType, null, userLogin, new Date(),newStatus));
				}
			}
		}
		
		return super.processResult();
	}
}
