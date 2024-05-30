package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementDetailedDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementsListWithPaginationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.service.AnnouncementService;
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
@RequestMapping("${apiPrefix}/announcements")
@AllArgsConstructor
public class AnnouncementController {
    private final AnnouncementService announcementService;


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
        return announcementService.findAnnouncementsPaginated(page, limit, productName,
                consoleType, transportPaid, productType, priceMin, priceMax,
                savedByUserWithId, datePosted, sellerId, sold, authentication);
    }


    @GetMapping("/{id}")
    public AnnouncementDetailedDto findById(@PathVariable("id") Long id,
                                            Authentication authentication) {
        LOGGER.info("GET announcements at announcements/{} api", id);
        return announcementService.findAnnouncementById(id, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid AnnouncementCreationDto announcementCreationDto) {
        LOGGER.info("POST request at announcements api");
        return announcementService.create(announcementCreationDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody @Valid AnnouncementUpdateDto announcementUpdateDto,
                       Authentication authentication) {
        LOGGER.info("PUT request at announcements/{} api", id);
        announcementService.update(id, announcementUpdateDto, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id, Authentication authentication) {
        LOGGER.info("DELETE announcements request at announcements/{} api", id);
        announcementService.delete(id, authentication);
    }

}
