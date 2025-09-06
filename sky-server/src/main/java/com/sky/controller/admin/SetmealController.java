package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "管理员端套餐相关的接口")
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 套餐分页查询时传递的数据模型
     */
    @ApiOperation(value = "套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult<SetmealVO>> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult<SetmealVO> setmealList = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(setmealList);
    }

    /**
     * 新增套餐
     * @param setmealDTO 新增套餐时传递的数据模型
     */
    @ApiOperation(value = "新增套餐")
    @PostMapping
    public Result<Void> save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * 设置套餐起售状态
     * @param status 状态
     * @param id 套餐id
     */
    @ApiOperation(value = "设置套餐起售状态")
    @PostMapping("/status/{status}")
    public Result<Void> setStatus(@PathVariable Integer status,Long id){
        setmealService.setStatus(status,id);
        return Result.success();
    }
}
