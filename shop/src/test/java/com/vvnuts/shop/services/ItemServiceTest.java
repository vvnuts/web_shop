package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CharacterItemRequest;
import com.vvnuts.shop.dtos.requests.ItemRequest;
import com.vvnuts.shop.entities.*;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.repositories.CharacterItemRepository;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.utils.ImageUtils;
import com.vvnuts.shop.utils.mappers.ItemMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository repository;
    @Mock
    private ItemMapper mapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CharacterItemRepository characterItemRepository;
    @InjectMocks
    private ItemService underTest;

    private static final int ITEM_ID = 1;

    @Test
    void itemService_create_returnItem() {
        //given
        ItemRequest request = createRequest();
        Item newItem = createItem();
        when(mapper.transferItemDtoToItem(request)).thenReturn(newItem);
        when(repository.save(newItem)).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Item result = underTest.create(request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getCharacterItems().get(0).getItem()).isEqualTo(newItem);
        Assertions.assertThat(result.getCharacterItems().get(1).getItem()).isEqualTo(newItem);
    }

    @Test
    void item_service_findById_returnItems() {
        //given
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(createItem()));

        //when
        Item result = underTest.findById(ITEM_ID);

        //then
        Mockito.verify(repository, times(1)).findById(ITEM_ID);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void itemService_findById_throwException() {
        //given
        when(repository.findById(ITEM_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(ITEM_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void itemService_update_returnItemWithOtherNameCharacterItemCategory() {
        //given
        ItemRequest request = createRequest();
        Item oldItem = createItem();
        Item check = createItem();
        List<CharacterItem> oldCharacter = oldItem.getCharacterItems();
        Category oldCategory = oldItem.getCategory();
        Item updDto = createItem();
        updDto.setItemName("text");
        updDto.setCharacterItems(createListCharacterItem());
        updDto.getCharacterItems().get(0).setNumValue(10);
        updDto.setCategory(Category.builder().categoryId(4).items(new ArrayList<>()).build());
        when(mapper.transferItemDtoToItem(request)).thenReturn(updDto);
        when(repository.save(oldItem)).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Item result = underTest.update(request, oldItem);

        //given
        Mockito.verify(characterItemRepository, times(oldItem.getCharacterItems().size())).save(any());
        Mockito.verify(categoryRepository, times(2)).save(any());
        Assertions.assertThat(result.getCharacterItems().get(0)).isNotEqualTo(oldCharacter.get(0));
        Assertions.assertThat(result.getCharacterItems().get(0).getItem()).isEqualTo(result);
        Assertions.assertThat(oldCharacter.get(0).getItem()).isNull();
        Assertions.assertThat(result.getCharacterItems().get(1)).isNotEqualTo(oldCharacter.get(1));
        Assertions.assertThat(oldCharacter.get(1).getItem()).isNull();
        Assertions.assertThat(result.getCategory().getItems().get(0)).isEqualTo(result);
        Assertions.assertThat(oldCategory.getItems().size()).isEqualTo(0);
        Assertions.assertThat(result.getItemName()).isNotEqualTo(check.getItemName());
        Assertions.assertThat(result.getDescription()).isEqualTo(check.getDescription());
        Assertions.assertThat(result.getPrice()).isEqualTo(check.getPrice());
        Assertions.assertThat(result.getQuantity()).isEqualTo(check.getQuantity());
        Assertions.assertThat(result.getQuantity()).isEqualTo(check.getQuantity());
        Assertions.assertThat(result.getSale()).isEqualTo(check.getSale());
    }

    @Test
    void itemService_update_returnItemWithOtherDescriptionPriceQuantitySale() {
        //given
        ItemRequest request = createRequest();
        Item oldItem = createItem();
        Item check = createItem();
        Category oldCategory = oldItem.getCategory();
        Item updDto = createItem();
        updDto.setDescription("text text");
        updDto.setPrice(400);
        updDto.setQuantity(300);
        updDto.setSale(0.);
        when(mapper.transferItemDtoToItem(request)).thenReturn(updDto);
        when(repository.save(oldItem)).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Item result = underTest.update(request, oldItem);

        //given
        Mockito.verify(characterItemRepository, times(oldItem.getCharacterItems().size())).save(any());
        Mockito.verify(categoryRepository, times(2)).save(any());
        Assertions.assertThat(result.getCategory().getCategoryId()).isEqualTo(oldCategory.getCategoryId());
        Assertions.assertThat(result.getItemName()).isEqualTo(check.getItemName());
        Assertions.assertThat(result.getDescription()).isNotEqualTo(check.getDescription());
        Assertions.assertThat(result.getPrice()).isNotEqualTo(check.getPrice());
        Assertions.assertThat(result.getQuantity()).isNotEqualTo(check.getQuantity());
        Assertions.assertThat(result.getQuantity()).isNotEqualTo(check.getQuantity());
        Assertions.assertThat(result.getSale()).isNotEqualTo(check.getSale());
    }

    @Test
    void itemService_uploadImage_returnItemWithImage() throws IOException {
        //given
        MultipartFile file = getFile();
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(createItem()));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when

        Item upd = underTest.uploadImage(file, ITEM_ID);

        //then
        Assertions.assertThat(upd.getImage()).isNotEmpty();
        Assertions.assertThat(upd.getImage()).isEqualTo(ImageUtils.compressImage(file.getBytes()));
    }

    @Test
    void itemService_uploadImage_throwFileIsEmptyException() {
        //given
        MultipartFile file = getEmptyFile();

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.uploadImage(file, ITEM_ID))
                .isInstanceOf(FileIsEmptyException.class);
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void itemService_downloadImage_returnImage() throws IOException {
        //given
        MultipartFile file = getFile();
        Item item = createItem();
        item.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        //when
        byte[] data = underTest.downloadImage(ITEM_ID);

        //then
        Assertions.assertThat(data).isNotEmpty();
        Assertions.assertThat(data).isEqualTo(file.getBytes());
    }

    @Test
    void itemService_downloadImage_returnNull() {
        //given
        Item item = createItem();
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        //when
        byte[] data = underTest.downloadImage(ITEM_ID);

        //then
        Assertions.assertThat(data).isNull();
    }

    @Test
    void itemService_deleteImage_returnItemWithoutImage() throws IOException {
        //given
        MultipartFile file = getFile();
        Item item = createItem();
        item.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Item itemWithoutImage = underTest.deleteImage(ITEM_ID);

        //then
        Mockito.verify(repository, times(1)).save(item);
        Assertions.assertThat(itemWithoutImage.getImage()).isNull();
    }

    @Test
    void itemService_deleteImage_throwImageIsAlreadyEmpty() {
        //given
        Item item = createItem();
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(item));

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.deleteImage(ITEM_ID))
                .isInstanceOf(ImageIsAlreadyNull.class);
        Mockito.verify(repository, never()).save(item);
    }

    @Test
    void itemService_delete_canDelete() {
        //given
        Item deletedItem = createItem();
        when(repository.findById(ITEM_ID)).thenReturn(Optional.of(deletedItem));

        //when
        underTest.delete(ITEM_ID);

        //then
        Mockito.verify(repository, times(1)).findById(ITEM_ID);
        Mockito.verify(repository, times(1)).delete(deletedItem);
    }

    @Test
    void calculateRatingItem() {
        //given
        Item item = createItem();
        item.setReviews(createListReview());
        item.setMark(2.0);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Item result = underTest.calculateRatingItem(item);

        //then
        Assertions.assertThat(result.getMark()).isEqualTo(3.33);
    }

    private ItemRequest createRequest() {
        return ItemRequest.builder()
                .categoryId(1)
                .itemName("test")
                .price(2000)
                .description("text")
                .quantity(3)
                .sale(0.2)
                .characterItems(createListCharacterItemRequest())
                .build();
    }

    private Item createItem() {
        Item item = Item.builder()
                .itemId(ITEM_ID)
                .itemName("test")
                .category(Category.builder().categoryId(1).items(new ArrayList<>()).build())
                .price(2000)
                .description("text")
                .quantity(3)
                .sale(0.2)
                .characterItems(createListCharacterItem())
                .build();
        item.getCategory().getItems().add(item);
        return item;
    }

    private List<CharacterItemRequest> createListCharacterItemRequest() {
        List<CharacterItemRequest> requests = new ArrayList<>();
        requests.add(CharacterItemRequest.builder()
                .characteristic(3)
                .numValue(4)
                .build());
        requests.add(CharacterItemRequest.builder()
                .characteristic(2)
                .value("some")
                .build());
        return requests;
    }

    private List<CharacterItem> createListCharacterItem() {
        List<CharacterItem> characterItems = new ArrayList<>();
        characterItems.add(CharacterItem.builder()
                .characteristic(Characteristic.builder().id(3).build())
                .numValue(4)
                .build());
        characterItems.add(CharacterItem.builder()
                .characteristic(Characteristic.builder().id(2).build())
                .value("some")
                .build());
        return characterItems;
    }

    private List<Review> createListReview() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(Review.builder()
                .mark(3)
                .build());
        reviews.add(Review.builder()
                .mark(2)
                .build());
        reviews.add(Review.builder()
                .mark(5)
                .build());
        return reviews;
    }

    private MockMultipartFile getFile() {
        return new MockMultipartFile("name.png",
                "originalFileName.png", "image/png", new byte[] {11, 101, 12, 13});
    }

    private MockMultipartFile getEmptyFile() {
        return new MockMultipartFile("name.png",
                "originalFileName.png", "image/png", new byte[] {});
    }
}