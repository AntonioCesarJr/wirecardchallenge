package com.wirecardchallenge.rest.controller.cache;

import com.wirecardchallenge.core.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/cache")
public class CacheController {

    @Autowired
    CacheService cacheService;

    @GetMapping(value = "/clean")
    public ResponseEntity<String> cleanCache(){
        return ResponseEntity.ok(cacheService.cleanCache());
    }

}
