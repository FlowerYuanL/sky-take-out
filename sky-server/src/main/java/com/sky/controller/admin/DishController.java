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
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "菜品相关的接口")
@RestController
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
}
