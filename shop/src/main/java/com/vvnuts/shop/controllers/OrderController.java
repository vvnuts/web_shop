package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Order> findOne (@PathVariable @Min(0) Integer orderId){
        Order entity = orderService.findById(orderId);
        return ResponseEntity.ok(entity);
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid OrderRequest dtoEntity){
        orderService.create(dtoEntity);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<HttpStatus> approveOrder(@PathVariable @Min(0) Integer orderId) {
        orderService.approveOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/canceling")
    public ResponseEntity<HttpStatus> cancelingOrder(@PathVariable @Min(0) Integer orderId) {
        orderService.cancelingOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable @Min(0) Integer orderId) {
        orderService.delete(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
