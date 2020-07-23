package com.mcredit.sharedbiz;

public class BusinessException extends Exception {

	public BusinessException() {
		super();
	}
	
	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(Exception ex) {
		super(ex);
	}
}
