package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.enums.SmsTypeEnum;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.sys.dataobj.form.SendSMSForm;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    /**
     * 发送注册用户的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/register")
    public ResponseObject sendRegisterSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.Register.getValue(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

    /**
     * 发送重置密码的短信验证码
     *
     * @param sendSMSForm 发送短信表单
     * @return ResponseObject
     */
    @PostMapping("/password")
    public ResponseObject sendResetPasswordSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(SmsTypeEnum.ResetPassword.getValue(), sendSMSForm.getMobile());
        return new ResponseObject();
    }

}
