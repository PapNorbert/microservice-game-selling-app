package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("jpa")
public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE User "
            + "SET username= :#{#entity.username}, firstName= :#{#entity.firstName}, password= :#{#entity.password}, "
            + "lastName= :#{#entity.lastName} WHERE entityId = :id ")
    @Override
    Integer update(Long id, User entity);

    Optional<User> findByUsername(String username);

    @Override
    Optional<User> findByEntityId(Long id);

    @Query("SELECT u.address FROM User u WHERE u.entityId = :userId")
    Optional<String> findAddressByUserId(Long userId);

}
