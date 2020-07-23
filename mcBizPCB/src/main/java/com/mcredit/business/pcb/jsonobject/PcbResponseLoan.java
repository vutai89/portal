package com.mcredit.business.pcb.jsonobject;

public class PcbResponseLoan {

	// So luong vay thong thuong : yeu cau
	public String instalmentsNumberOfRequested;

	// So luong vay thong thuong : tu choi
	public String instalmentsNumberOfRefused;

	// So luong vay thong thuong : tu bo
	public String instalmentsNumberOfRenounced;

	// So luong vay thong thuong : dang ton tai
	public String instalmentsNumberOfLiving;

	// So luong vay thong thuong : chan dut
	public String instalmentsNumberOfTerminated;

	// So luong vay cards : yeu cau
	public String cardsNumberOfRequested;

	// So luong vay cards : tu choi
	public String cardsNumberOfRefused;

	// So luong vay cards : tu bo
	public String cardsNumberOfRenounced;

	// So luong vay cards : dang ton tai
	public String cardsNumberOfLiving;

	// So luong vay cards : cham dut
	public String cardsNumberOfTerminated;

	// So luong vay thau chi : yeu cau
	public String nonInstalmentsNumberOfRequested;

	// So luong vay thau chi : tu choi
	public String nonInstalmentsNumberOfRefused;

	// So luong vay thau chi : tu bo
	public String nonInstalmentsNumberOfRenounced;

	// So luong vay thau chi : dang ton tai
	public String nonInstalmentsNumberOfLiving;

	// So luong vay thau chi : cham dut
	public String nonInstalmentsNumberOfTerminated;

	// So tien goc, lai tra hang thang (vay thong thuong, vai tro chinh)
	public String mainMonthlyInstalmentsAmount;
	// So tien goc, lai tra hang thang (vay thong thuong, vai tro bao lanh)
	public String guardMonthlyInstalmentsAmount;

	// So tien goc va lai con no (vay thong thuong, vai tro chinh)
	public String mainRemainingInstalmentsAmount;

	// So tien goc va lai con no (vay thong thuong, vai tro bao lanh)
	public String guradRemainingInstalmentsAmount;

	// So tien goc va lai qua han (vay thong thuong, vai tro chinh)
	public String mainUnpaidDueInstalmentsAmount;

	// So tien goc va lai qua han (vay thong thuong, vai tro bao lanh)
	public String guardUnpaidDueInstalmentsAmount;

	// The tin dung : han muc (chinh)
	public String mainLimitOfCredit;

	// The tin dung : han muc (bao lanh)
	public String guradLimitOfCredit;

	// The tin dung : du no (chinh)
	public String mainResidualAmount;

	// The tin dung : du no (bao lanh)
	public String guardResidualAmount;

	// The tin dung : so tien no qua han hien tai (chinh)
	public String mainOverDueAmount;

	// The tin dung : so tien no qua han hien tai (bao lanh)
	public String guardOverDueAmount;

	// Vay thau chi : han muc(chinh)
	public String mainCreditLimit;

	// Vay thau chi : han muc(bao lanh)
	public String guardCreditLimit;

	// Vay thau chi : du no (chinh)
	public String mainUtilization;

	// Vay thau chi : du no (bao lanh)
	public String guardUtilization;

	// Vay thau chi : so tien vuot qua han muc thau chi (chinh)
	public String mainOverdraft;

	// Vay thau chi : so tien vuot qua han muc thau chi (bao lanh)
	public String guardOverdraft;

	public PcbResponseLoan(String instalmentsNumberOfRequested, String instalmentsNumberOfRefused, String instalmentsNumberOfRenounced, String instalmentsNumberOfLiving,
			String instalmentsNumberOfTerminated, String cardsNumberOfRequested, String cardsNumberOfRefused, String cardsNumberOfRenounced, String cardsNumberOfLiving, String cardsNumberOfTerminated,
			String nonInstalmentsNumberOfRequested, String nonInstalmentsNumberOfRefused, String nonInstalmentsNumberOfRenounced, String nonInstalmentsNumberOfLiving,
			String nonInstalmentsNumberOfTerminated, String mainMonthlyInstalmentsAmount, String guardMonthlyInstalmentsAmount, String mainRemainingInstalmentsAmount,
			String guradRemainingInstalmentsAmount, String mainUnpaidDueInstalmentsAmount, String guardUnpaidDueInstalmentsAmount, String mainLimitOfCredit, String guradLimitOfCredit,
			String mainResidualAmount, String guardResidualAmount, String mainOverDueAmount, String guardOverDueAmount, String mainCreditLimit, String guardCreditLimit, String mainUtilization,
			String guardUtilization, String mainOverdraft, String guardOverdraft) {
		super();
		this.instalmentsNumberOfRequested = instalmentsNumberOfRequested;
		this.instalmentsNumberOfRefused = instalmentsNumberOfRefused;
		this.instalmentsNumberOfRenounced = instalmentsNumberOfRenounced;
		this.instalmentsNumberOfLiving = instalmentsNumberOfLiving;
		this.instalmentsNumberOfTerminated = instalmentsNumberOfTerminated;
		this.cardsNumberOfRequested = cardsNumberOfRequested;
		this.cardsNumberOfRefused = cardsNumberOfRefused;
		this.cardsNumberOfRenounced = cardsNumberOfRenounced;
		this.cardsNumberOfLiving = cardsNumberOfLiving;
		this.cardsNumberOfTerminated = cardsNumberOfTerminated;
		this.nonInstalmentsNumberOfRequested = nonInstalmentsNumberOfRequested;
		this.nonInstalmentsNumberOfRefused = nonInstalmentsNumberOfRefused;
		this.nonInstalmentsNumberOfRenounced = nonInstalmentsNumberOfRenounced;
		this.nonInstalmentsNumberOfLiving = nonInstalmentsNumberOfLiving;
		this.nonInstalmentsNumberOfTerminated = nonInstalmentsNumberOfTerminated;
		this.mainMonthlyInstalmentsAmount = mainMonthlyInstalmentsAmount;
		this.guardMonthlyInstalmentsAmount = guardMonthlyInstalmentsAmount;
		this.mainRemainingInstalmentsAmount = mainRemainingInstalmentsAmount;
		this.guradRemainingInstalmentsAmount = guradRemainingInstalmentsAmount;
		this.mainUnpaidDueInstalmentsAmount = mainUnpaidDueInstalmentsAmount;
		this.guardUnpaidDueInstalmentsAmount = guardUnpaidDueInstalmentsAmount;
		this.mainLimitOfCredit = mainLimitOfCredit;
		this.guradLimitOfCredit = guradLimitOfCredit;
		this.mainResidualAmount = mainResidualAmount;
		this.guardResidualAmount = guardResidualAmount;
		this.mainOverDueAmount = mainOverDueAmount;
		this.guardOverDueAmount = guardOverDueAmount;
		this.mainCreditLimit = mainCreditLimit;
		this.guardCreditLimit = guardCreditLimit;
		this.mainUtilization = mainUtilization;
		this.guardUtilization = guardUtilization;
		this.mainOverdraft = mainOverdraft;
		this.guardOverdraft = guardOverdraft;
	}

}
