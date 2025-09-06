package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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
     * 根据id查询套餐、套餐——菜品、分类
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 套餐分页查询
     *
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
     *
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
     *
     * @param status 状态
     * @param id     套餐id
     */
    @ApiOperation(value = "设置套餐起售状态")
    @PostMapping("/status/{status}")
    public Result<Void> setStatus(@PathVariable Integer status, Long id) {
        setmealService.setStatus(status, id);
        return Result.success();
    }

    /**
     * 修改套餐
     * @param setmealDTO 修改套餐时传递的数据模型
     */
    @PutMapping
    @ApiOperation(value = "修改套餐")
    public Result<Void> modifySetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.modify(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @ApiOperation(value = "批量删除套餐")
    @DeleteMapping
    public Result<Void> deleteSetmeal(@RequestParam List<Long> ids){
        log.debug("ids:{}", ids.toString());
        setmealService.deleteBatch(ids);
        return Result.success();
    }
}
