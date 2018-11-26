package com.gedoumi.quwabao.request;

import com.gedoumi.quwabao.QuwabaoApplicationTests;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.sys.request.SMSRequest;
import com.gedoumi.quwabao.sys.request.response.SMSResponse;
import com.gedoumi.quwabao.trans.request.BindEthAddressRequest;
import com.gedoumi.quwabao.trans.request.QueryEthAddressRequest;
import com.gedoumi.quwabao.trans.request.response.BindEthAddressResponse;
import com.gedoumi.quwabao.trans.request.response.QueryEthAddressResponse;
import org.junit.Test;
import org.springframework.stereotype.Component;

@Component
public class RequestTest extends QuwabaoApplicationTests {

    @Test
    public void smsTest() {
        SMSRequest smsRequest = new SMSRequest("13810060370", "123456");
        SMSResponse smsResponse = smsRequest.execute();
        System.out.println(smsResponse);
    }

    @Test
    public void queryEthAddressTest() {
        QueryEthAddressRequest query = new QueryEthAddressRequest("13810060370");
        QueryEthAddressResponse response = query.execute();
        System.out.println(JsonUtil.objectToJson(response));
    }

    @Test
    public void bindEthAddressTest() {
        BindEthAddressRequest bind = new BindEthAddressRequest("13810060370");
        BindEthAddressResponse response = bind.execute();
        System.out.println(JsonUtil.objectToJson(response));
    }

}