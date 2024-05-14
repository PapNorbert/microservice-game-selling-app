package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.*;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.exception.NotUpdatedException;
import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementMapper;
import edu.ubb.consolegamesales.backend.controller.mapper.GameDiscMapper;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.model.GameDiscType;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import edu.ubb.consolegamesales.backend.repository.jpa.AnnouncementSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/announcements")
@AllArgsConstructor
public class AnnouncementController {
    private final AnnouncementRepository announcementRepository;
    private final GameDiscRepository gameDiscRepository;
    private final AnnouncementMapper announcementMapper;
    private final GameDiscMapper gameDiscMapper;
    private final AnnouncementsSavesRepository announcementsSavesRepository;


    @GetMapping()
    public AnnouncementsListWithPaginationDto findPaginated(
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "ALL", required = false) String consoleType,
            @RequestParam(required = false) String transportPaid,
            @RequestParam(required = false) String productType,
            @RequestParam(defaultValue = "0", required = false) double priceMin,
            @RequestParam(defaultValue = "" + Double.MAX_VALUE, required = false) double priceMax,
            @RequestParam(required = false) @Positive Long savedByUserWithId,
            @RequestParam(required = false) String datePosted,
            @RequestParam(required = false) @Positive Long sellerId,
            @RequestParam(defaultValue = "false", required = false) boolean sold,
            Authentication authentication
    ) {

        LOGGER.info("GET paginated announcements at announcements api, "
                        + "page: {}, limit: {}, productName: {}, consoleType: {}, "
                        + "transportPaid: {}, productType: {}, priceMin: {}, priceMax: {}, "
                        + "savedByUserWithId: {}, datePosted: {}, sellerId: {} "
                        + "sold: {}",
                page, limit, productName, consoleType, transportPaid, productType,
                priceMin, priceMax, savedByUserWithId, datePosted, sellerId, sold);
        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("creationDate").descending());
        // pageNumber is 0 indexed
        if (savedByUserWithId != null) {
            // if it's a search for the saved sale announcements the user must be authenticated
            if (authentication == null || !authentication.isAuthenticated()
                    || authentication.getPrincipal() == null) {
                throw new AccessDeniedException("You must Login first to access this resource");
            }
        }
        Specification<Announcement> specification = createSpecificationFindAll(
                productName, consoleType, transportPaid, productType, priceMin,
                priceMax, savedByUserWithId, datePosted, sellerId, sold);
        Page<Announcement> announcementPage = announcementRepository.findAll(specification, pageRequest);
        List<AnnouncementListShortDto> announcementListShortDtos =
                announcementMapper.modelsToListShortDto(announcementPage.getContent());
        Pagination pagination = new Pagination(page, limit,
                announcementPage.getTotalElements(), announcementPage.getTotalPages());
        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() != null) {
            // if user is logged in set savedByUser
            User user = (User) authentication.getPrincipal();
            for (AnnouncementListShortDto announcementListShortDto : announcementListShortDtos) {
                announcementListShortDto.setSavedByUser(
                        announcementsSavesRepository.existsByAnnouncementEntityIdAndUserEntityId(
                                announcementListShortDto.getAnnouncementId(), user.getEntityId()
                        )
                );
            }
        }
        return new AnnouncementsListWithPaginationDto(announcementListShortDtos, pagination);
    }


    @GetMapping("/{id}")
    public AnnouncementDetailedDto findById(@PathVariable("id") Long id,
                                            Authentication authentication) throws NotFoundException {
        LOGGER.info("GET announcements at announcements/{} api", id);
        try {
            AnnouncementDetailedDto announcementDetailedDto = announcementMapper.modelToDetailedDto(
                    announcementRepository.getById(id));
            if (announcementDetailedDto == null) {
                throw new NotFoundException("Announcement with ID " + id + " not found");
            } else {
                boolean savedByUser = false;
                if (authentication != null && authentication.isAuthenticated()
                        && authentication.getPrincipal() != null) {
                    User user = (User) authentication.getPrincipal();
                    savedByUser = announcementsSavesRepository
                            .existsByAnnouncementEntityIdAndUserEntityId(
                                    id, user.getEntityId()
                            );
                }
                announcementDetailedDto.setSavedByUser(savedByUser);
                return announcementDetailedDto;
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Announcement with ID " + id + " not found", e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid AnnouncementCreationDto announcementCreationDto) {
        LOGGER.info("POST request at announcements api");
        GameDisc creationGameDisc = gameDiscMapper.creationDtoToModel(announcementCreationDto.getSoldGameDisc());
        GameDisc savedGameDisc = gameDiscRepository.saveAndFlush(creationGameDisc);
        Announcement announcement = announcementMapper.creationDtoToModel(announcementCreationDto);
        announcement.setSoldGameDisc(savedGameDisc);
        announcement.setSold(false);
        announcement.setCreationDate(new Date());
        Announcement savedAnnouncement = announcementRepository.saveAndFlush(announcement);
        return announcementMapper.modelToCreatedObjDto(savedAnnouncement);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody @Valid AnnouncementUpdateDto announcementUpdateDto)
            throws NotFoundException, NotUpdatedException {
        LOGGER.info("PUT request at announcements/{} api", id);
        try {
            if (announcementRepository.update(id,
                    announcementMapper.updateDtoToModel(announcementUpdateDto)) <= 0) {
                throw new NotUpdatedException("No update done for announcement with ID " + id);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Announcement with ID " + id + " not found", e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("DELETE announcements request at announcements/{} api", id);
        try {
            Announcement announcement = announcementRepository.getById(id);
            if (announcement != null) {
                announcementsSavesRepository.deleteByAnnouncementEntityId(announcement.getEntityId());
                announcementRepository.delete(announcement);
                gameDiscRepository.delete(announcement.getSoldGameDisc());
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Announcement with ID " + id + " not found", e);
        }
    }

    private Specification<Announcement> createSpecificationFindAll(
            String productName, String consoleType, String transportPaid, String productType,
            double priceMin, double priceMax, Long savedByUserWithId, String datePosted,
            Long sellerId, boolean sold) {

        // create a specification based on the request parameters
        Specification<Announcement> specification = Specification.where(AnnouncementSpecifications.isSold(sold));
        if (savedByUserWithId != null) {
            specification = specification.and(AnnouncementSpecifications.isSavedByUserWithId(savedByUserWithId));
        }
        if (productName != null && !productName.isEmpty()) {
            specification = specification.and(AnnouncementSpecifications.discNameContains(productName));
        }
        if (consoleType != null && !consoleType.isEmpty()) {
            if (consoleType.equals(GameDiscType.PS.name()) || consoleType.equals(GameDiscType.XBOX.name())
                    || consoleType.equals(GameDiscType.SWITCH.name())) {
                specification = specification.and(AnnouncementSpecifications.hasConsoleType(consoleType));
            }
        }
        if (transportPaid != null && !transportPaid.isEmpty()) {
            if (transportPaid.equals("SELLER")) {
                specification = specification.and(AnnouncementSpecifications.isTransportPaidBySeller(true));
            }
            if (transportPaid.equals("BUYER")) {
                specification = specification.and(AnnouncementSpecifications.isTransportPaidBySeller(false));
            }
        }
        if (productType != null && !productType.isEmpty()) {
            if (productType.equals("NEW")) {
                specification = specification.and(AnnouncementSpecifications.isNewDisc(true));
            }
            if (productType.equals("USED")) {
                specification = specification.and(AnnouncementSpecifications.isNewDisc(false));
            }
        }
        if (priceMin > 0) {
            specification = specification.and(AnnouncementSpecifications.priceGreaterOrEqualThen(priceMin));
        }
        if (priceMax >= 0 && priceMax != Double.MAX_VALUE) {
            specification = specification.and(AnnouncementSpecifications.priceLessOrEqualThen(priceMax));
        }
        if (sellerId != null) {
            specification = specification.and(AnnouncementSpecifications.isCreatedByUserWithId(sellerId));
        }
        if (datePosted != null && !datePosted.isEmpty()) {
            return addDatePostedSpecification(specification, datePosted);
        }

        return specification;
    }

    private Specification<Announcement> addDatePostedSpecification(
            Specification<Announcement> specification, String datePosted) {

        return switch (datePosted) {
            case "24H" -> {
                LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
                yield specification.and(AnnouncementSpecifications.createdAfter(twentyFourHoursAgo));
            }
            case "WEEK" -> {
                LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
                yield specification.and(AnnouncementSpecifications.createdAfter(oneWeekAgo));
            }
            case "MONTH" -> {
                LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
                yield specification.and(AnnouncementSpecifications.createdAfter(oneMonthAgo));
            }
            default -> specification;
        };

    }

}
