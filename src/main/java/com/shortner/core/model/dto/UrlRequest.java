package com.shortner.core.model.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequest {
    @NotBlank(message = "Original URL is required")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    public @NotBlank(message = "Original URL is required") @URL(message = "Invalid URL format") String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(@NotBlank(message = "Original URL is required") @URL(message = "Invalid URL format") String originalUrl) {
        this.originalUrl = originalUrl;
    }
}