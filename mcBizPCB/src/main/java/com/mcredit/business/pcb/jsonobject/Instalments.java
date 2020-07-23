
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Instalments {

    public List<GrantedContract> GrantedContract = null;
    public ACInstAmounts ACInstAmounts;
    public GInstAmounts GInstAmounts;
    public Summary Summary;
    public List<NotGrantedContract> NotGrantedContract = null;

}
