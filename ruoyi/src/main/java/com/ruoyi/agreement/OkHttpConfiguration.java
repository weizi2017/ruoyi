package com.ruoyi.agreement.Bean;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.TimeUnit;

/**
 * @author bill 2017-10-09 15:55
 */
@Configuration
public class OkHttpConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(OkHttpConfiguration.class);

    @Bean
    @Scope("prototype")
    public OkHttpClient okHttpClient(@Value("${okhttp.pool.maxIdleConnections:120}") int maxIdleConnections,
                                     @Value("${okhttp.pool.keepAliveDuration:500}") int keepAliveDuration,
                                     @Value("${okhttp.pool.connectTimeout:5}") int connectTimeout,
                                     @Value("${okhttp.pool.readTimeout:10}") int readTimeout,
                                     @Value("${okhttp.pool.writeTimeout:10}") int writeTimeout) {
        ConnectionPool pool = new ConnectionPool(maxIdleConnections, keepAliveDuration, TimeUnit.SECONDS);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .followRedirects(true)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .connectionPool(pool)
                .build();
        return client;
    }
}
