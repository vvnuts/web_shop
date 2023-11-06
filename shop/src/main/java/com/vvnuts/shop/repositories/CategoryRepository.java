package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName (String CategoryName);
}
