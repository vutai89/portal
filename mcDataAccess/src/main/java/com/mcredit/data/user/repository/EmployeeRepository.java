package com.mcredit.data.user.repository;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.data.user.entity.Users;

import org.hibernate.query.NativeQuery;

public class EmployeeRepository extends BaseRepository implements IRepository<Employee>, IUpsertRepository<Employee>, IAddRepository<Employee> {

	public EmployeeRepository(Session session) {
		super(session);
	}
	
	public List<Employee> listEmployee(String teamType, String teamGroup, String teamCode) {
		String strQuery = " select d.*, a.login_Id, a.id as user_id, ct.DESCRIPTION1 as departmentName from users a " + 
							" JOIN TEAM_MEMBER b on a.id = b.USER_ID " + 
							" JOIN TEAMS c on c.id = b.TEAM_ID " + 
							" left JOIN employees d on a.EMP_ID = d.id " +
							" left JOIN employee_link el on d.ID = el.EMP_ID " +
							" left JOIN CODE_TABLE ct on el.DEPARTMENT = ct.ID " +
							" where UPPER(c.TEAM_CODE) =:teamCode and UPPER(c.TEAM_GROUP) =:teamGroup and UPPER(TEAM_TYPE) =:teamType ";
                
                NativeQuery query = this.session.createNativeQuery(strQuery);
                query.setParameter("teamCode", teamCode.trim().toUpperCase()).setParameter("teamGroup", teamGroup.trim().toUpperCase()).setParameter("teamType", teamType.trim().toUpperCase());
        
		List lst =  query.list();
		
		 List<Employee> retList = new ArrayList<Employee>();
		 
		if (lst != null && !lst.isEmpty()) {
			for (Object o : lst) {
				retList.add(transformObject(o, Employee.class));
			}
		}
	       	
		return retList;
	}
	
	public int updateEmployee(Long empId, String hr_code, String email, String mobile_phone, String ext_phone) {
		
		String sqlString = "UPDATE EMPLOYEES " + 
				"    SET " + 
				"        hr_code = :hr_code, " + 
				"        email = :email, " + 
				"        mobile_phone = :mobile_phone, " + 
				"        ext_phone = :ext_phone " + 
				"    WHERE " + 
				"        id = :empId ";
		
		@SuppressWarnings("rawtypes")
		NativeQuery query = this.session.createNativeQuery(sqlString)
		.setParameter("hr_code", hr_code)
		.setParameter("email", email)
		.setParameter("mobile_phone", mobile_phone)
		.setParameter("ext_phone", ext_phone)
		.setParameter("empId", empId);
		
		return query.executeUpdate();
	}
	
	public Employee findById (Long id){
		return this.session.find(Employee.class, id);
	}

	public BigDecimal getSaleId(String empCode) {
		return (BigDecimal) this.session.getNamedNativeQuery("getSaleId").setParameter("emp_code", empCode).uniqueResult();
	}
	
	public String getSaleMobile(String empId) {
		return (String) this.session.getNamedNativeQuery("getSaleMobileBySaleId").setParameter("id", empId).uniqueResult();
	}

	public Object[] getEmpCodeEmpId(String loginId) {
		return (Object[]) this.session.getNamedNativeQuery("getEmpIdEmpCode").setParameter("loginId", loginId).getSingleResult();
	}
	
	@Override
	public void upsert(Employee item) {
		this.session.saveOrUpdate("Employees", item);
	}
	
	@Override
	public void add(Employee item) {
		this.session.save(item);
	}


}
