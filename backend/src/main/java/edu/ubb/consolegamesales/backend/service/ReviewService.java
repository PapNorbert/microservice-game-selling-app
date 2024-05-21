package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.ReviewCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.ReviewResponseDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.ReviewResponseWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.mapper.ReviewMapper;
import edu.ubb.consolegamesales.backend.model.Review;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponseWithPaginationDto findAllReviewsOfUser(Long userId, int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
                Sort.by("creationDate").descending());
        Page<Review> reviewsPage = reviewRepository.findAllBySellerEntityId(userId, pageRequest);
        List<ReviewResponseDto> reviewResponseDtos = reviewMapper.modelsToResponseDtos(
                reviewsPage.getContent());
        Pagination pagination = new Pagination(page, limit,
                reviewsPage.getTotalElements(), reviewsPage.getTotalPages());
        return new ReviewResponseWithPaginationDto(reviewResponseDtos, pagination);
    }

    public CreatedObjectDto createReview(ReviewCreationDto reviewCreationDto,
                                         Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("You must Login first to access this resource!");
        }
        User user = (User) authentication.getPrincipal();
        if (!Objects.equals(user.getEntityId(), reviewCreationDto.getReviewerId())) {
            throw new AccessDeniedException("You cannot review in the name of another user!");
        }

        Review review = reviewRepository.saveAndFlush(
                reviewMapper.creationDtoToModel(reviewCreationDto));
        return reviewMapper.modelToCreatedObjDto(review);
    }
}
