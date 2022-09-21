package me.hoon.userservice.repository;

import me.hoon.userservice.domain.User;
import me.hoon.userservice.domain.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String email);
}
