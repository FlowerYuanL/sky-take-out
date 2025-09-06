package com.sky.vo;

import com.sky.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "套餐分页查询时传递的数据模型")
public class SetmealVO implements Serializable {

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

    //更新时间
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    //分类名称
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    //套餐和菜品的关联关系
    @ApiModelProperty(value = "套餐包含的菜品列表")
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
