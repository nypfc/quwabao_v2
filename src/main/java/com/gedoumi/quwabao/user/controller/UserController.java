package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.enums.UserProfitVOEnum;
import com.gedoumi.quwabao.common.utils.ContextUtil;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.sys.dataobj.model.SysRent;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.dataobj.dto.UserRentNumberDTO;
import com.gedoumi.quwabao.user.dataobj.form.*;
import com.gedoumi.quwabao.user.dataobj.model.*;
import com.gedoumi.quwabao.user.dataobj.vo.*;
import com.gedoumi.quwabao.user.service.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户Controller
 *
 * @author Minced
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/v2/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserAssetDetailService userAssetDetailService;

    @Resource
    private UserRentService userRentService;

    @Resource
    private UserTeamExtService userTeamExtService;

    @Resource
    private SysRentService sysRentService;

    @Resource
    private UserProfitService userProfitService;

    @Resource
    private UserTreeService userTreeService;

    /**
     * 获取用户数据
     *
     * @return ResponseObject
     */
    @GetMapping("/info")
    public ResponseObject getUserInfo() {
        // 获取用户信息
        User user = ContextUtil.getUserFromRequest();
        // 获取用户资产信息
        UserAsset userAsset = userAssetService.getUserAsset(user.getId());
        // 获取用户团队信息
        UserTeamExt userTeamExt = userTeamExtService.getUserTeamExt(user.getId());
        // 封装返回信息
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setMobilePhone(user.getMobilePhone());
        userInfoVO.setInviteCode(user.getInviteCode());

        UserAssetVO userAssetVO = new UserAssetVO();
        userAssetVO.setRemainAsset(userAsset.getRemainAsset().stripTrailingZeros().toPlainString());
        userAssetVO.setTotalProfit(userAsset.getTotalAsset().stripTrailingZeros().toPlainString());

        UserTeamInfoVO teamInfoVO = new UserTeamInfoVO();
        teamInfoVO.setTeamLevel(userTeamExt.getTeamLevel());
        teamInfoVO.setTotalRentMoney(userTeamExt.getTeamTotalRent().stripTrailingZeros().toPlainString());

        UserVO userVO = new UserVO();
        userVO.setUser(userInfoVO);
        userVO.setUserAsset(userAssetVO);
        userVO.setUserTeam(teamInfoVO);

        return new ResponseObject<>(userVO);
    }

    /**
     * 重置密码（忘记密码）
     *
     * @param resetPasswordForm 重置密码表单
     * @return ResponseObject
     */
    @PutMapping("/password/reset")
    public ResponseObject resetPassword(@RequestBody @Valid ResetPasswordForm resetPasswordForm) {
        User user = userService.resetPassword(resetPasswordForm);
        // 封装返回数据
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>(loginTokenVO);
    }

    /**
     * 修改密码
     *
     * @param updatePasswordForm 修改密码表单
     * @return ResponseObject
     */
    @PutMapping("/password")
    public ResponseObject updatePassword(@RequestBody @Valid UpdatePasswordForm updatePasswordForm) {
        userService.updatePassword(updatePasswordForm);
        return new ResponseObject();
    }

    /**
     * 修改用户名
     *
     * @param updateUsernameForm 修改用户名表单
     * @return ResponseObject
     */
    @PutMapping("/username")
    public ResponseObject updateUsername(@RequestBody @Valid UpdateUsernameForm updateUsernameForm) {
        userService.updateUsername(updateUsernameForm);
        return new ResponseObject<>();
    }

    /**
     * 验证手机号
     *
     * @param mobile 手机号
     * @return ResponseObject
     */
    @GetMapping("/check/mobile/{mobile}")
    public ResponseObject checkMobilePhone(@MobilePhone @PathVariable String mobile) {
        return new ResponseObject<>(userService.checkMobilePhone(mobile));
    }

    /**
     * 验证邀请码
     *
     * @param inviteCode 邀请码
     * @return ResponseObject
     */
    @GetMapping("/check/inviteCode/{inviteCode}")
    public ResponseObject checkInviteCode(@Length(min = 8, max = 8, message = "邀请码必须为8位") @PathVariable String inviteCode) {
        return new ResponseObject<>(userService.checkInviteCode(inviteCode));
    }

    /**
     * 验证用户名
     *
     * @param username 用户名
     * @return ResponseObject
     */
    @GetMapping("/check/username/{username}")
    public ResponseObject checkUserName(@PathVariable String username) {
        return new ResponseObject<>(userService.checkUsername(username));
    }

    /**
     * 注册
     *
     * @param registerForm 注册表单
     * @return ResponseObject
     */
    @PostMapping("/register")
    public ResponseObject register(@RequestBody @Valid RegisterForm registerForm) {
        User user = userService.register(registerForm);
        // 封装返回数据
        LoginTokenVO loginTokenVO = new LoginTokenVO();
        loginTokenVO.setToken(user.getToken());
        return new ResponseObject<>();
    }

    /**
     * 获取用户已租用矿机列表
     *
     * @return ResponseObject
     */
    @GetMapping("/rent")
    public ResponseObject userRents() {
        // 获取用户信息
        User user = ContextUtil.getUserFromRequest();
        // 获取用户租用矿机信息
        List<UserRent> userRents = userRentService.getUserRents(user.getId());
        // 获取矿机信息
        List<SysRent> rents = sysRentService.getRents();
        // 遍历集合封装返回数据
        List<UserRentVO> userRentVOList = userRents.stream().map(userRent -> {
            UserRentVO userRentVO = new UserRentVO();
            // 遍历矿机，如果类型相同，设置名称
            for (SysRent sysRent : rents)
                if (sysRent.getRentCode().equals(userRent.getRentType()))
                    userRentVO.setRentName(sysRent.getName());
            userRentVO.setLastDig(userRent.getLastDig().stripTrailingZeros().toPlainString());
            // 矿机剩余收益
            BigDecimal remainProfit = userRent.getTotalAsset().subtract(userRent.getAlreadyDig())
                    .setScale(5, BigDecimal.ROUND_DOWN).stripTrailingZeros();
            userRentVO.setRemainProfit(remainProfit.toPlainString());
            return userRentVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(userRentVOList);
    }

    /**
     * 矿机租用
     *
     * @param rentForm 租用表单
     * @return ResponseObject
     */
    @PostMapping("/rent")
    public ResponseObject rent(@RequestBody @Valid RentForm rentForm) {
        userRentService.rent(rentForm);
        return new ResponseObject();
    }

    /**
     * 获取用户团队信息
     * 只显示下一级
     *
     * @return ResponseObject
     */
    @GetMapping("/team")
    public ResponseObject getUserTeamList() {
        User user = ContextUtil.getUserFromRequest();
        // 获取当前用户的下级用户
        List<User> users = userTreeService.getChildUser(user.getId());
        // 没有用户直接返回空集合
        if (CollectionUtils.isEmpty(users))
            return new ResponseObject<>(users);
        // 获取下级用户的ID集合并获取矿机数量
        List<Long> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        List<UserRentNumberDTO> numbers = userRentService.getUserRentNumber(userIds);
        // 封装返回信息
        List<UserTeamVO> userTeamVOList = users.stream().map(u -> {
            UserTeamVO userTeamVO = new UserTeamVO();
            userTeamVO.setMobile(u.getMobilePhone());
            userTeamVO.setUsername(u.getUsername());
            // 遍历矿机数量集合，如果ID相同，存入矿机数量
            for (UserRentNumberDTO number : numbers)
                if (number.getUserId().equals(u.getId()))
                    userTeamVO.setRentNumber(number.getNumber());
            return userTeamVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(userTeamVOList);
    }

    /**
     * 用户交易详情列表
     *
     * @param page 当前页码
     * @param rows 每页数据量
     * @return ResponseObject
     */
    @GetMapping("/trans/{page}/{rows}")
    public ResponseObject userTransactionList(@PathVariable String page, @PathVariable String rows) {
        User user = ContextUtil.getUserFromRequest();
        List<UserAssetDetail> userAssetDetailList = userAssetDetailService.getUserTransactionDetails(user.getId(), page, rows);
        // 封装返回信息
        List<UserAssetDetailVO> assetDetailVOs = userAssetDetailList.stream().map(userAssetDetail -> {
            UserAssetDetailVO assetDetailVO = new UserAssetDetailVO();
            assetDetailVO.setDay(userAssetDetail.getCreateTime());
            assetDetailVO.setTransType(userAssetDetail.getTransType());
            assetDetailVO.setTransMoney(String.valueOf(userAssetDetail.getMoney()));
            return assetDetailVO;
        }).collect(Collectors.toList());
        return new ResponseObject<>(assetDetailVOs);
    }

    /**
     * 用户收益详情列表
     *
     * @param page 当前页码
     * @param rows 每页数据量
     * @return ResponseObject
     */
    @GetMapping("/profit/{page}/{rows}")
    public ResponseObject userProfits(@PathVariable String page, @PathVariable String rows) {
        // 获取用户
        User user = ContextUtil.getUserFromRequest();
        // 遍历结果，封装数据
        List<UserProfitVO> profitVOs = Lists.newArrayList();
        List<UserProfit> profits = userProfitService.getUserProfits(user.getId(), page, rows);
        profits.forEach(userProfit -> {
            //日期
            Date date = userProfit.getDate();
            // 静态收益数据
            BigDecimal staticProfit = userProfit.getStaticProfit();
            if (staticProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO static_ = new UserProfitVO();
                static_.setDate(date);
                static_.setType(UserProfitVOEnum.STATIC_PROFIT.getValue());
                static_.setProfit(staticProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(static_);
            }
            // 动态收益数据
            BigDecimal dynamicProfit = userProfit.getDynamicProfit();
            if (dynamicProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO dynamic_ = new UserProfitVO();
                dynamic_.setDate(date);
                dynamic_.setType(UserProfitVOEnum.DYNAMIC_PROFIT.getValue());
                dynamic_.setProfit(dynamicProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(dynamic_);
            }
            // 俱乐部收益数据
            BigDecimal clubProfit = userProfit.getClubProfit();
            if (clubProfit.compareTo(BigDecimal.ZERO) > 0) {
                UserProfitVO club_ = new UserProfitVO();
                club_.setDate(date);
                club_.setType(UserProfitVOEnum.CLUB_PROFIT.getValue());
                club_.setProfit(clubProfit.stripTrailingZeros().toPlainString());
                profitVOs.add(club_);
            }
        });
        return new ResponseObject<>(profitVOs);
    }

}
