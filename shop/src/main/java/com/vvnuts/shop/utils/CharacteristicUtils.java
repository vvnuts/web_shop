package com.vvnuts.shop.utils;

import com.vvnuts.shop.dtos.responses.erorrs.NotFoundRelatedObjectException;
import com.vvnuts.shop.dtos.responses.erorrs.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.erorrs.Violation;
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
        ValidationErrorResponse response = new ValidationErrorResponse();
        for (Integer characteristicId : characteristicsId) {
            Optional<Characteristic> characteristic = characteristicRepository.findById(characteristicId);
            if (characteristic.isPresent()){
                characteristics.add(characteristic.get());
            } else {
                response.getViolations()
                        .add(new Violation("characteristic", "id = " + characteristicId + " not found"));
            }

        }
        if (response.getViolations().size() > 0) {
            throw new NotFoundRelatedObjectException(response);
        }
        return characteristics;
    }
}
