package com.sky.dto;

import com.sky.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "操做套餐时传递的数据模型", description = "用于新增或修改套餐信息的数据传输对象")
public class SetmealDTO implements Serializable {

    @ApiModelProperty(value = "主键ID", notes = "修改时必传，新增时不需要传")
    private Long id;

    @ApiModelProperty(value = "分类ID")
    private Long categoryId;

    @ApiModelProperty(value = "套餐名称",  example = "双人套餐")
    private String name;

    @ApiModelProperty(value = "套餐价格",  example = "58.8")
    private BigDecimal price;

    @ApiModelProperty(value = "套餐状态", notes = "0:停用, 1:启用")
    private Integer status;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "图片URL")
    private String image;

    @ApiModelProperty(value = "套餐包含的菜品列表", required = false)
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
