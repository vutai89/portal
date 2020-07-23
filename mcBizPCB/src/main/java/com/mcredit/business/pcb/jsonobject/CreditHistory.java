
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class CreditHistory {

    public GeneralData GeneralData;
    public CurrencySummary CurrencySummary;
    public Contract Contract;

}
