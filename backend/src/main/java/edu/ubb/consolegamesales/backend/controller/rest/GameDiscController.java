package edu.ubb.consolegamesales.backend.controller.rest;

import edu.ubb.consolegamesales.backend.controller.dto.incoming.GameDiscCreationDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.controller.dto.outgoing.GameDiscResponseDto;
import edu.ubb.consolegamesales.backend.service.GameDiscService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Slf4j
@AllArgsConstructor
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping("${apiPrefix}/games")
public class GameDiscController {
    private final GameDiscService gameDiscService;

    @GetMapping("/{id}")
    public GameDiscResponseDto findById(@PathVariable("id") Long id) {
        LOGGER.info("GET game at games/{} api", id);
        return gameDiscService.findGameDiscById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedObjectDto create(@RequestBody @Valid GameDiscCreationDto gameDiscCreationDto) {
        LOGGER.info("POST request at games api");
        return gameDiscService.createGameDisc(gameDiscCreationDto);
    }


}
