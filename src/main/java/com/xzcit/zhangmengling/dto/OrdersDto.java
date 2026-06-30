package com.xzcit.zhangmengling.dto;

import com.xzcit.zhangmengling.entity.OrderDetail;
import com.xzcit.zhangmengling.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {
    private String userName;
    private String phone;
    private String address;
    private String consignee;
    //订单详情集合
    private List<OrderDetail> orderDetails;
}