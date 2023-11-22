package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.responses.UserResponse;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.UserRepository;
import com.vvnuts.shop.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void uploadImage(MultipartFile file, Integer userId) throws IOException {
        if (file.isEmpty()){
            return; //TODO throw
        }
        User user = findById(userId);
        user.setImage(ImageUtils.compressImage(file.getBytes()));
        userRepository.save(user);
    }

    public byte[] downloadImage(Integer userId) {
        User user = findById(userId);
        return ImageUtils.decompressImage(user.getImage());
    }

    public UserResponse convertEntityToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
