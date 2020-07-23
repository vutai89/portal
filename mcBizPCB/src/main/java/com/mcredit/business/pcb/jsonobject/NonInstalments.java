
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class NonInstalments {

    public List<GrantedContract> GrantedContract;
    public GNoInstAmounts GNoInstAmounts;
    public Summary Summary;
    public ACNoInstAmounts ACNoInstAmounts;
    public List<NotGrantedContract> NotGrantedContract;

}
