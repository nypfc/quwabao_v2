package com.gedoumi.quwabao.asset.controller;

import com.gedoumi.quwabao.api.gateway.ApiBindResponse;
import com.gedoumi.quwabao.api.gateway.ApiResponse;
import com.gedoumi.quwabao.api.gateway.TransApi;
import com.gedoumi.quwabao.api.gateway.vo.QueryVO;
import com.gedoumi.quwabao.api.gateway.vo.WithDrawVO;
import com.gedoumi.quwabao.asset.entity.UserAsset;
import com.gedoumi.quwabao.asset.entity.UserAssetDetail;
import com.gedoumi.quwabao.asset.entity.UserRent;
import com.gedoumi.quwabao.asset.service.AssetDetailService;
import com.gedoumi.quwabao.asset.service.UserAssetService;
import com.gedoumi.quwabao.asset.service.UserRentService;
import com.gedoumi.quwabao.asset.vo.*;
import com.gedoumi.quwabao.common.AppConfig;
import com.gedoumi.quwabao.common.annotation.PfcRepeatAspect;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.constants.Constants;
import com.gedoumi.quwabao.common.enums.*;
import com.gedoumi.quwabao.common.exception.BusinessException;
import com.gedoumi.quwabao.common.utils.*;
import com.gedoumi.quwabao.sys.entity.SysLog;
import com.gedoumi.quwabao.sys.entity.SysRent;
import com.gedoumi.quwabao.sys.service.SysLogService;
import com.gedoumi.quwabao.sys.service.SysRentService;
import com.gedoumi.quwabao.user.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/uasset")
public class UserAssetController {

    @Resource
    private UserAssetService userAssetService;

    @Resource
    private UserRentService userRentService;

    @Resource
    private AssetDetailService assetDetailService;

    @Resource
    private UserService userService;

    @Resource
    private SysRentService rentService;

    @Resource
    private SysLogService logService;

    @Resource
    private AppConfig appConfig;


    @RequestMapping("rentType")
    public ResponseObject rentType() {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setSuccess();
        List<RentTypeVO> rentTypeVOS = Lists.newArrayList();
        List<SysRent> sysRents = rentService.getAcviteList();
        for (SysRent rentType : sysRents) {
            RentTypeVO vo = new RentTypeVO();
            if (appConfig.getVersion() == VersionType.WithoutRecharge) {
                vo.setProfitMoney(rentType.getProfitMoney());
            } else {
                vo.setProfitMoney(rentType.getProfitMoneyExt());
            }

            vo.setRate(rentType.getRate());
            vo.setRentMoney(rentType.getMoney());
            //目前定义的是整数
            vo.setRentType(Integer.parseInt(rentType.getCode()));
            vo.setRentName(rentType.getName());
            vo.setDays(rentType.getDays());
            int orgRentSize = 0;
            List<UserRent> orgUserRents = userRentService.getByRentType(vo.getRentType());
            if (!CollectionUtils.isEmpty(orgUserRents)) {
                orgRentSize = orgUserRents.size();
            }
            if (orgRentSize < rentType.getMaxNumber()) {
                vo.setRentFlag(true);
            }
            rentTypeVOS.add(vo);
        }
        responseObject.setData(rentTypeVOS);
        return responseObject;
    }

    @RequestMapping("userInfo")
    public ResponseObject userInfo() {
        ResponseObject responseObject = new ResponseObject();
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        UserAsset userAsset = userAssetService.getUserAsset(user);
        UserRent userRent = userRentService.getAcviteByUser(user);
        UserAssetVO userAssetVO = new UserAssetVO();
        UserInfoVO userInfoVO = new UserInfoVO();
        userAssetVO.setUser(userInfoVO);

        if (appConfig.isOnTrans()) {
            userAssetVO.setOnLine(1);
        } else {
            userAssetVO.setOnLine(0);
        }

        if (userAsset != null) {
            BeanUtils.copyProperties(userAsset.getUser(), userInfoVO);
            BeanUtils.copyProperties(userAsset, userAssetVO);
        } else {
            BeanUtils.copyProperties(user, userInfoVO);
        }

        if (userRent != null) {
            userAssetVO.setRentType(userRent.getRentType());

//			RentType rentType = RentType.fromValue(userRent.getRentType());
            SysRent rentType = rentService.getByCode(String.valueOf(userRent.getRentType()));
            userAssetVO.setRentName(rentType.getName() + Constants.RENT_NAME);
            userAssetVO.setDigNumber(NumberUtil.randomInt(rentType.getDigMin(), rentType.getDigMax()));
            List<UserAssetDetail> assetDetailList = assetDetailService.getRentDetail(userRent);
            userAssetVO.setRentProfit(assetDetailService.getProfitAsset(assetDetailList));
            userAssetVO.setExpireDate(userRent.getExpireDate());
        } else {
            userAssetVO.setDigNumber(0);
        }
        Date yesterday = DateUtils.addDays(new Date(), -1);
        List<UserAssetDetail> assetDetailList = assetDetailService.getDetailsByDigDate(user, yesterday);
        userAssetVO.setLastDayProfit(assetDetailService.getProfitAsset(assetDetailList));


        responseObject.setData(userAssetVO);
        responseObject.setSuccess();
        return responseObject;
    }


