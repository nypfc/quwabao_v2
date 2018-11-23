package com.gedoumi.quwabao.request;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.sys.request.SMSRequest;
import com.gedoumi.quwabao.trans.request.impl.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.impl.QueryEthAddressRequest;
import org.junit.Test;
import org.springframework.stereotype.Component;

@Component
public class RequestTest extends QuwabaoApplicationTests {

    @Test
    public void smsTest() {
        SMSRequest smsRequest = new SMSRequest("13810060370", "123456");
        String execute = smsRequest.execute();
        System.out.println(execute);
    }

    @Test
    public void queryEthAddressTest() {
        QueryEthAddressRequest query = new QueryEthAddressRequest("13810060370");
        String result = query.execute();
        System.out.println(result);
    }

    @Test
    public void bindEthAddressTest() {
        BindEthAddressRequest bind = new BindEthAddressRequest("13810060370");
        String result = bind.execute();
        System.out.println(result);
    }

}