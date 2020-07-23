package com.mcredit.data;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import com.mcredit.model.object.Param;
import com.mcredit.model.object.warehouse.WareHouseCodeTableCacheDTO;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;

import oracle.jdbc.OracleTypes;

public class BaseRepository {
	
	protected Session session;

	private String nameProcedure = "";
	private List<Param> paramProcedure = null;
	private String typeOutData = "";
	private CallableStatement cs = null;
	
	public BaseRepository(Session session) {
		this.session = session;
	}
	
	public <T> T transformObject(Object item, Class<T> input) {

		T result = null;
		try {
			result = input.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		if( result==null )
			return null;
		
		Field[] fields = null;

		Object[] o = null;

		fields = result.getClass().getDeclaredFields();

		o = (Object[]) item;

		int stt = 0;

		String fieldType = "";

		Object obj = null;

		for (Field field : fields) {

			if (stt >= o.length)
				break;

			field.setAccessible(true);

			if( "serialVersionUID".equals(field.getName()) )
				continue;
			
			fieldType = field.getType().toString();

			try {
				// System.out.println("type["+field.getType().toString()+"]" );

				obj = o[stt];

				if ("class java.lang.String".equals(fieldType)) {
					field.set(result, obj != null ? String.valueOf(obj) : "");
				} else if ("class java.lang.Long".equals(fieldType)) {
					field.set(result, obj != null ? Long.valueOf(obj.toString().trim()) : null);
				} else if ("class java.lang.Integer".equals(fieldType)) {
					field.set(result, obj != null ? Integer.valueOf(obj.toString().trim()) : null);
				} else if ("class java.math.BigDecimal".equals(fieldType)) {
					field.set(result, obj != null ? (BigDecimal) obj : null);
				} else if ("class java.util.Date".equals(fieldType)) {
					field.set(result, obj != null ? (Date) obj : null);
					
				} else if ("interface java.sql.Clob".equals(fieldType)) {
					field.set(result, obj != null ? (Clob) obj : null);
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			stt++;
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public <T> List transformList(List list, Class<T> input) {

		List<T> lstObject = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			try {
				lstObject.add(input.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		List<Object> result = new ArrayList<>();

		Object item = null;

		Field[] fields = null;

		Object[] o = null;

		for (int i = 0; i < list.size(); i++) {

			fields = lstObject.get(i).getClass().getDeclaredFields();

			item = list.get(i);

			o = (Object[]) item;

			int stt = 0;

			String fieldType = "";

			Object obj = null;

			for (Field field : fields) {

				if (stt >= o.length)
					break;

				field.setAccessible(true);

				if( "serialVersionUID".equals(field.getName()) )
					continue;
				
				fieldType = field.getType().toString();

				try {
					// System.out.println("type["+field.getType().toString()+"]" );

					obj = o[stt];

					if ("class java.lang.String".equals(fieldType)) {
						field.set(lstObject.get(i),
								obj != null ? String.valueOf(obj) : "");
					} else if ("class java.lang.Long".equals(fieldType)) {
						field.set(lstObject.get(i),
								obj != null ? Long.valueOf(obj.toString())
										: null);
					} else if ("class java.lang.Integer".equals(fieldType)) {
						field.set(lstObject.get(i),
								obj != null ? Integer.valueOf(obj.toString())
										: null);
					} else if ("class java.math.BigDecimal".equals(fieldType)) {
						field.set(lstObject.get(i),
								obj != null ? (BigDecimal) obj : null);
					} else if ("class java.util.Date".equals(fieldType)) {
						field.set(lstObject.get(i), obj != null ? (Date) obj
								: null);
					} else if ("interface java.sql.Clob".equals(fieldType)) {
						field.set(lstObject.get(i), obj != null ? (Clob) obj
								: null);
					}

				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}

				stt++;
			}
			result.add(lstObject.get(i));
		}
		return result;
	}
	
	public <T> void removeEntity(Class<T> entity, CriteriaBuilder builder, String key, Object value) {
		
		try {
			CriteriaDelete<T> criteria = builder.createCriteriaDelete(entity);
			Root<T> root = criteria.from(entity);
			
			criteria.where(builder.equal(root.get(key), value));
			
			this.session.createQuery(criteria).executeUpdate();
			
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}
	
	public void updateEntity(Object input, String entityName) {
		
		Object origin = null;
		try {
			Field field = input.getClass().getDeclaredField("id");
			field.setAccessible(true);
			origin = get(input.getClass(), (Long) field.get(input));
			
			System.out.println("Merge["+entityName+"] with ID["+(Long) field.get(input)+"]");
			
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (NoSuchFieldException e) {
			//e.printStackTrace();
		}
		
		if(origin == null)
			return;
		
		Field[] fields = input.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
				field.set(input, getNotNull(field.get(input), field.get(origin)));
			} catch (IllegalAccessException e) {
				//e.printStackTrace();
			}
        }
        
        this.session.merge(entityName, input);
	}
	
	public static <T> T getNotNull(T a, T b) {
		if( a==null )
			return b;
		return a;
	    //return b != null && a != null && !a.equals(b) ? a : b;
	}
	
	public final Object get(Class<?> entityClass, Long id) {
		try {
			return this.session.get(entityClass, id);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final Object get(Class<?> entityClass, Integer id) {
		try {
			return this.session.get(entityClass, id);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

    public Object callProcedureReturnSingleRow(String name, List<Param> param, String typeOut) {
    	
        Object o = null;
        nameProcedure = name;
        paramProcedure = param;
        typeOutData = typeOut;
        
        try {
            o = this.session.doReturningWork(new ReturningWork<Object>() {
                @Override
                public Object execute(Connection conn) throws SQLException {
                	
                    String query = "{call " + nameProcedure + "(";
                    if (paramProcedure != null && !paramProcedure.isEmpty()) {
                        for (int i = 0; i < paramProcedure.size(); i++) {
                            query += "?,";
                        }
                        query += "?)";
                    } else
                        query += "?)";
                    query += "}";
                    
                    cs = conn.prepareCall(query);
                    
                    int i = 0;
                    if (paramProcedure != null && !paramProcedure.isEmpty()) {
                    	int k = 0;
                        for (; k < paramProcedure.size(); k++) {
                            Param param = paramProcedure.get(k);
                            
                            if ("int".equals(param.getTypeData()))
                                cs.setInt(k + 1, Integer.parseInt(param.getValue()));
                            else if ("long".equals(param.getTypeData()))
                                cs.setLong(k + 1, new Long(param.getValue()));
                            else if ("string".equals(param.getTypeData()))
                                cs.setString(k + 1, param.getValue());
                        }
                    }

                    if ("int".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(i + 1, OracleTypes.NUMBER);
                    else if ("string".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(i + 1, OracleTypes.VARCHAR);
                    else if ("date".equalsIgnoreCase(typeOutData))
                        cs.registerOutParameter(i + 1, OracleTypes.DATE);

                    cs.execute();
                    //conn.commit();
                    return cs.getObject(i + 1);
                }
            });
        } catch (HibernateException e) {
        	//throw new DataException(e);
        }
        return o;
    }
    
    public WareHouseSeachObject transformWareHouseSeach(Object item,WareHouseCodeTableCacheDTO wareHouseCodeTableID) {
		 WareHouseSeachObject result = new WareHouseSeachObject();
		 HashMap<String , List<String>> objectMapCodeTabe =  result.objectMapCodeTabe();
		 
		Field[] fields =result.getClass().getDeclaredFields();
		Object[] o = (Object[]) item;

		int stt = 0;

		String fieldType = "";

		Object obj = null;

		for (Field field : fields) {

			if (stt >= o.length)
				break;

			field.setAccessible(true);

			if( "serialVersionUID".equals(field.getName()) )
				continue;
			
			fieldType = field.getType().toString();

			try {

				obj = o[stt];

				if ("class java.lang.String".equals(fieldType)) {
					field.set(result, obj != null ? String.valueOf(obj) : "");
				} else if ("class java.lang.Long".equals(fieldType)) {
					field.set(result, obj != null ? Long.valueOf(obj.toString().trim()) : null);
				} else if ("class java.lang.Integer".equals(fieldType)) {
					field.set(result, obj != null ? Integer.valueOf(obj.toString().trim()) : null);
				} else if ("class java.math.BigDecimal".equals(fieldType)) {
					field.set(result, obj != null ? (BigDecimal) obj : null);
				} else if ("class java.util.Date".equals(fieldType)) {
					field.set(result, obj != null ? (Date) obj : null);
				}

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			stt++;
		}
		
		for (Map.Entry<String, List<String>> entry : objectMapCodeTabe.entrySet()) {
			int i = 0;
			try {
				if (entry.getKey().equals("workFlowId")) {
				if(null != result.getClass().getField("workFlow").get(result) && !"".equals(result.getClass().getField("workFlow").get(result)) 
						&& wareHouseCodeTableID.getWorkFlowCodeTable().get(result.getClass().getField("workFlow").get(result)) != null ){
					result.getClass().getField("workFlowId").set(result, Long.valueOf(wareHouseCodeTableID.getWorkFlowCodeTable().get(result.getClass().getField("workFlow").get(result)).getId()));
					
					for (String ltsString : entry.getValue()) {
							if (i == 0)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getWorkFlowCodeTable().get(result.getClass().getField("workFlow").get(result)).getDescription1());
	
							if (i == 1)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getWorkFlowCodeTable().get(result.getClass().getField("workFlow").get(result)).getDescription2());
							i++;
						}
					}
				} 
				
				else if (entry.getKey().equals("productId")) {
					if(null != result.getClass().getField(entry.getKey()).get(result) && !"".equals(result.getClass().getField(entry.getKey()).get(result))
							&& wareHouseCodeTableID.getHashProduct().get(result.getClass().getField(entry.getKey()).get(result)) != null){
						for (String ltsString : entry.getValue()) {
							if (i == 0)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getHashProduct().get(result.getClass().getField(entry.getKey()).get(result)).getProductCode());
	
							if (i == 1)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getHashProduct().get(result.getClass().getField(entry.getKey()).get(result)).getProductName());
							i++;
						}
					}
				} 
				else if (entry.getKey().equals("indentityIssuePlace")) {
						if(null != result.getClass().getField(entry.getKey()).get(result) && !"".equals(result.getClass().getField(entry.getKey()).get(result))
								&& wareHouseCodeTableID.getHashIndentityIssuePlace().get(Long.valueOf(result.getClass().getField(entry.getKey()).get(result).toString())) != null){
							for (String ltsString : entry.getValue()) {
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getHashIndentityIssuePlace().get(Long.valueOf(result.getClass().getField(entry.getKey()).get(result).toString())).getDescription1());
							i++;
						}
					}
				} 
				
				else if (entry.getKey().equals("addressFull")) {
					String addressFull = (String) result.getClass().getField("address").get(result) ;
					for (String ltsString : entry.getValue()) {						
						if(null != result.getClass().getField(ltsString).get(result) && !"".equals(result.getClass().getField(ltsString).get(result))
								&& wareHouseCodeTableID.getHashCodeSeach().get(result.getClass().getField(ltsString).get(result)) != null){							
							addressFull = addressFull + " , " + wareHouseCodeTableID.getHashCodeSeach().get(result.getClass().getField(ltsString).get(result)).getDescription1();
						}
					}
					result.getClass().getField("addressFull").set(result,addressFull);
					
				} 
				
				else {
					if(null != result.getClass().getField(entry.getKey()).get(result) && !"".equals(result.getClass().getField(entry.getKey()).get(result))
							&& wareHouseCodeTableID.getHashCodeSeach().get(result.getClass().getField(entry.getKey()).get(result)) != null){
						for (String ltsString : entry.getValue()) {
							if (i == 0)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getHashCodeSeach().get(result.getClass().getField(entry.getKey()).get(result)).getCodeValue1());
							
							if (i == 1)
								result.getClass().getField(ltsString).set(result, wareHouseCodeTableID.getHashCodeSeach().get(result.getClass().getField(entry.getKey()).get(result)).getDescription1());
							i++;
						}
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
    
    /*public <T> T findByPredicate(Object item, Class<T> input){
    	CriteriaBuilder cb = this.session.getCriteriaBuilder();
		CriteriaQuery<T> cr = cb.createQuery(input);
		Root<T> root = cr.from(input);
		List<Predicate> lstPredicates = new ArrayList<>();
		lstPredicates.add(cb.equal(root.get("categoryId"),1));
		lstPredicates.add(cb.like(root.get("categoryId"),"1"));
		Predicate[] stringArray = lstPredicates.toArray(new Predicate[lstPredicates.size()]);
		cr.select(root).where(stringArray);
//		cr.select(root);
		Query query = this.session.createQuery(cr);
		List<T> results = query.getResultList();
    	return null;
    }*/
}
