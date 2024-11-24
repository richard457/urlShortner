package com.shortner.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// URL Repository to perform CRUD operations
public interface URLRepository extends JpaRepository<URLs, Long> {
    Optional<URLs> findByShortCode(String shortCode);
}
