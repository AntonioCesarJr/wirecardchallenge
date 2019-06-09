package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.CardEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    @Cacheable("cardsPage")
    Page<CardEntity> findAll(Pageable pageable);

    @Cacheable("cardId")
    Optional<CardEntity> findById(Long id);

    @Cacheable("cardPublicId")
    Optional<CardEntity> findByPublicId(UUID publicId);

    @Caching(evict = {
        @CacheEvict(value="cardsPage", allEntries=true),
        @CacheEvict(value="cardId", allEntries=true),
        @CacheEvict(value="cardPublicId", allEntries=true)})
    CardEntity save(CardEntity cardEntity);

    @Caching(evict = {
        @CacheEvict(value="cardsPage", allEntries=true),
        @CacheEvict(value="cardId", allEntries=true),
        @CacheEvict(value="cardPublicId", allEntries=true)})
    void delete(CardEntity cardEntity);
}
