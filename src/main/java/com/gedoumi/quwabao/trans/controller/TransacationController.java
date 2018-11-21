package com.gedoumi.quwabao.trans.controller;

import com.gedoumi.quwabao.trans.service.TransDetailService;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.TransferForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 交易Controller
 *
 * @author Minced
 */
@Slf4j
@RequestMapping("/v2/trans")
@RestController
public class TransacationController {

    @Resource
    private TransDetailService transDetailService;

    /**
     * APP内用户转账
     *
     * @param transferForm 转账表单
     * @return ResponseObject
     */
    @PostMapping("/transfer")
    public ResponseObject transfer(@RequestBody @Valid TransferForm transferForm) {
        transDetailService.transfer(transferForm);
        return new ResponseObject();
    }

}
