package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementsSavesCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementsSavesMapper;
import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/announcementsSaves")
@AllArgsConstructor
public class AnnouncementsSavesController {
    private final AnnouncementsSavesRepository announcementsSavesRepository;
    private final AnnouncementsSavesMapper announcementsSavesMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid AnnouncementsSavesCreationDto announcementCreationDto) {
        LOGGER.info("POST request at announcementsSaves api");
        AnnouncementsSaves savedAnnouncementsSaves =
                announcementsSavesRepository.saveAndFlush(
                        announcementsSavesMapper.creationDtoToModel(announcementCreationDto));
        return announcementsSavesMapper.modelToCreatedObjDto(savedAnnouncementsSaves);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam @Positive Long announcementId,
                       @RequestParam @Positive Long userId, Authentication authentication) {
        LOGGER.info("DELETE request at announcementsSaves api, announcementId: {}, userId: {}",
                announcementId, userId);
        User user = (User) authentication.getPrincipal();
        // user is logged in, it's required, configured by SecurityConfig
        if (!user.getEntityId().equals(userId)) {
            LOGGER.warn("WARN: User with id {} and username {} tried to acces resource of user with id {}",
                    user.getEntityId(), user.getUsername(), userId);
            throw new AccessDeniedException("You do not have permission to delete this resource");
        }
        announcementsSavesRepository.deleteByAnnouncementEntityIdAndUserEntityId(announcementId, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@PathVariable("id") @PositiveOrZero Long id) {
        LOGGER.info("DELETE request at announcementsSaves/{} api", id);
        announcementsSavesRepository.deleteById(id);
    }
}
