package com.sky.controller.user;


import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sky.service.SetmealService;

import java.util.List;

@Slf4j
@Api(tags = "用户端套餐相关的接口")
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     * @return
     */
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId")
    @GetMapping("/list")
    @ApiOperation(value = "根据分类的id查询套餐")
    public Result<List<Setmeal>> getByCategoryId(Integer categoryId){
        List<Setmeal> setmealList = setmealService.getByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询菜品信息
     * @param id 套餐id
     */
    @GetMapping("/dish/{id}")
    @ApiOperation(value = "根据套餐id查询包含的菜品数据")
    public Result<List<DishItemVO>> getDishItemBySetmealId(@PathVariable Long id){
        List<DishItemVO> dishItemVOList = setmealService.getBySetmealId(id);
        return Result.success(dishItemVOList);
    }


}
