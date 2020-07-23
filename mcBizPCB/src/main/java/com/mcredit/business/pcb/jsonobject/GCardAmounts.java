
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class GCardAmounts {

    public Long ResidualAmount;
    public Long OverDueAmount;
    public Long LimitOfCredit;

}
