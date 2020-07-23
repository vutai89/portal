package com.mcredit.sharedbiz.manager;

import java.util.List;

import com.mcredit.model.dto.common.ExtNumberDTO;
import com.mcredit.model.dto.telesales.TelesaleUser;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.model.object.Sale;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.factory.UserFactory;

public class UserManager extends BaseManager {
	
	public List<UserDTO> getActiveUsers() throws Exception {
		return this.tryCatch(()->{
			return UserFactory.getUserAgg(this.uok).getActiveUsers();
		});
	}
	
	public boolean isServiceAllow(String loginId, ServiceName serviceName) throws Exception {
		return this.tryCatch(()->{
			return UserFactory.getUserAgg(this.uok).isServiceAllow(loginId, serviceName);
		});
	}
 
	public List<Sale> findSaleByManager(Long empId) throws Exception {
		return this.tryCatch(()->{
			return UserFactory.getUserAgg(this.uok).findSaleByManager(empId);
		});
	}
	
	public List<TelesaleUser> findTsaList(boolean superVisor, String ntb, String asm, String xsm, boolean teamLead, Long currentUserId) throws Exception {
		return this.tryCatch(()->{
			return UserFactory.getUserAgg(this.uok).findTsaList(superVisor, ntb, asm, xsm, teamLead, currentUserId);
		});
	}
	
	public ExtNumberDTO getExtNumber(String adCode) throws Exception {
		return this.tryCatch(()->{
			return UserFactory.getUserAgg(this.uok).getExtNumber(adCode);
		});
	}
	
	/*public static void main(String[] args) throws ValidationException {
		UnitOfWork uok = new UnitOfWork();
		uok.start();
		
		ExtNumberDTO obj = UserFactory.getUserAgg(uok).getExtNumber("anhdv.ho");
		
		System.out.println( obj.getExtNumber() );
		
		uok.commit();
	}*/
}
