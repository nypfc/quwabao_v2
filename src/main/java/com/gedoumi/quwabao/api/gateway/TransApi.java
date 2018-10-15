package com.gedoumi.quwabao.api.gateway;

import com.gedoumi.quwabao.api.gateway.vo.QueryVO;
import com.gedoumi.quwabao.api.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.api.gateway.vo.WithDrawVO;
import com.gedoumi.quwabao.asset.vo.AppWithDrawVO;
import com.gedoumi.quwabao.common.base.ResponseObject;
import com.gedoumi.quwabao.common.utils.HttpClientUtils;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.PropertiesUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TransApi {

    static Logger logger = LoggerFactory.getLogger(TransApi.class);

    public static String test_url = "http://192.168.12.41:8090";
    public static String url = "http://175.25.17.53:8098";
    public static String path = "/api/v1/pfc/recharge";
    public static String path_get_address = "/api/v1/pfc_eth/query";
    public static String path_bind_address = "/api/v1/pfc_eth/bind";
    public static String path_withdraw = "/api/v1/pfc/withdraw";

    public static String getUrl(){
        String value = PropertiesUtils.getInstance().getValue("gateway.url");
        if(StringUtils.isNotEmpty(value)){
            return value;
        }
        return url;
    }

    public static String getTestUrl(){
        String testValue = PropertiesUtils.getInstance().getValue("test.url");
        if(StringUtils.isNotEmpty(testValue)){
            return testValue;
        }
        return test_url;
    }


    public static ApiResponse getEthAddress(QueryVO queryVO) throws Exception{
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        params.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        params.add(new BasicNameValuePair("sig", queryVO.getSig()));
        String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        logger.info("paramStr = {}", paramStr);
        HttpGet httpGet = new HttpGet(getUrl() + path_get_address +"?" + paramStr);

        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpGet);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ApiBindResponse postBindEthAddress(QueryVO queryVO) throws Exception{
        HttpPost httpPost=new HttpPost(getUrl() + path_bind_address);
//        String result = getPostResult(httpPost, JSON.toJSONString(queryVO));
        String result = getFormString(httpPost, getFormEntity(queryVO));
        return JsonUtil.jsonToPojo(result, ApiBindResponse.class);
    }

    private static String getPostResult(HttpPost httpPost, String jsonString) throws IOException {
        httpPost.setHeader("Content-Type", "application/json");
        logger.info("json = {}", jsonString);
        httpPost.setEntity(new StringEntity(jsonString));

//        httpPost.setEntity(getFormEntity(rechargeVO));
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return result;
    }


    private static String getFormString(HttpPost httpPost, HttpEntity formEntity) throws IOException {
        logger.info("formEntity = {}", formEntity);
//        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(formEntity);
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return result;
    }



    public static ApiResponse postWithDrawToGateWay(WithDrawVO withDrawVO) throws Exception{
        logger.info("withDrawVO = {}", JsonUtil.objectToJson(withDrawVO));
        HttpPost httpPost=new HttpPost(getUrl() + path_withdraw);
//        String result = getPostResult(httpPost, JSON.toJSONString(withDrawVO));
        String result = getFormString(httpPost, getFormEntity(withDrawVO));
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ApiResponse postRechargeToPFC(RechargeVO rechargeVO) throws Exception {
        logger.info("rechargeVO {}", JsonUtil.objectToJson(rechargeVO));
        HttpPost httpPost=new HttpPost(getTestUrl() + path);
//        String result = getPostResult(httpPost, JSON.toJSONString(rechargeVO));
        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ResponseObject postWithDrawToPFC(AppWithDrawVO appWithDrawVO) throws Exception {
        logger.info("appWithDrawVO {}", JsonUtil.objectToJson(appWithDrawVO));
        HttpPost httpPost=new HttpPost("http://127.0.0.1:8090/v1/uasset/withdraw");
        httpPost.setHeader("auth-token","fc958585-fd80-4300-a475-5cdc596dac54");
        httpPost.setHeader("deviceid","999999999999");
        String result = getPostResult(httpPost, JsonUtil.objectToJson(appWithDrawVO));
//        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        return JsonUtil.jsonToPojo(result, ResponseObject.class);
    }


    public static HttpEntity getFormEntity(RechargeVO rechargeVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("amount", rechargeVO.getAmount()));
        formParams.add(new BasicNameValuePair("asset_name", rechargeVO.getAsset_name()));
        formParams.add(new BasicNameValuePair("pfc_account", rechargeVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("seq", rechargeVO.getSeq()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(rechargeVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", rechargeVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    public static HttpEntity getFormEntity(QueryVO queryVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", queryVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    public static HttpEntity getFormEntity(WithDrawVO withDrawVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("pfc_account", withDrawVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(withDrawVO.getTs())));
        formParams.add(new BasicNameValuePair("amount", withDrawVO.getAmount()));
        formParams.add(new BasicNameValuePair("asset_name", withDrawVO.getAsset_name()));
        formParams.add(new BasicNameValuePair("memo", withDrawVO.getMemo()));
        formParams.add(new BasicNameValuePair("sig", withDrawVO.getSig()));
        formParams.add(new BasicNameValuePair("seq", String.valueOf(withDrawVO.getSeq())));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }


}
