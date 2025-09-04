package com.sky.controller.admin;


import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class ShopController {

    //TODO 待完善
    @ApiOperation(value = "获取商店营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        return Result.success(StatusConstant.ENABLE);
    }

    //TODO 待完善
    @ApiOperation(value = "设置商店营业状态")
    @PutMapping("/{status}")
    public Result<Void> setStatus(@PathVariable Integer status){
        return Result.success();
    }
}
