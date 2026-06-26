package com.xzcit.zhangmengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzcit.zhangmengling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

// 关键：继承 BaseMapper<实体类>
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}