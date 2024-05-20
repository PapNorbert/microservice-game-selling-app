package edu.ubb.consolegamesales.backend.repository;

import edu.ubb.consolegamesales.backend.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepository extends Repository<Review, Long> {
    Page<Review> findAllBySellerEntityId(Long sellerId, Pageable pageable);
}
