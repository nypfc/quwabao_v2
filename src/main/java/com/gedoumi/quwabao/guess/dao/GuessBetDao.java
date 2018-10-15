package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessBet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 用户下注Dao
 * @author Minced
 */
public interface GuessBetDao extends JpaRepository<GuessBet, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 下注集合
     */
    List<GuessBet> findByGuessDetailId(Long guessDetailId);

    /**
     * 根据竞猜详情ID和玩法查询数据数量
     * @param guessDetailId 竞猜ID
     * @param guessMode 玩法
     * @return 查询结果数量
     */
    Long countByGuessDetailIdAndGuessMode(Long guessDetailId, Integer guessMode);

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 下注集合
     */
    List<GuessBet> findByUserId(Long userId);

}
