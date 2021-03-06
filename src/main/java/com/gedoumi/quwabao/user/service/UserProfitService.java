package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.RentStatusEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.user.dataobj.dto.UserProfitDTO;
import com.gedoumi.quwabao.user.dataobj.model.UserProfit;
import com.gedoumi.quwabao.user.mapper.UserProfitMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    public List<UserProfit> getCurrentDayUserProfits(String date) {
        return userProfitMapper.selectByDate(date);
    }

    /**
     * 获取当日指定用户的收益
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 用户收益DTO对象
     */
    public UserProfitDTO getCurrentDayUserProfit(Long userId, String date) {
        return userProfitMapper.selectByuserIdAndDate(userId, date, RentStatusEnum.ACTIVE.getValue());
    }

    /**
     * 获取当日总静态收益
     *
     * @param date 日期
     * @return 总静态收益
     */
    public BigDecimal getCurrentDayTotalStaticProfit(String date) {
        return userProfitMapper.selectTotalStaticProfit(date);
    }

    /**
     * 批量添加用户收益
     *
     * @param userProfits 用户收益集合
     */
    public void insertBatch(List<UserProfit> userProfits) {
        userProfitMapper.insertBatch(userProfits);
    }

    /**
     * 批量更新用户收益
     *
     * @param userProfits 用户收益集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateBatch(List<UserProfit> userProfits) {
        userProfitMapper.updateBatchByUserId(userProfits);
    }

}
