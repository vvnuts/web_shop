package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer> {
    Optional<Order> findById(UUID id);
}
