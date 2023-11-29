package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.exceptions.StringAndNumValueTogetherException;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository repository;
    private final CategoryRepository categoryRepository;
    private final CharacterItemValidator characterItemValidator;

    public void validate(ItemRequest request) {
        ValidationErrorResponse response = new ValidationErrorResponse();
        Optional<Category> optional = categoryRepository.findById(request.getCategoryId());
        if (optional.isEmpty()) {
            response.getViolations()
                    .add(new Violation("CategoryId", "Категория с id "
                            + request.getCategoryId() + " not found"));
        }
        response.getViolations().addAll(characterItemValidator.isListCharacterItemValid(request.getCharacterItems()));
        if (response.getViolations().size() > 0) {
            throw new StringAndNumValueTogetherException(response);
        }
    }

    public Item validate(ItemRequest request, Integer id) {
        Item item = repository.findById(id).orElseThrow();
        validate(request);
        return item;
    }
}
