package com.springboot.usermanagementsystemapplication.controller;

import com.springboot.usermanagementsystemapplication.dto.OrderDTO;
import com.springboot.usermanagementsystemapplication.service.OrderServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServices orderServices;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@PathVariable Long userId, @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderServices.createOrder(userId, orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderServices.getOrdersByUser(userId));
    }
}
