package com.mcredit.product.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.w3c.dom.Document;

import com.mcredit.business.telesales.background.FileImportUtils;
import com.mcredit.common.Messages;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.product.dto.ProductFileDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.FileHelper;

import jxl.Sheet;

public class ProductUtil {

	public ProductUtil() {
	}

	public static ProductFileDTO writeFile(File fSource, String fileName, String fileType) throws Exception {
		InputStream input = null;
		OutputStream output = null;
		ProductFileDTO fileDTO = null;
		try {
			String campaignCode = "FILE_"+getDateTimeString();
	        ///String fileExt = "." + fileType;
	        String fileExt = "." + FilenameUtils.getExtension(fileName);
	        input = new FileInputStream(fSource);
	        //dung de test file tren local
//	        FileHelper.createFolder("FileImportXsell");
//	        String serverFilename = "FileImportXsell\\" + campaignCode + fileExt;
	        String serverFilename = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY_PROD) + campaignCode + fileExt;
	        //String serverFilename = Constants.PR_DIR + campaignCode + fileExt;
	        //Constants.PR_DIR

	        File targetFile = new File(serverFilename);
	        FileHelper.copyInputStreamToFile(input, targetFile);
	        String path = targetFile.getCanonicalPath();
	        System.out.println(path);
	        File importFile = new File(path);
	        XSSFSheet xlsxSheet = null;
	        Sheet sheet = null;
	        if (fileExt.equalsIgnoreCase(".xls")) {
	            sheet = FileImportUtils.getExcelSheet(importFile, 0);
	        } else if (fileExt.equalsIgnoreCase(".xlsx")) {
	            xlsxSheet = FileImportUtils.getSheetXlsxExcel(importFile, 0);
	        }

	        //Doc file cau hinh import
	        Document doc = (Document) FileImportUtils.readFileImportConfig();
	        if (xlsxSheet == null && sheet == null)
	            throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.no.record.inserted")));
	        /*
			
			int buf_size = 1024;
			String fileDir = Constants.PR_DIR;
			//String fileDir = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_DIRECTORY);
			// Doi ten file
			String extendFileName = "_"+ getDateTimeString();   
			fileName = "FILE_UPLOAD" + extendFileName;
			String fullFileName = fileName + "." + fileType;
			File dFile = new File(fileDir + fullFileName);
			input = new FileInputStream(fSource);
			output = new FileOutputStream(dFile);
			byte[] buffer = new byte[buf_size];
			int length;
			// copy the file content in bytes
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
				if (input.available() < buf_size) {
					System.out.println("Availble byte now is : " + input.available()
							+ " so change the size of the array byte the to the same size");
					buffer = new byte[input.available()];
				}
			}*/
			fileDTO = new ProductFileDTO();
			fileDTO.setFileNameServer(serverFilename);
			fileDTO.setFileName(campaignCode + fileExt);
			fileDTO.setFileDir(serverFilename);
			System.out.println("File is copied successful!");
			return fileDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if(input!=null) input.close();
			if(output!=null) output.close();
		}
	}
	
	public static String getDateTimeString() {
		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);  
		return "_"+ cal.get(Calendar.SECOND) + cal.get(Calendar.MINUTE) + cal.get(Calendar.HOUR_OF_DAY)+"_"+cal.get(Calendar.DAY_OF_MONTH)+ cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR);
	}
	
	public static Long getIdCodeTableFromCache(CTCodeValue1 codeValue, CTCat categoryValue) {
		return CodeTableCacheManager.getInstance().getIdBy(codeValue, categoryValue);
	}
}
