package edu.ubb.consolegamesales.backend.controller;

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
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import edu.ubb.consolegamesales.backend.repository.jpa.AnnouncementSpecifications;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping()
    public AnnouncementsListWithPaginationDto findPaginated(
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "ALL", required = false) String consoleType,
            @RequestParam(required = false) String transportPaid,
            @RequestParam(required = false) String productType,
            @RequestParam(defaultValue = "0", required = false) double priceMin,
            @RequestParam(defaultValue = "" + Double.MAX_VALUE, required = false) double priceMax
    ) {

        LOGGER.info("GET paginated announcements at announcements api, "
                        + "page: {}, limit: {}, productName: {}, consoleType: {}, "
                        + "transportPaid: {}, productType: {}, priceMin: {}, priceMax: {}",
                page, limit, productName, consoleType, transportPaid, productType, priceMin, priceMax);
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        // pageNumber is 0 indexed
        Specification<Announcement> specification = createSpecificationFindAllNotSold(
                productName, consoleType, transportPaid, productType, priceMin, priceMax);
        Page<Announcement> announcementPage = announcementRepository.findAll(specification, pageRequest);
        List<AnnouncementListShortDto> announcementListShortDtos =
                announcementMapper.modelsToListShortDto(announcementPage.getContent());
        Pagination pagination = new Pagination(page, limit,
                announcementPage.getTotalElements(), announcementPage.getTotalPages());
        return new AnnouncementsListWithPaginationDto(announcementListShortDtos, pagination);
    }


    @GetMapping("/{id}")
    public AnnouncementDetailedDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET announcements at announcements/{} api", id);
        try {
            AnnouncementDetailedDto announcementDetailedDto = announcementMapper.modelToDetailedDto(
                    announcementRepository.getById(id));
            if (announcementDetailedDto == null) {
                throw new NotFoundException("Announcement with ID " + id + " not found");
            } else {
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
        announcementRepository.saveAndFlush(announcement);
        return announcementMapper.modelToCreatedObjDto(announcement);
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

    private Specification<Announcement> createSpecificationFindAllNotSold(
            String productName, String consoleType, String transportPaid, String productType,
            double priceMin, double priceMax) {

        // create a specification based on the request parameters
        Specification<Announcement> specification = Specification.where(AnnouncementSpecifications.isSold(false));
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

        return specification;
    }

}
