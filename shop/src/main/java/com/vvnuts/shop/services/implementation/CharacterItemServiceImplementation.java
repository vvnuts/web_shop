package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.CharacterItem;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.services.interfaces.CharacterItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterItemServiceImplementation extends AbstractCrudService<CharacterItem, Integer> implements CharacterItemService {
    private final CharacterItemRepository characterItemRepository;
    @Override
    JpaRepository<CharacterItem, Integer> getRepository() {
        return characterItemRepository;
    }
}
