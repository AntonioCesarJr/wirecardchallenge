package com.wirecardchallenge.rest;

import com.wirecardchallenge.core.service.CacheService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties
@EntityScan(basePackages = {"com.wirecardchallenge.core.entity"})
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = {"com.wirecardchallenge.core.repository"})
@ComponentScan(basePackages = {"com.wirecardchallenge.core",
        "com.wirecardchallenge.rest.config",
        "com.wirecardchallenge.rest.controller"})
@EnableCaching
public class RestStarter {

    public static void main(String[] args) {

        ConfigurableApplicationContext context =SpringApplication.run(RestStarter.class, args);
        CacheService cacheService = context.getBean(CacheService.class);
        cacheService.cleanCache();
    }

}
