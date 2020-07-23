
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Amounts {

    public String MonthlyInstalmentAmount;
    public String TotalNumberOfInstalments;
    public String RequestDateOfTheContract;
    public Long TotalAmount;
    public String PaymentPeriodicity;
    public Long CreditLimit;

}
