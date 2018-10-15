package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.base.DataGrid;
import com.gedoumi.quwabao.common.base.PageParam;
import com.gedoumi.quwabao.common.base.TreeJson;
import com.gedoumi.quwabao.sys.entity.SysMenu;
import com.gedoumi.quwabao.sys.entity.SysUser;
import com.gedoumi.quwabao.sys.service.SysMenuService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.*;

@RestController
@RequestMapping("/admin/menu")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;
	
	@RequestMapping(value="/index", method= RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView("sys/menuIndex");
        return mv;
    }
	
	@GetMapping("/tree")
	public List<TreeJson> getTree(){
		return TreeJson.formatTree(sysMenuService.getMenuTree());
	}
	
	@GetMapping("/")
	public List<SysMenu> getAll(){
		return sysMenuService.getAll();
	}
	
	@GetMapping("/{id}")
	public SysMenu getOne(@PathVariable("id")Long id){
		return sysMenuService.getById(id);
	}
	
	@GetMapping("/page")
	public DataGrid getPage(PageParam param, Long pid){
		return sysMenuService.getList(param, pid);
	}
	
	@PostMapping("/")
	public SysMenu create(SysMenu sysMenu){
		return sysMenuService.create(sysMenu);
	}
	
	@PutMapping("/{id}")
	public SysMenu update(@PathVariable("id")Long id, SysMenu sysMenu){
		return sysMenuService.update(sysMenu);
	}
	
	@DeleteMapping("/{ids}")
	public Boolean del(@PathVariable("ids")String[] ids){
		return sysMenuService.delete(ids);
	}


	@RequestMapping("/userMenu")
	@ResponseBody
	public List<Map<String, Object>> getUserMenu(){
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		SysUser sysUser = (SysUser)SecurityUtils.getSubject().getSession().getAttribute(Constants.USER_KEY);
		List<Map<String, Object>> list = sysMenuService.getUserMenuList(sysUser.getId());

		Set<SysMenu> superSet = new HashSet<SysMenu>();
		for(Iterator<Map<String, Object>> iter = list.iterator(); iter.hasNext();){
			Map<String, Object> menu = iter.next();
			SysMenu smenu = new SysMenu();
			smenu.setId(((BigInteger)menu.get("super_id")).longValue());
			smenu.setMenuSort((Short)menu.get("menu_sort"));
			smenu.setMenuName((String)menu.get("super_name"));
			superSet.add(smenu);
		}
		List<SysMenu> superList = new ArrayList<SysMenu>(superSet);
		Collections.sort(superList, new Comparator<SysMenu>() {
			@Override
			public int compare(SysMenu o1, SysMenu o2) {
				if (o1.getMenuSort() < o2.getMenuSort())
					return -1;
				else if (o1.getMenuSort() > o2.getMenuSort())
					return 1;
				else
					return 0;
			}
		});

		for(Iterator<SysMenu> iter = superList.iterator(); iter.hasNext();){
			SysMenu superMenu = iter.next();
			List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
			for(Iterator<Map<String, Object>> iter1 = list.iterator(); iter1.hasNext();){
				Map<String, Object> menu = iter1.next();
				if(((BigInteger)menu.get("super_id")).longValue() == superMenu.getId()){
					menuList.add(menu);
				}
			}
			Map<String, Object> menuObject = new HashMap<String, Object>();
			menuObject.put("superMenu", superMenu);
			menuObject.put("menuList", menuList);
			returnList.add(menuObject);
		}

		return returnList;
	}
	
}
