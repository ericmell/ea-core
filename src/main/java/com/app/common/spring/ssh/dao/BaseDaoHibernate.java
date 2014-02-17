package com.app.common.spring.ssh.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.app.common.spring.ssh.model.BaseModel;
import com.app.common.spring.ssh.page.Pagination;
import com.opensymphony.util.BeanUtils;
import com.utils.time.TimeUtil;

public class BaseDaoHibernate extends HibernateDaoSupport implements BaseDao {
	private static final Log log = LogFactory.getLog(BaseDaoHibernate.class);
	
	
	public Long create(Object bm) {
		Long id = (Long) getHibernateTemplate().save(bm);
		getHibernateTemplate().flush();
		String[] names = BeanUtils.getPropertyNames(bm);
		for (int i = 0; i < names.length; i++) {
			try {
				if (names[i].endsWith("sortNob")) {
					BeanUtils.setValue(bm, "sortNob",
							BeanUtils.getValue(bm, "id"));
					updateObject(bm);
				}
				if (names[i].endsWith("inputtime")) {
					BeanUtils.setValue(bm, "inputtime",
							TimeUtil.getTimeStr("yyyy-MM-dd hh-mm-ss"));
				}
			} catch (Exception e) {
				log.error("更新sortnob 或者 inputtime失败");
				e.printStackTrace();
			}
		}
		return id;

	}

	public void update(Object bm) {
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD"); Date date
		 * = null; try { date = sdf.parse(sdf.format(new Date())); } catch
		 * (ParseException e) { e.printStackTrace(); } bm.setUpdateDate(date);
		 */
		getHibernateTemplate().merge(bm);

	}

	public void delete(Object bm) {

		getHibernateTemplate().delete(bm);
	}

	public Object loadById(Class clazz, Serializable id) {
		// getHibernateTemplate().execute(new HibernateCallback (){
		// public Object doInHibernate(Session session)throws
		// HibernateException{
		// log.debug(session.hashCode());
		// return null;
		// }
		// });
		Object bm = (BaseModel) getHibernateTemplate().get(clazz, id);
		// if (!Hibernate.isInitialized(bm)){
		// try {
		// Hibernate.initialize(bm);
		// } catch (HibernateException e) {
		// log.error("no record was found in "+ clazz.getName()+"(id="+id+").");
		// }
		// }
		return bm;
	}

	public Collection loadAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sunrise.core.persistence.BaseDAO#batchCreate(java.util.List)
	 */
	public void batchCreate(List bmList) {
		for (int i = 0, j = bmList.size(); i < j; i++) {
			getHibernateTemplate().save(bmList.get(i));
		}
		getHibernateTemplate().flush();
	}

	public List find(String sql) {
		return getHibernateTemplate().find(sql);

	}

	
	
	protected long loadTotalSize(String sql, Object[] objs, Type[] types)
			throws Exception {
		long count = 0;
		try {
			String midSql = getCountSql(sql);
			midSql = "select count(*) " + midSql;

			// lingli 我换掉了这里
			// List ls = this.getHibernateTemplate().find(midSql, objs, types);
			List ls = this.getHibernateTemplate().find(midSql, objs);
			// ��ȡ������
			if (ls != null && ls.size() > 0) {
				Object obj = ls.get(0);
				if (obj instanceof Integer) {
					count = ((Integer) obj).longValue();
				} else if (obj instanceof Long) {
					count = ((Long) obj).longValue();
				}
			}
		} catch (Exception he) {
			log.error(
					"Please check the parent talbe has child records when you delete parent record!"
							+ he.getMessage(), he);
			throw he;
		}
		return count;
	}

	protected String getCountSql(String sql) {
		String midSql = sql;
		int count = StringUtils.indexOf(midSql.toLowerCase(), "from");
		midSql = StringUtils.substring(midSql, count);
		return midSql;

	}

	public Object loadById(String entity, Long id) {
		List result = getHibernateTemplate().find(
				"from " + entity + " where id = ? ", id);
		if (result != null && result.size() > 0) {
			if (result.size() == 1) {
				return result.get(0);
			} else {
				log.debug("loadById=null");
				return null;
			}
		}
		log.debug("loadById=null");
		return null;
	}

	public Object loadByName(Class namedEntity, String name) {
		List result = getHibernateTemplate().find(
				"from " + namedEntity.getName() + " where name = ? ", name);
		if (result != null && result.size() > 0) {
			if (result.size() == 1) {
				return result.get(0);
			} else {
				log.error("more than one object using the name: " + name
						+ " with namedEntity: " + namedEntity.getName());
				// throw exception or return null?
			}
		}
		return null;
	}

	/**

	 * */
	public Object loadByFieldValue(Class namedEntity, String name, String value) {
		List result = getHibernateTemplate().find(
				"from " + namedEntity.getName() + " where " + name + " = ? ",
				value);
		if (result != null && result.size() > 0) {
			if (result.size() > 0) {
				return result.get(0);
			}
			if (result.size() > 1) {
				log.error(" 对象名: " + namedEntity.getName() + "多个数据的字段名: "
						+ name + "等于：" + value);
				return result.get(0);
			}
		} else {
			return null;
		}
		return null;

	}

	public Object loadByNamedQuery(String query, Object obj) {
		List result = findByNamedQuery(query, obj);
		if (result != null && result.size() > 0) {
			if (result.size() == 1) {
				return result.get(0);
			} else {
				log.error("got more than one object using the query: " + query
						+ " with parameter: " + obj);
				// throw exception or return null?
			}
		}
		return null;
	}

