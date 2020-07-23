package com.mcredit.business.telesales.background;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mcredit.business.telesales.aggregate.UploadAggregate;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class UploadBackground extends Thread {

	private UplDetail UD;
	private UnitOfWork uok = null;

	public UploadBackground(UplDetail UD) {
		this.UD = UD;
	}

	private String getCellValueAsString(Cell cell) throws ParseException {
		if( cell == null )
			return "";
		try {
			switch (cell.getCellTypeEnum()) {
			case BOOLEAN:
				return Boolean.toString(cell.getBooleanCellValue());
			case STRING:
				return cell.getStringCellValue();
			case NUMERIC:
				if (org.apache.poi.ss.usermodel.DateUtil
						.isCellDateFormatted(cell)) {
					try {
						return DateUtil.toString(cell.getDateCellValue(),
								"yyyy/MM/dd");
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				} else {
					NumberFormat nf = DecimalFormat.getInstance();
					nf.setMaximumFractionDigits(0);
					nf.setGroupingUsed(false);
					return nf.format(cell.getNumericCellValue());
				}
			default:
				return null;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	private String cutter(String s, int size) {
		if(StringUtils.isNullOrEmpty(s)) return StringUtils.Empty;
		return s.trim().length() <= size ? s.trim() : s.trim().substring(0,
				size);
	}

	private boolean isRowEmpty(Row row) {
	    if (row == null)
	        return true;
	    
	    if (row.getLastCellNum() <= 0)
	        return true;
	    
	    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
	        Cell cell = row.getCell(cellNum);
	        if ( cell != null && cell.getCellTypeEnum() != CellType.BLANK && !"".equals(cell.toString()) )
	            return false;
	    }
	    return true;
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		
		uok = new UnitOfWork();
		UploadAggregate UA = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
		
		try {
			Long Start = System.currentTimeMillis();			
			uok.start();
			
			System.out.println("**************Start Excel Processing*****************");
			//HSSF
			Workbook workbook = null;
			Sheet datatypeSheet = null;
			try {
				FileInputStream excelFile = new FileInputStream(new File(UD.getServerFileName()));
				workbook = new XSSFWorkbook(excelFile);
				datatypeSheet = workbook.getSheetAt(0);
			} catch (Exception e) {
				try {
					FileInputStream excelFile = new FileInputStream(new File(UD.getServerFileName()));
					workbook = new HSSFWorkbook(excelFile);
					datatypeSheet = workbook.getSheetAt(0);
				} catch (Exception e1) {
					throw e1;
				}
				
			}
			

			Iterator<Row> iterator = datatypeSheet.iterator();
			iterator.next(); // Ignore first row of Headers

			Integer UCCounter = 0;

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				
				if( isRowEmpty(currentRow) )
					continue;
				
				String phone = "";
				String name = "";
				String DOB = "";
				String CMND = "";
				String Address = "";
				String income = "";
				String Note = "";

				//Iterator<Cell> cellIterator = currentRow.iterator();
				//Cell cell;
				try {
					/*BLOCK_CELL_EXTRACT: {
						
						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						name = currentRow.getCell(0)!=null ? currentRow.getCell(0).toString();

						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						DOB = currentRow.getCell(1)!=null ? currentRow.getCell(1).toString();
						
						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						phone = currentRow.getCell(2)!=null ? currentRow.getCell(2).toString();

						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						CMND = currentRow.getCell(3)!=null ? currentRow.getCell(3).toString();

						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						Address = currentRow.getCell(4)!=null ? currentRow.getCell(4).toString();

						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						income = currentRow.getCell(5)!=null ? currentRow.getCell(5).toString();

						if (!cellIterator.hasNext())
							break BLOCK_CELL_EXTRACT;
						cell = cellIterator.next();
						Note = currentRow.getCell(6)!=null ? currentRow.getCell(6).toString();*/
					
						phone = getCellValueAsString(currentRow.getCell(2));
						name = getCellValueAsString(currentRow.getCell(0));
						DOB = getCellValueAsString(currentRow.getCell(1));
						CMND = getCellValueAsString(currentRow.getCell(3));
						Address = getCellValueAsString(currentRow.getCell(4));
						income = getCellValueAsString(currentRow.getCell(5));
						Note = getCellValueAsString(currentRow.getCell(6));
					
					//}
					
					if ( phone == null )
						continue;
					
					phone = cutter(phone.replaceAll("\\D+",""), 15);
					if( "".equals(phone) )
						continue;

					UplCustomer UC = new UplCustomer();

					UC.setUplDetailId(UD.getId());
					UC.setCustomerName(cutter(name, 100));
					UC.setBirthDate(cutter(DOB, 15));
					UC.setMobile(phone);
					UC.setIdentityNumber(cutter(CMND, 15));
					UC.setAddress(cutter(Address, 200));
					UC.setIncome(cutter(income, 15));
					UC.setNote(cutter(Note, 100));

					UA.insertUplCustomer(UC);
					
					UCCounter++;
					
				} catch (Exception e) {
					throw e;
				}
			}
			workbook.close();
			if (UCCounter == 0) {
				//throw new ValidationException("No record Inserted!");
				System.out.println("No record Inserted!");
				UD.setImported(0);
				
				UD.setStatus("E");
				UD.setErrorMessage("No record Inserted!");
				UA.setUpLoadDetail(UD);
				UA.upsertUploadDetail();
				
				uok.commit();
			}else {
				UD.setImported(UCCounter);
				
				/*UD.setStatus("I");
				UA.setUpLoadDetail(UD);
				UA.upsertUploadDetail();*/
				
				//uok.commit();
				//uok.reset();
				//uok.start();
				
				UD.setStatus("V");
				UA.setUpLoadDetail(UD);
				UA.upsertUploadDetail();
				
				//Fix error validation when not detect rows inserted hibernate
				uok.flush();
				
				//validate input data
				UA.checkUplCustomer(UD.getId());

				uok.commit();
				
				System.out.println("DONE! Number of Records: " + UCCounter);
				Long Stop = System.currentTimeMillis();
				System.out.println("Execution Time: " + (Stop - Start) / 1000 + "s");
			}
		} catch (Exception ex) {
			uok.rollback();
			
			try {
				uok.reset();
				uok.start();
				UD.setImported(0);
				UD.setStatus("E");
				String ErrorMessage="";
				Exception cloneEx = ex;
				while (cloneEx !=null) {
					ErrorMessage+=cloneEx.getMessage() +"\n";
					cloneEx = (Exception) cloneEx.getCause();
				}
				UD.setErrorMessage(ErrorMessage);
				UA.setUpLoadDetail(UD);
				UA.upsertUploadDetail();
				uok.commit();
				ex.getCause().getMessage();
			} catch (Exception e) {
				uok.rollback();
				e.printStackTrace();
			}
			ex.printStackTrace();
			
		}

	}

	public static void main(String[] argv) {
		for (int i = 0; i <= 10; i++) {
			UploadBackground bg = new UploadBackground(null);
			
			String phone = "";
			
			phone = bg.cutter(phone.replaceAll("\\D+",""), 15);
			
			System.out.println(phone);
		}
	}

}
