package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.UserTeamUnFrozen;
import com.gedoumi.quwabao.user.dataobj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTeamUnFrozenDao extends JpaRepository<UserTeamUnFrozen, Long> {

     UserTeamUnFrozen findByUserAndUnFrozenType(User user, Integer unFrozenType);

     List<UserTeamUnFrozen> findByUser(User user);

}
