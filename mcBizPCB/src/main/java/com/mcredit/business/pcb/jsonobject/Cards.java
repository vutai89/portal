
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Cards {

    public ACCardAmounts ACCardAmounts;
    public List<GrantedContract> GrantedContract;
    public Summary Summary;
    public GCardAmounts GCardAmounts;
    public List<NotGrantedContract> NotGrantedContract;

}
