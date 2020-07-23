package com.mcredit.data.user.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import com.mcredit.common.DataUtils;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.user.entity.EmployeeLink;
import com.mcredit.data.user.entity.Users;
import com.mcredit.model.dto.RoleDTO;
import com.mcredit.model.dto.assign.SearchUserDTO;
import com.mcredit.model.dto.assign.SearchUserTsaDTO;
import com.mcredit.model.dto.assign.UserSearchDTO;
import com.mcredit.model.dto.assign.UserSearchTsaDTO;
import com.mcredit.model.dto.assign.UsersDTO;
import com.mcredit.model.dto.common.ExtNumberDTO;
import com.mcredit.model.dto.telesales.TelesaleUser;
import com.mcredit.model.object.MenuFunction;
import com.mcredit.model.object.Sale;
import com.mcredit.model.object.ServicePermission;
import com.mcredit.model.object.UserRole;
import com.mcredit.util.StringUtils;

public class UsersRepository extends BaseRepository implements IUpsertRepository<Users> {
	
	public UsersRepository(Session session) {
		super(session);
	}

	public void update(Users item) {
		this.session.update(item);
	}

	public void upsert(Users item) {
		this.session.saveOrUpdate("Users", item);
	}

	public void remove(Users item) {
		this.session.delete("Users", item);
	}
	
