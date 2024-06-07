package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.dto.incoming.GameDiscCreationDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.CreatedObjectDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.GameDiscResponseDto;
import edu.ubb.consolegamesales.backend.controller.exception.NotFoundException;
import edu.ubb.consolegamesales.backend.controller.mapper.GameDiscMapper;
import edu.ubb.consolegamesales.backend.model.GameDisc;
import edu.ubb.consolegamesales.backend.repository.GameDiscRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GameDiscService {
    private final GameDiscRepository gameDiscRepository;
    private final GameDiscMapper gameDiscMapper;
    private final RedisService redisService;

    public GameDiscResponseDto findGameDiscById(Long id) throws NotFoundException {
        try {
            GameDisc gameDisc = redisService.getCachedGameDisc(id);
            if (gameDisc != null) {
                return gameDiscMapper.modelToResponseDto(gameDisc);
            }
            gameDisc = gameDiscRepository.findByEntityId(id).orElseThrow(
                    () -> new NotFoundException("GameDisc with ID " + id + " not found"));
            redisService.storeGameDiscInCache(id, gameDisc);
            return gameDiscMapper.modelToResponseDto(gameDisc);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Game with ID " + id + " not found", e);
        }
    }

    public CreatedObjectDto createGameDisc(GameDiscCreationDto gameDiscCreationDto) {
        GameDisc gameDisc = gameDiscMapper.creationDtoToModel(gameDiscCreationDto);
        gameDiscRepository.saveAndFlush(gameDisc);
        return gameDiscMapper.modelToCreatedObjDto(gameDisc);
    }

}
