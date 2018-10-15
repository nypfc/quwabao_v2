package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamDao extends JpaRepository<UserTeam, Long> {

     UserTeam findByUser(User user);

}
