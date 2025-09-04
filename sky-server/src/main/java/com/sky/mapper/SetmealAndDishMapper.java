package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealAndDishMapper {

    /**
     * 根据菜品id查询套餐的数量
     * @param id
     */
    @Select("SELECT COUNT(*) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long dishId);
}
