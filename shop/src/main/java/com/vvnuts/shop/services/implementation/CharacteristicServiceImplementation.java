package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.services.interfaces.CharacteristicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacteristicServiceImplementation extends AbstractCrudService<Characteristic, CharacteristicRequest, Integer> implements CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    @Override
    JpaRepository<Characteristic, Integer> getRepository() {
        return characteristicRepository;
    }

    @Override
    Characteristic transferToUpdateEntity(CharacteristicRequest dto, Characteristic updateEntity) {
        return null;
    }

    @Override
    Characteristic transferToCreateEntity(CharacteristicRequest dto) {
        return null;
    }

    @Override
    public List<Characteristic> transferIdsToCharacteristicList(List<Integer> ids, Category newCategory) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (Integer id: ids) {
            Optional<Characteristic> characteristic = characteristicRepository.findById(id);
            if (characteristic.isEmpty()) {
                return null; //TODO make throw
            }
            characteristics.add(characteristic.get());
            characteristic.get().getCategories().add(newCategory); //Мб сделать pred-check
        }
        return characteristics;
    }

    @Override
    public List<Characteristic> getCharacteristicListFromDTO(List<CharacteristicRequest> characteristicsDTO) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (CharacteristicRequest characteristicRequest : characteristicsDTO) {
            Optional<Characteristic> characteristic = characteristicRepository.findByName(characteristicRequest.getName());
            if (characteristic.isEmpty()){
                Characteristic newCharacteristic = new Characteristic();
                newCharacteristic.setName(characteristicRequest.getName());
                characteristicRepository.save(newCharacteristic);
                characteristics.add(newCharacteristic);
            } else {
                characteristics.add(characteristic.get());
            }
        }
        return characteristics;
    }
}
