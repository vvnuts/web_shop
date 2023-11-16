package com.vvnuts.shop.utils;

import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacteristicUtils {
    private final CharacteristicRepository characteristicRepository;

    public List<Characteristic> getCharacteristicListFromDTO(List<Integer> characteristicsId) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (Integer characteristicId : characteristicsId) {
            Optional<Characteristic> characteristic = characteristicRepository.findById(characteristicId);
            if (characteristic.isEmpty()){
                return null; //TODO throw
            } else {
                characteristics.add(characteristic.get());
            }
        }
        return characteristics;
    }
}
