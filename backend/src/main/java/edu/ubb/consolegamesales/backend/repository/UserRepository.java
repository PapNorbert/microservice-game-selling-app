package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends Repository<User, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE User "
            + "SET username= :#{#entity.username}, firstName= :#{#entity.firstName}, password= :#{#entity.password}, "
            + "lastName= :#{#entity.lastName} WHERE entityId = :id ")
    @Override
    Long update(Long id, User entity);
}
