package com.mcredit.model.object;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {

	private String gender;
	private int age;
	private Date birthDate;
	private String customerCAT;
	private float income;
	private float avgIncome;

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getCustomerCAT() {
		return customerCAT;
	}

	public void setCustomerCAT(String customerCAT) {
		this.customerCAT = customerCAT;
	}

	public float getIncome() {
		return income;
	}

	public void setIncome(float income) {
		this.income = income;
	}

	public float getAvgIncome() {
		return avgIncome;
	}

	public void setAvgIncome(float avgIncome) {
		this.avgIncome = avgIncome;
	}
	
}
