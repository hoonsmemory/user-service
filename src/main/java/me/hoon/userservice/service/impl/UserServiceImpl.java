package me.hoon.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoon.userservice.domain.User;
import me.hoon.userservice.domain.dto.OrderResponseDto;
import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.dto.UserRequestDto;
import me.hoon.userservice.domain.dto.UserResponseDto;
import me.hoon.userservice.repository.UserRepository;
import me.hoon.userservice.service.UserService;
import me.hoon.userservice.service.client.OrderServiceClient;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final Environment env;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = modelMapper.map(userRequestDto, User.class);
        user.setUserId(UUID.randomUUID().toString().substring(0, 10));
        user.setEncryptedPwd(passwordEncoder.encode(userRequestDto.getPwd()));

        userRepository.save(user);

        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public UserResponseDto getUserByUserId(String userId) throws UsernameNotFoundException{
        User user = userRepository.findByUserId(userId)
                                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);

        String orderUrl = String.format(env.getProperty("order_service.url"), userId);

        //url, httpMethod, params, return
        /*ResponseEntity<List<OrderResponseDto>> orderListResponse = restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OrderResponseDto>>() {

                }
        );

        List<OrderResponseDto> orderList = orderListResponse.getBody();*/

        //circuitBreaker 적용
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("orderByUserId");
        List<OrderResponseDto> orderList = circuitBreaker.run(() -> orderServiceClient.getOrderByUserId(userId),
                throwable -> new ArrayList<>());

        userResponseDto.setOrders(orderList);

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
