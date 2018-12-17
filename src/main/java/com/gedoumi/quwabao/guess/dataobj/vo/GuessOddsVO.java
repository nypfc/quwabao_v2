package com.gedoumi.quwabao.guess.dataobj.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 竞猜赔率VO
 *
 * @author Minced
 */
@Data
public class GuessOddsVO {

    /**
     * 玩法一赔率
     */
    private Odds1VO mode1 = new Odds1VO();

    @Data
    public class Odds1VO {

        /**
         * 赔率1
         */
        private BigDecimal o1;

        /**
         * 赔率2
         */
        private BigDecimal o2;

        /**
         * 赔率3
         */
        private BigDecimal o3;

        /**
         * 赔率4
         */
        private BigDecimal o4;

        /**
         * 赔率5
         */
        private BigDecimal o5;

        /**
         * 赔率6
         */
        private BigDecimal o6;

    }

    /**
     * 玩法二赔率
     */
    private Odds2VO mode2 = new Odds2VO();

    /**
     * 玩法二赔率
     *
     * @author Minced
     */
    @Data
    public class Odds2VO {

        /**
         * 赔率12
         */
        private BigDecimal o12;

        /**
         * 赔率13
         */
        private BigDecimal o13;

        /**
         * 赔率14
         */
        private BigDecimal o14;

        /**
         * 赔率15
         */
        private BigDecimal o15;

        /**
         * 赔率16
         */
        private BigDecimal o16;

        /**
         * 赔率23
         */
        private BigDecimal o23;

        /**
         * 赔率24
         */
        private BigDecimal o24;

        /**
         * 赔率25
         */
        private BigDecimal o25;

        /**
         * 赔率26
         */
        private BigDecimal o26;

        /**
         * 赔率34
         */
        private BigDecimal o34;

        /**
         * 赔率35
         */
        private BigDecimal o35;

        /**
         * 赔率36
         */
        private BigDecimal o36;

        /**
         * 赔率45
         */
        private BigDecimal o45;

        /**
         * 赔率46
         */
        private BigDecimal o46;

        /**
         * 赔率56
         */
        private BigDecimal o56;

    }

    /**
     * 玩法三赔率集合
     */
    private Odds3VO mode3 = new Odds3VO();

    /**
     * 玩法三赔率
     *
     * @author Minced
     */
    @Data
    public class Odds3VO {

        /**
         * 赔率1
         */
        private BigDecimal o1;

        /**
         * 赔率2
         */
        private BigDecimal o2;

    }

    /**
     * 剩余时间，单位：秒
     */
    private Integer remain;

}
