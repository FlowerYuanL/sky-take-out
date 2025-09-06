package com.sky.service;

import com.sky.entity.Setmeal;

import java.util.List;

public interface SetmealService {
    List<Setmeal> getByCategoryId(Integer categoryId);
}
