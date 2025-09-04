package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
