package com.mcredit.business.black_list.aggregate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.black_list.BlackListResponseDTO;
import com.mcredit.model.dto.black_list.CustMonitorDTO;

public class BlackListAggregate {
	private UnitOfWork _uok = null;
	
	public BlackListAggregate (UnitOfWork _uok) {
		this._uok = _uok;
	}
	
	@SuppressWarnings("resource")
	public Object importBlackList(InputStream fileContent, String userName) throws Exception {
		List<CustMonitorDTO> monitor = new ArrayList<CustMonitorDTO>();
		CustMonitorDTO monitorDTO = null;
		String pattern = "^([0-9]*)$";
		Workbook workbook = new XSSFWorkbook(fileContent);
		//Sheet 1
        Sheet sheet1 = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet1.iterator();
        int countS1 = 1;
        Map<String, CustMonitorDTO> mapReq = new HashMap<>();
        List<Long> cmnd = _uok.blackList.createCustMonitorRepository().findIdNumberByCode(Arrays.asList("1", "3"));
        List<Long> tax = _uok.blackList.createCustMonitorRepository().findIdNumberByCode(Arrays.asList("5"));
        List<CustMonitorDTO> listCode = _uok.blackList.createCustMonitorRepository().findIdByCodeValue();
        Map<String, String> mapCode = new HashMap<>();
        if(!CollectionUtils.isEmpty(listCode)){
        	listCode.forEach(cust -> {
            	mapCode.put(cust.getCustId(), String.valueOf(cust.getId()));
            });
        }
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            if (countS1 > 1 && cellIterator != null) {
            	monitorDTO = new CustMonitorDTO();
            	for (int i = 0; i < 2; i ++) {
            		Cell currentCell = currentRow.getCell(i);
            		switch(i) {
	            		case 0 :
	            			if(currentCell == null) throw new Exception("NationalID dòng " + countS1 + " không được để trống");
	            			String idNumber = currentCell.getStringCellValue();
	            			if(!idNumber.matches(pattern)) throw new Exception("NationalID dòng " + countS1 + " chỉ cho phép nhập kí tự số");
	            			if(idNumber.length() != 9 && idNumber.length() != 12) throw new Exception("NationalID dòng " + countS1 + " chỉ cho phép nhập 9 hoặc 12 kí tự");
	                		monitorDTO.setIdNumber(idNumber.trim());
	                		if(idNumber.length() == 9) monitorDTO.setIdType(mapCode.get("1"));
	                		if(idNumber.length() == 12) monitorDTO.setIdType(mapCode.get("3"));
	        				break;
	            		case 1:
	            			if(currentCell != null){
	            				String name = currentCell.getStringCellValue();
		            			if(StringUtils.isNotBlank(name)) monitorDTO.setCustName(name.trim());
	            			}
	            			break;
            		}
            	}
            	if(StringUtils.isNotBlank(monitorDTO.getIdNumber())){
//        			List<String> custId = _uok.blackList.createCustMonitorRepository().findCustIdByCode(monitorDTO.getIdNumber());
//        			if("!0".equals(custId.get(0))) monitorDTO.setCustId(custId.get(0));
        			if(null != mapReq.get(monitorDTO.getIdNumber())) throw new Exception("File import có dữ liệu trùng lặp. Vui lòng kiểm tra lại file");
        			if(!cmnd.contains(monitorDTO.getIdNumber())){
        				monitor.add(monitorDTO);
        				mapReq.put(monitorDTO.getIdNumber(), monitorDTO);
        			}
        		}
            	countS1++;
            } else {
            	countS1++;
            }
        }
        
        //Sheet 2
        Sheet sheet2 = workbook.getSheetAt(1);
        Iterator<Row> iteratorSheet2 = sheet2.iterator();
        int countS2 = 1;
        while (iteratorSheet2.hasNext()) {
            Row currentRow = iteratorSheet2.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            if (countS2 > 1 && cellIterator != null) {
            	monitorDTO = new CustMonitorDTO();
            	for (int i = 0; i < 2; i ++) {
            		Cell currentCell = currentRow.getCell(i);
            		switch(i) {
            		case 0 :
            			if(currentCell == null) throw new Exception("Taxcode dòng " + countS2 + " không được để trống");
            			String idNumber = currentCell.getStringCellValue();
                		monitorDTO.setIdNumber(idNumber.trim());
        				break;
            		case 1:
            			if(currentCell != null){
            				String name = currentCell.getStringCellValue();
	            			if(StringUtils.isNotBlank(name)) monitorDTO.setCustName(name.trim());
            			}
            			break;
            		}
            	}
            	if(StringUtils.isNotBlank(monitorDTO.getIdNumber())){
        			monitorDTO.setIdType(mapCode.get("5"));
        			if(null != mapReq.get(monitorDTO.getIdNumber())) throw new Exception("File import có dữ liệu trùng lặp. Vui lòng kiểm tra lại file");
        			if(!tax.contains(monitorDTO.getIdNumber())){
        				monitor.add(monitorDTO);
            			mapReq.put(monitorDTO.getIdNumber(), monitorDTO);
        			}
        		}
            	countS2++;
            } else {
            	countS2++;
            }
        }
        //Sheet 3
        Sheet sheet3 = workbook.getSheetAt(2);
        Iterator<Row> iteratorSheet3 = sheet3.iterator();
        int countS3 = 1;
        List<String> removes = new ArrayList<>();
        while (iteratorSheet3.hasNext()) {
        	Row currentRow = iteratorSheet3.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            if (countS3 > 1 && cellIterator != null) {
            	for (int i = 0; i < 1; i ++) {
            		Cell currentCell = currentRow.getCell(i);
            		switch(i) {
            			case 0 :
            				if(null != currentCell && currentCell.getCellTypeEnum() != CellType.BLANK) removes.add(currentCell.getStringCellValue().trim());
            				break;
            		}
            	}
            	countS3++;
            } else {
            	countS3++;
            }
        }
       //Insert DB
        if(!CollectionUtils.isEmpty(monitor)) {
        	for(CustMonitorDTO obj : monitor) {
        		System.out.println("List add: " + new Gson().toJson(obj));
        		_uok.blackList.createCustMonitorRepository().insert(obj, userName);
        	}
        }
        //Delete DB
        if(!CollectionUtils.isEmpty(removes)){
        	_uok.blackList.createCustMonitorRepository().update(removes, userName, "C");
        	System.out.println("List xóa: " + new Gson().toJson(removes));
        }
		
