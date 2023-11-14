package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.OrderDTO;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOne (@PathVariable Integer orderId){
        Order entity = orderService.findById(orderId);
        if(entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody OrderDTO dtoEntity){
        orderService.create(dtoEntity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<HttpStatus> approveOrder(@PathVariable Integer orderId) {
        orderService.approveOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/canceling")
    public ResponseEntity<HttpStatus> cancelingOrder(@PathVariable Integer orderId) {
        orderService.cancelingOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
