package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "菜品相关的接口")
@RestController("adminDishController")
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO 新增菜品时传递的数据模型
     */
    @PostMapping
    @ApiOperation(value = "新增菜品")
    @CacheEvict(cacheNames = "dishCache",key = "#dishDTO.categoryId")
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        dishService.save(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询传递的数据模型
     * @return 菜品表+分类表
     */
    @ApiOperation(value = "菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult<DishVO>> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        PageResult<DishVO> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids String类型的数据，通过@RequestParam注解可以自动封装
     */
    @ApiOperation(value = "批量删除菜品")
    @DeleteMapping
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    public Result<Void> deleteBatch(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 设置菜品起售状态
     * @param status 菜品起售状态
     * @param id 菜品id
     */
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @PostMapping("/status/{status}")
    @ApiOperation(value = "设置菜品起售状态")
    public Result<Void> setStatus(@PathVariable Integer status ,Long id){
        dishService.setStatus(status,id);
        return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类的id
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> getDishByList(Long categoryId){
        List<Dish> dishList = dishService.getDishByList(categoryId);
        return Result.success(dishList);
    }

    /**
     * 修改菜品信息
     * @param dishDTO 修改菜品时传递的数据模型
     */
    @CacheEvict(cacheNames = "dishCache",allEntries = true)
    @PutMapping
    @ApiOperation(value = "修改菜品信息")
    public Result<Void> modify(@RequestBody DishDTO dishDTO){
        dishService.modify(dishDTO);
        return Result.success();
    }

    /**
     * 根据id查询菜品信息、风味信息以及分类信息
     * @param id 菜品id
     */
    @ApiOperation(value = "根据id查询菜品信息")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        return Result.success(dishService.getById(id));
    }

}
