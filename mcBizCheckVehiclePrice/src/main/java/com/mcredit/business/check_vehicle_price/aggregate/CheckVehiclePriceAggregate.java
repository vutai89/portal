package com.mcredit.business.check_vehicle_price.aggregate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;

import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.check_vehicle_price.entity.MotorPrice;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.vehicle_price.MotorPriceDataDTO;
import com.mcredit.model.dto.vehicle_price.MotorPriceDataDetailDTO;
import com.mcredit.model.dto.vehicle_price.MotorPriceDroplistSearch;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CheckVehiclePriceAggregate {
	
	private UnitOfWork _uok = null;
		
	public CheckVehiclePriceAggregate (UnitOfWork _uok) {
		this._uok = _uok;
	}
	
	public MotorPriceDroplistSearch droplistSearch() {
		MotorPriceDroplistSearch result = new MotorPriceDroplistSearch();
		
		// get list brand
		result.setListBrand(_uok.checkVehicle.createMotorPriceRepository().getDroplistSearch("BRAND"));
		
		// get list motor type
		result.setListMotorType(_uok.checkVehicle.createMotorPriceRepository().getDroplistSearch("MOTOR_TYPE"));
				
		// get list motor code
		result.setListMotorCode(_uok.checkVehicle.createMotorPriceRepository().getDroplistSearch("MOTOR_CODE"));
		
		return result;
	}
	
	public MotorPriceDataDTO findMotorPrice(String brand, String motorCode, String motorType, Integer page, Integer rowPerPage) throws Exception {
		return _uok.checkVehicle.createMotorPriceRepository().findMotorPrice(brand, motorCode, motorType, page, rowPerPage);
	}
	
	public Object uploadVehiclePrice(InputStream fileContent, String userName) throws Exception {
		List<MotorPriceDataDetailDTO> motorPriceDTOList = new ArrayList<MotorPriceDataDetailDTO>();
        MotorPriceDataDetailDTO motorPriceDTO = null;
        
        Workbook workbook = new XSSFWorkbook(fileContent);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        int numberLineInExcel = 1;
        while (iterator.hasNext()) {
        	
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            if (numberLineInExcel >= 5 && cellIterator != null) {
            	motorPriceDTO = new MotorPriceDataDetailDTO();
                for (int i = 0; cellIterator.hasNext(); i ++) {
                	Cell currentCell = cellIterator.next();
//                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
//                        System.out.print(currentCell.getStringCellValue() + "--");
                        switch (i) {
	                		case 1:  
	                			currentCell.setCellType(CellType.STRING);
	                			motorPriceDTO.setBrand(currentCell.getStringCellValue().trim());
                				break;
	                		case 2:
	                			currentCell.setCellType(CellType.STRING);
	                			motorPriceDTO.setMotorType(currentCell.getStringCellValue().trim());
	                			break;
	                		case 5:
	                			currentCell.setCellType(CellType.STRING);
	                			motorPriceDTO.setSource(currentCell.getStringCellValue().trim());
	                			break;
	                		case 7:
	                			currentCell.setCellType(CellType.STRING);
	                			motorPriceDTO.setMotorCode(currentCell.getStringCellValue().trim());
	                			break;
//                        }
//                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//                        System.out.print(currentCell.getNumericCellValue() + "--");
//                        switch (i) {
	                		case 3:
	                			currentCell.setCellType(CellType.STRING);
	                			motorPriceDTO.setCapacity(currentCell.getStringCellValue().trim());
	                			break;
	                		case 4:
	                			if (currentCell.getCellTypeEnum() != CellType.NUMERIC)
	                				throw new Exception("Giá xe dòng " + numberLineInExcel + " không đúng định dạng");

	                			Double price = currentCell.getNumericCellValue();
	                			motorPriceDTO.setPrice(price.longValue());
	                			break;
	                		case 6:
	                			if (currentCell.getCellTypeEnum() != CellType.NUMERIC)
	                				throw new Exception("Ngày tra cứu dòng " + numberLineInExcel + " không đúng định dạng");
	                			
	                			Double dateValid =  currentCell.getNumericCellValue();
	                			motorPriceDTO.setValidDate(DateUtil.getJavaDate(dateValid));
	                			break;
                        }
//                    }
                }
                
                // check end row
                if (isEndRow(motorPriceDTO))
                	break;
                
                Date currentDate = new Date();
                motorPriceDTO.setCreatedDate(currentDate);
                motorPriceDTO.setLastUpdatedDate(currentDate);
                motorPriceDTO.setCreatedBy(userName);
                motorPriceDTO.setLastUpdatedBy(userName);
                if (motorPriceDTO.getMotorCode() != null) {
                	motorPriceDTOList.add(motorPriceDTO);
                }
                
            } else if (numberLineInExcel >= 5 && cellIterator == null) {
            	break;
            }
            numberLineInExcel++;
        }
        
		if (!motorPriceDTOList.isEmpty()) {
			 _uok.checkVehicle.createMotorPriceRepository().delete();
			 for (MotorPriceDataDetailDTO object : motorPriceDTOList) {
				 ModelMapper modelMapper = new ModelMapper();
				 MotorPrice _motorPriceObj = modelMapper.map(object, MotorPrice.class);
				 _uok.checkVehicle.createMotorPriceRepository().save(_motorPriceObj);
			 }				
		 } else {
			throw new ValidationException(Messages.getString("validation.not.exists", ""));
		}
		return new ResponseSuccess();
	}
	
	public boolean isEndRow(MotorPriceDataDetailDTO motorPriceDTO) {
		if (StringUtils.isNullOrEmpty(motorPriceDTO.getBrand()) && StringUtils.isNullOrEmpty(motorPriceDTO.getMotorType()) &&
				StringUtils.isNullOrEmpty(motorPriceDTO.getMotorCode()))
			return true;
		
		return false;
	}
			
}
