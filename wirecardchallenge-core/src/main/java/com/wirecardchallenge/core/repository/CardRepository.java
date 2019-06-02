package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Card;
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
public interface CardRepository extends JpaRepository<Card, Long> {

    @Cacheable("cardsPage")
    Page<Card> findAll(Pageable pageable);

    @Cacheable("cardId")
    Optional<Card> findById(Long id);

    @Cacheable("cardPublicId")
    Optional<Card> findByPublicId(UUID publicId);

    @Caching(evict = {
        @CacheEvict(value="cardsPage", allEntries=true),
        @CacheEvict(value="cardId", allEntries=true),
        @CacheEvict(value="cardPublicId", allEntries=true)})
    Card save(Card card);

    @Caching(evict = {
        @CacheEvict(value="cardsPage", allEntries=true),
        @CacheEvict(value="cardId", allEntries=true),
        @CacheEvict(value="cardPublicId", allEntries=true)})
    void delete(Card card);
}
