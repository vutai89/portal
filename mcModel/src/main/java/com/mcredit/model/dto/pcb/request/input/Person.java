package com.mcredit.model.dto.pcb.request.input;

public class Person {
	
	private String Name;
	private String IDCard;				// cmnd
	private String IDCard2;				// cmnd cu
	private String Gender;
	private String DateOfBirth;
	private Address Address;				// dia chi thuong tru
	private String CountryOfBirth;		// noi sinh
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public String getIDCard2() {
		return IDCard2;
	}

	public void setIDCard2(String iDCard2) {
		IDCard2 = iDCard2;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	public Address getAddress() {
		return Address;
	}

	public void setAddress(Address address) {
		Address = address;
	}

	public String getCountryOfBirth() {
		return CountryOfBirth;
	}

	public void setCountryOfBirth(String countryOfBirth) {
		CountryOfBirth = countryOfBirth;
	}

}
