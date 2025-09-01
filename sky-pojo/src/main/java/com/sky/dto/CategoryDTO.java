package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Data
@ApiModel(value = "新增菜品分类传递的数据模型")
public class CategoryDTO implements Serializable {

    @ApiModelProperty(value = "主键id")
    //主键
    private Long id;

    @ApiModelProperty(value = "类型")
    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    @ApiModelProperty(value = "分类名称")
    //分类名称
    private String name;

    @ApiModelProperty(value = "排序")
    //排序
    private Integer sort;

}
