package com.gedoumi.quwabao.user.controller;

import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.user.dataobj.form.TransferForm;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户交易Controller
 *
 * @author Minced
 */
@RequestMapping("/v2/trans")
@RestController
public class TransacationController {

    /**
     * APP内用户转账
     *
     * @param transferForm 转账表单
     * @return ResponseObject
     */
    @PostMapping("/transfer")
    public ResponseObject transfer(@RequestBody @Valid TransferForm transferForm) {
        return new ResponseObject();
    }

}
