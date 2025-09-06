package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    void setStatus(Integer status, Long id);

    List<Dish> getDishByList(Long categoryId);

    void modify(DishDTO dishDTO);

    DishVO getById(Long id);

    List<DishVO> getByCategoryId(Integer categoryId);
}
