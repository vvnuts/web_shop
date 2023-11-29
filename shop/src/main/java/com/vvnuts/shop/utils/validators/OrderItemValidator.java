package com.vvnuts.shop.utils.validators;

import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderItemValidator {
    private final ItemRepository itemRepository;

    private Violation validate(OrderItemRequest request) {
        Optional<Item> optional = itemRepository.findById(request.getItem());
        if (optional.isEmpty()) {
            return new Violation("Item", "Продукт с id " + request.getItem() + " not found");
        } else {
            return isQuantityEnough(optional.get(), request.getQuantity());
        }
    }

    private Violation isQuantityEnough(Item item, Integer quantity) {
        if (item.getQuantity() < quantity) {
            return new Violation("Quantity", "Кол-ва продукции с id " + item.getItemId() +
                    "на складе недостаточно");
        }
        return null;
    }

    public List<Violation> isListOrderItemValid(List<OrderItemRequest> requests) {
        List<Violation> violations = new ArrayList<>();
        for (OrderItemRequest request: requests) {
            violations.add(validate(request));
        }
        return violations;
    }

}