	public List findAll(Class entity) {
		return getHibernateTemplate().find("from " + entity.getName());
	}

	public List findByNamedQuery(String namedQuery) {
		return getHibernateTemplate().findByNamedQuery(namedQuery);
	}

	public List findByNamedQuery(String query, Object parameter) {
		return getHibernateTemplate().findByNamedQuery(query, parameter);
	}

	public List findByNamedQuery(String query, Object[] parameters) {
		return getHibernateTemplate().findByNamedQuery(query, parameters);
	}

	public List find(String query, Object parameter) {
		return getHibernateTemplate().find(query, parameter);
	}

	public List find(String query, Object[] parameters) {

		return getHibernateTemplate().find(query, parameters);
	}

	
	public List findByValueBean(String query, Object resource) {
		return getHibernateTemplate().findByNamedQuery(query, resource);
	}

	
	public List findByNamedQuery(String query, String paramName, Object value) {
		return getHibernateTemplate().findByNamedQueryAndNamedParam(query,
				paramName, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sunrise.core.persistence.QueryManager#findByNamedQuery(java.lang.
	 * String, java.lang.String[], java.lang.Object[])
	 */
	public List findByNamedQuery(String query, String[] paramNames,
			Object[] values) {
		return getHibernateTemplate().findByNamedQueryAndNamedParam(query,
				paramNames, values);
	}

	

	public Object getObject(Class clazz, Serializable id) {
		Object o = getHibernateTemplate().get(clazz, id);
		if (o == null) {
			throw new ObjectRetrievalFailureException(clazz, id);
		}

		return o;
	}

	

	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(getObject(clazz, id));
	}



	public void refreshObject(Object o) {
		getHibernateTemplate().refresh(o);

	}

	public void updateObject(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public List find(String sql, Object[] objs, Type[] types,
			Pagination pagination) throws Exception {
		/*
		 * if (pagination == null || pagination.isShowAll()) {
		 * //lingli:此句我删除了，没办法 return getHibernateTemplate().find(sql, objs,
		 * types);
		 * 
		 * }
		 */
		/*
		 * if (pagination == null || pagination.isShowAll()) {
		 * 
		 * return getHibernateTemplate().find(sql, objs); //return
		 * getHibernateTemplate().find(sql, objs,types); }
		 */

		pagination.setTotalSize(loadTotalSize(sql, objs, types));
		Session session = this.getSession();
		Query sqlQuery = null;
		List ls = null;

		try {
			sqlQuery = session.createQuery(sql);
			if (objs != null && objs.length > 0 && types != null
					&& types.length > 0 && objs.length == types.length) {
				for (int i = 0; i < objs.length; i++) {
					sqlQuery.setParameter(i, objs[i], types[i]);
				}
			}
			sqlQuery.setFirstResult((int) pagination.getStart() - 1)
					.setMaxResults((int) pagination.getMaxSize());

			ls = sqlQuery.list();
			if (ls == null) {
				ls = new ArrayList(0);
			}
		} catch (HibernateException e) {
			log.error(e);
		}
		return ls;
	}

	public List find(String sql, Object obj, Type type, Pagination pagination)
			throws Exception {

		return find(sql, new Object[] { obj }, new Type[] { type }, pagination);
	}

	public List find(String sql, Object obj, Pagination pagination)
			throws Exception {
		return find(sql, new Object[] { obj }, pagination);
	}

	public List findbyhsql(String query, Object parameter, Pagination pagination)
			throws Exception {
		if (pagination.isShowAll()) {
			return getHibernateTemplate().find(query, parameter);
		}
		return find(query, new Object[] { parameter }, pagination);

	}

	public List find(String sql, Pagination pagination) throws Exception {
		try {
			// if (pagination == null || pagination.isShowAll()) {
			if (pagination.isShowAll()) {
				return getHibernateTemplate().find(sql);
			}

		} catch (Exception he) {
			log.error(
					"Query error,Please check there parameter!"
							+ he.getMessage(), he);
			throw he;
		}

		try {
			return find(sql, new Object[0], new Type[0], pagination);
		} catch (Exception he) {

			throw he;
		}
	}

	public List page(String sql, Pagination pagination) {
		Query query = getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sql);
		query.setFirstResult(pagination.getMaxSize()
				* ((int) pagination.getCurrentPage() - 1));
		query.setMaxResults(pagination.getMaxSize());
		return query.list();

		/*
		 * 可以查询语句写在里面 return getHibernateTemplate().executeFind(new
		 * HibernateCallback() { public Object doInHibernate(Session s) throws
		 * HibernateException, SQLException { String q = "from  Event "; Query
		 * query = s.createQuery(q); query.setFirstResult(1);
		 * query.setMaxResults(3); List list = query.list(); return list; } });
		 */
	}

	@Override
	public List page(String sql, Object objs, Type types, Pagination pagination)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List page(String sql, Object[] obj, Type[] type,
			Pagination pagination) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List page(String sql, Object obj, Pagination pagination)
			throws Exception {

		pagination.setTotalSize(loadTotalSize(sql, new Object[] { obj },
				new Type[0]));
		Session session = this.getSession();
		Query sqlQuery = null;
		List ls = null;

		try {
			sqlQuery = session.createQuery(sql);

			if (obj != null)
				sqlQuery.setParameter(0, obj);

			sqlQuery.setFirstResult((int) pagination.getStart() - 1)
					.setMaxResults((int) pagination.getMaxSize());

			ls = sqlQuery.list();
			if (ls == null) {
				ls = new ArrayList(0);
			}
		} catch (HibernateException e) {
			log.error(e);
		}
		return ls;
	}

}