package com.mcredit.restcore.enums;

public enum AcceptType {
	NotSet("NotSet"),
	Json("application/json"),
	Xml("application/xml"),
	ScoresApi("application/scores.api.v3");
	
	String _value;
	AcceptType(String p) {
		_value = p;
    }
    
	public String getDescription() {
      return _value;
    }
}
