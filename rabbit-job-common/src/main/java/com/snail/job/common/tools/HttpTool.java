package com.snail.job.common.tools;

import com.google.gson.reflect.TypeToken;
import com.snail.job.common.model.ResultT;
import com.snail.job.common.constant.HttpConstants;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 吴庆龙
 * @date 2020/5/22 4:38 下午
 */
public class HttpTool {
    private static final Logger log = LoggerFactory.getLogger(HttpTool.class);

    private static final HttpClient CLIENT;

    static {
        CLIENT = HttpClientBuilder.create()
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(50)
                .setDefaultRequestConfig(
                        RequestConfig.custom()
                                .setConnectionRequestTimeout(3000)
                                .setConnectTimeout(5000)
                                .setSocketTimeout(5000)
                                .setAuthenticationEnabled(false)
                                .setRedirectsEnabled(false)
                                .build()
                )
                .disableRedirectHandling()
                .disableCookieManagement()
                .disableAutomaticRetries()
                .disableAuthCaching()
                .evictIdleConnections(10, TimeUnit.MINUTES)
                .setDefaultConnectionConfig(
                        ConnectionConfig.custom()
                                .setCharset(StandardCharsets.UTF_8)
                                .build()
                )
                .build();
    }

    /**
     * 发送Post请求
     */
    public static ResultT<String> post(String url, Object bodyObj, String secretKey) {
        try {
            HttpPost post = new HttpPost(url);

            // 请求体
            String bodyData = GsonTool.toJson(bodyObj);
            post.setEntity(new StringEntity(bodyData, StandardCharsets.UTF_8));

            // 当前时间戳
            String timestamp = System.currentTimeMillis() + "";

            // 加签
            String sign = SignTool.sign(bodyData, secretKey, timestamp);

            // 请求头
            post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            post.setHeader(HttpConstants.SIGN, sign);
            post.setHeader(HttpConstants.TIMESTAMP, timestamp);

            // 执行请求
            HttpResponse httpResponse = CLIENT.execute(post);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpConstants.SUCCESS_CODE) {
                return new ResultT<>(ResultT.FAIL_CODE, "请求接口错误，响应码:" + statusCode);
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            String respJson = EntityUtils.toString(httpEntity, UTF_8);
            return GsonTool.fromJson(respJson, new TypeToken<ResultT<String>>() {
            });
        } catch (Exception e) {
            log.error("Http请求异常。{}", StrTool.stringifyException(e));
            return new ResultT<>(ResultT.FAIL_CODE, "未知异常" + e.getMessage());
        }
    }
}