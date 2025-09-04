package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 动态插入信息
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 查询当前菜品的起售状态
     * @param id
     * @return
     */
    @Select("select status from dish where id = #{id}")
    Integer getStatus(Long id);

    /**
     * 批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getDishByList(Long categoryId);

    /**
     * 根据id获取菜品信息和风味信息以及分类信息
     * @param id
     */
    DishVO getById(Long id);
}
