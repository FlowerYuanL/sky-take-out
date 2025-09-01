package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 分类分页查询的接口
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    @LogAnnotation(value = "分类分页查询的接口")
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> categories = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())
                .doSelectPage(() -> categoryMapper.getAll(categoryPageQueryDTO.getName(),categoryPageQueryDTO.getType()));
        return new PageResult<>(categories.getTotal(),categories.getResult());
    }

    /**
     * 设置分类的状态
     * @param status
     * @param id
     */
    @Override
    @LogAnnotation(value = "设置分类的状态")
    public void setStatus(Integer status, long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 保存分类信息
     * @param categoryDTO
     */
    @Override
    @LogAnnotation(value = "保存分类信息")
    public void save(CategoryDTO categoryDTO) {
        //创建一个Category的实体类
        Category category = new Category();
        //借助BeanUtils将信息转存到Category的实体类中
        BeanUtils.copyProperties(categoryDTO,category);
        //设置分类的状态
        category.setStatus(StatusConstant.DISABLE);
//        //设置创建的时间信息
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        //从ThreadLocal中获取当前操作者的id
//        Long id = BaseContext.getCurrentId();
//        //设置人员信息
//        category.setCreateUser(id);
//        category.setUpdateUser(id);
        //调用接口将信息保存到数据库
        categoryMapper.save(category);
    }

    /**
     * 根据id删除分类信息
     * @param id
     */
    @Override
    @LogAnnotation(value = "根据id删除分类信息")
    public void deleteCategory(long id) {
        //删除前需要判断是否还有菜品表和套餐表中是否再对应的分类中
        //菜品
        if (dishMapper.countByCategoryId(id) > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        //套餐
        if (setmealMapper.countByCategoryId(id) > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deteleById(id);
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Override
    @LogAnnotation(value = "根据类型查询分类")
    public List<Category> getBytype(Integer type) {
        return categoryMapper.getByType(type);
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @Override
    @LogAnnotation(value = "修改分类")
    public void modifyCategory(CategoryDTO categoryDTO) {
        //新建Category实例
        Category category = new Category();
        //借助BeanUtils将CategoryDTO封装进category中
        BeanUtils.copyProperties(categoryDTO,category);
//        //设置当前的时间
//        category.setUpdateTime(LocalDateTime.now());
//        //获取当前的操作人
//        Long id = BaseContext.getCurrentId();
//        //设置当前的操作人
//        category.setUpdateUser(id);
        //调用接口将信息更新到数据库
        categoryMapper.update(category);
    }
}
