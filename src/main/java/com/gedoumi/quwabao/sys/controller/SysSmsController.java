package com.gedoumi.quwabao.sys.controller;

import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.common.validate.MobilePhone;
import com.gedoumi.quwabao.sys.service.SysSmsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     * 发送短信验证码
     *
     * @param sendType 短信类型
     * @param mobile   手机号
     * @param code     图片验证码
     * @return ResponseObject
     */
    @GetMapping("/{sendType}/{mobile}/{code}")
    public ResponseObject sendSms(@PathVariable String sendType, @MobilePhone @PathVariable String mobile, @PathVariable String code) {
        sysSmsService.sendSms(sendType, mobile, code);
        return new ResponseObject();
    }

}
