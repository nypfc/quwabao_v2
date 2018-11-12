package com.gedoumi.quwabao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gedoumi.quwabao.user.dataobj.model.UserAssetDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户资产详情Mapper
 *
 * @author Minced
 */
@Mapper
public interface UserAssetDetailMapper extends BaseMapper<UserAssetDetail> {

    /**
     * 查询用户资产详情列表
     *
     * @param userId    用户ID
     * @param transType 需要查询的类型集合
     * @return 用户资产详情集合
     */
    IPage<UserAssetDetail> selectTransInIds(Page page, @Param("userId") Long userId, @Param("transType") List<Integer> transType);

}
