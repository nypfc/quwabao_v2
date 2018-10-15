package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.UserReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRewardDao extends JpaRepository<UserReward, Long> {

     List<UserReward> findByRemainFrozenAfter(BigDecimal remainAsset);


}
