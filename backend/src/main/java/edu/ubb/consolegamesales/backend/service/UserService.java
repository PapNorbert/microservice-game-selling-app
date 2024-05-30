package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.model.User;
import edu.ubb.consolegamesales.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
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

}
