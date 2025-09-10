package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "订单详情实体类", description = "订单详情实体类")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "菜品或套餐名称", required = true)
    private String name;

    @ApiModelProperty(value = "所属订单ID", required = true)
    private Long orderId;

    @ApiModelProperty(value = "菜品ID (如果为菜品)")
    private Long dishId;

    @ApiModelProperty(value = "套餐ID (如果为套餐)")
    private Long setmealId;

    @ApiModelProperty(value = "口味描述")
    private String dishFlavor;

    @ApiModelProperty(value = "数量", required = true)
    private Integer number;

    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;

    @ApiModelProperty(value = "图片URL")
    private String image;
}
