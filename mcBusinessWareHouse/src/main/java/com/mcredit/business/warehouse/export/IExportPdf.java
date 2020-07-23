package com.mcredit.business.warehouse.export;

import com.mcredit.model.dto.warehouse.WareHouseExportHandoverBorrowedDTO;
import com.mcredit.model.dto.warehouse.WareHouseExportHandoverDTO;
import java.io.Closeable;
import java.util.List;

public interface IExportPdf extends Closeable {
	String exportPdf(List<WareHouseExportHandoverDTO> handoverDTO);
	String exportPdfBorrowed(List<WareHouseExportHandoverBorrowedDTO> handoverBrrowDTO);
}
