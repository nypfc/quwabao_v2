package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.user.dao.UserTreeDao;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.user.dataobj.entity.UserTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * 
 * 类名：
 * 功能：
 *
 */
@Service
public class UserTreeService {
	
	@Autowired
	private UserTreeDao userTreeDao;
	
	@PersistenceContext
    private EntityManager entityManager;
	
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