		return new ResponseSuccess();
	}
	
	public Object save(List<CustMonitorDTO> list, String userName) throws Exception {
		List<String> removes = new ArrayList<>();
		List<String> lstUpdate = new ArrayList<>();
		List<CustMonitorDTO> listCode = _uok.blackList.createCustMonitorRepository().findIdByCodeValue();
        Map<String, String> mapCode = new HashMap<>();
        Map<String, CustMonitorDTO> mapUpdate= new HashMap<>();
        if(!CollectionUtils.isEmpty(listCode)){
        	listCode.forEach(cust -> {
            	mapCode.put(cust.getCustId(), String.valueOf(cust.getId()));
            });
        }
        List<Long> cmnd = new ArrayList<>();
        List<Long> tax = new ArrayList<>();
        List<Long> cmndC = new ArrayList<>();
		for(CustMonitorDTO obj : list) {
			if("1".equals(obj.getStatus()) || "2".equals(obj.getStatus())) {
				if(CollectionUtils.isEmpty(cmndC)) cmndC = _uok.blackList.createCustMonitorRepository().findIdNumberByStatus();
			}
			if("1".equals(obj.getStatus())){
				System.out.print("insert cmnd/cccd: " + obj.getIdNumber());
				if(CollectionUtils.isEmpty(cmnd)) cmnd = _uok.blackList.createCustMonitorRepository().findIdNumberByCode(Arrays.asList("1", "3"));
				if(obj.getIdNumber().length() == 9) obj.setIdType(mapCode.get("1"));
	    		if(obj.getIdNumber().length() == 12) obj.setIdType(mapCode.get("3"));
	    		
			} else if("2".equals(obj.getStatus())) {
				if(CollectionUtils.isEmpty(tax)) tax = _uok.blackList.createCustMonitorRepository().findIdNumberByCode(Arrays.asList("5"));
				obj.setIdType(mapCode.get("5"));
			} else if("3".equals(obj.getStatus())) {
				if(!removes.contains(obj.getIdNumber())) removes.add(obj.getIdNumber());
			}
			if(cmnd.contains(obj.getIdNumber()) || tax.contains(obj.getIdNumber()) || "3".equals(obj.getStatus())) {
				System.out.println("IdNumber exits DB record_status=A not insert: " + obj.getIdNumber());
			} else if(cmndC.contains(obj.getIdNumber())) {
				System.out.println("IdNumber exits DB record_status=C is update record: " +  obj.getIdNumber());
				lstUpdate.add(obj.getIdNumber());
				mapUpdate.put(obj.getIdNumber(), obj);
			} else {
				_uok.blackList.createCustMonitorRepository().insert(obj, userName);
			}
		}
		
		//Update DB record_status = C
		if(!CollectionUtils.isEmpty(removes)) _uok.blackList.createCustMonitorRepository().update(removes, userName, "C");
		
		//Update DB record_status = A
		if(!CollectionUtils.isEmpty(lstUpdate)) {
			for(String id : lstUpdate) _uok.blackList.createCustMonitorRepository().update(id, userName, "A", mapUpdate.get(id));
		}
		
		return new ResponseSuccess();
	}
	
	public List<?> checkBlackList(String citizenId, String citizenIdOld, String militaryId, String companyTaxNumber) {
		List<BlackListResponseDTO> response = new ArrayList<>();
		if(StringUtils.isNotBlank(citizenId)) {
			List<BlackListResponseDTO> citizen = _uok.blackList.createCustMonitorRepository().findBlackList(citizenId, Arrays.asList("1", "3"));
			if(!CollectionUtils.isEmpty(citizen)) response.addAll(citizen);
		}
		
		if(StringUtils.isNotBlank(citizenIdOld)) {
			List<BlackListResponseDTO> citizenOld = _uok.blackList.createCustMonitorRepository().findBlackList(citizenIdOld, Arrays.asList("1", "3"));
			if(!CollectionUtils.isEmpty(citizenOld)) response.addAll(citizenOld);
		}
		
		if(StringUtils.isNotBlank(militaryId)) {
			List<BlackListResponseDTO> mili = _uok.blackList.createCustMonitorRepository().findBlackList(militaryId, Arrays.asList("2"));
			if(!CollectionUtils.isEmpty(mili)) response.addAll(mili);
		}
		
		if(StringUtils.isNotBlank(companyTaxNumber)) {
			List<BlackListResponseDTO> tax = _uok.blackList.createCustMonitorRepository().findBlackList(companyTaxNumber, Arrays.asList("5"));
			if(!CollectionUtils.isEmpty(tax)) response.addAll(tax);
		}
		return response;
		 
	}
	
}
