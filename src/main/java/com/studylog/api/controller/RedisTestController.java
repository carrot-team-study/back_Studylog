package com.studylog.api.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    private final StringRedisTemplate redisTemplate;

    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/api/redis/ping")
    public String ping() {
        redisTemplate.opsForValue().set("test", "ok");
        return redisTemplate.opsForValue().get("test");
    }
}
