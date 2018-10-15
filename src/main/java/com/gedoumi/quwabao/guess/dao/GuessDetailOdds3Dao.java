package com.gedoumi.quwabao.guess.dao;

import com.gedoumi.quwabao.guess.entity.GuessDetailOdds3;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 玩法三Dao
 * @author Minced
 */
public interface GuessDetailOdds3Dao extends JpaRepository<GuessDetailOdds3, Long> {

    /**
     * 根据竞猜详情ID查询
     * @param guessDetailId 竞猜详情ID
     * @return 玩法二赔率对象
     */
    GuessDetailOdds3 findByGuessDetailId(Long guessDetailId);

}
