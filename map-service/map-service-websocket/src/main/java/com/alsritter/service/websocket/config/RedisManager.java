package com.alsritter.service.websocket.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 单个 json 对象 Redis 存储
 *
 * @author alsritter
 * @version 1.0
 **/
@Service("redisManager")
@Setter(onMethod_ = {@Autowired})
public class RedisManager {

    /**
     * json 格式 Redis 模板
     */
    private RedisTemplate<String, Object> jsonRedisTemplate;

    /**
     * 批量删除对应的 value
     *
     * @param keys keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除 key
     *
     * @param pattern pattern
     */
    public void removePattern(final String pattern) {
        final Set<String> keys = jsonRedisTemplate.keys(pattern);

        if ((keys != null ? keys.size() : 0) > 0) {
            jsonRedisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的 value
     *
     * @param key key
     */
    public void remove(final String key) {
        if (exists(key)) {
            jsonRedisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的 value
     *
     * @param key key
     * @return value
     */
    public boolean exists(final String key) {
        return jsonRedisTemplate.hasKey(key);
    }

    /**
     * 获取所有的 key
     *
     * @return key
     */
    public Set<String> keys() {
        return jsonRedisTemplate.keys("*");
    }

    /**
     * 读取缓存
     *
     * @param key key
     * @return object
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<String, Object> operations = jsonRedisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key   key
     * @param value value
     * @return result
     */
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = jsonRedisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime expireTime
     * @return 结果
     */
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<String, Object> operations = jsonRedisTemplate.opsForValue();
            operations.set(key, value);
            jsonRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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