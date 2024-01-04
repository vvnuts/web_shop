package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.entities.enums.Role;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.exceptions.ImageIsAlreadyNull;
import com.vvnuts.shop.repositories.UserRepository;
import com.vvnuts.shop.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    public void delete(Integer id) {
        User user = findById(id);
        repository.delete(user);
    }

    public User setRole(Integer id, Role role) {
        User user = findById(id);
        user.setRole(role);
        return repository.save(user);
    }

    public User uploadImage(MultipartFile file, Integer userId) throws IOException {
        if (file.isEmpty()){
            throw new FileIsEmptyException("Файл пуст.");
        }
        User user = findById(userId);
        user.setImage(ImageUtils.compressImage(file.getBytes()));
        return repository.save(user);
    }

    public byte[] downloadImage(Integer userId) {
        User user = findById(userId);
        if (user.getImage() == null) {
            return null;
        }
        return ImageUtils.decompressImage(user.getImage());
    }

    public User deleteImage(Integer userId) {
        User user = findById(userId);
        if (user.getImage() == null) {
            throw new ImageIsAlreadyNull("Изображение и так уже пустое!");
        }
        user.setImage(null);
        return repository.save(user);
    }
}
