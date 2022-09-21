package me.hoon.userservice.service.impl;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.dto.UserRequestDto;
import me.hoon.userservice.domain.dto.UserResponseDto;
import me.hoon.userservice.repository.UserRepository;
import me.hoon.userservice.service.UserService;
import me.hoon.userservice.domain.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setUserId(UUID.randomUUID().toString());
        user.setEncryptedPwd(passwordEncoder.encode(userRequestDto.getPwd()));

        userRepository.save(user);

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserByUserId(String userId) throws UsernameNotFoundException{
        User user = userRepository.findByUserId(userId)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);

        //List

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> getUserByAll() {
        List<UserResponseDto> result = new ArrayList<>();

        Iterable<User> all = userRepository.findAll();

        userRepository.findAll().forEach(x -> {
            result.add(modelMapper.map(x, UserResponseDto.class));
        });
        return result;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("There is no "+ email));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("There is no "+ email));

        return new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
}
