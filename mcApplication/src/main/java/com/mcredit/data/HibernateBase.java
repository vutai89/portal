package com.mcredit.data;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

public class HibernateBase {

	private SessionFactory sessionFactory;
	private Transaction transaction;
//	private boolean isOpen = false;
//	private Session session;

	public SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			// throw new
			// IllegalStateException("SessionFactory has not been set on DAO before usage");
			this.sessionFactory = initSessionFactory();
		}

		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory initSessionFactory() {
		return HibernateUtility.getSessionFactory();
	}

	public Session getSession() {
		// if(this.transaction == null) {
		// this.transaction =
		// this.this.getSessionFactory().getCurrentSession().beginTransaction();
		// }
//		if(!isOpen) {
//			isOpen = true;
//			this.getSessionFactory().getCurrentSession() = this.getSessionFactory().openSession();
//			return this.getSessionFactory().getCurrentSession();
//		}
		return this.getSessionFactory().getCurrentSession();
	}

/*	public Transaction getTransaction() {
		this.transaction = this.this.getSessionFactory().getCurrentSession()
				.beginTransaction();
		MCLogger.info(this, "isConnected=" + this.this.getSessionFactory().getCurrentSession().isConnected() + 
				";isOpen=" + this.this.getSessionFactory().getCurrentSession().isOpen());
		return this.transaction;
	}
*/
	public void startTransaction() {
//		if(!isOpen) {
//			this.getSession();
//			this.transaction = this.getSessionFactory().getCurrentSession().beginTransaction();
//		} else {
//			this.transaction = this.getSessionFactory().getCurrentSession().beginTransaction();
//		}
//		return this.getSessionFactory().getCurrentSession();
		this.transaction = this.getSessionFactory().getCurrentSession().beginTransaction();
	}

	public void endTransaction() {
//		if(isOpen) {
//			isOpen = false;
//			this.transaction.commit();
//			this.getSessionFactory().getCurrentSession().close();
//		}
		if(this.transaction != null) {
			this.transaction.commit();
		}
	}

	public void rollbackTransaction() {
//		if(isOpen) {
//			isOpen = false;
//			this.transaction.rollback();
//			this.getSessionFactory().getCurrentSession().close();
//		}
		if(this.transaction != null) {
			this.transaction.rollback();
		}
	}

	public final void evict(Object entity) {
		this.getSessionFactory().getCurrentSession().evict(entity);
	}

	public final void clear() {
		this.getSessionFactory().getCurrentSession().clear();
	}

	public final void flush() {
		this.getSessionFactory().getCurrentSession().flush();
	}

	public final Object get(Class<?> entityClass, long id) {
		try {
			return this.getSessionFactory().getCurrentSession().get(entityClass,
					new Long(id));
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final Object load(Class<?> entityClass, long id) {
		try {
			return this.getSessionFactory().getCurrentSession().load(entityClass,
					new Long(id));
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final Object load(Class<?> entityClass, int id) {
		try {
			return this.getSessionFactory().getCurrentSession().load(entityClass,
					new Integer(id));
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public List loadAll(Class<?> entityClass) {
		try {
			return this.getSessionFactory().getCurrentSession()
					.createQuery("from " + entityClass.getName()).list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final Object save(Object entity) {
		try {
			return this.getSessionFactory().getCurrentSession().save(entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final Object save(String entityName, Object entity) {
		try {
			return this.getSessionFactory().getCurrentSession().save(entityName,
					entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void update(Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().update(entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void update(String entityName, Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().update(entityName, entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void saveOrUpdate(Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().saveOrUpdate(entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void saveOrUpdate(String entityName, Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().saveOrUpdate(entityName,
					entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void saveOrUpdateAll(Collection collection) {
		try {
			for (Object entity : collection) {
				this.getSessionFactory().getCurrentSession().saveOrUpdate(entity);
			}
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void merge(Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().merge(entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void delete(Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().delete(entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final void delete(String entityName, Object entity) {
		try {
			this.getSessionFactory().getCurrentSession().delete(entityName, entity);
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByNamedQuery(String queryName, String paramName,
			Object value, boolean enableCache) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			query.setParameter(paramName, value);
			query.setCacheable(enableCache);
			return query.list();

		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByNamedQuery(String queryName, String paramName,
			Object value) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			query.setParameter(paramName, value);
			return query.list();

		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByNamedQuery(String queryName, String[] paramNames,
			Object[] values) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(query, paramNames[i], values[i]);
				}
			}

			return query.list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public List findByNamedQueryWithFlush(String queryName,
			String[] paramNames, Object[] values) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(query, paramNames[i], values[i]);
				}
			}

			List result = query.list();
			this.clear();
			this.flush();

			return result;

		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByNamedQuery(String queryName) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			return query.list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByCriteria(DetachedCriteria criteria) {
		try {
			return criteria.getExecutableCriteria(
					this.getSessionFactory().getCurrentSession()).list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByCriteria(DetachedCriteria criteria, int maxResults) {
		try {
			return criteria
					.getExecutableCriteria(
							this.getSessionFactory().getCurrentSession())
					.setMaxResults(maxResults).list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByCriteria(Criteria criteria) {
		try {
			return criteria.list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByCriteria(Class<?> entityClass,
			Criterion[] criterions) {
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession()
					.createCriteria(entityClass);
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}

			return criteria.list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	public final List findByCriteria(Class<?> entityClass,
			Criterion[] criterions, Order[] orders) {
		try {
			Criteria criteria = this.getSessionFactory().getCurrentSession()
					.createCriteria(entityClass);
			for (Criterion criterion : criterions) {
				criteria.add(criterion);
			}

			if (orders != null) {
				for (Order order : orders) {
					criteria.addOrder(order);
				}
			}

			return criteria.list();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	/**
	 * Returns list of objects based on the HQL statement. You may specify the
	 * starting index of the records you wish to fetch and also the maximum
	 * number of records you wish to fetch.
	 * 
	 * @param queryName
	 *            HQL statement
	 * @param paramName
	 *            Array of parameter names in your HQL statement
	 * @param value
	 *            Corresponding parameter values in your HQL statement
	 * @param startIndex
	 *            Rownum in database. Index starts with zero for first rownum
	 * @param maxResults
	 *            Maximum number of results expected to be returned from the
	 *            query
	 * @return List
	 */
	public final List findByNamedQuery(String queryName, String paramName,
			Object value, int startIndex, int maxResults) {
		return findByNamedQuery(queryName, new String[] { paramName },
				new Object[] { value }, startIndex, maxResults);
	}

	/**
	 * Returns list of objects based on the HQL statement. You may specify the
	 * starting index of the records you wish to fetch and also the maximum
	 * number of records you wish to fetch.
	 * <p/>
	 * Since our current spring version does not support specifying maxResult
	 * and firstResult in its HibernateTemplate, we need to customize our own.
	 * Once the spring framework version is upgraded to perhaps 2.1.5 version
	 * and above, the content of this method should be just few lines.
	 * 
	 * @param queryName
	 *            HQL statement
	 * @param paramNames
	 *            Array of parameter names in your HQL statement
	 * @param values
	 *            Corresponding parameter values in your HQL statement
	 * @param startIndex
	 *            Rownum in database. Index starts with zero for first rownum
	 * @param maxResults
	 *            Maximum number of results expected to be returned from the
	 *            query
	 * @return List
	 */
	public final List findByNamedQuery(final String queryName,
			final String[] paramNames, final Object[] values,
			final int startIndex, final int maxResults) {
		if (paramNames != null && values != null
				&& paramNames.length != values.length) {
			throw new IllegalArgumentException(
					"Length of paramNames array must match length of values array");
		}

		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			query.setCacheable(true);
			if (maxResults > 0) {
				query.setMaxResults(maxResults);
			}

			if (startIndex > 0) {
				query.setFirstResult(startIndex);
			}

			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(query, paramNames[i], values[i]);
				}
			}
			return query.list();

		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}

	/**
	 * Apply the given name parameter to the given Query object.
	 * 
	 * @param queryObject
	 *            The Query object
	 * @param paramName
	 *            The name of the parameter
	 * @param value
	 *            The value of the parameter
	 * @throws HibernateException
	 *             if thrown by the Query object
	 */
	protected void applyNamedParameterToQuery(Query queryObject,
			String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	public final Object findByNamedQueryUniqueResult(String queryName,
			String[] paramNames, Object[] values) {
		try {
			Query query = this.getSessionFactory().getCurrentSession()
					.getNamedQuery(queryName);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					applyNamedParameterToQuery(query, paramNames[i], values[i]);
				}
			}

			return query.uniqueResult();
		} catch (HibernateException ex) {
			throw new DataException(ex);
		}
	}
}
