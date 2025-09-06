package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealAndDishMapper {

    /**
     * 根据菜品id查询套餐的数量
     * @param id
     */
    @Select("SELECT COUNT(*) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long dishId);

    /**
     * 批量插入信息到数据库
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    @Delete("delete from setmeal_dish where setmeal_id = #{SetmealId}")
    void deleteBySetmealId(Long SetmealId);
}
