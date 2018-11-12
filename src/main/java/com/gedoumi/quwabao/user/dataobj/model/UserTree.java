package com.gedoumi.quwabao.user.dataobj.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户--关联表
 *
 * @author Minced
 */
@TableName("user_tree")
@Data
public class UserTree {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
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