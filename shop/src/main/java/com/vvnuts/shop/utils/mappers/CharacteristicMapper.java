package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.exceptions.NotFoundRelatedObjectException;
import com.vvnuts.shop.dtos.responses.ValidationErrorResponse;
import com.vvnuts.shop.dtos.responses.Violation;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacteristicMapper {
    private final CharacteristicRepository characteristicRepository;
    private final ModelMapper modelMapper;

    public List<Characteristic> getCharacteristicListFromDTO(Collection<Integer> characteristicsId) {
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

    public List<CharacteristicResponse> convertEntityToListResponse(Collection<Characteristic> characteristics) {
        List<CharacteristicResponse> responses = new ArrayList<>();
        for (Characteristic characteristic: characteristics) {
            responses.add(convertEntityToResponse(characteristic));
        }
        return responses;
    }

    public CharacteristicResponse convertEntityToResponse(Characteristic characteristic) {
        return modelMapper.map(characteristic, CharacteristicResponse.class);
    }
}
