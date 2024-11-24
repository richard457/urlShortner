package com.shortner.core.service;

import com.shortner.core.config.AppConfig;
import com.shortner.core.exception.UrlNotFoundException;
import com.shortner.core.model.Url;
import com.shortner.core.model.dto.UrlRequest;
import com.shortner.core.model.dto.UrlResponse;
import com.shortner.core.repository.UrlRepository;
import com.shortner.core.util.UrlShortenerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

import com.shortner.core.exception.DuplicateShortCodeException;
import com.shortner.core.exception.InvalidUrlException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Service
public class UrlServiceImpl implements UrlService {
    private static final int MAX_RETRIES = 3;
    private static final int MAX_URL_LENGTH = 2048;

    private final UrlRepository urlRepository;
    private final AppConfig appConfig;
    private final UrlShortenerUtil urlShortenerUtil;

    @Autowired
    public UrlServiceImpl(UrlRepository urlRepository, AppConfig appConfig, UrlShortenerUtil urlShortenerUtil) {
        this.urlRepository = urlRepository;
        this.appConfig = appConfig;
        this.urlShortenerUtil = urlShortenerUtil;
    }

    @Override
    @Transactional
    public UrlResponse createShortUrl(UrlRequest request) {
        String originalUrl = validateAndNormalizeUrl(request.getOriginalUrl());

        // Check if URL already exists
        Url existingUrl = urlRepository.findByOriginalUrl(originalUrl).orElse(null);

        if (existingUrl != null) {
            return buildUrlResponse(existingUrl);
        }
        // Create new URL with retry mechanism
        return createNewShortUrl(originalUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public String getOriginalUrl(String shortCode) {
        Objects.requireNonNull(shortCode, "Short code cannot be null");

        if (!StringUtils.hasText(shortCode)) {
            throw new InvalidUrlException("Short code cannot be empty");
        }

        return urlRepository.findByShortCode(shortCode)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for code: " + shortCode));
    }

    private String validateAndNormalizeUrl(String originalUrl) {
        if (!StringUtils.hasText(originalUrl)) {
            throw new InvalidUrlException("URL cannot be empty");
        }

        if (originalUrl.length() > MAX_URL_LENGTH) {
            throw new InvalidUrlException("URL exceeds maximum length of " + MAX_URL_LENGTH);
        }

        try {
            URL url = new URL(originalUrl);
            String protocol = url.getProtocol().toLowerCase();

            if (!protocol.equals("http") && !protocol.equals("https")) {
                throw new InvalidUrlException("Only HTTP and HTTPS protocols are supported");
            }

            return url.toString();
        } catch (MalformedURLException e) {
            throw new InvalidUrlException("Invalid URL format: " + e.getMessage());
        }
    }

    private UrlResponse createNewShortUrl(String originalUrl) {
        int attempts = 0;
        while (true) {
            try {
                Url url = new Url();
                url.setOriginalUrl(originalUrl);
                url.setShortCode(urlShortenerUtil.generateUniqueShortCode());
                url.setCreatedAt(Instant.now());

                Url savedUrl = urlRepository.save(url);
                return buildUrlResponse(savedUrl);

            } catch (DataIntegrityViolationException e) {
                attempts++;
                if (attempts >= MAX_RETRIES) {
                    throw new DuplicateShortCodeException("Failed to generate unique short code after " + MAX_RETRIES + " attempts");
                }
            }
        }

    }

    private UrlResponse buildUrlResponse(Url url) {
        String shortUrl = appConfig.getBaseUrl() + url.getShortCode();
        return new UrlResponse(url.getOriginalUrl(), shortUrl);
    }
}
