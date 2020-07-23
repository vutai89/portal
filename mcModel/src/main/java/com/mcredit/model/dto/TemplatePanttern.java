package com.mcredit.model.dto;

public class TemplatePanttern {
	String Type;
	String Name;
	String Format;
	String Original;
	String Replacer;
	
	public String getOriginal() {
		return Original;
	}
	public void setOriginal(String original) {
		Original = original;
	}
	public String getReplacer() {
		return Replacer;
	}
	public void setReplacer(String replacer) {
		Replacer = replacer;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getFormat() {
		return Format;
	}
	public void setFormat(String format) {
		Format = format;
	}
}
