package com.mcredit.service;

import com.mcredit.sharedbiz.dto.UserDTO;

public interface IAuthorization {
	UserDTO authorize(String token) throws Exception; 
}
