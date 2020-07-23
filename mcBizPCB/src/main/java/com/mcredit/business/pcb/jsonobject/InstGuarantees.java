
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class InstGuarantees {

    public String GuarantorDescription;
    public String CodeGuaranteeType;
    public String GuaranteedAmount;
    public String FIGuaranteeCode;
    public RealGuarantee RealGuarantee;
    public PersonalGuarantee PersonalGuarantee;

}
