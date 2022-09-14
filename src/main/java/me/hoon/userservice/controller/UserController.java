package me.hoon.userservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoon.userservice.domain.dto.UserDto;
import me.hoon.userservice.domain.vo.ResponseUser;
import me.hoon.userservice.service.UserService;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final Environment env;
    private final UserService userService;


    @GetMapping("/health_check")
    public String status() {
        return "It's Working in User Service";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return env.getProperty("greeting.message");
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserDto userDto,
                                     BindingResult bindingResult) {

        log.info("userDto : {}", userDto);

        if(bindingResult.hasErrors()) {
           return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        ResponseUser responseUser = userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
