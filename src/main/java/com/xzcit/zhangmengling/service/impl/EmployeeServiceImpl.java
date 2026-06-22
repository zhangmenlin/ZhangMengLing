package com.xzcit.zhangmengling.service.impl;

import com.xzcit.zhangmengling.entity.Employee;
import com.xzcit.zhangmengling.mapper.EmployeeMapper;
import com.xzcit.zhangmengling.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(Employee employee) {
        // 根据用户名查询
        Employee emp = employeeMapper.getByUsername(employee.getUsername());
        if (emp == null) {
            return null;
        }

        // 将前端传来的明文密码进行MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        // 比对加密后的密码
        if (!emp.getPassword().equals(md5Password)) {
            return null;
        }

        // 账号禁用判断
        if (emp.getStatus() == 0) {
            return null;
        }

        return emp;
    }
}
