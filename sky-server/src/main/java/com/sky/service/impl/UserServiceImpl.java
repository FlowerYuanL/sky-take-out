package com.sky.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.UserNotLoginException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    private String getOpenId(String code) throws JsonProcessingException {
        //将必要的信息封装进Map集合中
        Map<String,String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type", "authorization_code");
        //调用微信接口服务，获取当前微信用户的openId
        String jsonStr = HttpClientUtil.doGet(WX_URL, map);
        if(!StringUtils.hasText(jsonStr)){
            //如果为空，则抛出异常
            throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
        }
        JsonNode node = mapper.readTree(jsonStr);
        return node.get("openid").asText();
    }

    /**
     * 用户微信登录
     * @param userLoginDTO
     * @return
     */
    @LogAnnotation(value = "用户微信登录")
    public User wxLogin(UserLoginDTO userLoginDTO) throws JsonProcessingException {
        String openid = getOpenId(userLoginDTO.getCode());
        //判断openid是否为空
        if (!StringUtils.hasText(openid)){
            //如果为空，则抛出异常
            throw new UserNotLoginException(MessageConstant.LOGIN_FAILED);
        }
        //判断用户是否为新用户
        User user = userMapper.getByOpenId(openid);
        //如果为新用户
        if(user == null){
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回这个用户对象
        return user;
    }

}
