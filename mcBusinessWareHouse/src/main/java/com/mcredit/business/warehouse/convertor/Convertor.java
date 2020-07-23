package com.mcredit.business.warehouse.convertor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  org.apache.commons.lang3.SerializationUtils ;
import org.modelmapper.ModelMapper;

import com.mcredit.data.warehouse.entity.WhBorrowedDocument;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.PagingDTO;
import com.mcredit.model.dto.warehouse.WareHouseSeachDTO;
import com.mcredit.model.dto.warehouse.WhBorrowedDocumentDTO;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.model.object.warehouse.WareHouseObject;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.util.DateUtil;

public class Convertor {
	private static ModelMapper _modelMapper = new ModelMapper();
	
	public static WareHouseSeachObject convertFrom(CodeTableDTO codeTable,String whStatusName,WareHouseSeachObject tmp, boolean whCode){
		WareHouseSeachObject item = SerializationUtils.clone(tmp); 
		item.setWhId(null);
		item.setVersion(null);
		item.setBatchId(null);
		item.setStatusWH(null);
		item.setStatusWHCode(null);
		item.setActualReceiptDate(null);	
		item.setStatusWHName(whStatusName);
		item.setDocTypeId(Long.valueOf(codeTable.getId()));
		item.setDocTypeCode(codeTable.getCodeValue1());
		item.setDocTypeName(codeTable.getDescription1());
		if(!whCode){
			item.setWhCode(null); 
			item.setWhCodeId(null);
		}
		return item;
	}
	
	public static List<WhBorrowedDocument> convertFrom(List<WhBorrowedDocumentDTO> items) throws Exception {
		List<WhBorrowedDocument> borrowedDocument = new ArrayList<>();
		for (WhBorrowedDocumentDTO item : items) {
			WhBorrowedDocument doc = new WhBorrowedDocument();
			
			Date getDate=DateUtil.toDate(item.getAppointmentDate(), DateFormatTag.DATEFORMAT_dd_MM_yyyy.value());
			Calendar calendarNow=Calendar.getInstance();
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(getDate);
			calendar.set(Calendar.HOUR, calendarNow.get(Calendar.HOUR));
			calendar.set(Calendar.MINUTE, calendarNow.get(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendarNow.get(Calendar.SECOND));
			doc.setAppointmentDate(calendar.getTime());
			doc.setWhDocId(item.getWhDocId());
			doc.setObjectTo(item.getObjectTo());
			doc.setType(item.getType() != null ? item.getType() : 0);
			doc.setDepartmentName(item.getDepartmentName());
			
			doc.setRecordStatus(RecordStatus.ACTIVE.value());
			doc.setBorrowedDate(calendar.getTime());
			doc.setReturnDate(null);
			
			borrowedDocument.add(doc);
		}
		return borrowedDocument;
	}
	
	public static Object convertFrom(List<WareHouseSeachObject> input, Integer totalCount, Integer pageSize,
			Integer pageNum, boolean paging) throws Exception {

		String dateFormat = DateFormatTag.DATEFORMAT_slash_dd_MM_yyyy_HH_mm_ss.value();

		PagingDTO out = new PagingDTO(null, pageSize, pageNum, null);
		List<Object> lsHouseSeachDTOs = new ArrayList<>();

		if (input == null || input.isEmpty()) {
			return out;
		}
		
		for (WareHouseSeachObject obj : input) {
			
			WareHouseObject dto = _modelMapper.map(obj, WareHouseObject.class);
			WareHouseSeachDTO wDto = _modelMapper.map(dto, WareHouseSeachDTO.class);
			
			Field[] fieldsIN = obj.getClass().getDeclaredFields();
			HashMap<String, String> in = new HashMap<>();
			
			for (Field field : fieldsIN) {
				if ("class java.util.Date".equals(field.getType().toString())) {					
					if(null != field.get(obj)) {
						in.put(field.getName(), DateUtil.toString((Date) field.get(obj), dateFormat));
					}
				}
			}
			
			Field[] fieldsOut = wDto.getClass().getDeclaredFields();
			for (Field fOut : fieldsOut) {
				for (Map.Entry<String, String> entry : in.entrySet()) {
					if(fOut.getName().equals(entry.getKey())){
						fOut.set(wDto, obj != null ? entry.getValue() : null);
					}
				}
			}
			
			lsHouseSeachDTOs.add(wDto);
		}

		if (!paging) {
			return lsHouseSeachDTOs;
		} else {
			out.setTotalCount(totalCount);
			out.setData(lsHouseSeachDTOs);
			return out;
		}
	}
}