    @PfcRepeatAspect
    @RequestMapping("rent")
    public ResponseObject rent(@RequestBody RentVO rentVO) {
        log.info("in rent rentVO = {}", JsonUtil.objectToJson(rentVO));
        ResponseObject responseObject = new ResponseObject();
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        UserAsset userAsset = userAssetService.getUserAsset(user);

        UserRent userRent = new UserRent();
        Date now = new Date();
        userRent.setUpdateTime(now);
        userRent.setCreateTime(now);
        userRent.setUser(user);
        SysRent rentType = rentService.getByCode(String.valueOf(rentVO.getRentType()));
        if (rentType == null) {
            log.error("rent type error , type value = {}", rentVO.getRentType());
            responseObject.setInfo(CodeEnum.SysError);
            return responseObject;
        }
        int orgRentSize = 0;
        List<UserRent> orgUserRents = userRentService.getByRentType(rentVO.getRentType());
        if (!CollectionUtils.isEmpty(orgUserRents)) {
            orgRentSize = orgUserRents.size();
        }
        if (orgRentSize > rentType.getMaxNumber()) {
            responseObject.setInfo(CodeEnum.RentMaxError);
            return responseObject;
        }

        userRent.setRentType(rentVO.getRentType());
        userRent.setDays(rentType.getDays());
        userRent.setRentAsset(rentType.getMoney());
        userRent.setDigNumber(NumberUtil.randomInt(rentType.getDigMin(), rentType.getDigMax()));

        Date endDate = DateUtils.addDays(now, userRent.getDays());
        userRent.setExpireDate(endDate);
        try {
            userAssetService.addRent(userRent);
        } catch (BusinessException e) {
            e.printStackTrace();
            responseObject.setInfo(e.getCodeEnum());
            return responseObject;
        }
        responseObject.setData(userAsset);
        responseObject.setMessage(CodeEnum.Success.getMessage());
        responseObject.setCode(CodeEnum.Success.getCode());
        return responseObject;
    }


    @RequestMapping("transfer")
    public ResponseObject transfer(@RequestBody TransferVO transferVO) {
        log.info("transfer {}, from {} to {}", transferVO.getTransMoney(), transferVO.getFromMobile(), transferVO.getToMobile());
        ResponseObject responseObject = new ResponseObject();

        if (transferVO.getTransMoney().compareTo(BigDecimal.ZERO) <= 0) {
            responseObject.setInfo(CodeEnum.TransMoneyNegError);
            return responseObject;
        }

        User user = userService.getByMobilePhone(transferVO.getFromMobile());
        User toUser = userService.getByMobilePhone(transferVO.getToMobile());
        if (user == null || toUser == null) {
            responseObject.setInfo(CodeEnum.TransMobileError);
            return responseObject;
        }

        if (StringUtils.equals(user.getMobilePhone(), toUser.getMobilePhone())) {
            responseObject.setInfo(CodeEnum.TransSelfError);
            return responseObject;
        }

        try {
            userAssetService.transfer(transferVO);
        } catch (BusinessException e) {
            log.error(e.getCodeEnum().getMessage(), e);
            responseObject.setInfo(e.getCodeEnum());
            return responseObject;
        }
        responseObject.setMessage(CodeEnum.Success.getMessage());
        responseObject.setCode(CodeEnum.Success.getCode());
        return responseObject;
    }


