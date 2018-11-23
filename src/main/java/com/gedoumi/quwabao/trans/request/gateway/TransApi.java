package com.gedoumi.quwabao.trans.request.gateway;

import com.gedoumi.quwabao.trans.request.gateway.vo.AppWithDrawVO;
import com.gedoumi.quwabao.trans.request.gateway.vo.QueryVO;
import com.gedoumi.quwabao.trans.request.gateway.vo.RechargeVO;
import com.gedoumi.quwabao.trans.request.gateway.vo.WithDrawVO;
import com.gedoumi.quwabao.common.utils.ResponseObject;
import com.gedoumi.quwabao.common.utils.HttpClientUtils;
import com.gedoumi.quwabao.common.utils.JsonUtil;
import com.gedoumi.quwabao.common.utils.PropertiesUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TransApi {

    public static String path = "/api/v2/pfc/recharge";
    public static String path_get_address = "/api/v1/pfc_eth/query";
    public static String path_bind_address = "/api/v1/pfc_eth/bind";
    public static String path_withdraw = "/api/v1/pfc/withdraw";

    private static String getUrl() {
        return "http://139.198.12.20:4000";
    }

    private static String getTestUrl() {
        return "http://192.168.12.41:8090";
    }

    public static ApiResponse getEthAddress(QueryVO queryVO) throws Exception {
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        params.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        params.add(new BasicNameValuePair("sig", queryVO.getSig()));
        String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        log.info("paramStr = {}", paramStr);
        HttpGet httpGet = new HttpGet(getUrl() + path_get_address + "?" + paramStr);

        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpGet);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("call result = {}", result);
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ApiBindResponse postBindEthAddress(QueryVO queryVO) throws Exception {
        HttpPost httpPost = new HttpPost(getUrl() + path_bind_address);
        String result = getFormString(httpPost, getFormEntity(queryVO));
        return JsonUtil.jsonToPojo(result, ApiBindResponse.class);
    }

    private static String getPostResult(HttpPost httpPost, String jsonString) throws IOException {
        httpPost.setHeader("Content-Type", "application/json");
        log.info("json = {}", jsonString);
        httpPost.setEntity(new StringEntity(jsonString));
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("call result = {}", result);
        return result;
    }

    private static String getFormString(HttpPost httpPost, HttpEntity formEntity) throws IOException {
        log.info("formEntity = {}", formEntity);
        httpPost.setEntity(formEntity);
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        log.info("call result = {}", result);
        return result;
    }

    public static ApiResponse postWithDrawToGateWay(WithDrawVO withDrawVO) throws Exception {
        log.info("withDrawVO = {}", JsonUtil.objectToJson(withDrawVO));
        HttpPost httpPost = new HttpPost(getUrl() + path_withdraw);
        String result = getFormString(httpPost, getFormEntity(withDrawVO));
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ApiResponse postRechargeToPFC(RechargeVO rechargeVO) throws Exception {
        log.info("rechargeVO {}", JsonUtil.objectToJson(rechargeVO));
        HttpPost httpPost = new HttpPost(getTestUrl() + path);
        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        return JsonUtil.jsonToPojo(result, ApiResponse.class);
    }

    public static ResponseObject postWithDrawToPFC(AppWithDrawVO appWithDrawVO) throws Exception {
        log.info("appWithDrawVO {}", JsonUtil.objectToJson(appWithDrawVO));
        HttpPost httpPost = new HttpPost("http://127.0.0.1:8090/v1/uasset/withdraw");
        httpPost.setHeader("auth-token", "fc958585-fd80-4300-a475-5cdc596dac54");
        httpPost.setHeader("deviceid", "999999999999");
        String result = getPostResult(httpPost, JsonUtil.objectToJson(appWithDrawVO));
        return JsonUtil.jsonToPojo(result, ResponseObject.class);
    }

    private static HttpEntity getFormEntity(RechargeVO rechargeVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("amount", rechargeVO.getAmount()));
        formParams.add(new BasicNameValuePair("asset_name", rechargeVO.getAsset_name()));
        formParams.add(new BasicNameValuePair("pfc_account", rechargeVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("seq", rechargeVO.getSeq()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(rechargeVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", rechargeVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    private static HttpEntity getFormEntity(QueryVO queryVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("pfc_account", queryVO.getPfc_account()));
        formParams.add(new BasicNameValuePair("ts", String.valueOf(queryVO.getTs())));
        formParams.add(new BasicNameValuePair("sig", queryVO.getSig()));
        return new UrlEncodedFormEntity(formParams, "UTF-8");
    }

    private static HttpEntity getFormEntity(WithDrawVO withDrawVO) throws UnsupportedEncodingException {
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
