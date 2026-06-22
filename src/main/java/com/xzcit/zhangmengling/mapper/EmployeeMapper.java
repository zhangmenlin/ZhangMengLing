package com.xzcit.zhangmengling.mapper;

import com.xzcit.zhangmengling.entity.Employee;
import com.xzcit.zhangmengling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
}
