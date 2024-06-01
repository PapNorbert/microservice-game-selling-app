package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.exception.NotUpdatedException;
import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementMapper;
import edu.ubb.consolegamesales.backend.controller.mapper.GameDiscMapper;
import edu.ubb.consolegamesales.backend.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.*;
import edu.ubb.consolegamesales.backend.model.*;
import edu.ubb.consolegamesales.backend.repository.AnnouncementEventRepository;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import edu.ubb.consolegamesales.backend.repository.jpa.AnnouncementSpecifications;
import edu.ubb.consolegamesales.backend.service.security.AuthenticationInformation;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final GameDiscRepository gameDiscRepository;
    private final AnnouncementMapper announcementMapper;
    private final GameDiscMapper gameDiscMapper;
    private final AnnouncementsSavesRepository announcementsSavesRepository;
    private final RedisService redisService;
    private final AnnouncementEventRepository announcementEventRepository;


    public AnnouncementsListWithPaginationDto findAnnouncementsPaginated(
            int page, int limit, String productName, String consoleType,
            String transportPaid, String productType, double priceMin,
            double priceMax, Long savedByUserWithId, String datePosted,
            Long sellerId, boolean sold,
            Authentication authentication
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
                Sort.by("creationDate").descending());
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
                        announcementsSavesRepository.existsByAnnouncementEntityIdAndUserId(
                                announcementListShortDto.getAnnouncementId(), user.getEntityId()
                        )
                );
            }
        }
        return new AnnouncementsListWithPaginationDto(announcementListShortDtos, pagination);
    }


    public AnnouncementDetailedDto findAnnouncementById(Long id, Authentication authentication)
            throws NotFoundException {
        try {
            Announcement announcement = redisService.getCachedAnnouncement(id);
            if (announcement == null) {
                announcement = announcementRepository.findByEntityId(id).orElseThrow(
                        () -> new NotFoundException("Announcement with ID " + id + " not found"));
                redisService.storeAnnouncementInCache(announcement.getEntityId(), announcement);
            }
            AnnouncementDetailedDto announcementDetailedDto = announcementMapper.modelToDetailedDto(
                    announcement);
            if (announcementDetailedDto == null) {
                throw new NotFoundException("Announcement with ID " + id + " not found");
            } else {
                boolean savedByUser = false;
                if (authentication != null && authentication.isAuthenticated()
                        && authentication.getPrincipal() != null) {
                    User user = (User) authentication.getPrincipal();
                    savedByUser = announcementsSavesRepository
                            .existsByAnnouncementEntityIdAndUserId(
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

    public CreatedObjectDto create(AnnouncementCreationDto announcementCreationDto) {
        GameDisc creationGameDisc = gameDiscMapper.creationDtoToModel(announcementCreationDto.getSoldGameDisc());
        GameDisc savedGameDisc = gameDiscRepository.saveAndFlush(creationGameDisc);
        redisService.storeGameDiscInCache(savedGameDisc.getEntityId(), savedGameDisc);
        Announcement announcement = announcementMapper.creationDtoToModel(announcementCreationDto);
        announcement.setSoldGameDisc(savedGameDisc);
        announcement.setSold(false);
        announcement.setCreationDate(new Date());
        Announcement savedAnnouncement = announcementRepository.saveAndFlush(announcement);
        redisService.storeAnnouncementInCache(savedAnnouncement.getEntityId(), savedAnnouncement);
        AnnouncementEvent announcementEvent = new AnnouncementEvent(
                announcement, "Announcement created", new Date(), announcement.getSellerId());
        announcementEventRepository.saveAndFlush(announcementEvent);
        return announcementMapper.modelToCreatedObjDto(savedAnnouncement);
    }

    public void update(Long id, AnnouncementUpdateDto announcementUpdateDto,
                       Authentication authentication)
            throws NotFoundException, NotUpdatedException {
        try {

            if (announcementRepository.update(id,
                    announcementMapper.updateDtoToModel(announcementUpdateDto)) <= 0) {
                throw new NotUpdatedException("No update done for announcement with ID " + id);
            }
            // update cache
            redisService.deleteCachedAnnouncement(id);
            // save event
            Announcement announcement = new Announcement();
            announcement.setEntityId(id);
            User user = AuthenticationInformation.extractUser(authentication);
            AnnouncementEvent announcementEvent = new AnnouncementEvent(
                    announcement, "Announcement data updated", new Date(), user.getEntityId());
            announcementEventRepository.saveAndFlush(announcementEvent);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Announcement with ID " + id + " not found", e);
        }
    }


    public void delete(Long id, Authentication authentication) throws NotFoundException {
        try {
            Announcement announcement = announcementRepository.getById(id);
            if (announcement != null) {
                User user = AuthenticationInformation.extractUser(authentication);
                if (!Objects.equals(user.getEntityId(), announcement.getSellerId())) {
                    throw new AccessDeniedException("You cannot delete announcement of another user!");
                }
                // save announcement event
                AnnouncementEvent announcementEvent = new AnnouncementEvent(
                        announcement, "Announcement deleted", new Date(), user.getEntityId());
                announcementEventRepository.saveAndFlush(announcementEvent);

                // delete resources
                announcementsSavesRepository.deleteByAnnouncementEntityId(announcement.getEntityId());
                announcementRepository.delete(announcement);
                redisService.deleteCachedAnnouncement(announcement.getEntityId());
                gameDiscRepository.delete(announcement.getSoldGameDisc());
                redisService.deleteCachedGameDisc(announcement.getSoldGameDisc().getEntityId());
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
