package com.gedoumi.quwabao.miner.dataobj.vo;

import lombok.Data;

import java.util.List;

/**
 * 矿机信息
 *
 * @author Minced
 */
@Data
public class UserRentInfoVO {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 矿机集合
     */
    private List<UserRentVO> userRentVOList;

}
