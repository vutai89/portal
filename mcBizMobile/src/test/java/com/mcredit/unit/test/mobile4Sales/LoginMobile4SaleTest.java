package com.mcredit.unit.test.mobile4Sales;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mcredit.business.mobile.manager.MobileManager;
import com.mcredit.data.HibernateBase;
import com.mcredit.data.mobile.entity.UsersProfiles;
import com.mcredit.data.mobile.repository.UsersProfilesRepository;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.mobile.dto.LoginMobileRequestDTO;
import com.mcredit.model.object.mobile.dto.LoginMobileResponseDTO;
import com.mcredit.sharedbiz.manager.AuthorizationManager;
import com.mcredit.sharedbiz.validation.AuthorizationException;
import com.mcredit.unit.test.mobile.manager.MobileManagerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LoginMobile4SaleTest {
//	AuthorizationManager manager = new AuthorizationManager();
//	@BeforeAll
//    static void setUp() throws Exception {
//		System.out.println("Setting it up!");
//    }
//	
//	@Test
//    public void testLoginSuccess01() throws Exception {
//		System.out.println("---------->   " + "Test111111111111");
//		LoginMobileRequestDTO loginMobileRequestDTOSuccess01  = initLoginMobileRequestDTO("imei", "notificationId", "ANDROID", "123Abc@", "sonhv.ho");
//    	LoginMobileResponseDTO loginMobileResponseDTO = manager.authorizedMobile(loginMobileRequestDTOSuccess01);
//    	assertEquals("sonhv.ho", loginMobileResponseDTO.getUserId());
//    	assertEquals(36, loginMobileResponseDTO.getToken().length());
//    	assertFalse(loginMobileResponseDTO.getRoleCodeLst().isEmpty());
//    }
//	
//	
////	The first time login, store imei
//	@Test
//    public void testLoginSuccess02() throws Exception {
//		LoginMobileRequestDTO loginMobileRequestDTOSuccess02 = initLoginMobileRequestDTO("imei123", "notificationId", "ANDROID", "123Abc@", "binhnt.ho");
//    	LoginMobileResponseDTO loginMobileResponseDTO = manager.authorizedMobile(loginMobileRequestDTOSuccess02);
//    	assertEquals("binhnt.ho", loginMobileResponseDTO.getUserId());
//    	assertEquals(36, loginMobileResponseDTO.getToken().length());
//    	assertFalse(loginMobileResponseDTO.getRoleCodeLst().isEmpty());
//    }
//    
//	//This account is not in Mobile for Sale System
//    @Test
//    public void testLoginFail01() throws Exception {
//    	try {
//    		LoginMobileRequestDTO loginMobileRequestDTOFail01 = initLoginMobileRequestDTO("imei", "notificationId", "ANDROID", "123Abc@", "phaonx.ho");
//    		manager.authorizedMobile(loginMobileRequestDTOFail01);
//    	} catch(AuthorizationException e){
//    		assertEquals(401, e.getHttpStatusCode());
//    		assertEquals("This account is not in Mobile for Sale System", e.getMessage());
//    	}
//    }
//    
//    //This Imei is invalid
//    @Test
//    public void testLoginFail02() throws Exception {
//    	try {
//    		LoginMobileRequestDTO loginMobileRequestDTOFail02 = initLoginMobileRequestDTO("imei1", "notificationId", "ANDROID", "123Abc@", "sonhv.ho");
//    		manager.authorizedMobile(loginMobileRequestDTOFail02);
//    	} catch(AuthorizationException e){
//    		assertEquals(401, e.getHttpStatusCode());
//    		assertEquals("This Imei is invalid", e.getMessage());
//    	}
//    }
//    
//	@SuppressWarnings("resource")
//	@AfterAll
//    static void tearDown() throws Exception {
//        System.out.println("Running: tearDown");
//        
//        //Set imei is null for user binhnt.ho
////        HibernateBase hibernateBase = new HibernateBase(SessionType.JTA);
////		Session session = hibernateBase.getSession();
////		session.getTransaction().begin();
////		UsersProfilesRepository usersProfilesRepository = new UsersProfilesRepository(session);
////		UsersProfiles usersProfiles = usersProfilesRepository.getByUserId(54121L);
////		usersProfiles.setMobileImei(null);
////		usersProfilesRepository.update(usersProfiles);
////		session.getTransaction().commit();
//        
//        MobileManagerTest manager = new MobileManagerTest();
//        Boolean result = manager.setNullForImei("binhnt.ho");
//        System.out.println("---------->   " + result);
//    }
//    
//    public LoginMobileRequestDTO initLoginMobileRequestDTO(String imei, String notificationId, String osType, String password, String username) {
//		LoginMobileRequestDTO loginMobileRequestDTO = new LoginMobileRequestDTO();
//		loginMobileRequestDTO.setImei(imei);
//		loginMobileRequestDTO.setNotificationId(notificationId);
//		loginMobileRequestDTO.setOsType(osType);
//		loginMobileRequestDTO.setPassword(password);
//		loginMobileRequestDTO.setUsername(username);
//		
//    	return loginMobileRequestDTO;
//    }
}
