package com.mcredit.data.warehouse.repository;


import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.jdbc.ReturningWork;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhCode;
import com.mcredit.model.object.Param;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhCodeRepository extends BaseRepository implements IAddRepository<WhCode>,IUpsertRepository<WhCode> {

	private String nameProcedure = "";
	private List<Param> paramProcedure = null;
	private String typeOutData = "";
	private CallableStatement cs = null;
	
	public WhCodeRepository(Session session) {
		super(session);
	}
	@Override
	public void add(WhCode item) {
		this.session.save(item);
	}

	@Override
	public void upsert(WhCode item) {
		this.session.saveOrUpdate("WhCode", item);
	}
	
	public int updateStatusWhCode(Long newStatus, String code) {
		return this.session.getNamedQuery("updateStatusWhCode")
				.setParameter("newStatus", newStatus)
				.setParameter("code", code)
				.executeUpdate();
	}

	public Object callFunctionReturnSingleRow(String name, final List<Param> param, String typeOut) {
	    	
        Object o = null;
        nameProcedure = name;
        paramProcedure = param;
        typeOutData = typeOut;
        
        try {
            o = this.session.doReturningWork(new ReturningWork<Object>() {
                @Override
                public Object execute(Connection conn) throws SQLException {
                    try {
                        String params = "";
                	if( param!=null && param.size()>0 ) {
	                	for( int j = 0; j < param.size() ; j++ ) {
	                		params += "?,";
	                	}
	                	params = params.substring(0, params.length() - 1);
                	}
                	
                	cs = conn.prepareCall(" { ? = call " + nameProcedure + " ("+params+") } ");
                	
                	if ("int".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(1, OracleTypes.NUMBER);
                    else if ("string".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(1, OracleTypes.VARCHAR);
                    else if ("date".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(1, OracleTypes.DATE);
                	
                    if (paramProcedure != null && !paramProcedure.isEmpty()) {
                        for (int k = 0; k < paramProcedure.size(); k++) {
                        	
                            Param param = paramProcedure.get(k);
                            
                            if ("int".equals(param.getTypeData()))
                                cs.setInt(k + 2, Integer.parseInt(param.getValue()));
                            else if ("long".equals(param.getTypeData()))
                                cs.setLong(k + 2, new Long(param.getValue()));
                            else if ("string".equals(param.getTypeData()))
                                cs.setString(k + 2, param.getValue());
                        }
                    }

                    cs.execute();
                    //conn.commit();
                    return cs.getObject(1);
                    } catch (Exception e) {
                    } finally {
                        try {
                            if (cs != null && !cs.isClosed()) {
                                cs.close();
                            }
                        } catch (SQLException e) {
                            throw new HibernateException("Error closing the CallableStatement: " + e.toString());
                        }
                    }
                    return null;
                }
            });
        } catch (HibernateException e) {
        	//throw new DataException(e);
        } 
        return o;
    }
	
	public void updateStatusBy(Long whCodeId, Long status) {
		String sqlQuery = ("update WH_CODE c set c.status = :status where c.id = :whCodeId ");
		Query<?> query = session.createNativeQuery(sqlQuery);
		query.setParameter("status", status).setParameter("whCodeId", whCodeId).executeUpdate();
	}
	
	public Long getIdByWHCode(String whCode) {
		String sqlString = "SELECT ID FROM WH_CODE WHERE CODE = :whCode";
		
		Query<?>  query = this.session.createNativeQuery(sqlString);
		query.setParameter("whCode", whCode);
		Object out = query.uniqueResult();
		
		if(out != null)
			return Long.valueOf(out.toString());
		return null;
	}

}
