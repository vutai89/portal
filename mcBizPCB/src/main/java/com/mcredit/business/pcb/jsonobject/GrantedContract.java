
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class GrantedContract {

    public String EndDateOfContract;
    public String RemainingInstalmentsNumber;
    public String TypeOfLeasingSubject;
    public String PersonalGuaranteeAmount;
    public String TotalNumberOfInstalments;
    public Long UnpaidDueInstalmentsAmount;
    public String PaymentsPeriodicity;
    public Long RemainingInstalmentsAmount;
    public String MaximumLevelOfDefault;
    public String FlagNewUsed;
    public String NumberOfMonthsWithMaximumLevelOfDefault;
    public List<Profile> Profiles = null;
    public String GuarantedAmountFromGuarantor;
    public Integer NrOfDaysOfPaymentDelay;
    public String RegistrationNumber;
    public Integer WorstStatus;
    public String DateMaxNrOfDaysOfPaymentDelay;
    public String MethodOfPayment;
    public String UnpaidDueInstalmentsNumber;
    public String DateWorstStatus;
    public String LastPaymentDate;
    public Long TotalAmount;
    public String Brand;
    public CommonData CommonData;
    public String ReorganizedCredit;
    public Long MonthlyInstalmentAmount;
    public String ValueOfLeasingSubject;
    public String ExpirationDateofNextInstallment;
//    public String nextDueInstalmentAmount;
    public Integer MaxNrOfDaysOfPaymentDelay;
    public Long MaximumUnpaidAmount;
//    public String yearOfManufacturing;
//    public LinkedSubjects linkedSubjects;
//    public InstGuarantees instGuarantees;
    public Long ResidualAmount;
    public String CreditLimit;
    public String AmountOfTheCredits;

}
