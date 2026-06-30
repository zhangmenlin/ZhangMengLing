package com.xzcit.zhangmengling.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xzcit.zhangmengling.entity.OrderDetail;
import com.xzcit.zhangmengling.mapper.OrderDetailMapper;
import com.xzcit.zhangmengling.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {

}