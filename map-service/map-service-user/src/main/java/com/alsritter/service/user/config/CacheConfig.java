package com.alsritter.service.user.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

/**
 * 配置 Cache
 * 参考资料 https://blog.csdn.net/f641385712/article/details/95234347
 *
 * @author alsritter
 * @version 1.0
 **/
@EnableCaching // 使用了CacheManager，别忘了开启它  否则无效
@Configuration
@RequiredArgsConstructor
public class CacheConfig extends CachingConfigurerSupport {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1)) // 默认没有特殊指定的
                .computePrefixWith(cacheName -> "CACHING:" + cacheName); // 自动加上前缀

        // // 针对不同 cacheName，设置不同的过期时间
        // Map<String, RedisCacheConfiguration> initialCacheConfiguration = new HashMap<>();
        // initialCacheConfiguration.put("demoCache", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1))); //1小时
        // initialCacheConfiguration.put("demoCar", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))); // 10分钟

        // return RedisCacheManager.builder(redisConnectionFactory)
        //         .cacheDefaults(defaultCacheConfig) // 默认配置（强烈建议配置上）。  比如动态创建出来的都会走此默认配置
        //         .withInitialCacheConfigurations(initialCacheConfiguration) // 不同 cache 的个性化配置
        //         .build();

        return new CustomRedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), defaultCacheConfig);
    }
}
