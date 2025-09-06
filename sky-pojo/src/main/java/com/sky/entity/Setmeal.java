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
 * 套餐
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "套餐", description = "套餐信息")
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    //分类id
    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    //套餐名称
    @ApiModelProperty(value = "套餐名称")
    private String name;

    //套餐价格
    @ApiModelProperty(value = "套餐价格")
    private BigDecimal price;

    //状态 0:停用 1:启用
    @ApiModelProperty(value = "状态 0:停用 1:启用")
    private Integer status;

    //描述信息
    @ApiModelProperty(value = "描述信息")
    private String description;

    //图片
    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    private Long updateUser;
}