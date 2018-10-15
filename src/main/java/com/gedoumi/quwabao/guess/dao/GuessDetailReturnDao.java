package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailReturn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 实际返奖率Dao
 * @author Minced
 */
public interface GuessDetailReturnDao extends JpaRepository<GuessDetailReturn, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 实际返奖率
     */
    GuessDetailReturn findByGuessDetailId(Long guessDetailId);

}
