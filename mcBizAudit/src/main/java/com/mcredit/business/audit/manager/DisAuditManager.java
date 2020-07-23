package com.mcredit.business.audit.manager;

import java.io.File;
import java.io.InputStream;

import com.mcredit.business.audit.aggregate.ConsolidateAggregate;
import com.mcredit.business.audit.aggregate.DisAuditAggregate;
import com.mcredit.business.audit.validation.DisAuditValidation;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.audit.AuditCommandDTO;
import com.mcredit.model.object.audit.AuditReturnResult;
import com.mcredit.model.object.audit.DetailReturnResult;
import com.mcredit.model.object.audit.GeneralReturnResult;
import com.mcredit.sharedbiz.manager.BaseManager;

public class DisAuditManager extends BaseManager {
	private DisAuditAggregate _agg;
	private ConsolidateAggregate _consoleAgg;
	private DisAuditValidation _valid = new DisAuditValidation(); 

	public DisAuditManager() throws Exception {
		_agg = new DisAuditAggregate(this.uok);
		_consoleAgg = new ConsolidateAggregate(this.uok);
	}

	/**
	 * Service lay va doi soat du lieu hang ngay
	 * 
	 * @author linhtt2.ho
	 * @param date: Ngay lay du lieu
	 *        thirdParty: Doi tac lay du lieu
	 *        override: co ghi de du lieu cu khong
	 * @return return success
	 * @throws Exception: loi khi lay file that bai hoac cau truc file khong hop le
	 */
	public ResponseSuccess importData(String date, String thirdParty, int override) throws Exception {
		return this.tryCatch(() -> {
			return _agg.importData(date, thirdParty, override);
		});
	}

	/**
	 * Service thuc hien lay thong tin bao cao duoi dang excel
	 * 
	 * @author linhtt2.ho
	 * @param fromDate: Ngay bat dau lay du lieu
	 * 		  toDate: Ngay ket thuc lay du lieu
	 *        thirdParty: Doi tac lay du lieu
	 *        reportType: Loai bao cao thu/chi
	 * @return return file bao cao excel
	 * @throws Exception
	 */
	public File getReport(String thirdParty, String fromDate, String toDate, String reportType) throws Exception {
		return this.tryCatch(() -> {
			return _agg.getReport(thirdParty, fromDate, toDate, reportType);
		});
	}

	/**
	 * Service lay thong tin bao cao: thong tin tong quan
	 * 
	 * @author linhtt2.ho
	 * @param file: File bao cao cua Viettel (neu thuc hien doi soat Viettel)
	 * 		  payload: Noi dung thong tin day len de lay bao cao
	 * @return return json chua thong tin tong quan
	 * @throws Exception
	 */
	public GeneralReturnResult auditResult(InputStream uploadedInputStream, String payload, String fileName) throws Exception {
		return this.tryCatch(() -> {
			AuditCommandDTO auditCD = _valid.validateAuditDC(payload, fileName);
			return _consoleAgg.auditResult(uploadedInputStream, auditCD , fileName);
		});
	}

	/**
	 * Service lay thong tin bao cao: danh sach ban ghi khop/khong khop chi tiet
	 * 
	 * @author linhtt2.ho
	 * @param thirdParty: ben thu ba thuc hien lay thong tin
	 * 		  fromDate: Ngay bat dau doi soat
	 * 		  toDate: Ngay ket thuc doi soat
	 * 		  reportType: loai bao cao doi soat (thu/chi)
	 * 		  result: ket qua lua chon (khop / khong khop)
	 *        workflow: Loai thanh toan
	 *        time: Thoi gian doi soat (17h/24h)
	 *        pageSize: so ban ghi tra ve 1 page
	 *        pageNum: trang bao nhieu
	 * @return return json chua cac ban ghi khop / khong khop chi tiet
	 * @throws Exception
	 */
	public DetailReturnResult getResult(String thirdParty, String fromDate, String toDate, String reportType, 
			String result, String workflow, String time, int pageSize, int pageNum) throws Exception {
		return this.tryCatch(() -> {
			_valid.validateGetResult(thirdParty, fromDate, toDate, reportType, result);
			return _consoleAgg.getResult(thirdParty, fromDate, toDate, reportType, result, workflow, time, pageSize, pageNum);
		});
	}
}
