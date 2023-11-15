package com.vvnuts.shop.controllers;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import com.vvnuts.shop.services.interfaces.CrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/characteristic")
public class CharacteristicController extends AbstractCrudController<Characteristic, CharacteristicRequest, Integer>{
    private final CharacteristicService characteristicService;

    @Override
    CrudService<Characteristic, CharacteristicRequest, Integer> getService() {
        return characteristicService;
    }
}
