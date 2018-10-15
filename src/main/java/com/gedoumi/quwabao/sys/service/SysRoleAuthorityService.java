package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.sys.dao.SysRoleAuthorityDao;
import com.gedoumi.quwabao.sys.entity.SysRoleAuthority;
import com.gedoumi.quwabao.sys.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 
 * 类名：SysRoleAuthorityService
 * 功能：用户角色关联管理 业务层
 *
 */
@Service
public class SysRoleAuthorityService {
	
	@Autowired
	private SysRoleAuthorityDao sysRoleAuthorityDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public List<SysRoleAuthority> getAll(){
		return sysRoleAuthorityDao.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public DataGrid getAuthorityListByRole(Long roleId, PageParam param){
		DataGrid data=new DataGrid();
		StringBuffer sqlCount = new StringBuffer("select count(id) from sys_role_authority where role_id = ").append(roleId);
		StringBuffer sqlData = new StringBuffer("select a.id, a.auth_name, a.auth_url, m.menu_name from sys_role_authority ra left join sys_authority a on a.id = ra.auth_id left join sys_menu m on m.id = a.menu_id where ra.role_id = ").append(roleId);
		Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
		Query queryData = entityManager.createNativeQuery(sqlData.toString());
		queryData.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<SysRoleAuthority> list = (List<SysRoleAuthority>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();
		data.setTotal(count);
		data.setRows(list);
		return data;
	}
	
	@SuppressWarnings("unchecked")
	public DataGrid getNoSelectAuthorityListByRole(Long roleId, Long menuId, PageParam param){
		DataGrid data=new DataGrid();
		StringBuffer sqlCount = new StringBuffer("select count(a.id) from sys_authority a left join sys_menu m on m.id = a.menu_id left join sys_role_authority ra on a.id = ra.auth_id where a.menu_id = ").append(menuId).append(" and a.id not in (select sra.auth_id from sys_role_authority sra where sra.role_id = ").append(roleId).append(")");
		StringBuffer sqlData = new StringBuffer("select a.id, a.auth_name, a.auth_url, m.menu_name from sys_authority a left join sys_menu m on m.id = a.menu_id left join sys_role_authority ra on a.id = ra.auth_id where a.menu_id = ").append(menuId).append(" and a.id not in (select sra.auth_id from sys_role_authority sra where sra.role_id = ").append(roleId).append(")");
		Query queryCount = entityManager.createNativeQuery(sqlCount.toString());
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
		Query queryData = entityManager.createNativeQuery(sqlData.toString());
		queryData.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = (List<Map<String, Object>>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();
		data.setTotal(count);
		data.setRows(list);
		return data;
	}
	
	public SysRoleAuthority getById(Long id){
		return sysRoleAuthorityDao.findById(id).get();
	}
	
	public SysRoleAuthority create(SysRoleAuthority sysRoleAuthority){
		return sysRoleAuthorityDao.save(sysRoleAuthority);
	}
	
	public void batchCreate(Long roleId, String[] ids){
		for(String id : ids){
			if(!StringUtils.isEmpty(id)){
				SysRoleAuthority sysRoleAuthority = new SysRoleAuthority();
				sysRoleAuthority.setRoleId(roleId);
				sysRoleAuthority.setAuthId(Long.valueOf(id));
				SysUser loginUser = (SysUser)SecurityUtils.getSubject().getSession().getAttribute("sysUser");
				sysRoleAuthority.setCreateUserId(loginUser.getId());
				sysRoleAuthority.setCreateTime(new Timestamp(System.currentTimeMillis()));
				sysRoleAuthorityDao.save(sysRoleAuthority);
			}
		}
		sysRoleAuthorityDao.flush();
	}
	
	public SysRoleAuthority update(SysRoleAuthority sysRoleAuthority){
		return sysRoleAuthorityDao.save(sysRoleAuthority);
	}
	
	public Boolean delete(String[] ids){
		List<SysRoleAuthority> list =new ArrayList<SysRoleAuthority>();
		for(String id : ids){
			SysRoleAuthority role = sysRoleAuthorityDao.findById(Long.valueOf(id)).get();
			if(role!=null){
				list.add(role);
			}
		}
		sysRoleAuthorityDao.deleteInBatch(list);
		return true;
	}
	
	public Boolean delete(Long roleId, String[] ids){
		List<SysRoleAuthority> list =new ArrayList<SysRoleAuthority>();
		for(String id : ids){
			SysRoleAuthority roleAuth = sysRoleAuthorityDao.findByAuthIdAndRoleId(Long.valueOf(id), roleId);
			if(roleAuth!=null){
				list.add(roleAuth);
			}
		}
		sysRoleAuthorityDao.deleteInBatch(list);
		return true;
	}
	
	public Boolean delete(Long id){
		sysRoleAuthorityDao.deleteById(id);
		return true;
	}
	
	public Boolean deleteByRoleId(Long roleId){
		List<SysRoleAuthority> list = sysRoleAuthorityDao.findByRoleId(roleId);
		sysRoleAuthorityDao.deleteInBatch(list);
		return true;
	}
	
	public Boolean deleteByMenuId(Long menuId){
		List<SysRoleAuthority> list = sysRoleAuthorityDao.findByMenuId(menuId);
		sysRoleAuthorityDao.deleteInBatch(list);
		return true;
	}
}
