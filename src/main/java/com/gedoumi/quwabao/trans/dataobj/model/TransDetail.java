package com.gedoumi.quwabao.trans.dataobj.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 转账详情
 *
 * @author Minced
 */
@Alias("TransDetail")
@Data
public class TransDetail {

    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 转账金额
     */
    private BigDecimal money;

    /**
     * 转账状态
     */
    private Integer transStatus;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 转账人
     */
    private Long fromUserId;

    /**
     * 被转账人
     */
    private Long toUserId;

}