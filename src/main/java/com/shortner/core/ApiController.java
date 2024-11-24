package com.shortner.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {


    private static final String BASE_URL = "http://localhost:8080/"; // Replace with your domain

    // Injecting the URLRepository
    @Autowired
    private URLRepository urlRepository;

    @GetMapping
    public String hello() {
        return "Hello from Spring Boot REST";
    }

    @PostMapping("/url")
    public ResponseEntity<ShortenedUrlResponse> createURL(@RequestBody URLs newUrl) {
        if (newUrl.getOriginalUrl() == null || newUrl.getOriginalUrl().isEmpty()) {
            return ResponseEntity.badRequest().body(new ShortenedUrlResponse("Original URL is required", null));
        }

        // Generate a short code if it's not provided
        if (newUrl.getShortCode() == null || newUrl.getShortCode().isEmpty()) {
            newUrl.setShortCode(generateShortCode());
        }

        // Set the creation time
        newUrl.setCreatedAt(Instant.now().getEpochSecond());

        // Save the new URL entry to the database using URLRepository
        urlRepository.save(newUrl);

        // Construct the shortened URL
        String shortenedURL = BASE_URL + newUrl.getShortCode();

        // Return the shortened URL
        return ResponseEntity.status(201).body(new ShortenedUrlResponse(newUrl.getOriginalUrl(), shortenedURL));
    }

    // Endpoint to handle redirects from shortened URLs
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalURL(@PathVariable String shortCode) {
        Optional<URLs> urlEntry = urlRepository.findByShortCode(shortCode);
        // Redirect to the original URL
        return urlEntry.<ResponseEntity<Void>>map(urLs -> ResponseEntity.status(302).header("Location", urLs.getOriginalUrl()).build()).orElseGet(() -> ResponseEntity.status(404).build());
    }

    // Utility method to generate a simple short code (e.g., random alphanumeric string)
    private String generateShortCode() {
        // This is a very basic random short code generator. Customize for production.
        return java.util.UUID.randomUUID().toString().substring(0, 6);
    }

    // Class to represent the response with the shortened URL
    public static class ShortenedUrlResponse {
        private String originalUrl;
        private String shortenedUrl;

        public ShortenedUrlResponse(String originalUrl, String shortenedUrl) {
            this.originalUrl = originalUrl;
            this.shortenedUrl = shortenedUrl;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public String getShortenedUrl() {
            return shortenedUrl;
        }

        public void setShortenedUrl(String shortenedUrl) {
            this.shortenedUrl = shortenedUrl;
        }
    }
}

