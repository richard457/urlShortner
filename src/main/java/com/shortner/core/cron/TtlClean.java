package com.shortner.core.cron;

import com.shortner.core.model.Url;
import com.shortner.core.repository.UrlRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class TtlClean {

    private final UrlRepository urlRepository;

    public TtlClean(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // Cron expression: Runs every minute (0 * * * * ?)
    //@Scheduled(cron = "0 * * * * ?")
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void deleteExpiredUrls() {

        Instant now = Instant.now();
        System.out.println("Querying with current Instant: " + now.toEpochMilli());
        // Fetch URLs with expired TTL
        List<Url> expiredUrls = urlRepository.findByTtlBefore(now.toEpochMilli());
        if (!expiredUrls.isEmpty()) {
            System.out.println("About to delete");
            urlRepository.deleteAll(expiredUrls);
            //log.info("Deleted {} expired URLs", expiredUrls.size());
        }
    }
}

