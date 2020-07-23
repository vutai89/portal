package com.mcredit.business.ocr.callout;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.poi.hssf.record.formula.functions.Time;

import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.mobile.dto.BasicAuthenBPMDTO;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class EsbApi {
	private ParametersCacheManager _cParam = CacheManager.Parameters();

	private String _esbHost;

	public EsbApi() {
		this._esbHost = _cParam.findParamValueAsString(ParametersName.ESB_HOST);
	}

	public ApiResult getAccessToken() throws ISRestCoreException {
		BasicAuthenBPMDTO basicAuthenBPMDTO = new BasicAuthenBPMDTO();
		basicAuthenBPMDTO.setGrant_type("password");
		basicAuthenBPMDTO.setScope("*");
		basicAuthenBPMDTO.setClient_id(_cParam.findParamValueAsString(ParametersName.QR_CODE_CLIENT_ID));
		basicAuthenBPMDTO.setClient_secret(_cParam.findParamValueAsString(ParametersName.QR_CODE_CLIENT_SECRET));
		basicAuthenBPMDTO.setUsername(_cParam.findParamValueAsString(ParametersName.QR_CODE_USER_NAME));
		basicAuthenBPMDTO.setPassword(_cParam.findParamValueAsString(ParametersName.QR_CODE_PASS_WORD));

		try (BasedHttpClient bs = new BasedHttpClient()) {
			return bs.doPost(this._esbHost + BusinessConstant.QR_CODE_GET_ACCESS_TOKEN_BPM,
					JSONConverter.toJSON(basicAuthenBPMDTO).toString(), ContentType.Json);
		}
	}

	public String downloadImageFile(String imageLink, String bearerToken) throws IOException, ValidationException {
		URL url = new URL(imageLink);
		URLConnection connection = null;
		int responseCode = 200;
		if (imageLink.startsWith("https")) {
			connection = (HttpsURLConnection) url.openConnection();
			((HttpsURLConnection) connection).setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
			connection.setDoOutput(true);
			responseCode = ((HttpsURLConnection) connection).getResponseCode();

		} else if (imageLink.startsWith("http")) {
			connection = (HttpURLConnection) url.openConnection();
			((HttpURLConnection) connection).setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
			connection.setDoOutput(true);
			responseCode = ((HttpURLConnection) connection).getResponseCode();
		}

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = connection.getHeaderField("Content-Disposition");

			if (null != disposition) {
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = imageLink.substring(imageLink.lastIndexOf("/") + 1, imageLink.length());
			}

			InputStream inputStream = connection.getInputStream();

			// String outPath =
			String outPath = CacheManager.Parameters().findParamValueAsString(ParametersName.FILE_QR_CODE);
			// String outPath = "E:\\OCR_TEST\\";
			String endFile = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
			outPath += UUID.randomUUID().toString() + File.separator;
			File file = new File(outPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".JPEG")) {
				String name = outPath + "output.png";
				BufferedImage image = ImageIO.read(inputStream);

				ImageIO.write(image, "png", new File(name));
			} else if (fileName.endsWith(".pdf") || fileName.endsWith(".PDF")) {
				String name = outPath + "output." + endFile;
				byte[] fileAsBytes = getArrayFromInputStream(inputStream);
				writeContent(fileAsBytes, name);
				pdf2Png(name, outPath);
			} else {
				throw new ValidationException("Invalid Image Encryption...");
			}
			inputStream.close();
			if (connection instanceof HttpURLConnection) {
				((HttpURLConnection) connection).disconnect();
			} else {
				((HttpsURLConnection) connection).disconnect();
			}
			return outPath;
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}

		return null;
	}

	private static void writeContent(byte[] content, String fileToWriteTo) throws IOException {
		File file = new File(fileToWriteTo);
		try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
			out.write(content);
			out.flush();
		}
	}

	private static void pdf2Png(String sourceDir, String outPath) {
		Long start = System.currentTimeMillis();
		System.out.println("===ConvertPdfToPng===" + start);
		File f = null;
		try {
			f = new File(sourceDir);
			int dpi = 400;
			PDDocument document = PDDocument.load(new File(sourceDir));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			int numOfPage = document.getNumberOfPages();
			BufferedImage[] buffImages = new BufferedImage[2];
//			}
			for(int i = 0; i < numOfPage; i++) {
                if(i>=2) {
                	break;
                }
				File outPutFile = new File(outPath + "output" + i + ".png");
				BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
				if(i==1) {
	                ImageIO.write(bImage.getSubimage(0, 0, bImage.getWidth(), bImage.getHeight()-3000), "png", outPutFile);
				} else {
	                ImageIO.write(bImage.getSubimage(0, 0, bImage.getWidth(), bImage.getHeight()), "png", outPutFile);
				}
                buffImages[i] = ImageIO.read(outPutFile);

			}
			BufferedImage output = new BufferedImage(buffImages[0].getWidth(), buffImages[0].getHeight()+buffImages[1].getHeight(), BufferedImage.TYPE_INT_ARGB );
			Graphics g = output.getGraphics();
			g.drawImage(buffImages[0], 0, 0, null );
			g.drawImage(buffImages[1], 0, buffImages[0].getHeight(), null );
			g.dispose();

			ImageIO.write(output, "png", new File(outPath+"output.png"));
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != f) {
				f.delete();
			}
			System.out.println("===EndPdfToPng===" + (System.currentTimeMillis() - start));
		}
	}

	private static byte[] getArrayFromInputStream(InputStream inputStream) throws IOException {
		byte[] bytes;
		byte[] buffer = new byte[1024];
		try (BufferedInputStream is = new BufferedInputStream(inputStream)) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int length;
			while ((length = is.read(buffer)) > -1) {
				bos.write(buffer, 0, length);
			}
			bos.flush();
			bytes = bos.toByteArray();
		}
		return bytes;
	}
		
	/*
	 * public static void main(String[] args) { pdf2Png(
	 * "C:\\Users\\huyendtt.ho\\Documents\\check_scan_file\\limit\\hdtd-trí.pdf",
	 * "C:\\Users\\huyendtt.ho\\Documents\\check_scan_file\\limit\\"); }
	 */
}
