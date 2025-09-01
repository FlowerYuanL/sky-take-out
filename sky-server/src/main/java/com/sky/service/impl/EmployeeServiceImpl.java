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
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
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
    @LogAnnotation(value = "分页查询员工信息")
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

    /**
     * 设置员工状态（启用/禁用）
     * @param status
     * @param id
     */
    @LogAnnotation(value = "启用/禁用 员工账号")
    @Override
    public void setStatus(Integer status, Long id) {
        //将参数封装进实体类中
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();
        //调用Mapper接口更新Status
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @LogAnnotation(value = "根据id查询员工信息")
    @Override
    public Employee getById(Long id) {
        //调用接口查询员工信息，并返回查询到的结果
        Employee employee = employeeMapper.getById(id);
        //将密码去掉
        employee.setPassword(PasswordConstant.SECRET_PASSWORD);
        return employee;
    }

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    @LogAnnotation(value = "编辑员工信息")
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        //创建employee对象
        Employee employee = new Employee();
        //将结果封装进员工类中——借助BeanUtils
        BeanUtils.copyProperties(employeeDTO,employee);
        //从ThreadLocal中获取当前操作者的id
        Long currentId = BaseContext.getCurrentId();
        //设置修改人的id
        employee.setUpdateUser(currentId);
        //设置本次修改的时间
        employee.setUpdateTime(LocalDateTime.now());
        //调用接口更新员工信息
        employeeMapper.update(employee);
    }

    /**
     * 修改员工密码
     * @param passwordEditDTO
     */
    @LogAnnotation(value = "修改员工的密码")
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        //取出提交的旧密码和新密码
        String oldPassword = passwordEditDTO.getOldPassword();
        String newPassword = passwordEditDTO.getNewPassword();
        //通过ThreadLocal获取当前用户的id
        long id = BaseContext.getCurrentId();
        //查询当前员工信息
        Employee employee = employeeMapper.getById(id);
        //获取当前员工的密码信息
        String myPassword = employee.getPassword();
        //借助DigestUtils
        String md5OldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        //如果输入的旧密码不正确，抛出异常
        if (!md5OldPassword.equals(myPassword)) {
            throw new PasswordEditFailedException(MessageConstant.INCORRECT_CURRENT_PASSWORD);
        }
        //如果输入的新密码于旧密码一样，抛出异常
        if (newPassword.equals(oldPassword)) {
            throw new PasswordEditFailedException(MessageConstant.RECEIVED_SAME_PASSWORD);
        }
        //到这里说明验证通过，将新密码转化未md5格式存储到数据库中
        String md5NewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        //将新的密码封装到用户信息中
        employee.setPassword(md5NewPassword);
        //调用函数将数据存储到数据库中
        employeeMapper.update(employee);
    }

}
