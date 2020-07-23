
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Profile {

    public Integer Status;
    public String ReferenceMonth;
    public String ReferenceYear;
    public Long Default;
    public String Granted;
    public String GuarantedAmount;
    public String Utilization;
    public String ResidualAmount;

}
