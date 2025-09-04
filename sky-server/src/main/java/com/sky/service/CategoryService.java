package com.sky.service;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void setStatus(Integer status, long id);

    void save(CategoryDTO categoryDTO);

    void deleteCategory(long id);

    List<Category> getByType(Integer type);

    void modifyCategory(CategoryDTO categoryDTO);
}
