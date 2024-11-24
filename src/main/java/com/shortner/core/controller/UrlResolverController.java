package com.shortner.core.controller;

import com.shortner.core.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UrlResolverController {
    @Autowired
    private UrlService urlService;

    private static final Logger log = LoggerFactory.getLogger(UrlResolverController.class);
    @GetMapping("{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        log.debug("Redirecting short code: {}", shortCode);
        String originalUrl = urlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(302)
                .header("Location", originalUrl)
                .build();
    }
}
