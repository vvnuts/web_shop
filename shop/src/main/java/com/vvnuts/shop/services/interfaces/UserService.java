package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CreateUserRequest;
import com.vvnuts.shop.dtos.responses.ItemResponse;
import com.vvnuts.shop.dtos.responses.UserResponse;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.User;

public interface UserService  extends CrudService<User, CreateUserRequest, Integer>{
    UserResponse convertEntityToResponse(User User);
}
