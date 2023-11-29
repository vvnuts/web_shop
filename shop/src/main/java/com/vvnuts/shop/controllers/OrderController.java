package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.exceptions.OrderStatusException;
import com.vvnuts.shop.services.OrderService;
import com.vvnuts.shop.utils.validators.OrderValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final OrderValidator validator;

    @GetMapping()
    public ResponseEntity<Order> findOne (@RequestParam("id") UUID orderId){
        Order entity = orderService.findById(orderId);
        return ResponseEntity.ok(entity);
    }

    @PostMapping()
    public ResponseEntity<Order> create(@RequestBody @Valid OrderRequest request){
        validator.validate(request);
        Order order = orderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(order);
    }

    @PatchMapping("/approve")
    public ResponseEntity<HttpStatus> approveOrder(@RequestParam("id") UUID orderId) {
        validator.validate(orderId);
        orderService.approveOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/canceling")
    public ResponseEntity<HttpStatus> cancelingOrder(@RequestParam("id") UUID orderId) {
        validator.validate(orderId);
        orderService.cancelingOrder(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<HttpStatus> delete(@RequestParam("id") UUID orderId) {
        orderService.delete(orderId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(OrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onOrderStatusException(RuntimeException e) {
        return e.getMessage();
    }
}
