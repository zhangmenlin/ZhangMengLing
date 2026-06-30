package com.xzcit.zhangmengling.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xzcit.zhangmengling.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
