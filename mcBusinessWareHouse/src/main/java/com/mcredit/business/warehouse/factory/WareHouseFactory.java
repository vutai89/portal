package com.mcredit.business.warehouse.factory;

import com.mcredit.business.warehouse.aggregate.DocumentLodgeLoanAggregate;
import com.mcredit.business.warehouse.aggregate.DocumentLodgecCavetAggregate;
import com.mcredit.business.warehouse.aggregate.DocumentLodgecThankLetterAggregate;
import com.mcredit.business.warehouse.aggregate.IDocumentLodgeAggregate;
import com.mcredit.data.warehouse.UnitOfWorkWareHouse;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;

public class WareHouseFactory {

	public static IDocumentLodgeAggregate getDocumentLodgeAgg(UnitOfWorkWareHouse uok,LodgeDocumentDTO lodgeDocument, String userLogin) {
		
		CTCodeValue1 type = CTCodeValue1.valueOf(lodgeDocument.getType().toUpperCase());
		if( CTCodeValue1.WH_LOAN_DOC == type) {
			
			return new DocumentLodgeLoanAggregate(uok,lodgeDocument,userLogin);
		}
		
		else if( CTCodeValue1.WH_CAVET == type ) {
			
			return new DocumentLodgecCavetAggregate(uok,lodgeDocument,userLogin);
		}
		
		//if( CTCodeValue1.WH_THANKS_LETTER.equals(CTCodeValue1.valueOf(lodgeDocument.getType())) ) {
		else{	
			return new DocumentLodgecThankLetterAggregate(uok,lodgeDocument,userLogin);
		}
	}
}
