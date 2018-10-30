package com.gedoumi.quwabao.user.dataobj.dto;

import lombok.Data;

/**
 * 用户租用矿机数量DTO
 *
 * @author Minced
 */
@Data
public class UserRentNumberDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户租用矿机数量
     */
    private Integer number;

}
