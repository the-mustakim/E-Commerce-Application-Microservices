package com.ecommerce.user.services;

import com.ecommerce.user.dto.AddressDto;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.exception.NotFoundException;
import com.ecommerce.user.models.Address;
import com.ecommerce.user.models.User;
import com.ecommerce.user.repository.UserRepository;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepo;


    public UserServiceImpl(UserRepository userRepo, LoggersEndpoint loggersEndpoint){
        this.userRepo = userRepo;
    }

    @Override
    public UserResponse addUser(UserRequest userRequest) {
        return mapToUserResponse(userRepo.save(mapToUser(userRequest)));
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> userList = userRepo.findAll();
        return userList.stream().map(UserServiceImpl::mapToUserResponse).toList();
    }

    @Override
    public UserResponse getUser(String userId) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isPresent()){return mapToUserResponse(user.get());}
        throw new NotFoundException("No user present with the given ID: " + userId);
    }

    @Override
    public UserResponse updateUser(String userId, UserRequest userRequest) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){throw new NotFoundException("No user present with the given ID: " + userId);}
        user.get().setFirstName(userRequest.getFirstName());
        user.get().setLastName(userRequest.getLastName());
        userRepo.save(user.get());
        return new UserResponse(String.valueOf(user.get().getId()), user.get().getFirstName(),user.get().getLastName(),user.get().getEmail(),user.get().getPhone(),user.get().getUserRole());
    }

    public static User mapToUser(UserRequest userRequest){
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setUserRole(userRequest.getUserRole());
        user.setAddress(mapToAddress(userRequest.getAddress()));
        return user;
    }

    public static UserResponse mapToUserResponse(User user){
        return new UserResponse(String.valueOf(user.getId()), user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone(),user.getUserRole(),mapToAddressDto(user.getAddress()));
    }

    public static AddressDto mapToAddressDto(Address address){
        return new AddressDto(address.getState(),address.getCity(),address.getState(),address.getCountry(),address.getZipcode());
    }

    public static Address mapToAddress(AddressDto addressDto){
        return new Address(addressDto.getState(),addressDto.getCity(),addressDto.getState(),addressDto.getCountry(),addressDto.getZipcode());
    }


}