    @PfcRepeatAspect
    @RequestMapping(value = "/withdraw")
    public ResponseObject withdraw(@RequestBody AppWithDrawVO vo) {
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        ResponseObject responseObject = new ResponseObject();
        long start = System.currentTimeMillis();
        Date now = new Date();
        if (StringUtils.isEmpty(vo.getAmount())) {
            responseObject.setInfo(CodeEnum.WithDrawAmountEmty);
            return responseObject;
        }

        if (StringUtils.isEmpty(vo.getEthAddress())) {
            responseObject.setInfo(CodeEnum.WithDrawAddressError);
            return responseObject;
        }

        BigDecimal withDraw = new BigDecimal(vo.getAmount());
        if (withDraw.compareTo(BigDecimal.ZERO) <= 0) {
            responseObject.setInfo(CodeEnum.WithDrawAmountEmty);
            return responseObject;
        }

        String salt = MD5EncryptUtil.md5Encrypy(user.getMobilePhone());
        String pswd = MD5EncryptUtil.md5Encrypy(vo.getPswd(), salt);
        if (!StringUtils.equals(pswd, user.getPassword())) {
            responseObject.setInfo(CodeEnum.TransPswdError);
            return responseObject;
        }

        BigDecimal dayLimit = new BigDecimal(PropertiesUtils.getInstance().getValue("withdraw.day.limit"));


        UserAsset userAsset = userAssetService.getUserAsset(user);

        if (userAsset == null || userAsset.getRemainAsset() == null || withDraw.compareTo(userAsset.getRemainAsset()) > 0) {
            responseObject.setInfo(CodeEnum.WithDrawAmountError);
            return responseObject;
        }

        Date startDate = PfcDateUtils.getDayStart(now);
        log.info("startDate {}", startDate);
        List<UserAssetDetail> details = assetDetailService.getByUserAndTransTypeAndCreateTimeAfter(user, TransType.NetOut.getValue(), startDate);
        BigDecimal total = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(details)) {
            for (UserAssetDetail detail : details) {
                total = total.add(detail.getMoney());
            }
        }

        if (withDraw.add(total).compareTo(dayLimit) > 0) {
            responseObject.setInfo(CodeEnum.WithDrawDayLimitError);
            return responseObject;
        }

        if (StringUtils.isEmpty(user.getEthAddress())) {
            log.info("还么有绑定eth地址");
            ResponseObject ethResp = getEthAddress();
            if (!StringUtils.equals(ethResp.getCode(), CodeEnum.Success.getCode())) {
                log.info("绑定eth地址失败");
                responseObject.setInfo(CodeEnum.GateWayError);
                return responseObject;
            }
            user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        }

        SysLog sysLog = new SysLog();

        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.WithDraw.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(StringUtils.EMPTY);
        sysLog.setMobile(user.getMobilePhone());
        sysLog.setLogStatus(SysLogStatus.Fail.getValue());
        logService.add(sysLog);

        WithDrawVO withDrawVO = new WithDrawVO();
        withDrawVO.setPfc_account(user.getMobilePhone());
        withDrawVO.setTs(System.currentTimeMillis() / 1000);
        withDrawVO.setAmount(vo.getAmount());
        withDrawVO.setMemo("erc20#" + vo.getEthAddress());
        withDrawVO.setAsset_name(Constants.ASSET_NAME);
        withDrawVO.setSeq(sysLog.getId());

        String sign = withDrawVO.generateSign(TransApi.path_withdraw);
        withDrawVO.setSig(sign);

        sysLog.setRequestBody(JsonUtil.objectToJson(withDrawVO));
        sysLog.setSeq(String.valueOf(withDrawVO.getSeq()));
        logService.update(sysLog);
        ApiResponse apiResponse = null;
        try {
            apiResponse = TransApi.postWithDrawToGateWay(withDrawVO);
            log.info("apiResponse = {}", JsonUtil.objectToJson(apiResponse));
            if (apiResponse.getCode() == Constants.API_SUCCESS_CODE) {
                sysLog.setLogStatus(SysLogStatus.Success.getValue());
                UserAssetDetail detail = new UserAssetDetail();
                detail.setUser(user);
                detail.setMoney(withDraw);
                detail.setProfit(BigDecimal.ZERO);
                detail.setProfitExt(BigDecimal.ZERO);
                detail.setTransType(TransType.NetOut.getValue());
                detail.setApiTransSeq(String.valueOf(withDrawVO.getSeq()));
                userAssetService.addAsset(detail);

            } else {
                responseObject.setInfo(CodeEnum.WithDrawError);
                sysLog.setLogStatus(SysLogStatus.Fail.getValue());
                return responseObject;
            }

        } catch (Exception e) {
            log.error("withdraw error ", e);
            responseObject.setInfo(CodeEnum.GateWayError);
            sysLog.setLogStatus(SysLogStatus.Fail.getValue());
            return responseObject;
        }
        logService.update(sysLog);

