package com.shortner.core.repository;


import com.shortner.core.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {

    /**
     * Find a URL by its short code
     * @param shortCode the short code to look up
     * @return Optional containing the URL if found
     */
    Optional<Url> findByShortCode(String shortCode);

    /**
     * Find a URL by its original URL
     * @param originalUrl the original URL to look up
     * @return Optional containing the URL if found
     */
    Optional<Url> findByOriginalUrl(String originalUrl);

    /**
     * Check if a short code already exists
     * @param shortCode the short code to check
     * @return true if the short code exists
     */
    boolean existsByShortCode(String shortCode);

    /**
     * Find all URLs created after a certain date
     * @param date the date after which to find URLs
     * @return List of URLs created after the given date
     */
    List<Url> findByCreatedAtAfter(Instant date);

    /**
     * Find URLs by original URL pattern
     * @param pattern the pattern to match against original URLs
     * @return List of matching URLs
     */
    @Query("SELECT u FROM Url u WHERE u.originalUrl LIKE %:pattern%")
    List<Url> findByOriginalUrlPattern(@Param("pattern") String pattern);

    /**
     * Delete URLs older than a certain date
     * @param date the date before which to delete URLs
     * @return number of deleted records
     */
    long deleteByCreatedAtBefore(Instant date);

    /**
     * Count URLs created between two dates
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return count of URLs created in the date range
     */
    @Query("SELECT COUNT(u) FROM Url u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countUrlsCreatedBetween(
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );
}
