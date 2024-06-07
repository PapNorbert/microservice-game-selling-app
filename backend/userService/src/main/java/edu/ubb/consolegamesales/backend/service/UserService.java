package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.controller.mapper.UserMapper;
import edu.ubb.consolegamesales.backend.dto.kafka.UserChattedWithDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.Pagination;
import edu.ubb.consolegamesales.backend.dto.outgoing.UserResponseDto;
import edu.ubb.consolegamesales.backend.dto.outgoing.UsersResponseWithPaginationDto;
import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RedisService redisService;

    public User loadUserByUserId(Long userId) {
        User user = redisService.getCachedUser(userId);
        if (user != null) {
            return user;
        }
        user = userRepository.findByEntityId(userId).orElse(null);
        redisService.storeUserInCache(userId, user);
        return user;
    }

    public String loadUserAddressByUserId(Long userId) {
        String address = redisService.getCachedUserAddress(userId);
        if (address != null) {
            return address;
        }
        address = userRepository.findAddressByUserId(userId).orElse(null);
        redisService.storeUserAddressInCache(userId, address);
        return address;
    }

    public UsersResponseWithPaginationDto loadUsersByUserChattedWith(UserChattedWithDto userChattedWithDto) {
        List<User> users = new ArrayList<>();
        for (Long currentUserId : userChattedWithDto.getUserIdsList()) {
            User user = loadUserByUserId(currentUserId);
            if (user != null) {
                users.add(user);
            }
        }
        List<UserResponseDto> userResponseDtos = userMapper.modelsToResponseDtos(users);
        Pagination pagination = new Pagination(
                userChattedWithDto.getPage(), userChattedWithDto.getLimit(),
                userChattedWithDto.getTotalElements(),
                userChattedWithDto.getTotalPages());
        return new UsersResponseWithPaginationDto(userResponseDtos, pagination);
    }

}
