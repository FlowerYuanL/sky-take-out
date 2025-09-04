package com.sky.mapper;

import com.sky.dto.DishPageQueryDTO;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishAndCategoryMapper {

    List<DishVO> getAll(DishPageQueryDTO dishPageQueryDTO);

}
