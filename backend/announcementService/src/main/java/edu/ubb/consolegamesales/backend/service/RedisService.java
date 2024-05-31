package edu.ubb.consolegamesales.backend.service;

import edu.ubb.consolegamesales.backend.model.Announcement;
import edu.ubb.consolegamesales.backend.model.GameDisc;
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
    private final RedisTemplate<String, GameDisc> redisTemplateGameDisc;
    private final RedisTemplate<String, Announcement> redisTemplateAnnouncement;
    private static final String KEY_PREFIX_USER = "users:";
    private static final String KEY_POSTFIX_USER_ADDRESS = ":address";
    private static final String KEY_PREFIX_ORDER = "orders:";
    private static final String KEY_PREFIX_GAMEDISC = "gameDiscs:";
    private static final String KEY_PREFIX_ANNOUNCEMENT = "announcements:";


    public User getCachedUser(Long userId) {
        User user = null;
        try {
            user = redisTemplateUser.opsForValue().get(KEY_PREFIX_USER + userId);
        } catch (Exception e) {
            LOGGER.error("Error accessing user in Redis cache: {}", e.getMessage());
        }
        return user;
    }


    public void storeGameDiscInCache(Long gameDiscId, GameDisc gameDisc) {
        try {
            String key = KEY_PREFIX_GAMEDISC + gameDiscId;
            redisTemplateGameDisc.opsForValue().set(key, gameDisc);
            // set expiration date of 2 hours
            redisTemplateGameDisc.expire(key, 2, TimeUnit.HOURS);
            LOGGER.info("GameDisc stored in Redis cache with key: {}", key);
        } catch (Exception e) {
            LOGGER.error("Error storing data in Redis cache: ", e);
        }
    }

    public GameDisc getCachedGameDisc(Long gameDiscId) {
        GameDisc gameDisc = null;
        try {
            gameDisc = redisTemplateGameDisc.opsForValue().get(KEY_PREFIX_GAMEDISC + gameDiscId);
        } catch (Exception e) {
            LOGGER.error("Error accessing gameDisc in Redis cache: {}", e.getMessage());
        }
        return gameDisc;
    }

    public void deleteCachedGameDisc(Long gameDiscId) {
        try {
            redisTemplateGameDisc.delete(KEY_PREFIX_GAMEDISC + gameDiscId);
        } catch (Exception e) {
            LOGGER.error("Error deleting gameDisc in Redis cache: {}", e.getMessage());
        }
    }

    public void storeAnnouncementInCache(Long announcementId, Announcement announcement) {
        try {
            String key = KEY_PREFIX_ANNOUNCEMENT + announcementId;
            redisTemplateAnnouncement.opsForValue().set(key, announcement);
            // set expiration date of 2 hours
            redisTemplateAnnouncement.expire(key, 2, TimeUnit.HOURS);
            LOGGER.info("Announcement stored in Redis cache with key: {}", key);
        } catch (Exception e) {
            LOGGER.error("Error storing data in Redis cache: ", e);
        }
    }

    public Announcement getCachedAnnouncement(Long announcementId) {
        Announcement announcement = null;
        try {
            announcement = redisTemplateAnnouncement.opsForValue().get(KEY_PREFIX_ANNOUNCEMENT + announcementId);
        } catch (Exception e) {
            LOGGER.error("Error accessing announcement in Redis cache: {}", e.getMessage());
        }
        return announcement;
    }

    public void deleteCachedAnnouncement(Long announcementId) {
        try {
            redisTemplateAnnouncement.delete(KEY_PREFIX_ANNOUNCEMENT + announcementId);
        } catch (Exception e) {
            LOGGER.error("Error deleting announcement in Redis cache: {}", e.getMessage());
        }
    }
}
