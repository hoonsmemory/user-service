package me.hoon.userservice.service;

import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.dto.UserRequestDto;
import me.hoon.userservice.domain.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserByUserId(String userId);
    List<UserResponseDto> getUserByAll();

    UserDto getUserDetailsByEmail(String username);
}
