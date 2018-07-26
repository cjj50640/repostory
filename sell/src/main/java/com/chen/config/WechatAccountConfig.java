package com.chen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application-prod.yml")
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    private String myAppId;

    private String MyAppSecret;

    /*商户号*/
    private String mchId;

    /*商户秘钥*/
    private String mchKey;

    /*商户证书路径*/
    private String keyPath;
}
