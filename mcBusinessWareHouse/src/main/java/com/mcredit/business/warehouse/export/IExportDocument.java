package com.mcredit.business.warehouse.export;

import java.io.Closeable;

public interface IExportDocument extends Closeable {
	String export(String contractNumber,String loginId);
	String exportThankLetter(String contractNumber, int typeExport,String loginId);
}
