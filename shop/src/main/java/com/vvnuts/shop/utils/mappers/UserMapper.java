package com.vvnuts.shop.utils.mappers;

import com.vvnuts.shop.dtos.responses.UserResponse;
import com.vvnuts.shop.entities.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserResponse convertEntityToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
}
