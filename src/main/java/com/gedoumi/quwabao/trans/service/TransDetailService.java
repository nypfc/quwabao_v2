package com.gedoumi.quwabao.trans.service;

import com.gedoumi.quwabao.common.enums.CodeEnum;
import com.gedoumi.quwabao.common.enums.TransStatusEnum;
import com.gedoumi.quwabao.common.enums.TransTypeEnum;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.MD5EncryptUtil;
import com.gedoumi.quwabao.sys.service.SysConfigService;
import com.gedoumi.quwabao.trans.dataobj.model.TransDetail;
import com.gedoumi.quwabao.trans.mapper.TransDetailMapper;
import com.gedoumi.quwabao.user.dataobj.form.TransferForm;
import com.gedoumi.quwabao.user.dataobj.model.User;
import com.gedoumi.quwabao.user.service.UserAssetDetailService;
import com.gedoumi.quwabao.user.service.UserAssetService;
import com.gedoumi.quwabao.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static com.gedoumi.quwabao.common.constants.Constants.SCALE;

/**
 * 交易Service
 *
 * @author Minced
 */
@Slf4j
@Service
public class TransDetailService {

    @Resource
    private TransDetailMapper transDetailMapper;

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 转账
     *
     * @param transferForm 转账表单
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferForm transferForm) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        String mobile = user.getMobilePhone();
        log.info("从：{} 向：{} 转账，转账金额：{}", mobile, transferForm.getToMobile(), transferForm.getTransMoney());
        // 获取参数
        BigDecimal transMoney = new BigDecimal(transferForm.getTransMoney());
        String toMobile = transferForm.getToMobile();
        String password = transferForm.getPassword();
        Long fromUserId = user.getId();  // 转账人ID
        // 验证参数
        if (mobile.equals(toMobile)) {
            log.error("手机号：{}向自身：{}转账", toMobile, mobile);
            throw new BusinessException(CodeEnum.TransSelfError);
        }
        String encrypyPassword = MD5EncryptUtil.md5Encrypy(password, MD5EncryptUtil.md5Encrypy(mobile));
        if (!user.getPayPassword().equals(encrypyPassword)) {
            log.error("密码：{}错误", password);
            throw new BusinessException(CodeEnum.PasswordError);
        }
        User toUser = Optional.ofNullable(userService.getByMobile(toMobile)).orElseThrow(() -> {
            log.error("被转账手机号：{}不存在", toMobile);
            return new BusinessException(CodeEnum.TransMobileNotExist);
        });
        Long toUserId = toUser.getId();  // 被转账人ID
        // 余额判断
        if (!userAssetService.remainAsset(fromUserId, transMoney)) {
            log.error("转账手机号:{}余额不足", mobile);
            throw new BusinessException(CodeEnum.RemainAssetError);
        }
        // 创建转账详情
        Date now = new Date();
        TransDetail transDetail = new TransDetail();
        transDetail.setCreateTime(now);
        transDetail.setUpdateTime(now);
        transDetail.setMoney(transMoney);
        transDetail.setFromUserId(fromUserId);
        transDetail.setToUserId(toUserId);
        transDetail.setTransStatus(TransStatusEnum.Success.getValue());
        transDetailMapper.insert(transDetail);
        // 计算手续费与实际接收的金额
        BigDecimal transFeeProportion = sysConfigService.getSysConfig().getTransFeeProportion();
        BigDecimal transFee = transMoney.multiply(transFeeProportion).setScale(SCALE, BigDecimal.ROUND_DOWN).stripTrailingZeros();
        BigDecimal receiveMoney = transMoney.subtract(transFee).setScale(SCALE, BigDecimal.ROUND_DOWN).stripTrailingZeros();
        // 变更转账人与被转账人的资产，注意被转账人金额转为负数
        userAssetService.updateUserAsset(fromUserId, transMoney.negate(), false);
        userAssetService.updateUserAsset(toUserId, receiveMoney, false);
        // 创建转账人与被转账人的资产详情
        userAssetDetailService.insertUserDetailAsset(fromUserId, toUserId, transMoney, null,
                BigDecimal.ZERO, BigDecimal.ZERO, TransTypeEnum.TransOut.getValue(), transFee);
        userAssetDetailService.insertUserDetailAsset(toUserId, fromUserId, receiveMoney, null,
                BigDecimal.ZERO, BigDecimal.ZERO, TransTypeEnum.TransIn.getValue(), null);
    }

}
