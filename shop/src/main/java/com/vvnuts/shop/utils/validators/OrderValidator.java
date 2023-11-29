package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.exceptions.OrderItemValidException;
import com.vvnuts.shop.exceptions.OrderStatusException;
import com.vvnuts.shop.repositories.OrderRepository;
import com.vvnuts.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderValidator {
    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final OrderItemValidator orderItemValidator;

    public void validate(OrderRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();

        Optional<User> optional = userRepository.findById(request.getUser());
        if (optional.isEmpty()) {
            response.getViolations()
                    .add(new Violation("User", "Пользователь с id "
                            + request.getUser() + " not found"));
        }

        response.getViolations().addAll(orderItemValidator.isListOrderItemValid(request.getOrderItemRequests()));
        if (response.getViolations().size() > 0) {
            throw new OrderItemValidException(response);
        }
    }

    public Order validate(UUID id) {
        Order order = repository.findById(id).orElseThrow();

        if (order.getStatus().equals(Status.WAITING)) {
            throw new OrderStatusException("Вы не можете вносить  данный заказ изменения!");
        }

        return order;
    }
}
