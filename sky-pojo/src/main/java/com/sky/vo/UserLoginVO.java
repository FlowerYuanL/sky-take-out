package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "用户登录时返回的数据模型")
public class UserLoginVO implements Serializable {

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "openid")
    private String openid;
    @ApiModelProperty(value = "token")
    private String token;

}
