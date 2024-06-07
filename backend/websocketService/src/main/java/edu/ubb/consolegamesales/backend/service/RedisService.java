package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, User> redisTemplateUser;
    private static final String KEY_PREFIX_USER = "users:";

    public User getCachedUser(Long userId) {
        User user = null;
        try {
            user = redisTemplateUser.opsForValue().get(KEY_PREFIX_USER + userId);
        } catch (Exception e) {
            LOGGER.error("Error accessing user in Redis cache: {}", e.getMessage());
        }
        return user;
    }

}
