package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEntityId(Long id);

    Optional<String> findAddressByUserId(Long userId);
}
