package com.sky.service.impl;

import com.sky.annotation.LogAnnotation;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     */
    @LogAnnotation
    public List<Setmeal> getByCategoryId(Integer categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }
}
