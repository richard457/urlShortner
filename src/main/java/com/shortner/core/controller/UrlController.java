package com.shortner.core.controller;

import com.shortner.core.service.UrlService;
import com.shortner.core.model.dto.UrlRequest;
import com.shortner.core.model.dto.UrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/urls")
@RequiredArgsConstructor
public class UrlController {
    private static final Logger log = LoggerFactory.getLogger(UrlController.class);
    @Autowired
    private UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody UrlRequest request) {
        log.debug("Creating short URL for: {}", request.getOriginalUrl());
        return ResponseEntity.ok(urlService.createShortUrl(request));
    }


}