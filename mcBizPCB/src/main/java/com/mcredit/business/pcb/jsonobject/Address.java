
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Address {

    public Additional Additional;
    public List<Main> Main = null;

}
