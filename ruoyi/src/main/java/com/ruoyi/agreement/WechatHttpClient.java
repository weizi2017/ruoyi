package com.ruoyi.agreement.Bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Mars协议
 * 不稳定
 *
 * @author zbm
 * @date 2019-08-17
 */
@Component
@ConditionalOnBean(OkHttpClient.class)
public class WechatHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(WechatHttpClient.class);

    @Value("${wechat.http.server:http://localhost:9000/weixin}")
    private String server;

    @Resource
    @Qualifier("defaultStringRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private com.ruoyi.agreement.Bean.ZeusHttpClient httpClient;

    private static final List<String> WHITELIST = Arrays.asList("weixin,fmessage,medianote,floatbottle,qmessage,qqmail,tmessage,weibo,filehelper".split(","));

    /**
     * 初始化
     * @param wxId
     * @param data62
     * @param name
     * @param proxy
     * @param proxyName
     * @param proxyPassword
     * @return
     */
    private InitBean WxInit(String wxId,String data62,String name,String proxy,String proxyName,String proxyPassword){
        Response response = null;
        try {
            String url = server+"?A=WXInit&t[]="+data62+"&t[]="+name+"&t[]="+proxy+"&t[]="+proxyName+"&t[]="+proxyPassword;
            response = httpClient.getResponse(url);
            if(response!=null){
                String responseBody = response.body().toString();
                InitBean initBean = JSON.parseObject(responseBody,InitBean.class);
                if(initBean!=null){
                     return initBean;
                }else{
                    logger.error("Uuid:{},初始化失败，获取的initBean为null", wxId, response.message());
                }
            }else{
                logger.error("Uuid:{},初始化失败，返回的response为null", wxId, response.message());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Uuid:{}, Auto AutoLogin error.", wxId, e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }



    /**
     * 是否是微信官方微信号
     *
     * @param userName
     * @return
     */
    public boolean isWxOffice(String userName) {
        if (userName.startsWith("gh_") || userName.contains("@chatroom") || WHITELIST.contains(userName)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是微信官方微信号
     *
     * @param userName
     * @return
     */
    public boolean isWxOfficeNoChatRoom(String userName) {
        if (userName.startsWith("gh_") || WHITELIST.contains(userName)) {
            return true;
        }
        return false;
    }



}
