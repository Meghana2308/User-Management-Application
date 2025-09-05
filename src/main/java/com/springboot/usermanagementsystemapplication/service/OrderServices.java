package com.springboot.usermanagementsystemapplication.service;

import com.springboot.usermanagementsystemapplication.dto.OrderDTO;

import java.util.List;

public interface OrderServices {

    OrderDTO createOrder( Long userId, OrderDTO orderDTO) ;
    List<OrderDTO> getOrdersByUser(Long userId) ;
}
