package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.entity.Employee;
import com.xzcit.zhangmengling.mapper.EmployeeMapper;
import com.xzcit.zhangmengling.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
