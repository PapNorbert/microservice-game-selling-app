package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("jpa")
public interface GameDiscJpaRepository extends GameDiscRepository, JpaRepository<GameDisc, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE GameDisc "
            + "SET name= :#{#entity.name}, type= :#{#entity.type}, "
            + "gameYear = :#{#entity.gameYear} WHERE entityId = :id ")
    @Override
    Integer update(Long id, GameDisc entity);

    @Override
    Optional<GameDisc> findByEntityId(Long id);
}
