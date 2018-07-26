package com.chen.service.impl;

import com.chen.dto.OrderDTO;
import com.chen.enums.ResultEnum;
import com.chen.exception.SellException;
import com.chen.service.BuyerService;
import com.chen.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if (orderDTO == null){
            log.error("【取消订单】查不到该订单， orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXITS);
        }
        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openid, String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null){
            return null;
        }
        //判断是否是自己的订单
        if (!orderDTO.getOrderId().equals(openid)){
            log.error("【查询订单】订单的openid不一致， openid={}, orderDTO={}",openid,orderDTO);
            throw new SellException(ResultEnum.ORDER_OWWER_ERROR);
        }
        return orderDTO;
    }
}
