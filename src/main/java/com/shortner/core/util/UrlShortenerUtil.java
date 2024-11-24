package com.shortner.core.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class UrlShortenerUtil {
    private static final String ALPHABET = "23456789BCDFGHJKLMNPQRSTVWXYZbcdfghjklmnpqrstvwxyz";
    private static final int CODE_LENGTH = 6;
    private final SecureRandom random = new SecureRandom();

    public String generateUniqueShortCode() {
        StringBuilder shortCode = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            shortCode.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return shortCode.toString();
    }
}
