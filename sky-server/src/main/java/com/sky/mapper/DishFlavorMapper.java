package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 保存菜品风味信息
     * @param dishFlavor
     */
    void save(DishFlavor dishFlavor);

    /**
     * 根据dishId 批量删除菜品风味信息
     * @param ids 菜品id
     */
    void deleteBatch(List<Long> ids);
}
