package com.vvnuts.shop.services.specifications;


import com.vvnuts.shop.dtos.requests.SpecificationItemRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ItemSpecification {
    public Specification<Item> createSpecification(SpecificationItemRequest request) {
        Specification<Item> itemSpecification = getItemInStock().and(getItemFromCategory(request.getCategory()));

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
}
