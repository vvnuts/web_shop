package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CharacterItemRepository extends JpaRepository<CharacterItem, Integer> {
    Optional<CharacterItem> findByCharacteristicAndItem(Characteristic characteristic, Item item);
}
