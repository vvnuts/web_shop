package com.vvnuts.shop.services.interfaces;

import com.vvnuts.shop.dtos.requests.CreateUserRequest;
import com.vvnuts.shop.entities.User;

public interface UserService  extends CrudService<User, CreateUserRequest, Integer>{
}
