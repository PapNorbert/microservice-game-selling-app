package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.incoming.AnnouncementUpdateDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.AnnouncementDetailedDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.exception.NotUpdatedException;
import edu.ubb.consolegamesales.backend.controller.mapper.AnnouncementMapper;
import edu.ubb.consolegamesales.backend.controller.mapper.GameDiscMapper;
import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.repository.AnnouncementRepository;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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


    @GetMapping("/{id}")
    public AnnouncementDetailedDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET announcements at announcements/" + id + " api");
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
        LOGGER.info("PUT request at announcements/" + id + "api");
        try {
            if (announcementRepository.update(id,
                    announcementMapper.updateDtoToModel(announcementUpdateDto)) <= 0) {
                throw new NotUpdatedException("No update done for announcement with ID " + id);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Announcement with ID " + id + " not found", e);
        }
    }

}
