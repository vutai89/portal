package com.mcredit.business.telesales.background;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.mcredit.business.telesales.aggregate.UploadAggregate;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.data.common.entity.AllocationMaster;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.model.enums.TelesaleTag;
import com.mcredit.sharedbiz.BusinessLogs;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class UploadBackgroundCustom extends Thread {


	private UplDetail ud;
	private AllocationMaster amt;
	private UnitOfWork uok = null;
	private UserDTO _user;
	CodeTableCacheManager cacheManager =  CodeTableCacheManager.getInstance() ;

	public UploadBackgroundCustom(UplDetail ud, AllocationMaster amt, UserDTO user) {
		this.ud = ud;
		this.amt = amt;
		this._user = user;
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
		UploadAggregate ua = TelesalesFactory.getUploadAggregateInstance(uok.telesale);
		UploadAggregate uaComm = TelesalesFactory.getUploadAggregateInstanceCustom(uok.common);
		
		try {
			Long Start = System.currentTimeMillis();			
			uok.start();
			
			System.out.println("**************Start Excel Processing*****************");
			//HSSF
			Workbook workbook = null;
			Sheet datatypeSheet = null;
			try {
				FileInputStream excelFile = new FileInputStream(new File(ud.getServerFileName()));
				workbook = new XSSFWorkbook(excelFile);
				datatypeSheet = workbook.getSheetAt(0);
			} catch (Exception e) {
				try {
					FileInputStream excelFile = new FileInputStream(new File(ud.getServerFileName()));
					workbook = new HSSFWorkbook(excelFile);
					datatypeSheet = workbook.getSheetAt(0);
				} catch (Exception e1) {
					throw e1;
				}
				
			}
			

			Iterator<Row> iterator = datatypeSheet.iterator();
			iterator.next(); // Ignore first row of Headers

			Integer ucCount = 0;

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

				try {
					phone = getCellValueAsString(currentRow.getCell(2));
					name = getCellValueAsString(currentRow.getCell(0));
					DOB = getCellValueAsString(currentRow.getCell(1));
					CMND = getCellValueAsString(currentRow.getCell(3));
					Address = getCellValueAsString(currentRow.getCell(4));
					income = getCellValueAsString(currentRow.getCell(5));
					Note = getCellValueAsString(currentRow.getCell(6));
					
					if ( phone == null )
						continue;
					
					phone = cutter(phone.replaceAll("\\D+",""), 15);
					if( "".equals(phone) )
						continue;

					UplCustomer uc = new UplCustomer();

					uc.setUplDetailId(ud.getId());
					uc.setCustomerName(cutter(name, 100));
					uc.setBirthDate(cutter(DOB, 15));
					uc.setMobile(phone);
					uc.setIdentityNumber(cutter(CMND, 15));
					uc.setAddress(cutter(Address, 200));
					uc.setIncome(cutter(income, 15));
					uc.setNote(cutter(Note, 100));
					
					AllocationDetail ad = new AllocationDetail();
					ad.setAllocationMasterId(amt.getId());
					ad.setAllocatedNumber(1);
					ad.setObjectType("U");
					ad.setObjectId(this._user.getId());
					ad.setCreatedDate(new Date());
					ad.setLastUpdatedDate(new Date());
					ad.setCreatedBy(this._user.getLoginId());
					ad.setLastUpdatedBy(this._user.getLoginId());
					ad.setStatus(new Long(cacheManager.getCodeByCategoryCodeValue1("ALCTYPE_TL", "F").getId()));
					//ad.setStatus(new Long(cacheManager.getCodeByCategoryCodeValue1(TelesaleTag.ALLOCATIONDETAIL_ALCTYPE_SP.stringValue(), TelesaleTag.UPLDETAIL_STATUS_N.stringValue()).getId()));
					
					ua.insertUplCustomer(uc);
					System.out.println(new BusinessLogs().writeLog("--Insert UplCustomer count: " + ucCount + " request: " + new Gson().toJson(uc)));
					ad.setUplDetailId(ud.getId());
					ad.setUplCustomerId(uc.getId());
					uaComm.insertAllocationDetail(ad);
					System.out.println(new BusinessLogs().writeLog("--Insert AllocationDetail count: " + ucCount +  " request: " + new Gson().toJson(ad)));
					ucCount++;
					
				} catch (Exception e) {
					throw e;
				}
			}
			workbook.close();
			if (ucCount == 0) {
				//throw new ValidationException("No record Inserted!");
				System.out.println("No record Inserted!");
				ud.setImported(0);
				
				ud.setStatus("E");
				ud.setErrorMessage("No record Inserted!");
				ua.setUpLoadDetail(ud);
				ua.upsertUploadDetail();
				
				uok.commit();
			}else {
				ud.setImported(ucCount);
				
				/*UD.setStatus("I");
				UA.setUpLoadDetail(UD);
				UA.upsertUploadDetail();*/
				
				//uok.commit();
				//uok.reset();
				//uok.start();
				
				ud.setStatus("V");
				ua.setUpLoadDetail(ud);
				ua.upsertUploadDetail();
				
				//Fix error validation when not detect rows inserted hibernate
				uok.flush();
				
				//validate input data
				ua.checkUplCustomer(ud.getId());

				uok.commit();
				
				System.out.println("DONE! Number of Records: " + ucCount);
				Long Stop = System.currentTimeMillis();
				System.out.println("Execution Time: " + (Stop - Start) / 1000 + "s");
			}
		} catch (Exception ex) {
			uok.rollback();
			
			try {
				uok.reset();
				uok.start();
				ud.setImported(0);
				ud.setStatus("E");
				String ErrorMessage="";
				Exception cloneEx = ex;
				while (cloneEx !=null) {
					ErrorMessage+=cloneEx.getMessage() +"\n";
					cloneEx = (Exception) cloneEx.getCause();
				}
				ud.setErrorMessage(ErrorMessage);
				ua.setUpLoadDetail(ud);
				ua.upsertUploadDetail();
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
			UploadBackgroundCustom bg = new UploadBackgroundCustom(null, null, null);
			
			String phone = "";
			
			phone = bg.cutter(phone.replaceAll("\\D+",""), 15);
			
			System.out.println(phone);
		}
	}


}
