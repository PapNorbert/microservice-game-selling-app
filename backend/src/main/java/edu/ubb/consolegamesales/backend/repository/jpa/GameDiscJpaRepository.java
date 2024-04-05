package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface GameDiscJpaRepository extends GameDiscRepository, JpaRepository<GameDisc, Long> {
}
