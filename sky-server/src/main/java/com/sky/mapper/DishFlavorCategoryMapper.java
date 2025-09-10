package com.sky.mapper;

import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorCategoryMapper {

    /**
     * 根据分类id查询菜品信息、风味信息以及分类信息
     * @param categoryId 分类id
     */
    List<DishVO> getByCategoryId(Integer categoryId);

    /**
     * 根据分类id查询正在出售的菜品信息、风味信息以及分类信息
     * @param categoryId
     * @return
     */
    List<DishVO> getOnSaleByCategoryId(Long categoryId);

    /**
     * 根据id获取菜品信息、风味信息以及分类信息
     * @param id 菜品id
     */
    DishVO getById(Long id);
}
