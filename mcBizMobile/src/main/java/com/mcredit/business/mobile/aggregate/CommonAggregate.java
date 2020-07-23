package com.mcredit.business.mobile.aggregate;

import com.mcredit.business.mobile.utils.CaseUtils;
import com.mcredit.business.mobile.utils.FTPUtils;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.KioskDTO;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.model.object.mobile.dto.NotificationDTO;
import com.mcredit.model.object.mobile.dto.PdfDTO;
import com.mcredit.model.object.mobile.dto.SearchNotiDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.CompanyCacheManager;
import com.mcredit.sharedbiz.cache.DocumentsCachManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommonAggregate {
	private UnitOfWork unitOfWork = null;
	private CodeTableCacheManager ctCache = null;
	private UserDTO currentUser = null;
	
	public CommonAggregate(UnitOfWork unitOfWork) {
		this.unitOfWork = unitOfWork;
		ctCache = CacheManager.CodeTable();
	}
	
	
	/**
	 * Get products
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list product effective
	 * @throws Exception
	 */
	public List<ProductDTO> getProducts() {
		return ProductCacheManager.getInstance().getEffectiveProducts();
	}

	
	/**
	 * Get kiosks
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list kiosk
	 * @throws Exception
	 */
	public List<KioskDTO> getKiosks() {
		
		List<CodeTableDTO> codeTableKiosks = ctCache.getCodeByCategory(CTCat.TRAN_OFF.value());

		List<KioskDTO> kiosks = new ArrayList<>();
		for (CodeTableDTO cc : codeTableKiosks) {
			kiosks.add(new KioskDTO(cc.getId(), cc.getCodeValue1(), cc.getDescription2(), cc.getDescription1()));
		}
		
		return kiosks;
	}
	
	public CategoryDTO getCompanyInfo(String taxNumber) {
		return CompanyCacheManager.getInstance().checkCategoryById(taxNumber);
	}
	
	/**
	 * Download file pdf
	 * 
	 * @author hoanx.ho
	 * @param document id
	 * @return file pdf
	 * @throws Exception
	 */
	public File downloadFile(Long documentId) throws Exception {
		FTPClient _client = FTPUtils.createFTPConnection();
		String tempDirectory = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_TEMP_DIR);
		PdfDTO file = this.unitOfWork.mobile.creditAppDocumentRepo().getPdfFileById(documentId);
		String remotePath = CaseUtils.getRealFilePath(file);
		String fileName = CaseUtils.getFileNameByPath(remotePath);
		String tempFile = tempDirectory + fileName;
		File fileout = new File(tempFile);
		_client.download(remotePath, fileout);
		return fileout;
	}
	
	/**
	 * Get notifications
	 * 
	 * @author hoanx.ho
	 * @param 
	 * @return list notifications
	 * @throws Exception
	 */
	public List<NotificationDTO> getNotifications(SearchNotiDTO searchDTO, UserDTO currentUser) throws SQLException {
		String saleCode = currentUser.getEmpCode();
		List<NotificationDTO> notifications = this.unitOfWork.common.messageLogRepo().getNotifications(searchDTO, saleCode);
		
		notifications = this.handleAddMessage(notifications);
		
		return notifications;
		
	}
	
	/**
	 * Add message from clob to notification
	 * 
	 * @author hoanx.ho
	 * @param list notification
	 * @return list notification added message
	 * @throws Exception
	 */
	private List<NotificationDTO> handleAddMessage(List<NotificationDTO> notifications) throws SQLException {
		
		 
		String msg = "";
		Clob cl = null;
		
		for (NotificationDTO noti : notifications) {
			
			cl = noti.getClobMessage();
			if (cl != null) {
				msg = cl.getSubString(1, (int) cl.length());
				noti.setMessage(msg);
			}

			noti.setClobMessage(null);
		}
		
		return notifications;
	}
}
