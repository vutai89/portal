package com.mcredit.model.object.audit;

public class PartnerMapping {
	private int partner_id;
	private String partner_name;

	public int getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(int partner_id) {
		this.partner_id = partner_id;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public PartnerMapping(int partner_id, String partner_name) {
		super();
		this.partner_id = partner_id;
		this.partner_name = partner_name;
	}

	public PartnerMapping() {
		super();
	}

}
