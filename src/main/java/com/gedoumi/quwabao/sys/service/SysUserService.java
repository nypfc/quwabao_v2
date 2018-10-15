package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.enums.UserStatus;
import com.gedoumi.quwabao.sys.dao.SysUserDao;
import com.gedoumi.quwabao.sys.dao.SysUserRoleDao;
import com.gedoumi.quwabao.sys.entity.SysUser;
import com.gedoumi.quwabao.sys.entity.SysUserRole;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;


/**
 * 
 * 类名：SysUserService
 * 功能：用户管理 业务层
 *
 */
@Service
public class SysUserService {
	
	@Resource
	private SysUserDao sysUserDao;

	@Resource
	private SysUserRoleDao sysUserRoleDao;

	@PersistenceContext
    private EntityManager entityManager;

	@Transactional
	public void addUser(SysUser user){

		int length = String.valueOf(user.getId()).length();
		length = length > 4 ? length : 4;
		String format = "%0" + length + "d";
		user.setUsername(User.PREFIX+String.format(format,user.getId()));
		sysUserDao.save(user);

	}
	
	public List<SysUser> getAll(){
		return sysUserDao.findAll();
	}
	

	public SysUser getById(Long id){
		return sysUserDao.findById(id).get();
	}
	
	public SysUser create(SysUser user){
		if(StringUtils.isEmpty(user.getPassword()))
			user.setPassword(new Md5Hash(User.PWD_INIT, user.getUsername()).toString());
		return sysUserDao.save(user);
	}
	
	public SysUser update(Long id, SysUser user){
		return null;
	}
	
	public SysUser update(SysUser user){
		return sysUserDao.save(user);
	}
	
	public Boolean delete(String[] ids){
		return true;
	}
	
	public Boolean delete(Long id){
		sysUserDao.deleteById(id);
		return true;
	}
	

	
	public SysUser getByUsername(String username){
		return sysUserDao.findByUsername(username);
	}


	
	public SysUser checkLoginUser(String username, UserStatus userStatus){
		SysUser user = sysUserDao.findByUsernameAndUserStatus(username, userStatus.getValue());
		return user;
	}

	@Transactional
	public void updateLoginInfo(SysUser user){
		sysUserDao.save(user);
	}


	@SuppressWarnings("unchecked")
	public Map<String,Collection<String>> getRolesPowers(String username){
		Map<String,Collection<String>> map=new HashMap<String,Collection<String>>();
		List<String> roleIds = new ArrayList<String>();
		List<SysUserRole> urList = sysUserRoleDao.findByUsername(username);
		//用户可以访问的url集合
		Set<String> urls=new HashSet<String>();
		for(SysUserRole ur : urList){
			roleIds.add(ur.getRoleId().toString());
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct m.menu_url from sys_role_authority ra, sys_authority a, sys_menu m where ra.auth_id = a.id and a.menu_id = m.id and ra.role_id = ").append(ur.getRoleId());
			Query query = entityManager.createNativeQuery(sql.toString());
			List<String> urlList = (List<String>)query.getResultList();
			for(String url:urlList){
				if(StringUtils.isNotBlank(url)){
					urls.add(url.split("\\?")[0]);
				}
			}
		}
		//获取操作url
		for(SysUserRole ur : urList){
			StringBuffer sql = new StringBuffer();
			sql.append("select distinct a.auth_url from sys_authority a,sys_role_authority ra where a.id=ra.auth_id and ra.role_id = ").append(ur.getRoleId());
			Query query = entityManager.createNativeQuery(sql.toString());
			List<String> authList = (List<String>)query.getResultList();
			for(String url:authList){
				if(StringUtils.isNotBlank(url)){
					urls.addAll(Arrays.asList(url.split(",")));
				}
			}
		}
		map.put("roleIds", roleIds);
		map.put("powers", urls);
		return map;
	}

}
