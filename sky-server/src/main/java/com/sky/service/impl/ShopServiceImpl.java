package com.sky.service.impl;

import com.sky.annotation.LogAnnotation;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ShopServiceImpl implements ShopService {

    public static final String KEY = "SHOP_STATUS";


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 获取店铺营业状态
     * @return
     */
    @Override
    @LogAnnotation(value = "获取店铺营业状态")
    public Integer getStatus() {
        ValueOperations<String, Object> valueOP = redisTemplate.opsForValue();
        return (Integer) valueOP.get(KEY);
    }

    /**
     * 设置店铺营业状态
     * @param status
     */
    @Override
    @LogAnnotation(value = "设置店铺的营业状态")
    public void setStatus(Integer status) {
        log.debug("设置店铺营业状态为:{}",status==1?"营业中":"打烊中");
        ValueOperations<String, Object> valueOP = redisTemplate.opsForValue();
        valueOP.set(KEY,status);
    }
}
