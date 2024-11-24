package com.shortner.core.service;

import com.shortner.core.model.dto.UrlRequest;
import com.shortner.core.model.dto.UrlResponse;

public interface UrlService {
    UrlResponse createShortUrl(UrlRequest request);
    String getOriginalUrl(String shortCode);
}