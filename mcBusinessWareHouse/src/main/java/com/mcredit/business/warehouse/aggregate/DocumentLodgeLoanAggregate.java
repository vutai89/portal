package com.mcredit.business.warehouse.aggregate;

import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.data.warehouse.entity.WhDocumentChange;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.LodgeDocumentData;
import com.mcredit.model.object.warehouse.LodgeResponseDTO;

import java.util.List;

public class DocumentLodgeLoanAggregate extends DocumentLodgeAbstractAggregate implements IDocumentLodgeAggregate {

	public DocumentLodgeLoanAggregate(UnitOfWorkWareHouse uok,LodgeDocumentDTO lodgeDocument,String userLogin) {
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
			if(newStatus == null)
				return result;
			
			int updateStatus = unitOfWorkWareHouse.whDocumentRepo().lodge(newStatus, obj.getId());
                        
                        //Kiem tra xem cavet da co ma luu kho hay chua, neu co roi se khong tao them ma luu kho nua
			if (updateStatus > 0) { // Neu update trang thai HD thanh cong
                                if (CTCodeValue1.WH_LODGE_COMPLETE_BORROW != status && CTCodeValue1.WH_LODGED_ERR_UPDATE_BORROW != status){
                                    if (CTCodeValue1.WH_WAIT_COMPLETE == status) {// Xu ly sinh ma luu kho theo trang thai HD
					
					this.createLodgeCode(StringEscapeUtils.unescapeJava("H\u0110"));
					
                                    } else if (CTCodeValue1.WH_WAIT_ERR_UPDATE == status) {

                                            this.createLodgeCode(StringEscapeUtils.unescapeJava("H\u0110L"));

                                    } 

                                    CodeTableDTO ctDocType = ctCache.getBy(CTCodeValue1.WH_LOAN_DOC, CTCat.WH_DOC_TYPE);
                                    super.docType = ctDocType != null ? ctDocType.getId().longValue() : 0;

                                    this.update(newStatus, obj.getId(), 1);
                                    unitOfWorkWareHouse.whDocumentChangeRepo().add(new WhDocumentChange(obj.getId(), changeType, null, userLogin, new Date(), newStatus));
                                }
				
			}
		} 

		return super.processResult();
	}
}
