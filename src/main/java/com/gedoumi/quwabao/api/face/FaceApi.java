package com.gedoumi.quwabao.api.face;

import com.gedoumi.quwabao.api.face.vo.FaceVO;
import com.gedoumi.quwabao.common.utils.HttpClientUtils;
import com.gedoumi.quwabao.util.JsonUtil;
import com.gedoumi.quwabao.util.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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

public class FaceApi {

    static Logger logger = LoggerFactory.getLogger(FaceApi.class);

    public static String url = "http://120.27.32.132:8080";
    public static String path = "/idFace/server";
    public static String mall_id = "111381";


    public static String getUrl(){
        String value = PropertiesUtils.getInstance().getValue("faceapi.url");
        if(StringUtils.isNotEmpty(value)){
            return value;
        }
        return url;
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
        httpPost.setHeader("Connection","close");
        httpPost.setEntity(formEntity);
        HttpResponse resp = HttpClientUtils.getInstance().getHttpClient().execute(httpPost);
        HttpEntity entity = resp.getEntity();
        String result = EntityUtils.toString(entity, "UTF-8");
        logger.info("call result = {}", result);
        return result;
    }



    public static FaceApiResponse testFace(FaceVO faceVO) throws Exception {
        logger.info("faceVO {}", faceVO);
        HttpPost httpPost=new HttpPost(getUrl()+path);
        String result = getFormString(httpPost, getFormEntity(faceVO));
//        String result = getFormString(httpPost, getFormEntity(rechargeVO));
        logger.info("result = {}", result);
        return JsonUtil.jsonToPojo(result, FaceApiResponse.class);
    }


    public static HttpEntity getFormEntity(FaceVO faceVO) throws UnsupportedEncodingException {
        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("mall_id", faceVO.getMall_id()));
        formParams.add(new BasicNameValuePair("realname", faceVO.getRealname()));
        formParams.add(new BasicNameValuePair("idcard", faceVO.getIdcard()));
        formParams.add(new BasicNameValuePair("tm", faceVO.getTm()));
        formParams.add(new BasicNameValuePair("image", faceVO.getImage()));
        formParams.add(new BasicNameValuePair("sign", faceVO.getSign()));

        return new UrlEncodedFormEntity(formParams, "UTF-8");
//        return new GzipCompressingEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));

    }


}
