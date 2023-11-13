package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.UserDTO;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.UserRepository;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation extends AbstractCrudService<User, UserDTO, Integer> implements UserService {
    private final UserRepository userRepository;

    @Override
    JpaRepository<User, Integer> getRepository() {
        return userRepository;
    }

    @Override
    User transferToUpdateEntity(UserDTO dto, User updateEntity) {
        return null;
    }

    @Override
    User transferToCreateEntity(UserDTO dto) {
        return null;
    }
}
