package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByRefreshToken(String refreshToken);

    Page<User> findUsersChattedWith(Long userId, Pageable pageable);
}
