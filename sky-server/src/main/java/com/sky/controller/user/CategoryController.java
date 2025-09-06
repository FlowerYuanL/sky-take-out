package com.sky.controller.user;


import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "分类相关接口")
@RestController("userCategoryController")
@RequestMapping("/user/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据菜品类型的id查询菜品
     * @param type 菜品类型
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据菜品类型的id查询菜品")
    public Result<List<Category>> getByType(Integer type){
        List<Category> categoryList = categoryService.getByType(type);
        return Result.success(categoryList);
    }
}
