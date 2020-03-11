package com.ruoyi.agreement.Bean;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zbm
 * @date 2019-07-23
 */
public final class OkHttpClientDestroyer {

    public static final Logger logger = LoggerFactory.getLogger(OkHttpClientDestroyer.class);

    public static void destroy(OkHttpClient okHttpClient) {
        if (okHttpClient != null) {
            ConnectionPool connectionPool = okHttpClient.connectionPool();
            connectionPool.evictAll();
            logger.info("OkHttp connections idle: {}, all: {}", connectionPool.idleConnectionCount(), connectionPool.connectionCount());
            ExecutorService executorService = okHttpClient.dispatcher().executorService();
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
                logger.info("OkHttp ExecutorService closed.");
            } catch (InterruptedException e) {
                logger.warn("InterruptedException on destroying okhttp instance", e);
            }
        }
    }
}