        responseObject.setSuccess();
        long end = System.currentTimeMillis();
        log.info("withdraw process time : {}ms", end - start);
        return responseObject;
    }

    @RequestMapping(value = "/getEthAddress")
    public ResponseObject getEthAddress() {
        ResponseObject responseObject = new ResponseObject();
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        if (StringUtils.isNotEmpty(user.getEthAddress())) {
            responseObject.setData(user.getEthAddress());
            responseObject.setSuccess();
            return responseObject;
        }

        QueryVO queryVO = new QueryVO();
        queryVO.setPfc_account(user.getMobilePhone());
        queryVO.setTs(System.currentTimeMillis() / 1000);

        String sign = queryVO.generateSign(TransApi.path_bind_address);
        queryVO.setSig(sign);
        SysLog sysLog = new SysLog();
        Date now = new Date();
        sysLog.setClientIp(SessionUtil.getClientIp());
        sysLog.setCreateTime(now);
        sysLog.setUpdateTime(now);
        sysLog.setLogType(LogType.BindEthAddress.getValue());
        sysLog.setRequestUrl(SessionUtil.getRequest().getRequestURI());
        sysLog.setRequestBody(JsonUtil.objectToJson(queryVO));
        sysLog.setMobile(queryVO.getPfc_account());
        sysLog.setLogStatus(SysLogStatus.Fail.getValue());
        logService.add(sysLog);
        ApiBindResponse apiResponse = null;
        try {
            apiResponse = TransApi.postBindEthAddress(queryVO);
            log.info("apiResponse = {}", apiResponse);
            if (apiResponse.getCode() == Constants.API_SUCCESS_CODE) {
                sysLog.setLogStatus(SysLogStatus.Success.getValue());
                QueryVO queryResult = apiResponse.getData();
                user.setEthAddress(queryResult.getEth_address());
            } else {
                sysLog.setLogStatus(SysLogStatus.Fail.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("get ethAddress error ", e);
            responseObject.setInfo(CodeEnum.GateWayError);
            sysLog.setLogStatus(SysLogStatus.Fail.getValue());
            return responseObject;
        }

        logService.update(sysLog);

        if (StringUtils.isNotEmpty(user.getEthAddress())) {
            userService.update(user);
            responseObject.setData(user.getEthAddress());
            responseObject.setSuccess();
            SessionUtil.getSession().setAttribute(Constants.API_USER_KEY, user);
        } else {
            responseObject.setInfo(CodeEnum.GateWayError);
        }

        return responseObject;
    }

    @RequestMapping(value = "/getWithDrawInfo")
    public ResponseObject getWithDrawInfo() {
        ResponseObject responseObject = new ResponseObject();
        User user = (User) SessionUtil.getSession().getAttribute(Constants.API_USER_KEY);
        BigDecimal singleLimit = new BigDecimal(PropertiesUtils.getInstance().getValue("withdraw.single.limit"));
        BigDecimal dayLimit = new BigDecimal(PropertiesUtils.getInstance().getValue("withdraw.day.limit"));
        BigDecimal remainLimit = dayLimit;

        Date startDate = PfcDateUtils.getDayStart(new Date());
        log.info("startDate {}", startDate);
        List<UserAssetDetail> details = assetDetailService.getByUserAndTransTypeAndCreateTimeAfter(user, TransType.NetOut.getValue(), startDate);
        if (!CollectionUtils.isEmpty(details)) {
            BigDecimal total = BigDecimal.ZERO;
            for (UserAssetDetail detail : details) {
                total = total.add(detail.getMoney());
            }
            remainLimit = dayLimit.subtract(total);
            if (remainLimit.compareTo(BigDecimal.ZERO) < 0) {
                remainLimit = BigDecimal.ZERO;
            }
        }
        WithDrawInfoVO withDrawInfoVO = new WithDrawInfoVO();
        withDrawInfoVO.setDayLimit(dayLimit);
        withDrawInfoVO.setSingleLimit(singleLimit);
        withDrawInfoVO.setRemainLimit(remainLimit);
        responseObject.setData(withDrawInfoVO);

        responseObject.setSuccess();
        return responseObject;
    }

}
