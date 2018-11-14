package com.gedoumi.quwabao.user.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 用户--关联表
 *
 * @author Minced
 */
@Alias("UserTree")
@Data
public class UserTree {

    /**
     * ID
     */
    private Long id;

    /**
     * 子用户ID
     */
    private Long childId;

    /**
     * 父用户ID
     */
    private Long parentId;

}