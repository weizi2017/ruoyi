package com.ruoyi.agreement;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zbm
 * @date 2019-08-19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({com.ruoyi.agreement.Bean.OkHttpConfiguration.class, com.ruoyi.agreement.Bean.ZeusHttpClient.class, com.ruoyi.agreement.Bean.WechatHttpClient.class})
public @interface EnableWechatHttpClient {

}
