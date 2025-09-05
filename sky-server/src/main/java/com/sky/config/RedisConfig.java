package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        //初始化
        redisTemplate.afterPropertiesSet();
        log.info("[RedisConfig] <== RedisTemplate配置信息加载完成");
        return redisTemplate;
    }
}