	public List<Users> findAllUsers() {
		return this.session.createQuery("from Users", Users.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> findActiveUser() {
		return this.session.getNamedQuery("findActiveUser").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> findActiveEmployee() {
		
		Query<?> query = this.session.getNamedNativeQuery("findActiveEmployee");
		
		List<?> lst = query.list();
		
        if (lst != null && !lst.isEmpty())
            return transformList(lst, Users.class);
        
		return null;
	}
	
	public ExtNumberDTO getExtNumber(String adCode) {
		Object obj = this.session.getNamedNativeQuery("getExtNumber")
				.setParameter("loginId", adCode)
				.uniqueResult();
		
		if( obj!=null )
			return transformObject(obj, ExtNumberDTO.class);
		
		return new ExtNumberDTO("", "");
	}
	
	public Users findUserById(Long userId) {
		return this.session.createQuery("from Users where id= :id", Users.class).setParameter("id", userId).getSingleResult();
	}

	public Users getUsersBySaleCode(String saleCode) {
		Users item = null;
		List<?> lst = this.session.createQuery("from Users where saleCode = :saleCode", Users.class).setParameter("saleCode", saleCode!=null?saleCode.trim():"").list();
		if( lst!=null && lst.size()>0 )
			item = (Users) lst.get(0);
		return item;
	}

	public Users findUserByLoginId(String currentUsername) {

		List<?> ojb = this.session.getNamedQuery("findUserByLoginId").setParameter("currentUsername",currentUsername).getResultList();
		if(ojb != null && ojb.size()>0)
			return (Users) ojb.get(0) ;
		return null;
	}
	
	public EmployeeLink findEmpBySaleCode(String saleCode) {
		EmployeeLink item = null;
        List<?> lst = this.session.createQuery("from EmployeeLink where empCode = :saleCode", EmployeeLink.class)
                                .setParameter("saleCode", saleCode!=null?saleCode.trim():"")
                                .list();
        if( lst!=null && lst.size()>0 )
            item = (EmployeeLink) lst.get(0);
        return item;

	}

	public List<MenuFunction> findUserMenu(String loginId) {
        @SuppressWarnings("unchecked")
		List<Object[]> objList = this.session.getNamedNativeQuery("findUserMenu")
                                .setParameter("loginId", loginId)
                                .getResultList();
        List<MenuFunction> retList = new ArrayList<MenuFunction>();
        for(Object[] objects : objList) {
        	MenuFunction mf = new MenuFunction();
            mf = (MenuFunction) DataUtils.bindingData(mf, objects);
            retList.add(mf);
        }
        return retList;
	}

	public List<ServicePermission> findUserService(String loginId) {
        @SuppressWarnings("unchecked")
		List<Object[]> objList = this.session.getNamedNativeQuery("findUserService")
                                .setParameter("loginId", loginId)
                                .getResultList();
        List<ServicePermission> retList = new ArrayList<ServicePermission>();
        for(Object[] objects : objList) {
        	ServicePermission sp = new ServicePermission();
            sp = (ServicePermission) DataUtils.bindingData(sp, objects);
            retList.add(sp);
        }
        return retList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Sale> findSaleByManager(Long empId) {
		
		List<Sale> result = new ArrayList<>();
		
		/*String sql =  " select el.EMP_CODE as saleCoce, e.FULL_NAME as saleName, ct.CODE_VALUE1 as position " +
						" from USERS u " +
						" inner join EMPLOYEE_LINK el on u.EMP_ID = el.EMP_ID " +
						" inner join CODE_TABLE ct on (el.EMP_POSITION = ct.ID and el.EMP_POSITION in ( " +
						"    (select ID from CODE_TABLE where CODE_GROUP='EMPL' and CATEGORY='EM_POS_TS' and CODE_VALUE1='BDS'), " + 
						"    (select ID from CODE_TABLE where CODE_GROUP='EMPL' and CATEGORY='EM_POS_TS' and CODE_VALUE1='DSA'), " +
						"    (select ID from CODE_TABLE where CODE_GROUP='EMPL' and CATEGORY='EM_POS_TS' and CODE_VALUE1='TSA') " +
						" )) " +
						" inner join EMPLOYEES e on el.EMP_ID = e.ID " +
						" where el.EMP_ID = :empId or el.MANAGER_ID = :empId " +
						" order by el.EMP_CODE ";*/
		
		String sql =  " select u.LOGIN_ID as loginId, el.EMP_CODE as saleCoce, e.FULL_NAME as saleName, ct.CODE_VALUE1 as position " +
					" from USERS u " +
					" inner join EMPLOYEE_LINK el on u.EMP_ID = el.EMP_ID " +
					" inner join CODE_TABLE ct on el.EMP_POSITION = ct.ID " +
					" inner join EMPLOYEES e on el.EMP_ID = e.ID " +
					" where el.STATUS = 'A' and (el.EMP_ID = :empId or el.MANAGER_ID = :empId) " +
					" order by el.EMP_CODE ";
		
		List lst = this.session.createNativeQuery(sql)
				.setParameter("empId", empId)
				.list();
		
		if (lst != null && lst.size() > 0)
			result =  transformList(lst, Sale.class);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<TelesaleUser> findTsaList(boolean superVisor, String ntb, String asm, String xsm, boolean teamLead, Long currentUserId) {
		
		List<TelesaleUser> result = new ArrayList<>();
		
		String sql =  "";
		
		if( teamLead )
			sql = " select u.ID as id, u.LOGIN_ID as loginId " +
					  " from USERS u  " +
					  " inner join TEAM_MEMBER tm on u.ID = tm.USER_ID " +
					  " inner join TEAMS t on t.ID = tm.TEAM_ID " +
					  " where t.MANAGER_ID = :teamLeadId " +
					  " order by u.LOGIN_ID ";
		else { // SUP
			
			String filterGroup = "";
			
			if( !"".equals(asm) && !"".equals(xsm) && !"".equals(ntb) )
				filterGroup = " ('LG', 'XS', 'NTB') ";
			else if( !"".equals(asm) && !"".equals(xsm) )
				filterGroup = " ('LG', 'XS') ";
			else if( !"".equals(asm) && !"".equals(ntb) )
				filterGroup = " ('LG', 'NTB') ";
			else if( !"".equals(xsm) && !"".equals(ntb) )
				filterGroup = " ('XS', 'NTB') ";
			else if( !"".equals(asm) )
				filterGroup = " ('LG') ";
			else if( !"".equals(xsm) )
				filterGroup = " ('XS') ";
			else // Only NTB rights
				filterGroup = " ('NTB') ";
			
			sql = " select u.ID as id, u.LOGIN_ID as loginId " +
				  " from USERS u " +
				  " inner join TEAM_MEMBER tm on u.ID = tm.USER_ID " +
				  " inner join TEAMS t on t.ID = tm.TEAM_ID " +
				  " where t.TEAM_GROUP in " + filterGroup +
				  " order by u.LOGIN_ID ";
		}
		
		@SuppressWarnings("rawtypes")
		Query query = this.session.createNativeQuery(sql);
		if( teamLead )
			query.setParameter("teamLeadId", currentUserId);
		
		@SuppressWarnings("rawtypes")
		List lst = query.list();
		
		if (lst != null && !lst.isEmpty() )
			result =  transformList(lst, TelesaleUser.class);
		
		return result;
	}
	
	public List<UserRole> getUserRoles() {
		
		List<UserRole> result = new ArrayList<>();
		
		@SuppressWarnings("unchecked")
		List<Object[]> lst = this.session.getNamedNativeQuery("getUserRoles").list();
		
		if (lst != null && lst.size() > 0) {
			for( Object[] objs : lst ) {
				result.add(new UserRole(String.valueOf(objs[0]), String.valueOf(objs[1])));
			}
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<RoleDTO> findRoleCodeLst(String loginId) {
		
		List<RoleDTO> result = new ArrayList<>();
		
		String sql =  " select distinct r.ROLE_CODE as roleCode " +
					" from USERS u " +
					" inner join USERS_ROLE_MAPPING urm on u.ID = urm.USER_ID " +
					" inner join ROLES r on urm.OBJECT_ID = r.ID " +
					" where urm.OBJECT_TYPE = 'R' and u.LOGIN_ID = :loginId ";
		
		List lst = this.session.createNativeQuery(sql)
				.setParameter("loginId", loginId)
				.list();
		
		if (lst != null && lst.size() > 0) {
			for( Object obj : lst ) {
				result.add(new RoleDTO(String.valueOf(obj)));
			}
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> findUserByLoginId(String[] loginId) {
		return (List<Users>) this.session.getNamedNativeQuery("findUserById")
				.setParameterList("loginId", loginId)
				.addEntity(Users.class)
				.list();
	}
	
	public Long countUsers(SearchUserDTO searchDTO) {
		boolean isSearch = !StringUtils.isNullOrEmpty(searchDTO.getKeyword().trim());
		
		String sqlString = "SELECT count(id) count FROM USERS where 1=1";
		if (isSearch) {
			sqlString += "AND (LOWER(LOGIN_ID) = :keyword)";
		}
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString);
		if (isSearch) {
//			String keywordSearch = "%" + searchDTO.getKeyword().toLowerCase() + "%";
			query.setParameter("keyword", searchDTO.getKeyword().toLowerCase());
		}
		
		BigDecimal count =  (BigDecimal) query.getSingleResult();
		return count.longValue();
		
	}
	
	public List<UserSearchDTO> searchUsers(SearchUserDTO searchDTO) {
		
		Integer pageNumber = searchDTO.getPageNumber();
		Integer pageSize = searchDTO.getPageSize();
		
		Integer maxNumber = (pageNumber * pageSize) + 1;
		Integer minNumber = ((pageNumber - 1) * pageSize) + 1;
		String sqlString = "SELECT * FROM ( SELECT a.*, rownum rn FROM (" + 
				"    SELECT " + 
				"        u.id, " + 
				"        u.login_id, " + 
				"        u.USR_FULL_NAME, " + 
				"        emp.HR_CODE, " + 
				"        ct.DESCRIPTION1 USER_TYPE, " + 
				"        emp.EMAIL, " + 
				"        emp.MOBILE_PHONE, " + 
				"        emp.EXT_PHONE, " + 
				"        up.DEVICE_NAME, " + 
				"        u.record_status, " + 
				"        u.CREATED_DATE, " + 
				"        u.LAST_UPDATED_DATE, " + 
				"        u.START_EFF_DATE, " + 
				"        u.END_EFF_DATE, " + 
				"        u.USR_TYPE, " + 
				"        eum.username, " +
				"        up.external_user_mapping_id " +
				"    FROM users u " + 
				"    LEFT JOIN EMPLOYEES emp ON emp.id = u.emp_id " + 
				"    LEFT JOIN CODE_TABLE ct ON ct.id = u.USR_TYPE " + 
				"    LEFT JOIN users_profiles up ON up.USER_ID = u.id " + 
				"    LEFT JOIN external_user_mapping eum ON eum.id = up.external_user_mapping_id " +
				"    WHERE 1 = 1 "; 
		
		boolean isSearch = !StringUtils.isNullOrEmpty(searchDTO.getKeyword().trim());
		if (isSearch) {
			sqlString += " AND (LOWER(u.LOGIN_ID) = :keyword)";
		}
		
		sqlString += " ORDER BY CREATED_DATE DESC) a WHERE rownum < :maxNumber ) WHERE rn >= :minNumber";
//		sqlString += " AND rownum < :maxNumber" +  ") WHERE rn >= :minNumber";

		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString);
		
		if (isSearch) {
//			String keywordSearch = "%" + searchDTO.getKeyword().toLowerCase() + "%";
			query.setParameter("keyword", searchDTO.getKeyword().toLowerCase());
		}
		
		query.setParameter("maxNumber", maxNumber);
		query.setParameter("minNumber", minNumber);
		
		@SuppressWarnings("rawtypes")
		List lst = query.list();
		@SuppressWarnings("unchecked")
		List<UserSearchDTO> users = transformList(lst, UserSearchDTO.class);
		return users;
	}
	
	public int changeStatusUser(Long userId, boolean isActive) {
		String status = isActive ? "A" : "C"; 
		String sqlString = "UPDATE users SET record_status = :status WHERE id = :userId";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString).setParameter("status", status).setParameter("userId", userId);
		
		return query.executeUpdate();
		
	}
	
	public int updateUser(UserSearchDTO userSearchDTO, String emp_id) {
		
		String sqlString = "UPDATE users " + 
				" SET " + 
				"    USR_FULL_NAME = :user_full_name, " + 
				"    record_status = :record_status, " + 
				"    status = :record_status, " + 
				"    LAST_UPDATED_DATE = :last_update_date, " +
				"    EMP_ID = :emp_id, " +
				"    START_EFF_DATE = TO_DATE(:start_eff_date, 'dd/mm/yyyy'), " + 
				"    END_EFF_DATE = TO_DATE(:end_eff_date, 'dd/mm/yyyy') ";
		
		if (null != userSearchDTO.getUserTypeId() || !userSearchDTO.getUserType().equals("")) {
			sqlString += "	, USR_TYPE = :usr_type ";
		}
		
		sqlString += " WHERE " + 
				"    login_id = :login_id ";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString)
		.setParameter("login_id", userSearchDTO.getLoginId())
		.setParameter("user_full_name", userSearchDTO.getUserFullName())
		.setParameter("record_status", userSearchDTO.getUserStatus())
		.setParameter("last_update_date", new Date())
		.setParameter("emp_id", Long.parseLong(emp_id))
		.setParameter("start_eff_date", userSearchDTO.getStartEffDate())
		.setParameter("end_eff_date", userSearchDTO.getEndEffDate());
		
		if (null != userSearchDTO.getUserTypeId() || !userSearchDTO.getUserType().equals("")) {
			query.setParameter("usr_type", userSearchDTO.getUserTypeId());
		}
			
		return query.executeUpdate();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<UserSearchTsaDTO> searchUsersTsa(SearchUserTsaDTO searchTsaDTO) {
		Integer pageNumber = searchTsaDTO.getPageNumber();
		Integer pageSize = searchTsaDTO.getPageSize();
		Integer maxNumber = (pageNumber * pageSize) + 1;
		Integer minNumber = ((pageNumber - 1) * pageSize) + 1;
		
		String sql =  "";
		sql = "SELECT * FROM ( SELECT a.*, rownum rn FROM (" + 
			"    SELECT " + 
			"        u.id, " + 
			"        u.login_id, " + 
			"        u.usr_full_name, " + 
			"        emp.hr_code, " + 
			"        ct.description1, " + 
			"        emp.email, " + 
			"        emp.mobile_phone, " + 
			"        emp.ext_phone, " + 
			"        u.record_status, " + 
			"        t.team_name " +
			"    FROM USERS u " + 
			"    LEFT JOIN TEAM_MEMBER tm ON u.id = tm.user_id " + 
			"    LEFT JOIN TEAMS t ON t.id = tm.team_id " + 
			"    LEFT JOIN EMPLOYEES emp ON emp.id = u.emp_id " + 
			"    LEFT JOIN CODE_TABLE ct ON ct.id = u.usr_type " + 
			" 	 INNER JOIN USERS_ROLE_MAPPING urm ON u.id = urm.user_id " + 
			" 	 INNER JOIN ROLES r ON urm.object_id = r.id " + 
			"	 WHERE r.ROLE_CODE = 'TS_TSA' AND urm.object_type = 'R' ";
		
		boolean isSearch = !StringUtils.isNullOrEmpty(searchTsaDTO.getKeyword().trim());
		if (isSearch) {
			sql += " AND (LOWER(u.LOGIN_ID) = :keyword)";
		}
		
		sql += " ORDER BY u.created_date DESC) a WHERE rownum < :maxNumber ) WHERE rn >= :minNumber";
		
		@SuppressWarnings("rawtypes")
		Query query = this.session.createNativeQuery(sql);
			
		if (isSearch) {
			query.setParameter("keyword", searchTsaDTO.getKeyword().toLowerCase());
		}
		
		query.setParameter("maxNumber", maxNumber);
		query.setParameter("minNumber", minNumber);
		
		@SuppressWarnings("rawtypes")
		List lst = query.list();
		@SuppressWarnings("unchecked")
		List<UserSearchTsaDTO> users = transformList(lst, UserSearchTsaDTO.class);
		return users;
	}
	
	public Long countUsersTsa(SearchUserTsaDTO searchUserTsaDTO) {
		boolean isSearch = !StringUtils.isNullOrEmpty(searchUserTsaDTO.getKeyword().trim());
		
		String sqlString = "SELECT count(*) FROM users u " +
						"LEFT JOIN TEAM_MEMBER tm on u.id = tm.user_id " +
						"LEFT JOIN TEAMS t on t.id = tm.team_id " +
						"LEFT JOIN EMPLOYEES emp on emp.id = u.emp_id " + 
						"LEFT JOIN CODE_TABLE ct on ct.id = u.usr_type " +
						"INNER JOIN USERS_ROLE_MAPPING urm ON u.id = urm.user_id " + 
						"INNER JOIN ROLES r ON urm.object_id = r.id " + 
						"WHERE r.ROLE_CODE = 'TS_TSA' AND urm.object_type = 'R' ";
		if (isSearch) {
			sqlString += "AND (LOWER(u.LOGIN_ID) = :keyword)";
		}
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString);
		if (isSearch) {
			query.setParameter("keyword", searchUserTsaDTO.getKeyword().toLowerCase());
		}
		
		BigDecimal count =  (BigDecimal) query.getSingleResult();
		return count.longValue();
		
	}
	
}









