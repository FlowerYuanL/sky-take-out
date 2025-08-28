package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.LogAnnotation;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @LogAnnotation(value = "登录功能")
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        //需要进行md5加密，然后再进行比对
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 保存员工信息
     * @param employeeDTO
     */
    @LogAnnotation(value = "新增用户信息")
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //new一个Employee对象
        Employee employee = new Employee();
        //将数据传输对象封装到实体对象中——借助BeanUtils
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置员工状态信息
        employee.setStatus(StatusConstant.ENABLE);
        //设置信息创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置员工密码信息——借助DegesUtils将默认密码转换为md5加密过的密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //设置创建信息的员工id
        //获取当前操作者的id
        Long createUserId = BaseContext.getCurrentId();
        employee.setCreateUser(createUserId);
        //设置更新信息的员工id
        employee.setUpdateUser(createUserId);

        //调用mapper将信息保存到数据库
        employeeMapper.save(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    @LogAnnotation
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
//        //配置PageHelper的参数（页码，记录数）
//        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
//        //紧跟PageHelper配置，查询全体员工信息
//        List<Employee> employeeList = employeeMapper.getAll(employeePageQueryDTO.getName());
//        //将结果强转为Page类型
//        Page<Employee> page = (Page<Employee>) employeeList;
//        //获取记录信息
//        List<Employee> records = page.getResult();
//        //获取查询总数
//        long total = page.getTotal();
//        //返回结果
//        return new PageResult<Employee>(total,records);
        //配置PageHelper的参数（页码，记录数）
        Page<Employee> page = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())
                .doSelectPage(() -> employeeMapper.getAll(employeePageQueryDTO.getName()));
        return new PageResult<>(page.getTotal(), page.getResult());
    }

}
