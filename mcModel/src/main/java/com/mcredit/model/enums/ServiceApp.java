package com.mcredit.model.enums;

public enum ServiceApp {
	
	MC_LOS_SERVICE("MC_LOS_SERVICE"),
	MC_WEBSOCKET("MC_WEBSOCKET"),
	MC_SERVICE("MC_SERVICE");
	
	private final String value;

	ServiceApp(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
	
	public static ServiceApp from(String text) {
        for (ServiceApp b : ServiceApp.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
