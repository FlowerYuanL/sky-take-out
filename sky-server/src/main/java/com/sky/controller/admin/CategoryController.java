package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理相关接口
 */
@Api(tags = "分类管理相关接口")
@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询时传递的数据模型
     */
    @GetMapping("/page")
    @ApiOperation(value = "分类分页查询")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 设置分类状态信息
     * @param Status 分类状态
     * @param id 分类的id
     */
    @ApiOperation(value = "设置分类状态信息")
    @PostMapping("/status/{status}")
    public Result<Void> setStatus(@PathVariable Integer status,long id){
        categoryService.setStatus(status,id);
        return Result.success();
    }

    /**
     * 保存分类信息
     * @param categoryDTO 保存分类信息时传递的数据模型
     */
    @ApiOperation(value = "保存分类信息")
    @PostMapping
    public Result<Void> save(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 根据id删除分类
     * @param id 菜品分类的id
     */
    @ApiOperation(value = "根据id删除分类")
    @DeleteMapping
    public Result<Void> deleteCategory(long id){
        categoryService.deleteCategory(id);
        return Result.success();
    }

    /**
     * 根据分类类型查询分类
     * @param type 分类类型
     */
    @ApiOperation(value = "根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> getByType(Integer type){
        List<Category> categories = categoryService.getByType(type);
        return Result.success(categories);
    }

    /**
     * 修改分类信息
     * @param categoryDTO 修改分类时传递的数据模型
     */
    @ApiOperation(value = "修改分类信息")
    @PutMapping
    public Result<Void> modifyCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.modifyCategory(categoryDTO);
        return Result.success();
    }


}
