package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessUserAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 竞猜用用户资产Dao
 * @author Minced
 */
public interface GuessUserAssetDao extends JpaRepository<GuessUserAsset, Long> {

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 资产
     */
    GuessUserAsset findByUserId(Long userId);

    /**
     * 根据用户ID集合查询
     * @param userIdList 用户ID集合
     * @return 资产集合
     */
    List<GuessUserAsset> findByUserIdIn(List<Long> userIdList);

}
