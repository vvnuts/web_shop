package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.CharacterItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterItemRepository extends JpaRepository<CharacterItem, Integer> {
}
