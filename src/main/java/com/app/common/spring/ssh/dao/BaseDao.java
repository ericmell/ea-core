package com.app.common.spring.ssh.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.type.Type;

import com.app.common.spring.ssh.model.BaseModel;
import com.app.common.spring.ssh.page.Pagination;

public interface BaseDao {
	// 基本添加删除
	Long create(Object bm);

	void update(Object bm);

	void delete(Object bm);

	void delete(Class clazz, Serializable id);

	// 加载对象
	Object loadById(Class clazz, Serializable id);

	Object loadById(String entity, Long id);

	Object loadByFieldValue(Class namedEntity, String name, String value);

	Collection loadAll(Class clazz);
	

	// 查询
	List find(String sql);

	List find(String query, Object parameter);

	List find(String query, Object[] parameters);

	
	
	// 分页查询
	List page(String sql, Object objs, Type types, Pagination pagination)
			throws Exception;

	List page(String sql, Object[] obj, Type[] type, Pagination pagination)
			throws Exception;

	List page(String sql, Object obj, Pagination pagination) throws Exception;

	List page(String sql, Pagination pagination) throws Exception;

    
	/*淘汰做法：这中用法并不能集中编写sql
	 首先需要在User.hbm.xml中定义命名查询
            <hibernate-mapping>
                 <class>......</class>
                 <query name="queryAllUser"><!--此查询被调用的名字-->
 
                      <![CDATA[
                           from bean.User
                       ]]>
                 </query>
            </hibernate-mapping>
	List findByNamedQuery(String query);
	List findByNamedQuery(String query, Object parameter);
	List findByNamedQuery(String query, Object[] parameters);
	List findByValueBean(String query, Object resource);
	List findByNamedQuery(String query, String paramName, Object value);
	List findByNamedQuery(String query, String[] paramNames, Object[] values);
    */

}