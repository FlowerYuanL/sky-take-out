package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "套餐分页查询数据传输模型", description = "用于查询套餐列表的分页数据")
public class SetmealPageQueryDTO implements Serializable {

    @ApiModelProperty(value = "页码", example = "1")
    private int page;

    @ApiModelProperty(value = "每页记录数", example = "10")
    private int pageSize;

    @ApiModelProperty(value = "套餐名称", example = "双人套餐")
    private String name;

    //分类id
    @ApiModelProperty(value = "分类id", example = "1")
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    @ApiModelProperty(value = "状态 0表示禁用 1表示启用", example = "1")
    private Integer status;

}
