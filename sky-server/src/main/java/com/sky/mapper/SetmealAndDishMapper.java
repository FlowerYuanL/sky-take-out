package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
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

    /**
     * 根据套餐id删除套餐菜品表中的数据
     * @param SetmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{SetmealId}")
    void deleteBySetmealId(Long SetmealId);

    /**
     * 根据套餐id查询菜品信息
     * @param setmealId
     * @return
     */
    @Select("select sd.name,sd.copies,d.description,d.image from setmeal_dish sd left join dish d on sd.dish_id = d.id where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getBySetmealId(Long setmealId);
}
