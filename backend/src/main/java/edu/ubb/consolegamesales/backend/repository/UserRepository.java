package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEntityId(Long id);

    Page<User> findUsersChattedWith(Long userId, Pageable pageable);

    Optional<String> findAddressByUserId(Long userId);
}
