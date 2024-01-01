package com.vvnuts.shop.services.specifications;


import com.vvnuts.shop.dtos.requests.SpecificationItemRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.enums.Type;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemSpecification {
    private final CharacteristicRepository characteristicRepository;

    public Specification<Item> createSpecification(SpecificationItemRequest request) {
        Specification<Item> itemSpecification = getItemInStock().and(getItemFromCategory(request.getCategory()));

        if (request.getRangeOfPrice() != null) {
            itemSpecification = itemSpecification.and(getItemWithRangeOfPrice(request.getRangeOfPrice()));
        }
        if (request.getRangeOfMark() != null) {
            itemSpecification = itemSpecification.and(getItemWithRangeOfMark(request.getRangeOfMark()));
        }
        if (request.getCharacteristicValue() != null) {
            for (Map.Entry<Integer, String> pair : request.getCharacteristicValue().entrySet()) {
                Characteristic characteristic = characteristicRepository.findById(pair.getKey()).orElseThrow();
                itemSpecification = itemSpecification.and(getItemWithCharacteristic(characteristic, pair.getValue()));
            }
        }

        return itemSpecification;
    }

    private Specification<Item> getItemInStock() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isNotNull(root.get("quantity"));
    }

    private Specification<Item> getItemFromCategory(Category id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), id);
    }

    private Specification<Item> getItemWithRangeOfPrice(String range) {
        String[] stringLimits = range.split("-");
        List<Integer> limits = Arrays.stream(stringLimits).map(Integer::parseInt).toList();
        if (limits.get(0) > limits.get(1)) {
            return null; //TODO exception
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), limits.get(0), limits.get(1));
    }

    private Specification<Item> getItemWithRangeOfMark(String range) {
        List<Double> limits = prepareStringLimits(range);
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("mark"), limits.get(0), limits.get(1));
    }

    private Specification<Item> getItemWithCharacteristic(Characteristic characteristic, String value) {
        return (root, query, criteriaBuilder) -> {
            Join<Item, CharacterItem> itemWithCharacterItem = root.join("characterItems");
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(itemWithCharacterItem.get("characteristic"), characteristic));
            Join<CharacterItem, Characteristic> allInfo = itemWithCharacterItem.join("characteristic");

            if ((value.matches("\\d+\\.\\d+-\\d+\\.\\d+") || value.matches("\\d+-\\d+")) && value.split("-").length == 2) {
                List<Double> limits = prepareStringLimits(value);
                predicates.add(criteriaBuilder.equal(allInfo.get("type"), Type.INTEGER));
                predicates.add(criteriaBuilder.between(itemWithCharacterItem.get("numValue"), limits.get(0), limits.get(1)));
            } else {
                predicates.add(criteriaBuilder.equal(allInfo.get("type"), Type.STRING));
                predicates.add(criteriaBuilder.equal(itemWithCharacterItem.get("value"), value));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private List<Double> prepareStringLimits(String value) {
        String[] stringLimits = value.split("-");
        List<Double> limits = Arrays.stream(stringLimits).map(Double::parseDouble).toList();
        if (limits.get(0) > limits.get(1) && limits.get(0) >= 0 && limits.get(1) <= 5) {
            throw new RuntimeException(); //TODO exception
        }
        return limits;
    }
}
