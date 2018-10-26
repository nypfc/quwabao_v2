package com.gedoumi.quwabao.sys.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 短信监听Service
 *
 * @author Minced
 */
@Service
public class SmsListener implements MessageListener {

    @Resource
    private SysSmsService sysSmsService;

    /**
     * 过期短信通知
     *
     * @param message 消息
     * @param pattern topic pattern
     */
    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        String[] itemValue = new String(message.getBody()).split(":");
        if (!StringUtils.equals(itemValue[0], "sms"))
            return;
        sysSmsService.updateSmsStatus(itemValue[1]);
    }

}
