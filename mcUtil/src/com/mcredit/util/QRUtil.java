package com.mcredit.util;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;
import java.util.UUID;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class QRUtil {
	
	public static String createImage(String input,int width, int height,String rootPath) throws Exception{

	    if(rootPath.endsWith("/"))
	    	rootPath = rootPath.substring(0,rootPath.length() - 1);
		
	    String directoryPath = rootPath + "/QR_Files/";
	    File directory = new File(directoryPath);
	    if (! directory.exists())
	        directory.mkdir();
	    
	    String outputFile = directoryPath + UUID.randomUUID().toString() + ".png";
	    
	    Charset charset = Charset.forName("UTF-8");
	    CharsetEncoder encoder = charset.newEncoder();
	    byte[] b = null;
	    
	    // Convert a string to UTF-8 bytes in a ByteBuffer
        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(input));
        b = bbuf.array();
        
        String tempdata = new String(b, "UTF-8");
        // get a byte matrix for the data
       
        Writer writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(3);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        
        // get a byte matrix for the data
        BitMatrix matrix = writer.encode(tempdata,
        com.google.zxing.BarcodeFormat.QR_CODE, width, height, hints);
        
        File file = new File(outputFile);
        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
        
	    return outputFile;
	}

	public static void main(String[] args) throws Exception {
//		QRCodeDTO item = new QRCodeDTO();
//		item.setData("Về lý thuyết, đội tuyển Việt Nam chỉ cần hoà, thậm chí thua với tỷ số tối thiểu 0-1 vẫn có thể giành quyền vào chung kết. Tuy nhiên, với tương quan thực lực của đôi bên, và tâm lý hiện tại của 2 đội, đội bóng của HLV Park Hang Seo hoàn toàn có thể giành chiến thắng.");;
//		item.setHeight(100);
//		item.setWidth(100);
//		new QRService().createImage(item);
	}
}
