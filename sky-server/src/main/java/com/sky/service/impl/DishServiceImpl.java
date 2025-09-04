package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishAndCategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private DishAndCategoryMapper dishAndCategoryMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Override
    @LogAnnotation(value = "新增菜品")
    @Transactional(rollbackFor = Exception.class)
    public void save(DishDTO dishDTO) {
        //新建Dish实例
        Dish dish = new Dish();
        //新建List<DishFlavor>实例
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        //利用BeanUtils工具将DishDTO实例转换为Dish实例和List<DishFlavor>实例
        BeanUtils.copyProperties(dishDTO,dish);
        //调用接口将dish实例存入数据库中
        dishMapper.save(dish);
        //将id回写到dish实例中
        Long id = dish.getId();
        //判断dish是否为空
        if(!dishFlavorList.isEmpty()){
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
    @Override
    @LogAnnotation(value = "菜品分类查询")
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> page = PageHelper.startPage(
                dishPageQueryDTO.getPage(),
                dishPageQueryDTO.getPageSize()
        ).doSelectPage(() -> dishAndCategoryMapper.getAll(dishPageQueryDTO));
        return new PageResult<>(page.getTotal(),page.getResult());
    }
}
