
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class NotGrantedContract {

    public String Role;
    public String TypeOfFinancing;
    public Amounts Amounts;
    public String ContractPhase;
    public String EncryptedFICode;
    public String ReferenceNumber;
    public String FIContractCode;
    public String CBContractCode;

}
