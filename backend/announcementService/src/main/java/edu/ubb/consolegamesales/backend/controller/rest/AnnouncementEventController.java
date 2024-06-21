package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.dto.outgoing.AnnouncementEventsPaginated;
import edu.ubb.consolegamesales.backend.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.model.AnnouncementEvent;
import edu.ubb.consolegamesales.backend.repository.AnnouncementEventRepository;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/announcementEvents")
@AllArgsConstructor
public class AnnouncementEventController {
    private final AnnouncementEventRepository announcementEventRepository;

    @GetMapping()
    public AnnouncementEventsPaginated findPaginated(
            @RequestParam(defaultValue = "1", required = false) @Positive int page,
            @RequestParam(defaultValue = "5", required = false) @Positive int limit
    ) {
        LOGGER.info("GET paginated announcementEvents at announcementEvents api, "
                        + "page: {}, limit: {}",
                page, limit);
        PageRequest pageRequest = PageRequest.of(page - 1, limit,
                Sort.by("date").descending());
        Page<AnnouncementEvent> announcementEventPage = announcementEventRepository.findAll(pageRequest);
        Pagination pagination = new Pagination(page, limit,
                announcementEventPage.getTotalElements(), announcementEventPage.getTotalPages());
        return new AnnouncementEventsPaginated(announcementEventPage.getContent(), pagination);
    }
}
