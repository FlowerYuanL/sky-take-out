package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "密码传递模型")
public class PasswordEditDTO implements Serializable {

    //员工id
    @ApiModelProperty(value = "员工id")
    private Long empId;

    //旧密码
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    //新密码
    @ApiModelProperty(value = "新密码")
    private String newPassword;

}
