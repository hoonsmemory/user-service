package me.hoon.userservice.service;

import me.hoon.userservice.domain.dto.UserRequestDto;
import me.hoon.userservice.domain.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserByUserId(String userId);
    List<UserResponseDto> getUserByAll();

}
