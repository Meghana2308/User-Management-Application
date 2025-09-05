package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.dto.OrderDTO;
import com.springboot.usermanagementsystemapplication.exception.UserNotFoundException;
import com.springboot.usermanagementsystemapplication.model.Order;
import com.springboot.usermanagementsystemapplication.model.User;
import com.springboot.usermanagementsystemapplication.repository.OrderRepository;
import com.springboot.usermanagementsystemapplication.repository.UserRepository;
import com.springboot.usermanagementsystemapplication.service.OrderServices;
import com.springboot.usermanagementsystemapplication.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderServices {


    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public OrderDTO createOrder(Long userId, OrderDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Order order = modelMapper.map(dto, Order.class);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        Order saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        log.debug("Fetching orders for user {}", userId);
        return orderRepository.findByUserId(userId).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }
}
