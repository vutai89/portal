
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Contract {

    public Instalments Instalments;
    public NonInstalments NonInstalments;
    public Cards Cards;
    public ScoreProfile ScoreProfile;

}
