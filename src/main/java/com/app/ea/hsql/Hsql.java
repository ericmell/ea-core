package com.app.ea.hsql;

public class Hsql {

	/* 常量定义区 */
	public static final String super_admin = "admin";
	public static final String user_login = "user_logined";
	public static final String status_ok = "1";

	/* sql语句定义区 */

	public static final String All_USER = "from User u ";

	// public static final String sql_getuser_by_roleid =
	// "select u from User u  left join u.roles role where  u.status="
	// + status_ok + " and role.id =? ";
	// /* 根据组织ID得到所有用户 */
	// public static final String sql_get_user_by_organizeid =
	// "select u from User u  left join u.organizes org where  u.status="
	// + status_ok + " and org.id =? ";
	// /* 根据组织ID得到该组织下的角色 */
	// public static final String sql_get_role_by_organizeid =
	// "select role from Role role  left join role.organizes organize where  role.status="
	// + status_ok + " and organize.id =? ";
	// /* 根据资源ID得到可以访问该资源的角色 */
	// public static final String sql_get_role_by_resourceid =
	// "select role from Role role  left join role.resources resource where  role.status="
	// + status_ok + " and resource.id =? ";
	// /* 根据资源ID得到可以访问该资源的角色组 */
	// public static final String sql_get_rolegroup_by_resourceid =
	// "select rolegroup from Rolegroup rolegroup  left join rolegroup.resources resource where  rolegroup.status="
	// + status_ok + " and resource.id =? ";
	// /* 选择其他成员时 */
	// public static final String sql_getuser_by_not_in_organizeid = All_USER
	// +
	// "and u.id not in ( select u.id from User u  left join u.organizes org where  u.status="
	// + status_ok + " and org.id=? )";
	// public static final String sql_getuser_by_not_in_roleid = All_USER
	// +
	// "and u.id not in ( select u.id from User u  left join u.roles role     where  u.status="
	// + status_ok + " and role.id=? )";
	// public static final String sql_getorganize_by_alias =
	// "from Organize org where org.status="
	// + status_ok + " and org.alias=? and org.realm.alias=?";
	// public static final String sql_get_roottreeresource_by_realmalias =
	// "from Resource t where t.status="
	// + status_ok
	// + " and  t.type='"
	// + resource_type_tree
	// +
	// "' and t.parentModel is null and t.realm.alias=?  order by  t.sortnob asc";
	// public static final String sql_get_childtreeresource =
	// "from Resource t where t.status="
	// + status_ok
	// + " and  t.type='"
	// + resource_type_tree
	// + "'   and t.parentModel.id=?  order by  t.sortnob asc";
	// public static final String sql_get_root_resource_by_realmid =
	// "from Resource resource where resource.realm.id = ? and resource.status="
	// + status_ok
	// + " and resource.parentModel is null order by  resource.sortnob asc";
	// /* 树形菜单的查询语句 */
	// public static final String sql_get_resource_by_parentid =
	// "from Resource resource where parent_id = ? and resource.status="
	// + status_ok + " order by  resource.sortnob asc";
	// /* 根据域别名取所有的资源，admin登录UEAAC时可以，取所有的资源 */
	// public static final String sql_get_resource_by_realm_alias =
	// "select  res   from Resource res where res.realm.alias=?  and res.status="
	// + status_ok;
	//
	// public static final String sql_get_root_organize_by_realmid =
	// "from Organize organize where organize.realm.id = ? and organize.status="
	// + status_ok + " and organize.parentModel is null";
	// public static final String sql_get_organize_by_parentid =
	// "from Organize organize where parent_id = ? and organize.status="
	// + status_ok;
	// public static final String sql_get_root_role_by_realmid =
	// "from Role role where role.realm.id = ? and role.status="
	// + status_ok + " and role.parentModel is null";
	// public static final String sql_get_role_by_parentid =
	// "from Role role where parent_id = ? and role.status="
	// + status_ok;
	// public static final String sql_get_root_organizegroup_by_realmid =
	// "from Organizegroup organizegroup where organizegroup.realm.id = ? and organizegroup.status="
	// + status_ok + " and organizegroup.parentModel is null";
	// public static final String sql_get_organizegroup_by_parentid =
	// "from Organizegroup organizegroup where parent_id = ? and organizegroup.status="
	// + status_ok;
	// public static final String sql_get_root_rolegroup_by_realmid =
	// "from Rolegroup rolegroup where rolegroup.realm.id = ? and rolegroup.status="
	// + status_ok + " and rolegroup.parentModel is null";
	// public static final String sql_get_rolegroup_by_parentid =
	// "from Rolegroup rolegroup where parent_id = ? and rolegroup.status="
	// + status_ok;
	// public static final String sql_get_organizegroup_by_alias =
	// "from Organizegroup organizegroup where alias = ? and organizegroup.status="
	// + status_ok;
	// public static final String sql_get_organize_by_organizegroupid =
	// "select organize from Organize organize left join organize.organizegroups organizegroup where  organize.status="
	// + status_ok + " and organizegroup.id =? ";
	//
	// public static final String sql_get_user_by_role_of_organize =
	// "select u from User u  left join u.roles role left join role.organizes org where  u.status="
	// + status_ok + " and org.id =? ";
	//
	// public static final String sql_get_role_by_userid_in_realm =
	// "select role from Role role left join role.users user where role.realm.alias = ? and user.id = ?  and role.status="
	// + status_ok;
}
