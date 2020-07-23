package com.mcredit.sharedbiz.aggregate;

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
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CustomerValidationLength;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.enums.AmoEnums;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.model.leadgen.LeadDTO;
import com.mcredit.model.leadgen.LeadResultDTO;
import com.mcredit.model.leadgen.UploadCustomerDTO;
import com.mcredit.model.leadgen.UploadDataId;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class AmoAggregate extends AllocationAggregate {

	private LeadDTO partnerInput = null;

	private UploadCustomerDTO uplCustomerExists;

	private final static Lock lock = new ReentrantLock();

	public AmoAggregate(UnitOfWorkTelesale uokTelesale, UnitOfWorkCustomer uokCustomer, LeadDTO input) {

		super(uokTelesale, uokCustomer);

		this.partnerInput = input;
	}

	public AmoAggregate(UnitOfWorkTelesale uokTelesale, String identityNumber, String mobileNumber) {

		super(uokTelesale);
	}

	public LeadResultDTO createLead() {

		LeadResultDTO result = validateDataCreateSrv();

		if (result.getStatus().equals(AmoEnums.PASSED.value()))
			result = createCompleteData();
		else
			result = createErrorData(result);

		result.setDateTime(DateUtil.today("yyyy-MM-dd"));

		return result;
	}

	public LeadResultDTO validateDataCreateSrv() {

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

			result.setStatus(AmoEnums.ERROR.value());
			result.setReason(msgStr);
		} else {

			result.setStatus(AmoEnums.PASSED.value());

			// Check ton tai SDT trong he thong AMO
			this.uplCustomerExists = unitOfWorkTelesale.uplCustomerRepo()
					.findResponseCodeUplCustomer(AmoEnums.AMO.value(), this.partnerInput.getPhoneNumber());

			if (this.uplCustomerExists != null) {
				result.setStatus(AmoEnums.DEDUP_1.value());
				result.setReason(Messages.getString("label.leadgen.custExists"));
			} else {

				// Check ton tai SDT trong he thong NTB
				if (unitOfWorkTelesale.uplCustomerRepo()
						.checkExistsDataVersusNTBSystem(this.partnerInput.getPhoneNumber())) {

					result.setStatus(AmoEnums.DEDUP_2.value());
					result.setReason(Messages.getString("label.leadgen.custExists"));

				} else {

					if (!StringUtils.isNullOrEmpty(this.partnerInput.getNationalId())) {

						Integer statusBlackList = unitOfWorkTelesale.uplCustomerRepo()
								.checkBlackListWatchListLeadGenInput(this.partnerInput.getNationalId(),
										AmoEnums.BLACK_LIST.value());

						if (statusBlackList != null && statusBlackList > 0) {

							result.setStatus(AmoEnums.ERROR.value());
							result.setReason(AmoEnums.CUST_IN_BLACK_LIST.value());
						}
					}
				}
			}
		}

		return result;
	}

	public LeadResultDTO createCompleteData() {

		lock.lock();

		LeadResultDTO result = null;

		try {

			result = new LeadResultDTO();

			result.setStatus(AmoEnums.ERROR.value());

			String uploadCode = AmoEnums.PREFIX_UPL_CODE.value() + DateUtil.today("MMyyyy");

			UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

			if (uploadDataId != null) { // Insert/Update vao UplCustomer, Update imported+1 cho UplMaster va UplDetail

				UplCustomer uplCustomer = new UplCustomer(uploadDataId.getUploadDetailId(),
						this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
						this.partnerInput.getNationalId(), this.partnerInput.getIncomeLevel(), AmoEnums.OK.value(), "",
						this.partnerInput.getProvince(), null, null, null, new Date(), this.partnerInput.getRefId());

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
							result.setStatus(AmoEnums.SUCCESS.value());
							
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

				Long fromSource = ctCache.getIdBy(CTCodeValue1.AMO, CTCat.UPL_SRC, 0);
				Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

				UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
						fromSource, uploadCode, uplType, new Long("1"), new Long("0"));

				unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

				if (uplMaster.getId() != null && uplMaster.getId() > 0) {

					UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 1, 0,
							AmoEnums.UPL_STATUS.value(), uplMaster.getId());

					unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

					if (uplDetail.getId() != null && uplDetail.getId() > 0) {

						uplCustomer = new UplCustomer(uplDetail.getId(), this.partnerInput.getFullName(),
								this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
								this.partnerInput.getNationalId(), this.partnerInput.getIncomeLevel(),
								AmoEnums.OK.value(), "", this.partnerInput.getProvince(), null, null, null, new Date(),
								this.partnerInput.getRefId());

						unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

						result.setBankLeadId((uplCustomer.getId() != null && !uplCustomer.getId().equals(0L))
								? uplCustomer.getId() + ""
								: "");

						if (uplCustomer.getId() != null && uplCustomer.getId() > 0)
							result.setStatus(AmoEnums.SUCCESS.value());
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

	public LeadResultDTO createErrorData(LeadResultDTO result) {

		String uploadCode = AmoEnums.PREFIX_UPL_CODE.value() + DateUtil.today("MMyyyy");

		UploadDataId uploadDataId = unitOfWorkTelesale.uplCustomerRepo().findUploadMasterIdByUploadCode(uploadCode);

		if (uploadDataId != null) { // Insert vao UplCustomer, Update imported+1 cho UplMaster va UplDetail

			UplCustomer uplCustomer = new UplCustomer(
					// uploadDataId.getUploadDetailId()
					0L // TODO Set uplDetailId = 0 de ko anh huong den phan bo
					, this.partnerInput.getFullName(), this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
					this.partnerInput.getNationalId(), this.partnerInput.getIncomeLevel(), result.getStatus(),
					result.getReason(), this.partnerInput.getProvince(), new Long("-1"), new Long("-1"), new Long("-1"),
					new Date(), this.partnerInput.getRefId());

			unitOfWorkTelesale.uplCustomerRepo().add(uplCustomer);

			if (uplCustomer.getId() != null && uplCustomer.getId() > 0) {
			} else
				System.out.println("463.Khong insert dc vao UplCustomer");

			// TODO insert UplCustomerHistory
			unitOfWorkTelesale.uplCustomerHistoryRepo()
					.add(new UplCustomerHistory(uplCustomer.getId(), uploadDataId.getUploadMasterId(),
							this.partnerInput.getRefId(), result.getStatus(), result.getReason()));

		} else { // Insert moi vao UplMaster, UplDetail, UplCustomer

			UplCustomer uplCustomer = new UplCustomer();

			Long fromSource = ctCache.getIdBy(CTCodeValue1.AMO, CTCat.UPL_SRC, 0);
			Integer uplType = ctCache.getIdBy(CTCodeValue1.GC, CTCat.UPL_TYPE, 0).intValue();

			UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".json",
					fromSource, uploadCode, uplType, new Long("1"), new Long("0"));

			unitOfWorkTelesale.uplMasterRepo().add(uplMaster);

			if (uplMaster.getId() != null && uplMaster.getId() > 0) {

				UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 1, 0,
						AmoEnums.UPL_STATUS.value(), uplMaster.getId());

				unitOfWorkTelesale.uplDetailRepo().add(uplDetail);

				if (uplDetail.getId() != null && uplDetail.getId() > 0) {

					uplCustomer = new UplCustomer(uplDetail.getId(), this.partnerInput.getFullName(),
							this.partnerInput.getDob(), this.partnerInput.getPhoneNumber(),
							this.partnerInput.getNationalId(), this.partnerInput.getIncomeLevel(), result.getStatus(),
							result.getReason(), this.partnerInput.getProvince(), new Long("-1"), new Long("-1"),
							new Long("-1"), new Date(), this.partnerInput.getRefId());

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

	public UploadCustomerDTO getUplCustomerExists() {
		return uplCustomerExists;
	}

	public void setUplCustomerExists(UploadCustomerDTO uplCustomerExists) {
		this.uplCustomerExists = uplCustomerExists;
	}
}
