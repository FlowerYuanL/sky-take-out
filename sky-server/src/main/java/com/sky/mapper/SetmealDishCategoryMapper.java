package com.sky.mapper;

import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishCategoryMapper {

    SetmealVO getBySetmealId(Long id);
}
