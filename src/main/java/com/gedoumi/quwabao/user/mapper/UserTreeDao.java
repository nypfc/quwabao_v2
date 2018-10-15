package com.gedoumi.quwabao.user.mapper;

import com.gedoumi.quwabao.team.dataobj.model.UserTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTreeDao extends JpaRepository<UserTree, Long> {

	public UserTree findByChild(User child);

	public List<UserTree> findByParent(User parent);


}
