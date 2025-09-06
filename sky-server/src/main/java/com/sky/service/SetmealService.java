package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    List<Setmeal> getByCategoryId(Integer categoryId);

    PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void save(SetmealDTO setmealDTO);

    void setStatus(Integer status, Long id);
}
