package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.GameDisc;

import java.util.Optional;

public interface GameDiscRepository extends Repository<GameDisc, Long> {
    Optional<GameDisc> findByEntityId(Long id);

}
