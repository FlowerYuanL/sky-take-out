package com.sky.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("[RedisConfig] ==> 开始加载RedisTemplate配置信息");

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //注入连接工厂，由配置文件中的信息确定
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //设置主键序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //设置键值json序列化
        redisTemplate.setValueSerializer(buildRedisValueSerializer());
        redisTemplate.setHashValueSerializer(buildRedisValueSerializer());

        //初始化
        redisTemplate.afterPropertiesSet();
        log.info("[RedisConfig] <== RedisTemplate配置信息加载完成");
        return redisTemplate;
    }

    /**
     * 自定义RedisCacheManager，用于覆盖Spring Boot的自动配置。
     *
     * @param redisConnectionFactory Redis连接工厂，由Spring Boot自动注入。
     * @return 配置好的RedisCacheManager实例。
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        log.info("[RedisConfig] ==> 正在加载RedisCacheManager配置信息");
        // 1. 配置键和值的序列化器
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置键的序列化器为StringRedisSerializer
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置值的序列化器为GenericJackson2JsonRedisSerializer
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(buildRedisValueSerializer()))
                // 禁用缓存null值，防止缓存穿透
                .disableCachingNullValues()
                // 设置默认缓存过期时间为1小时
                .entryTtl(Duration.ofHours(1));

//        // 2. (可选) 为特定的缓存空间配置不同的过期时间
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        cacheConfigurations.put("users", config.entryTtl(Duration.ofMinutes(30))); // users缓存空间30分钟过期
//        cacheConfigurations.put("products", config.entryTtl(Duration.ofDays(1)));   // products缓存空间1天过期
        log.info("[RedisConfig] <== RedisCacheManager配置信息加载完成");
        // 3. 构建并返回RedisCacheManager
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config) // 设置默认的缓存配置
//                .withInitialCacheConfigurations(cacheConfigurations) // 应用特定的缓存配置
                .build();
    }


    /**
     * 构建值的JSON序列化器。
     * 使用GenericJackson2JsonRedisSerializer，并配置ObjectMapper以包含类型信息。
     * 这样可以确保任何Java对象都能被正确地序列化和反序列化。
     *
     * @return GenericJackson2JsonRedisSerializer实例。
     */
    private GenericJackson2JsonRedisSerializer buildRedisValueSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 关键配置：指定序列化输入的类型，以便反序列化时可以恢复为原始类型。
        // `LaissezFaireSubTypeValidator.instance` 是一个安全的默认类型验证器。
        // `ObjectMapper.DefaultTyping.NON_FINAL` 会在JSON中包含非final类型的类型信息。
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        // 可选：配置Java 8日期时间类型的序列化，需要 jackson-datatype-jsr310 依赖
        objectMapper.findAndRegisterModules();

        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }
}
