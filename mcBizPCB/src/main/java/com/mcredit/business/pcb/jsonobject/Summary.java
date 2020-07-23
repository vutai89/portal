
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Summary {

    public Integer NumberOfRefused;
    public Integer NumberOfRenounced;
    public Integer NumberOfLiving;
    public Integer NumberOfTerminated;
    public Integer NumberOfRequested;

}
