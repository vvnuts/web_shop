package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.exceptions.BucketValidException;
import com.vvnuts.shop.exceptions.OrderItemValidException;
import com.vvnuts.shop.exceptions.OrderValidException;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.repositories.UserRepository;
import com.vvnuts.shop.services.BucketService;
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
        }
        response.getViolations().addAll(orderItemValidator.isListOrderItemValid(request.getOrderItem()));
        if (response.getViolations().size() > 0) {
            throw new BucketValidException(response);
        }
    }

    public Bucket validate(BucketRequest request, Integer bucketId) {
        Bucket bucket = repository.findById(bucketId).orElseThrow();
        validate(request);
        return bucket;
    }
}
