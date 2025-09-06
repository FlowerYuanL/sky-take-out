package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealAndDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealAndDishMapper setmealAndDishMapper;

    /**
     * 根据分类id查询套餐
     * @param categoryId
     */
    @LogAnnotation(value = "根据分类id获取套餐信息")
    public List<Setmeal> getByCategoryId(Integer categoryId) {
        return setmealMapper.getByCategoryId(categoryId);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     */
    @LogAnnotation(value = "套餐分页查询")
    public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<SetmealVO> setmealVOPage = PageHelper.startPage(
                setmealPageQueryDTO.getPage(),
                setmealPageQueryDTO.getPageSize()
        ).doSelectPage(() -> setmealMapper.getForPage(setmealPageQueryDTO));
        return new PageResult<>(setmealVOPage.getTotal(), setmealVOPage.getResult());
    }

    /**
     * 保存套餐信息
     * @param setmealDTO
     */
    @LogAnnotation
    @Transactional(rollbackFor = Exception.class)
    public void save(SetmealDTO setmealDTO) {
        //新建setmeal实例
        Setmeal setmeal = new Setmeal();
        //借助BeanUtils.copyProperties组件快捷复制
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //初始化起售状态为禁售
        setmeal.setStatus(StatusConstant.DISABLE);
        //将值赋给List<SetmealDish>实例
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //将setmeal中的数据保存到数据库中
        setmealMapper.insert(setmeal);
        //返回setmeal中的主键id
        Long setmealId = setmeal.getId();
        //如果List<SetmealDish>中的数据不为空
        if(!CollectionUtils.isEmpty(setmealDishes)){
            //批量为List列表中的setmealId赋值
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
        }
        //将List列表中的数据保存到数据库中
        setmealAndDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 设置套餐的起售状态
     * @param status
     * @param id
     */
    @LogAnnotation(value = "设置套餐的起售状态")
    public void setStatus(Integer status, Long id) {
        //将状态和id封装进套餐的实体类中
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }
}
