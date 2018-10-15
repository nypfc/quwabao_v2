package com.gedoumi.quwabao.asset.dao;

import com.gedoumi.quwabao.asset.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserAssetDao extends JpaRepository<UserAsset, Long> {

     UserAsset findByUser(User user);

     List<UserAsset> findByFrozenAssetAfter(BigDecimal frozenAsset);
}
