package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.SetmealAndDishMapper;
import com.sky.mapper.SetmealDishAndDishMapper;
import com.sky.mapper.SetmealDishCategoryMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealAndDishMapper setmealAndDishMapper;

    @Autowired
    private SetmealDishCategoryMapper setmealDishCategoryMapper;

    @Autowired
    private SetmealDishAndDishMapper setmealDishAndDishMapper;

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
    @LogAnnotation(value = "保存套餐信息")
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
        //检查当前需要设置为何种状态
        //如果是将要设置为起售状态
        if (StatusConstant.ENABLE.equals(status)) {
            //需要检查当前套餐所包含的菜品的状态
            List<Dish> dishList = setmealDishAndDishMapper.getHaltSaleBySetmealId(id);
            //判断是否为空，如果非空
            if(!CollectionUtils.isEmpty(dishList)){
                //有菜品的状态处于未起售的状态
                StringBuilder sbDishNames = new StringBuilder("，套餐下有未起售的菜品：");
                dishList.forEach(dish -> {
                    sbDishNames.append(dish.getName() + ",");
                    });
                String errMsg = sbDishNames.substring(0, sbDishNames.length() - 1);
                //抛出异常
                throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED + errMsg);

            }
            //如果均处于起售的状态
        }
        //如果将要设置为未起售的状态
        //将状态和id封装进套餐的实体类中
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据套餐id查询套餐表、套餐——菜品表、分类表
     * @param id
     */
    @LogAnnotation(value = "根据套餐id查询套餐、关联的菜品以及所在的分类")
    public SetmealVO getById(Long id) {
        return setmealDishCategoryMapper.getBySetmealId(id);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    @LogAnnotation(value = "修改套餐信息")
    public void modify(SetmealDTO setmealDTO) {
        //新建套餐对象
        Setmeal setmeal = new Setmeal();
        //借助BeanUtils.copyProperties为套餐对象赋值
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //个人建议这里应该将状态设置为为起售的状态
        setmeal.setStatus(StatusConstant.DISABLE);
        //将套餐更新到数据库中
        setmealMapper.update(setmeal);
        //获取套餐的id
        Long id = setmeal.getId();
        //根据菜品-套餐表中的菜品id删除信息
        setmealAndDishMapper.deleteBySetmealId(id);
        //为套餐中的关联的菜品信息赋值
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        //判断List列表是否为空
        if(!CollectionUtils.isEmpty(setmealDishes)){
            //如果不为空
            setmealDishes.forEach(setmealDish -> {
                //批量为List列表中套餐id字段赋值
                setmealDish.setSetmealId(id);
            });
        }
        //批量保存进SetmealDish表中
        setmealAndDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 根据id批量删除
     * 正在售卖的套餐不能删除
     * @param ids 套餐id的列表集合
     */
    @LogAnnotation(value = "根据套餐id批量删除")
    public void deleteBatch(List<Long> ids) {
        //根据id查询，返回名称，让界面交互更加流畅
        List<Setmeal> setmealList = setmealMapper.getOnSaleById(ids);
        //判断查询结果是否为空
        if (!CollectionUtils.isEmpty(setmealList)) {
            //这里采用StringBuilder是线程安全的
            StringBuilder sbNames = new StringBuilder();
            sbNames.append("，以下套餐正在售卖中：");
            setmealList.forEach(setmeal -> {
                sbNames.append(setmeal.getName() + ",");
            });
            String errMsg = sbNames.substring(0, sbNames.length() - 1);
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE + errMsg);
        }
        //如果为空，批量删除套餐
        setmealMapper.deleteBatch(ids);
    }

    /**
     * 根据套餐id查询菜品信息
     * @param id
     * @return
     */
    @LogAnnotation(value = "根据套餐id查询菜品信息")
    public List<DishItemVO> getBySetmealId(Long id) {
        return setmealAndDishMapper.getBySetmealId(id);
    }
}
