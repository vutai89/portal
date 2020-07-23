package com.mcredit.service.allfreco;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.mcredit.model.enums.DataSizeEnum;

public class TestDownloadECM {

	public static void main(String[] args) throws IOException {
		byte[] buffer = new byte[DataSizeEnum.BYTE_SIZE.intValue()];
		String contracNumber = "99999999";
		//String url = "http://localhost:8080/mcService/service/v1.0/alfreco-service/download-zip-file?contractNumber={contractNumber}";
		String url = "http://sit-mcportal.mcredit.com.vn/mcService/service/v1.0/alfreco-service/download-zip-file?contractNumber={contractNumber}";
		 String pathParent = "E:\\TMP\\capture\\test\\" ;
		OutputStream outStream = null;
		URLConnection uCon = null;
		InputStream is = null;	
		boolean result = true ;
		
		int ByteRead;
		URL Url = new URL(url.replace("{contractNumber}", contracNumber));
		String token = "" ;
					
		uCon = Url.openConnection();					
		//uCon.setRequestProperty("Authorization","Basic " +  token);
		//uCon.setRequestProperty("Authorization","Bearer " +  token);
		
		is = uCon.getInputStream();
		outStream = new BufferedOutputStream(new FileOutputStream(pathParent + contracNumber + ".zip"));
		try {	
			while ((ByteRead = is.read(buffer)) != -1) {
				System.out.print((char) ByteRead);
				outStream.write(buffer, 0, ByteRead);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				outStream.close();
				is.close();	
				
			} catch (IOException e) {
				e.printStackTrace();
				result = false;
			}
		}
		
		System.out.println( " Test download file ECM  return :" + result );
	}
}
	
