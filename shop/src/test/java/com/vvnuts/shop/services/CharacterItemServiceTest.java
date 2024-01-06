package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.entities.*;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.mappers.CharacterItemMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterItemServiceTest {
    @Mock
    private CharacterItemRepository repository;
    @Mock
    private CharacterItemMapper mapper;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CharacteristicRepository characteristicRepository;
    @InjectMocks
    private CharacterItemService underTest;

    private static final int CHARACTER_ITEM_ID = 1;
    private static final int ITEM_ID = 3;

    @Test
    void characterItemService_create_returnCharacterItem() {
        //given
        CharacterItemRequest request = createNumericCharacterItemRequest();
        CharacterItem characterItem = createNumericCharacterItem();
        Item item = createItem();
        when(mapper.transferRequestToEntity(request)).thenReturn(characterItem);
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(repository.save(characterItem)).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        CharacterItem result = underTest.create(ITEM_ID, request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Mockito.verify(itemRepository, times(1)).save(any());
        Mockito.verify(characteristicRepository, times(1)).save(any());
        Assertions.assertThat(result.getItem()).isEqualTo(item);
        Assertions.assertThat(result.getCharacteristic().getCharacterItems().get(0)).isEqualTo(result);
        Assertions.assertThat(result.getItem().getCharacterItems().get(0)).isEqualTo(result);
    }

    @Test
    void characterItemService_create_throwException() {
        //given
        CharacterItemRequest request = createNumericCharacterItemRequest();

        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.create(ITEM_ID, request))
                .isInstanceOf(NoSuchElementException.class);
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void characterItemService_findById_returnCharacterItem() {
        //given
        when(repository.findById(CHARACTER_ITEM_ID)).thenReturn(Optional.of(createNumericCharacterItem()));

        //when
        CharacterItem result = underTest.findById(CHARACTER_ITEM_ID);

        //then
        Mockito.verify(repository, times(1)).findById(CHARACTER_ITEM_ID);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void characterItemService_findById_throwException() {
        //given
        when(repository.findById(CHARACTER_ITEM_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(CHARACTER_ITEM_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void characterItemService_findAllCharacterItemFromItem_returnTwoCharacterItem() {
        //given
        Item item = createItem();
        item.getCharacterItems().add(createStringCharacterItem());
        item.getCharacterItems().add(createNumericCharacterItem());
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        //when
        List<CharacterItem> result = underTest.findAllCharacterItemFromItem(ITEM_ID);

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result.get(0).getNumValue()).isNull();
        Assertions.assertThat(result.get(1).getValue()).isNull();
    }

    @Test
    void characterItemService_findAllCharacterItemFromItem_throwException() {
        //given
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findAllCharacterItemFromItem(ITEM_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void characterItemService_update_returnStringCharacterItem() {
        //given
        CharacterItemRequest request = createStringCharacterItemRequest();
        CharacterItem characterItem = createNumericCharacterItem();
        CharacterItem update = createStringCharacterItem();
        when(mapper.transferRequestToEntity(request)).thenReturn(update);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        CharacterItem result = underTest.update(characterItem, request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getNumValue()).isNull();
        Assertions.assertThat(result.getValue()).isEqualTo("text");
        Assertions.assertThat(result.getCharacteristic().getId()).isEqualTo(3);
    }

    @Test
    void characterItemService_update_returnNumericCharacterItem() {
        //given
        CharacterItemRequest request = createNumericCharacterItemRequest();
        CharacterItem characterItem = createStringCharacterItem();
        CharacterItem update = createNumericCharacterItem();
        when(mapper.transferRequestToEntity(request)).thenReturn(update);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        CharacterItem result = underTest.update(characterItem, request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getValue()).isNull();
        Assertions.assertThat(result.getNumValue()).isEqualTo(123);
        Assertions.assertThat(result.getCharacteristic().getId()).isEqualTo(2);
    }

    @Test
    void characterItemService_deleteCharacterItem_canDelete() {
        //given
        CharacterItem characterItem = createStringCharacterItem();
        when(repository.findById(CHARACTER_ITEM_ID)).thenReturn(Optional.of(characterItem));

        //when
        underTest.deleteCharacterItem(CHARACTER_ITEM_ID);

        //then
        Mockito.verify(repository, times(1)).findById(CHARACTER_ITEM_ID);
        Mockito.verify(repository, times(1)).delete(characterItem);
    }

    @Test
    void characteristicItemService_addCategoryItemsCharacteristic_createCharacterItem() {
        //given
        Category category = createCategory();
        Characteristic characteristic = Characteristic.builder().id(3).build();

        //when
        underTest.addCategoryItemsCharacteristic(category, characteristic);

        //then
        Mockito.verify(repository, times(category.getItems().size())).save(any());
    }

    @Test
    void characterItemService_removeCategoryItemsCharacteristic_removeTwoCharacterItemOfTheThree() {
        //given
        Category category = createCategory();
        List<Item> categoryItem = category.getItems();
        Characteristic characteristic = Characteristic.builder().id(3).build();
        when(repository.findByCharacteristicAndItem(characteristic, categoryItem.get(0)))
                .thenReturn(Optional.of(categoryItem.get(0).getCharacterItems().get(0)));
        when(repository.findByCharacteristicAndItem(characteristic, categoryItem.get(1)))
                .thenReturn(Optional.empty());
        when(repository.findByCharacteristicAndItem(characteristic, categoryItem.get(2)))
                .thenReturn(Optional.of(categoryItem.get(2).getCharacterItems().get(0)));

        //when
        underTest.removeCategoryItemsCharacteristic(category, characteristic);

        //then
        Mockito.verify(repository, times(2)).delete(any());
    }

    private CharacterItemRequest createNumericCharacterItemRequest() {
        return CharacterItemRequest.builder()
                .numValue(123)
                .build();
    }

    private CharacterItemRequest createStringCharacterItemRequest() {
        return CharacterItemRequest.builder()
                .value("text")
                .build();
    }

    private CharacterItem createNumericCharacterItem() {
        return CharacterItem.builder()
                .id(CHARACTER_ITEM_ID)
                .numValue(123)
                .characteristic(Characteristic.builder().id(2).characterItems(new ArrayList<>()).build())
                .build();
    }

    private CharacterItem createStringCharacterItem() {
        return CharacterItem.builder()
                .id(CHARACTER_ITEM_ID)
                .value("text")
                .characteristic(Characteristic.builder().id(3).characterItems(new ArrayList<>()).build())
                .build();
    }

    private Item createItem() {
        return Item.builder()
                .itemId(ITEM_ID)
                .characterItems(new ArrayList<>())
                .build();
    }

    private Category createCategory() {
        Category category = Category.builder()
                .items(new ArrayList<>())
                .build();
        category.getItems().add(Item.builder().characterItems(new ArrayList<>()).build());
        category.getItems().get(0).getCharacterItems().add(createStringCharacterItem());

        category.getItems().add(Item.builder().characterItems(new ArrayList<>()).build());

        category.getItems().add(Item.builder().characterItems(new ArrayList<>()).build());
        category.getItems().get(2).getCharacterItems().add(createStringCharacterItem());
        return category;
    }
}