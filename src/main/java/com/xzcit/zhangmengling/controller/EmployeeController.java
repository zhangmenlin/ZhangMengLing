package com.xzcit.zhangmengling.controller;

import com.xzcit.zhangmengling.entity.Employee;
import com.xzcit.zhangmengling.service.EmployeeService;
import com.xzcit.zhangmengling.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录接口 POST /employee/login
     * 接收前端JSON参数 {username:"",password:""}
     */
    @PostMapping("/login")
    public R<Employee> login(HttpSession session, @RequestBody Employee employee) {
        Employee loginUser = employeeService.login(employee);
        if (loginUser != null) {
            // 将员工id存入Session
            session.setAttribute("employee", loginUser.getId());
            // 登录成功 code=1
            return R.success(loginUser);
        }
        // 登录失败
        return R.error("账号或密码不正确");
    }
}
