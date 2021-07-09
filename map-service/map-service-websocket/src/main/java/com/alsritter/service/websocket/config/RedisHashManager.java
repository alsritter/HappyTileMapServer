package com.alsritter.service.websocket.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author alsritter
 * @version 1.0
 **/
@Slf4j
@Service("redisHashManager")
@Setter(onMethod_ = {@Autowired})
public class RedisHashManager<T> {
    private RedisTemplate<String, Object> jsonRedisTemplate;

    /**
     * 写去缓存 hash
     *
     * @param key        key
     * @param hashKey    hashKey
     * @param value      value
     * @param expireTime 过期时间
     * @return 结果
     */
    public boolean put(final String key, String hashKey, T value, Long expireTime) {
        boolean result = false;
        try {
            jsonRedisTemplate.opsForHash().put(key, hashKey, value);
            jsonRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            log.error("写入redis失败", e);
        }
        return result;
    }


    /**
     * 写去缓存 hash
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     * @return 结果
     */
    public boolean put(final String key, String hashKey, T value) {
        boolean result = false;
        try {
            jsonRedisTemplate.opsForHash().put(key, hashKey, value);
            result = true;
        } catch (Exception e) {
            log.error("写入redis失败", e);
        }
        return result;
    }

    /**
     * 读取缓存 Hash
     *
     * @param key     key
     * @param hashKey hashKey
     * @return object
     */
    public T get(final String key, String hashKey) {
        return (T) jsonRedisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 删除缓存 Hash
     *
     * @param key     key
     * @param hashKey hashKey
     * @return object
     */
    public Long delete(final String key, String hashKey) {
        return jsonRedisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 删除缓存Hash
     *
     * @param key key
     * @return object
     */
    public Boolean delete(final String key) {
        return jsonRedisTemplate.delete(key);
    }

    /**
     * 删除缓存Hash
     *
     * @param key key
     */
    public void deleteAll(final String key) {
        HashOperations<String, Object, T> operations = jsonRedisTemplate.opsForHash();
        Set<Object> hashKeySet = operations.keys(key);
        for (Object object : hashKeySet) {
            operations.delete(key, String.valueOf(object));
        }
    }

    /**
     * redis
     *
     * @param jsonRedisTemplate jsonRedisTemplate
     */
    public void setJsonRedisTemplate(RedisTemplate<String, Object> jsonRedisTemplate) {
        this.jsonRedisTemplate = jsonRedisTemplate;
    }
}
