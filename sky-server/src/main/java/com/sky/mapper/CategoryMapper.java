package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 根据名称模糊查询
     * @param name
     * @return
     */
    List<Category> getAll(String name,Integer type);

    /**
     * 根据id动态更新
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * 动态插入数据
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    void save(Category category);

    /**
     * 根据id删除分类信息
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deteleById(long id);

    List<Category> getByType(Integer type);
}
