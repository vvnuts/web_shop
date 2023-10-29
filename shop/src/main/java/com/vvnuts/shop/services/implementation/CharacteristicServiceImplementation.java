package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacteristicServiceImplementation extends AbstractCrudService<Characteristic, Integer> implements CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    @Override
    JpaRepository<Characteristic, Integer> getRepository() {
        return characteristicRepository;
    }
}
