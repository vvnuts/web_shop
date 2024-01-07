package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacteristicRequest;
import com.vvnuts.shop.dtos.responses.CharacteristicResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.entities.enums.Type;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacteristicRepository;
import com.vvnuts.shop.utils.mappers.CategoryMapper;
import com.vvnuts.shop.utils.mappers.CharacteristicMapper;
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
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacteristicServiceTest {
    @Mock
    private CharacteristicRepository repository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CharacterItemService characterItemService;
    @Mock
    private CharacteristicMapper mapper;
    @InjectMocks
    private CharacteristicService underTest;

    private static final int CHARACTERISTIC_ID = 3;

    @Test
    void characteristicService_create_returnCharacteristic() {
        //given
        CharacteristicRequest request = createRequest();
        List<Category> categories = createListOfCategory(new int[] {1, 3});
        when(categoryMapper.getCategoryListFromIds(any())).thenReturn(categories);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Characteristic result = underTest.create(request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Mockito.verify(characterItemService, times(request.getCategories().size())).addCategoryItemsCharacteristic(any(), any());
        Assertions.assertThat(result.getName()).isEqualTo(request.getName());
        Assertions.assertThat(result.getType()).isEqualTo(Type.STRING);
        Assertions.assertThat(result.getCategories().get(0)).isEqualTo(categories.get(0));
    }

    @Test
    void characteristicService_findById_returnCharacteristic() {
        //given
        when(repository.findById(CHARACTERISTIC_ID)).thenReturn(Optional.of(createCharacteristic()));

        //when
        Characteristic result = underTest.findById(CHARACTERISTIC_ID);

        //then
        Mockito.verify(repository, times(1)).findById(CHARACTERISTIC_ID);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void characteristicService_findById_throwException() {
        //given
        when(repository.findById(CHARACTERISTIC_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(CHARACTERISTIC_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void characteristicService_findOne_returnCharacteristicResponse() {
        //given
        Characteristic characteristic = createCharacteristic();
        CharacteristicResponse response = createResponse();
        when(repository.findById(CHARACTERISTIC_ID)).thenReturn(Optional.of(characteristic));
        when(mapper.convertEntityToResponse(characteristic)).thenReturn(response);

        //when
        CharacteristicResponse result = underTest.findOne(CHARACTERISTIC_ID);

        //then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void characteristicService_findAll_returnListOfTwoResponses() {
        //given
        List<Characteristic> characteristics = List.of(createCharacteristic(), createCharacteristic());
        when(repository.findAll()).thenReturn(characteristics);
        when(mapper.convertEntityToResponse(characteristics.get(0))).thenReturn(createResponse());
        when(mapper.convertEntityToResponse(characteristics.get(1))).thenReturn(createResponse());

        //when
        List<CharacteristicResponse> result = underTest.findAll();

        //then
        Assertions.assertThat(result.size()).isEqualTo(characteristics.size());
    }

    @Test
    void characteristicService_update_returnCharacteristicWithTwoOtherAndOneSameCategories() {
        //given
        CharacteristicRequest request = createRequest();
        request.setName("Test2");
        request.setType(Type.INTEGER);
        List<Category> categories = createListOfCategory(new int[] {2, 3, 4});
        Characteristic characteristic = createCharacteristic();
        when(categoryMapper.getCategoryListFromIds(any())).thenReturn(categories);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Characteristic result = underTest.update(request, characteristic);

        //then
        Mockito.verify(characterItemService, times(2)).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, times(1)).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(categoryRepository, times(3)).save(any());
        Assertions.assertThat(result.getCategories().size()).isEqualTo(3);
        Assertions.assertThat(result.getCategories().get(0).getCategoryId()).isEqualTo(3);
        Assertions.assertThat(result.getCategories().get(1).getCategoryId()).isEqualTo(2);
        Assertions.assertThat(result.getCategories().get(2).getCategoryId()).isEqualTo(4);
        Assertions.assertThat(result.getName()).isEqualTo("Test2");
        Assertions.assertThat(result.getType()).isEqualTo(Type.INTEGER);
    }

    @Test
    void characteristicService_update_returnCharacteristicWithTwoSameCategories() {
        //given
        CharacteristicRequest request = createRequest();
        List<Category> categories = createListOfCategory(new int[] {3, 1});
        Characteristic characteristic = createCharacteristic();
        when(categoryMapper.getCategoryListFromIds(any())).thenReturn(categories);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Characteristic result = underTest.update(request, characteristic);

        //then
        Mockito.verify(characterItemService, never()).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, never()).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(categoryRepository, never()).save(any());
        Assertions.assertThat(result.getCategories().size()).isEqualTo(2);
        Assertions.assertThat(result.getCategories().get(0).getCategoryId()).isEqualTo(1);
        Assertions.assertThat(result.getCategories().get(1).getCategoryId()).isEqualTo(3);
    }

    @Test
    void characteristicService_update_returnCharacteristicWithTwoSameAndOneNewCategories() {
        //given
        CharacteristicRequest request = createRequest();
        List<Category> categories = createListOfCategory(new int[] {3, 1, 4});
        Characteristic characteristic = createCharacteristic();
        when(categoryMapper.getCategoryListFromIds(any())).thenReturn(categories);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Characteristic result = underTest.update(request, characteristic);

        //then
        Mockito.verify(characterItemService, times(1)).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, never()).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(categoryRepository, times(1)).save(any());
        Assertions.assertThat(result.getCategories().size()).isEqualTo(3);
        Assertions.assertThat(result.getCategories().get(0).getCategoryId()).isEqualTo(1);
        Assertions.assertThat(result.getCategories().get(1).getCategoryId()).isEqualTo(3);
        Assertions.assertThat(result.getCategories().get(2).getCategoryId()).isEqualTo(4);
    }

    @Test
    void characteristicService_update_returnCharacteristicWithOneSameCategory() {
        //given
        CharacteristicRequest request = createRequest();
        List<Category> categories = createListOfCategory(new int[] {3});
        Characteristic characteristic = createCharacteristic();
        when(categoryMapper.getCategoryListFromIds(any())).thenReturn(categories);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Characteristic result = underTest.update(request, characteristic);

        //then
        Mockito.verify(characterItemService, never()).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, times(1)).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(categoryRepository, times(1)).save(any());
        Assertions.assertThat(result.getCategories().size()).isEqualTo(1);
        Assertions.assertThat(result.getCategories().get(0).getCategoryId()).isEqualTo(3);
    }

    @Test
    void characterItemService_deleteCharacterItem_canDelete() {
        //given
        Characteristic characteristic = createCharacteristic();
        when(repository.findById(CHARACTERISTIC_ID)).thenReturn(Optional.of(characteristic));

        //when
        underTest.delete(CHARACTERISTIC_ID);

        //then
        Mockito.verify(repository, times(1)).findById(CHARACTERISTIC_ID);
        Mockito.verify(repository, times(1)).delete(characteristic);
    }

    @Test
    void characteristicService_replaceCharacteristicsInCategory_returnCategoryWithTwoOtherAndOneSameCharacteristics() {
        //given
        Category updCategory = createCategory();
        Category updDTO = createCategory();
        updDTO.setCharacteristics(createListOfCharacteristic(new int[] {2, 3, 4}));

        //when
        Category result = underTest.replaceCharacteristicsInCategory(updCategory, updDTO);

        //then
        Mockito.verify(characterItemService, times(2)).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, times(1)).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(repository, times(3)).save(any());
        Assertions.assertThat(result.getCharacteristics().size()).isEqualTo(3);
        Assertions.assertThat(result.getCharacteristics().get(0).getId()).isEqualTo(3);
        Assertions.assertThat(result.getCharacteristics().get(1).getId()).isEqualTo(2);
        Assertions.assertThat(result.getCharacteristics().get(2).getId()).isEqualTo(4);
    }

    @Test
    void characteristicService_replaceCharacteristicsInCategory_returnCategoryWithTwoSameCharacteristics() {
        //given
        Category updCategory = createCategory();
        Category updDTO = createCategory();
        updDTO.setCharacteristics(createListOfCharacteristic(new int[] {3, 1}));

        //when
        Category result = underTest.replaceCharacteristicsInCategory(updCategory, updDTO);

        //then
        Mockito.verify(characterItemService, never()).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, never()).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(repository, never()).save(any());
        Assertions.assertThat(result.getCharacteristics().size()).isEqualTo(2);
        Assertions.assertThat(result.getCharacteristics().get(0).getId()).isEqualTo(1);
        Assertions.assertThat(result.getCharacteristics().get(1).getId()).isEqualTo(3);}

    @Test
    void characteristicService_replaceCharacteristicsInCategory_returnCategoryWithTwoSameAndOneNewCharacteristics() {
        //given
        Category updCategory = createCategory();
        Category updDTO = createCategory();
        updDTO.setCharacteristics(createListOfCharacteristic(new int[] {1, 3, 4}));

        //when
        Category result = underTest.replaceCharacteristicsInCategory(updCategory, updDTO);

        //then
        Mockito.verify(characterItemService, times(1)).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, never()).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getCharacteristics().size()).isEqualTo(3);
        Assertions.assertThat(result.getCharacteristics().get(0).getId()).isEqualTo(1);
        Assertions.assertThat(result.getCharacteristics().get(1).getId()).isEqualTo(3);
        Assertions.assertThat(result.getCharacteristics().get(2).getId()).isEqualTo(4);
    }

    @Test
    void characteristicService_replaceCharacteristicsInCategory_returnCategoryOneSameCharacteristic() {
        //given
        Category updCategory = createCategory();
        Category updDTO = createCategory();
        updDTO.setCharacteristics(createListOfCharacteristic(new int[] {3}));

        //when
        Category result = underTest.replaceCharacteristicsInCategory(updCategory, updDTO);

        //then
        Mockito.verify(characterItemService, never()).addCategoryItemsCharacteristic(any(), any());
        Mockito.verify(characterItemService, times(1)).removeCategoryItemsCharacteristic(any(), any());
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getCharacteristics().size()).isEqualTo(1);
        Assertions.assertThat(result.getCharacteristics().get(0).getId()).isEqualTo(3);
    }

    private CharacteristicRequest createRequest() {
        return CharacteristicRequest.builder()
                .name("Test")
                .type(Type.STRING)
                .categories(List.of(1, 3))
                .build();
    }

    private Characteristic createCharacteristic() {
        return Characteristic.builder()
                .id(CHARACTERISTIC_ID)
                .name("Test")
                .type(Type.STRING)
                .categories(createListOfCategory(new int[] {1, 3}))
                .build();
    }

    private Category createCategory() {
        return Category.builder()
                .characteristics(createListOfCharacteristic(new int[] {1, 3}))
                .build();
    }

    private List<Category> createListOfCategory(int[] ids) {
        List<Category> categories = new ArrayList<>();
        IntStream.of(ids).forEach(id -> categories.add(Category.builder()
                .categoryId(id)
                .characteristics(new ArrayList<>())
                .build()));
        return  categories;
    }

    private List<Characteristic> createListOfCharacteristic(int[] ids) {
        List<Characteristic> characteristics = new ArrayList<>();
        IntStream.of(ids).forEach(id -> characteristics.add(Characteristic.builder()
                .id(id)
                .categories(new ArrayList<>())
                .build()));
        return  characteristics;
    }

    private CharacteristicResponse createResponse() {
        return CharacteristicResponse.builder()
                .id(CHARACTERISTIC_ID)
                .name("Test")
                .type(Type.STRING)
                .build();
    }
}