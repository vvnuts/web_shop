package com.vvnuts.shop.services.specifications;


import com.vvnuts.shop.entities.Item;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemSpecification {
    public Specification<Item> getSpecification(String name) {
        return (root, query, criteriaBuilder)
                -> {
            List<Predicate> predicates = new ArrayList<>();

        }
    }
}
