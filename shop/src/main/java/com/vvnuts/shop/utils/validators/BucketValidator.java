package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.exceptions.BucketValidException;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketValidator {
    private final BucketRepository repository;
    private final UserRepository userRepository;
    private final OrderItemValidator orderItemValidator;

    public void validate(BucketRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        Optional<User> optional = userRepository.findById(request.getUser());
        if (optional.isEmpty()) {
            response.getViolations()
                    .add(new Violation("User", "Пользователь с id "
                            + request.getUser() + " not found"));
        } else if (optional.get().getBucket() != null) {
            response.getViolations()
                    .add(new Violation("User", "Пользователь с id "
                            + request.getUser() + " уже имеет корзину"));
        }
        response.getViolations().addAll(orderItemValidator.isListOrderItemValid(request.getOrderItem()));
        if (!response.getViolations().isEmpty()) {
            throw new BucketValidException(response);
        }
    }

    public Bucket validate(BucketRequest request, Integer bucketId) {
        Bucket bucket = repository.findById(bucketId).orElseThrow();

        ValidationErrorResponse response = new ValidationErrorResponse();
        response.getViolations().addAll(orderItemValidator.isListOrderItemValid(request.getOrderItem()));
        if (!response.getViolations().isEmpty()) {
            throw new BucketValidException(response);
        }

        return bucket;
    }

    public Bucket validate(OrderItemRequest request, Integer bucketId) {
        Bucket bucket = repository.findById(bucketId).orElseThrow();

        ValidationErrorResponse response = new ValidationErrorResponse();
        Violation violation = orderItemValidator.validate(request);
        if (violation != null) {
            response.getViolations().add(violation);
        }
        if (!response.getViolations().isEmpty()) {
            throw new BucketValidException(response);
        }

        return bucket;
    }
}
