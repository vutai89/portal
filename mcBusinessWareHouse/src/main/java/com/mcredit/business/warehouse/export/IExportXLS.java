/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcredit.business.warehouse.export;
import java.io.Closeable;
import java.util.List;
import com.mcredit.model.dto.warehouse.WareHouseExportHistoryDTO;

public interface IExportXLS extends Closeable {
	String exportWarehouseHistory (List<WareHouseExportHistoryDTO> lstHistory);
}


