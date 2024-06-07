package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, User> redisTemplateUser;
    private final RedisTemplate<String, String> redisTemplateString;
    private static final String KEY_PREFIX_USER = "users:";
    private static final String KEY_POSTFIX_USER_ADDRESS = ":address";


    public void storeUserInCache(Long userId, User user) {
        try {
            String key = KEY_PREFIX_USER + userId;
            redisTemplateUser.opsForValue().set(key, user);
            // set expiration date of 2 hours
            redisTemplateUser.expire(key, 2, TimeUnit.HOURS);
            LOGGER.info("User stored in Redis cache with key: {}", key);
        } catch (Exception e) {
            LOGGER.error("Error storing data in Redis cache: ", e);
        }
    }

    public User getCachedUser(Long userId) {
        User user = null;
        try {
            user = redisTemplateUser.opsForValue().get(KEY_PREFIX_USER + userId);
        } catch (Exception e) {
            LOGGER.error("Error accessing user in Redis cache: {}", e.getMessage());
        }
        return user;
    }

    public void storeUserAddressInCache(Long userId, String address) {
        try {
            String key = KEY_PREFIX_USER + userId + KEY_POSTFIX_USER_ADDRESS;
            redisTemplateString.opsForValue().set(key, address);
            // set expiration date of 2 hours
            redisTemplateUser.expire(key, 2, TimeUnit.HOURS);
            LOGGER.info("User address stored in Redis cache with key: {}", key);
        } catch (Exception e) {
            LOGGER.error("Error storing data in Redis cache: ", e);
        }
    }

    public String getCachedUserAddress(Long userId) {
        String address = null;
        try {
            address = redisTemplateString.opsForValue().get(KEY_PREFIX_USER + userId + KEY_POSTFIX_USER_ADDRESS);
        } catch (Exception e) {
            LOGGER.error("Error accessing user address Redis cache: {}", e.getMessage());
        }
        return address;
    }

}
