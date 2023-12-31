package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.exceptions.ReviewValidException;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewValidator {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void validate(ReviewRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        Optional<Item> item = itemRepository.findById(request.getItem());
        if (item.isEmpty()) {
            response.getViolations()
                    .add(new Violation("Item", "Продукт с id "
                            + request.getItem() + " not found"));
        }
        Optional<User> user = userRepository.findById(request.getUser());
        if (user.isEmpty()) {
            response.getViolations()
                    .add(new Violation("User", "Пользователь с id "
                            + request.getUser() + " not found"));
        }
        if (!response.getViolations().isEmpty()) {
            throw new ReviewValidException(response);
        }
    }
}
