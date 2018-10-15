package com.gedoumi.quwabao.user.dao;

import com.gedoumi.quwabao.user.dataobj.entity.User;
import com.gedoumi.quwabao.user.dataobj.entity.UserTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTreeDao extends JpaRepository<UserTree, Long> {

	public UserTree findByChild(User child);

	public List<UserTree> findByParent(User parent);


}
