package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO 员工登录传递的数据模型
     */
    @ApiOperation(value = "员工登录接口")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出登录
     */
    @ApiOperation(value = "员工退出接口")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工信息
     * @param employeeDTO 新增员工信息传递的数据模型
     */
    @ApiOperation(value = "保存员工信息接口")
    @PostMapping
    public Result<Void> save(@RequestBody EmployeeDTO employeeDTO) {
        //调用方法
        employeeService.save(employeeDTO);
        //返回结果
        return Result.success();
    }

    /**
     * 员工信息分页查询
     * @param employeePageQueryDTO 员工分页查询传递的数据模型
     */
    @ApiOperation(value = "员工信息分页查询接口")
    @GetMapping("/page")
    public Result<PageResult<Employee>> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //调用方法
        PageResult<Employee> pageResults = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResults);
    }

    /**
     * 设置员工状态信息
     * @param status 员工状态
     * @param id    员工id
     */
    @ApiOperation(value = "启动、禁用员工账号")
    @PostMapping("status/{status}")
    public Result<Void> setStatus(@PathVariable Integer status,Long id){
        employeeService.setStatus(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id 员工id
     */
    @ApiOperation(value = "根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        //调用接口查询员工信息
        Employee employee = employeeService.getById(id);
        //将结果封装进Result中去
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO 编辑员工信息传递的数据模型
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新员工信息")
    public Result<Void> updateEmployee(@RequestBody EmployeeDTO employeeDTO){
        //调用接口更新员工信息
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 修改密码
     * @param passwordEditDTO 修改密码传递的数据模型
     * @return
     */
    @ApiOperation(value = "编辑密码")
    @PutMapping("/editPassword")
    public Result<Void> editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        //调用接口编辑密码
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
