package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工信息
     * @param employee
     */
    void save(Employee employee);

    /**
     * 查询全体员工，支持根据员工姓名模糊查询
     * @param name
     * @return
     */
    List<Employee> getAll(String name);

    /**
     * 动态更新SQL语句
     * @param employee
     */
    void update(Employee employee);

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM employee where id = #{id}")
    Employee getById(Long id);
}
