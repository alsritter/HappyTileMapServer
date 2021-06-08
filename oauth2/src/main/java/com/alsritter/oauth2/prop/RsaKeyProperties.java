package com.alsritter.oauth2.prop;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.PostConstruct;
import java.security.KeyPair;

/**
 * @author alsritter
 * @version 1.0
 **/
@Data
@ConfigurationProperties("rsa.key")     //指定配置文件的key
public class RsaKeyProperties {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 私钥加解密密码
     */
    @NestedConfigurationProperty
    private String keyPassword;

    /**
     * 文件存储密码
     */
    @NestedConfigurationProperty
    private String storePassword;

    /**
     * keystore 存储位置
     */
    @NestedConfigurationProperty
    private String path;

    /**
     * 实体别名(包括证书私钥)
     */
    @NestedConfigurationProperty
    private String alias;

    private KeyPair keyPair;

    @PostConstruct
    public void createKey() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resourceLoader.getResource(path), storePassword.toCharArray());
        this.keyPair = keyStoreKeyFactory.getKeyPair(alias, keyPassword.toCharArray());
    }
}
