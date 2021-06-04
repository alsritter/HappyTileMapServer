package com.alsritter.common.util;

import cn.hutool.json.JSONUtil;
import com.alsritter.common.entity.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

/**
 * 这里使用公钥私钥的机制防止对称加密那种加密解密用的同一个密钥，而被逆推出的问题
 * 注意，这里使用 JWT 单纯就是 ”为了防篡改“（通过前面的数字签名保证防篡改），
 * 而不是用来 ”去中心“，所以并不会在里面存很多东西（就存个 ID 和权限），
 * 因为只凭借 JWT 是无法做到踢人下线，避免多端登陆之类的操作的，所以得搭配 Redis 来维护状态，
 *
 * @author alsritter
 * @version 1.0
 **/
public final class JwtUtil {

    private JwtUtil() {}

    // 这个 JWT_PAYLOAD_USER_KEY 用作负荷的 key
    private static final String JWT_PAYLOAD_USER_KEY = "user";

    /**
     * 私钥加密token
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT
     */
    public static String generateTokenExpireInMinutes(Object userInfo, PrivateKey privateKey, int expire) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, expire);

        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JSONUtil.toJsonStr(userInfo))
                .setId(createJTI())
                .setExpiration(calendar.getTime())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 私钥加密token
     *
     * @param userInfo   载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位秒
     * @return JWT
     */
    public static String generateTokenExpireInSeconds(Object userInfo, PrivateKey privateKey, int expire) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, expire);

        return Jwts.builder()
                .claim(JWT_PAYLOAD_USER_KEY, JSONUtil.toJsonStr(userInfo))
                .setId(createJTI())
                .setExpiration(calendar.getTime())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 公钥解析 token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    private static String createJTI() {
        return new String(Base64.getEncoder().encode(UUID.randomUUID().toString().getBytes()));
    }

    /**
     * 获取 token 中的用户信息
     * 因为是签名，所以用公钥解密
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey, Class<T> userType) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setUserInfo(JSONUtil.toBean(body.get(JWT_PAYLOAD_USER_KEY).toString(), userType));
        claims.setExpiration(body.getExpiration());
        return claims;
    }

    /**
     * 获取token中的载荷信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static <T> Payload<T> getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        Payload<T> claims = new Payload<>();
        claims.setId(body.getId());
        claims.setExpiration(body.getExpiration());
        return claims;
    }
}