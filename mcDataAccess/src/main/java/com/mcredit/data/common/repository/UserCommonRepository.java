package com.mcredit.data.common.repository;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.user.entity.Users;
import com.mcredit.util.StringUtils;

public class UserCommonRepository extends BaseRepository implements IRepository<Users> {

	public UserCommonRepository(Session session) {
		super(session);
	}

	public String getEmailByLoginId(String loginId) {
		String email = "";
		
		if (!StringUtils.isNullOrEmpty(loginId)) {
			String queryStr = "SELECT EMAIL FROM EMPLOYEES WHERE ID = (SELECT EMP_ID FROM USERS WHERE LOGIN_ID=:loginId)";
			@SuppressWarnings("rawtypes")
			NativeQuery query = this.session.createNativeQuery(queryStr);
			query.setParameter("loginId", loginId);
			email = (String) query.uniqueResult();
		}
		
		return email;
	}

}
