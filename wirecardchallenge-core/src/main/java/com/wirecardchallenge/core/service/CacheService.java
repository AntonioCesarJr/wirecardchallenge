package com.wirecardchallenge.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public String cleanCache(){
        RedisConnection connection = redisConnectionFactory.getConnection();
        try {
            connection.flushAll();
            log.info("Cache cleaned! \\o/");
        } catch (Exception e) {
            log.warn("Something went wrong cleaning Redis cache! :(  -> ", e);
            return "fail!";
        } finally {
            connection.close();
        }
        return "success!";
    }
}
