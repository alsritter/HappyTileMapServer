package com.alsritter.oauth2.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取 RSA 公钥接口
 * <p>
 * 无需自己手动返回公钥，可用配置 .tokenKeyAccess("permitAll()") 返回
 * 参考资料：
 * https://www.jianshu.com/p/227f7e7503cb
 *
 * @author alsritter
 * @version 1.0
 **/
@AllArgsConstructor
@RestController
@RequestMapping("/rsa")
public class KeyPairController {

    // private final KeyPair keyPair;
    //
    // // 由于我们的网关服务需要 RSA 的公钥来验证签名是否合法，所以认证服务需要有个接口把公钥暴露出来；
    // @GetMapping("/publicKey")
    // public Map<String, Object> getPublicKet() {
    //     // 这个 JWKSet 是 nimbusds 这个工具包提供的
    //     // 这个工具的使用方法 https://segmentfault.com/a/1190000023411227
    //     // 注意：
    //     // JWS：JSON Web Signature，Digital signature/HMAC specification（签名）
    //     // JWE：JSON Web Encryption，Encryption specification（加密）
    //     // JWK：JSON Web Key，Public key specification
    //     // JWA：JSON Web Algorithms，Algorithms and identifiers specification（算法）
    //     // JWT：JSON Web token
    //     return new JWKSet(
    //             new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).build()
    //     ).toJSONObject();
    // }
}
