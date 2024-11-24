package com.shortner.core.model.dto;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.Instant;

@Data
public class UrlRequest {
    @NotBlank(message = "Original URL is required")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    private String shortCode;

    private Instant ttl;

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Instant getTtl() {
        return ttl;
    }

    public void setTtl(Instant ttl) {
        this.ttl = ttl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public @NotBlank(message = "Original URL is required") @URL(message = "Invalid URL format") String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(@NotBlank(message = "Original URL is required") @URL(message = "Invalid URL format") String originalUrl) {
        this.originalUrl = originalUrl;
    }
}