package edu.ubb.consolegamesales.backend.controller.mapper;

import edu.ubb.consolegamesales.backend.dto.incoming.ReviewCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.ReviewResponseDto;
import edu.ubb.consolegamesales.backend.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReviewMapper {
    CreatedObjectDto modelToCreatedObjDto(Review review);

    @Mapping(target = "entityId", ignore = true)
    @Mapping(target = "reviewer.entityId", source = "reviewerId")
    @Mapping(target = "seller.entityId", source = "sellerId")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    Review creationDtoToModel(ReviewCreationDto reviewCreationDto);

    @Mapping(target = "reviewId", source = "entityId")
    ReviewResponseDto modelToResponseDto(Review review);

    @Mapping(target = "reviewId", source = "entityId")
    List<ReviewResponseDto> modelsToResponseDtos(List<Review> reviews);
}
