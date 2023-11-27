package com.vvnuts.shop.utils;

import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderUtils {
    private final OrderRepository orderRepository;

    public void validateStatus(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow();
        if (!order.getStatus().equals(Status.WAITING)) {
            throw new RuntimeException("Заказ уже апрувнут или отклонен");
        }
    }
}
