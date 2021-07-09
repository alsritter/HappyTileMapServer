package com.alsritter.service.websocket.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置 RedisTemplate
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
// @EnableRedisRepositories
public class RedisRepositoryConfig {

    /**
     * 自定义 JSON Template
     *
     * @param connectionFactory redisConnectionFactory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> jsonRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // 使用 Jackson2JsonRedisSerializer 来序列化和反序列化 Redis 的 Value
        Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 自定义的一个映射配置（ObjectMapper 提供了读取和写入 JSON 的功能）
        ObjectMapper mapper = new ObjectMapper();
        // 设置未找到该属性的值时不抛出错误
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 默认设置属性有非空这个注释
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // 该方法是指定序列化输入的类型，就是将数据库里的数据按照一定类型存储到 redis 缓存中。
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL); // 新版本要使用这个方法代替

        // 为什么要指定序列化输入类型？
        // 1、没有指定序列化输入类型的情况：
        // 如果注释掉 enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)，那存储到 redis 里的数据将是没有类型的纯 json，
        // 我们调用 redis API 获取到数据后，java 解析将是一个 LinkHashMap 类型的 key-value 的数据结构，我们需要使用的话就要自行解析，这样增加了编程的复杂度。
        // [{"id":72,"uuid":"c4d7fc52-4096-4c79-81ef-32cb1b87fd28","type":2}]

        // 2、指定序列化输入类型的情况：
        // 指定 enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL) 的话，存储到 Redis 里的数据将是有类型的 json 数据，例如：
        // ["java.util.ArrayList",[{"@class":"com.model.app","id":72,"uuid":"c4d7fc52-4096-4c79-81ef-32cb1b87fd28","type":2}]]
        // 这样 java 获取到数据后，将会将数据自动转化为 java.util.ArrayList 和 com.model.app，方便直接使用。

        serializer.setObjectMapper(mapper);

        // 使用 StringRedisSerializer 来序列化和反序列化 redis 的 key 值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * redis模板，主要用于存放序列化对象
     *
     * @param redisConnectionFactory redisConnectionFactory
     * @return redis模板
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 这个 @ConditionalOnMissingBean 注解作用在 @bean 定义上，
     * 它的作用就是在容器加载它作用的 bean 时，检查容器中是否存在目标类型（ConditionalOnMissingBean 注解的 value 值）的 bean 了，
     * 如果存在，则跳过原始 bean 的 BeanDefinition 加载动作。
     */
    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
