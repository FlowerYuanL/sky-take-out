package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "购物车")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", example = "1001")
    private Long id;

    @ApiModelProperty(value = "名称", example = "红烧肉")
    private String name;

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "菜品ID", example = "10")
    private Long dishId;

    @ApiModelProperty(value = "套餐ID", example = "20")
    private Long setmealId;

    @ApiModelProperty(value = "口味", example = "微辣")
    private String dishFlavor;

    @ApiModelProperty(value = "数量", example = "2")
    private Integer number;

    @ApiModelProperty(value = "金额", example = "35.50")
    private BigDecimal amount;

    @ApiModelProperty(value = "图片", example = "http://example.com/image.jpg")
    private String image;

    @ApiModelProperty(value = "创建时间", example = "2023-10-27T10:00:00")
    private LocalDateTime createTime;
}
