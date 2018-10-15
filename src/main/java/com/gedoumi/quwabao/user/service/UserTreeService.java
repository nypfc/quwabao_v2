package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.team.dataobj.model.UserTree;
import com.gedoumi.quwabao.user.mapper.UserTreeDao;
import com.gedoumi.quwabao.user.dataobj.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserTreeService {
	
	@Resource
	private UserTreeDao userTreeDao;
	
	public List<UserTree> getAll(){
		return userTreeDao.findAll();
	}

	@Transactional
	public void add(UserTree userTree){
		userTreeDao.save(userTree);
	}

	public UserTree findByChild(User child){
		return userTreeDao.findByChild(child);
	}

	public List<UserTree> findByParent(User parent){
		return userTreeDao.findByParent(parent);
	}

}
