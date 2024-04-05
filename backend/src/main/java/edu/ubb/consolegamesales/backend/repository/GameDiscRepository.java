package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.GameDisc;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GameDiscRepository extends Repository<GameDisc, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE GameDisc "
            + "SET name= :#{#entity.name}, type= :#{#entity.type}, sold = :#{#entity.sold}, "
            + "gameYear = :#{#entity.gameYear} WHERE entityId = :id ")
    @Override
    Long update(Long id, GameDisc entity);
}
