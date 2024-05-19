package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            + "lastName= :#{#entity.lastName}, refreshToken= :#{#entity.refreshToken} WHERE entityId = :id ")
    @Override
    Integer update(Long id, User entity);

    Optional<User> findByUsername(String username);

    User findByRefreshToken(String refreshToken);


    @Query("SELECT DISTINCT u "
            + "FROM User u "
            + "JOIN Message m ON u = m.sender OR u = m.receiver "
            + "WHERE (m.sender.entityId = :userId OR m.receiver.entityId = :userId) "
            + "AND u.entityId <> :userId")
    Page<User> findUsersChattedWith(Long userId, Pageable pageable);

    @Query("SELECT u.address FROM User u WHERE u.entityId = :userId")
    String findAddressByUserId(Long userId);

}
