package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.entity.Orders;
import com.xzcit.zhangmengling.mapper.OrderMapper;
import com.xzcit.zhangmengling.service.OrderService;
import org.springframework.stereotype.Service;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders>
        implements OrderService {

}


