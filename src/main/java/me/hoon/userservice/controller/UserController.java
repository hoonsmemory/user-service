package me.hoon.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoon.userservice.domain.dto.UserRequestDto;
import me.hoon.userservice.domain.dto.UserResponseDto;
import me.hoon.userservice.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user-service")
@RestController
public class UserController {

    private final Environment env;
    private final UserService userService;


    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working in User Service on Port %s", request.getServerPort());
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity getUser(@PathVariable(value = "userId") String userId) {
        UserResponseDto responseUser = userService.getUserByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity getUsers() {

        List<UserResponseDto> userByAll = userService.getUserByAll();
        if(userByAll.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("User is empty");
        }

        return ResponseEntity.status(HttpStatus.OK).body(userByAll);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody @Validated UserRequestDto userRequestDto,
                                     BindingResult bindingResult) {

        log.info("userRequestDto : {}", userRequestDto);

        if(bindingResult.hasErrors()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        UserResponseDto userResponseDto = userService.createUser(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }
}
