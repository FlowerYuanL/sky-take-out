package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 根据分类id查询套餐
     * @param categoryId
     */
    List<Setmeal> getByCategoryId(Integer categoryId);

    /**
     * 套餐分页查询，支持分类id，套餐起售状态，以及套餐名称的模糊查询，连接分类表
     * @param setmealPageQueryDTO
     */
    List<SetmealVO> getForPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 保存套餐信息到数据库
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 编辑套餐信息
     * @param setmeal
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
