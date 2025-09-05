package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
@ApiModel(value = "用户登录时传递的数据模型")
public class UserLoginDTO implements Serializable {

    @ApiModelProperty(value = "通过wx.login获取的code")
    private String code;

}
