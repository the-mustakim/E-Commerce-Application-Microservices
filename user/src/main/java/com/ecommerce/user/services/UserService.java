package com.ecommerce.user.services;

import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    public UserResponse addUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUser(String userId);

    UserResponse updateUser(String userId, UserRequest userRequest);
}
