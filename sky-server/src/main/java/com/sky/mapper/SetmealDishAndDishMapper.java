package com.sky.mapper;

import com.sky.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishAndDishMapper {


    @Select("select d.* from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id} and d.status = 0")
    List<Dish> getHaltSaleBySetmealId(Long id);
}
