package me.hoon.userservice.service;

import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.vo.ResponseUser;

public interface UserService {

    ResponseUser createUser(UserDto userDto);
}
