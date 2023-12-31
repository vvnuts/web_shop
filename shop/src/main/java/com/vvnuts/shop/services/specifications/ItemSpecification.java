package com.vvnuts.shop.services.specifications;


import com.vvnuts.shop.dtos.requests.SpecificationItemRequest;
import com.vvnuts.shop.entities.Item;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemSpecification {
    public Specification<Item> createSpecification(SpecificationItemRequest request) {
        Specification<Item> itemSpecification = getItemInStock().and(getItemFromCategory(request.getCategoryId()));

        return itemSpecification;
    }

    private Specification<Item> getItemInStock() {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.isNotNull(root.get("quantity"));
    }

    private Specification<Item> getItemFromCategory(Integer id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("categoryId"), id);
    }
}
