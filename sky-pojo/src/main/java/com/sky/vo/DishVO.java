package com.sky.vo;

import com.sky.entity.DishFlavor;
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
@ApiModel(value = "菜品分页查询展示的结果")
public class DishVO implements Serializable {
    @ApiModelProperty
    private Long id;
    //菜品名称
    @ApiModelProperty
    private String name;
    //菜品分类id
    @ApiModelProperty
    private Long categoryId;
    //菜品价格
    @ApiModelProperty
    private BigDecimal price;
    //图片
    @ApiModelProperty
    private String image;
    //描述信息
    @ApiModelProperty
    private String description;
    //0 停售 1 起售
    @ApiModelProperty
    private Integer status;
    //更新时间
    @ApiModelProperty
    private LocalDateTime updateTime;
    //分类名称
    @ApiModelProperty
    private String categoryName;
    //菜品关联的口味
    @ApiModelProperty
    private List<DishFlavor> flavors = new ArrayList<>();

    //private Integer copies;
}
