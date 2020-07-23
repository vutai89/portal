
package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Person {

	public String DateOfBirth;
	public String IDCard;
	public String IDCard2;
	public Address Address;
	public List<Reference> Reference = null;
	public String TIN;
	public String Gender;
	public List<Document> Document = null;
//	public HistoricalAddress HistoricalAddress;
	public String Name;
	public String PlaceOfBirth;
	public String CountryOfBirth;

}
