package com.mcredit.data.audit.entity;

public class PartnerMapping {
	private Long id;
	private String partner_name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public PartnerMapping(Long id, String partner_name) {
		super();
		this.id = id;
		this.partner_name = partner_name;
	}

	public PartnerMapping() {
		super();
	}

}
