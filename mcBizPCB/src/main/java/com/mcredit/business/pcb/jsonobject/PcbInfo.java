
package com.mcredit.business.pcb.jsonobject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;

import com.google.common.base.Strings;
import com.mcredit.model.enums.pcb.PCBContractPhase;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PcbInfo {

	public Result Result;
	
	
	public String getCusNameRequest() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Inquired.Person.Name;
		});
	}
	
	public String getIdentityRequest() throws Exception {
		return this.returnData(() -> {
			return converDoubleToString(this.Result.Subject.Inquired.Person.IDCard);
		});
	}
	
	public String getBirthDateRequest() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Inquired.Person.DateOfBirth;
		});
	}
	
	public String getMainAddressRequest() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Inquired.Person.Address.Main.get(0).FullAddress;
		});
	}
	
	public String getGenderRequest() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Inquired.Person.Gender;
		});
	}
	
	public String getCusName() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.Name : this.Result.Subject.Inquired.Person.Name;
		});
	}

	public String getPcbCode() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? this.Result.Subject.Matched.CBSubjectCode : this.Result.Subject.Inquired.CBSubjectCode;
		});
	}

	public String getIdentityCode() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? converDoubleToString(this.Result.Subject.Matched.Person.IDCard) : converDoubleToString(this.Result.Subject.Inquired.Person.IDCard);
		});
	}
	
	public String getIdentityCode2() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? converDoubleToString(this.Result.Subject.Matched.Person.IDCard2) : converDoubleToString(this.Result.Subject.Inquired.Person.IDCard2);
		});
	}

	public String getBirthDate() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.DateOfBirth : this.Result.Subject.Inquired.Person.DateOfBirth;
		});
	}

	public String getMainAddress() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.Address.Main.get(0).FullAddress : this.Result.Subject.Inquired.Person.Address.Main.get(0).FullAddress;
		});
	}

	public String getAdditionalAddress() throws Exception {
		return this.returnData(() -> {
			return this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.Address.Additional.FullAddress : this.Result.Subject.Inquired.Person.Address.Additional.FullAddress;
		});
	}

	public List<Document> getDocumentList() throws Exception {
		return this.returnData(() -> {
			List<Document> documentList = this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.Document : this.Result.Subject.Inquired.Person.Document;
			String str = "";
			if (documentList != null && documentList.size() > 0) {
				for (int i = 0; i < documentList.size(); i++) {
					str = converDoubleToString(documentList.get(i).Number);
					documentList.get(i).Number=str;
				}
			}
			return documentList;
		});
	}

	// SDT lien he
	public String getReferenceNumber() throws Exception {
		return this.returnData(() -> {

			List<Reference> referenceList = this.Result.Subject.Matched != null ? this.Result.Subject.Matched.Person.Reference : this.Result.Subject.Inquired.Person.Reference;
			String str = "";
			if (referenceList != null && referenceList.size() > 0) {
				for (int i = 0; i < referenceList.size(); i++) {
					if (i == 0) {
						str = converDoubleToString(referenceList.get(i).Number);
					} else {
						str = str + "/" + converDoubleToString(referenceList.get(i).Number);
					}
				}
			}

			return str;
		});
	}
	
	public String converDoubleToString(String input) {
		if (!input.contains("E") || Strings.isNullOrEmpty(input)) return input;
		try {
		Double x = Double.valueOf(input);
		return String.format("%.0f", x);	
		}catch(Exception ex)
		{
			System.out.println("Convert converDoubleToString exception input:" + input);
			return "";
		}
	}

	// So luong NH ma KH dang co quan he tin dung
	// Cach tinh : Count distinct
	// (Instalments/NonInstalments/Cards->GrantedContract->CommonData->EncryptedFICode = Bxx ) ,
	// Dieu kien : (Instalments/NonInstalments/Cards ->GrantedContract->CommonData->ContractPhase) = “LV”
	public String getCountBank() throws Exception {
		return this.returnData(() -> {
			return this.getRelationshipAmount(1);
		});
	}

	// Du no tai ngan hang (vai tro chinh)
	public String getLoanMainTotal() throws Exception {
		return this.returnData(() -> {
			return this.getLoanAmount(true);
		});
	}

	// Du no tai ngan hang (vai tro bao lanh)
	public String getLoanGuaranteeTotal() throws Exception {
		return this.returnData(() -> {
			return this.getLoanAmount(false);
		});
	}

	// So luong CTTC ma KH dang co quan he tin dung
	public String getFinalcialCompanyAmount() throws Exception {
		return this.returnData(() -> {
			return this.getRelationshipAmount(2);
		});
	}

	// Du no tai cong ty tai chinh
	public String getFinalComLoanTotal() throws Exception {
		return this.returnData(() -> {
			Long loanTotal = 0L;

			// Vay thong thuong total
			List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
			if (gIList != null && gIList.size() > 0) {
				for (GrantedContract cContract : gIList) {
					if (cContract != null && cContract.CommonData != null) {
						String fICodeStr = cContract.CommonData.EncryptedFICode;
						String cPhaseStr = cContract.CommonData.ContractPhase;
						if (fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_F)) {
							if (cContract.RemainingInstalmentsAmount != null) {
								loanTotal = loanTotal + Long.valueOf(cContract.RemainingInstalmentsAmount);
							}
							if (cContract.UnpaidDueInstalmentsAmount != null) {
								loanTotal = loanTotal + Long.valueOf(cContract.UnpaidDueInstalmentsAmount);
							}

						}
					}
				}
			}

			// Vay thau chi total
			List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
			if (gNList != null && gNList.size() > 0) {
				for (GrantedContract cContract : gNList) {
					if (cContract != null && cContract.CommonData != null) {
						String fICodeStr = cContract.CommonData.EncryptedFICode;
						String cPhaseStr = cContract.CommonData.ContractPhase;
						if (fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_F)) {
							List<Profile> profilesList = cContract.Profiles;
							if (profilesList != null && profilesList.size() > 0) {
								if (profilesList.get(0) != null) {
									if (profilesList.get(0).Utilization != null) {
										loanTotal = loanTotal + Long.valueOf(profilesList.get(0).Utilization);
									}
								}

							}

						}
					}
				}
			}

			// The tin dung total
			List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;
			if (gCList != null && gCList.size() > 0) {
				for (GrantedContract cContract : gCList) {
					if (cContract != null && cContract.CommonData != null) {
						String fICodeStr = cContract.CommonData.EncryptedFICode;
						String cPhaseStr = cContract.CommonData.ContractPhase;
						if (fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_F)) {
							if (cContract.ResidualAmount != null) {
								loanTotal = loanTotal + Long.valueOf(cContract.ResidualAmount);
							}
						}
					}
				}
			}

			return String.valueOf(loanTotal);
		});
	}

	// So luong TCTD ma khach hang dang co yeu cau vay
	public String getCreditComRequestAmount() throws Exception {
		return this.returnData(() -> {
			return this.getCreditComAmount(true);
		});
	}

	// So luong TCTD ma khach hang da bi tu choi
	public String getCreditComRejectAmount() throws Exception {
		return this.returnData(() -> {
			return this.getCreditComAmount(false);
		});
	}

	// Nhom no cao nhat trong vong 12 thang
	public String getHighest12MonthLoan() throws Exception {
		return this.returnData(() -> {
			return this.getHighestLoan(true);
		});
	}

	// Nhom no cao nhat trong vong 3 nam
	public String getHighest3YearLoan() throws Exception {
		return this.returnData(() -> {
			return this.getHighestLoan(false);
		});
	}

	// So ngay qua han lon nhat
	public String getOutOfDateMax() throws Exception {
		return this.returnData(() -> {

			Integer outOfDateMax = 0;
			List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
			List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
			List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;

			List<GrantedContract> mergeList = new ArrayList<>();
			if (gIList != null && gIList.size() > 0)
				mergeList.addAll(gIList);
			if (gNList != null && gNList.size() > 0)
				mergeList.addAll(gNList);
			if (gCList != null && gCList.size() > 0)
				mergeList.addAll(gCList);

			for (GrantedContract gc : mergeList) {
				if (gc != null && gc.MaxNrOfDaysOfPaymentDelay != null && gc.MaxNrOfDaysOfPaymentDelay > outOfDateMax) {
					outOfDateMax = gc.MaxNrOfDaysOfPaymentDelay;
				}
			}

			return String.valueOf(outOfDateMax);
		});
	}

	// So luong To chuc tin dung khach hang dang co quan he
	public String getCreditInstitutionsAmount() throws Exception {
		return this.returnData(() -> {
			return this.getRelationshipAmount(3);
		});
	}

	// So luong To chuc tin dung khach hang dang co quan he
	public String getTypeCurrency() throws Exception {
		return this.returnData(() -> {
			return PcbConstants.TYPE_CURRENCY_VND;
		});
	}

	// Nhom no cao nhat (all)
	public String getMaxWorstStatus() throws Exception {
		return this.returnData(() -> {
			Integer maxStatus = 0;
			List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
			List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
			List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;

			List<GrantedContract> mergeList = new ArrayList<>();
			if (gIList != null && gIList.size() > 0)
				mergeList.addAll(gIList);
			if (gNList != null && gNList.size() > 0)
				mergeList.addAll(gNList);
			if (gCList != null && gCList.size() > 0)
				mergeList.addAll(gCList);

			if (mergeList != null & mergeList.size() > 0) {
				for (GrantedContract gc : mergeList) {
					if (gc != null && gc.WorstStatus != null && gc.WorstStatus > maxStatus) {
						maxStatus = gc.WorstStatus;
					}
				}
			}

			return maxStatus == 0 ? null : String.valueOf(maxStatus);
		});
	}

	// Dai diem tin dung
	public String getScoreRangeCode() throws Exception {
		return this.returnData(() -> {
			return this.Result.CreditHistory.Contract.ScoreProfile.ScoreDetail.ScoreRange.Code;
		});
	}

	// Diem tin dung
	public String getScoreScoreRaw() throws Exception {
		return this.returnData(() -> {
			return this.Result.CreditHistory.Contract.ScoreProfile.ScoreDetail.ScoreRaw;
		});
	}

	// Danh gia diem tin dung
	public String getScoreDescription() throws Exception {
		return this.returnData(() -> {
			return this.Result.CreditHistory.Contract.ScoreProfile.ScoreDetail.ScoreRange.Description;
		});
	}

	// Vay thong thuong : so luong yeu cau
	public String getInstalmentsNumberOfRequested() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.Summary.NumberOfRequested);
		});
	}

	// Vay thong thuong : so luong tu choi
	public String getInstalmentsNumberOfRefused() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.Summary.NumberOfRefused);
		});
	}

	// Vay thong thuong : so luong tu bo
	public String getInstalmentsNumberOfRenounced() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.Summary.NumberOfRenounced);
		});
	}

	// Vay thong thuong : so luong dang ton tai
	public String getInstalmentsNumberOfLiving() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.Summary.NumberOfLiving);
		});
	}

	// Vay thong thuong : so luong cham dut
	public String getInstalmentsNumberOfTerminated() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.Summary.NumberOfTerminated);
		});
	}

	// The tin dung : so luong yeu cau
	public String getCardsNumberOfRequested() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.Summary.NumberOfRequested);
		});
	}

	// The tin dung : so luong tu choi
	public String getCardsNumberOfRefused() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.Summary.NumberOfRefused);
		});
	}

	// The tin dung : so luong tu bo
	public String getCardsNumberOfRenounced() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.Summary.NumberOfRenounced);
		});
	}

	// The tin dung : dang ton tai
	public String getCardsNumberOfLiving() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.Summary.NumberOfLiving);
		});
	}

	// The tin dung : cham dut
	public String getCardsNumberOfTerminated() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.Summary.NumberOfTerminated);
		});
	}

	// Vay thau chi : yeu cau
	public String getNonInstalmentsNumberOfRequested() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.Summary.NumberOfRequested);
		});
	}

	// Vay thau chi : tu choi
	public String getNonInstalmentsNumberOfRefused() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.Summary.NumberOfRefused);
		});
	}

	// Vay thau chi : tu bo
	public String getNonInstalmentsNumberOfRenounced() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.Summary.NumberOfRenounced);
		});
	}

	// Vay thau chi : dang ton tai
	public String getNonInstalmentsNumberOfLiving() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.Summary.NumberOfLiving);
		});
	}

	// Vay thau chi : cham dut
	public String getNonInstalmentsNumberOfTerminated() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.Summary.NumberOfTerminated);
		});
	}

	// So tien goc, lai tra hang thang (vay thong thuong, vai tro chinh)
	public String getMainMonthlyInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.ACInstAmounts.MonthlyInstalmentsAmount);
		});
	}

	// So tien goc, lai tra hang thang (vay thong thuong, vai tro bao lanh)
	public String getGuardMonthlyInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.GInstAmounts.MonthlyInstalmentsAmount);
		});
	}

	// So tien goc va lai con no (vay thong thuong, vai tro chinh)
	public String getMainRemainingInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.ACInstAmounts.RemainingInstalmentsAmount);
		});
	}

	// So tien goc va lai con no (vay thong thuong, vai tro bao lanh)
	public String getGuradRemainingInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.GInstAmounts.RemainingInstalmentsAmount);
		});
	}

	// So tien goc va lai qua han (vay thong thuong, vai tro chinh)
	public String getMainUnpaidDueInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.ACInstAmounts.UnpaidDueInstalmentsAmount);
		});
	}

	// So tien goc va lai qua han (vay thong thuong, vai tro bao lanh)
	public String getGuardUnpaidDueInstalmentsAmount() throws Exception {
		return this.returnData(() -> {
			Instalments instalments = this.Result.CreditHistory.Contract.Instalments;
			return String.valueOf(instalments.GInstAmounts.UnpaidDueInstalmentsAmount);
		});
	}

	// The tin dung : han muc (chinh)
	public String getMainLimitOfCredit() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.ACCardAmounts.LimitOfCredit);
		});
	}

	// The tin dung : han muc (bao lanh)
	public String getGuradLimitOfCredit() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.GCardAmounts.LimitOfCredit);
		});
	}

	// The tin dung : du no (chinh)
	public String getMainResidualAmount() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.ACCardAmounts.ResidualAmount);
		});
	}

	// The tin dung : du no (bao lanh)
	public String getGuardResidualAmount() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.GCardAmounts.ResidualAmount);
		});
	}

	// The tin dung : so tien no qua han hien tai (chinh)
	public String getMainOverDueAmount() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.ACCardAmounts.OverDueAmount);
		});
	}

	// The tin dung : so tien no qua han hien tai (bao lanh)
	public String getGuardOverDueAmount() throws Exception {
		return this.returnData(() -> {
			Cards cards = this.Result.CreditHistory.Contract.Cards;
			return String.valueOf(cards.GCardAmounts.OverDueAmount);
		});
	}

	// Vay thau chi : han muc(chinh)
	public String getMainCreditLimit() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.ACNoInstAmounts.CreditLimit);
		});
	}

	// Vay thau chi : han muc(bao lanh)
	public String getGuardCreditLimit() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.GNoInstAmounts.CreditLimit);
		});
	}

	// Vay thau chi : du no (chinh)
	public String getMainUtilization() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.ACNoInstAmounts.Utilization);
		});
	}

	// Vay thau chi : du no (bao lanh)
	public String getGuardUtilization() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.GNoInstAmounts.Utilization);
		});
	}

	// Vay thau chi : so tien vuot qua han muc thau chi (chinh)
	public String getMainOverdraft() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.ACNoInstAmounts.Overdraft);
		});
	}

	// Vay thau chi : so tien vuot qua han muc thau chi (bao lanh)
	public String getGuardOverdraft() throws Exception {
		return this.returnData(() -> {
			NonInstalments nonInstalments = this.Result.CreditHistory.Contract.NonInstalments;
			return String.valueOf(nonInstalments.GNoInstAmounts.Overdraft);
		});
	}

	// Danh sach hop dong vay dang ton tai : vay thong thuong
	public List<GrantedContract> getInstalmentsGrantedContractLiving() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream().filter(line -> line != null && line.CommonData != null && PcbConstants.CONTRACT_PHASE_LV.equals(line.CommonData.ContractPhase))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay dang ton tai : the tin dung
	public List<GrantedContract> getCardsGrantedContractLiving() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.Cards.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream().filter(line -> line != null && line.CommonData != null && PcbConstants.CONTRACT_PHASE_LV.equals(line.CommonData.ContractPhase))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay dang ton tai : vay thau chi
	public List<GrantedContract> getNonInstalmentsGrantedContractLiving() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream().filter(line -> line != null && line.CommonData != null && PcbConstants.CONTRACT_PHASE_LV.equals(line.CommonData.ContractPhase))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay dang da cham dut : vay thong thuong
	public List<GrantedContract> getInstalmentsGrantedContractEnd() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream()
						.filter(line -> line != null && line.CommonData != null
								&& (PcbConstants.CONTRACT_PHASE_TM.equals(line.CommonData.ContractPhase) || PcbConstants.CONTRACT_PHASE_TA.equals(line.CommonData.ContractPhase)))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay dang da cham dut : the tin dung
	public List<GrantedContract> getCardsGrantedContractEnd() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.Cards.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream()
						.filter(line -> line != null && line.CommonData != null
								&& (PcbConstants.CONTRACT_PHASE_TM.equals(line.CommonData.ContractPhase) || PcbConstants.CONTRACT_PHASE_TA.equals(line.CommonData.ContractPhase)))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay dang da cham dut : vay thau chi
	public List<GrantedContract> getNonInstalmentsGrantedContractEnd() throws Exception {
		return this.returnData(() -> {
			List<GrantedContract> grantedContractList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
			List<GrantedContract> resultList = null;
			if (grantedContractList != null && grantedContractList.size() > 0) {
				resultList = grantedContractList.stream()
						.filter(line -> line != null && line.CommonData != null
								&& (PcbConstants.CONTRACT_PHASE_TM.equals(line.CommonData.ContractPhase) || PcbConstants.CONTRACT_PHASE_TA.equals(line.CommonData.ContractPhase)))
						.collect(Collectors.toList());
			}

			return resultList;
		});
	}

	// Danh sach hop dong vay tinh trang yeu cau/tu choi/tu bo : vay thong thuong + card + thau chi
	public List<NotGrantedContract> getNotGrantedContract() throws Exception {
		return this.returnData(() -> {
			List<NotGrantedContract> instalmentsNotGrantedContract = this.Result.CreditHistory.Contract.Instalments.NotGrantedContract;
			List<NotGrantedContract> cardsNotGrantedContract = this.Result.CreditHistory.Contract.Cards.NotGrantedContract;
			List<NotGrantedContract> nonInstalmentsNotGrantedContract = this.Result.CreditHistory.Contract.NonInstalments.NotGrantedContract;

			// Edit lai so tien vay yeu cau cho Card
			if (cardsNotGrantedContract != null && cardsNotGrantedContract.size() > 0) {
				for (NotGrantedContract item : cardsNotGrantedContract) {
					if (item != null && item.Amounts != null) {
						item.Amounts.TotalAmount = item.Amounts.CreditLimit;
					}

				}
			}

			List<NotGrantedContract> mergeList = new ArrayList<>();
			if (instalmentsNotGrantedContract != null && instalmentsNotGrantedContract.size() > 0)
				mergeList.addAll(instalmentsNotGrantedContract);
			if (cardsNotGrantedContract != null && cardsNotGrantedContract.size() > 0)
				mergeList.addAll(cardsNotGrantedContract);
			if (nonInstalmentsNotGrantedContract != null && nonInstalmentsNotGrantedContract.size() > 0)
				mergeList.addAll(nonInstalmentsNotGrantedContract);

			return mergeList;
		});
	}
	
	// EMI tat ca cac khoan vay thong thuong tai cac TCTD khac. Chi lay khoan vay trang thai LV (Living). Khong tinh khoan vay co so ky tra no con lai <= 1 (tinh 30 ngay)
	public Long getEMIInstalments() throws Exception {
		Long emi = 0L;
		List<GrantedContract> instalmentsGrantedContract = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
		
		if (null != instalmentsGrantedContract) {
			for (GrantedContract gc : instalmentsGrantedContract) {
				if (!StringUtils.isNullOrEmpty(gc.EndDateOfContract)) {
					Long dateRemain = DateUtil.getDateDiff(DateUtil.getDateWithoutTime(new Date()), DateUtil.toDate(gc.EndDateOfContract, DateTimeFormat.ddMMyyyy), TimeUnit.DAYS);
					if (null != gc.MonthlyInstalmentAmount && null != gc.CommonData && PCBContractPhase.Living.value().equals(gc.CommonData.ContractPhase) && dateRemain > 30)
						emi += gc.MonthlyInstalmentAmount;
				}
			}
		}

		return emi;
	}
	
	// EMI tat ca cac khoan vay thau chi tai cac TCTD khac. Chi lay khoan vay trang thai LV (Living). Khong tinh khoan vay co so ky tra no con lai <= 1 (tinh 30 ngay)
	public Long getEMINonInstalments() throws Exception {
		Long emi = 0L;
		List<GrantedContract> nonInstalmentsGrantedContract = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
		
		if (null != nonInstalmentsGrantedContract) {
			for (GrantedContract gc : nonInstalmentsGrantedContract) {
				if (!StringUtils.isNullOrEmpty(gc.EndDateOfContract)) {
					Long dateRemain = DateUtil.getDateDiff(DateUtil.getDateWithoutTime(new Date()), DateUtil.toDate(gc.EndDateOfContract, DateTimeFormat.ddMMyyyy), TimeUnit.DAYS);
					if (!StringUtils.isNullOrEmpty(gc.AmountOfTheCredits) && null != gc.CommonData && PCBContractPhase.Living.value().equals(gc.CommonData.ContractPhase) && dateRemain > 30)
						emi += (Double.valueOf(gc.AmountOfTheCredits).longValue() * 5/100);
				}
			}
		}

		return emi;
	}

	// EMI tat ca cac khoan vay card tai cac TCTD khac. Chi lay khoan vay trang thai LV (Living). Khong tinh khoan vay co so ky tra no con lai <= 1 (tinh 30 ngay)
	public Long getEMICard() throws Exception {
		Long emi = 0L;
		List<GrantedContract> cardGrantedContract = this.Result.CreditHistory.Contract.Cards.GrantedContract;
		
		if (null != cardGrantedContract) {
			for (GrantedContract gc : cardGrantedContract) {
				if (!StringUtils.isNullOrEmpty(gc.EndDateOfContract)) {
					Long dateRemain = DateUtil.getDateDiff(DateUtil.getDateWithoutTime(new Date()), DateUtil.toDate(gc.EndDateOfContract, DateTimeFormat.ddMMyyyy), TimeUnit.DAYS);
					if (!StringUtils.isNullOrEmpty(gc.CreditLimit) && null != gc.CommonData && PCBContractPhase.Living.value().equals(gc.CommonData.ContractPhase) && dateRemain > 30)
						emi += (Double.valueOf(gc.CreditLimit).longValue() * 5/100);
				}
			}
		}

		return emi;
	}

	// Private Methods
	private <T> T returnData(Callable<T> func) throws Exception {
		try {
			return func.call();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Tong no tai NH hoac Cong ty TC
	private String getLoanAmount(Boolean isMain) {
		Long loanTotal = 0L;
		HashSet<String> hset = new HashSet<>();
		if (isMain) {
			hset.add(PcbConstants.ROLE_A);
			hset.add(PcbConstants.ROLE_C);
		} else {
			hset.add(PcbConstants.ROLE_G);
		}

		// Vay thong thuong total
		List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
		if (gIList != null && gIList.size() > 0) {
			for (GrantedContract cContract : gIList) {
				if (cContract.CommonData != null) {
					String fICodeStr = cContract.CommonData.EncryptedFICode;
					String cPhaseStr = cContract.CommonData.ContractPhase;
					String roleStr = cContract.CommonData.Role;
					if ((fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_B)) && hset.contains(roleStr)) {
						if (cContract.RemainingInstalmentsAmount != null) {
							loanTotal = loanTotal + Long.valueOf(cContract.RemainingInstalmentsAmount);
						}
						if (cContract.UnpaidDueInstalmentsAmount != null) {
							loanTotal = loanTotal + Long.valueOf(cContract.UnpaidDueInstalmentsAmount);
						}

					}
				}
			}
		}

		// Vay thau chi total
		List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
		if (gNList != null && gNList.size() > 0) {
			for (GrantedContract cContract : gNList) {
				if (cContract.CommonData != null) {
					String fICodeStr = cContract.CommonData.EncryptedFICode;
					String cPhaseStr = cContract.CommonData.ContractPhase;
					String roleStr = cContract.CommonData.Role;
					if ((fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_B)) && hset.contains(roleStr)) {
						List<Profile> profilesList = cContract.Profiles;
						if (profilesList != null && profilesList.size() > 0) {
							if (profilesList.get(0) != null) {
								if (profilesList.get(0).Utilization != null) {
									loanTotal = loanTotal + Long.valueOf(profilesList.get(0).Utilization);
								}
							}

						}

					}
				}
			}
		}

		// The tin dung total
		List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;
		if (gCList != null && gCList.size() > 0) {
			for (GrantedContract cContract : gCList) {
				if (cContract.CommonData != null) {
					String fICodeStr = cContract.CommonData.EncryptedFICode;
					String cPhaseStr = cContract.CommonData.ContractPhase;
					String roleStr = cContract.CommonData.Role;
					if ((fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(PcbConstants.EFI_CODE_B)) && hset.contains(roleStr)) {
						if (cContract.ResidualAmount != null) {
							loanTotal = loanTotal + Long.valueOf(cContract.ResidualAmount);
						}
					}
				}
			}
		}

		return String.valueOf(loanTotal);

	}

	// So luong Cong ty tai chinh /bank KH dang co quan he tin dung
	// 1 : Ngan Hang , 2 : cong ty tai chinh, 3 : tat ca cac to chuc tin dung (1&2...)
	private String getRelationshipAmount(int typeCreditors) {
		List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
		List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
		List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;

		List<GrantedContract> mergeList = new ArrayList<>();
		if (gIList != null && gIList.size() > 0)
			mergeList.addAll(gIList);
		if (gNList != null && gNList.size() > 0)
			mergeList.addAll(gNList);
		if (gCList != null && gCList.size() > 0)
			mergeList.addAll(gCList);

		String code = "";
		if (typeCreditors == 1) {
			code = PcbConstants.EFI_CODE_B;
		}
		if (typeCreditors == 2) {
			code = PcbConstants.EFI_CODE_F;
		}

		HashSet<String> hSet = new HashSet<>();
		for (GrantedContract cContract : mergeList) {
			if (cContract.CommonData != null) {
				String fICodeStr = cContract.CommonData.EncryptedFICode;
				String cPhaseStr = cContract.CommonData.ContractPhase;
				if (typeCreditors == 3) {
					if (fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr)) {
						hSet.add(fICodeStr);
					}
				} else {
					if (fICodeStr != null && PcbConstants.CONTRACT_PHASE_LV.equals(cPhaseStr) && fICodeStr.startsWith(code)) {
						hSet.add(fICodeStr);
					}
				}

			}
		}
		return String.valueOf(hSet.size());
	}

	private String getCreditComAmount(Boolean isRequest) {
		List<NotGrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.NotGrantedContract;
		List<NotGrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.NotGrantedContract;
		List<NotGrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.NotGrantedContract;

		List<NotGrantedContract> mergeList = new ArrayList<>();
		if (gIList != null && gIList.size() > 0)
			mergeList.addAll(gIList);
		if (gNList != null && gNList.size() > 0)
			mergeList.addAll(gNList);
		if (gCList != null && gCList.size() > 0)
			mergeList.addAll(gCList);

		HashSet<String> codeHSet = new HashSet<>();
		HashSet<String> contractPhaseHSet = new HashSet<>();

		if (isRequest) {
			contractPhaseHSet.add(PcbConstants.CONTRACT_PHASE_RQ);
		} else {
			contractPhaseHSet.add(PcbConstants.CONTRACT_PHASE_RN);
			contractPhaseHSet.add(PcbConstants.CONTRACT_PHASE_RF);
		}

		for (NotGrantedContract cContract : mergeList) {
			try {
				String strDate = cContract.Amounts.RequestDateOfTheContract;
				Date dateContract = new SimpleDateFormat(PcbConstants.DDMMYYYY).parse(strDate);
				Date dateNow = new Date();
				Date before1Months =  DateUtils.addDays(DateUtils.addMonths(dateNow, -1), -2);
				if (contractPhaseHSet.contains(cContract.ContractPhase) && dateContract.after(before1Months)) {
					codeHSet.add(cContract.EncryptedFICode);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		return String.valueOf(codeHSet.size());
	}

	// Nhom no cao nhat trong 12 thang hoac 3 nam
	@SuppressWarnings("deprecation")
	private String getHighestLoan(Boolean is12Month) throws Exception {
		Integer highestLoan = 0;
		List<GrantedContract> gIList = this.Result.CreditHistory.Contract.Instalments.GrantedContract;
		List<GrantedContract> gNList = this.Result.CreditHistory.Contract.NonInstalments.GrantedContract;
		List<GrantedContract> gCList = this.Result.CreditHistory.Contract.Cards.GrantedContract;

		List<GrantedContract> mergeList = new ArrayList<>();
		if (gIList != null && gIList.size() > 0)
			mergeList.addAll(gIList);
		if (gNList != null && gNList.size() > 0)
			mergeList.addAll(gNList);
		if (gCList != null && gCList.size() > 0)
			mergeList.addAll(gCList);

		List<Profile> profileList = new ArrayList<>();

		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(new Date());
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		if (is12Month) {
			calendar1.add(Calendar.MONTH, -12);
		} else {
			calendar1.add(Calendar.MONTH, -36);
		}

		for (GrantedContract gc : mergeList) {
			if (gc.Profiles != null && !gc.Profiles.isEmpty()) {
				try {
					for (Profile profile : gc.Profiles) {
						Integer month = Integer.valueOf(profile.ReferenceMonth);
						Integer year = Integer.valueOf(profile.ReferenceYear);
						Calendar calendar2 = Calendar.getInstance();
						calendar2.set(year, month - 1, 31);
						if (!calendar2.before(calendar1)) {
							profileList.add(profile);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}

		for (Profile profile : profileList) {
			if (profile.Status != null && profile.Status > highestLoan) {
				highestLoan = profile.Status;
			}
		}

		return highestLoan == 0 ? null : String.valueOf(highestLoan);
	}

}
