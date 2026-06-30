package com.xzcit.zhangmengling.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzcit.zhangmengling.common.R;
import com.xzcit.zhangmengling.dto.OrdersDto;
import com.xzcit.zhangmengling.entity.OrderDetail;
import com.xzcit.zhangmengling.entity.Orders;
import com.xzcit.zhangmengling.service.OrderDetailService;
import com.xzcit.zhangmengling.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 订单信息分页查询
     */
    @GetMapping("/page")
    public R<Page<OrdersDto>> page(int page, int pageSize, String number,
                                   String beginTime, String endTime) {
        // 构造分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件：订单号模糊查询
        queryWrapper.like(number != null, Orders::getNumber, number);
        // 时间区间查询
        queryWrapper.between(beginTime != null && endTime != null,
                Orders::getOrderTime, beginTime, endTime);
        // 添加排序条件：按下单时间倒序
        queryWrapper.orderByDesc(Orders::getOrderTime);

        // 执行分页查询
        orderService.page(pageInfo, queryWrapper);

        // 新建DTO分页对象
        Page<OrdersDto> ordersDtoPage = new Page<OrdersDto>();
        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");

        // 封装每一条订单+对应订单详情
        List<OrdersDto> ordersDtos = pageInfo.getRecords().stream().map(orders -> {
            OrdersDto ordersDto = new OrdersDto();
            // 复制订单基础信息
            BeanUtils.copyProperties(orders, ordersDto);

            // 根据订单id查询对应订单明细
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper
                    = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
            List<OrderDetail> ordersDetailList = orderDetailService.list(orderDetailLambdaQueryWrapper);

            // 把明细集合存入DTO
            ordersDto.setOrderDetails(ordersDetailList);
            return ordersDto;
        }).collect(Collectors.toList());

        // 将封装好的DTO列表放入分页对象
        ordersDtoPage.setRecords(ordersDtos);
        return R.success(ordersDtoPage);
    }

    @PutMapping
    public R<String> update(@RequestBody Orders orders) {
        orderService.updateById(orders);
        return R.success("修改成功");
    }
}
