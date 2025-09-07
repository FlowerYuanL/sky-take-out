package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    List<ShoppingCart> checkSale(ShoppingCart shoppingCart);

    void update(ShoppingCart cart);

    void insert(ShoppingCart cart);

    @Select("select * from shopping_cart where user_id = #{id}")
    List<ShoppingCart> getByUserId(Long id);

    @Delete("delete from shopping_cart where user_id = #{id}")
    void deleteByUserId(Long id);

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);
}
