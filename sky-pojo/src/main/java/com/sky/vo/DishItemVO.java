package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "套餐包含的菜品数据模型", description = "用于展示套餐详情中的具体菜品信息")
public class DishItemVO implements Serializable {

    @ApiModelProperty(value = "菜品名称")
    private String name;

    @ApiModelProperty(value = "份数")
    private Integer copies;

    @ApiModelProperty(value = "菜品图片")
    private String image;

    @ApiModelProperty(value = "菜品描述")
    private String description;
}
