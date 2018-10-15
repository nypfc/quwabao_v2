package com.gedoumi.quwabao.guess.dto;

import com.gedoumi.quwabao.guess.entity.GuessDetailOdds1;
import com.gedoumi.quwabao.guess.entity.GuessDetailOdds2;
import com.gedoumi.quwabao.guess.entity.GuessDetailOdds3;
import lombok.Data;

/**
 * 赔率DTO
 * @author Minced
 */
@Data
public class GuessOddsDTO {

    /**
     * 玩法一赔率
     */
    private GuessDetailOdds1 guessDetailOdds1;

    /**
     * 玩法二赔率
     */
    private GuessDetailOdds2 guessDetailOdds2;

    /**
     * 玩法三赔率
     */
    private GuessDetailOdds3 guessDetailOdds3;

}
