package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.dto.incoming.ReviewCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.ReviewResponseWithPaginationDto;
import edu.ubb.consolegamesales.backend.service.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ReviewResponseWithPaginationDto findPaginated(
            @RequestParam @Positive Long sellerId,
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit) {
        LOGGER.info("GET paginated reviews of user with id {} at reviews api, "
                + "page: {}, limit: {}", sellerId, page, limit);
        return reviewService.findAllReviewsOfUser(sellerId, page, limit);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid ReviewCreationDto reviewCreationDto,
                                   Authentication authentication) {
        LOGGER.info("POST request at reviews api");
        return reviewService.createReview(reviewCreationDto, authentication);
    }
}
