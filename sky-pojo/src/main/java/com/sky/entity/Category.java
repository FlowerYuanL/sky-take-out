package com.sky.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "菜品分类实体")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "类型")
    //类型: 1菜品分类 2套餐分类
    private Integer type;

    @ApiModelProperty(value = "分类名称")
    //分类名称
    private String name;

    @ApiModelProperty(value = "顺序")
    //顺序
    private Integer sort;

    @ApiModelProperty(value = "分类状态")
    //分类状态 0标识禁用 1表示启用
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    //创建时间
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    //更新时间
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    //创建人
    private Long createUser;

    @ApiModelProperty(value = "修改人")
    //修改人
    private Long updateUser;
}
