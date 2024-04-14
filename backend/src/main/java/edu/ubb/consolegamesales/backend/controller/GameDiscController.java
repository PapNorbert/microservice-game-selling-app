package edu.ubb.consolegamesales.backend.controller;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.GameDiscCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.GameDiscResponseDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.GameDiscMapper;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/games")
public class GameDiscController {
    private final GameDiscRepository gameDiscRepository;
    private final GameDiscMapper gameDiscMapper;

    public GameDiscController(GameDiscRepository gameDiscRepository, GameDiscMapper gameDiscMapper) {
        this.gameDiscRepository = gameDiscRepository;
        this.gameDiscMapper = gameDiscMapper;
    }

    @GetMapping("/{id}")
    public GameDiscResponseDto findById(@PathVariable("id") Long id) throws NotFoundException {
        LOGGER.info("GET games at /games/" + id);
        try {
            GameDiscResponseDto gameDiscResponseDto = gameDiscMapper.modelToResponseDto(gameDiscRepository.getById(id));
            if (gameDiscResponseDto == null) {
                throw new NotFoundException("Game with ID " + id + " not found");
            } else {
                return gameDiscResponseDto;
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Game with ID " + id + " not found", e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid GameDiscCreationDto gameDiscCreationDto) {
        LOGGER.info("POST request at /games");
        GameDisc gameDisc = gameDiscMapper.creationDtoToModel(gameDiscCreationDto);
        gameDisc.setSold(false);
        gameDiscRepository.saveAndFlush(gameDisc);
        return gameDiscMapper.modelToCreatedObjDto(gameDisc);
    }


}
