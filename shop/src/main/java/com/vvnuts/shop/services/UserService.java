package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.entities.enums.Role;
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

    public void setRole(Integer id, Role role) {
        User user = findById(id);
        user.setRole(role);
        repository.save(user);
    }

    public void uploadImage(MultipartFile file, Integer userId) throws IOException {
        User user = findById(userId);
        user.setImage(ImageUtils.compressImage(file.getBytes()));
        repository.save(user);
    }

    public byte[] downloadImage(Integer userId) {
        User user = findById(userId);
        return ImageUtils.decompressImage(user.getImage());
    }

    public void deleteImage(Integer userId) {
        User user = findById(userId);
        user.setImage(null);
        repository.save(user);
    }
}
