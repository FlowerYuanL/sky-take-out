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
 * 菜品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "菜品")
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "菜品的主键")
    private Long id;

    //菜品名称
    @ApiModelProperty(value = "菜品名称")
    private String name;

    //菜品分类id
    @ApiModelProperty(value = "菜品分类id")
    private Long categoryId;

    //菜品价格
    @ApiModelProperty(value = "菜品价格")
    private BigDecimal price;

    //图片
    @ApiModelProperty(value = "图片")
    private String image;

    //描述信息
    @ApiModelProperty(value = "描述信息")
    private String description;

    //售卖状态 0 停售 1 起售
    @ApiModelProperty(value = "描述信息")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建者")
    private Long createUser;

    @ApiModelProperty(value = "更新者")
    private Long updateUser;

}
