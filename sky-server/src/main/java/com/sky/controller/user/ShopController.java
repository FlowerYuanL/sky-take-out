package com.sky.controller.user;


import com.sky.constant.StatusConstant;
import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户端口店铺相关接口")
@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
public class ShopController {



    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺的营业状态
     * @return
     */
    @ApiOperation(value = "获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer status = shopService.getStatus();
        return Result.success(status);
    }
}
