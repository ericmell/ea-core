package com.app.ea.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.app.common.spring.ssh.dao.BaseDao;
import com.app.common.spring.ssh.model.BaseModel;
import com.app.ea.api.ImpEa;
import com.app.ea.api.InfEa;
import com.app.ea.model.Organize;
import com.app.ea.model.Organizegroup;
import com.app.ea.model.Resource;
import com.app.ea.model.Role;
import com.app.ea.model.Rolegroup;
import com.app.ea.model.Systempara;
import com.app.ea.model.User;
@Component("impTrainSchool")
public class ImpTrainSchool extends ParentDemo implements InfDemo{
	private final Logger log = LoggerFactory.getLogger(ImpTrainSchool.class);
	public void initData() throws Exception {
		//if(baseDao.find(" from User ").size()==0){
		if(true){
		log.debug("初始化数据");
		User admin = create_user("admin", "admin", "1234567890","3436070@qq.com", "");
		User test = create_user("test", "test", "1234567890","test@qq.com", "");

		Organize o1 = create_organize("部门架构", "department", null);
		Organize o2 = create_organize("赞歌艺术学校", "school", null);
		Organize o3 = create_organize("新宿酒店", "hotel", null);
		
		Systempara sp1 = create_systempara("Organize_extp_hotel", "房间扩展属性");
		Systempara sp11 = create_sub_systempara(sp1, "efee", "电表读数");
		Systempara sp12 = create_sub_systempara(sp1, "wfee", "水表读数");
		Systempara sp13 = create_sub_systempara(sp1, "begindate", "登记日期");
		Systempara sp14 = create_sub_systempara(sp1, "enddate", "结束日期");
		
		Rolegroup r1 = create_rolegroup("部门岗位", "department");
		Rolegroup r11 = create_sub_rolegroup(r1, "部门经理", "department-manager");
		Rolegroup r2 = create_rolegroup("项目岗位", "rolegroup-project");
		Rolegroup r21 = create_sub_rolegroup(r2, "项目经理", "project-manager");
		Rolegroup r3 = create_rolegroup("虚拟团队", "virtualteam");
		// 项目
		Organizegroup organizegroup1 = create_organizegroup(null, "公司","company");
		Organizegroup organizegroup2 = create_organizegroup(null,"项目", "project");
		
		
		Resource r0 = create_resource("系统管理", "system-admin", null);
		Resource r01 = create_sub_resource(r0, "组织架构管理","organize-manager", "ea_organize_menu_organize.do");
		Resource r02 = create_sub_resource(r0, "岗位管理","positon-admin","ea_rolegroup_menu_rolegroup.do");
		Resource r03 = create_sub_resource(r0, "职位管理","role-admin","ea_ea_menu_relation.do");
		Resource r04 = create_sub_resource(r0, "人员管理","user-admin","ea_user_list.do");
		Resource r05 = create_sub_resource(r0, "资源管理","resource-admin","ea_resource_menu_resource.do");
		Resource r06 = create_sub_resource(r0, "邮件服务器设置 ","smtp_menu_smtp_init", "ea_smtp_menu_smtp_init.do");
		Resource r07 = create_sub_resource(r0, "系统参数 ","smtp_menu_smtp_init", "ea_systempara_menu_systempara.do");	
		
		admin.getResources().add(r0);
		admin.getResources().add(r01);
		admin.getResources().add(r02);
		admin.getResources().add(r03);
		admin.getResources().add(r04);
		admin.getResources().add(r05);
		admin.getResources().add(r06);
		admin.getResources().add(r07);
		
		baseDao.update(admin);
	    
       
		
		}
	}
}
