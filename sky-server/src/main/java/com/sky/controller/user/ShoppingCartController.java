package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "用户端购物车相关接口")
@RestController("userShoppingCartController")
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO 添加购物车时传递的数据模型
     */
    @ApiOperation(value = "添加购物车")
    @PostMapping("/add")
    public Result<Void> addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }


    /**
     * 查询购物车
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询购物车")
    public Result<List<ShoppingCart>> getAll(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.getAll();
        return Result.success(shoppingCartList);
    }

    /*
    * 清空购物车
    * */
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车")
    public Result<Void> cleanCart(){
        shoppingCartService.cleanAll();
        return Result.success();
    }

    /**
     * 删除一个商品
     * @param shoppingCartDTO 删除单个商品时传递的数据模型
     */
    @ApiOperation(value = "删除一个商品")
    @PostMapping("/sub")
    public Result<Void> deleteOne(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.deleteOne(shoppingCartDTO);
        return Result.success();
    }
}
