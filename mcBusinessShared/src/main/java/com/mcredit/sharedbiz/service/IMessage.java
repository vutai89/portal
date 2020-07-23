package com.mcredit.sharedbiz.service;

import com.mcredit.sharedbiz.validation.ValidationException;

public interface IMessage {
	void send() throws ValidationException;
}
