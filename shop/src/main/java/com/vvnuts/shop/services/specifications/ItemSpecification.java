package com.vvnuts.shop.services.specifications;


import com.vvnuts.shop.dtos.requests.SpecificationItemRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemSpecification {
    public Specification<Item> createSpecification(SpecificationItemRequest request) {
        Specification<Item> itemSpecification = getItemInStock();

        if (request.getRangeOfPrice() != null) {
            itemSpecification = itemSpecification.and(getItemWithRangeOfMark(request.getRangeOfPrice()));
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

    private Specification<Item> getItemWithRangeOfMark(String range) {
        String[] stingLimits = range.split("-");
        List<Integer> limits = Arrays.stream(stingLimits).map(Integer::parseInt).toList();
        if (limits.get(0) > limits.get(1)) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), limits.get(0), limits.get(1));
    }
}
