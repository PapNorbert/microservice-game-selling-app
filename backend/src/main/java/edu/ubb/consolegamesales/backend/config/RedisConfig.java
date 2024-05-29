package edu.ubb.consolegamesales.backend.config;

import edu.ubb.consolegamesales.backend.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    private final String host;
    private final int port;

    public RedisConfig(@Value("${spring.data.redis.host}") String host,
                       @Value("${spring.data.redis.port}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.port")
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.port")
    public RedisTemplate<String, User> redisTemplateUser() {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.port")
    public RedisTemplate<String, String> redisTemplateString() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
}
