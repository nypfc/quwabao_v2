package com.pfc.quwabao.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HttpClientUtils {

    final static int MAX_TOTAL = 200;
    final static int MAX_PERROUTE = 20;
    final static int RETRY_TIMES = 3;
    private static PoolingHttpClientConnectionManager cm ;
    private static HttpClientUtils instance;

    private HttpClientUtils() {
        try {
            //需要通过以下代码声明对https连接支持
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(null,
                    new TrustSelfSignedStrategy())
                    .build();
            HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
            //初始化连接管理器
            if(cm == null)
                cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            cm.setMaxTotal(MAX_TOTAL);
            // Increase default max connection per route to 20
            cm.setDefaultMaxPerRoute(MAX_PERROUTE);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public static HttpClientUtils getInstance() {
        if(instance == null){
            instance = new HttpClientUtils();
        }
        return instance;
    }

    public  CloseableHttpClient getHttpClient() {
        ServiceUnavailableRetryStrategy serviceUnavailableRetryStrategy = new ServiceUnavailableRetryStrategy() {
            /**
             * retry逻辑
             */
            @Override
            public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                if(response.getStatusLine().getStatusCode()==HttpStatus.SC_OK){
                    return false;
                }
                if (executionCount <= RETRY_TIMES){
                    log.info("{} resp status = {}, try times = {}",context.getAttribute("http.request"), response.getStatusLine(),executionCount);
                    return true;
                } else{
                    return false;
                }

            }

            /**
             * retry间隔时间
             */
            @Override
            public long getRetryInterval() {
                return 3000;
            }
        };


        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(
                    IOException exception,
                    int executionCount,
                    HttpContext context) {
                    if (executionCount > RETRY_TIMES) {
                        // Do not retry if over max retry count
                        return false;
                    }
                    if (exception instanceof ConnectTimeoutException) {
                        // Timeout
                        log.info("connect timeout , try times = {}", executionCount);
                        return true;
                    }
                    if (exception instanceof SocketTimeoutException) {
                        // Timeout
                        log.info("socket timeout , try times = {}", executionCount);
                        return true;
                    }
                    if (exception instanceof InterruptedIOException) {
                        // Connection refused
                        return false;
                    }
                    if (exception instanceof UnknownHostException) {
                        // Unknown host
                        return false;
                    }

                    if (exception instanceof SSLException) {
                        // SSL handshake exception
                        return false;
                    }
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                    if (idempotent) {
                        // Retry if the request is considered idempotent
                        return true;
                    }
                    return false;
            }

        };


        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(90000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setServiceUnavailableRetryStrategy(serviceUnavailableRetryStrategy)
                .setRetryHandler(myRetryHandler)
                .setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();


        log.info("httpClient {}",httpClient);
        log.info("PoolingHttpClientConnectionManager {}", cm);
        return httpClient;
    }

}
