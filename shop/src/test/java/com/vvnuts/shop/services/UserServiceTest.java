package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.entities.enums.Role;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
import com.vvnuts.shop.repositories.UserRepository;
import com.vvnuts.shop.utils.ImageUtils;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService underTest;

    private static final int USER_ID = 1;

    @Test
    void userService_findById_returnUser() {
        //given
        when(repository.findById(USER_ID)).thenReturn(Optional.of(getUser()));

        //when
        User result = underTest.findById(USER_ID);

        //then
        Mockito.verify(repository, times(1)).findById(USER_ID);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void userService_findById_throwException() {
        //given
        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(USER_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void userService_delete_canDelete() {
        //given
        User deletedUser = getUser();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(deletedUser));

        //when
        underTest.delete(USER_ID);

        //then
        Mockito.verify(repository, times(1)).findById(USER_ID);
        Mockito.verify(repository, times(1)).delete(deletedUser);
    }

    @Test
    void userService_delete_throwException() {
        //given
        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.delete(USER_ID))
                        .isInstanceOf(NoSuchElementException.class);
        Mockito.verify(repository, never()).delete(any());
    }

    @Test
    void userService_setRole_returnUserWithOtherRole() {
        //given
        User old = getUser();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(getUser()));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        User upd = underTest.setRole(USER_ID, Role.ROLE_ADMIN);

        //then
        Assertions.assertThat(upd.getRole()).isNotEqualTo(old.getRole());
        Assertions.assertThat(upd.getRole()).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    void userService_setRole_returnUserWithSameRole() {
        //given
        User old = getUser();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(getUser()));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        User upd = underTest.setRole(USER_ID, Role.ROLE_USER);

        //then
        Assertions.assertThat(upd.getRole()).isEqualTo(old.getRole());
    }

    @Test
    void userService_uploadImage_returnUserWithImage() throws IOException {
        //given
        MultipartFile file = getFile();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(getUser()));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when

        User upd = underTest.uploadImage(file, USER_ID);

        //then
        Assertions.assertThat(upd.getImage()).isNotEmpty();
        Assertions.assertThat(upd.getImage()).isEqualTo(ImageUtils.compressImage(file.getBytes()));
    }

    @Test
    void userService_uploadImage_throwFileIsEmptyException() {
        //given
        MultipartFile file = getEmptyFile();

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.uploadImage(file, USER_ID))
                .isInstanceOf(FileIsEmptyException.class);
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void userService_downloadImage_returnImage() throws IOException {
        //given
        MultipartFile file = getFile();
        User user = getUser();
        user.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        byte[] data = underTest.downloadImage(USER_ID);

        //then
        Assertions.assertThat(data).isNotEmpty();
        Assertions.assertThat(data).isEqualTo(file.getBytes());
    }

    @Test
    void userService_downloadImage_returnNull() {
        //given
        User user = getUser();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        byte[] data = underTest.downloadImage(USER_ID);

        //then
        Assertions.assertThat(data).isNull();
    }

    @Test
    void userService_deleteImage_returnUserWithoutImage() throws IOException {
        //given
        MultipartFile file = getFile();
        User user = getUser();
        user.setImage(ImageUtils.compressImage(file.getBytes()));
        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        User userWithoutImage = underTest.deleteImage(USER_ID);

        //then
        Mockito.verify(repository, times(1)).save(user);
        Assertions.assertThat(userWithoutImage.getImage()).isNull();
    }

    @Test
    void userService_deleteImage_throwImageIsAlreadyEmpty() {
        //given
        User user = getUser();
        when(repository.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.deleteImage(USER_ID))
                        .isInstanceOf(ImageIsAlreadyNull.class);
        Mockito.verify(repository, never()).save(user);
    }

    private User getUser() {
        return User.builder()
                .role(Role.ROLE_USER)
                .userId(USER_ID)
                .email("tim@mail.ru")
                .firstname("Tim")
                .lastname("Cook")
                .build();
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