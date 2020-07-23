package com.mcredit.alfresco.api;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.DocumentName;

public class DownloadFile {
	final static int size = 1024;

	public void fileUrl(String fAddress , String filename , String basicToken, String filePath) {
		OutputStream outStream = null;
		URLConnection uCon = null;

		InputStream is = null;
		try {
			URL Url;
			byte[] buf = new byte[size];
			int ByteRead;
			Url = new URL(fAddress);
						
			uCon = Url.openConnection();
			
			uCon.setRequestProperty("Authorization","Basic " +  basicToken);

			is = uCon.getInputStream();
			if(filename.indexOf(DocumentName.CREDIT_CONTRACT.value()) != -1) {
				filename = filename + ".pdf"; 
				
				FileOutputStream fos1 = new FileOutputStream(filePath + Commons.SLASH.value() + filename);
				
				 while ((ByteRead = is.read(buf)) != -1) {
		              fos1.write(buf, 0, ByteRead);
		          }
		          fos1.flush();
		          fos1.close();
		          is.close();
			    
			}else{
				outStream = new BufferedOutputStream(new FileOutputStream(filePath + Commons.SLASH.value() + filename));
				
				
				while ((ByteRead = is.read(buf)) != -1) {
					System.out.print((char) ByteRead);
					outStream.write(buf, 0, ByteRead);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				// outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}