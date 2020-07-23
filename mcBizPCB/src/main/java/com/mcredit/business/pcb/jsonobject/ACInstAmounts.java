
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class ACInstAmounts {

    public Long RemainingInstalmentsAmount;
    public Long MonthlyInstalmentsAmount;
    public Long UnpaidDueInstalmentsAmount;

}
