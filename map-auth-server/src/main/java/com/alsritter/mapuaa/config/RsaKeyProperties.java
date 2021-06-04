package com.alsritter.mapuaa.config;

import com.alsritter.common.util.RsaUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA 位置
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@ConfigurationProperties("rsa.key")     //指定配置文件的key
public class RsaKeyProperties {

    @Autowired
    private ResourceLoader resourceLoader;

    @NestedConfigurationProperty
    private String pubKeyPath;
    @NestedConfigurationProperty
    private String priKeyPath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void createKey() throws Exception {
        // 先取得路径，因为使用的是 classpath 的形式，所以得使用 Spring 提供的路径工具
        String pp = resourceLoader.getResource(pubKeyPath).getFile().getAbsolutePath();
        String pk = resourceLoader.getResource(priKeyPath).getFile().getAbsolutePath();

        System.out.println("公钥路径" + pp);

        this.publicKey = RsaUtils.getPublicKey(pp);
        this.privateKey = RsaUtils.getPrivateKey(pk);
    }
}
