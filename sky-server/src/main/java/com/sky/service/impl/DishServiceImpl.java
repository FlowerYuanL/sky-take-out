package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private DishAndCategoryMapper dishAndCategoryMapper;

    @Autowired
    private SetmealAndDishMapper setmealAndDishMapper;

    @Autowired
    private DishFlavorCategoryMapper dishFlavorCategoryMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    
    @LogAnnotation(value = "新增菜品")
    @Transactional(rollbackFor = Exception.class)
    public void save(DishDTO dishDTO) {
        //新建Dish实例
        Dish dish = new Dish();
        //新建List<DishFlavor>实例
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        //利用BeanUtils工具将DishDTO实例转换为Dish实例和List<DishFlavor>实例
        BeanUtils.copyProperties(dishDTO,dish);
        //设置默认的起售状态
        dish.setStatus(StatusConstant.DISABLE);
        //调用接口将dish实例存入数据库中
        dishMapper.save(dish);
        //将id回写到dish实例中
        Long id = dish.getId();
        //判断dish是否为空
        if(CollectionUtils.isNotEmpty(dishFlavorList)){
            //遍历dishFlavor，为dishId赋值
            dishFlavorList.forEach(flavor -> {
                flavor.setDishId(id);
                dishFlavorMapper.save(flavor);
            });
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    
    @LogAnnotation(value = "菜品分页查询")
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = PageHelper.startPage(
                dishPageQueryDTO.getPage(),
                dishPageQueryDTO.getPageSize()
        ).doSelectPage(() -> dishAndCategoryMapper.getAll(dishPageQueryDTO));
        return new PageResult<>(page.getTotal(),page.getResult());
    }




    /*
     * 1.可以一次删除一个菜品，也可以批量删除
     * 2.起售中的菜品不能删除 -->dish.status
     * 3.被套餐关联的菜品不能删除 -->count(*) setmeal_dish.dish_id
     * 4.删除菜品后，关联的口味数据也需要删除掉
     * */

    /**
     * 批量删除菜品
     * @param ids
     */
    @LogAnnotation(value = "根据id批量删除菜品")
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        //判断菜品的起售状态
        for (Long id : ids) {
            //调用接口返回当前菜品的状态
            Integer status = dishMapper.getStatus(id);
            //调用接口返回菜品关联套餐的数量
            Integer nums = setmealAndDishMapper.countByDishId(id);
            if (status == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
            if (nums > 0) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }
        //根据id批量删除菜品中的数据
        dishMapper.deleteBatch(ids);
        //根据dish_id批量删除风味表中的数据
        dishFlavorMapper.deleteBatch(ids);
    }

    /**
     * 设置菜品起售状态
     * @param status
     * @param id
     */
    @LogAnnotation(value = "设置菜品起售状态")
    public void setStatus(Integer status, Long id) {
        //将属性封装进Dish中
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @LogAnnotation(value = "根据分类id查询菜品")
    public List<Dish> getDishByList(Long categoryId) {
        return dishMapper.getDishByList(categoryId);
    }

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    @LogAnnotation(value = "修改菜品信息")
    @Transactional(rollbackFor = Exception.class)
    public void modify(DishDTO dishDTO) {
        Dish dish = new Dish();
        //将DishDTO封装进Dish中
        BeanUtils.copyProperties(dishDTO,dish);
        //将风味信息封装进List<DishFlavor>
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        //将dish存入数据库中
        dishMapper.update(dish);
        //返回dish的id,并将其封装为List数组
        List<Long> id = Collections.singletonList(dish.getId());
        //首先删除有该id值的风味信息
        dishFlavorMapper.deleteBatch(id);
        //判断修改后的风味信息是否为空
        if (CollectionUtils.isNotEmpty(dishFlavorList)) {
            dishFlavorList.forEach(flavor -> {
                //若风味信息不为空，则为风味表中的菜品id赋值
                flavor.setDishId(id.get(0));
                //将风味信息保存到数据库中
                dishFlavorMapper.save(flavor);
            });
        }
    }

    /**
     * 根据菜品id获取菜品信息+风味信息+分类信息
     * @param id
     * @return
     */
    @LogAnnotation(value = "根据菜品id获取菜品、风味以及分类信息")
    public DishVO getById(Long id) {
        return dishFlavorCategoryMapper.getById(id);
    }

    /**
     * 根据菜品id获取菜品信息+风味信息+分类信息
     * @param categoryId
     * @return
     */
    @LogAnnotation(value = "根据分类id获取菜品、风味以及分类信息")
    public List<DishVO> getByCategoryId(Long categoryId) {
        return dishFlavorCategoryMapper.getOnSaleByCategoryId(categoryId);
    }

}
