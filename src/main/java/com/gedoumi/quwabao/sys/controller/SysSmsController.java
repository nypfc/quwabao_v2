package com.gedoumi.quwabao.sys.controller;

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

import static com.gedoumi.quwabao.common.constants.ApiConstants.APP_SMS;

/**
 * 短信Controller
 *
 * @author Minced
 */
@Validated
@RequestMapping(APP_SMS)
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
    @PostMapping("/send")
    public ResponseObject sendRegisterSms(@RequestBody @Valid SendSMSForm sendSMSForm) {
        sysSmsService.sendSms(sendSMSForm);
        return new ResponseObject();
    }

}
