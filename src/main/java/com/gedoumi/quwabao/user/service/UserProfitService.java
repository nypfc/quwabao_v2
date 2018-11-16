package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import com.gedoumi.quwabao.user.mapper.UserProfitMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户收益Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserProfitService {

    @Resource
    private UserProfitMapper userProfitMapper;

    /**
     * 获取用户收益列表
     *
     * @param userId 用户ID
     * @param page   当前页码
     * @param rows   每页数据量
     * @return 收益集合
     */
    public List<UserProfit> getUserProfits(Long userId, String page, String rows) {
        // 分页查询
        try {
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        } catch (NumberFormatException ex) {
            log.error("分页参数不能格式化为int类型");
            throw new BusinessException(CodeEnum.ParamError);
        }
        PageInfo<UserProfit> p = new PageInfo<>(userProfitMapper.selectByUserId(userId));
        return p.getList();
    }

    /**
     * 获取当日静态收益列表
     *
     * @param date 日期
     * @return 用户收益集合
     */
    public List<UserProfit> getCurrentDayUserProfit(String date) {
        return userProfitMapper.selectByDate(date);
    }

    /**
     * 批量添加用户收益
     *
     * @param userProfits 用户收益集合
     * @return 数据库受影响行数
     */
    public void insertBatch(List<UserProfit> userProfits) {
        userProfitMapper.insertBatch(userProfits);
    }

    /**
     * 批量更新用户收益
     *
     * @param userProfits 用户收益集合
     * @return 数据库受影响行数
     */
    public void updateBatch(List<UserProfit> userProfits) {
        userProfitMapper.updateBatch(userProfits);
    }

}
