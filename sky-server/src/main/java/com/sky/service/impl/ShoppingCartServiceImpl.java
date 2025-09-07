package com.sky.service.impl;

import com.sky.annotation.LogAnnotation;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 添加到购物车
     * @param shoppingCartDTO 添加购物车时传递的数据模型
     */
    @LogAnnotation
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //创建一个ShoppingCart的实体类
        ShoppingCart shoppingCart = new ShoppingCart();
        //将shoppingCartDTO中的数据封装到shoppingCart中
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        //从ThreadLocal中获取用户id
        Long id = BaseContext.getCurrentId();
        //将id存储到shoppingCart中
        shoppingCart.setUserId(id);
        //首先查询数据库，需要添加的商品是否已经存在
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.checkSale(shoppingCart);
        //如果存在，则将购物车中的num+1更新到数据库
        if (!CollectionUtils.isEmpty(shoppingCarts)) {
            ShoppingCart cart = shoppingCarts.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.update(cart);
            return;
        }
        //如果不存在，则创建一个新得购物车数据存储到数据库
        if (shoppingCart.getSetmealId() != null) {
            Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        } else {
            Dish dish = dishMapper.getById(shoppingCart.getDishId());
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * 查询当前用户的购物车
     * @return
     */
    @LogAnnotation(value = "查询当前用户的购物车")
    public List<ShoppingCart> getAll() {
        Long id = BaseContext.getCurrentId();
        return shoppingCartMapper.getByUserId(id);
    }

    /**
     * 删除购物车全部商品
     */
    @LogAnnotation(value = "删除当前用户全部的购物信息")
    public void cleanAll() {
        //获取当前用户id
        Long id = BaseContext.getCurrentId();
        //调用接口删除当前用户的全部购物车
        shoppingCartMapper.deleteByUserId(id);
    }

    /**
     * 删除购物车中的一个商品
     * @param shoppingCartDTO
     */
    @LogAnnotation(value = "删除当前用户购物车中的一个商品")
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.checkSale(shoppingCart);
        ShoppingCart myCart = shoppingCartList.get(0);
        Integer number = myCart.getNumber();
        if (number == 1) {
            shoppingCartMapper.deleteById(myCart.getId());
            return;
        }
        myCart.setNumber(number - 1);
        shoppingCartMapper.update(myCart);
    }

}
