package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.services.interfaces.CharacterItemService;
import com.vvnuts.shop.utils.CategoryUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryUtils categoryUtils;
    private final CharacterItemService characterItemService;
    private final ModelMapper modelMapper;

    public void create(CharacteristicRequest request) {
        Characteristic characteristic = Characteristic.builder()
                .name(request.getName())
                .categories(categoryUtils.getCategoryListFromIds(request.getCategories()))
                .build();
        characteristicRepository.save(characteristic);
        for (Category category: characteristic.getCategories()) {
            category.getCharacteristics().add(characteristic);
            characterItemService.addCategoryItemsCharacteristic(category, characteristic);
            categoryRepository.save(category);
        }
    }

    public CharacteristicResponse findById(Integer id) {
        Characteristic characteristic = characteristicRepository.findById(id).orElseThrow();
        return convertEntityToResponse(characteristic);
    }

    public List<CharacteristicResponse> findAll() {
        List<CharacteristicResponse> characteristicResponses = new ArrayList<>();
        for (Characteristic characteristic: characteristicRepository.findAll()) {
            characteristicResponses.add(convertEntityToResponse(characteristic));
        }
        return characteristicResponses;
    }

    public void update(CharacteristicRequest request, Integer id) {
        Characteristic updateCharacteristic = characteristicRepository.findById(id).orElseThrow();
        Characteristic updateDto = Characteristic.builder()
                .name(request.getName())
                .categories(categoryUtils.getCategoryListFromIds(request.getCategories()))
                .build();
        updateCharacteristic.setName(request.getName());
        Set<Category> oldCategory = new HashSet<>(updateCharacteristic.getCategories());
        for (Characteristic newCharacteristic : updateDTO.getCharacteristics()) {
            if (!oldCategory.contains(newCharacteristic)) {
                updateCategory.getCharacteristics().add(newCharacteristic);
                if (newCharacteristic.getCategories() == null) {
                    List<Category> temp = new ArrayList<>();
                    temp.add(updateCategory);
                    newCharacteristic.setCategories(temp);
                } else {
                    newCharacteristic.getCategories().add(updateCategory);
                }
                characterItemService.addCategoryItemsCharacteristic(updateCategory, newCharacteristic); // TODO Проверить работает ли. Есть сомнение по отсутсвию id в newCharacteristic
                characteristicRepository.save(newCharacteristic);
            } else {
                oldCategory.remove(newCharacteristic);
            }
        }

        E entity = transferToUpdateEntity(request, updateCharacteristic);
        getRepository().save(entity);
    }

    @Override
    public void delete(E entity) {
        getRepository().delete(entity);
    }

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

    public List<Characteristic> getCharacteristicListFromDTO(List<Integer> characteristicsId) {
        List<Characteristic> characteristics = new ArrayList<>();
        for (Integer characteristicId : characteristicsId) {
            Optional<Characteristic> characteristic = characteristicRepository.findById(characteristicId);
            if (characteristic.isEmpty()){
//                Characteristic newCharacteristic = new Characteristic();
//                newCharacteristic.setName(characteristicId.getName());
//                characteristicRepository.save(newCharacteristic);
//                characteristics.add(newCharacteristic);
                return null; //TODO throw
            } else {
                characteristics.add(characteristic.get());
            }
        }
        return characteristics;
    }

    public void replaceCharacteristicsInCategory (Category updateCategory, Category updateDTO) {
        Set<Characteristic> oldCharacteristic = new HashSet<>(updateCategory.getCharacteristics());
        for (Characteristic newCharacteristic : updateDTO.getCharacteristics()) {
            if (!oldCharacteristic.contains(newCharacteristic)) {
                updateCategory.getCharacteristics().add(newCharacteristic);
                newCharacteristic.getCategories().add(updateCategory);
                characterItemService.addCategoryItemsCharacteristic(updateCategory, newCharacteristic); // TODO Проверить работает ли. Есть сомнение по отсутсвию id в newCharacteristic
                characteristicRepository.save(newCharacteristic);
            } else {
                oldCharacteristic.remove(newCharacteristic);
            }
        }
        if (oldCharacteristic.size() > 0) {
            for (Characteristic removeCharacteristic : oldCharacteristic) {
                updateCategory.getCharacteristics().remove(removeCharacteristic);
                removeCharacteristic.getCategories().remove(updateCategory);
                characterItemService.removeCategoryItemsCharacteristic(updateCategory, removeCharacteristic);  //TODO смущает Category
                characteristicRepository.save(removeCharacteristic);
            }
        }
    }

    CharacteristicResponse convertEntityToResponse(Characteristic characteristic) {
        return modelMapper.map(characteristic, CharacteristicResponse.class);
    }
}
