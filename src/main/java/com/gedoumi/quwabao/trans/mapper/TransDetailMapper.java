package com.gedoumi.quwabao.trans.mapper;

import com.gedoumi.quwabao.trans.dataobj.model.TransDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 转账详情Mapper
 *
 * @author Minced
 */
@Mapper
public interface TransDetailMapper {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 转账详情对象
     */
    TransDetail selectById(Long id);

    /**
     * 创建转账详情
     *
     * @param transDetail 转账详情对象
     * @return 数据库受影响行数
     */
    int insert(TransDetail transDetail);

}