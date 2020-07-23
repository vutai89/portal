
package com.mcredit.business.pcb.jsonobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public class Document {

    public Integer Type;
    public String Number;
    public String CountryIssued;
    public String DateIssued;

}
