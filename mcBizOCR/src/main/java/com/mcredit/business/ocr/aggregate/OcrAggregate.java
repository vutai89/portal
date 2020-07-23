package com.mcredit.business.ocr.aggregate;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.json.JSONObject;

import com.mcredit.business.ocr.callout.EsbApi;
import com.mcredit.business.ocr.compare.Comparator;
import com.mcredit.business.ocr.document.DocFactory;
import com.mcredit.business.ocr.document.IDoc;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.warehouse.ContractCompareResult;
import com.mcredit.model.dto.warehouse.ContractInfo;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.CacheManager;

public class OcrAggregate {

	private UnitOfWork _uok = null;
	 private EsbApi _esbApi = null;

	public OcrAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		_esbApi = new EsbApi();
	}

	public ContractCompareResult compareQrCode(String shd, String ngayKyHopDong, String hoTenKhachHang, String soCMT,
			String ngayCap, String noiCap, String soTienVay, String thoiHanVay, String laiSuatVay, String phiBaoHiem,
			String tenCongTyBaoHiem, String soTaiKhoanNguoiNhan, String nganHangNhan, String daiLyChiHo,
			String tenHangHoa, String nhanHieu, String mauMa, String soKhung, String soMay, String pos, String fileType,
			String imageLink) throws Exception {
		
		// get access token in bpm
		String accessToken = getAccessToken();
		
		// decode URL
		imageLink = decodeUrl(imageLink);
		// get Image
		System.out.println("ImageLink:" + imageLink);
		
		ContractInfo info = getResult(imageLink, fileType, accessToken);
		
		return Comparator.compare(info, shd, ngayKyHopDong, hoTenKhachHang, soCMT, ngayCap, noiCap, soTienVay,
				thoiHanVay, laiSuatVay, phiBaoHiem, tenCongTyBaoHiem, soTaiKhoanNguoiNhan, nganHangNhan, daiLyChiHo,
				tenHangHoa, nhanHieu, mauMa, soKhung, soMay, pos, fileType);
	}
	
	private String decodeUrl(String imageLink) {
		byte[] decodedBytes = Base64.getDecoder().decode(imageLink);
		return new String(decodedBytes);
	}

	private String getAccessToken() throws ISRestCoreException {
		ApiResult resultApi = _esbApi.getAccessToken();
		System.out.println("API RESULT: " + resultApi.getCode());
		System.out.println("API BODY: " + resultApi.getBodyContent());
		JSONObject jsonObj = new JSONObject(resultApi.getBodyContent());
		return jsonObj.getString("access_token");
	}

	public ContractInfo getResult(String name, String fileType, String bearToken) throws Exception {
		Long start = System.currentTimeMillis();
		System.out.println("OcrAggregate.getResult==" + start);
		ContractInfo result = new ContractInfo();
		
		// create file
		String filePath = _esbApi.downloadImageFile(name, bearToken);

		// run script to extract File
		Long t1 = System.currentTimeMillis();
		runShellScript(filePath);

		// extract Info
		Long t2 = System.currentTimeMillis();
		IDoc doc = DocFactory.create(fileType);
		String fileAddr = filePath + "output.txt";
		result = doc.getInfo(fileAddr);

		// delete folder
		 deleteFolder(filePath);
		 System.out.println("End_getResult===" +(System.currentTimeMillis() - start) + "downloadImageFile==" + (t1-start) + "runShellScript==" +(t2-t1));
		return result;
	}

	private void runShellScript(String filePath) throws IOException, InterruptedException {
		String fileAddr = CacheManager.Parameters().findParamValueAsString(ParametersName.OCR_BASH_FILE_ADDR);
		if (!filePath.endsWith(File.separator)) {
			filePath += File.separator;
		}
		String[] cmd = { "bash", "-c", fileAddr + " " + filePath };
		Process p = Runtime.getRuntime().exec(cmd);
		p.waitFor();
	}

	public static void deleteFolder(String dirPath) {
		if (null == dirPath || dirPath.equals("")) {
			return;
		}
		File dir = new File(dirPath);

		// make sure directory exists
		if (!dir.exists()) {
			System.out.println("Directory: " + dirPath + " not exists");
		} else {
			try {
				delete(dir);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private static void delete(File dir) throws IOException {
		if (dir.isDirectory()) {

			// directory is empty, then delete it
			if (dir.list().length == 0) {

				dir.delete();
				System.out.println("Directory is deleted : " + dir.getAbsolutePath());

			} else {

				// list all the directory contents
				String files[] = dir.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(dir, temp);

					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (dir.list().length == 0) {
					dir.delete();
					System.out.println("Directory is deleted : " + dir.getAbsolutePath());
				}
			}

		} else {
			// if file, then delete it
			dir.delete();
//			System.out.println("File is deleted : " + dir.getAbsolutePath());
		}
	}

}