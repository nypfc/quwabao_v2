package com.gedoumi.quwabao.sys.service;

import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.TreeJson;
import com.gedoumi.quwabao.sys.dao.SysMenuDao;
import com.gedoumi.quwabao.sys.entity.SysMenu;
import com.gedoumi.quwabao.sys.entity.SysUser;
import org.apache.shiro.SecurityUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleAuthorityService sysRoleAuthorityService;
	
	@Autowired
	private SysAuthorityService sysAuthorityService;
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<TreeJson> getMenuTree(){
		StringBuffer sql = new StringBuffer("select id, menu_name, super_id from sys_menu order by menu_sort");
		
		Query query = entityManager.createNativeQuery(sql.toString());
		List<TreeJson> returnList = new ArrayList<TreeJson>();
		List<Object[]> list = query.getResultList();
		if(list != null && !list.isEmpty()){
			for(Object[] obj : list){
				TreeJson treeJson = new TreeJson();
				treeJson.setId(((BigInteger)obj[0]).toString());
				treeJson.setText((String)obj[1]);
				treeJson.setPid(((BigInteger)obj[2]).toString());
				treeJson.setState("open");
				returnList.add(treeJson);
			}
		}
		return returnList;
	}
	
	public List<SysMenu> getAll(){
		return sysMenuDao.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public DataGrid getList(PageParam param, Long pid){
		DataGrid data=new DataGrid();
		StringBuffer sqlCount = new StringBuffer("select count(id) from SysMenu");
		StringBuffer sqlData = new StringBuffer("select r from SysMenu r");
		if(pid != null){
			sqlCount.append(" where superId = ").append(pid);
			sqlData.append(" where r.superId = ").append(pid);
		}
			
		Query queryCount = entityManager.createQuery(sqlCount.toString());
		Long count = Long.parseLong(queryCount.getResultList().get(0).toString());
		Query queryData = entityManager.createQuery(sqlData.toString());
		List<SysMenu> list = (List<SysMenu>)queryData.setFirstResult((param.getPage() - 1) * param.getRows()).setMaxResults(param.getRows()).getResultList();
		data.setTotal(count);
		data.setRows(list);
		return data;
	}
	
	public SysMenu getById(Long id){
		return sysMenuDao.findById(id).get();
	}
	
	public SysMenu create(SysMenu sysMenu){
		SysUser loginUser = (SysUser)SecurityUtils.getSubject().getSession().getAttribute("sysUser");
		sysMenu.setCreateUserId(loginUser.getId());
		sysMenu.setCreateTime(new Timestamp(System.currentTimeMillis()));
		return sysMenuDao.save(sysMenu);
	}
	
	public SysMenu update(SysMenu sysMenu){
		SysUser loginUser = (SysUser)SecurityUtils.getSubject().getSession().getAttribute("sysUser");
		sysMenu.setUpdateUserId(loginUser.getId());
		sysMenu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		return sysMenuDao.save(sysMenu);
	}
	
	public Boolean delete(String[] ids){
		List<SysMenu> list =new ArrayList<SysMenu>();
		for(String id : ids){
			SysMenu menu = sysMenuDao.findById(Long.valueOf(id)).get();
			if(menu!=null){
				list.add(menu);
				sysRoleAuthorityService.deleteByMenuId(Long.valueOf(id));
				sysAuthorityService.deleteByMenuId(Long.valueOf(id));
			}
		}
		sysMenuDao.deleteInBatch(list);
		return true;
	}
	
	public Boolean delete(Long id){
		sysMenuDao.deleteById(id);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getUserMenuList(Long userId){
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct m.id, m.menu_name, m.super_id, m.menu_sort, sm.menu_name as super_name, m.menu_url ");
		sql.append("from sys_role_authority ra ");
		sql.append("left join sys_authority a on a.id = ra.auth_id ");
		sql.append("left join sys_menu m on m.id = a.menu_id ");
		sql.append("left join sys_menu sm on sm.id = m.super_id ");
		sql.append("left join sys_user_role ur on ur.role_id = ra.role_id ");
		sql.append("where ur.user_id = ");
		sql.append(userId);
		sql.append(" order by m.super_id, m.menu_sort");
		Query query = entityManager.createNativeQuery(sql.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.getResultList();
		return list;
	}
}
