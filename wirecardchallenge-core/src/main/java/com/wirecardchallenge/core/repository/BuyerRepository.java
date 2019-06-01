package com.wirecardchallenge.core.repository;

import com.wirecardchallenge.core.entity.Buyer;
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
public interface BuyerRepository extends JpaRepository<Buyer, Long>{

    @Cacheable("buyersPage")
    Page<Buyer> findAll(Pageable pageable);
    @Cacheable("buyerId")
    Optional<Buyer> findById(Long id);
    @Cacheable("buyerPublicId")
    Optional<Buyer> findByPublicId(UUID publicId);
    @Caching(evict = {
            @CacheEvict(value="buyersPage", allEntries=true),
            @CacheEvict(value="buyerId", allEntries=true),
            @CacheEvict(value="buyerPublicId", allEntries=true)
    })
    Buyer save(Buyer buyer);
    @Caching(evict = {
            @CacheEvict(value="buyersPage", allEntries=true),
            @CacheEvict(value="buyerId", allEntries=true),
            @CacheEvict(value="buyerPublicId", allEntries=true)
    })
    void delete(Buyer buyer);
}
