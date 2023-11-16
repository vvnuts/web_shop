package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.CreateUserRequest;
import com.vvnuts.shop.dtos.responses.UserResponse;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserResponse convertEntityToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
