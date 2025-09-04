package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
@ApiModel(value = "菜品分页查询传递的数据模型")
public class DishPageQueryDTO implements Serializable {

    @ApiModelProperty
    private int page;

    @ApiModelProperty
    private int pageSize;

    @ApiModelProperty
    private String name;

    //分类id
    @ApiModelProperty
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    @ApiModelProperty
    private Integer status;

}
