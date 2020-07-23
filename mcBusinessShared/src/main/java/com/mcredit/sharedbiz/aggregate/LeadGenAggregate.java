package com.mcredit.sharedbiz.aggregate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.telesale.UnitOfWorkTelesale;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.cic.CICUpdateQualifySttDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.model.leadgen.UploadCustomerDTO;
import com.mcredit.model.leadgen.UploadDataId;
import com.mcredit.model.object.ContractCheck;
import com.mcredit.model.object.warehouse.LeadGenCustomerInfo;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.esb.EsbApi;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class LeadGenAggregate extends AllocationAggregate {
	
	public static final int QUALIFY_STATUS_PASSED = 0;
	public static final int QUALIFY_STATUS_FAILED = 1;
	public static final int QUALIFY_REJECT_CODE_PASSED = 0;
	public static final int QUALIFY_REJECT_CODE_FAILED = 7;
	public static final String QUALIFY_REASON_PASSED = "PASSED";
	public static final String QUALIFY_REASON_FAILED = "FAILED";

	private LeadDTO partnerInput = null;
	private UploadCustomerDTO uplCustomerExists;
	private String identityNumber;
	private String mobileNumber;

	private final static Lock lock = new ReentrantLock();

	public LeadGenAggregate(UnitOfWorkTelesale uokTelesale, UnitOfWorkCustomer uokCustomer, LeadDTO input) {

		super(uokTelesale, uokCustomer);

		this.partnerInput = input;
	}

	public LeadGenAggregate(UnitOfWorkTelesale uokTelesale, String identityNumber, String mobileNumber) {

		super(uokTelesale);

		this.identityNumber = identityNumber;
		this.mobileNumber = mobileNumber;
	}

	public LeadResultDTO validateDataCheckSrv() {

		LeadResultDTO result = new LeadResultDTO();

		result.setStatus(LeadGenEnums.PASSED.value());

		// Check ton tai SDT hoac CMND trong he thong cua LeadGen
		UploadCustomerDTO uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
				.findResponseCodeUplCustomer(LeadGenEnums.LEAD_GEN.value(), this.partnerInput.getPhoneNumber());

		if (uplCustomerExists != null) {
			result.setStatus(LeadGenEnums.DEDUP.value());
			result.setReason(Messages.getString("label.leadgen.custExists"));
		} else {

			boolean isInBlackList = false;

			if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {
				Integer statusBlackList = unitOfWorkTelesale.uplCustomerRepo().checkBlackListWatchListLeadGenInput(
						this.partnerInput.getNationalId(), LeadGenEnums.BLACK_LIST.value());

				if (statusBlackList != null && statusBlackList > 0) {

					isInBlackList = true;
					result.setStatus(LeadGenEnums.REJECT.value());
					result.setReason(Messages.getString("label.leadgen.custInBlackList"));
				}
			}

			if (!isInBlackList) {
				ContractCheck item = unitOfWorkCustomer.customerPersonalInfoRepo()
						.findContractDuplicate(this.partnerInput.getNationalId(), this.partnerInput.getPhoneNumber());

				if (item == null || item.getCustId().equals(0L)) {
					// Check tiep dieu kien tongDuNo >= 100000000

					Long tongDuNo = unitOfWorkCustomer.customerPersonalInfoRepo().findTotalDebtPaymentNext(
							this.partnerInput.getNationalId(), this.partnerInput.getPhoneNumber());
					if (tongDuNo >= Commons.TONG_DU_NO.intValue())
						item.setCustId(new Long("1"));

					// Set custId co gia tri, danh dau la bi duplicate
				}

				if (item != null && !item.getCustId().equals(0L)) {
					result.setStatus(LeadGenEnums.REJECT.value());
					result.setReason(Messages.getString("label.leadgen.custExists"));
				}
			}
		}

		result.setDateTime(DateUtil.today("yyyy-MM-dd"));

		return result;
	}

	public LeadResultDTO validateDataCreateSrv() {
		LeadResultDTO result = new LeadResultDTO();

		result.setStatus(LeadGenEnums.PASSED.value());

		// Check refId
		this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
				.findResponseCodeUplCustomerByRefId(LeadGenEnums.LEAD_GEN.value(), this.partnerInput.getRefId());
		if (this.uplCustomerExists != null) {
			result.setBankLeadId(this.uplCustomerExists.getId().toString());
			result.setStatus(LeadGenEnums.DUPREF.value());
			result.setReason(Messages.getString("label.leadgen.custExists"));

			return result;
		}

		// Check phone number exits
		this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
				.findResponseCodeUplCustomer(LeadGenEnums.LEAD_GEN.value(), this.partnerInput.getPhoneNumber());

		if (this.uplCustomerExists != null) {
			result.setStatus(LeadGenEnums.DEDUP.value());
			result.setReason(Messages.getString("label.leadgen.custExists"));
		}

		boolean isInBlackList = false;

		if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {
			Integer statusBlackList = unitOfWorkTelesale.uplCustomerRepo().checkBlackListWatchListLeadGenInput(
					this.partnerInput.getNationalId(), LeadGenEnums.BLACK_LIST.value());

			if (statusBlackList != null && statusBlackList > 0) {
				isInBlackList = true;
				result.setStatus(LeadGenEnums.REJECT.value());
				result.setReason(Messages.getString("label.leadgen.custInBlackList"));
			}
		}

		if (!isInBlackList) {
			ContractCheck item = unitOfWorkCustomer.customerPersonalInfoRepo()
					.findContractDuplicate(this.partnerInput.getNationalId(), this.partnerInput.getPhoneNumber());

			if (item == null || item.getCustId().equals(0L)) {
				// Check tiep dieu kien tongDuNo >= 100000000
				Long tongDuNo = unitOfWorkCustomer.customerPersonalInfoRepo().findTotalDebtPaymentNext(
						this.partnerInput.getNationalId(), this.partnerInput.getPhoneNumber());
				if (tongDuNo >= Commons.TONG_DU_NO.intValue())
					item.setCustId(new Long("1"));
				// Set custId co gia tri, danh dau la bi duplicate
			}

			if (item != null && !item.getCustId().equals(0L)) {
				result.setStatus(LeadGenEnums.REJECT.value());
				result.setReason(Messages.getString("label.leadgen.custExists"));
			}
		}

		return result;
	}

	public LeadResultDTO checkLead() {
		return validateDataCheckSrv();
	}

	public LeadResultDTO createLead() {
		LeadResultDTO result = validateDataCreateSrv();

		if (result.getStatus().equals(LeadGenEnums.PASSED.value())) {
			// voi lead tu TS tao data trang thai pending -> can verify lai data (hien tai la check ket qua cic)
			if (LeadGenEnums.PARTNER_LEADGEN.value().equals(this.partnerInput.getPartner()) || 
					LeadGenEnums.PARTNER_TSNTB.value().equals(this.partnerInput.getPartner()) ||
					LeadGenEnums.PARTNER_CICLG.value().equals(this.partnerInput.getPartner())) {
				result = createPendingData();
			} else {
				result = createCompleteData();
			}
		} else {
			result = createErrorData(result);
		}

		result.setDateTime(DateUtil.today("yyyy-MM-dd"));

		return result;
	}

	public LeadResultDTO createCompleteData() {
		lock.lock();

		LeadResultDTO result = null;

		try {
			result = new LeadResultDTO();
			result.setStatus(LeadGenEnums.ERROR.value());

			String productCode = String.valueOf("");
			ProductDTO productDTO = new ProductDTO();
			productDTO.setId((long) 0);

			// Check for NoScore case
			if (this.partnerInput.getMaxScore() != 0) {
				productCode = this.partnerInput.getRuleResult().getMultiValue();
				productDTO = ProductCacheManager.getInstance().findProductByCode(productCode);

				if (productDTO == null || productDTO.getId() == 0) {
					result.setStatus(LeadGenEnums.REJECT.value());
					result.setReason(Messages.getString("label.leadgen.productNotFound"));
					System.out.println(
							"LeadGenAggregate.sendLead: Ko tim thay productId theo productCode[" + productCode + "] ");
					return result;
				}
			}

			String uploadCode = this.partnerInput.getPartner() + "-" + DateUtil.today("MMyyyy");
			UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

			if (uploadDataId != null) {
				// Insert/Update vao UplCustomer, Update imported+1 cho UplMaster va UplDetail

				UplCustomer uplCustomer = new UplCustomer(uploadDataId.getUploadDetailId(),
						this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
						this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
						this.partnerInput.getOther(), LeadGenEnums.OK.value(), "", this.partnerInput.getTelcoCode(),
						this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
						this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
						this.partnerInput.getMaxScore(), productDTO.getId(), new Date(), this.partnerInput.getRefId(),
						this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

				// TODO check nếu đã tồn tại SĐT và CMND thì update UplCustomer theo SĐT và CMND
				// ko thì inser mới
				// Long uplCustomerId =
				// unitOfWorkTelesale.uplCustomerRepo().isUplCustomerExists(this.partnerInput.getPhoneNumber(),
				// this.partnerInput.getNationalId());

				unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);
				result.setBankLeadId(
						(uplCustomer.getId() != null && !uplCustomer.getId().equals(0L)) ? uplCustomer.getId() + ""
								: "");

				if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {
					int resultUpdateUplMaster = unitOfWorkTelesale.uplCustomerRepo()
							.increaseImportedUplMaster(1L, uploadDataId.getUploadMasterId());

					if (resultUpdateUplMaster > 0) {

						int resultUpdateUplDetail = unitOfWorkTelesale.uplCustomerRepo()
								.increaseImportedUplDetail(1L, uploadDataId.getUploadDetailId());
						if (resultUpdateUplDetail > 0)
							result.setStatus(LeadGenEnums.SUCCESS.value());
						else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("304.Khong update dc UplDetail");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("308.Khong update dc UplMaster");
					}
					/*
					 * }else result.setStatus(LeadGenEnums.SUCCESS.value());
					 */
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("312.Khong insert dc vao UplCustomer");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo()
						.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
								this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

			} else {
				// Insert moi vao UplMaster, UplDetail, UplCustomer
				UplCustomer uplCustomer = new UplCustomer();

				Long fromSource = ctCache.getIdBy(CTCodeValue1.LEAD_GEN, CTCat.UPL_SRC, 0);
				Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

				UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
						fromSource, uploadCode, uplType, new Long("1"), new Long("0"));

				unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

				if (uplMaster.getId() != null && uplMaster.getId() > 0) {
					UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 1, 0,
							LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());

					unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

					if (uplDetail.getId() != null && uplDetail.getId() > 0) {

						uplCustomer = new UplCustomer(uplDetail.getId(), this.partnerInput.getFullName(), "",
								this.partnerInput.getPhoneNumber(), this.partnerInput.getNationalId(), "",
								this.partnerInput.getIncomeLevel(), this.partnerInput.getOther(),
								LeadGenEnums.OK.value(), "", this.partnerInput.getTelcoCode(),
								this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
								this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
								this.partnerInput.getMaxScore(), productDTO.getId(), new Date(),
								this.partnerInput.getRefId(), this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

						unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);
						result.setBankLeadId((uplCustomer.getId() != null && !uplCustomer.getId().equals(0L))
								? uplCustomer.getId() + ""
								: "");

						if (uplCustomer.getId() != null && uplCustomer.getId() > 0)
							result.setStatus(LeadGenEnums.SUCCESS.value());
						else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("395.Khong insert dc vao UplCustomer");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("399.Khong insert dc vao UplDetail");
					}
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("403.Khong insert dc vao UplMaster");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo().add(new UplCustomerHistory(uplCustomer.getId(),
						uplMaster.getId(), this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

			}
		} catch (Exception ex) {
			System.out.println("createCompleteData.ex: " + ex.toString());
		} finally {
			lock.unlock();
		}

		return result;
	}
	
	public LeadResultDTO createPendingData() {
		lock.lock();

		LeadResultDTO result = null;

		try {
			result = new LeadResultDTO();
			result.setStatus(LeadGenEnums.ERROR.value());

			String productCode = String.valueOf("");
			ProductDTO productDTO = new ProductDTO();
			productDTO.setId((long) 0);

			// Check for NoScore case
			if (this.partnerInput.getMaxScore() != 0) {
				productCode = this.partnerInput.getRuleResult().getMultiValue();
				productDTO = ProductCacheManager.getInstance().findProductByCode(productCode);

				if (productDTO == null || productDTO.getId() == 0) {
					result.setStatus(LeadGenEnums.REJECT.value());
					result.setReason(Messages.getString("label.leadgen.productNotFound"));
					System.out.println(
							"LeadGenAggregate.sendLead: Ko tim thay productId theo productCode[" + productCode + "] ");
					return result;
				}
			}

			String uploadCode = this.partnerInput.getPartner() + "-" + DateUtil.today("MMyyyy");
			UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

			if (uploadDataId != null) {
				// Insert/Update vao UplCustomer, Update imported+1 cho UplMaster va UplDetail

				UplCustomer uplCustomer = new UplCustomer(
						// uploadDataId.getUploadDetailId(),
						0L, // TODO Set uplDetailId = 0 de ko anh huong den phan bo
						this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
						this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
						this.partnerInput.getOther(), LeadGenEnums.APPROVING.value(), "", this.partnerInput.getTelcoCode(),
						this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
						this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
						this.partnerInput.getMaxScore(), productDTO.getId(), new Date(), this.partnerInput.getRefId(), this.partnerInput.getCampaignCode(), this.partnerInput.getGender());
				uplCustomer.setUdf08("" + uploadDataId.getUploadDetailId()); 		// luu upl_detail_id vao udf08 de cap nhat lai khi da duoc approve

				// TODO check nếu đã tồn tại SĐT và CMND thì update UplCustomer theo SĐT và CMND
				// ko thì inser mới
				// Long uplCustomerId =
				// unitOfWorkTelesale.uplCustomerRepo().isUplCustomerExists(this.partnerInput.getPhoneNumber(),
				// this.partnerInput.getNationalId());

				unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);
				result.setBankLeadId(
						(uplCustomer.getId() != null && !uplCustomer.getId().equals(0L)) ? uplCustomer.getId() + ""
								: "");

				if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {
					result.setStatus(LeadGenEnums.SUCCESS.value());
					
					// Se tang import khi nao approve (check cic de approve) xong
					/**
					int resultUpdateUplMaster = unitOfWorkTelesale.uplCustomerRepo()
							.increaseImportedUplMaster(1L, uploadDataId.getUploadMasterId());

					if (resultUpdateUplMaster > 0) {

						int resultUpdateUplDetail = unitOfWorkTelesale.uplCustomerRepo()
								.increaseImportedUplDetail(1L, uploadDataId.getUploadDetailId());
						if (resultUpdateUplDetail > 0)
							result.setStatus(LeadGenEnums.SUCCESS.value());
						else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("304.Khong update dc UplDetail");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("308.Khong update dc UplMaster");
					}
					**/
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("302.Khong insert dc vao UplCustomer");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo()
						.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
								this.partnerInput.getRefId(), LeadGenEnums.APPROVING.value(), "Đang chờ approve"));

			} else {
				// Insert moi vao UplMaster, UplDetail, UplCustomer
				UplCustomer uplCustomer = new UplCustomer();

				Long fromSource = ctCache.getIdBy(CTCodeValue1.LEAD_GEN, CTCat.UPL_SRC, 0);
				Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

				UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
						fromSource, uploadCode, uplType, new Long("0"), new Long("0"));

				unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

				if (uplMaster.getId() != null && uplMaster.getId() > 0) {
					UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 0, 0,
							LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());

					unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

					if (uplDetail.getId() != null && uplDetail.getId() > 0) {

						uplCustomer = new UplCustomer(
								// uplDetail.getId(), 
								0L, // TODO Set uplDetailId = 0 de ko anh huong den phan bo
								this.partnerInput.getFullName(), this.partnerInput.getDob(),
								this.partnerInput.getPhoneNumber(), this.partnerInput.getNationalId(), this.partnerInput.getAddress(),
								this.partnerInput.getIncomeLevel(), this.partnerInput.getOther(),
								LeadGenEnums.APPROVING.value(), "", this.partnerInput.getTelcoCode(),
								this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
								this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
								this.partnerInput.getMaxScore(), productDTO.getId(), new Date(),
								this.partnerInput.getRefId(), this.partnerInput.getCampaignCode(), this.partnerInput.getGender());
						uplCustomer.setUdf08("" + uplDetail.getId()); 		// luu upl_detail_id vao udf08 de cap nhat lai khi da duoc approve

						unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);
						result.setBankLeadId((uplCustomer.getId() != null && !uplCustomer.getId().equals(0L))
								? uplCustomer.getId() + ""
								: "");

						if (uplCustomer.getId() != null && uplCustomer.getId() > 0)
							result.setStatus(LeadGenEnums.SUCCESS.value());
						else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("385.Khong insert dc vao UplCustomer");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("389.Khong insert dc vao UplDetail");
					}
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("393.Khong insert dc vao UplMaster");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo().add(new UplCustomerHistory(uplCustomer.getId(),
						uplMaster.getId(), this.partnerInput.getRefId(), LeadGenEnums.APPROVING.value(), "Đang chờ approve"));
			}
			
			// gui yeu cau check cic neu insert lead thanh cong
			if (LeadGenEnums.SUCCESS.value().equals(result.getStatus())) {
				// Chi gui khi nationalId khac rong. TH bang rong se tu dong
				// duoc approve khi chay job process leadgen data
				if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {
					EsbApi esb = new EsbApi();
					ApiResult apiResult = esb.createCIC(this.partnerInput.getNationalId(), StringUtils.isNullOrEmpty(this.partnerInput.getFullName())?"No Name" : this.partnerInput.getFullName(), "Leadgen");
					if (null == apiResult || !apiResult.getStatus()) {
						System.out.println("Send request check cic from Leadgen error: " + JSONConverter.toJSON(apiResult));
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("createPendingData.ex: " + ex.toString());
		} finally {
			lock.unlock();
		}

		return result;
	}

	public LeadResultDTO createErrorData(LeadResultDTO result) {

		String uploadCode = this.partnerInput.getPartner() + "-" + DateUtil.today("MMyyyy");

		UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

		if (uploadDataId != null) {
			// Insert vao UplCustomer, Update imported+1 cho UplMaster va UplDetail
			UplCustomer uplCustomer = new UplCustomer(
					// uploadDataId.getUploadDetailId()
					0L // TODO Set uplDetailId = 0 de ko anh huong den phan bo
					, this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
					this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
					this.partnerInput.getOther(), result.getStatus(), result.getReason(),
					this.partnerInput.getTelcoCode(), this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
					this.partnerInput.getProvince(), new Long("-1"), new Long("-1"), new Long("-1"), new Date(),
					this.partnerInput.getRefId(), this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

			unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

			if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {
			} else
				System.out.println("463.Khong insert dc vao UplCustomer");

			// TODO insert UplCustomerHistory
			unitOfWorkTelesale.uplCustomerHistoryRepo()
					.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
							this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

		} else {
			// Insert moi vao UplMaster, UplDetail, UplCustomer
			UplCustomer uplCustomer = new UplCustomer();

			Long fromSource = ctCache.getIdBy(CTCodeValue1.LEAD_GEN, CTCat.UPL_SRC, 0);
			Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

			UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
					fromSource, uploadCode, uplType, new Long("0"), new Long("0"));

			unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

			if (uplMaster.getId() != null && uplMaster.getId() > 0) {

				UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 0, 0,
						LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());

				unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

				if (uplDetail.getId() != null && uplDetail.getId() > 0) {
					uplCustomer = new UplCustomer(
							// uplDetail.getId(), 
							0L, // uplDetailId = 0 de ko anh huong den phan bo
							this.partnerInput.getFullName(), "",
							this.partnerInput.getPhoneNumber(), this.partnerInput.getNationalId(), "",
							this.partnerInput.getIncomeLevel(), this.partnerInput.getOther(), result.getStatus(),
							result.getReason(), this.partnerInput.getTelcoCode(), this.partnerInput.getScoreRange(),
							this.partnerInput.getSource(), this.partnerInput.getProvince(), new Long("-1"),
							new Long("-1"), new Long("-1"), new Date(), this.partnerInput.getRefId(),
							this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

					unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

					if (uplCustomer.getId() == null || uplCustomer.getId() == 0)
						System.out.println("534.Khong insert dc vao UplCustomer");
				} else
					System.out.println("536.Khong insert dc vao UplDetail");
			} else
				System.out.println("538.Khong insert dc vao UplMaster");

			// TODO insert UplCustomerHistory
			unitOfWorkTelesale.uplCustomerHistoryRepo().add(new UplCustomerHistory(uplCustomer.getId(),
					uplMaster.getId(), this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

		}

		return result;
	}

	public List<LeadGenCustomerInfo> findLead() {

		return unitOfWorkTelesale.uplCustomerRepo().findLeadGenCustomerInfo(this.identityNumber, this.mobileNumber);
	}

	public UploadCustomerDTO getUplCustomerExists() {
		return uplCustomerExists;
	}

	public void setUplCustomerExists(UploadCustomerDTO uplCustomerExists) {
		this.uplCustomerExists = uplCustomerExists;
	}

	public LeadResultDTO checkRemainingTypes() {
		// doi voi doi tac co db rieng
		return validateDataCheckRemanningTypes();
	}

	public LeadResultDTO validateDataCheckRemanningTypes() {
		LeadResultDTO result = new LeadResultDTO();

		result.setStatus(LeadGenEnums.PASSED.value());

		// Check ton tai SDT trong he thong doi tac
		this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
				.findResponseCodeUplCustomer(this.partnerInput.getPartner(), this.partnerInput.getPhoneNumber());

		if (this.uplCustomerExists != null) {
			result.setStatus(LeadGenEnums.DEDUP_1.value());
			result.setReason(Messages.getString("label.leadgen.custExistsWithin90days"));
		} else {

			// Check ton tai SDT trong he thong NTB
			if (unitOfWorkTelesale.uplCustomerRepo()
					.checkExistsDataInNTBSystem(this.partnerInput.getPhoneNumber())) {

				result.setStatus(LeadGenEnums.DEDUP_2.value());
				result.setReason(Messages.getString("label.leadgen.custExistsWithin90days"));

			} else {

				if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {

					Integer statusBlackList = unitOfWorkTelesale.uplCustomerRepo().checkBlackListWatchListLeadGenInput(
							this.partnerInput.getNationalId(), LeadGenEnums.BLACK_LIST.value());

					if (statusBlackList != null && statusBlackList > 0) {

						result.setStatus(LeadGenEnums.REJECT.value());
						result.setReason(LeadGenEnums.CUST_IN_BLACK_LIST.value());
					}
				}
			}
		}

		result.setDateTime(DateUtil.today("yyyy-MM-dd"));

		return result;
	}

	public LeadResultDTO validateDataCreateRemainingTypes() {
		LeadResultDTO result = new LeadResultDTO();

		List<String> msgLst = new ArrayList<>();

		if (StringUtils.isNullOrEmpty(this.partnerInput.getPhoneNumber()))
			msgLst.add(Messages.getString("validation.field.madatory", Labels.getString("label.leadgen.phoneNumber")));
		else if (!StringUtils.checkMobilePhoneNumberNew(this.partnerInput.getPhoneNumber()))
			msgLst.add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.leadgen.phoneNumber")));

		if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {
			if (!StringUtils.isNumberic((this.partnerInput.getNationalId())))
				msgLst.add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.leadgen.nationalId")));
			else if (!Arrays.asList(new String[] { "9", "12" })
					.contains(this.partnerInput.getNationalId().length() + "")) {

				msgLst.add(Messages.getString("label.leadgen.nationalId.inRange",
						Labels.getString("label.leadgen.nationalId")));

				if (this.partnerInput.getNationalId().length() > CustomerValidationLength.MAX_LEN_PROVINCE.value())
					this.partnerInput.setNationalId(this.partnerInput.getNationalId().substring(0,
							CustomerValidationLength.MAX_LEN_PROVINCE.value()));
			}
		}

		if (!StringUtils.isNullOrEmpty(this.partnerInput.getFullName()) && this.partnerInput.getFullName()
				.length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.fullName"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));

			this.partnerInput.setFullName(this.partnerInput.getFullName().substring(0,
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_NAME.value()));
		}

		if (!StringUtils.isNullOrEmpty(this.partnerInput.getProvince()) && this.partnerInput.getProvince()
				.length() > CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.province"),
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));

			this.partnerInput.setProvince(this.partnerInput.getProvince().substring(0,
					CustomerValidationLength.MAX_LEN_CUSTOMER_ACCOUNT_LINK_VALUE.value()));
		}

		if (!StringUtils.isNullOrEmpty(this.partnerInput.getIncomeLevel())
				&& this.partnerInput.getIncomeLevel().length() > CustomerValidationLength.MAX_LEN_PROVINCE.value()) {

			msgLst.add(Messages.getString("validation.field.length", Labels.getString("label.leadgen.incomeLevel"),
					CustomerValidationLength.MAX_LEN_PROVINCE.value()));

			this.partnerInput.setIncomeLevel(
					this.partnerInput.getIncomeLevel().substring(0, CustomerValidationLength.MAX_LEN_PROVINCE.value()));
		}

		/**/
		if (msgLst.size() > 0) {

			StringBuilder sb = new StringBuilder();
			int count = 0;
			for (Iterator<String> iterator = msgLst.iterator(); iterator.hasNext();) {
				String msg = (String) iterator.next();
				sb.append(msg);
				sb.append("\r\n");
				count++;
			}
			String msgStr = sb.toString();
			if (count == 1)
				msgStr = msgStr.replace("\r\n", "");

			result.setStatus(LeadGenEnums.ERROR.value());
			result.setReason(msgStr);
		} else {

			result.setStatus(LeadGenEnums.PASSED.value());
			
			// Check refId
			this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
					.findResponseCodeUplCustomerByRefId(this.partnerInput.getPartner(), this.partnerInput.getRefId());
			if (this.uplCustomerExists != null) {
				result.setBankLeadId(this.uplCustomerExists.getId().toString());
				result.setStatus(LeadGenEnums.DUPREF.value());
				result.setReason(Messages.getString("label.leadgen.custExists"));

				return result;
			}
			
			// Check ton tai SDT trong he thong doi tac
			this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
					.findResponseCodeUplCustomer(this.partnerInput.getPartner(), this.partnerInput.getPhoneNumber());

			if (this.uplCustomerExists != null) {
				result.setStatus(LeadGenEnums.DEDUP_1.value());
				result.setReason(Messages.getString("label.leadgen.custExistsWithin90days"));
			} else {

				// Check ton tai SDT trong he thong NTB
				if (unitOfWorkTelesale.uplCustomerRepo()
						.checkExistsDataInNTBSystem(this.partnerInput.getPhoneNumber())) {

					result.setStatus(LeadGenEnums.DEDUP_2.value());
					result.setReason(Messages.getString("label.leadgen.custExistsWithin90days"));

				} else {

					if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {

						Integer statusBlackList = unitOfWorkTelesale.uplCustomerRepo()
								.checkBlackListWatchListLeadGenInput(this.partnerInput.getNationalId(),
										LeadGenEnums.BLACK_LIST.value());

						if (statusBlackList != null && statusBlackList > 0) {

							result.setStatus(LeadGenEnums.REJECT.value());
							result.setReason(LeadGenEnums.CUST_IN_BLACK_LIST.value());
						}
					}
				}
			}
		}

		return result;
	}

	public LeadResultDTO createCompleteDataRemainingTypes() {
		lock.lock();

		LeadResultDTO result = null;

		try {

			result = new LeadResultDTO();

			result.setStatus(LeadGenEnums.ERROR.value());

			String uploadCode = this.partnerInput.getPartner() + "-" + DateUtil.today("MMyyyy");

			UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

			if (uploadDataId != null) { // Insert/Update vao UplCustomer, Update imported+1 cho UplMaster va UplDetail

				UplCustomer uplCustomer = new UplCustomer(uploadDataId.getUploadDetailId(),
						this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
						this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
						this.partnerInput.getOther(), LeadGenEnums.OK.value(), "", this.partnerInput.getTelcoCode(),
						this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
						this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
						this.partnerInput.getMaxScore(), null, new Date(), this.partnerInput.getRefId(),
						this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

				unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

				result.setBankLeadId(
						(uplCustomer.getId() != null && !uplCustomer.getId().equals(0L)) ? uplCustomer.getId() + ""
								: "");
				/**/

				if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {

					int resultUpdateUplMaster = unitOfWorkTelesale.uplCustomerRepo()
							.increaseImportedUplMaster(1L, uploadDataId.getUploadMasterId());

					if (resultUpdateUplMaster > 0) {

						int resultUpdateUplDetail = unitOfWorkTelesale.uplCustomerRepo()
								.increaseImportedUplDetail(1L, uploadDataId.getUploadDetailId());
						if (resultUpdateUplDetail > 0) {
							result.setStatus(LeadGenEnums.SUCCESS.value());
							
							// Kiem tra neu trang thai phan bo hien tai la A (Allocated All) thi chuyen thanh P (Partial Allocated) de co the phan bo tiep
							UplDetail uplDetail = unitOfWorkTelesale.uplDetailRepo().getUplDetailbyId(uploadDataId.getUploadDetailId());
							if (uplDetail != null && "A".equalsIgnoreCase(uplDetail.getStatus())) 
								unitOfWorkTelesale.uplCustomerRepo().changeUplDetailStatusToPartialAllocated(uplDetail.getId());
						} else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("304.Khong update dc UplDetail");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("308.Khong update dc UplMaster");
					}
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("312.Khong insert dc vao UplCustomer");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo()
						.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
								this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

			} else { // Insert moi vao UplMaster, UplDetail, UplCustomer

				UplCustomer uplCustomer = new UplCustomer();

//				CTCodeValue1 codeValue1;
//				if (LeadGenEnums.PARTNER_TRUST_CONNECT.equals(this.partnerInput.getPartner().toUpperCase())) {
//					codeValue1 = CTCodeValue1.TRUST_CONN;
//				} else if (LeadGenEnums.PARTNER_AMO.equals(this.partnerInput.getPartner().toUpperCase())) {
//					codeValue1 = CTCodeValue1.AMO;
//				} else {
//					// dedault la thuoc Leadgen
//					codeValue1 = CTCodeValue1.LEAD_GEN;
//				}

				Long fromSource = ctCache.getIdBy(CTCodeValue1.valueOf(this.partnerInput.getPartner().toUpperCase()), CTCat.UPL_SRC, 0);
				Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

				UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
						fromSource, uploadCode, uplType, new Long("1"), new Long("0"));

				unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

				if (uplMaster.getId() != null && uplMaster.getId() > 0) {

					UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 1, 0,
							LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());

					unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

					if (uplDetail.getId() != null && uplDetail.getId() > 0) {

						uplCustomer = new UplCustomer(uploadDataId.getUploadDetailId(),
								this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
								this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
								this.partnerInput.getOther(), LeadGenEnums.OK.value(), "", this.partnerInput.getTelcoCode(),
								this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
								this.partnerInput.getProvince(), this.partnerInput.getMinScore(),
								this.partnerInput.getMaxScore(), null, new Date(), this.partnerInput.getRefId(),
								this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

						unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

						result.setBankLeadId((uplCustomer.getId() != null && !uplCustomer.getId().equals(0L))
								? uplCustomer.getId() + ""
								: "");

						if (uplCustomer.getId() != null && uplCustomer.getId() > 0)
							result.setStatus(LeadGenEnums.SUCCESS.value());
						else {
							result.setReason(Messages.getString("label.leadgen.actionError"));
							System.out.println("395.Khong insert dc vao UplCustomer");
						}
					} else {
						result.setReason(Messages.getString("label.leadgen.actionError"));
						System.out.println("399.Khong insert dc vao UplDetail");
					}
				} else {
					result.setReason(Messages.getString("label.leadgen.actionError"));
					System.out.println("403.Khong insert dc vao UplMaster");
				}

				// TODO insert UplCustomerHistory
				unitOfWorkTelesale.uplCustomerHistoryRepo().add(new UplCustomerHistory(uplCustomer.getId(),
						uplMaster.getId(), this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

			}
		} catch (Exception ex) {
			System.out.println("createCompleteData.ex: " + ex.toString());
		} finally {
			lock.unlock();
		}

		return result;
	}
	
	public LeadResultDTO createErrorDataRemainingTypes(LeadResultDTO result) {

		String uploadCode = this.partnerInput.getPartner() + "-" + DateUtil.today("MMyyyy");

		UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

		if (uploadDataId != null) {
			// Insert vao UplCustomer, Update imported+1 cho UplMaster va UplDetail
			UplCustomer uplCustomer = new UplCustomer(
					// uploadDataId.getUploadDetailId()
					0L // TODO Set uplDetailId = 0 de ko anh huong den phan bo
					, this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
					this.partnerInput.getNationalId(), this.partnerInput.getAddress(), this.partnerInput.getIncomeLevel(),
					this.partnerInput.getOther(), result.getStatus(), result.getReason(),
					this.partnerInput.getTelcoCode(), this.partnerInput.getScoreRange(), this.partnerInput.getSource(),
					this.partnerInput.getProvince(), new Long("-1"), new Long("-1"), new Long("-1"), new Date(),
					this.partnerInput.getRefId(), this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

			unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

			if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {
			} else
				System.out.println("463.Khong insert dc vao UplCustomer");

			// TODO insert UplCustomerHistory
			unitOfWorkTelesale.uplCustomerHistoryRepo()
					.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
							this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

		} else {
			// Insert moi vao UplMaster, UplDetail, UplCustomer
			UplCustomer uplCustomer = new UplCustomer();

			Long fromSource = ctCache.getIdBy(CTCodeValue1.valueOf(this.partnerInput.getPartner().toUpperCase()), CTCat.UPL_SRC, 0);
			Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

			UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
					fromSource, uploadCode, uplType, new Long("0"), new Long("0"));

			unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

			if (uplMaster.getId() != null && uplMaster.getId() > 0) {

				UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 0, 0,
						LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());

				unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

				if (uplDetail.getId() != null && uplDetail.getId() > 0) {
					uplCustomer = new UplCustomer(
							// uplDetail.getId(), 
							0L, // uplDetailId = 0 de ko anh huong den phan bo
							this.partnerInput.getFullName(), "",
							this.partnerInput.getPhoneNumber(), this.partnerInput.getNationalId(), "",
							this.partnerInput.getIncomeLevel(), this.partnerInput.getOther(), result.getStatus(),
							result.getReason(), this.partnerInput.getTelcoCode(), this.partnerInput.getScoreRange(),
							this.partnerInput.getSource(), this.partnerInput.getProvince(), new Long("-1"),
							new Long("-1"), new Long("-1"), new Date(), this.partnerInput.getRefId(),
							this.partnerInput.getCampaignCode(), this.partnerInput.getGender());

					unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

					if (uplCustomer.getId() == null || uplCustomer.getId() == 0)
						System.out.println("534.Khong insert dc vao UplCustomer");
				} else
					System.out.println("536.Khong insert dc vao UplDetail");
			} else
				System.out.println("538.Khong insert dc vao UplMaster");

			// TODO insert UplCustomerHistory
			unitOfWorkTelesale.uplCustomerHistoryRepo().add(new UplCustomerHistory(uplCustomer.getId(),
					uplMaster.getId(), this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

		}

		return result;
	}
	
	public LeadResultDTO createRemainingTypes() {

		LeadResultDTO result = validateDataCreateRemainingTypes();

		if (result.getStatus().equals(LeadGenEnums.PASSED.value()))
			result = createCompleteDataRemainingTypes();
		else
			result = createErrorDataRemainingTypes(result);

		result.setDateTime(DateUtil.today("yyyy-MM-dd"));

		return result;
	}

}
