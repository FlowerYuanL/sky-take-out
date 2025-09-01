package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "菜品风味")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜品风味主键")
    private Long id;

    //菜品id
    @ApiModelProperty(value = "菜品id")
    private Long dishId;

    //口味名称
    @ApiModelProperty(value = "口味名称")
    private String name;

    //口味数据 list
    @ApiModelProperty(value = "口味数据")
    private String value;

}
