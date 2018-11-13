package com.gedoumi.quwabao.user.service;

import com.gedoumi.quwabao.common.enums.TransType;
import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.UserRentStatus;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentNumberDTO;
import com.gedoumi.quwabao.user.dataobj.form.RentForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.dataobj.model.UserRent;
import com.gedoumi.quwabao.user.mapper.UserRentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 用户矿机Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class UserRentService {

    @Resource
    private UserRentMapper userRentMapper;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private SysRentService sysRentService;

    /**
     * 获取用户租用矿机信息列表
     *
     * @param userId 用户ID
     * @return 矿机信息集合
     */
    public List<UserRent> getUserRents(Long userId) {
        return userRentMapper.selectUserRents(userId, UserRentStatus.Active.getValue());
    }

    /**
     * 获取指定用户租用矿机的数量
     *
     * @param userIds 用户ID集合
     * @return 矿机信息集合
     */
    public List<UserRentNumberDTO> getUserRentNumber(List<Long> userIds) {
        return userRentMapper.countUserRentsByIds(userIds, UserRentStatus.Active.getValue());
    }

    /**
     * 获取指定用户已租用的矿机价格的总和
     *
     * @param userIds 用户ID集合
     * @return 矿机价格总和
     */
    public BigDecimal getTotalRentAsset(List<Long> userIds) {
        return userRentMapper.queryTotalRentAsset(userIds);
    }

    /**
     * 矿机租用
     *
     * @param rentForm 租用表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void rent(RentForm rentForm) {
        // 判断是否是结算期，如果在23:00-00:00之间，不能租用
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        if (cal.get(Calendar.HOUR_OF_DAY) >= 23)
            throw new BusinessException(CodeEnum.AddRentTimeError);
        // 获取参数
        Integer rentType = rentForm.getRentType();
        String password = rentForm.getPassword();
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        Long userId = user.getId();
        String mobile = user.getMobilePhone();
        // 密码加密比对
        String encrypyPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));
        if (!StringUtils.equals(encrypyPassword, user.getPassword())) {
            log.error("手机号:{} 密码:{} 租用矿机密码不匹配", mobile, password);
            throw new BusinessException(CodeEnum.PasswordError);
        }
        // 查询矿机信息
        SysRent rent = sysRentService.getRent(rentType);
        BigDecimal rentMoney = rent.getMoney();
        // 判断余额
        userAssetService.remainAsset(userId, rentMoney);
        // 创建用户矿机
        UserRent userRent = new UserRent();
        userRent.setRentAsset(rentMoney);
        userRent.setLastDig(BigDecimal.ZERO);
        userRent.setAlreadyDig(BigDecimal.ZERO);
        userRent.setTotalAsset(rent.getProfitMoneyExt());
        userRent.setUserId(userId);
        userRent.setRentType(rentType);
        userRent.setRentStatus(UserRentStatus.Active.getValue());
        userRentMapper.insert(userRent);
        // 更新用户资产（注意将rentMoney转为负数）
        userAssetService.updateUserAsset(userId, rentMoney.negate(), BigDecimal.ZERO);
        // 创建用户资产详情
        userAssetDetailService.createUserDetailAsset(userId, rentMoney, TransType.Rent.getValue());
    }

}
