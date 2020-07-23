package com.mcredit.alfresco.factory;

import com.mcredit.alfresco.aggregate.DocumentAggregate;
import com.mcredit.alfresco.validate.DocumentValidation;
import com.mcredit.data.UnitOfWork;

public class DocumentFactory {

	UnitOfWork uok = null;

	public static DocumentValidation validation(UnitOfWork uok) {
		return new DocumentValidation(uok);
	}

	public static DocumentAggregate getDocumentAggregateInstance() {
		return new DocumentAggregate();
	}

}
