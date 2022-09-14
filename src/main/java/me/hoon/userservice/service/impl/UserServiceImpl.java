package me.hoon.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import me.hoon.userservice.domain.User;
import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.vo.ResponseUser;
import me.hoon.userservice.repository.UserRepository;
import me.hoon.userservice.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseUser createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setUserId(UUID.randomUUID().toString());
        user.setEncryptedPwd("encrypted_password");

        userRepository.save(user);

        return modelMapper.map(user, ResponseUser.class);
    }
}
