
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class GeneralData {

    public String CurrencyCode;
    public String NumberOfReportingInstitution;
    public String WorstRecentStatus;
    public String TotalNumberOfContract;

}
