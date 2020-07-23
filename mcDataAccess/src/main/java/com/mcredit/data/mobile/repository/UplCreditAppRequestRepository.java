package com.mcredit.data.mobile.repository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.dto.telesales.UploadCaseDTO;
import com.mcredit.model.enums.UplCreAppRequestStatus;
import com.mcredit.model.object.mobile.UploadDocumentDTO;
import com.mcredit.model.object.mobile.dto.InfoMessForThirdPartyDTO;
import com.mcredit.model.object.mobile.dto.InforMessForSaleDTO;
import com.mcredit.model.object.mobile.dto.SaleDTO;
import com.mcredit.util.DateUtil;

public class UplCreditAppRequestRepository extends BaseRepository implements IRepository<UplCreditAppRequest> {

	public UplCreditAppRequestRepository(Session session) {
		super(session);
	}

	public void update(UplCreditAppRequest item) {
		this.session.update(item);
	}

	public void upsert(UplCreditAppRequest item) {
		this.session.saveOrUpdate("UplCreditAppRequest", item);
	}

	public Long add(UplCreditAppRequest upl) {
		return (Long) this.session.save("UplCreditAppRequest", upl);
	}

	public int updateReturnedCase(Long mobileAppCode, String saleName) {
		return this.session.getNamedNativeQuery("updateReturnedCase").setParameter("lastUpdatedBy", saleName)
				.setParameter("status", UplCreAppRequestStatus.I.value()).setParameter("id", mobileAppCode)
				.executeUpdate();
	}

	public BigDecimal isValid(Long id) {
		return (BigDecimal) this.session.getNamedNativeQuery("checkValid").setParameter("id", id)
				.setParameter("status", UplCreAppRequestStatus.R.value()).uniqueResult();
	}

	public UplCreditAppRequest getCancelCase(Long id, String saleCode) {
		return (UplCreditAppRequest) this.session.getNamedNativeQuery("checkCancelCase").setParameter("id", id)
				.setParameter("saleCode", saleCode).setParameter("status", UplCreAppRequestStatus.R.value())
				.addEntity(UplCreditAppRequest.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<UplCreditAppRequest> getCaseForSyncFile() {
		return this.session.getNamedQuery("getCaseForSyncFile").getResultList();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<UplCreditAppRequest> getCaseForAbort() {
		List<UplCreditAppRequest> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("getCaseForAbort").list();
		if (lst != null && !lst.isEmpty()) {
			results = transformList(lst, UplCreditAppRequest.class);
		}
		return results;
	}

	public SaleDTO getSaleByAppNumber(String appNumber) {
		Object obj = this.session.getNamedNativeQuery("getSaleByAppNumber").setParameter("appNumber", appNumber)
				.uniqueResult();
		SaleDTO dto = null;
		if (obj != null)
			dto = transformObject(obj, SaleDTO.class);
		return dto;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<InforMessForSaleDTO> findInfoMessForSale(String transType, String[] msgStatus) {
		List<InforMessForSaleDTO> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findInfoMessForSale").setParameter("transType", transType)
				.setParameter("msgStatus", Arrays.asList(msgStatus)).getResultList();
		if (null != lst && !lst.isEmpty()) {
			results = transformList(lst, InforMessForSaleDTO.class);
		}
		return results;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<InfoMessForThirdPartyDTO> findInfoMessForThirdParty(String transType, String[] msgStatus) {
		List<InfoMessForThirdPartyDTO> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findInfoMessForThirdParty").setParameter("transType", transType)
				.setParameter("msgStatus", Arrays.asList(msgStatus)).getResultList();
		if (null != lst && !lst.isEmpty()) {
			results = transformList(lst, InfoMessForThirdPartyDTO.class);
		}
		return results;
	}

	public int changeStatus(String status, Long id) {
		return this.session.getNamedNativeQuery("updateStatusUplCreAppRe").setParameter("status", status)
				.setParameter("id", id).executeUpdate();
	}

	public int updateForCreateCaseBPM(String status, Long appNumber, String appId, Long id) {
		return this.session.getNamedNativeQuery("updateForCreateCaseBPM").setParameter("status", status)
				.setParameter("appNumber", appNumber).setParameter("appId", appId).setParameter("id", id)
				.executeUpdate();
	}

	public void changeLastUpdatedDate(Long id) {
		this.session.getNamedNativeQuery("updateLastDateUplCreditAppReqeust").setParameter("id", id).executeUpdate();
	}
	

	public void remove(UplCreditAppRequest item) {
		this.session.delete("UplCreditAppRequest", item);
	}

	public UplCreditAppRequest getById(Long id) {
		return this.session.find(UplCreditAppRequest.class, id);
	}

	public UplCreditAppRequest checkDuplicate(UploadDocumentDTO upload) {
		return (UplCreditAppRequest) this.session.getNamedNativeQuery("checkDuplicate")
				.setParameter("customer_name",upload.getRequest().getCustomerName())
				.setParameter("citizen_id", upload.getRequest().getCitizenId())
				.setParameter("issue_date_citizen", upload.getMobileIssueDateCitizen())
				.setParameter("product_id", upload.getRequest().getProductId())
				.setParameter("loan_tenor", upload.getRequest().getLoanTenor())
				.setParameter("loan_amount", upload.getRequest().getLoanAmount())
				.addEntity(UplCreditAppRequest.class)
				.uniqueResult();
	}
	
	public UplCreditAppRequest checkDuplicate(UploadCaseDTO newCase, Long productId) throws ParseException {
		return (UplCreditAppRequest) this.session.getNamedNativeQuery("checkDuplicate")
				.setParameter("customer_name",newCase.getCustomerName())
				.setParameter("citizen_id", newCase.getCitizenId())
				.setParameter("issue_date_citizen", DateUtil.changeFormat(newCase.getIssueDateCitizen(), "dd/MM/yyyy", "dd/MM/yyyy"))	// formart dung 2/2/2020 -> 02/02/2020
				.setParameter("product_id", productId)
				.setParameter("loan_tenor", newCase.getLoanTenor())
				.setParameter("loan_amount", newCase.getLoanAmount())
				.addEntity(UplCreditAppRequest.class)
				.uniqueResult();
	}

}
