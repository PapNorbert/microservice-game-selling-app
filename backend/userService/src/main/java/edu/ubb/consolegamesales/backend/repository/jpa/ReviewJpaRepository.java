package edu.ubb.consolegamesales.backend.repository.jpa;

import edu.ubb.consolegamesales.backend.model.Review;
import edu.ubb.consolegamesales.backend.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface ReviewJpaRepository extends ReviewRepository, JpaRepository<Review, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Review "
            + "SET reviewText = :#{#entity.reviewText} "
            + " WHERE entityId = :id ")
    @Override
    Integer update(Long id, Review entity);

    Page<Review> findAllBySellerEntityId(Long sellerId, Pageable pageable);
}
