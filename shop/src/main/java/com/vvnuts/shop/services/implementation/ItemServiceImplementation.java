package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemServiceImplementation extends AbstractCrudService<Item, Integer> implements ItemService {
    private final ItemRepository itemRepository;
    @Override
    JpaRepository<Item, Integer> getRepository() {
        return itemRepository;
    }
}
