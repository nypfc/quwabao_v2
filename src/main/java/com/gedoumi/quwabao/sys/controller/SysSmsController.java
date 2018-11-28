package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.enums.SmsTypeEnum;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.sys.dataobj.form.SendSMSForm;
import com.gedoumi.quwabao.sys.dataobj.model.SysZone;
import com.gedoumi.quwabao.sys.dataobj.vo.ZoneVO;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import com.gedoumi.quwabao.sys.service.SysZoneService;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 短信Controller
 *
 * @author Minced
 */
@Validated
@RequestMapping("/v2/sms")
@RestController
public class SysSmsController {

    @Resource
    private SysSmsService sysSmsService;

    @Resource
    private SysZoneService sysZoneService;

    /**
     * 获取国家编码
     *
     * @return ResponseObject
     */
    @GetMapping("/zone")
    public ResponseObject getZoneList() {
        List<SysZone> sysZones = sysZoneService.getAllSysZone();
        return new ResponseObject<>(sysZones.stream().map(sysZone -> {
            ZoneVO zoneVO = new ZoneVO();
            BeanUtils.copyProperties(sysZone, zoneVO);
            return zoneVO;
        }).collect(Collectors.toList()));
    }

    /**
     * 发送注册用户的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/register")
    public ResponseObject sendRegisterSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.REGISTER.getValue(), sendSMSForm.getZone(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

    /**
     * 发送重置密码的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/reset/password")
    public ResponseObject sendResetPasswordSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.RESET_PASSWORD.getValue(), sendSMSForm.getZone(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

    /**
     * 发送修改支付密码的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/update/payPassword")
    public ResponseObject sendUpdatePayPasswordSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.UPDATE_PAY_PASSWORD.getValue(), sendSMSForm.getZone(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

    /**
     * 发送修改手机号的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/update/mobile")
    public ResponseObject sendUpdateMobileSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.UPDATE_MOBILE.getValue(), sendSMSForm.getZone(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

}
