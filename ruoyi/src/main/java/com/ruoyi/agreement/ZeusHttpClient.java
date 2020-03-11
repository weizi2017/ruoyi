package com.ruoyi.agreement.Bean;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zbm
 * @date 2019-07-23
 */
@Component("zeusHttpClient")
@ConditionalOnBean(OkHttpClient.class)
public class ZeusHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(ZeusHttpClient.class);

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    protected OkHttpClient okHttpClient;

    @PostConstruct
    public void init() {
        logger.info("Http client initialized 1: {}", okHttpClient);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @PreDestroy
    public void destroy() {
        OkHttpClientDestroyer.destroy(okHttpClient);
        logger.info("Http client destroyed.");
    }

    public Response postForResponse(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        return getOkHttpClient().newCall(requestBuilder.build()).execute();
    }

    public Response postForResponse(String url, byte[] bytes) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, bytes);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        return getOkHttpClient().newCall(requestBuilder.build()).execute();
    }

    public Response postForResponseWithProxy(String url, byte[] bytes, String ip, int port) throws IOException {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, bytes);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        OkHttpClient newOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .proxy(proxy)
                .retryOnConnectionFailure(true).build();
        return newOkHttpClient.newCall(requestBuilder.build()).execute();
    }

    public Response postForResponseWithHeaders(String url, String json, Map<String, String> headers) throws IOException {
        Assert.isTrue(!headers.isEmpty(), "Headers must not be empty !");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);
        for (String header : headers.keySet()) {
            requestBuilder.addHeader(header, headers.get(header));
        }
        return getOkHttpClient().newCall(requestBuilder.build()).execute();
    }

    public Response getResponse(String url) throws IOException {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();
        return getOkHttpClient().newCall(request).execute();
    }

    public Response getResponseWithHeaders(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        for (String header : headers.keySet()) {
            builder.addHeader(header, headers.get(header));
        }
        Request request = builder.build();
        return getOkHttpClient().newCall(request).execute();
    }


}
