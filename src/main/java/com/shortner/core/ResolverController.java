package com.shortner.core;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ResolverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResolverController.class);

    // Injecting the URLRepository
    @Autowired
    private URLRepository urlRepository;

    // GET method to resolve a shortened URL to the original URL
    @GetMapping("/{shortCode}")
    @Transactional
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        // Find the URL entry by shortCode
        Optional<URLs> urlEntry = urlRepository.findByShortCode(shortCode);

        if (urlEntry.isEmpty()) {
            // If the short code does not exist, return a 404 Not Found
            return ResponseEntity.status(404).body(null);
        }

        // If the original URL is found, redirect to it
        URI originalUri = URI.create(urlEntry.get().getOriginalUrl());
        return ResponseEntity.status(302).location(originalUri).build(); // 302 redirect to the original URL
    }
}