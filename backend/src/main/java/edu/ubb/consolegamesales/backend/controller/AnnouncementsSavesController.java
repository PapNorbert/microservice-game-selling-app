package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementsSavesCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementsSavesMapper;
import edu.ubb.consolegamesales.backend.model.AnnouncementsSaves;
import edu.ubb.consolegamesales.backend.repository.AnnouncementsSavesRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@PathVariable("id") @PositiveOrZero Long id) {
        LOGGER.info("DELETE request at announcementsSaves/{} api", id);
        announcementsSavesRepository.deleteById(id);
    }
}
