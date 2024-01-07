package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CategoryRequest;
import com.vvnuts.shop.dtos.responses.CategoryResponse;
import com.vvnuts.shop.entities.Category;
import com.vvnuts.shop.entities.Characteristic;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
import com.vvnuts.shop.repositories.CategoryRepository;
import com.vvnuts.shop.utils.ImageUtils;
import com.vvnuts.shop.utils.mappers.CategoryMapper;
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
import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository repository;
    @Mock
    private CategoryMapper mapper;
    @Mock
    private CharacteristicService characteristicService;
    @InjectMocks
    private CategoryService underTest;

    private static final int CATEGORY_ID = 3;

    @Test
    void categoryService_create_returnCategory() {
        //given
        CategoryRequest request = createRequest();
        Category category = createCategory();
        when(mapper.transferCategoryDtoToCategory(request)).thenReturn(category);
        when((repository.save(any()))).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Category result = underTest.create(request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getCharacteristics().get(0).getCategories().get(0)).isEqualTo(result);
        Assertions.assertThat(result.getCharacteristics().get(1).getCategories().get(0)).isEqualTo(result);
        Assertions.assertThat(result.getParents().get(0).getChildren().get(0)).isEqualTo(result);
        Assertions.assertThat(result.getParents().get(1).getChildren().get(0)).isEqualTo(result);
    }

    @Test
    void categoryService_findById_returnCategory() {
        //given
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(createCategory()));

        //when
        Category result = underTest.findById(CATEGORY_ID);

        //then
        Mockito.verify(repository, times(1)).findById(CATEGORY_ID);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void categoryService_findById_throwException() {
        //given
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(CATEGORY_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void categoryService_findAll_returnListOfTwoResponse() {
        //given
        when(repository.findAll()).thenReturn(List.of(createCategory(), createCategory()));
        when(mapper.convertEntityToListResponse(any())).thenReturn(List.of(new CategoryResponse(), new CategoryResponse()));

        //when
        List<CategoryResponse> result = underTest.findAll();

        //then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void categoryService_update_returnCategoryWithOtherParent() {
        //given
        CategoryRequest request = createRequest();
        Category updCategory = createCategory();
        updCategory.getParents().get(0).getChildren().add(updCategory);
        updCategory.getParents().get(1).getChildren().add(updCategory);
        Category updateDTO = createCategory();
        updateDTO.setCategoryName("Test3");
        updateDTO.setParents(createListOfCategory(new int[] {7, 8}));
        when(mapper.transferCategoryDtoToCategory(request)).thenReturn(updateDTO);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Category result = underTest.update(request, updCategory);

        //then
        Mockito.verify(repository, times(5)).save(any());
        Mockito.verify(characteristicService, times(1)).replaceCharacteristicsInCategory(any(), any());
        Assertions.assertThat(result.getCategoryName()).isEqualTo("Test3");
        Assertions.assertThat(result.getParents().get(0).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(result.getParents().get(1).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(updateDTO.getParents().get(0).getChildren().get(0)).isEqualTo(result);
        Assertions.assertThat(updateDTO.getParents().get(1).getChildren().get(0)).isEqualTo(result);
    }

    @Test
    void categoryService_delete_canDelete() {
        //given
        Category category = createCategory();
        category.getParents().get(0).getChildren().add(category);
        category.getParents().get(1).getChildren().add(category);
        category.getChildren().get(0).getParents().add(new Category());
        category.getChildren().get(1).getParents().add(new Category());
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        //when
        underTest.delete(CATEGORY_ID);

        //then
        Mockito.verify(repository, times(2)).save(any());
        Mockito.verify(repository, times(1)).delete(any());
        Assertions.assertThat(category.getParents().get(0).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(category.getParents().get(1).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(category.getChildren().get(0).getParents().size()).isEqualTo(1);
        Assertions.assertThat(category.getChildren().get(1).getParents().size()).isEqualTo(1);
    }

    @Test
    void categoryService_delete_canDeleteCategoryWithoutNodeWithOtherParent() {
        //given
        Category category = createCategory();
        category.getParents().get(0).getChildren().add(category);
        category.getParents().get(1).getChildren().add(category);
        category.getChildren().get(0).getParents().add(new Category());
        category.getChildren().get(0).getParents().add(category);
        category.getChildren().get(1).getParents().add(new Category());
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        //when
        underTest.delete(CATEGORY_ID);

        //then
        Mockito.verify(repository, times(3)).save(any());
        Mockito.verify(repository, times(1)).delete(any());
        Assertions.assertThat(category.getParents().get(0).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(category.getParents().get(1).getChildren().size()).isEqualTo(0);
        Assertions.assertThat(category.getChildren().get(0).getParents().size()).isEqualTo(1);
        Assertions.assertThat(category.getChildren().get(0).getParents().get(0)).isNotEqualTo(category);
        Assertions.assertThat(category.getChildren().size()).isEqualTo(1);
    }

    @Test
    void categoryService_uploadImage_returnCategoryWithImage() throws IOException {
        //given
        MultipartFile file = getFile();
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(createCategory()));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when

        Category upd = underTest.uploadImage(file, CATEGORY_ID);

        //then
        Assertions.assertThat(upd.getImage()).isNotEmpty();
        Assertions.assertThat(upd.getImage()).isEqualTo(ImageUtils.compressImage(file.getBytes()));
    }

    @Test
    void categoryService_uploadImage_throwFileIsEmptyException() {
        //given
        MultipartFile file = getEmptyFile();

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.uploadImage(file, CATEGORY_ID))
                .isInstanceOf(FileIsEmptyException.class);
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void categoryService_downloadImage_returnImage() throws IOException {
        //given
        MultipartFile file = getFile();
        Category category = createCategory();
        category.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        //when
        byte[] data = underTest.downloadImage(CATEGORY_ID);

        //then
        Assertions.assertThat(data).isNotEmpty();
        Assertions.assertThat(data).isEqualTo(file.getBytes());
    }

    @Test
    void categoryService_downloadImage_returnNull() {
        //given
        Category category = createCategory();
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        //when
        byte[] data = underTest.downloadImage(CATEGORY_ID);

        //then
        Assertions.assertThat(data).isNull();
    }

    @Test
    void categoryService_deleteImage_returnCategoryWithoutImage() throws IOException {
        //given
        MultipartFile file = getFile();
        Category category = createCategory();
        category.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Category categoryWithoutImage = underTest.deleteImage(CATEGORY_ID);

        //then
        Mockito.verify(repository, times(1)).save(category);
        Assertions.assertThat(categoryWithoutImage.getImage()).isNull();
    }

    @Test
    void categoryService_deleteImage_throwImageIsAlreadyEmpty() {
        //given
        Category category = createCategory();
        when(repository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.deleteImage(CATEGORY_ID))
                .isInstanceOf(ImageIsAlreadyNull.class);
        Mockito.verify(repository, never()).save(category);
    }

    private CategoryRequest createRequest() {
        return CategoryRequest.builder()
                .categoryName("Test")
                .characteristics(Set.of(5, 6))
                .parents(Set.of(1, 2))
                .build();
    }

    private Category createCategory() {
        return Category.builder()
                .categoryId(CATEGORY_ID)
                .categoryName("Test")
                .characteristics(createListOfCharacteristic(new int[] {5, 6}))
                .parents(createListOfCategory(new int[] {1, 2}))
                .children(createListOfCategory(new int[] {3, 4}))
                .build();
    }

    private List<Characteristic> createListOfCharacteristic(int[] ids) {
        List<Characteristic> characteristics = new ArrayList<>();
        IntStream.of(ids).forEach(id -> characteristics.add(Characteristic.builder()
                .id(id)
                .categories(new ArrayList<>())
                .build()));
        return characteristics;
    }

    private List<Category> createListOfCategory(int[] ids) {
        List<Category> categories = new ArrayList<>();
        IntStream.of(ids).forEach(id -> categories.add(Category.builder()
                .categoryId(id)
                .parents(new ArrayList<>())
                .children(new ArrayList<>())
                .build()));
        return categories;
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