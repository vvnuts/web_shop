package com.vvnuts.shop.repositories;

import com.vvnuts.shop.entities.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacteristicRepository extends JpaRepository<Characteristic, Integer> {
    Optional<Characteristic> findByName(String name);
}
